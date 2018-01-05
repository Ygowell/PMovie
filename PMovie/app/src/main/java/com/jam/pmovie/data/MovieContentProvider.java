package com.jam.pmovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jam.pmovie.data.MovieContract.CommentEntity;
import com.jam.pmovie.data.MovieContract.MovieEntity;
import com.jam.pmovie.data.MovieContract.NoticeEntity;

/**
 * Created by jam on 17/9/8.
 */

public class MovieContentProvider extends ContentProvider {

    private final static int MOVIES = 100;
    private final static int MOVIES_WITH_ID = 101;
    private final static int NOTICES = 102;
    private final static int NOTICES_WITH_MOVIE_ID = 103;
    private final static int COMMENTS = 104;
    private final static int COMMENTS_WITH_MOVIE_ID = 105;
    private final static String TAG = "MovieContentProvider";

    private MovieDbHelper mMovieDbHelper;
    private UriMatcher mUriMatcher = buidUriMatcher();

    public static UriMatcher buidUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_NOTICES, NOTICES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_COMMENTS, COMMENTS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case MOVIES:
                retCursor = db.query(MovieEntity.TB_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case NOTICES_WITH_MOVIE_ID:
                retCursor = db.query(NoticeEntity.TB_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COMMENTS_WITH_MOVIE_ID:
                retCursor = db.query(CommentEntity.TB_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unkonw uri:" + uri.toString());
        }

        if (retCursor != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        Uri retUri = null;
        String tableName;

        switch (match) {
            case MOVIES:
                tableName = MovieEntity.TB_NAME;
                break;
            case COMMENTS:
                tableName = CommentEntity.TB_NAME;
                break;
            case NOTICES:
                tableName = NoticeEntity.TB_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unkonw uri: " + uri.toString());
        }

        long id = db.insert(tableName, null, values);

        if (id > 0) {
            Log.d(TAG, "uri = " + uri.toString());
            retUri = ContentUris.withAppendedId(uri, id);
        } else {
            Log.e(TAG, "Failed to insert uri: " + uri.toString());
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                int id = db.update(MovieEntity.TB_NAME,
                        values,
                        selection,
                        selectionArgs);
                return id;
            default:
                throw new UnsupportedOperationException("Unkonw uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
