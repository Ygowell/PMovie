package com.jam.pmovie.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.bean.MovieListBean;
import com.jam.pmovie.common.ComUtils;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.data.MovieCpHelper;
import com.jam.pmovie.data.PrefHelper;
import com.jam.pmovie.http.AppApi;
import com.jam.pmovie.ui.detail.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MovieListActivity extends BaseActivity implements MovieListAdapter.OnMovieItemClickListener {

    private static final String TAG = MovieListActivity.class.getSimpleName();
    @BindView(R.id.rv_movie_list)
    RecyclerView mMovieListRv;
    @BindView(R.id.pb_movie_list)
    ProgressBar mLoadingPb;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.tv_none_data)
    TextView mNoneDataTv;

    private ActionBar mActionBar;

    private MovieListAdapter mMovieListAdapter;
    private List<MovieInfo> mMovieInfoList;
    private int mSortType = Constant.SORT_TYPE_POPULAR;
    private boolean mOnlyCollected = false;
    private boolean mClickRefresh = false;

    @Override
    public void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);
        mMovieListAdapter = new MovieListAdapter(this);
        mMovieListAdapter.setOnItemClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMovieListRv.setLayoutManager(gridLayoutManager);
        mMovieListRv.setAdapter(mMovieListAdapter);

        boolean isFirstLoadPopuplarData = PrefHelper.getFirstLoadPopularData();
        if (isFirstLoadPopuplarData) { // Loading data automatically at the first time
            requestMovieList();
        } else {
            getMovieListFromDb();
        }

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.movie_list);
            mActionBar.setSubtitle(R.string.movie_list_popular);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.tv_error)
    public void onRetry() {
        requestMovieList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.refresh:
                mClickRefresh = true;
                requestMovieList();
                break;
            case R.id.collect:
                mOnlyCollected = !mOnlyCollected;
                item.setIcon(mOnlyCollected ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
                getMovieListFromDb();
                break;
            case R.id.popular:
                mSortType = Constant.SORT_TYPE_POPULAR;
                mActionBar.setSubtitle(R.string.movie_list_popular);
                getMovieData();
                break;
            case R.id.top_rated:
                mSortType = Constant.SORT_TYPE_SCORE;
                mActionBar.setSubtitle(R.string.movie_list_top_rated);
                getMovieData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getMovieData() {
        if (getFirstLoad()) {
            requestMovieList();
        } else {
            getMovieListFromDb();
        }
    }

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

    private void saveFirstLoadSp() {
        if (mSortType == Constant.SORT_TYPE_POPULAR) {
            PrefHelper.saveFirstLoadPopularData(false);
        } else if (mSortType == Constant.SORT_TYPE_SCORE) {
            PrefHelper.saveFirstLoadScoreData(false);
        }
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

    private void showMovieList(List<MovieInfo> movieInfoList) {
        mMovieListAdapter.setData(movieInfoList);
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
        intent.putExtra(Constant.ExtraName.MOVIE_DATA, mMovieInfoList.get(pos));
        startActivity(intent);
    }
}
