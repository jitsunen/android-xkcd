package com.wt.parth.xkcd_ver2.ui;

import com.google.common.collect.Lists;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepository;
import com.wt.parth.xkcd_ver2.model.Comic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by parth on 20/11/16.
 */
public class ComicPresenterTest {

    @Mock
    private ComicRepository comicRepository;

    @Mock
    private ComicContract.View comicView;

    private List<Comic> COMICS;

    @Captor
    private ArgumentCaptor<ComicDataSource.LoadComicsCallback> loadComicsCallbackArgumentCaptor;

    private ComicPresenter sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.sut = new ComicPresenter(comicView, comicRepository);

        when(comicView.isActive()).thenReturn(true);

        COMICS = Lists.newArrayList(new Comic(), new Comic());
    }

    @Test
    public void loadComics() throws Exception {
        // exercise
        sut.loadComics(false);

        // verify
        verify(comicRepository).getComics(loadComicsCallbackArgumentCaptor.capture());
        loadComicsCallbackArgumentCaptor.getValue().onComicsLoaded(COMICS);

        InOrder inOrder = inOrder(comicView);
        inOrder.verify(comicView).showLoadingIndicator(true);
        inOrder.verify(comicView).showLoadingIndicator(false);
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(comicView).showComics(argumentCaptor.capture());
        assertEquals("Comics size must be equal to expected", COMICS.size(), argumentCaptor.getValue().size());
    }

    //    @Test
    public void displayComic() throws Exception {

    }

    //    @Test
    public void start() throws Exception {

    }

}