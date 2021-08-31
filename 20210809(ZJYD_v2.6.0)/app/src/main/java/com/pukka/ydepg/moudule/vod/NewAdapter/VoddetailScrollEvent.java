package com.pukka.ydepg.moudule.vod.NewAdapter;

public class VoddetailScrollEvent {
    public static final int EpisodeListRequesetFocus = 12345;

    public static final int PlayViewRequesetFocus = 67890;

    private int index;

    public VoddetailScrollEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
