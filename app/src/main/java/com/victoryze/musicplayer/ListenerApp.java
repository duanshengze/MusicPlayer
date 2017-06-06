package com.victoryze.musicplayer;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.victoryze.musicplayer.injector.component.ApplicationComponent;
import com.victoryze.musicplayer.injector.component.DaggerApplicationComponent;
import com.victoryze.musicplayer.injector.module.ApplicationModule;
import com.victoryze.musicplayer.injector.module.NetworkModule;

/**
 * Created by dsz on 17/6/6.
 */

public class ListenerApp extends Application {
    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化注入器
        setupInjector();

        //初始化图片加载器
        initImageLoader();

    }

    private void setupInjector() {
       mApplicationComponent= DaggerApplicationComponent.builder()
                                .applicationModule(new ApplicationModule(this))
                                .networkModule(new NetworkModule(this)).build();

    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void initImageLoader(){
        ImageLoaderConfiguration localImageLoaderConfiguration=new ImageLoaderConfiguration.Builder(this).build();

        ImageLoader.getInstance().init(localImageLoaderConfiguration);


    }



}
