package com.victoryze.musicplayer.mvp.model;

/**
 * Created by dsz on 17/6/11.
 */

public class Album {


    //Album的id
    public final long id;
    //Album的标题名
    public final String title;
    //Album作者的名字
    public final String artistName;
    //Album作者的id
    public final long artistId;
    //Album下的歌曲总数
    public final int songCount;
    //Album出版的哪年
    public  final  int year;

    public Album() {
        this.id=-1;
        this.title="";
        this.artistName="";
        this.artistId=-1;
        this.songCount=-1;
        this.year=-1;
    }
    public Album(long _id,String _title,String _atristName,long _artistId
    ,int _songCount,int _year
    ){
        this.id=_id;
        this.title=_title;
        this.artistName=_atristName;
        this.artistId=_artistId;
        this.songCount=_songCount;
        this.year=_year;
    }



}
