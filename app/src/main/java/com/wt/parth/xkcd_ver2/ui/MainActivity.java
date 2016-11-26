package com.wt.parth.xkcd_ver2.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wt.parth.xkcd_ver2.R;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepository;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepositoryI;
import com.wt.parth.xkcd_ver2.io.persistence.local.ComicLocalDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.remote.ComicRemoteDataSource;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComicRepository repository = new ComicRepository(new ComicLocalDataSource(getApplicationContext()), new ComicRemoteDataSource());
        repository.refreshComics();
        repository.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                Log.i("MainActivity", "loaded " + comicList.size() + " comics");
            }

            @Override
            public void onDataNotAvailable() {

            }
        }, 5, null);
    }
}
