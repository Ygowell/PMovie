package com.jam.pmovie.http;

import com.jam.pmovie.bean.MovieListBean;

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
}
