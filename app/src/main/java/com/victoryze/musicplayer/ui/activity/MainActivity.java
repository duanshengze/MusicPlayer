package com.victoryze.musicplayer.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.victoryze.musicplayer.Constants;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.permission.PermissionCallback;
import com.victoryze.musicplayer.permission.PermissionManager;
import com.victoryze.musicplayer.ui.fragment.FoldersFragment;
import com.victoryze.musicplayer.ui.fragment.MainFragment;
import com.victoryze.musicplayer.ui.fragment.PlayListFragment;
import com.victoryze.musicplayer.ui.fragment.PlayRankingFragment;
import com.victoryze.musicplayer.ui.fragment.SearchFragment;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.util.ListenerUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout panelLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    //navigateView的header部分view元素
    private TextView songTitle;
    private TextView songArtist;
    private ImageView albumart;


    private boolean isDarkTheme;
    private String mAction;
    private Runnable mRunnable;
    private Handler navDrawRunnable=new Handler();

    private Map<String,Runnable>navigationMap=new HashMap<>();


    private final PermissionCallback mPermissionReadstorageCallback=new PermissionCallback() {
        @Override
        public void permissionGranted() {
            //加载所有
            //TODO
//            loadEverything();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };



    //我的歌曲
    private Runnable navigateLibrary=new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_library).setChecked(true);
            Fragment fragment= MainFragment.newInstance(Constants.NAVIGATE_ALLSONG);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            //but allows the commit to be executed after an activity's state is saved.
            //允许被提交 当activity的状态被保存时，如果调用commit ，当activity状态保存后调用commit会抛出异常
            transaction.replace(R.id.fragment_container,fragment).commitAllowingStateLoss();
        }
    };
    //我的歌单
    private Runnable navigatePlaylist=new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_playlists).setChecked(true);
            Fragment fragment=new PlayListFragment();
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            //避免之前的fragment重新被实例化
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container,fragment).commit();
        }
    };

    //我喜欢
    private Runnable navigateFavorite=new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_favourate).setChecked(true);
            Fragment fragment=MainFragment.newInstance(Constants.NAVIGATE_PLAYLIST_FAVOURITE);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,fragment).commit();
        }
    };
    //文件夹
    private Runnable navigateFolders=new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_folders).setChecked(true);
            Fragment fragment = new FoldersFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    //最近播放
    private Runnable navigateRecentPlay = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_recent_play).setChecked(true);
            Fragment fragment = MainFragment.newInstance(Constants.NAVIGATE_PLAYLIST_RECENTPLAY);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    //最近添加
    private Runnable navigateRecentAdd = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_recent_add).setChecked(true);
            Fragment fragment = MainFragment.newInstance(Constants.NAVIGATE_PLAYLIST_RECENTADD);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };

    //播放排行
    private Runnable navigatePlayRanking = new Runnable() {
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_play_ranking).setChecked(true);
            Fragment fragment = new PlayRankingFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    //搜索
    private Runnable navigateSearch = new Runnable() {
        public void run() {
            Fragment fragment = new SearchFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.add(R.id.fragment_container, fragment);
            transaction.addToBackStack(null).commit();
        }
    };

    //设置界面
    private Runnable navigateSetting=new Runnable() {
        @Override
        public void run() {
            final Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            MainActivity.this.startActivity(intent);
        }
    };

    //专辑界面
    private Runnable navigateAlbum=new Runnable() {
        @Override
        public void run() {
            //TODO
        }
    };
    //艺术家界面
    private Runnable navigateArtist=new Runnable() {
        @Override
        public void run() {
            //TODO
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAction=getIntent().getAction();
        isDarkTheme= ATEUtil.getATEKey(this).equals("dark_theme");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationMap.put(Constants.NAVIGATE_LIBRARY, navigateLibrary);
        navigationMap.put(Constants.NAVIGATE_ALBUM, navigateAlbum);
        navigationMap.put(Constants.NAVIGATE_ARTIST, navigateArtist);
        View header=mNavigationView.inflateHeaderView(R.layout.nav_header);
        albumart=(ImageView)header.findViewById(R.id.album_art);
        songTitle=(TextView)header.findViewById(R.id.song_title);
        songArtist=(TextView)header.findViewById(R.id.song_artist);



        navDrawRunnable.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDrawerContent(mNavigationView);
                setupNavigationIcons(mNavigationView);
            }
        },700);

        if (ListenerUtil.isMarshmallow()) {
            checkPermissionAndThenLoad();
        } else {
//            loadEverything();
        }
    }



    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 updatePostion(item);
                return true;
            }
        });
    }

    private void updatePostion(MenuItem item) {
        mRunnable=null;
        switch (item.getItemId()){
            //我的歌曲
            case R.id.nav_library:
                mRunnable=navigateLibrary;
                break;
            //我的歌单
            case R.id.nav_playlists:
                mRunnable=navigatePlaylist;
                break;
            case R.id.nav_folders:
                mRunnable=navigateFolders;
                break;
            case R.id.nav_favourate:
                mRunnable=navigateFavorite;
                break;
            case R.id.nav_recent_play:
                mRunnable=navigateRecentPlay;
                break;
            case R.id.nav_recent_add:
                mRunnable=navigateRecentAdd;
                break;
            case R.id.nav_play_ranking:
                mRunnable=navigatePlayRanking;
                break;
            case R.id.nav_settings:
                mRunnable=navigateSetting;
                break;
            case R.id.nav_exit:
                this.finish();
                break;
        }
        if(mRunnable!=null){
            mDrawerLayout.closeDrawers();
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRunnable.run();
                }
            },350);


        }



    }

    /**
     * 设置图标
     * @param navigationView
     */
    private void setupNavigationIcons(NavigationView navigationView) {

        //material-icon-lib currently doesn't work with navigationview of design support library 22.2.0+
        //set icons manually for now
        //https://github.com/code-mc/material-icon-lib/issues/15

        if (!isDarkTheme) {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.ic_music_note_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.ic_queue_music_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_folders).setIcon(R.drawable.ic_folder_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_favourate).setIcon(R.drawable.ic_favorite_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_recent_play).setIcon(R.drawable.ic_watch_later_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_recent_add).setIcon(R.drawable.ic_add_box_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_play_ranking).setIcon(R.drawable.ic_sort_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.ic_settings_black_48dp);
            navigationView.getMenu().findItem(R.id.nav_exit).setIcon(R.drawable.ic_exit_to_app_black_48dp);
        } else {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.ic_music_note_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.ic_queue_music_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_folders).setIcon(R.drawable.ic_folder_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_favourate).setIcon(R.drawable.ic_favorite_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_recent_play).setIcon(R.drawable.ic_watch_later_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_recent_add).setIcon(R.drawable.ic_add_box_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_play_ranking).setIcon(R.drawable.ic_sort_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.ic_settings_white_48dp);
            navigationView.getMenu().findItem(R.id.nav_exit).setIcon(R.drawable.ic_exit_to_app_white_48dp);
        }

    }
    private void checkPermissionAndThenLoad() {
        //check for permission
        if (PermissionManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            loadEverything();
        } else {
            if (PermissionManager.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(panelLayout, "Listener will need to read external storage to display songs on your device.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionManager.askForPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, mPermissionReadstorageCallback);
                            }
                        }).show();
            } else {
                PermissionManager.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, mPermissionReadstorageCallback);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
