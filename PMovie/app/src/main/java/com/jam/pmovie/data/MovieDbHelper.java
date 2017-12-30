package com.jam.pmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jam.pmovie.data.MovieContract.MovieEntity;

/**
 * Created by jam on 17/9/8.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "PMovie.db";
    private final static int DB_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "CREATE TABLE "+ MovieEntity.TB_NAME + " ("
                + MovieEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntity.CL_MOVIE_ID + " LONG, " + MovieEntity.CL_TITLE + " TEXT, "
                + MovieEntity.CL_VIDEO + " BLOB, " + MovieEntity.CL_VOTE_AVERAGE + " TEXT, "
                + MovieEntity.CL_VOTE_COUNT + " INTEGER, " + MovieEntity.CL_ADULT + " BLOB, "
                + MovieEntity.CL_BACKDROP_PATH + " TEXT, " + MovieEntity.CL_OVERVIEW + " TEXT,"
                + MovieEntity.CL_POSTER_PATH + " TEXT, " + MovieEntity.CL_ORG_LANGUAGE + " TEXT, "
                + MovieEntity.CL_ORG_TITLE + " TEXT, " + MovieEntity.CL_POPULARITY + " TEXT, "
                + MovieEntity.CL_RELEASE_DATE + " TEXT)";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
