package com.victoryze.musicplayer.ui.fragment.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.victoryze.musicplayer.Constants;

/**
 * Created by dsz on 17/6/13.
 */

public class AlbumFragment extends Fragment {



    public static AlbumFragment newInstance(String action){
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
        AlbumFragment fragment=new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
