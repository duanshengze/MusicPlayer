package com.victoryze.musicplayer.util;

import android.provider.MediaStore;

/**
 * Created by dsz on 17/6/11.
 */

public class SortOrder {
    /**
     * 这个类不被实例化
     */
    public SortOrder() {

    }


    public interface AlumSortOrder {
        /**
         * Album sort order a-z
         */
        String ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;
        /**
         * Album sort order z-a
         */
        String ALBUM_Z_A = ALBUM_A_Z + " DESC";

        String ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

    }

    public interface SongSortOrder {
        String SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        /*Song sort order Z-A
         */
        String SONG_Z_A = SONG_A_Z + " DESC";
        /*
         Song sort order artist
         */
        String SONG_ARTIST=MediaStore.Audio.Media.ARTIST;

        /*Song sort order album*/
        String SONG_ALBUM=MediaStore.Audio.Media.ALBUM;

        /*Song sort order duration*/
        String SONG_DURATION=MediaStore.Audio.Media.DURATION+" DESC";

        /*Song sort order date*/
        String SONG_DATE=MediaStore.Audio.Media.DATE_ADDED+" DESC";


        /*Song sort order filename*/
        String SONG_FILENAME=MediaStore.Audio.Media.DATA;








    }


}
