package com.victoryze.musicplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 * Created by dsz on 17/6/11.
 * SharedPreferences的工具类
 * 使用单例模式
 *
 */

public class PreferencesUtility {
    //key
    private static final String ALBUM_SORT_ORDER="album_sort_order";

    private static final String SONG_SORT_ORDER="song_sort_order";

    private static final String START_PAGE_INDEX = "start_page_index";

    private static final String TOGGLE_ALBUM_GRID ="toggle_album_grid";

    private static volatile PreferencesUtility sInstance;

    private static SharedPreferences mPreferences;
    private PreferencesUtility(final Context context){
        mPreferences= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static PreferencesUtility getInstance(Context context){
        if (sInstance==null){
            synchronized (PreferencesUtility.class){

                if (sInstance==null){
                    sInstance=new PreferencesUtility(context);
                }

            }

        }
        return sInstance;
    }


    private void setSortOrder(final String key,final String value){
        final SharedPreferences.Editor editor=mPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public final void setAlbumSortOrder(final String value){
        setSortOrder(ALBUM_SORT_ORDER,value);
    }

    public final String getAlbumSortOrder(){
        return mPreferences.getString(ALBUM_SORT_ORDER,SortOrder.AlumSortOrder.ALBUM_A_Z);
    }

    public final String getSongSortOrder(){
        return  mPreferences.getString(SONG_SORT_ORDER,SortOrder.SongSortOrder.SONG_A_Z);
    }

    public final void setSongSortOrder(String value){
        setSortOrder(SONG_SORT_ORDER,value);
    }

    /**
     * 设置viewpager所指的索引index用于后期恢复
     * @param index
     */
    public final void setStartPageIndex(final int index){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.putInt(START_PAGE_INDEX,index);
                editor.apply();
                return null;
            }
        }.execute();
    }

    public final int getStartPageIndex(){
        return mPreferences.getInt(START_PAGE_INDEX,0);
    }


    public boolean isAlbumsGrid() {
        return mPreferences.getBoolean(TOGGLE_ALBUM_GRID,true);
    }

    public void setAlbumsInGrid(final  boolean b){
        final SharedPreferences.Editor editor=mPreferences.edit();
        editor.putBoolean(TOGGLE_ALBUM_GRID,b).apply();
    }
}
