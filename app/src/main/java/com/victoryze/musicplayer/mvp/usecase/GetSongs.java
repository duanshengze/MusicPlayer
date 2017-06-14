package com.victoryze.musicplayer.mvp.usecase;

import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.mvp.base.UseCase;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import java.util.List;

import rx.Observable;

/**
 * Created by dsz on 17/6/13.
 */

public class GetSongs extends UseCase<GetSongs.RequestValues,GetSongs.ResponseValue>{

    private final Repository mRepository;

    public GetSongs(Repository repository){
        mRepository=repository;
    }
    @Override
    public ResponseValue execute(RequestValues requestValues) {
        String action=requestValues.getAction();
        switch (action){
            case Constants.NAVIGATE_ALLSONG:
                return new ResponseValue(mRepository.getAllSongs());
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                return new ResponseValue(mRepository.getRecentlyPlayedSongs());
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                return new ResponseValue(mRepository.getRencentlyAddedSongs());
            case Constants.NAVIGATE_PALYLIST_TOPPLAYED:
                return  new ResponseValue(mRepository.getTopPlaySongs());
            case Constants.NAVIGATE_QUEUE:
                return  new ResponseValue(mRepository.getQueueSongs());
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                return new ResponseValue(mRepository.getFavoriteSongs());
            default:
                throw new RuntimeException("wrong acyion type");
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{
        private  final String action;
        public RequestValues(String action){
            this.action=action;
        }

        public String getAction() {
            return action;
        }
    }



    public static final class ResponseValue implements UseCase.ResponseValue {
        private final Observable<List<Song>> mListObservable;
        public ResponseValue(Observable<List<Song>>listObservable){
            mListObservable=listObservable;
        }

        public Observable<List<Song>> getListObservable() {
            return mListObservable;
        }
    }
}
