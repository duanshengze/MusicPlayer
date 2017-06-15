package com.victoryze.musicplayer.repository.interfaces;

import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.mvp.model.Song;

import java.util.List;

import rx.Observable;

/**
 * Created by dsz on 17/6/11.
 */

public interface Repository {
    /**
     * 获取所有的歌曲
     * @return
     */
    Observable<List<Song>> getAllSongs();

    /**
     * 获取最近的播放的歌曲
     * @return
     */
    Observable<List<Song>> getRecentlyPlayedSongs();

    /**
     * 获取最近添加的歌曲
     * @return
     */

    Observable<List<Song>> getRencentlyAddedSongs();

    /**
     * 获取播放排名靠前的歌曲
     * @return
     */

    Observable<List<Song>> getTopPlaySongs();

    /**
     * 获取播放列表的歌曲
     * @return
     */
    Observable<List<Song>> getQueueSongs();

    /**
     * 获取我喜欢的歌曲
     * @return
     */
    Observable<List<Song>> getFavoriteSongs();


    /**
     * 获得所有的专辑
     * @return
     */
    Observable<List<Album>> getAllAlbums();

    Observable<List<Album>> getRecentlyAddedAlbums();

    Observable<List<Album>> getRencentlyPlayedAlbums();

    Observable<List<Album>> getFavoriteAlbums();
}
