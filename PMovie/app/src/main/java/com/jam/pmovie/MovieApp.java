package com.jam.pmovie;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jam.pmovie.data.PrefHelper;

/**
 * Created by jam on 17/12/26.
 */

public class MovieApp extends Application {

    private static MovieApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Stetho.initializeWithDefaults(mContext);
        PrefHelper.init(mContext);
    }

    public static MovieApp getContext() {
        return mContext;
    }
}
