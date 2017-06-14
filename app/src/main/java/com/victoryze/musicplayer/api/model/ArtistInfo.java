package com.victoryze.musicplayer.api.model;

import com.google.gson.annotations.SerializedName;



/**
 * Created by dsz on 17/6/14.
 */

public class ArtistInfo {
    private static final String ARTIST="artist";

    @SerializedName(ARTIST)
    public LastfmArtist mArtist;
}
