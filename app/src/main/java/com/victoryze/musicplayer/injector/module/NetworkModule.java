package com.victoryze.musicplayer.injector.module;

import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.injector.scope.PerApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dsz on 17/6/6.
 */
@Module
public class NetworkModule {
    private final ListenerApp mListenerApp;


    public NetworkModule(ListenerApp listenerApp){
        mListenerApp=listenerApp;
    }


}
