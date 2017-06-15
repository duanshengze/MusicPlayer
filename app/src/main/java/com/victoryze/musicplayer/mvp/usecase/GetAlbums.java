package com.victoryze.musicplayer.mvp.usecase;

import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.mvp.base.UseCase;
import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import java.util.List;

import rx.Observable;

/**
 * Created by dsz on 17/6/15.
 */

public class GetAlbums extends UseCase<GetAlbums.RequestValues,GetAlbums.ResponseValue> {

    private final Repository mRepository;

    public GetAlbums(Repository repository) {
        mRepository = repository;
    }

    @Override
    public ResponseValue execute(RequestValues requestValues) {
        String action=requestValues.getAction();
        switch (action){
            case Constants.NAVIGATE_ALLSONG:
                return new ResponseValue(mRepository.getAllAlbums());
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                return new ResponseValue(mRepository.getRecentlyAddedAlbums());
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                return  new ResponseValue(mRepository.getRencentlyPlayedAlbums());
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                return  new ResponseValue(mRepository.getFavoriteAlbums());
            default:
                throw  new RuntimeException("wraong action type");
        }

    }

    public static final class RequestValues implements UseCase.RequestValues{
        private final String action;

        public RequestValues(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }
    }
    public static final class ResponseValue implements UseCase.ResponseValue{

        private final Observable<List<Album>> mListObservable;

        public ResponseValue(Observable<List<Album>> listObservable) {
            mListObservable = listObservable;
        }

        public Observable<List<Album>> getListObservable() {
            return mListObservable;
        }
    }
}
