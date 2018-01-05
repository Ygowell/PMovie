package com.jam.pmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jam.pmovie.data.MovieContract.MovieEntity;
import com.jam.pmovie.data.MovieContract.NoticeEntity;
import com.jam.pmovie.data.MovieContract.CommentEntity;

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
        initMoiveTable(db);
        initNoticeTable(db);
        initCommentTable(db);
    }

    private void initMoiveTable(SQLiteDatabase db) {
        String createSql = "CREATE TABLE " + MovieEntity.TB_NAME + " ("
                + MovieEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntity.CL_MOVIE_ID + " LONG, "
                + MovieEntity.CL_TITLE + " TEXT, "
                + MovieEntity.CL_VOTE_AVERAGE + " TEXT, "
                + MovieEntity.CL_VOTE_COUNT + " INTEGER, "
                + MovieEntity.CL_BACKDROP_PATH + " TEXT, "
                + MovieEntity.CL_OVERVIEW + " TEXT,"
                + MovieEntity.CL_POSTER_PATH + " TEXT, "
                + MovieEntity.CL_ORG_LANGUAGE + " TEXT, "
                + MovieEntity.CL_ORG_TITLE + " TEXT, "
                + MovieEntity.CL_POPULARITY + " TEXT, "
                + MovieEntity.CL_RELEASE_DATE + " TEXT, "
                + MovieEntity.CL_ADULT + " INTEGER, "
                + MovieEntity.CL_VIDEO + " INTEGER, "
                + MovieEntity.CL_COLLECTED + " INTEGER,"
                + MovieEntity.CL_SORT_TYPE + " INTEGER,"
                + MovieEntity.CL_HAS_LOAD + " INTEGER)";
        db.execSQL(createSql);
    }

    private void initNoticeTable(SQLiteDatabase db) {
        String createSql = "CREATE TABLE " + NoticeEntity.TB_NAME + " ("
                + NoticeEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NoticeEntity.CL_MOVIE_ID + " LONG, "
                + NoticeEntity.CL_KEY + " TEXT, "
                + NoticeEntity.CL_NOTICE_ID + " TEXT,"
                + NoticeEntity.CL_NAME + " TEXT, "
                + NoticeEntity.CL_SITE + " TEXT, "
                + NoticeEntity.CL_SIZE + " INTEGER, "
                + NoticeEntity.CL_TYPE + " TEXT)";
        db.execSQL(createSql);
    }

    private void initCommentTable(SQLiteDatabase db) {
        String createSql = "CREATE TABLE " + CommentEntity.TB_NAME + " ("
                + CommentEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CommentEntity.CL_MOVIE_ID + " LONG, "
                + CommentEntity.CL_AUTHOR + " TEXT, "
                + CommentEntity.CL_COMMENT_ID + " TEXT, "
                + CommentEntity.CL_CONTENT + " TEXT, "
                + CommentEntity.CL_URL + " TEXT)";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
