package com.victoryze.musicplayer.mvp.base;

/**
 * Created by dsz on 17/6/13.
 */

public abstract class UseCase<Q extends UseCase.RequestValues,P extends UseCase.ResponseValue> {


    private Q mRequestValues;


    public Q getRequestValues() {
        return mRequestValues;
    }

    public void setRequestValues(Q requestValues) {
        mRequestValues = requestValues;
    }


    public abstract P execute(Q requestValues);

    public interface RequestValues{

    }
    public interface ResponseValue {

    }

}
