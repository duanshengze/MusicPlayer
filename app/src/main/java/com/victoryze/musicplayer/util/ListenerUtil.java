package com.victoryze.musicplayer.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.view.View;

/**
 * Created by dsz on 17/6/6.
 */

public class ListenerUtil {

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static Uri getAlbumArtUri(long albumId){

        return  ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isRtl(Resources res) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) &&
                (res.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }


    public static final String makeLabel(final Context context, final int pluralInt,
                                         final int number) {
        return context.getResources().getQuantityString(pluralInt, number, number);
    }
    public enum IdType{
        NA(0),
        Artist(1),
        Album(2),
        Playlist(3),
        Folder(4)
        ;
        public  final int mId;
        IdType(int id){
            mId=id;
        }

        public static IdType getTypeById(int id){


            for (IdType type:values()){
                if (type.mId==id){
                    return type;
                }
            }

            throw  new IllegalArgumentException("Unrecognized id:"+id);
        }
    }
}
