package com.jam.pmovie;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by jam on 17/12/26.
 */

public class MovieApp extends Application {

    private static MovieApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }

    public static MovieApp getContext() {
        return mContext;
    }
}
