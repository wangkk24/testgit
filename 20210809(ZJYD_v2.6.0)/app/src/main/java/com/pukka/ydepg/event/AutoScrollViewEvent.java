package com.pukka.ydepg.event;

/**
 * 首页自动播放混搭
 * 桌边播放视频，右边海报
 * 落焦发送Event Bus通知开始播放
 */

public class AutoScrollViewEvent {

    private boolean isStartShimmer = false;

    public AutoScrollViewEvent(boolean isStartShimmer) {
        this.isStartShimmer = isStartShimmer;
    }


    public boolean isStartShimmer() {
        return isStartShimmer;
    }
}
