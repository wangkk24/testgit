package com.pukka.ydepg.event;

/**
 * 首页自动播放混搭
 * 桌边播放视频，右边海报
 * 落焦发送Event Bus通知开始播放
 */

public class MixAutoPlayTimeEvent {

    private int layoutId = 0;
    private boolean isOldFocusViewId = false;

    public MixAutoPlayTimeEvent(int layoutId,boolean isOldFocusViewId) {
        this.layoutId = layoutId;
        this.isOldFocusViewId = isOldFocusViewId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public boolean isOldFocusViewId() {
        return isOldFocusViewId;
    }
}
