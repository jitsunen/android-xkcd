package com.wt.parth.xkcd_ver2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.test.runner.AndroidJUnit4;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepository;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepositoryI;
import com.wt.parth.xkcd_ver2.io.persistence.local.ComicLocalDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.remote.ComicDownloadInput;
import com.wt.parth.xkcd_ver2.io.persistence.remote.ComicRemoteDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.remote.FetchComicsTask;
import com.wt.parth.xkcd_ver2.model.Comic;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.wt.parth.xkcd_ver2", appContext.getPackageName());
    }

    @Ignore
    @Test
    public void testComicFetch() throws Exception {
        final List<Comic> comics = Lists.newArrayList();
        ComicDataSource.LoadComicsCallback comicsCallback = new ComicDataSource.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                comics.addAll(comicList);
            }

            @Override
            public void onDataNotAvailable() {

            }
        };

        FetchComicsTask task = new FetchComicsTask(comicsCallback);
        List<Comic> comics1 = task.execute(new ComicDownloadInput(1, null)).get(20, TimeUnit.SECONDS);
        assertTrue("Comics must not be empty", comics.size() > 0);
    }

    @Ignore
    @Test
    public void testComicRepository() throws Exception {
        ComicDataSource local = new ComicLocalDataSource(InstrumentationRegistry.getTargetContext());
        ComicDataSource remote = new ComicRemoteDataSource();

        ComicRepository sut = new ComicRepository(local, remote);

        sut.refreshComics();
        sut.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                assertTrue("Comics must not be empty", comicList.size() > 0);
            }

            @Override
            public void onDataNotAvailable() {
                fail("Comics should have been loaded");
            }
        }, 50, null);
    }

    //    @Ignore
    @Test
    public void testComicRepository_paged() throws Exception {
        ComicDataSource local = new ComicLocalDataSource(InstrumentationRegistry.getTargetContext());
        ComicDataSource remote = new ComicRemoteDataSource();

        ComicRepository sut = new ComicRepository(local, remote);

        final int[] startNumber = {0, 0};
        sut.refreshComics();

        final CountDownLatch latch = new CountDownLatch(1);
        sut.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                assertTrue("Comics must not be empty", comicList.size() > 0);
                Comic comic = comicList.get(4);
                startNumber[0] = comic.getIndex();
                latch.countDown();
            }

            @Override
            public void onDataNotAvailable() {
                fail("Comics should have been loaded");
            }
        }, 5, null);
        latch.await();

        assertTrue("Comic number must not be zero", startNumber[0] != 0);

        final CountDownLatch latch2 = new CountDownLatch(1);
        sut.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                assertTrue("Comics must not be empty", comicList.size() > 0);
                Comic comic = comicList.get(0);
                startNumber[1] = comic.getIndex();
                latch2.countDown();
            }

            @Override
            public void onDataNotAvailable() {
                fail("Comics should have been loaded");
            }
        }, 10, startNumber[0] - 1);
        latch2.await();

        assertTrue("Comic number 2 must not be zero", startNumber[1] != 0);
        assertEquals("Comic number 2 must not be one less than comic number 1", (startNumber[1] + 1), startNumber[0]);
    }

    @Ignore
    @Test
    public void testComicLocalRepository() throws Exception {
        ComicDataSource sut = new ComicLocalDataSource(InstrumentationRegistry.getTargetContext());

        sut.refreshComics();
        sut.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                assertTrue("Comics must not be empty", comicList.size() > 0);
            }

            @Override
            public void onDataNotAvailable() {
                fail("Comics should have been loaded");
            }
        }, null, null);
    }
}
