package com.wt.parth.xkcd_ver2.ui;

import android.support.annotation.NonNull;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepository;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

/**
 * Created by parth on 20/11/16.
 */
public class ComicPresenter implements ComicContract.Presenter {

    private final ComicRepository repo;
    private final ComicContract.View view;

    public ComicPresenter(@NonNull ComicContract.View comicView, @NonNull ComicRepository comicRepository) {
        this.view = comicView;
        this.repo = comicRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadComics(boolean forceRefresh) {
        innerLoadTasks(forceRefresh, true);
    }

    private void innerLoadTasks(final boolean forceRefresh, final boolean showLoadingUI) {
        if (showLoadingUI) {
            view.showLoadingIndicator(showLoadingUI);
        }
        if (forceRefresh) {
            repo.refreshComics();
        }

        repo.getComics(new ComicDataSource.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                if (!view.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    view.showLoadingIndicator(false);
                }
                view.showComics(comicList);
            }

            @Override
            public void onDataNotAvailable() {
                if (!view.isActive()) {
                    return;
                }
                view.showLoadingComicsError();
            }
        }, 50, 0);
    }

    @Override
    public void displayComic(@NonNull Comic comic) {
        view.showComicUI(comic);
    }

    @Override
    public void start() {
        loadComics(false);
        repo.init();
    }
}
