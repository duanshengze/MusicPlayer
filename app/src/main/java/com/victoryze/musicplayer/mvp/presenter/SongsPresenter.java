package com.victoryze.musicplayer.mvp.presenter;

import com.victoryze.musicplayer.mvp.contract.SongsContract;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.mvp.usecase.GetSongs;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by dsz on 17/6/13.
 */

public class SongsPresenter implements SongsContract.Presenter {
    private final GetSongs mGetSongs;
    private SongsContract.View mView;
    //用来取消所有的订阅
    private CompositeSubscription mCompositeSubscription;
    public SongsPresenter(final GetSongs getSongs) {
        mGetSongs=getSongs;
    }

    @Override
    public void attachView(SongsContract.View view) {
        mView=view;
        mCompositeSubscription=new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.clear();

    }

    @Override
    public void loadSongs(String action) {
        mCompositeSubscription.clear();
        final Subscription subscription=mGetSongs.execute(new GetSongs.RequestValues(action))
                .getListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        if (songs==null||songs.size()==0) {
                            mView.showEmptyView();
                        }else {
                            mView.showSongs(songs);
                        }
                    }
                });

        mCompositeSubscription.add(subscription);

    }
}
