package com.wt.parth.xkcd_ver2.io.persistence;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */

public interface ComicDataSource {
    interface LoadComicsCallback {
        void onComicsLoaded(List<Comic> comicList);

        void onDataNotAvailable();
    }

    interface LoadComicCallback {
        void onComicLoaded(@NonNull Comic comic);

        void onDataNotAvailable();
    }

    void getComics(@NonNull LoadComicsCallback loadComicsCallback);

    void getComic(@NonNull LoadComicCallback loadComicCallback);

    void refreshComics();
}
