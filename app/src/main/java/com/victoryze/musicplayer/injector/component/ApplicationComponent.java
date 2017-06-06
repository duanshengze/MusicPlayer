package com.victoryze.musicplayer.injector.component;

import android.app.Application;

import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.injector.module.ApplicationModule;
import com.victoryze.musicplayer.injector.module.NetworkModule;
import com.victoryze.musicplayer.injector.scope.PerApplication;

import javax.inject.Scope;

import dagger.Component;

/**
 * Created by dsz on 17/6/6.
 */
@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Application application();

    ListenerApp listenerApplication();

//    Repository repository();


}
