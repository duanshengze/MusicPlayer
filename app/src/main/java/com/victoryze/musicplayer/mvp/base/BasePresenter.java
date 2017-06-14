package com.victoryze.musicplayer.mvp.base;

import com.victoryze.musicplayer.mvp.base.BaseView;

/**
 * Created by dsz on 17/6/13.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void subscribe();
    void unsubscribe();


}
