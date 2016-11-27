package com.wt.parth.xkcd_ver2.io.persistence.local;

import com.google.common.collect.Lists;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepositoryI;
import com.wt.parth.xkcd_ver2.io.persistence.local.db.ComicContentContract;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 25/11/16.
 */

public class ComicLocalDataSource implements ComicDataSource {

    private static final String TAG = "ComicLocalDataSource";
    private final Context context;

    public ComicLocalDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void init() {

    }

    @Override
    public void getComics(@NonNull LoadComicsCallback loadComicsCallback, Integer numberOfComicsToRetrieve, Integer startNumber) {
        List<Comic> comics = Lists.newArrayList();
        String selection = "";
        List<String> selectionArgs = Lists.newArrayList();
        if (startNumber != null) {
            selection += ComicContentContract.ComicEntry.COLUMN_NAME_NUM + " = ?";
            selectionArgs.add(startNumber + "");
        } else {
            selection += ComicContentContract.ComicEntry.COLUMN_NAME_NUM + " != ? ";
            selectionArgs.add("0");
        }

        if (selection.equals("")) {
            selection = null;
        }

        String[] args = null;
        if (!selectionArgs.isEmpty()) {
            args = selectionArgs.toArray(new String[]{});
        }

        Cursor cursor = null;
        if (numberOfComicsToRetrieve != null) {
            cursor = context.getContentResolver().query(
                    ContentUris.withAppendedId(ComicContentContract.ComicEntry.CONTENT_LIMIT_URI, numberOfComicsToRetrieve),
                    null,
                    selection, args,
                    ComicContentContract.ComicEntry.COLUMN_NAME_NUM + " DESC");
        } else {
            cursor = context.getContentResolver().query(
                    ComicContentContract.ComicEntry.CONTENT_URI,
                    null,
                    selection, args,
                    ComicContentContract.ComicEntry.COLUMN_NAME_NUM + " DESC");
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Comic comic = new Comic(cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_ALT)),
                        cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_IMG)),
                        cursor.getInt(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_NUM)));
                comics.add(comic);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (comics.isEmpty()) {
            loadComicsCallback.onDataNotAvailable();
        } else {
            loadComicsCallback.onComicsLoaded(comics);
        }
    }

    @Override
    public void getComic(@NonNull LoadComicCallback loadComicCallback, Integer comicNumber) {
        Uri contentUri = ContentUris.withAppendedId(ComicContentContract.ComicEntry.CONTENT_URI, comicNumber);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        Comic comic = null;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                comic = new Comic(cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_ALT)),
                        cursor.getString(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_IMG)),
                        cursor.getInt(cursor.getColumnIndex(ComicContentContract.ComicEntry.COLUMN_NAME_NUM)));
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        if (comic != null) {
            loadComicCallback.onComicLoaded(comic);
        } else {
            loadComicCallback.onDataNotAvailable();
        }
    }

    @Override
    public void refreshComics() {

    }

    @Override
    public void addComics(List<Comic> comics) {
        for (Comic comic : comics) {
            add(comic);
        }
    }

    private void add(Comic comic) {
        Cursor comicCursor = context.getContentResolver().query(ComicContentContract.ComicEntry.CONTENT_URI,
                new String[]{ComicContentContract.ComicEntry._ID},
                ComicContentContract.ComicEntry.COLUMN_NAME_NUM + " = ?",
                new String[]{comic.getIndex() + ""}, null);
        if (!comicCursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ComicContentContract.ComicEntry.COLUMN_NAME_TITLE, comic.getTitle());
            contentValues.put(ComicContentContract.ComicEntry.COLUMN_NAME_NUM, comic.getIndex());
            contentValues.put(ComicContentContract.ComicEntry.COLUMN_NAME_ALT, comic.getTranscript());
            contentValues.put(ComicContentContract.ComicEntry.COLUMN_NAME_IMG, comic.getUrl());
            // ToDo: add date column
            Uri uri = context.getContentResolver().insert(ComicContentContract.ComicEntry.CONTENT_URI, contentValues);
            Log.i(TAG, "Added uri " + uri);
        } else {
            Log.i(TAG, "Comic " + comic.getIndex() + " already exists");
        }
    }

    @Override
    public void removeAll() {
        int numRowssDeleted = context.getContentResolver().delete(ComicContentContract.ComicEntry.CONTENT_URI, null, null);
        Log.i(TAG, "Deleted " + numRowssDeleted + " comics");
    }
}
