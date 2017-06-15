package com.victoryze.musicplayer.mvp.contract;

import com.victoryze.musicplayer.mvp.base.BasePresenter;
import com.victoryze.musicplayer.mvp.base.BaseView;
import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.ui.activity.BaseActivity;

import java.util.List;

/**
 * Created by dsz on 17/6/15.
 */

public interface AlbumsContract {
    interface View extends BaseView{
        void showAblums(List<Album> ablumList);
        void showEmptyView();
    }

    interface Presenter extends BasePresenter<View>{
        void loadAblums(String action);
    }

}
