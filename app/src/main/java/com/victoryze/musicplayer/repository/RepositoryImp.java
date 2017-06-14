package com.victoryze.musicplayer.repository;

import android.content.Context;

import com.victoryze.musicplayer.api.KuGouApiService;
import com.victoryze.musicplayer.api.LastFmApiService;
import com.victoryze.musicplayer.dataloader.SongLoader;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by dsz on 17/6/11.
 */

public class RepositoryImp implements Repository {

    private final Context mContext;

    private LastFmApiService mLastFmApiService;

    private KuGouApiService mKuGouApiService;

    public RepositoryImp(final Context context, Retrofit kugo, Retrofit lastfm) {
        mContext = context;
//        mKuGouApiService = kugo.create(KuGouApiService.class);
//        mLastFmApiService = lastfm.create(LastFmApiService.class);
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return SongLoader.getAllSongs(mContext);
    }

    @Override
    public Observable<List<Song>> getRecentlyPlayedSongs() {
        return null;
    }

    @Override
    public Observable<List<Song>> getRencentlyAddedSongs() {
        return null;
    }

    @Override
    public Observable<List<Song>> getTopPlaySongs() {
        return null;
    }

    @Override
    public Observable<List<Song>> getQueueSongs() {
        return null;
    }

    @Override
    public Observable<List<Song>> getFavoriteSongs() {
        return null;
    }
}
