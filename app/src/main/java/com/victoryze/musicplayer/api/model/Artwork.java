package com.victoryze.musicplayer.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dsz on 17/6/14.
 */

public class Artwork {

    private static final String URL="#text";
    private static final String SIZE="size";

    @SerializedName(URL)
    public String mUrl;

    @SerializedName(SIZE)
    public String mSize;
}
