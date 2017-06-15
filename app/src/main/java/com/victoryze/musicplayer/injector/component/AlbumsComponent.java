package com.victoryze.musicplayer.injector.component;

import com.victoryze.musicplayer.injector.module.AlbumsModule;
import com.victoryze.musicplayer.injector.module.NetworkModule;
import com.victoryze.musicplayer.injector.scope.PerActivity;
import com.victoryze.musicplayer.mvp.usecase.GetAlbums;
import com.victoryze.musicplayer.ui.fragment.viewpager.AlbumsFragment;

import dagger.Component;


/**
 * Created by dsz on 17/6/15.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = { AlbumsModule.class})
public interface AlbumsComponent {
    void inject(AlbumsFragment fragment);
}
