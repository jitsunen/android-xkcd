package com.wt.parth.xkcd_ver2.model;

/**
 * Created by parth on 20/11/16.
 */

public class Comic {
    private final int index;
    private final String url;
    private final String transcript;
    private final String title;

    public Comic() {
        this(null, null, null, 0);
    }

    public Comic(String title, String transcript, String url, int index) {
        this.title = title;
        this.transcript = transcript;
        this.url = url;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getUrl() {
        return url;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getTitle() {
        return title;
    }
}
