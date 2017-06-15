package com.victoryze.musicplayer.mvp.contract;

import android.view.View;

import com.victoryze.musicplayer.mvp.base.BasePresenter;
import com.victoryze.musicplayer.mvp.base.BaseView;
import com.victoryze.musicplayer.mvp.model.Artist;

import java.util.List;

/**
 * Created by dsz on 17/6/15.
 */

public interface ArtistContract {
    interface View extends BaseView{
        void showArtists(List<Artist>artists);
        void showEmptyView();
    }

    interface  Prestenter extends BasePresenter<View>{
        void loadArtists(String action);
    }
}
