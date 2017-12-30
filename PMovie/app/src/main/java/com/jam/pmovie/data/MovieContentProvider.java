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

/**
 * Created by jam on 17/9/8.
 */

public class MovieContentProvider extends ContentProvider {

    private final static int CODE_ITEMS = 100;
    private final static int CODE_ITEM_WITH_MOVIE_ID = 101;

    private MovieDbHelper mMovieDbHelper;
    private UriMatcher mUriMatcher = buidUriMatcher();

    public static UriMatcher buidUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, CODE_ITEMS);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", CODE_ITEM_WITH_MOVIE_ID);
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
            case CODE_ITEMS:
                retCursor = db.query(MovieContract.MovieEntity.TB_NAME,
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

        switch (match) {
            case CODE_ITEMS:
                long id = db.insert(MovieContract.MovieEntity.TB_NAME, null, values);

                if(id != 0) {
                    retUri = ContentUris.withAppendedId(MovieContract.MovieEntity.CONTENT_URI, id);
                } else {
                    Log.e("MovieContentProvider", "Failed to insert uri: " + uri.toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkonw uri: " + uri.toString());
        }

        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
