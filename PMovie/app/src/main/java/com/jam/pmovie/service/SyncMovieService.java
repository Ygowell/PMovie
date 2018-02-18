package com.jam.pmovie.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jam on 18/1/31.
 */

public class SyncMovieService extends Service {

    private static final String TAG = "SyncMovieService";
    private static final Object sSyncAdapterLock = new Object();
    private static SyncMovieAdapter sSyncMovieAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        synchronized (sSyncAdapterLock) {
            if (sSyncMovieAdapter == null) {
                sSyncMovieAdapter = new SyncMovieAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncMovieAdapter.getSyncAdapterBinder();
    }
}
