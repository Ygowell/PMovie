package com.jam.pmovie.http;

import android.net.Uri;

import com.google.gson.Gson;
import com.jam.pmovie.BuildConfig;
import com.jam.pmovie.bean.MovieListBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jam on 17/8/15.
 */

public class HttpUtils {

    private static final String COMMON_URL = "http://api.themoviedb.org/3/";
    public static final String MOVIE_PIC_URL = "https://image.tmdb.org/t/p/w185";
    private static final String URL_MOVIE_POPULAR= "movie/popular";
    private static final String URL_MOVIE_TOP_RATED= "movie/top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT= 10000;

    public static Observable<MovieListBean> getMovieList(boolean isPopular) {
        return Observable.just(isPopular).flatMap(new Func1<Boolean, Observable<MovieListBean>>() {
            @Override
            public Observable<MovieListBean> call(Boolean aBoolean) {
                String prefix = aBoolean ? URL_MOVIE_POPULAR : URL_MOVIE_TOP_RATED;
                Uri uri = Uri.parse(COMMON_URL + prefix)
                        .buildUpon()
                        .appendQueryParameter(PARAM_API_KEY, BuildConfig.OPEN_MOVIE_API_KEY)
                        .build();

                String result = getResponeFromHttpUrl(uri.toString());
                Gson gson = new Gson();
                MovieListBean movieListBean = gson.fromJson(result, MovieListBean.class);
                return Observable.just(movieListBean);
            }
        }).subscribeOn(Schedulers.io());
    }

    private static String getResponeFromHttpUrl(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(DEFAULT_TIMEOUT);
            httpURLConnection.setReadTimeout(DEFAULT_READ_TIMEOUT);

            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return null;
    }
}
