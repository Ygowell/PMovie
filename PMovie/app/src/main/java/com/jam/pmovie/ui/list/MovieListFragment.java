package com.jam.pmovie.ui.list;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jam.pmovie.BaseFragment;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.bean.MovieListBean;
import com.jam.pmovie.common.ComUtils;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.data.MovieCpHelper;
import com.jam.pmovie.data.PrefHelper;
import com.jam.pmovie.http.AppApi;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jam on 18/1/21.
 */

public class MovieListFragment extends BaseFragment implements MovieListAdapter.OnMovieItemClickListener {

    @BindView(R.id.rv_movie_list)
    RecyclerView mMovieListRv;
    @BindView(R.id.pb_movie_list)
    ProgressBar mLoadingPb;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.tv_none_data)
    TextView mNoneDataTv;

    private MovieListAdapter mMovieListAdapter;
    private List<MovieInfo> mMovieInfoList;
    private Context mContext;
    private OnMovieClickListener mListener;

    private int mSortType = Constant.SORT_TYPE_POPULAR;
    private boolean mOnlyCollected = false;
    private boolean mClickRefresh = false;

    public interface OnMovieClickListener {
        void onMovieClicked(MovieInfo movieInfo);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movie_list;
    }

    @Override
    protected void onProxyCreateView() {
        mContext = getContext();
        mMovieListAdapter = new MovieListAdapter(mContext);
        mMovieListAdapter.setOnItemClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mMovieListRv.setLayoutManager(gridLayoutManager);
        mMovieListRv.setAdapter(mMovieListAdapter);

        boolean isFirstLoadPopuplarData = PrefHelper.getFirstLoadPopularData();
        if (isFirstLoadPopuplarData) { // Loading data automatically at the first time
            requestMovieList();
        } else {
            getMovieListFromDb();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnMovieClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMovieClickListener");
        }

    }

    @OnClick(R.id.tv_error)
    public void onRetry() {
        requestMovieList();
    }

    @Override
    public void onItemClick(int pos) {
        mListener.onMovieClicked(mMovieInfoList.get(pos));
    }

    public void changeSortType(int sortType) {
        mSortType = sortType;
        getMovieData();
    }

    public void changeCollectState(boolean isSelectCollect) {
        mOnlyCollected = isSelectCollect;
        getMovieListFromDb();
    }

    public void refreshList() {
        mClickRefresh = true;
        requestMovieList();
    }

    private void getMovieData() {
        if (getFirstLoad()) {
            requestMovieList();
        } else {
            getMovieListFromDb();
        }
    }

    /**
     * 从服务器请求电影列表数据
     */
    private void requestMovieList() {
        String stuffix;
        if (mSortType == Constant.SORT_TYPE_POPULAR) {
            stuffix = "/popular";
        } else if (mSortType == Constant.SORT_TYPE_SCORE) {
            stuffix = "/top_rated";
        } else {
            throw new UnsupportedOperationException("Unknow sort type: " + mSortType);
        }

        AppApi.getMovieList(stuffix)
                .doOnNext(new Action1<MovieListBean>() {
                    @Override
                    public void call(MovieListBean movieListBean) {
                        if (mClickRefresh || getFirstLoad()) { // 更新数据
                            List<MovieInfo> movieInfoList = movieListBean.getResults();
                            if (ComUtils.isEmpty(movieInfoList)) {
                                return;
                            }

                            for (MovieInfo movieInfo : movieInfoList) {
                                movieInfo.setSortType(mSortType);
                            }

                            MovieCpHelper.getInstance().saveMovieList(movieInfoList);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieListBean>() {
                    @Override
                    public void onStart() {
                        mLoadingPb.setVisibility(View.VISIBLE);
                        mErrorTv.setVisibility(View.GONE);
                        mNoneDataTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCompleted() {
                        mLoadingPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingPb.setVisibility(View.GONE);
                        mErrorTv.setVisibility(View.VISIBLE);
                        mErrorTv.setText(R.string.error_server);
                    }

                    @Override
                    public void onNext(MovieListBean bean) {
                        mMovieInfoList = bean.getResults();
                        showMovieList(mMovieInfoList);
                        saveFirstLoadSp();

                        if (ComUtils.isEmpty(mMovieInfoList)) {
                            mNoneDataTv.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 从本地数据库获取电影列表数据
     */
    private void getMovieListFromDb() {
        MovieCpHelper.getInstance().getMovieList(mSortType, mOnlyCollected)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MovieInfo>>() {

                    @Override
                    public void onStart() {
                        mLoadingPb.setVisibility(View.VISIBLE);
                        mErrorTv.setVisibility(View.GONE);
                        mNoneDataTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCompleted() {
                        mLoadingPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<MovieInfo> movieInfoList) {
                        mMovieInfoList = movieInfoList;
                        showMovieList(mMovieInfoList);

                        if (ComUtils.isEmpty(movieInfoList)) {
                            mNoneDataTv.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private boolean getFirstLoad() {
        if (mSortType == Constant.SORT_TYPE_POPULAR) {
            return PrefHelper.getFirstLoadPopularData();
        } else if (mSortType == Constant.SORT_TYPE_SCORE) {
            return PrefHelper.getFirstLoadScoreData();
        } else {
            throw new UnsupportedOperationException("Unknow sort type: " + mSortType);
        }
    }


    private void saveFirstLoadSp() {
        if (mSortType == Constant.SORT_TYPE_POPULAR) {
            PrefHelper.saveFirstLoadPopularData(false);
        } else if (mSortType == Constant.SORT_TYPE_SCORE) {
            PrefHelper.saveFirstLoadScoreData(false);
        }
    }

    private void showMovieList(List<MovieInfo> movieInfoList) {
        mMovieListAdapter.setData(movieInfoList);
    }
}
