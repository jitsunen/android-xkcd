package com.wt.parth.xkcd_ver2.io.persistence;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */

public interface ComicDataSource extends ComicRepositoryI {
    void addComics(List<Comic> comics);

    void removeAll();
}
