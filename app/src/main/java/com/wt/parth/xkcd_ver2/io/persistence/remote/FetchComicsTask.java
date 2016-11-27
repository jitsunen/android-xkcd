package com.wt.parth.xkcd_ver2.io.persistence.remote;

import com.google.common.collect.Lists;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wt.parth.xkcd_ver2.io.persistence.ComicDataSource;
import com.wt.parth.xkcd_ver2.model.Comic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Input parameter is number of comics to retrieve.
 *
 * Created by parth on 20/11/16.
 */

public class FetchComicsTask extends AsyncTask<ComicDownloadInput, Void, List<Comic>> {
    public static final String XKCD_SEED_URL = "http://xkcd.com/info.0.json";
    public static final String XKCD_URL_TEMPLATE = "http://xkcd.com/%s/info.0.json";
    public static final String TAG = "FetchComicsTask";

    private final OkHttpClient client;
    private final ComicDataSource.LoadComicsCallback callback;

    private boolean wasError = false;

    public FetchComicsTask(ComicDataSource.LoadComicsCallback loadComicsCallback) {
        this.callback = loadComicsCallback;
        client = new OkHttpClient();
    }

    /**
     * Runs on Main (UI) thread.
     */
    @Override
    protected void onPostExecute(List<Comic> result) {
        if (!wasError) {
            callback.onComicsLoaded(result);
        } else {
            callback.onDataNotAvailable();
        }
        wasError = false;
    }

    /**
     * Runs in background thread.
     */
    @Override
    protected List<Comic> doInBackground(ComicDownloadInput... params) {
        if (params != null) {
            return getRecentComics(params[0]);
        } else {
            return getRecentComics(null);
        }
    }

    private List<Comic> getRecentComics(final ComicDownloadInput downloadInput) {
        final List<Comic> comics = Lists.newArrayList();

        Request request = null;
        if (downloadInput.getStartNumber() == null) {
            request = new Request.Builder().url(XKCD_SEED_URL).build();
        } else {
            String url = String.format(XKCD_URL_TEMPLATE, downloadInput.getStartNumber());
            request = new Request.Builder().url(url).build();
        }

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            comics.add(createComic(jsonObject));
            Log.d(TAG, " >>>>> Retrieved latest comic JSON " + new String(responseBody));
            int comicNumber = jsonObject.getInt("num") - 1;
            Integer numberToRetrieve = downloadInput.getNumberToDownload();
            // if the numberToRetrieve is null, retrieve all of the comics.
            Integer numberOfComicsToRetrieve = (numberToRetrieve != null) ? numberToRetrieve - 1 : comicNumber;
            getOtherComic(comicNumber, numberOfComicsToRetrieve, comics);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't load comic", e);
            wasError = true;
        }

        return comics;
    }

    private Comic createComic(JSONObject jsonObject) throws JSONException {
        return new Comic(jsonObject.getString("title"), jsonObject.getString("transcript"), jsonObject.getString("img"), jsonObject.getInt("num"));
    }

    private void getOtherComic(final int number, final int numberToRetrieve, final List<Comic> comics) {
        if (numberToRetrieve == 0) {
            return;
        }
        String url = String.format(XKCD_URL_TEMPLATE, number);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            comics.add(createComic(jsonObject));
            Log.d(TAG, " >>>>> Retrieved latest comic JSON " + new String(responseBody));
            getOtherComic(number - 1, numberToRetrieve - 1, comics);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't load comic", e);
            wasError = true;
        }
    }
}
