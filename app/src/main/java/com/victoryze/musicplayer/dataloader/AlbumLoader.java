package com.victoryze.musicplayer.dataloader;

/**
 * Created by dsz on 17/6/7.
 */

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.victoryze.musicplayer.mvp.model.Album;
import com.victoryze.musicplayer.util.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * 专辑数据访问层
 * 使用Android提供的MediaStore对歌曲专辑信息查询
 *
 */
public class AlbumLoader {


    private static Observable<Album>getAlbum(final Cursor cursor){
        return  Observable.create(new Observable.OnSubscribe<Album>() {
            @Override
            public void call(Subscriber<? super Album> subscriber) {
                Album album=new Album();
                if (cursor!=null&&cursor.moveToFirst()){

                    long id=cursor.getLong(cursor.getColumnIndex("_id"));
                    String ablum=cursor.getString(cursor.getColumnIndex("album"));
                    String artist=cursor.getString(cursor.getColumnIndex("artist"));
                    //TODO 对请求"artist_id"在AlbumColumns中没有
                    long artistId=cursor.getLong(cursor.getColumnIndex("artist_id"));
                    int numsongs=cursor.getInt(cursor.getColumnIndex("numsongs"));
                    //专辑里面歌曲最早的出版时间
                    int year=cursor.getInt(cursor.getColumnIndex("minyear"));
                    album=new Album(id,ablum,artist,artistId,numsongs,year);
                }
                if (cursor!=null){
                    cursor.close();
                }
                subscriber.onNext(album);
                subscriber.onCompleted();
            }
        });

    }


    /**
     *
     * @param cursor
     * @return
     */
    private static Observable<List<Album>>getAlumsForCursor(final  Cursor cursor){
        return Observable.create(new Observable.OnSubscribe<List<Album>>() {
            @Override
            public void call(Subscriber<? super List<Album>> subscriber) {
                List<Album>arrayList=new ArrayList<Album>();
                if(cursor!=null&&cursor.moveToFirst()){


                    do{
                        long id=cursor.getLong(cursor.getColumnIndex("_id"));
                        String ablum=cursor.getString(cursor.getColumnIndex("album"));
                        String artist=cursor.getString(cursor.getColumnIndex("artist"));
                        //TODO 对请求"artist_id"在AlbumColumns中没有
                        long artistId=cursor.getLong(cursor.getColumnIndex("artist_id"));
                        int numsongs=cursor.getInt(cursor.getColumnIndex("numsongs"));

                        //专辑里面歌曲最早的出版时间
                        int year=cursor.getInt(cursor.getColumnIndex("minyear"));
                        Album album=new Album(id,ablum,artist,artistId,numsongs,year);
                        arrayList.add(album);

                    }while (cursor.moveToNext());

                    if (cursor!=null){
                        cursor.close();
                    }
                    subscriber.onNext(arrayList);
                    subscriber.onCompleted();
                }

            }
        });
    }



    public static Observable<List<Album>>getAllAlbums(Context context){

        return getAlumsForCursor(makeAlbumCursor(context,null,null));

    }


    /**
     * 根据id查询Ablum
     * @param context
     * @param id
     * @return
     */
    public static Observable<Album> getAlbum(Context context,long id){
        return getAlbum(makeAlbumCursor(context,"_id"+"=?",new String[]{String.valueOf(id)}));


    }
    /**
     * 获取专辑或者艺术家包含paramString的所有专辑
     *
     * paramString+% 表示以paramString开头
     * %+paramString 表示以paramString结尾
     * %+paramString+% 表示包含paramString
     * @param context
     * @param paramString
     * @return
     */
    public static Observable<List<Album>> getAlbum(Context context,String paramString){
        return  getAlumsForCursor(makeAlbumCursor(context,"album LIKE ? OR artist LIKE ?",
                new String[]{"%"+paramString+"%","%"+paramString+"%"}
                ));
    }

    /**
     * 访问 MediaStore.Audio.Albums得到相关Cursor
     * @param context
     * @param selection
     * @param paramArrayString
     * @return
     */
    private static Cursor makeAlbumCursor(Context context,String selection,String[]paramArrayString){
        final String albumSortOrder= PreferencesUtility.getInstance(context).getAlbumSortOrder();

        //TODO 对请求"artist_id"在AlbumColumns中没有
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{"_id","album","artist","artist_id","numsongs","minyear"},selection,paramArrayString,albumSortOrder
                );
        return  cursor;
    }
}
