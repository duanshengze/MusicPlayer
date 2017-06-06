package com.victoryze.musicplayer.injector.module;

import android.app.Application;

import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.injector.scope.PerApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dsz on 17/6/6.
 */
@Module
public class ApplicationModule {
    private final ListenerApp mListenerApp;


    public ApplicationModule(ListenerApp listenerApp){
        mListenerApp=listenerApp;
    }

    @Provides
    @PerApplication
    public  ListenerApp provideListenerApp(){

        return  mListenerApp;
    }

    @Provides
    @PerApplication
    public Application provideApplication(){

        return  mListenerApp;
    }





}
