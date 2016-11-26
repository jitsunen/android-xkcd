package com.wt.parth.xkcd_ver2.io.persistence.local.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Pair;

import com.wt.parth.xkcd_ver2.ui.ComicContract;

/**
 * Created by parth on 25/11/16.
 */

public class ComicContentContract {
    public static final String CONTENT_AUTHORITY = "com.wt.parth.xkcd.content.provider.v2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECENT_COMIC = "comic/recent";
    public static final String PATH_COMIC = "comic";
    public static final String PATH_COMIC_LIMIT = "comic/limit";

    // column types
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String TYPE_TEXT = "TEXT";

    private ComicContentContract() {
    }

    public static class ComicEntry implements BaseColumns {

        public static final String TABLE_NAME = "xkcd_comics";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COMIC);
        public static final Uri CONTENT_LIMIT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COMIC_LIMIT);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + CONTENT_AUTHORITY + ".xkcd_comic";

        public static final String COLUMN_NAME_NUM = "number";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ALT = "alt";
        public static final String COLUMN_NAME_IMG = "img_link";

        public static final String[] COLUMNS = new String[]{COLUMN_NAME_NUM, COLUMN_NAME_ALT, COLUMN_NAME_TITLE, COLUMN_NAME_IMG, COLUMN_NAME_DATE, _ID};

        public static final Pair<String, String>[] COLUMN_NAMES_AND_TYPES = new Pair[]{Pair.create(COLUMN_NAME_NUM, TYPE_INTEGER),
                Pair.create(COLUMN_NAME_DATE, TYPE_TEXT),
                Pair.create(COLUMN_NAME_TITLE, TYPE_TEXT),
                Pair.create(COLUMN_NAME_ALT, TYPE_TEXT),
                Pair.create(COLUMN_NAME_IMG, TYPE_TEXT)};

    }
}
