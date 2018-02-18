package com.jam.pmovie.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.jam.pmovie.common.Constant;

/**
 * Created by jam on 17/12/31.
 */

public class PrefHelper {

    private static SharedPreferences mSp;

    public static void init(Context context) {
        mSp = context.getSharedPreferences("movie_sp", Context.MODE_PRIVATE);
    }

    public static void saveFirstLoadPopularData(boolean isFirst) {
        mSp.edit().putBoolean(PrefKey.FIRST_LOAD_POPUPLAER_DATA, isFirst).commit();
    }

    public static boolean getFirstLoadPopularData() {
        return mSp.getBoolean(PrefKey.FIRST_LOAD_POPUPLAER_DATA, true);
    }

    public static void saveFirstLoadScoreData(boolean isFirst) {
        mSp.edit().putBoolean(PrefKey.FIRST_LOAD_SCORE_DATA, isFirst).commit();
    }

    public static boolean getFirstLoadScoreData() {
        return mSp.getBoolean(PrefKey.FIRST_LOAD_SCORE_DATA, true);
    }

    public static void saveNotificationOpen(boolean isOpen) {
        mSp.edit().putBoolean(PrefKey.NOTIFICATION_OPEN, isOpen).commit();
    }

    public static boolean getNotificationOpen() {
        return mSp.getBoolean(PrefKey.NOTIFICATION_OPEN, true);
    }

    public static void saveReqSortType(int sortType) {
        mSp.edit().putInt(PrefKey.SORT_TYPE, sortType).commit();
    }

    public static int getReqSortType() {
        return mSp.getInt(PrefKey.SORT_TYPE, Constant.SORT_TYPE_POPULAR);
    }

    public static void saveInt(String key, int value) {
        mSp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    public static void saveString(String key, String value) {
        mSp.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public static void saveBoolean(String key, boolean value) {
        mSp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }
}

