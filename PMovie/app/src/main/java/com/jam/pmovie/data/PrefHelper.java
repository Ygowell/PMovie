package com.jam.pmovie.data;

import android.content.Context;
import android.content.SharedPreferences;

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
}

