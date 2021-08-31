package com.pukka.ydepg.moudule.search.event;

/**
 * 搜索事件
 * Created by hasee on 2017/6/2.
 */

public class SearchKeyEvent {
    private String mSearchKey;

    public SearchKeyEvent(String mSearchKey) {
        this.mSearchKey = mSearchKey;
    }

    public String getmSearchKey() {
        return mSearchKey;
    }
}
