package com.wt.parth.xkcd_ver2.ui;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.ui.base.BasePresenter;
import com.wt.parth.xkcd_ver2.ui.base.BaseView;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */

public interface ComicContract {
    interface Presenter extends BasePresenter {
        void loadComics(boolean forceRefresh);

        void displayComic(@NonNull Comic comic);
    }

    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean show);

        void showComics(List<Comic> comics);

        boolean isActive();

        void showLoadingComicsError();

        void showComicUI(Comic comic);
    }
}
