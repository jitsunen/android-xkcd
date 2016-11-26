package com.wt.parth.xkcd_ver2.io.persistence.remote;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */

public class ComicRemoteDataSource implements ComicDataSource {
    @Override
    public void init() {

    }

    @Override
    public void getComics(@NonNull LoadComicsCallback loadComicsCallback, Integer numberOfComicsToRetrieve, Integer startNumber) {
        FetchComicsTask fetchComicsTask = new FetchComicsTask(loadComicsCallback);
        fetchComicsTask.execute(new ComicDownloadInput(numberOfComicsToRetrieve, startNumber));
    }

    @Override
    public void refreshComics() {
        // nothing to do for remote data source.
    }

    @Override
    public void addComics(List<Comic> comics) {
        // nothing to do for remote data source.
    }

    @Override
    public void removeAll() {
        // nothing to do for remote data source.
    }
}
