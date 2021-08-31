package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybillContext.java
 * @author: yh
 * @date: 2017-06-23 00:43
 */

public class ChannelPlaybillContext {


    /**
     * channelDetail : channelDetail
     * prePlaybillLites : ["prePlaybillLites"]
     * currentPlaybillLite : currentPlaybillLite
     * nextPlaybillLites : ["nextPlaybillLites"]
     */

    @SerializedName("channelDetail")
    private ChannelDetail channelDetail;
    @SerializedName("currentPlaybillLite")
    private PlaybillLite currentPlaybillLite;
    @SerializedName("prePlaybillLites")
    private List<PlaybillLite> prePlaybillLites;
    @SerializedName("nextPlaybillLites")
    private List<PlaybillLite> nextPlaybillLites;

    public ChannelDetail getChannelDetail() {
        return channelDetail;
    }

    public void setChannelDetail(ChannelDetail channelDetail) {
        this.channelDetail = channelDetail;
    }

    public PlaybillLite getCurrentPlaybillLite() {
        return currentPlaybillLite;
    }

    public void setCurrentPlaybillLite(PlaybillLite currentPlaybillLite) {
        this.currentPlaybillLite = currentPlaybillLite;
    }

    public List<PlaybillLite> getPrePlaybillLites() {
        return prePlaybillLites;
    }

    public void setPrePlaybillLites(List<PlaybillLite> prePlaybillLites) {
        this.prePlaybillLites = prePlaybillLites;
    }

    public List<PlaybillLite> getNextPlaybillLites() {
        return nextPlaybillLites;
    }

    public void setNextPlaybillLites(List<PlaybillLite> nextPlaybillLites) {
        this.nextPlaybillLites = nextPlaybillLites;
    }
}
