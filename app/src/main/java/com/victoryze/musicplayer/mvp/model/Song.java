package com.victoryze.musicplayer.mvp.model;

/**
 * Created by dsz on 17/6/11.
 * 歌曲的实例
 */

public class Song {
    public final long albumId;
    public final String albumName;
    public final long artistId;
    public final String artistName;
    public final int duration;
    public final long id;
    public final String title;
    public final int trackNumber;
    public float playCountScore;
    public final String path;

    public Song() {
        this.id=-1;
        this.albumId=-1;
        this.artistId=-1;
        this.title="";
        this.albumName="";
        this.artistName="";
        this.duration=-1;
        this.trackNumber=-1;
        this.path="";
    }

    public Song(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber, String _path) {
        this.albumId = _albumId;
        this.albumName = _albumName;
        this.artistId = _artistId;
        this.artistName = _artistName;
        this.duration = _duration;
        this.id = _id;
        this.title = _title;
        this.trackNumber = _trackNumber;
        this.path = _path;
    }


    public Song(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber) {
        this(_id,_albumId,_artistId,_title,_artistName,_albumName,_duration,_trackNumber,"");
    }


    public float getPlayCountScore() {
        return playCountScore;
    }

    public void setPlayCountScore(float playCountScore) {
        this.playCountScore = playCountScore;
    }
}
