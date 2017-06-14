package com.victoryze.musicplayer.ui.fragment.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.afollestad.appthemeengine.ATE;
import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.RxBus;
import com.victoryze.musicplayer.injector.component.ApplicationComponent;

import com.victoryze.musicplayer.injector.component.DaggerSongsComponent;
import com.victoryze.musicplayer.injector.component.SongsComponent;
import com.victoryze.musicplayer.injector.module.ActivityModule;
import com.victoryze.musicplayer.injector.module.SongsModule;
import com.victoryze.musicplayer.mvp.contract.SongsContract;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.ui.adapter.SongsListAdapter;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.widget.DividerItemDecoration;
import com.victoryze.musicplayer.widget.fastscroller.FastScrollRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dsz on 17/6/13.
 */

public class SongsFragment extends Fragment implements SongsContract.View{
    @Inject
    SongsContract.Presenter mPresenter;
    @BindView(R.id.recyclerview)
    FastScrollRecyclerView mRecyclerView;
    @BindView(R.id.view_empty)
    ViewStub emptyView;
    private SongsListAdapter mAdapter;
    private String action;



    public static SongsFragment newInstance(String action){
        Bundle args=new Bundle();
        switch (action){
            //我的所有歌曲
            case Constants.NAVIGATE_ALLSONG:
                args.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                args.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                args.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                args.putString(Constants.PLAYLIST_TYPE,action);
                break;
            default:
                throw new RuntimeException("wrong action type");
        }
        SongsFragment fragment=new SongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependence();
        mPresenter.attachView(this);
        if (getArguments() != null) {
            action = getArguments().getString(Constants.PLAYLIST_TYPE);
        }
        mAdapter=new SongsListAdapter((AppCompatActivity)getActivity(),null,action,true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_recyclerview,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ATE.apply(this, ATEUtil.getATEKey(getActivity()));

        ATE.apply(this, ATEUtil.getATEKey(getActivity()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, true));
        mPresenter.loadSongs(action);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();

    }

    /**
     * dagger2 依赖注入
     */
    private void injectDependence() {
        ApplicationComponent applicationComponent=((ListenerApp)getActivity().getApplication()).getApplicationComponent();
        SongsComponent songsComponet= DaggerSongsComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .songsModule(new SongsModule())
                .build();
        songsComponet.inject(this);

    }

    @Override
    public void showSongs(List<Song> songList) {
        emptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setSongList(songList);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
}
