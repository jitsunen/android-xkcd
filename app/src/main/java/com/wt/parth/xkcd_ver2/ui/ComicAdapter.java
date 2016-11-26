package com.wt.parth.xkcd_ver2.ui;

/**
 * Created by parth on 26/11/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wt.parth.xkcd_ver2.R;
import com.wt.parth.xkcd_ver2.model.Comic;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private final List<Comic> comics;
    private final Context context;

    public ComicAdapter(Context context, List<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_card, parent, false);

        return new ComicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        Comic comic = comics.get(position);
        holder.getTitleView().setText(comic.getTitle());

        // loading comic thumbnail using Glide library
        Glide.with(context).load(comic.getUrl()).into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleView;

        public ComicViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            titleView = (TextView) itemView.findViewById(R.id.title);
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
