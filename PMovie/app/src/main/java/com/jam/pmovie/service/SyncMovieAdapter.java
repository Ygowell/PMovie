package com.jam.pmovie.service;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jam.pmovie.R;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.bean.MovieListBean;
import com.jam.pmovie.common.ComUtils;
import com.jam.pmovie.common.Constant;
import com.jam.pmovie.data.MovieContract;
import com.jam.pmovie.data.MovieCpHelper;
import com.jam.pmovie.data.PrefHelper;
import com.jam.pmovie.data.PrefKey;
import com.jam.pmovie.http.AppApi;
import com.jam.pmovie.ui.list.MovieListActivity;

import java.util.List;

import rx.Subscriber;

/**
 * Created by jam on 18/2/2.
 */

public class SyncMovieAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncMovieAdapter";

    private static final String[] PROJECTION = {MovieContract.MovieEntity.CL_MOVIE_ID};

    public SyncMovieAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncMovieAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        final int sortType = PrefHelper.getReqSortType();
        String stuffix = "/popular";
        if (sortType == Constant.SORT_TYPE_SCORE) {
            stuffix = "/top_rated";
        }

        AppApi.getMovieList(stuffix)
                .subscribe(new Subscriber<MovieListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendErrorBroadcast();
                    }

                    @Override
                    public void onNext(MovieListBean movieListBean) {
                        Log.d(TAG, "onNext: success!");
                        if (sortType == Constant.SORT_TYPE_POPULAR) {
                            PrefHelper.saveFirstLoadPopularData(false);
                        } else if (sortType == Constant.SORT_TYPE_SCORE) {
                            PrefHelper.saveFirstLoadScoreData(false);
                        }
                        if (!ComUtils.isEmpty(movieListBean.getResults())) {
                            updateLocalMovieData(movieListBean.getResults(), sortType);
                        } else {
                            sendNoDataBroadcast();
                        }
                    }
                });
    }

    private void updateLocalMovieData(final List<MovieInfo> movieInfos, final int sortType) {
        MovieCpHelper.getInstance().getMovieList(sortType, false)
                .subscribe(new Subscriber<List<MovieInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendErrorBroadcast();
                    }

                    @Override
                    public void onNext(List<MovieInfo> movieInfoList) {
                        for (MovieInfo movieInfo : movieInfos) {
                            movieInfo.setSortType(sortType);
                        }
                        MovieCpHelper.getInstance().saveMovieList(movieInfos);
                        if (ComUtils.isEmpty(movieInfoList)) {
                            sendComBroadcast(Constant.STATE_SUCCESS);
                        } else {
                            if (PrefHelper.getBoolean(PrefKey.NOTIFICATION_OPEN, true)) {
                                int localMovieId = movieInfoList.get(0).getId();
                                int remoteMovieId = movieInfos.get(0).getId();
                                if (localMovieId != remoteMovieId) {
                                    notifyFirstMovieChanged(getContext());
                                }
                            }
                            sendComBroadcast(Constant.STATE_SUCCESS);
                        }
                    }
                });
    }

    private void notifyFirstMovieChanged(Context context) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MovieListActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.movie_changed))
                .setSmallIcon(R.drawable.ic_app)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.FLAG_SHOW_LIGHTS;

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 100;

        notificationManager.notify(notifyId, notification);
    }

    private void sendNoDataBroadcast() {
        sendComBroadcast(Constant.STATE_NO_DATA);
    }

    private void sendErrorBroadcast() {
        sendComBroadcast(Constant.STATE_ERROR);
    }

    private void sendComBroadcast(int state) {
        Intent intent = new Intent();
        intent.setAction(Constant.BC_ACTION_DATA_CHANGED);
        intent.putExtra(Constant.ExtraName.DATA_LOAD_STATE, state);
        getContext().sendBroadcast(intent);
    }
}
