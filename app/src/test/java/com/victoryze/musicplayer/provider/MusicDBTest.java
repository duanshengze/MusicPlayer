package com.victoryze.musicplayer.provider;


import android.database.Cursor;
import android.os.Build;

import com.victoryze.musicplayer.BuildConfig;
import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.mvp.model.MusicPlaybackTrack;
import com.victoryze.musicplayer.util.ListenerUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by dsz on 17/6/8.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,manifest = Config.NONE,sdk = Build.VERSION_CODES.JELLY_BEAN, application = ListenerApp.class)
public class MusicDBTest {
    @Before
    public void setUp(){

    }

    @Test
    public void testFavoriteSong(){
        FavoriteSong favoriteSong=FavoriteSong.getInstance(RuntimeEnvironment.application);
        //测试 isFavorite
        favoriteSong.addFavoriteSong(new long[]{112223,11115});
        Assert.assertEquals(favoriteSong.getFavoriteSongs().getCount(),2);
        boolean favorite=favoriteSong.isFavorite(11115);
        Assert.assertEquals(favorite,true);
        //测试 removeFavoriteSong
        int count=favoriteSong.removeFavoriteSong(new long[]{11115});
        Assert.assertEquals(count,1);

    }
    @Test
    public void testMusicPlaybackState(){
        MusicPlaybackState musicPlaybackState=MusicPlaybackState.getInstance(RuntimeEnvironment.application);
        ArrayList<MusicPlaybackTrack>musicPlaybackTracks=new ArrayList<>();
        LinkedList<Integer> history=new LinkedList<>();


        for(int i=0;i<40;i++){
            MusicPlaybackTrack track=new MusicPlaybackTrack(110+i,111+i, ListenerUtil.IdType.getTypeById(1),i);
            musicPlaybackTracks.add(track);
            history.add(111+i);
        }


        musicPlaybackState.saveState(musicPlaybackTracks,history);
        Assert.assertEquals(musicPlaybackState.getQueue().size(),40);
        Assert.assertEquals(musicPlaybackState.getHistory(151).size(),40);
        System.out.println(musicPlaybackState.getQueue());
        System.out.println(musicPlaybackState.getHistory(150));

    }

    @Test
    public void testRecentStore(){
        RecentStore recentStore=RecentStore.getInstance(RuntimeEnvironment.application);

        for(int i=0;i<1000;i++){
            recentStore.addSongId(i+110);
        }
        Cursor cursor=recentStore.queryRecentIds(1000+"");
        cursor.moveToFirst();
        Assert.assertEquals(cursor.getCount(),100);
        Assert.assertEquals(cursor.getLong(0),1109);

    }


    @Test
    public void testSearchHistory(){

        SearchHistory searchHistory=SearchHistory.getInstance(RuntimeEnvironment.application);
        for (int i=0;i<100;i++){
            searchHistory.addSearchString("history"+i);
        }
        Cursor cursor=searchHistory.queryRecentSearches("100");
        Assert.assertEquals(cursor.getCount(),25);
        cursor.moveToFirst();
        Assert.assertEquals(cursor.getString(0),"history"+99);
    }


    @Test
    public void testSongPlayCount(){
        SongPlayCount songPlayCount=SongPlayCount.getInstance(RuntimeEnvironment.application);
        for(int i=0;i<100;i++){
            songPlayCount.bumpSongCount(110+i);
        }

        for (int i=0;i<10;i++){
            songPlayCount.bumpSongCount(111);
        }

        Cursor cursor=songPlayCount.getTopPlayedResult(-1);
        cursor.moveToFirst();
//        Assert.assertEquals(cursor.getCount(),100);
        Assert.assertEquals(cursor.getLong(0),111);

        System.out.print(cursor.getDouble(1));

        songPlayCount.removeItem(111);

        Cursor cursor1=songPlayCount.getTopPlayedResult(-1);
        cursor1.moveToFirst();
        Assert.assertEquals(cursor1.getLong(0),111);


    }


}