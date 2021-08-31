package com.pukka.ydepg.event;


//播放记录列表滚动至顶部，防止信息显示不全
public class HistoryListScrollToTopEvent {

    public HistoryListScrollToTopEvent(int postion) {
        this.postion = postion;
    }

    int postion;

    public int getPostion() {
        return postion;
    }
}
