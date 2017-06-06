package com.victoryze.musicplayer.util;

/**
 * Created by dsz on 17/6/6.
 */

public class ListenerUtil {


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
