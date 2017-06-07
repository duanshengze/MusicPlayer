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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
