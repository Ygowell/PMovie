package com.jam.pmovie.ui.detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jam.pmovie.BaseFragment;
import com.jam.pmovie.R;
import com.jam.pmovie.bean.CommentInfo;
import com.jam.pmovie.bean.MovieDetailInfo;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.bean.NoticeInfo;
import com.jam.pmovie.common.ComUtils;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.http.AppApi;
import com.jam.pmovie.http.UrlUtils;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jam on 18/1/21.
 */

public class MovieDetailFragment extends BaseFragment {

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
    @BindView(R.id.vs_notice)
    ViewStub mNoticeVs;
    @BindView(R.id.vs_comment)
    ViewStub mCommentVs;

    private LinearLayout mNoticeLayout;
    private LinearLayout mCommentLayout;
    private Context mContext;
    private MovieInfo mMovieInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    protected void onProxyCreateView() {
        mContext = getContext();

        if (getArguments() == null) {
            return;
        }

        mMovieInfo = getArguments().getParcelable(Constant.ExtraName.MOVIE_DATA);
        if (mMovieInfo == null) {
            return;
        }

        showMovieDetail(mMovieInfo);
    }

    /**
     * 展示电影详情
     */
    public void showMovieDetail(MovieInfo movieInfo) {
        mMovieInfo = movieInfo;
        mMovieNameTv.setText(mMovieInfo.getTitle());
        mMovieRateTv.setText(getString(R.string.rated, String.valueOf(mMovieInfo.getVoteAverage())));
        mMovieReleaseDateTv.setText(getString(R.string.release_date, mMovieInfo.getReleaseDate()));
        mMovieOverviewTv.setText(mMovieInfo.getOverview());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().placeholder(R.drawable.default_movie_icon)
                .error(R.drawable.default_movie_icon);
        Glide.with(this)
                .load(UrlUtils.getPicUrl(mMovieInfo.getPosterPath()))
                .apply(requestOptions)
                .into(mMovieCoverIv);

        reqMovieRuntime();
        reqMovieComments();
        reqMovieNotices();
    }

    private void reqMovieRuntime() {
        AppApi.getMovieDetail(mMovieInfo.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieDetailInfo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mMovieRuntimeTv.setVisibility(View.GONE);
                    }

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
                    public void onStart() {
                        super.onStart();
                        if (mNoticeLayout != null) {
                            mNoticeLayout.setVisibility(View.GONE);
                        }
                    }

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
                        if (ComUtils.isEmpty(noticeInfo.getResults())) {
                            return;
                        }

                        createNoticeView(noticeInfo.getResults());
                    }
                });
    }

    private void createNoticeView(List<NoticeInfo.ResultsEntity> noticeInfoList) {
        if (mNoticeLayout == null) {
            View noticeView = mNoticeVs.inflate();
            mNoticeLayout = (LinearLayout) noticeView.findViewById(R.id.ll_movie_notice);
        } else {
            mNoticeLayout.removeAllViews();
        }
        mNoticeLayout.setVisibility(View.VISIBLE);

        for (NoticeInfo.ResultsEntity noticeInfo : noticeInfoList) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_notice,
                    mNoticeLayout, false);
            mNoticeLayout.addView(itemView);
            final String youtubeKey = noticeInfo.getKey();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + youtubeKey)));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, R.string.not_found_browser, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void reqMovieComments() {
        AppApi.getCommentInfo(mMovieInfo.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentInfo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (mCommentLayout != null) {
                            mCommentLayout.setVisibility(View.GONE);
                        }
                    }

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
                        if (ComUtils.isEmpty(commentInfo.getResults())) {
                            return;
                        }

                        createCommentView(commentInfo.getResults());
                    }
                });
    }

    private void createCommentView(List<CommentInfo.Comment> commentList) {
        if (mCommentLayout == null) {
            View commentView = mCommentVs.inflate();
            mCommentLayout = (LinearLayout) commentView.findViewById(R.id.ll_movie_comment);
        } else {
            mCommentLayout.removeAllViews();
        }
        mCommentLayout.setVisibility(View.VISIBLE);
        for (CommentInfo.Comment comment : commentList) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_comment,
                    mCommentLayout, false);
            TextView contentTv = (TextView) itemView.findViewById(R.id.tv_comment_content);
            TextView authorTv = (TextView) itemView.findViewById(R.id.tv_comment_author);

            contentTv.setText(comment.getContent());
            authorTv.setText(getString(R.string.author, comment.getAuthor()));
            mCommentLayout.addView(itemView);
        }
    }
}
