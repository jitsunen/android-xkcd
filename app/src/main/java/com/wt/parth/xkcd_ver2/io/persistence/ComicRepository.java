package com.wt.parth.xkcd_ver2.io.persistence;

import com.google.common.collect.Lists;

import android.support.annotation.NonNull;
import android.util.Log;

import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */

public class ComicRepository implements ComicRepositoryI {
    private static final String TAG = "ComicRepository";
    private final ComicDataSource localDataSource;
    private final ComicDataSource remoteDataSource;

    public ComicRepository(ComicDataSource localDataSource, ComicDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void init() {

    }

    @Override
    public void getComics(@NonNull final LoadComicsCallback loadComicsCallback, final Integer numberOfComicsToRetrieve, final Integer startNumber) {
        // first get from local, if not found, get from remote and add to local.
        localDataSource.getComics(new LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                Log.i(TAG, "Found " + comicList.size() + " comics in local store");
                loadComicsCallback.onComicsLoaded(comicList);
            }

            @Override
            public void onDataNotAvailable() {
                Log.i(TAG, "Did not find any comics in local store, getting from remote store");
                getTasksFromRemoteDataSource(loadComicsCallback, numberOfComicsToRetrieve, startNumber);
            }
        }, numberOfComicsToRetrieve, startNumber);
    }

    private void getTasksFromRemoteDataSource(final LoadComicsCallback callback, Integer numberOfComicsToRetrieve, Integer startNumber) {
        remoteDataSource.getComics(new LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comics) {
                refreshLocalDataSource(Lists.newArrayList(comics));
                Log.i(TAG, "Added " + comics.size() + " comics to local store");
                callback.onComicsLoaded(comics);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        }, numberOfComicsToRetrieve, startNumber);
    }

    private void refreshLocalDataSource(List<Comic> comics) {
        localDataSource.addComics(comics);
    }

    @Override
    public void refreshComics() {
        localDataSource.removeAll();
    }
}
