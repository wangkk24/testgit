package com.pukka.ydepg.moudule.vod.presenter;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/2/13.
 */

public class BookmarkEvent {
    private String playVodBean;

    public BookmarkEvent(String playVodBean) {
        this.playVodBean = playVodBean;
    }

    public String getPlayVodBean() {
        return playVodBean;
    }

}
