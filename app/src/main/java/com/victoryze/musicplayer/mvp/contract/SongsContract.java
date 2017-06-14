package com.victoryze.musicplayer.mvp.contract;

import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.mvp.base.BasePresenter;
import com.victoryze.musicplayer.mvp.base.BaseView;

import java.util.List;

/**
 * Created by dsz on 17/6/13.
 */

public interface SongsContract {
    interface View extends BaseView{
        void showSongs(List<Song> songList);
        void showEmptyView();
    }
    interface Presenter extends BasePresenter<View>{
        void loadSongs(String action);
    }
}
