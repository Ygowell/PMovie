package com.jam.pmovie.http;

/**
 * Created by jam on 17/12/30.
 */

public class UrlUtils {
    public static final String COMMON_URL = "http://api.themoviedb.org/3/movie";
    public static final String MOVIE_PIC_URL = "https://image.tmdb.org/t/p/w185";
    public static final String SUFFIX_NOTICE = "videos";
    public static final String SUFFIX_REVIEW = "reviews";

    public static String getPicUrl(String path) {
        return MOVIE_PIC_URL + path;
    }

    public static String addUrlPath(String url, String path) {
        return url + "/" + path;
    }
}
