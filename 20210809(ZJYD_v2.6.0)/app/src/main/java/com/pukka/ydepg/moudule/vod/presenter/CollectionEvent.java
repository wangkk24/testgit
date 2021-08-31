package com.pukka.ydepg.moudule.vod.presenter;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/2/9.
 */

public class CollectionEvent {
    private boolean isCollection;

    private String vodId;

    public String getVodId() {
        return vodId;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public CollectionEvent(boolean isCollection,String vodId) {
        this.vodId=vodId;
        this.isCollection = isCollection;
    }
}
