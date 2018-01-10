package com.jam.pmovie.http;

import android.util.Log;

import com.google.gson.Gson;
import com.jam.pmovie.BuildConfig;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by jam on 17/12/30.
 */

public class OkHttpService<T> {

    private static final String PARAM_API_KEY = "api_key";

    private OkHttpClient mClient = null;

    private Class<T> mClass;

    public OkHttpService(Class<T> clazz) {
        mClass = clazz;
        mClient = new OkHttpClient();
    }

    private String run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        if (response != null & response.isSuccessful()) {
            return response.body().string();
        } else {
            String errorMsg = "请求失败";
            if (response != null) {
                errorMsg = response.message();
            }
            throw new Exception(errorMsg);
        }
    }

    public Observable<T> execute(final String url) {
        return execute(url, null);
    }

    public Observable<T> execute(final String url, HashMap<String, String> params) {
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(url).newBuilder()
                .addQueryParameter(PARAM_API_KEY, BuildConfig.OPEN_MOVIE_API_KEY);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        final String reqUrl = httpUrlBuilder.build().toString();
        Log.d("OkHttpService", "reqUrl = " + reqUrl);

        return Observable.unsafeCreate(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    String result = run(reqUrl);
                    Gson gson = new Gson();
                    T t = gson.fromJson(result, mClass);
                    if (t == null) {
                        subscriber.onError(new Exception("服务器数据错误"));
                    } else {
                        subscriber.onNext(t);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
