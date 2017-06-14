package com.victoryze.musicplayer.injector.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.injector.scope.PerApplication;
import com.victoryze.musicplayer.repository.RepositoryImp;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dsz on 17/6/6.
 */
@Module
public class NetworkModule {
    private final ListenerApp mListenerApp;


    public NetworkModule(ListenerApp listenerApp){
        mListenerApp=listenerApp;
    }
    @PerApplication
    @Provides
    Repository provideRespoditory(){
        return new RepositoryImp(mListenerApp,null,null);
    }








}
