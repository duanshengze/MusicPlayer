package com.victoryze.musicplayer.api;

import com.victoryze.musicplayer.api.model.KUGouRawLyic;
import com.victoryze.musicplayer.api.model.KuGouSearchLyricResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dsz on 17/6/14.
 */

public interface KuGouApiService {
    /**
     * 查询歌词
     * @param songName 歌名
     * @param duration 歌的时长
     * @return
     */
    @GET("search?ver=1&man=yes&client=pc")
    Observable<KuGouSearchLyricResult>searchLyic(
            @Query("keywords")String songName,
            @Query("duration")String duration
    );

    /**
     * 下载歌词
     * @param id 歌曲的id（通过查询歌词所得）
     * @param accesskey 通过查询歌词所得
     * @return
     */
    @GET("download?ver=1&client=pc&fmt=lrc&charset=utf8")
    Observable<KUGouRawLyic>getRawLyic(@Query("id")String id,@Query("accesskey")String accesskey);




}
