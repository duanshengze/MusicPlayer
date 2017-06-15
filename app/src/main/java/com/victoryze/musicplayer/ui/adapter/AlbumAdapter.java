package com.victoryze.musicplayer.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.victoryze.musicplayer.R;
import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.util.ATEUtil;
import com.victoryze.musicplayer.util.ColorUtil;
import com.victoryze.musicplayer.util.ListenerUtil;
import com.victoryze.musicplayer.util.PreferencesUtility;
import com.victoryze.musicplayer.widget.fastscroller.FastScrollRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dsz on 17/6/15.
 */

public class AlbumAdapter extends RecyclerView.Adapter implements FastScrollRecyclerView.SectionedAdapter {

    private List<Album> mData;
    private String mAction;
    private boolean isGrid;
    private Activity mContext;


    public AlbumAdapter( Activity context,String action) {
        mAction = action;
        mContext = context;
        isGrid= PreferencesUtility.getInstance(context).isAlbumsGrid();
    }

    public void setData(List<Album> data){
        mData=data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (isGrid){
           v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_grid_layout_item,viewGroup,false);

        }else {
          v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_linear_layout_item,viewGroup,false);

        }
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Album album=mData.get(position);
        final ItemHolder itemHolder=(ItemHolder)holder;
        itemHolder.title.setText(album.title);
        itemHolder.artist.setText(album.artistName);
        itemHolder.songcount.setText(ListenerUtil.makeLabel(mContext, R.plurals.Nsongs, album.songCount));

        Glide.with(itemHolder.itemView.getContext())
                .load(ListenerUtil.getAlbumArtUri(album.id))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (isGrid) {
                            itemHolder.footer.setBackgroundColor(ATEUtil.getThemeAlbumDefaultPaletteColor(mContext));
                        }
                        itemHolder.albumArt.setImageDrawable(ATEUtil.getDefaultAlbumDrawable(mContext));
                        itemHolder.title.setTextColor(ATEUtil.getThemeTextColorPrimary(mContext));
                        itemHolder.artist.setTextColor(ATEUtil.getThemeTextColorSecondly(mContext));
                        itemHolder.songcount.setTextColor(ATEUtil.getThemeTextColorSecondly(mContext));
                        itemHolder.popupMenu.setColorFilter(mContext.getResources().getColor(R.color.background_floating_material_dark));
                    }

                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (isGrid){
                                    new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            Palette.Swatch swatch= ColorUtil.getMostPopulousSwatch(palette);
                                            if (swatch!=null){
                                                int color=swatch.getRgb();
                                                itemHolder.footer.setBackgroundColor(color);
                                                int detailColor = swatch.getTitleTextColor();
                                                itemHolder.albumArt.setImageBitmap(resource);
                                                itemHolder.title.setTextColor(ColorUtil.getOpaqueColor(detailColor));
                                                itemHolder.artist.setTextColor(detailColor);
                                                itemHolder.songcount.setTextColor(detailColor);
                                                itemHolder.popupMenu.setColorFilter(detailColor);
                                            }

                                        }
                                    });
                            }else {
                                itemHolder.albumArt.setImageBitmap(resource);
                            }
                    }


                });

        if(ListenerUtil.isLollipop()){
            itemHolder.albumArt.setTransitionName("transition_album_art" + position);
        }

        setOnPopupMenuListener(itemHolder, position);

    }

    private void setOnPopupMenuListener(ItemHolder itemHolder, int position) {
        //TODO  添加弹出菜单
    }

    @Override
    public int getItemCount() {
        return (null==mData?0:mData.size());
    }

    @NonNull
    @Override
    public String getSectionName(int position) {

        if (mData==null||mData.size()==0){
            return "";
        }
        Character ch=mData.get(position).title.charAt(0);
        if (Character.isDigit(ch)){
            return  "#";
        }else {
            return Character.toString(ch);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.text_item_title)
        TextView title;
        @BindView(R.id.text_item_subtitle)
        TextView artist;
        @BindView(R.id.text_item_subtitle_2)
        TextView songcount;
        @BindView(R.id.image)
        ImageView albumArt;
        @BindView(R.id.popup_menu)
        ImageView popupMenu;
        @BindView(R.id.footer)
        View footer;



        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
