package com.wt.parth.xkcd_ver2.io.persistence.remote;

/**
 * Created by parth on 25/11/16.
 */
public class ComicDownloadInput {
    private final Integer startNumber;
    private final Integer numberToDownload;

    public ComicDownloadInput(Integer numberToDownload, Integer startNumber) {
        this.numberToDownload = numberToDownload;
        this.startNumber = startNumber;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public Integer getNumberToDownload() {
        return numberToDownload;
    }
}
