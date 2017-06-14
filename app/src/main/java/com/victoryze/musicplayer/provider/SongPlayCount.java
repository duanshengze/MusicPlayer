package com.victoryze.musicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by dsz on 17/6/7.
 * 统计个人播放音乐的次数表，通过该表来展示用户对歌曲的喜好
 * 采用单例模式
 */

public class SongPlayCount {

    private static final int NUM_WEEKS = 52;

    private static volatile SongPlayCount sInstance = null;

    private static Interpolator sInterpolator = new AccelerateInterpolator(1.5f);

    private static int INTERPOLATOR_HEIGHT = 50;
    private static int INTERPOLATOR_BASE = 25;


    private MusicDB mMusicDataBase = null;

    private static int ONE_WEEK_IN_MS = 1000 * 60 * 60 * 24 * 7;
    //从时间起点开始，有多少周
    private int mNumberOfWeekSinceEpoch;
    //用于更新数据库更新
    private boolean mDatabaseUpdated;


    private SongPlayCount(final Context context) {
        mMusicDataBase = MusicDB.getInstance(context);
        long msSinceEpoch = System.currentTimeMillis();
        mNumberOfWeekSinceEpoch = (int) (msSinceEpoch / ONE_WEEK_IN_MS);
        mDatabaseUpdated = false;
    }

