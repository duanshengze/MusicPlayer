package com.victoryze.musicplayer.provider;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dsz on 17/6/6.
 * 音乐数据库
 * 单例模式
 *
 */

public class MusicDB extends SQLiteOpenHelper {

    private static final String DATABASENAME="musicdb.db";

    private static final int VERSION=1;

    private static volatile MusicDB sInstance=null;

    private final Context mContext;

    private MusicDB(final Context context){
        super(context,DATABASENAME,null,VERSION);
        mContext=context;
    }


    public static MusicDB getInstance(final Context context){
        if (sInstance==null){

            synchronized (MusicDB.class){
                if (sInstance==null ){

                    //context.getApplicationContext()表示 该Context服务于整个应用，应用销毁它才销毁
                    sInstance=new MusicDB(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        FavoriteSong.getInstance(mContext).onCreate(database);
        MusicPlaybackState.getInstance(mContext).onCreate(database);
        RecentStore.getInstance(mContext).onCreate(database);
        SearchHistory.getInstance(mContext).onCreate(database);
        SongPlayCount.getInstance(mContext).onCreate(database);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FavoriteSong.getInstance(mContext).onUpgrade(db,oldVersion,newVersion);
        MusicPlaybackState.getInstance(mContext).onUpgrade(db,oldVersion,newVersion);
        RecentStore.getInstance(mContext).onUpgrade(db,oldVersion,newVersion);
        SearchHistory.getInstance(mContext).onUpgrade(db,oldVersion,newVersion);
        SongPlayCount.getInstance(mContext).onUpgrade(db,oldVersion,newVersion);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FavoriteSong.getInstance(mContext).onDowngrade(db,oldVersion,newVersion);
        MusicPlaybackState.getInstance(mContext).onDowngrade(db,oldVersion,newVersion);
        RecentStore.getInstance(mContext).onDowngrade(db,oldVersion,newVersion);
        SearchHistory.getInstance(mContext).onDowngrade(db,oldVersion,newVersion);
        SongPlayCount.getInstance(mContext).onDowngrade(db,oldVersion,newVersion);
    }
}
