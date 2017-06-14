package com.victoryze.musicplayer.provider;

/**
 * Created by dsz on 17/6/6.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.victoryze.musicplayer.mvp.model.MusicPlaybackTrack;
import com.victoryze.musicplayer.util.ListenerUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

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
        builder.append(" INT NOT NULL)");
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
     * 将输入数据保存到两个表(旧数据被清除)
     * @param queue
     * @param history
     */
    public synchronized void saveState(final ArrayList<MusicPlaybackTrack>queue,
                                       LinkedList<Integer>history
                                       ){
        SQLiteDatabase database=mMusicDataBase.getWritableDatabase();
        database.beginTransaction();
        //删除旧数据
        try {
            database.delete(PlaybackQueueColumns.NAME,null,null);
            database.delete(PlaybackHistoryColumns.NAME,null,null);
            database.setTransactionSuccessful();
        }finally {
            database.endTransaction();
        }
        final int NUM_PROCESS=20;
        int position=0;
        while(position<queue.size()){
            database.beginTransaction();

            try{
                //一次最多批量插入NUM_PROCESS条记录
                for(int i=position;i<queue.size()&&i-position<NUM_PROCESS;i++){
                    MusicPlaybackTrack track=queue.get(i);
                    ContentValues values=new ContentValues(4);
                    values.put(PlaybackQueueColumns.TRACK_ID,track.mId);
                    values.put(PlaybackQueueColumns.SOURCE_ID,track.mSourceId);
                    values.put(PlaybackQueueColumns.SOURCE_TYPE,track.mSourceType.mId);
                    values.put(PlaybackQueueColumns.SOURCE_POSITION,track.mSourcePosition);
                   database.insert(PlaybackQueueColumns.NAME,null,values);
                }
                database.setTransactionSuccessful();
            }finally {
                database.endTransaction();
                position+=NUM_PROCESS;
            }
        }
        if(history!=null){
            Iterator<Integer>iter=history.iterator();
            while (iter.hasNext()){
                database.beginTransaction();
                try{
                    for(int i=0;iter.hasNext()&&i<NUM_PROCESS;i++){
                        ContentValues values=new ContentValues(1);
                        values.put(PlaybackHistoryColumns.POSITION,iter.next());
                        database.insert(PlaybackHistoryColumns.NAME,null,values);
                    }
                    database.setTransactionSuccessful();



                }finally {
                    database.endTransaction();
                }

            }
        }
    }

    /**
     * 获取playbackqueue表中的数据
     * @return
     */
    public ArrayList<MusicPlaybackTrack>getQueue(){
        ArrayList<MusicPlaybackTrack> results=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor=mMusicDataBase.getReadableDatabase().query(PlaybackQueueColumns.NAME,null,null,null,null,null,null);

            if (cursor!=null&&cursor.moveToFirst()){
                results.ensureCapacity(cursor.getCount());
                do{
                    long trackId=cursor.getLong(cursor.getColumnIndex(PlaybackQueueColumns.TRACK_ID));
                    long sourceId=cursor.getLong(cursor.getColumnIndex(PlaybackQueueColumns.SOURCE_ID));
                    int sourceTypeId=cursor.getInt(cursor.getColumnIndex(PlaybackQueueColumns.SOURCE_TYPE));
                    int sourcePosition=cursor.getInt(cursor.getColumnIndex(PlaybackQueueColumns.SOURCE_POSITION));

                    results.add(new MusicPlaybackTrack(trackId,sourceId,ListenerUtil.IdType.getTypeById(sourceTypeId),sourcePosition));

                }while (cursor.moveToNext());


            }

            return results;


        }finally {
            if (cursor!=null){
                cursor.close();
                cursor=null;
            }
        }

    }

    /**
     * 获取playbackhistory表中的数据
     * @param playlistSize
     * @return
     */
    public LinkedList<Integer>getHistory(final int playlistSize){

        LinkedList<Integer>results=new LinkedList<>();

        Cursor cursor=null;
        try {
            cursor=mMusicDataBase.getReadableDatabase().query(PlaybackHistoryColumns.NAME,null,null,null,null,null,null);
            if(cursor!=null&&cursor.moveToFirst()){

                do {
                    int pos=cursor.getInt(cursor.getColumnIndex(PlaybackHistoryColumns.POSITION));
                    if (pos>=0&&pos<playlistSize){
                        results.add(pos);
                    }
                }while (cursor.moveToNext());
            }
            return results;

        }finally {
            if(cursor!=null){
                cursor.close();
                cursor=null;
            }
        }







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
