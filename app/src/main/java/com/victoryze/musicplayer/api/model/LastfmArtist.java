package com.victoryze.musicplayer.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;




/**
 * Created by dsz on 17/6/14.
 */

public class LastfmArtist {

    private static final String NAME="name";
    private static final String IMAGE="image";
    private static final String SIMILAR="similar";
    private static final String TAGS="tags";
    private static final String BIO="bio";

    @SerializedName(NAME)
    public String mName;

    @SerializedName(IMAGE)
    public List<Artwork>mArtwork;

    @SerializedName(SIMILAR)
    public SimilarArtist mSimilarArtist;

    @SerializedName(TAGS)
    public ArtistTags mArtistTag;

    @SerializedName(BIO)
    public  ArtistBio mArtistBio;


    public static class SimilarArtist{
        private static final String ARTIST="artist";

        @SerializedName(ARTIST)
        public List<LastfmArtist> mSimilarArtist;
    }

    public static class ArtistTags{
            private static final String TAG="tag";
            @SerializedName(TAG)
            public List<ArtistTag> mArtistTags;
    }

    public static class ArtistTag{

        private static final String NAME="name";
        private static final String URL="url";
        @SerializedName(NAME)
        public String mName;
        @SerializedName(URL)
        public String mUrl;
    }






}
