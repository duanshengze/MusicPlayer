package com.victoryze.musicplayer.provider;

/**
 * Created by dsz on 17/6/6.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 追踪音乐播放
 * 记录音乐播放服务的历史状态
 * 采用单例模式
 */
public class MusicPlaybackState {
    private static volatile MusicPlaybackState sInstance=null;

    private  MusicDB mMusicDataBase=null;

    private  MusicPlaybackState(final Context context){
        mMusicDataBase=MusicDB.getInstance(context);
    }

    public  static MusicPlaybackState getInstance(final Context context){
        if (sInstance==null){
            synchronized (MusicPlaybackState.class){
                if (sInstance==null){

                    sInstance=new MusicPlaybackState(context.getApplicationContext());


                }


            }


        }
        return sInstance;
    }

    /**
     * 创建相关数据库表
     * 主要为
     * @param db
     */
    public void onCreate(final SQLiteDatabase db){
        //创建播放队列表
        StringBuilder builder=new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(PlaybackQueueColumns.NAME);
        builder.append("(");
        builder.append(PlaybackQueueColumns.TRACK_ID);
        builder.append(" LONG NOT NULL,");
        builder.append(PlaybackQueueColumns.SOURCE_ID);
        builder.append(" LONG NOT NULL,");
        builder.append(PlaybackQueueColumns.SOURCE_TYPE);
        builder.append(" LONG NOT NULL,");
        builder.append(PlaybackQueueColumns.SOURCE_POSITION);
        builder.append(" LONG NOT NULL);");
        db.execSQL(builder.toString());

        //创建播放历史表

        builder=new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(PlaybackHistoryColumns.NAME);
        builder.append("(");
        builder.append(PlaybackHistoryColumns.POSITION);
        builder.append(" INT NOT NULL");
        db.execSQL(builder.toString());
    }


    /**
     * 当版本号发生升高变化时调用此方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(final SQLiteDatabase db,final  int oldVersion,final int newVersion){
        if (oldVersion<2&&newVersion>=2){
            onCreate(db);
        }
    }
    /**
     * 当版本号发生降级变化时调用此方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    public void onDowngrade(final SQLiteDatabase db,final int oldVersion,final  int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+PlaybackQueueColumns.NAME);
        db.execSQL("DROP TABLE IF EXISTS "+PlaybackHistoryColumns.NAME);
        onCreate(db);
    }



    /**
     * 播放队列 表的相关属性名
     */
    private interface PlaybackQueueColumns{
        String NAME="playbackqueue";

        String TRACK_ID="trackid";
        String SOURCE_ID="sourceid";
        String SOURCE_TYPE="sourcetype";
        String SOURCE_POSITION="sourceposition";
    }
    /**
     * 播放历史 表的相关属性名
     */

    private interface  PlaybackHistoryColumns{
        String NAME="playbackhistory";

        String POSITION="position";


    }

}
