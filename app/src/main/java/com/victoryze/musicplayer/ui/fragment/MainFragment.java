package com.victoryze.musicplayer.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.appthemeengine.ATE;
import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.ui.fragment.viewpager.AlbumFragment;
import com.victoryze.musicplayer.ui.fragment.viewpager.ArtistFragment;
import com.victoryze.musicplayer.ui.fragment.viewpager.SongsFragment;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.util.DensityUtil;
import com.victoryze.musicplayer.util.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dsz on 17/6/13.
 */

public class MainFragment extends Fragment {
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private PreferencesUtility mPreferences;
    private String action;

    public static MainFragment newInstance(String action){
        MainFragment mainFragment=new MainFragment();
        Bundle bundle=new Bundle();
        switch (action){
            //我的所有歌曲
            case Constants.NAVIGATE_ALLSONG:
                bundle.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                bundle.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                bundle.putString(Constants.PLAYLIST_TYPE,action);
                break;
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                bundle.putString(Constants.PLAYLIST_TYPE,action);
                break;

            default:
                throw new RuntimeException("wrong action type");
        }
        mainFragment.setArguments(bundle);
        return mainFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences=PreferencesUtility.getInstance(getActivity());
        if(getArguments()!=null){
            action=getArguments().getString(Constants.PLAYLIST_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ATE.apply(this, ATEUtil.getATEKey(getActivity()));
        ButterKnife.bind(this,view);

        //android 5.0
        if (Build.VERSION.SDK_INT<21&&view.findViewById(R.id.status_bar)!=null){
            view.findViewById(R.id.status_bar).setVisibility(View.GONE);
            int statusBarHeight= DensityUtil.getStatusBarHeight(getContext());
            Toolbar toolbar=(Toolbar)view.findViewById(R.id.toolbar);
            toolbar.setPadding(0,statusBarHeight,0,0);
            AppBarLayout.LayoutParams params=(AppBarLayout.LayoutParams)toolbar.getLayoutParams();
            //?
            params.setScrollFlags(0);
            toolbar.setLayoutParams(params);
        }
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        final ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        switch (action){
            case Constants.NAVIGATE_ALLSONG:
                actionBar.setTitle(R.string.library);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTADD:
                actionBar.setTitle(R.string.recent_add);
                break;
            case Constants.NAVIGATE_PLAYLIST_RECENTPLAY:
                actionBar.setTitle(R.string.recent_play);
                break;
            case Constants.NAVIGATE_PLAYLIST_FAVOURITE:
                actionBar.setTitle(R.string.favourate);
                break;
        }
        mTabLayout.setupWithViewPager(mViewPager);

        if(mViewPager!=null){
            setupViewPager(mViewPager);
            //闲置时保留的page的个数
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setCurrentItem(mPreferences.getStartPageIndex());
        }
    }


    private void setupViewPager(ViewPager viewPager){
        Adapter adapter=new Adapter(getChildFragmentManager());
        adapter.addFragment(SongsFragment.newInstance(action),this.getString(R.string.songs));
        adapter.addFragment(ArtistFragment.newInstance(action),this.getString(R.string.artists));
        adapter.addFragment(AlbumFragment.newInstance(action),this.getString(R.string.albums));
        viewPager.setAdapter(adapter);

    }
    @Override
    public void onPause() {
        super.onPause();
        mPreferences.setStartPageIndex(mViewPager.getCurrentItem());
    }

    static class Adapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragments=new ArrayList<>();
        private final List<String>mFragmentTitles=new ArrayList<>();
        public Adapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(Fragment fragment,String title){
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
