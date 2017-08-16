package com.jam.pmovie.http;

import android.net.Uri;
import com.jam.pmovie.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jam on 17/8/15.
 */

public class HttpUtils {

    private static final String COMMON_URL = "http://api.themoviedb.org/3/";
    private static final String URL_MOVIE_POPULAR= "movie/popular";
    private static final String PARAM_API_KEY = "api_key";

    public static String getMovieList() {
        Uri uri = Uri.parse(COMMON_URL + URL_MOVIE_POPULAR)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.OPEN_MOVIE_API_KEY)
                .build();

        return getResponeFromHttpUrl(uri.toString());
    }

    private static String getResponeFromHttpUrl(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();

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
