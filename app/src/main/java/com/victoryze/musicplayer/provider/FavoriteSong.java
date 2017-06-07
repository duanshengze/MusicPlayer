package com.victoryze.musicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dsz on 17/6/7.
 */

public class FavoriteSong {
    private static volatile FavoriteSong sInstance=null;
    private MusicDB mMusicDatabase=null;

    private FavoriteSong(final Context context){
        mMusicDatabase=MusicDB.getInstance(context.getApplicationContext());
    }

    public static FavoriteSong getInstance(final Context context){
        if (sInstance==null){

            synchronized (FavoriteSong.class){
                if (sInstance==null){
                    sInstance=new FavoriteSong(context);
                }

            }
        }

        return sInstance;

    }

    public void onCreate(final SQLiteDatabase db){
        StringBuilder builder=new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(FavoriteSongColumns.NAME);
        builder.append("(");
        builder.append(FavoriteSongColumns.SONGID);
        builder.append(" LONG NOT NULLL,");
        builder.append(FavoriteSongColumns.TIMEADDED);
        builder.append(" LONG NOT NULL);");
        db.execSQL(builder.toString());
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteSongColumns.NAME);
        onCreate(db);
    }

    /**
     * 添加喜欢的歌曲id
     * @param songIds
     * @return
     */
    public int addFavoriteSong(final long[]songIds){
        final SQLiteDatabase database=mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        Cursor cursor=null;
        int insert=0;
        try {
            for (long songId:songIds){
                //避免重复插入，先查询是否存在
                cursor=database.query(FavoriteSongColumns.NAME,new String[]{

                  FavoriteSongColumns.SONGID
                },FavoriteSongColumns.SONGID+"=?",new String[]{
                      String.valueOf(songId)
                },null,null,null);

                if (cursor!=null&&cursor.getCount()==0){
                    ContentValues values=new ContentValues(2);
                    values.put(FavoriteSongColumns.SONGID,songId);
                    values.put(FavoriteSongColumns.TIMEADDED,System.currentTimeMillis());
                    database.insert(FavoriteSongColumns.NAME,null,values);
                    insert++;
                }
            }
             return  insert;
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }


    /**
     * 删除喜欢的歌曲
     * @param songIds
     * @return
     */
    public int removeFavoriteSong(final long[]songIds){
        final SQLiteDatabase database=mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        Cursor cursor=null;
        int deleted=0;
        try {
            for (long songId:songIds){
                cursor=database.query(FavoriteSongColumns.NAME,new String[]{FavoriteSongColumns.SONGID},

                        FavoriteSongColumns.SONGID+"=?",new String[]{String.valueOf(songId)},null,null,null
                        );
                if (cursor!=null&&cursor.getCount()>=0){
                    database.delete(FavoriteSongColumns.NAME,FavoriteSongColumns.SONGID+"=?",new String[]{
                            String.valueOf(songId)
                    });
                    deleted++;

                }

            }
            return  deleted;

        }finally {
            if (cursor!=null){
                cursor.close();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }

    }

    /**
     * 获取喜欢的曲目的游标
     * @return
     */
     public Cursor getFavoriteSongs(){
         final SQLiteDatabase database=mMusicDatabase.getReadableDatabase();
         Cursor cursor=database.query(FavoriteSongColumns.NAME,new String[]{FavoriteSongColumns.SONGID},null,
                 null,null,null,FavoriteSongColumns.TIMEADDED+" DESC",null);

         return cursor;
     }

    public boolean isFavorite(long songId){
        final SQLiteDatabase database=mMusicDatabase.getReadableDatabase();
        database.beginTransaction();

        Cursor cursor=null;

        try {
            cursor=database.query(FavoriteSongColumns.NAME,new String[]{

                    FavoriteSongColumns.SONGID
            },FavoriteSongColumns.SONGID+"=?",new String[]{String.valueOf(songId)},null,null,null);

            if (cursor!=null&&cursor.getCount()>0){

                return  true;
            }

        }finally {
            if (cursor!=null){
                cursor.close();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }
        return  false;


    }



    private interface FavoriteSongColumns{
        String NAME="favoritesong";
        String SONGID="songid";
        String TIMEADDED="timeadded";
    }







}
