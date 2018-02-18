package com.jam.pmovie.common;

/**
 * Created by jam on 17/8/19.
 */

public class Constant {
    public static final class ExtraName {
        public static final String MOVIE_DATA = "MOVIE_DATA";

        public static final String DATA_LOAD_STATE = "DATA_LOAD_STATE";
    }

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_NO_DATA = 2;

    public static final int SORT_TYPE_POPULAR = 0;
    public static final int SORT_TYPE_SCORE = 1;

    public static final String BC_ACTION_DATA_CHANGED = "com.jam.pmovie.datachanged";

    public static final int REQ_SETTING = 100;
}
