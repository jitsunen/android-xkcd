package com.wt.parth.xkcd_ver2.io.persistence.local.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by parth on 25/11/16.
 */

public class ComicContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private ComicDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ComicContentContract.CONTENT_AUTHORITY, ComicContentContract.PATH_COMIC, COMICS);
        uriMatcher.addURI(ComicContentContract.CONTENT_AUTHORITY, ComicContentContract.PATH_COMIC_LIMIT + "/#", COMICS_LIMIT);
        uriMatcher.addURI(ComicContentContract.CONTENT_AUTHORITY, ComicContentContract.PATH_COMIC + "/#", COMIC_ID);
        uriMatcher.addURI(ComicContentContract.CONTENT_AUTHORITY, ComicContentContract.PATH_RECENT_COMIC, COMIC_RECENT);
        return uriMatcher;
    }

    private static final int COMICS = 100;
    private static final int COMIC_ID = 101;
    private static final int COMIC_RECENT = 102;
    private static final int COMICS_LIMIT = 103;

    public ComicContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ComicContentContract.ComicEntry.TABLE_NAME, null, null);
    }

    @Override
    public String getType(Uri uri) {
        Log.i("ComicContentProvider", "> getType " + uri);
        switch (uriMatcher.match(uri)) {
            case COMICS:
                return ComicContentContract.ComicEntry.CONTENT_TYPE;
            case COMIC_ID:
                return ComicContentContract.ComicEntry.CONTENT_ITEM_TYPE;
            case COMIC_RECENT:
                return ComicContentContract.ComicEntry.CONTENT_ITEM_TYPE;
            case COMICS_LIMIT:
                return ComicContentContract.ComicEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case COMICS:
                db.insert(ComicContentContract.ComicEntry.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(ComicContentContract.ComicEntry.CONTENT_URI,
                        Long.valueOf(values.getAsString(ComicContentContract.ComicEntry.COLUMN_NAME_NUM)));
                break;
            default:
                throw new UnsupportedOperationException("Cannot insert for uri " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case COMICS: {
                db.beginTransaction();
                int insertedRows = 0;
                try {
                    for (ContentValues contentValues : values) {
                        long id = db.insert(ComicContentContract.ComicEntry.TABLE_NAME, null, contentValues);
                        if (id > 0) {
                            insertedRows++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return insertedRows;
            }
        }
        return 0;
    }

    @Override
    public boolean onCreate() {
        Log.i("ComicContentProvider", "> onCreate ");
        dbHelper = new ComicDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.i("ComicContentProvider", "> query " + uri);
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case COMICS:
                retCursor = dbHelper.getReadableDatabase().query(ComicContentContract.ComicEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COMICS_LIMIT:
                String limit = ContentUris.parseId(uri) + "";
                retCursor = dbHelper.getReadableDatabase().query(ComicContentContract.ComicEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, limit);
                break;
            case COMIC_ID:
                long id = ContentUris.parseId(uri);
                retCursor = dbHelper.getReadableDatabase().query(ComicContentContract.ComicEntry.TABLE_NAME, projection,
                        ComicContentContract.ComicEntry._ID + "=?",
                        new String[]{id + ""}, null, null, sortOrder);
                break;
            case COMIC_RECENT:
                retCursor = dbHelper.getReadableDatabase().query(ComicContentContract.ComicEntry.TABLE_NAME, projection,
                        ComicContentContract.ComicEntry._ID + "=max(" + ComicContentContract.ComicEntry._ID + ")",
                        null, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
