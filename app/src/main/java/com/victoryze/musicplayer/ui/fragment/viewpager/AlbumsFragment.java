package com.victoryze.musicplayer.ui.fragment.viewpager;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.appthemeengine.ATE;
import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.ListenerApp;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.injector.component.AlbumsComponent;
import com.victoryze.musicplayer.injector.component.ApplicationComponent;
import com.victoryze.musicplayer.injector.component.DaggerAlbumsComponent;
import com.victoryze.musicplayer.injector.module.AlbumsModule;
import com.victoryze.musicplayer.mvp.contract.AlbumsContract;
import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.ui.adapter.AlbumAdapter;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.util.PreferencesUtility;
import com.victoryze.musicplayer.widget.DividerItemDecoration;
import com.victoryze.musicplayer.widget.fastscroller.FastScrollRecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dsz on 17/6/13.
 */

public class AlbumsFragment extends Fragment implements AlbumsContract.View {
    @Inject
    AlbumsContract.Presenter mPresenter;

    @BindView(R.id.recyclerview)
    FastScrollRecyclerView mRecyclerView;
    @BindView(R.id.view_empty)
    View emptyView;
    private PreferencesUtility mPreferences;
    private boolean isGrid;
    private String mAction;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private AlbumAdapter mAlbumAdapter;

    public static AlbumsFragment newInstance(String action) {
        Bundle args = new Bundle();
        switch (action) {
            //我的所有歌曲
            case Constants.NAVIGATE_ALLSONG:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                args.putString(Constants.PLAYLIST_TYPE, action);
                break;
            default:
                throw new RuntimeException("wrong action type");
        }
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injetctDependences();
        mPresenter.attachView(this);
        mPreferences = PreferencesUtility.getInstance(getActivity());
        isGrid = mPreferences.isAlbumsGrid();
        if (isGrid) {
            mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        }
        if (getArguments() != null) {
            mAction = getArguments().getString(Constants.PLAYLIST_TYPE);
        }
        mAlbumAdapter=new AlbumAdapter(getActivity(),mAction);

    }


    private void injetctDependences() {
        ApplicationComponent applicationComponent = ((ListenerApp) getActivity().getApplication()).getApplicationComponent();
        AlbumsComponent albumsComponent = DaggerAlbumsComponent
                .builder()
                .applicationComponent(applicationComponent)
                .albumsModule(new AlbumsModule())
                .build();
        albumsComponent.inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ATE.apply(this, ATEUtil.getATEKey(getActivity()));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAlbumAdapter);
        setItemDecoration();

        mPresenter.loadAblums(mAction);



    }
    private void setItemDecoration() {
        if (isGrid) {
            int spacingInPixels = getActivity().getResources().getDimensionPixelSize(R.dimen.spacing_card_album_grid);
            mItemDecoration = new SpacesItemDecoration(spacingInPixels);
        } else {
            mItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, true);
        }
        mRecyclerView.addItemDecoration(mItemDecoration);
    }
    @Override
    public void showAblums(List<Album> ablumList) {
        mAlbumAdapter.setData(ablumList);
    }

    @Override
    public void showEmptyView() {

    }



    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position % 2 == 0) {
                outRect.left = 0;
                outRect.top = space;
                outRect.right = space / 2;
            } else {
                outRect.left = space / 2;
                outRect.top = space;
                outRect.right = 0;
            }
        }
    }

}
