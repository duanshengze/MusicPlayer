package com.victoryze.musicplayer.mvp.model;

/**
 * Created by dsz on 17/6/6.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.victoryze.musicplayer.util.ListenerUtil;

/**
 * 用来记录当前播放曲目来自什么资源(专辑，歌手，播放列表或无)
 * 资源id
 * 在资源中的序号
 */
public class MusicPlaybackTrack implements Parcelable {

    public long mId;
    //资源id
    public long mSourceId;

    public ListenerUtil.IdType mSourceType;
    public int mSourcePosition;

    public MusicPlaybackTrack(Parcel in) {
        mId=in.readLong();
        mSourceId=in.readLong();
        mSourceType=ListenerUtil.IdType.getTypeById(in.readInt());
        mSourcePosition=in.readInt();
    }

    public MusicPlaybackTrack(long id, long sourceId, ListenerUtil.IdType sourceType, int sourcePosition) {
        mId = id;
        mSourceId = sourceId;
        mSourceType = sourceType;
        mSourcePosition = sourcePosition;
    }

    public static final Creator<MusicPlaybackTrack> CREATOR = new Creator<MusicPlaybackTrack>() {
        @Override
        public MusicPlaybackTrack createFromParcel(Parcel in) {
            //必须按照成员变量声明的顺序读取数据，不然出现获取数据出错


            return new MusicPlaybackTrack(in);
        }

        @Override
        public MusicPlaybackTrack[] newArray(int size) {
            return new MusicPlaybackTrack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(mId);
        dest.writeLong(mSourceId);
        dest.writeInt(mSourceType.mId);
        dest.writeInt(mSourcePosition);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MusicPlaybackTrack){
            MusicPlaybackTrack other=(MusicPlaybackTrack)obj;
            return  mId==other.mId
                    &&mSourceId==other.mSourceId
                    &&mSourceType==other.mSourceType
                    &&mSourcePosition==other.mSourcePosition;

        }
        return  super.equals(obj);
    }


    @Override
    public String toString() {
        return "MusicPlaybackTrack{" +
                "mId=" + mId +
                ", mSourceId=" + mSourceId +
                ", mSourceType=" + mSourceType +
                ", mSourcePosition=" + mSourcePosition +
                '}';
    }
}
