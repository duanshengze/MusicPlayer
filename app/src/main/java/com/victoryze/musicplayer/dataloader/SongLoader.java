package com.victoryze.musicplayer.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.nostra13.universalimageloader.utils.L;
import com.victoryze.musicplayer.mvp.model.Song;
import com.victoryze.musicplayer.provider.SongPlayCount;
import com.victoryze.musicplayer.util.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dsz on 17/6/11.
 * MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
 * _id ，_data（音频路径)，_display_name（音频名称包括后缀），_size， mime_type，
 * date_added， is_drm ，date_modified ，title ，title_key
 * duration ，artist_id ，composer， album_id， track ，year，
 * is_ringtone ，is_music ，is_alarm， is_notification，
 * is_podcast， bookmark ，album_artist， artist_id， artist_key ，
 * artist，album_id ，album_key ，album
 */

public class SongLoader {





    public static Observable<List<Song>>getAllSongs(Context context){
        return getSongsForCursor(makeSongCursor(context,null,null));
    }

    /**
     * 获取文件路径下的所有歌曲
     *
     * @param context
     * @param path
     * @return
     */
    public static Observable<List<Song>> getSongListInFolder(Context context, String path) {
        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
        //以path为前缀
        String[] whereArgs = new String[]{path + "%"};
        return getSongsForCursor(makeSongCursor(context, selection, whereArgs));
    }

    /**
     * 通过 Cursor获得
     * @param cursor
     * @param scoreCursor
     * @return
     */
     static Observable<List<Song>> getSongsWitchSocreForCursor(final Cursor cursor, final Cursor scoreCursor) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {

                List<Song> arrayList = new ArrayList<Song>();

                    if (cursor!=null&&scoreCursor!=null&&cursor.moveToFirst()&&scoreCursor.moveToFirst()){
                        do{
                            long id = cursor.getLong(cursor.getColumnIndex("_id"));
                            String title = cursor.getString(cursor.getColumnIndex("title"));
                            String artist = cursor.getString(cursor.getColumnIndex("artist"));
                            String album = cursor.getString(cursor.getColumnIndex("album"));
                            int duration = cursor.getInt(cursor.getColumnIndex("duration"));
                            int trackNumber = cursor.getInt(cursor.getColumnIndex("track"));
                            long artistId = cursor.getLong(cursor.getColumnIndex("artist_id"));
                            long albumId = cursor.getLong(cursor.getColumnIndex("album_id"));
                            String path = cursor.getString(cursor.getColumnIndex("_data"));
                            float score=scoreCursor.getFloat(scoreCursor.getColumnIndex(SongPlayCount.SongPlayCountColumns.PLAYCOUNTSCORE));
                            Song song=new Song(id, albumId, artistId, title, artist, album, duration, trackNumber, path);
                            song.setPlayCountScore(score);
                            arrayList.add(song);
                        }while (cursor.moveToNext());
                        subscriber.onNext(arrayList);
                        subscriber.onCompleted();
                    }
            }
        });
    }


    /**
     * "_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", "_data"
     *
     * @param cursor
     * @return
     */
    public static Observable<List<Song>> getSongsForCursor(final Cursor cursor) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                List<Song> arrayList = new ArrayList<Song>();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        long id = cursor.getLong(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String artist = cursor.getString(cursor.getColumnIndex("artist"));
                        String album = cursor.getString(cursor.getColumnIndex("album"));
                        int duration = cursor.getInt(cursor.getColumnIndex("duration"));
                        int trackNumber = cursor.getInt(cursor.getColumnIndex("track"));
                        long artistId = cursor.getLong(cursor.getColumnIndex("artist_id"));
                        long albumId = cursor.getLong(cursor.getColumnIndex("album_id"));
                        String path = cursor.getString(cursor.getColumnIndex("_data"));
                        arrayList.add(new Song(id, albumId, artistId, title, artist, album, duration, trackNumber, path));

                    } while (cursor.moveToNext());
                    if (cursor != null) {

                        cursor.close();


                    }
                }
                subscriber.onNext(arrayList);
                subscriber.onCompleted();
            }
        });

    }


    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayString) {
        final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();
        return makeSongCursor(context, selection, paramArrayString, songSortOrder);
    }

    /**
     * @param context
     * @param selection
     * @param paramArrayOfString
     * @param sortOrder
     * @return
     */
    private static Cursor makeSongCursor(Context context, String selection,
                                         String[] paramArrayOfString, String sortOrder) {
        //is_music 字段非零表示歌曲
        String selectionStatement = "is_music=1 AND title !=''";
        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }


        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", "_data"},
                selectionStatement, paramArrayOfString, sortOrder
        );
    }
}
