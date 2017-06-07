package com.victoryze.musicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by dsz on 17/6/7.
 * <p>
 * 记录最近播放的歌曲信息
 * <p>
 * 采用单例模式
 */

public class RecentStore {

    private MusicDB mMusicDatabase = null;

    private final int MAX_ITEMS_IN_DB=100;

    private static volatile RecentStore sInstance = null;

    private RecentStore(final Context context) {
        mMusicDatabase = MusicDB.getInstance(context);
    }

    public static RecentStore getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (RecentStore.class) {
                if (sInstance == null) {
                    sInstance = new RecentStore(context);
                }
            }
        }
        return sInstance;
    }

    public void onCreate(final SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(RecentStoreColumns.NAME);
        builder.append("(");
        builder.append(RecentStoreColumns.ID);
        builder.append(" LONG NOT NULL,");
        builder.append(RecentStoreColumns.TIMEPLAYED);
        builder.append(" LONG NOT NULL);");
        db.execSQL(builder.toString());
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecentStoreColumns.NAME);
        onCreate(db);
    }

    /**
     * 将新的播放曲目id插入表中，并保持总记录数在100 条
     *
     * @param songId
     */
    public void addSongId(final long songId) {
        final SQLiteDatabase database=mMusicDatabase.getWritableDatabase();
        //开启事务
        database.beginTransaction();
        try {
            Cursor mostRecentItem=null;
            try {
                mostRecentItem=queryRecentIds("1");
                if (mostRecentItem!=null&&mostRecentItem.moveToFirst()){
                    if (songId==mostRecentItem.getLong(0)){
                        return;
                    }
                }
            }finally {
                if (mostRecentItem!=null){
                    mostRecentItem.close();
                    mostRecentItem=null;
                }
            }

            final ContentValues values=new ContentValues(2);
            values.put(RecentStoreColumns.ID,songId);
            values.put(RecentStoreColumns.TIMEPLAYED, System.currentTimeMillis());
            database.insert(RecentStoreColumns.NAME,null,values);

            Cursor oldest=null;
            try{
                oldest=database.query(RecentStoreColumns.NAME,new String[]{RecentStoreColumns.TIMEPLAYED}

                ,null,null,null,null,RecentStoreColumns.TIMEPLAYED+" ASC");

                if (oldest!=null&&oldest.getCount()>MAX_ITEMS_IN_DB){
                    oldest.moveToPosition(oldest.getCount()-MAX_ITEMS_IN_DB);
                    long timeOfRecordToKeep=oldest.getLong(0);
                    database.delete(RecentStoreColumns.NAME,RecentStoreColumns.TIMEPLAYED+"< ?",
                            new String[]{String.valueOf(timeOfRecordToKeep)});

                }


            }finally {
                if(oldest!=null){
                    oldest.close();
                    oldest=null;
                }
            }

        }finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }





    }

    /**
     * 获取最近播放的n首歌曲的id
     *
     * @param limit
     * @return
     */
    public Cursor queryRecentIds(final String limit) {
        final SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        //DESC从大到小
        return database.query(RecentStoreColumns.NAME, new String[]{RecentStoreColumns.ID}, null
                , null, null, null, RecentStoreColumns.TIMEPLAYED+" DESC", limit);

    }


    private interface RecentStoreColumns {
        //表名
        String NAME = "recenthistory";
        //
        String ID = "songid";

        String TIMEPLAYED = "timeplayed";


    }


}
