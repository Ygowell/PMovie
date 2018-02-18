package com.jam.pmovie.ui.list;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncStatusObserver;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jam.pmovie.BaseFragment;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.common.ComUtils;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.common.SyncUtils;
import com.jam.pmovie.data.MovieContract;
import com.jam.pmovie.data.MovieCpHelper;
import com.jam.pmovie.data.PrefHelper;
import com.jam.pmovie.service.MovieAccountService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jam on 18/1/21.
 */

public class MovieListFragment extends BaseFragment implements MovieListAdapter.OnMovieItemClickListener {

    private final static String TAG = "MovieListFragment";
    @BindView(R.id.rv_movie_list)
    RecyclerView mMovieListRv;
    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingLayout;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.tv_none_data)
    TextView mNoneDataTv;

    private MovieListAdapter mMovieListAdapter;
    private List<MovieInfo> mMovieInfoList;
    private Context mContext;
    private OnMovieClickListener mListener;
    private DataChangedReceiver mDataChangedReceiver;

    private int mSortType;
    private boolean mOnlyCollected = false;
    private Object mSyncObserverHandle;

    public interface OnMovieClickListener {
        void onMovieClicked(MovieInfo movieInfo);

        void onReturnFirstMovie(MovieInfo movieInfo);
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

        mSortType = PrefHelper.getReqSortType();

        if (!getFirstLoad()) { // 不是第一次加载数据，从数据库取数据
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

        SyncUtils.createSyncAccount(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);

        mDataChangedReceiver = new DataChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BC_ACTION_DATA_CHANGED);
        mContext.registerReceiver(mDataChangedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }

        mContext.unregisterReceiver(mDataChangedReceiver);
    }

    @OnClick(R.id.tv_error)
    public void onRetry() {
        refreshList();
    }

    @Override
    public void onItemClick(int pos) {
        mListener.onMovieClicked(mMovieInfoList.get(pos));
    }

    public void changeSortType(int sortType) {
        mSortType = sortType;
        PrefHelper.saveReqSortType(sortType);
        getMovieData();
    }

    public void changeCollectState(boolean isSelectCollect) {
        mOnlyCollected = isSelectCollect;
        getMovieListFromDb();
    }

    public void refreshList() {
        SyncUtils.triggerRefresh();
    }

    private void getMovieData() {
        if (getFirstLoad()) {
            refreshList();
        } else {
            getMovieListFromDb();
        }
    }

    /**
     * 从本地数据库获取电影列表数据
     */
    public void getMovieListFromDb() {
        MovieCpHelper.getInstance().getMovieList(mSortType, mOnlyCollected)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MovieInfo>>() {

                    @Override
                    public void onStart() {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mErrorTv.setVisibility(View.GONE);
                        mNoneDataTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCompleted() {
                        mLoadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<MovieInfo> movieInfoList) {
                        mMovieInfoList = movieInfoList;

                        if (ComUtils.isEmpty(movieInfoList)) {
                            mNoneDataTv.setVisibility(View.VISIBLE);
                        } else {
                            showMovieList(mMovieInfoList);
                            mListener.onReturnFirstMovie(mMovieInfoList.get(0));
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

    private void showMovieList(List<MovieInfo> movieInfoList) {
        mMovieListAdapter.setData(movieInfoList);
    }

    private void setRefreshLayoutState(boolean refreshing) {
        if (refreshing) {
            mLoadingLayout.setVisibility(View.VISIBLE);
            mErrorTv.setVisibility(View.GONE);
            mNoneDataTv.setVisibility(View.GONE);
        }
    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            Log.d(TAG, "onStatusChanged ：" + which);
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = MovieAccountService.getAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshLayoutState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(account, MovieContract.AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(account, MovieContract.AUTHORITY);
                    setRefreshLayoutState(syncActive || syncPending);
                }
            });
        }
    };

    private class DataChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(Constant.ExtraName.DATA_LOAD_STATE, -1);

            switch (state) {
                case Constant.STATE_SUCCESS:
                    getMovieListFromDb();
                    break;
                case Constant.STATE_ERROR:
                    mErrorTv.setVisibility(View.VISIBLE);
                    break;
                case Constant.STATE_NO_DATA:
                    mNoneDataTv.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }
}
