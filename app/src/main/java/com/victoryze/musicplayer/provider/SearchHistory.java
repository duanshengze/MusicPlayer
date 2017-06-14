package com.victoryze.musicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dsz on 17/6/7.
 * 搜索记录数据表
 * 单例模式
 */

public class SearchHistory {
    private static final int MAX_ITEM_IN_DB = 25;
    private static volatile SearchHistory sInstance = null;

    private MusicDB mMusicDatabase = null;

    private SearchHistory(final Context context) {
        mMusicDatabase = MusicDB.getInstance(context.getApplicationContext());
    }

    public static SearchHistory getInstance(final Context context) {

        if (sInstance == null) {
            synchronized (SearchHistory.class) {
                if (sInstance == null) {
                    sInstance = new SearchHistory(context);
                }

            }

        }
        return sInstance;
    }


    public void onCreate(final SQLiteDatabase db) {

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(SearchHistoryColumns.NAME);
        builder.append("(");
        builder.append(SearchHistoryColumns.SEARCHSTRING);
        builder.append(" STRING NOT NULL,");
        builder.append(SearchHistoryColumns.TIMESEARCHED);
        builder.append(" LONG NOT NULL);");
        db.execSQL(builder.toString());

    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryColumns.NAME);
        onCreate(db);
    }

    /**
     * 添加搜索记录，并删除溢出记录
     *
     * @param searchString
     */
    public void addSearchString(final String searchString) {
        if (searchString == null) {
            return;
        }

        String trimStr = searchString.trim();
        if (trimStr.isEmpty()) {
            return;
        }
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();

        database.beginTransaction();
        try {

            database.delete(SearchHistoryColumns.NAME, SearchHistoryColumns.SEARCHSTRING
                    + "= ? COLLATE NOCASE", new String[]{trimStr}
            );
            final ContentValues values=new ContentValues(2);
            values.put(SearchHistoryColumns.SEARCHSTRING,trimStr);
            values.put(SearchHistoryColumns.TIMESEARCHED,System.currentTimeMillis());
            database.insert(SearchHistoryColumns.NAME,null,values);

            Cursor oldest=null;
            try {
                oldest=database.query(SearchHistoryColumns.NAME,new String[]{
                        SearchHistoryColumns.TIMESEARCHED

                },null,null,null,null,SearchHistoryColumns.TIMESEARCHED+" ASC");
                if (oldest!=null&&oldest.getCount()>MAX_ITEM_IN_DB){
                    oldest.moveToPosition(oldest.getCount()-MAX_ITEM_IN_DB);
                    long timeOfRecordToKeep=oldest.getLong(0);
                    database.delete(SearchHistoryColumns.NAME,SearchHistoryColumns.TIMESEARCHED+"< ?",
                          new String[]{  String.valueOf(timeOfRecordToKeep)});
                }
            }finally {
                if (oldest!=null){
                    oldest.close();
                    oldest=null;
                }
            }

        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();

        }
    }

    /**
     * 获取最近搜索的n条记录
     * @param limit
     * @return
     */
    public Cursor queryRecentSearches(final String limit){

        final SQLiteDatabase database=mMusicDatabase.getReadableDatabase();
        return database.query(SearchHistoryColumns.NAME,
                new String[]{SearchHistoryColumns.SEARCHSTRING},null,null,null,null,
                SearchHistoryColumns.TIMESEARCHED+" DESC",limit
                );
    }


    private interface SearchHistoryColumns {
        String NAME = "searchhistory";
        String SEARCHSTRING = "searchstring";
        String TIMESEARCHED = "timesearched";


    }


}