    public static SongPlayCount getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (SongPlayCount.class) {
                if (sInstance == null) {
                    sInstance = new SongPlayCount(context);
                }
            }
        }

        return sInstance;

    }


    public void onCreate(final SQLiteDatabase db) {

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(SongPlayCountColumns.NAME);
        builder.append("(");
        builder.append(SongPlayCountColumns.ID);
        builder.append(" INT UNIQUE,");

        for (int i = 0; i < NUM_WEEKS; i++) {
            builder.append(getColumnNameForWeek(i));
            builder.append(" INT DEFAULT 0,");
        }
        builder.append(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
        builder.append(" INT NOT NULL,");
        builder.append(SongPlayCountColumns.PLAYCOUNTSCORE);
        builder.append(" REAL DEFAULT 0);");

        db.execSQL(builder.toString());

    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果降级，选择删除原表并重新创建
        db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
        onCreate(db);
    }


    public void bumpSongCount(final long songId) {
        if (songId < 0) {
            return;
        }
        final SQLiteDatabase database = mMusicDataBase.getWritableDatabase();
        updateExistingRow(database, songId, true);
    }

    /**
     * 更新歌曲的播放计数和评分
     *
     * @param database  可写的数据库
     * @param songId    歌曲id
     * @param bumpCount 是否将当前的week的播放统计加1，并更新分数
     */
    private void updateExistingRow(SQLiteDatabase database, long songId, boolean bumpCount) {
        String songIdstr = String.valueOf(songId);

        //开启事务
        database.beginTransaction();
        //必须查询所有的列属性
        Cursor cursor = database.query(SongPlayCountColumns.NAME, null, SongPlayCountColumns.ID + "=?",

                new String[]{songIdstr}, null, null, null
        );
        if (cursor != null & cursor.moveToFirst()) {
            int lastUpatedIndex = cursor.getColumnIndex(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
            int lastUpdateWeek = cursor.getInt(lastUpatedIndex);
            int weekDiff = mNumberOfWeekSinceEpoch - lastUpdateWeek;

            //如果相差的周数大于NUM_WEEKS,则删除，并重新创建一条记录
            if (Math.abs(weekDiff) >= NUM_WEEKS) {
                //因为记录太超时，需要删除
                deleteEntry(database, songIdstr);
                if (bumpCount) {
                    createNewPlayedEntry(database, songId);
                }
            } else if (weekDiff != 0) {

                int[] palyCounts = new int[NUM_WEEKS];
                if (weekDiff > 0) {
                    for (int i = 0; i < NUM_WEEKS - weekDiff; i++) {
                        //将所有的周播放数后移weekDiff列
                        palyCounts[i + weekDiff] = cursor.getInt(getColumnIndexForWeek(i));

                    }

                } else if (weekDiff < 0) {
                    for (int i = 0; i < NUM_WEEKS + weekDiff; i++) {
                        palyCounts[i] = cursor.getInt(getColumnIndexForWeek(i - weekDiff));
                    }
                }

                if(bumpCount){
                    palyCounts[0]++;//最近第一周播放次数加1
                }

                float score=calculateScore(palyCounts);
                //分数小于0.01 则删除该歌曲的记录
                if (score<.01f){
                    deleteEntry(database,songIdstr);
                }else {
                    //大于0.01则更新该记录
                    ContentValues values=new ContentValues(NUM_WEEKS+2);
                    values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX,mNumberOfWeekSinceEpoch);
                    values.put(SongPlayCountColumns.PLAYCOUNTSCORE,score);
                    for (int i=0;i<NUM_WEEKS;i++){
                        values.put(getColumnNameForWeek(i),palyCounts[i]);
                    }
                    database.update(SongPlayCountColumns.NAME,values,SongPlayCountColumns.ID+"=?",new String[]{songIdstr});
                }
            //如果没有偏移，仅仅更新分数
            }else if (bumpCount){
                ContentValues values=new ContentValues(2);
                int scoreIndex=cursor.getColumnIndex(SongPlayCountColumns.PLAYCOUNTSCORE);
                float score=cursor.getFloat(scoreIndex)+getScoreMultipliterForWeek(0);
                values.put(SongPlayCountColumns.PLAYCOUNTSCORE,score);
                values.put(getColumnNameForWeek(0),cursor.getInt(getColumnIndexForWeek(0))+1);
                database.update(SongPlayCountColumns.NAME,values,SongPlayCountColumns.ID+"=?",new String[]{songIdstr});
            }
            cursor.close();
        //如果查询不到结果，则创建新的记录
        }else if(bumpCount){

            createNewPlayedEntry(database,songId);

        }

        database.setTransactionSuccessful();
        database.endTransaction();

    }

    /**
     * 获得一个Cursor 含有播放最高的歌曲记录，注意：只返回NUM_WEEKS内播放的
     * @param numResults 如果<=0则返回全部，则返回numResults条记录
     * @return
     */
    public Cursor getTopPlayedResult(int numResults){
        //先更新记录删除过时的
        updateResults();
        final SQLiteDatabase database=mMusicDataBase.getReadableDatabase();
        return database.query(SongPlayCountColumns.NAME,new String[]{SongPlayCountColumns.ID,SongPlayCountColumns.PLAYCOUNTSCORE}
        ,null,null,null,null,SongPlayCountColumns.PLAYCOUNTSCORE+" DESC",(numResults<=0?null:String.valueOf(numResults)));

    }

    /**
     * 删除太长时间没有播放的歌曲记录，并重新计算得分
     */
    private synchronized void updateResults() {
        if (mDatabaseUpdated){
            return;
        }

        final SQLiteDatabase database=mMusicDataBase.getWritableDatabase();
        database.beginTransaction();
        int oldestWeekWeCareAbout=mNumberOfWeekSinceEpoch-NUM_WEEKS+1;
        database.delete(SongPlayCountColumns.NAME,SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX+"< ?",
                new String[]{String.valueOf(oldestWeekWeCareAbout)}
                );

        Cursor cursor=database.query(SongPlayCountColumns.NAME,new String[]{SongPlayCountColumns.ID},null
        ,null,null,null,null
        );

        if (cursor!=null&&cursor.moveToFirst()){
            do {
                updateExistingRow(database,cursor.getLong(cursor.getColumnIndex(SongPlayCountColumns.ID)),false);
            }while (cursor.moveToNext());

            cursor.close();
            cursor=null;
        }

        mDatabaseUpdated=true;
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /**
     *
     * @param songId 需要被删除的歌曲id
     */
    public void removeItem(final  long songId){

        final SQLiteDatabase database=mMusicDataBase.getWritableDatabase();
        deleteEntry(database,String.valueOf(songId));

    }

    /**
     * 通过所给的播放次数计算分数
     * @param palyCounts
     * @return
     */
    private static float calculateScore(int[] palyCounts) {
        if (palyCounts==null){
            return 0;
        }
        float score=0;
        for (int i=0;i<Math.min(palyCounts.length,NUM_WEEKS);i++){
            score+=palyCounts[i]*getScoreMultipliterForWeek(i);
        }
        return score;

    }


    /**
     * 为了或读取性能采用静态方法，返回也高静态值，week在查询数据中的一个列索引
     * WARNING：只有确定查询所有的列项才有效
     *
     * @param week
     * @return
     */
    private static int getColumnIndexForWeek(int week) {
        return week + 1;
    }

    /**
     * 创建一条记录表明，这首歌已经被播放并且评分了
     *
     * @param database
     * @param songId
     */

    private void createNewPlayedEntry(SQLiteDatabase database, long songId) {
        //如记录存在，创建一个新的分数
        float newScore = getScoreMultipliterForWeek(0);
        int newPlayCounnt = 1;
        final ContentValues values = new ContentValues(3);
        values.put(SongPlayCountColumns.ID, songId);
        values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeekSinceEpoch);
        values.put(getColumnNameForWeek(0), newPlayCounnt);
        database.insert(SongPlayCountColumns.NAME, null, values);
    }


    /**
     * 为每一周获取一个分数的乘数
     *
     * @param week
     * @return
     */
    private static float getScoreMultipliterForWeek(int week) {
        return sInterpolator.getInterpolation(1 - (week / (float) NUM_WEEKS)) * INTERPOLATOR_HEIGHT + INTERPOLATOR_BASE;
    }

    /**
     * 删除记录
     *
     * @param database
     * @param songId
     */
    private void deleteEntry(SQLiteDatabase database, String songId) {
        database.delete(SongPlayCountColumns.NAME, SongPlayCountColumns.ID + "=?", new String[]{songId});
    }


    private static String getColumnNameForWeek(int week) {

        return SongPlayCountColumns.WEEK_PLAY_COUNT + String.valueOf(week);

    }


    public interface SongPlayCountColumns {

        /**
         * 表名
         */
        String NAME = "songplaycount";
        /**
         * 歌曲id
         */
        String ID = "songid";
        /**
         * 周统计
         */
        String WEEK_PLAY_COUNT = "week";

        /**
         * 周指数
         */
        String LAST_UPDATED_WEEK_INDEX = "weekindex";

        /**
         * 播放分
         */
        String PLAYCOUNTSCORE = "playcountscore";

    }
}
