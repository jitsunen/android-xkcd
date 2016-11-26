package com.wt.parth.xkcd_ver2.io.persistence;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 25/11/16.
 */

public interface ComicRepositoryI {
    interface LoadComicsCallback {
        void onComicsLoaded(List<Comic> comicList);

        void onDataNotAvailable();
    }

    void init();

    void getComics(@NonNull LoadComicsCallback loadComicsCallback, Integer numberOfComicsToRetrieve, Integer startNumber);

    void refreshComics();
}
