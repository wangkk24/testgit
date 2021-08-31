package com.pukka.ydepg.event;

/**
 * 首页自动播放混搭
 * 桌边播放视频，右边海报
 * 落焦发送Event Bus通知开始播放
 */

public class VideoLivePlayTimeEvent {

    private int layoutId = 0;

    public VideoLivePlayTimeEvent(int layoutId) {
        this.layoutId = layoutId;
    }
    public int getLayoutId() {
        return layoutId;
    }
}
