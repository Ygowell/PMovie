package com.jam.pmovie.http;

import com.google.gson.Gson;
import com.jam.pmovie.BuildConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    private String run(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = mClient.newCall(request).execute();
        if (response != null & response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

    public Observable<T> execute(final String url) {
        return execute(url, null);
    }

    public Observable<T> execute(final String url, HashMap<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder()
                .add(PARAM_API_KEY, BuildConfig.OPEN_MOVIE_API_KEY);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        final RequestBody body = builder.build();

        return Observable.unsafeCreate(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    String result = run(url, body);
                    Gson gson = new Gson();
                    T t = gson.fromJson(result, mClass);
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
