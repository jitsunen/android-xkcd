package com.wt.parth.xkcd_ver2.io.persistence.local.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

/**
 * Created by parth on 25/11/16.
 */

public class ComicDbHelper extends SQLiteOpenHelper{
    private static final String COLUMN_NAME_NUM = "number";
    private static final String TABLE_NAME = "xkcd_comics";

    private  static final String INDEX_SQL = "CREATE INDEX NUMBER on " + TABLE_NAME + " ( " + COLUMN_NAME_NUM + " ); ";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "xkcd_comics.db";

    public ComicDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase dbObj) {
                //ToDo: implement this
                Log.e("ComicDbHelper", "Db got corrupted");
            }
        });
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder columns = new StringBuilder();
        for (Pair<String, String> colDef : ComicContentContract.ComicEntry.COLUMN_NAMES_AND_TYPES) {
            columns.append(colDef.first).append(" ").append(colDef.second).append(",");
        }
        // remove last comma
        columns.deleteCharAt(columns.length() - 1);

        String sqlTemplate = "CREATE TABLE %s (_id INTEGER PRIMARY KEY, %s);";
        String sql = String.format(sqlTemplate, ComicContentContract.ComicEntry.TABLE_NAME, columns.toString());
        sql = sql + INDEX_SQL;

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do right now.
    }
}
