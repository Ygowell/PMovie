package com.jam.pmovie.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jam.pmovie.BaseActivity;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieListBean;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.http.HttpUtils;

import butterknife.BindView;

public class MovieDetailActivity extends BaseActivity {

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

    private MovieListBean.MovieInfo mMovieInfo;

    @Override
    public void onProxyCreate(@Nullable Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);

        mMovieInfo = (MovieListBean.MovieInfo) getIntent().getSerializableExtra(Constant.ExtraName.MOVIE_DATA);
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
    }

    /**
     * 展示电影详情
     */
    private void showMovieDetail() {
        mMovieNameTv.setText(mMovieInfo.getTitle());
        mMovieRateTv.setText(getResources().getString(R.string.rated,
                String.valueOf(mMovieInfo.getVote_average())));
        mMovieReleaseDateTv.setText(getResources().getString(R.string.release_date,
                mMovieInfo.getRelease_date()));
        mMovieOverviewTv.setText(getResources().getString(R.string.overview,
                mMovieInfo.getOverview()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(R.drawable.default_movie_icon);
        Glide.with(this)
                .load(HttpUtils.MOVIE_PIC_URL + mMovieInfo.getPoster_path())
                .apply(requestOptions)
                .into(mMovieCoverIv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_movie_detail;
    }
}
