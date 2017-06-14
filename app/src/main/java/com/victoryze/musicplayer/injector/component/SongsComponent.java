package com.victoryze.musicplayer.injector.component;

import com.victoryze.musicplayer.injector.module.ActivityModule;
import com.victoryze.musicplayer.injector.module.SongsModule;
import com.victoryze.musicplayer.injector.scope.PerActivity;
import com.victoryze.musicplayer.ui.fragment.viewpager.SongsFragment;

import dagger.Component;

/**
 * Created by dsz on 17/6/14.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class, SongsModule.class})
public interface SongsComponent {
    void inject(SongsFragment songsFragment);
}
