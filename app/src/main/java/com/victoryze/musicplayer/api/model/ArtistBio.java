package com.victoryze.musicplayer.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dsz on 17/6/14.
 */

public class ArtistBio {
    private static final String PUBLISHED="published";
    private static final String SUMMARY="summary";
    private static final String CONTENT="content";
    @SerializedName(PUBLISHED)
    public String mPublished;
    @SerializedName(SUMMARY)
    public String mSummary;
    @SerializedName(CONTENT)
    public String mContent;

}
