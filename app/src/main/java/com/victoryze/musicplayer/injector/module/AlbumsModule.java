package com.victoryze.musicplayer.injector.module;

import com.victoryze.musicplayer.injector.scope.PerActivity;
import com.victoryze.musicplayer.mvp.contract.AlbumsContract;
import com.victoryze.musicplayer.mvp.presenter.AlbumsPresenter;
import com.victoryze.musicplayer.mvp.usecase.GetAlbums;
import com.victoryze.musicplayer.repository.interfaces.Repository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dsz on 17/6/15.
 */
@Module
public class AlbumsModule {

    @PerActivity
    @Provides
    AlbumsContract.Presenter getAlbumsPresenter(GetAlbums getAlbums){
        return  new AlbumsPresenter(getAlbums);
    }
    @PerActivity
    @Provides
    GetAlbums getAlbumsUseCase(Repository repository){
        return  new GetAlbums(repository);
    }

}
