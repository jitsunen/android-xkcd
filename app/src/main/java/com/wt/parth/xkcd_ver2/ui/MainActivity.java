package com.wt.parth.xkcd_ver2.ui;

import com.google.common.collect.Lists;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.wt.parth.xkcd_ver2.R;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepository;
import com.wt.parth.xkcd_ver2.io.persistence.ComicRepositoryI;
import com.wt.parth.xkcd_ver2.io.persistence.local.ComicLocalDataSource;
import com.wt.parth.xkcd_ver2.io.persistence.remote.ComicRemoteDataSource;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Comic> comics;
    private ComicAdapter comicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        comics = Lists.newArrayList();
        comicAdapter = new ComicAdapter(this, comics);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(comicAdapter);

        ComicRepository repository = new ComicRepository(new ComicLocalDataSource(getApplicationContext()), new ComicRemoteDataSource());
        repository.refreshComics();
        repository.getComics(new ComicRepositoryI.LoadComicsCallback() {
            @Override
            public void onComicsLoaded(List<Comic> comicList) {
                Log.i("MainActivity", "loaded " + comicList.size() + " comics");
                comics.addAll(comicList);
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDataNotAvailable() {
                Log.i("MainActivity", " some problem occured while loading comics");
            }
        }, 5, null);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
