package com.victoryze.musicplayer.ui.adapter;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.mvp.contract.SongsContract;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.util.ListenerUtil;
import com.victoryze.musicplayer.widget.fastscroller.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dsz on 17/6/14.
 */

public class SongsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>implements FastScrollRecyclerView.SectionedAdapter {
    private List<Song> mArrayList;
    private AppCompatActivity mContext;
    private String mAction;
    private boolean isWitchHeader;
    private float mTopPlayScore;
    private long[]mSongIds;
    public SongsListAdapter(AppCompatActivity context, List<Song>arrayList,String action,boolean withHeader) {

        if (arrayList==null){
            mArrayList=new ArrayList<>();
        }else {
           mArrayList= arrayList;
        }

        mContext=context;
        mAction=action;
        isWitchHeader=withHeader;
        mSongIds=getSongIds();


    }

    @Override
    public int getItemViewType(int position) {
        if(position==0&&isWitchHeader){
            return Type.TYPE_PLAY_SHUFFLE;
        }else {
            return  Type.TYPE_SONG;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        switch (viewType){
            case Type.TYPE_PLAY_SHUFFLE:
                View playShuffle= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_shuffle,parent,false);
                viewHolder=new PlayShuffleViewHoler(playShuffle);
                break;
            case Type.TYPE_SONG:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_linear_layout_item, parent, false);
                viewHolder = new ItemHolder(v);
                break;
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type=getItemViewType(position);
        switch (type){
            case Type.TYPE_PLAY_SHUFFLE:
                break;

            case Type.TYPE_SONG:
                ItemHolder itemHolder = (ItemHolder) holder;
                Song localItem;
                if (isWitchHeader){
                    localItem = mArrayList.get(position - 1);
                }else {
                    localItem = mArrayList.get(position);
                }
                itemHolder.title.setText(localItem.title);
                itemHolder.artist.setText(localItem.artistName);
                itemHolder.album.setText(localItem.albumName);

                Glide.with(holder.itemView.getContext()).load(ListenerUtil.getAlbumArtUri(localItem.albumId).toString())
                        .error(ATEUtil.getDefaultAlbumDrawable(mContext))
                        .placeholder(ATEUtil.getDefaultAlbumDrawable(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(itemHolder.albumArt);

                break;
        }


    }

    @Override
    public int getItemCount() {
        if (isWitchHeader&&mArrayList.size()!=0){
            return mArrayList.size()+1;
        }else {
            return mArrayList.size();
        }

    }

    public void setSongList(List<Song>arrayList){
        if (arrayList==null){
            mArrayList=new ArrayList<>();
        }else {
            mArrayList=arrayList;
        }
        mSongIds=getSongIds();
        //TODO
        if (arrayList.size()!=0){
            mTopPlayScore=arrayList.get(0).getPlayCountScore();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return null;
    }

    public long[] getSongIds() {
        int songNum=mArrayList.size();
        long[]result=new long[songNum];
        for(int i=0;i<songNum;i++){
            result[i]=mArrayList.get(i).id;
        }

        return result;
    }


    public class PlayShuffleViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.play_shuffle)
         ImageView play_shuffle;

        public PlayShuffleViewHoler(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            play_shuffle.getDrawable().setColorFilter(ATEUtil.getThemeAccentColor(mContext), PorterDuff.Mode.SRC_IN);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO 播放音乐
        }
    }


  public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       @BindView(R.id.text_item_title)
      TextView title;
      @BindView(R.id.text_item_subtitle)
      TextView artist;
      @BindView(R.id.text_item_subtitle_2)
      TextView album;
      @BindView(R.id.image)
      ImageView albumArt;
      @BindView(R.id.popup_menu)
      ImageView popuMenu;
      @BindView(R.id.playscore)
       View playcore;

      public ItemHolder(View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);
          itemView.setOnClickListener(this);
      }

      @Override
      public void onClick(View view) {
          //播放
      }
  }



    public static final class Type{
        public  static final int TYPE_PLAY_SHUFFLE=0;
        public static  final int TYPE_SONG=1;
    }
}
