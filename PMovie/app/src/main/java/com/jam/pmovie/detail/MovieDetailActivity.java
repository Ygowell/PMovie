package com.jam.pmovie.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.CommentInfo;
import com.jam.pmovie.bean.MovieDetailInfo;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.bean.NoticeInfo;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.data.MovieCpHelper;
import com.jam.pmovie.http.AppApi;
import com.jam.pmovie.http.UrlUtils;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MovieDetailActivity extends BaseActivity {

    private static final String TAG = "MovieDetailActivity";
    @BindView(R.id.tv_detail_movie_name)
    TextView mMovieNameTv;
    @BindView(R.id.tv_detail_movie_time)
    TextView mMovieReleaseDateTv;
    @BindView(R.id.tv_detail_movie_rate)
    TextView mMovieRateTv;
    @BindView(R.id.tv_detail_movie_overview)
    TextView mMovieOverviewTv;
    @BindView(R.id.iv_detail_movie_cover)
    ImageView mMovieCoverIv;
    @BindView(R.id.tv_detail_movie_runtime)
    TextView mMovieRuntimeTv;

    private MovieInfo mMovieInfo;
    private boolean mIsCollect;

    @Override
    public void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);

        mMovieInfo = getIntent().getParcelableExtra(Constant.ExtraName.MOVIE_DATA);
        if (mMovieInfo == null) {
            finish();
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.movie_detail);
        }
        showMovieDetail();

        mIsCollect = mMovieInfo.isCollected();

        reqMovieRuntime();
        reqMovieComments();
        reqMovieNotices();
    }

    /**
     * 展示电影详情
     */
    private void showMovieDetail() {
        mMovieNameTv.setText(mMovieInfo.getTitle());
        mMovieRateTv.setText(getResources().getString(R.string.rated,
                String.valueOf(mMovieInfo.getVoteAverage())));
        mMovieReleaseDateTv.setText(getResources().getString(R.string.release_date,
                mMovieInfo.getReleaseDate()));
        mMovieOverviewTv.setText(getResources().getString(R.string.overview,
                mMovieInfo.getOverview()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(R.drawable.default_movie_icon)
                .error(R.drawable.default_movie_icon);
        Glide.with(this)
                .load(UrlUtils.getPicUrl(mMovieInfo.getPosterPath()))
                .apply(requestOptions)
                .into(mMovieCoverIv);
    }

    private void reqMovieRuntime() {
        AppApi.getMovieDetail(mMovieInfo.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMovieRuntimeTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(MovieDetailInfo movieDetailInfo) {
                        Log.d(TAG, "加载电影时长成功！");
                        int runtime = movieDetailInfo.getRuntime();
                        mMovieRuntimeTv.setVisibility(View.VISIBLE);
                        mMovieRuntimeTv.setText(getResources()
                                .getString(R.string.movie_runtime, runtime + ""));
                    }
                });
    }

    private void reqMovieNotices() {
        AppApi.getNoticeInfo(mMovieInfo.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoticeInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "加载预告片列表失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(NoticeInfo noticeInfo) {
                        Log.d(TAG, "加载预告片列表成功！");
                    }
                });
    }

    private void reqMovieComments() {
        AppApi.getCommentInfo(mMovieInfo.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "加载评论列表失败:" + e.getMessage());
                    }

                    @Override
                    public void onNext(CommentInfo commentInfo) {
                        Log.d(TAG, "加载评论列表成功！");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        menu.findItem(R.id.collect_detail).setIcon(mIsCollect
                ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        } else if (itemId == R.id.collect_detail) {
            mIsCollect = !mIsCollect;
            MovieCpHelper.updateMovieCollectState(mMovieInfo.getId(),
                    mIsCollect ? 1 : 0).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) { // Fail to update
                            mIsCollect = !mIsCollect;
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                item.setIcon(mIsCollect ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
                            } else { // Fail to update
                                mIsCollect = !mIsCollect;
                            }
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }
}
