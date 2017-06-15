package com.victoryze.musicplayer.mvp.presenter;

import com.victoryze.musicplayer.mvp.contract.AlbumsContract;
import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.mvp.usecase.GetAlbums;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dsz on 17/6/15.
 */

public class AlbumsPresenter implements AlbumsContract.Presenter{

    private AlbumsContract.View mView;
    private GetAlbums mUsercase;
    private CompositeSubscription mCompositeSubscription;

    public AlbumsPresenter(GetAlbums getAlbums){
        mUsercase=getAlbums;
    }


    @Override
    public void attachView(AlbumsContract.View view) {
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
    public void loadAblums(String action) {
        mCompositeSubscription.clear();
        Subscription subscription=mUsercase.execute(new GetAlbums.RequestValues(action))
                .getListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Album>>() {
                    @Override
                    public void call(List<Album> albumList) {
                        if (albumList==null||albumList.size()==0){
                            mView.showEmptyView();
                        }else {
                            mView.showAblums(albumList);
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
