package com.jam.pmovie.http;

import com.jam.pmovie.bean.MovieListBean;

import rx.Observable;

/**
 * Created by jam on 17/12/30.
 */

public class AppApi {

    public static Observable<MovieListBean> getMovieList(boolean isPopular) {
        OkHttpService<MovieListBean> service = new OkHttpService<>(MovieListBean.class);
        String stuffix = isPopular ? "/popular" : "/top_rated";
        String url = UrlUtils.COMMON_URL + stuffix;
        return service.execute(url);
    }
}
