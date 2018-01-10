package com.jam.pmovie.http;

import com.jam.pmovie.bean.CommentInfo;
import com.jam.pmovie.bean.MovieDetailInfo;
import com.jam.pmovie.bean.MovieListBean;
import com.jam.pmovie.bean.NoticeInfo;

import rx.Observable;

/**
 * Created by jam on 17/12/30.
 */

public class AppApi {

    public static Observable<MovieListBean> getMovieList(String reqStuffix) {
        OkHttpService<MovieListBean> service = new OkHttpService<>(MovieListBean.class);
        String url = UrlUtils.COMMON_URL + reqStuffix;
        return service.execute(url);
    }

    public static Observable<MovieDetailInfo> getMovieDetail(long movieId) {
        OkHttpService<MovieDetailInfo> service = new OkHttpService<>(MovieDetailInfo.class);
        String url = UrlUtils.addUrlPath(UrlUtils.COMMON_URL, movieId + "");
        return service.execute(url);
    }

    public static Observable<CommentInfo> getCommentInfo(long movieId) {
        OkHttpService<CommentInfo> service = new OkHttpService<>(CommentInfo.class);
        String url = UrlUtils.addUrlPath(UrlUtils.COMMON_URL, movieId + "");
        url = UrlUtils.addUrlPath(url, UrlUtils.SUFFIX_REVIEW);;
        return service.execute(url);
    }

    public static Observable<NoticeInfo> getNoticeInfo(long movieId) {
        OkHttpService<NoticeInfo> service = new OkHttpService<>(NoticeInfo.class);
        String url = UrlUtils.addUrlPath(UrlUtils.COMMON_URL, movieId + "");
        url = UrlUtils.addUrlPath(url, UrlUtils.SUFFIX_NOTICE);
        return service.execute(url);
    }
}
