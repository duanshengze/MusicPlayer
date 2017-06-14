package com.victoryze.musicplayer.injector.module;

import com.victoryze.musicplayer.mvp.contract.SongsContract;
import com.victoryze.musicplayer.mvp.presenter.SongsPresenter;
import com.victoryze.musicplayer.mvp.usecase.GetSongs;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dsz on 17/6/14.
 */
@Module
public class SongsModule {
    @Provides
    SongsContract.Presenter getSongsPresenter(GetSongs getSongs){
        return new SongsPresenter(getSongs);
    }


    @Provides
    GetSongs getSongsUseCase(Repository repository){
        return new GetSongs(repository);
    }
}
