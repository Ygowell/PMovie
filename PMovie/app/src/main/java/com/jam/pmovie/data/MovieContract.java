package com.jam.pmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jam on 17/9/8.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.jam.pmovie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_NOTICES = "notices";
    public static final String PATH_COMMENTS = "comments";
    public static class MovieEntity implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();

        public static final String TB_NAME = "tb_movies";

        public static final String CL_VOTE_COUNT = "vote_count";
        public static final String CL_MOVIE_ID ="movie_id";
        public static final String CL_VIDEO ="video";
        public static final String CL_VOTE_AVERAGE= "vote_average";
        public static final String CL_TITLE= "title";
        public static final String CL_POPULARITY = "popularity";
        public static final String CL_POSTER_PATH = "poster_path";
        public static final String CL_ORG_LANGUAGE = "org_language";
        public static final String CL_ORG_TITLE = "org_title";
        public static final String CL_BACKDROP_PATH = "backdrop_path";
        public static final String CL_ADULT= "adult";
        public static final String CL_OVERVIEW = "overview";
        public static final String CL_RELEASE_DATE = "release_date";
        public static final String CL_COLLECTED = "collected";
        public static final String CL_HAS_LOAD = "has_load";
        public static final String CL_SORT_TYPE = "sort_type";
    }

    public static class NoticeEntity implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_NOTICES).build();

        public static final String TB_NAME = "tb_notices";

        public static final String CL_MOVIE_ID ="movie_id";
        public static final String CL_NOTICE_ID ="notice_id";
        public static final String CL_KEY ="key";
        public static final String CL_NAME ="name";
        public static final String CL_SITE ="site";
        public static final String CL_SIZE ="size";
        public static final String CL_TYPE ="type";
    }

    public static class CommentEntity implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COMMENTS).build();

        public static final String TB_NAME = "tb_comments";

        public static final String CL_MOVIE_ID ="movie_id";
        public static final String CL_COMMENT_ID ="comment_id";
        public static final String CL_AUTHOR ="author";
        public static final String CL_CONTENT ="content";
        public static final String CL_URL="url";
    }
}
