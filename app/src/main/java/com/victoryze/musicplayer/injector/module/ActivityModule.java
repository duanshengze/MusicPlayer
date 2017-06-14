package com.victoryze.musicplayer.injector.module;

import android.app.Activity;
import android.content.Context;

import com.victoryze.musicplayer.injector.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dsz on 17/6/14.
 */
@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }
    @Provides
    @PerActivity
    public Context provideContext(){
        return mActivity;
    }
}
