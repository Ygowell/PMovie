package com.jam.pmovie.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.jam.pmovie.MovieApp;
import com.jam.pmovie.bean.MovieInfo;
import com.jam.pmovie.data.MovieContract.MovieEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jam on 18/1/4.
 */

public class MovieCpHelper {

    private static ContentResolver mResolver;

    private static MovieCpHelper mInstance = null;

    private MovieCpHelper() {
        mResolver = MovieApp.getContext().getContentResolver();
    }

    public static synchronized MovieCpHelper getInstance() {
        if (mInstance == null) {
            mInstance = new MovieCpHelper();
        }
        return mInstance;
    }

    public static void saveMovieList(List<MovieInfo> movieInfoList) {
        for (MovieInfo movieInfo : movieInfoList) {
            ContentValues values = new ContentValues();
            values.put(MovieEntity.CL_MOVIE_ID, movieInfo.getId());
            values.put(MovieEntity.CL_ADULT, movieInfo.isAdult() ? 1 : 0);
            values.put(MovieEntity.CL_BACKDROP_PATH, movieInfo.getBackdropPath());
            values.put(MovieEntity.CL_COLLECTED, movieInfo.isCollected() ? 1 : 0);
            values.put(MovieEntity.CL_HAS_LOAD, movieInfo.isLoaded() ? 1 : 0);
            values.put(MovieEntity.CL_ORG_LANGUAGE, movieInfo.getOrgLanguage());
            values.put(MovieEntity.CL_ORG_TITLE, movieInfo.getOrgTitle());
            values.put(MovieEntity.CL_OVERVIEW, movieInfo.getOverview());
            values.put(MovieEntity.CL_POPULARITY, movieInfo.getPopularity());
            values.put(MovieEntity.CL_POSTER_PATH, movieInfo.getPosterPath());
            values.put(MovieEntity.CL_RELEASE_DATE, movieInfo.getReleaseDate());
            values.put(MovieEntity.CL_VIDEO, movieInfo.isVideo() ? 1 : 0);
            values.put(MovieEntity.CL_TITLE, movieInfo.getTitle());
            values.put(MovieEntity.CL_VOTE_AVERAGE, movieInfo.getVoteAverage());
            values.put(MovieEntity.CL_VOTE_COUNT, movieInfo.getVoteCount());
            values.put(MovieEntity.CL_SORT_TYPE, movieInfo.getSortType());
            mResolver.insert(MovieEntity.CONTENT_URI, values);
        }
    }

    public Observable<List<MovieInfo>> getMovieList(final int sortType, final boolean onlyCollected) {
        return Observable.just(onlyCollected)
                .map(new Func1<Boolean, Cursor>() {
                    @Override
                    public Cursor call(Boolean aBoolean) {
                        String selection = MovieEntity.CL_SORT_TYPE + " = ? and "
                                + MovieEntity.CL_COLLECTED + " = ?";
                        String[] selectionArgs = {sortType + "", onlyCollected ? "1" : "0"};
                        return mResolver.query(MovieEntity.CONTENT_URI,
                                null,
                                selection,
                                selectionArgs,
                                null);
                    }
                }).flatMap(new Func1<Cursor, Observable<List<MovieInfo>>>() {
                    @Override
                    public Observable<List<MovieInfo>> call(Cursor cursor) {
                        if (cursor == null || cursor.getCount() == 0) {
                            return Observable.just(null);
                        }
                        cursor.moveToFirst();
                        List<MovieInfo> movieInfoList = new ArrayList<>();
                        do {
                            MovieInfo movieInfo = new MovieInfo();
                            movieInfo.setId(getLong(cursor, MovieEntity.CL_MOVIE_ID));
                            movieInfo.setAdult(getBool(cursor, MovieEntity.CL_ADULT));
                            movieInfo.setBackdropPath(getString(cursor, MovieEntity.CL_BACKDROP_PATH));
                            movieInfo.setCollected(getBool(cursor, MovieEntity.CL_COLLECTED));
                            movieInfo.setLoaded(getBool(cursor, MovieEntity.CL_HAS_LOAD));
                            movieInfo.setVideo(getBool(cursor, MovieEntity.CL_VIDEO));
                            movieInfo.setOrgLanguage(getString(cursor, MovieEntity.CL_ORG_LANGUAGE));
                            movieInfo.setOrgTitle(getString(cursor, MovieEntity.CL_ORG_TITLE));
                            movieInfo.setTitle(getString(cursor, MovieEntity.CL_TITLE));
                            movieInfo.setOverview(getString(cursor, MovieEntity.CL_OVERVIEW));
                            movieInfo.setPopularity(getString(cursor, MovieEntity.CL_POPULARITY));
                            movieInfo.setPosterPath(getString(cursor, MovieEntity.CL_POSTER_PATH));
                            movieInfo.setReleaseDate(getString(cursor, MovieEntity.CL_RELEASE_DATE));
                            movieInfo.setSortType(getInt(cursor, MovieEntity.CL_SORT_TYPE));
                            movieInfo.setVoteAverage(getString(cursor, MovieEntity.CL_VOTE_AVERAGE));
                            movieInfo.setVoteCount(getInt(cursor, MovieEntity.CL_VOTE_COUNT));
                            movieInfoList.add(movieInfo);
                        } while (cursor.moveToNext());

                        return Observable.just(movieInfoList);
                    }
                }).subscribeOn(Schedulers.io());
    }

    private long getLong(Cursor cursor, String cl_name) {
        return cursor.getLong(cursor.getColumnIndex(cl_name));
    }

    private String getString(Cursor cursor, String cl_name) {
        return cursor.getString(cursor.getColumnIndex(cl_name));
    }

    private int getInt(Cursor cursor, String cl_name) {
        return cursor.getInt(cursor.getColumnIndex(cl_name));
    }

    private boolean getBool(Cursor cursor, String cl_name) {
        return getInt(cursor, cl_name) == 0 ? false : true;
    }
}
