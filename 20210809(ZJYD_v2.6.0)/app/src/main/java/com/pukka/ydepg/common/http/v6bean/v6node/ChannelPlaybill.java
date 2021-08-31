package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ChannelPlaybill.java
 * @author: yh
 * @date: 2017-04-24 14:18
 */

public class ChannelPlaybill {


    /**
     * channelDetail : 12
     * playbillCount : 12
     * playbillLites : []
     */

    /**
     * 频道信息，根据终端请求参数确定是否填充。

     */
    @SerializedName("channelDetail")
    private ChannelDetail channelDetail;

    /**
     * 频道对应的节目单总数。

     */
    @SerializedName("playbillCount")
    private int playbillCount;

    /**
     * 频道对应的节目单列表。

     */
    @SerializedName("playbillLites")
    private List<PlaybillLite> playbillLites;

    public ChannelDetail getChannelDetail() {
        return channelDetail;
    }

    public void setChannelDetail(ChannelDetail channelDetail) {
        this.channelDetail = channelDetail;
    }

    public int getPlaybillCount() {
        return playbillCount;
    }

    public void setPlaybillCount(int playbillCount) {
        this.playbillCount = playbillCount;
    }

    public List<PlaybillLite> getPlaybillLites() {
        return playbillLites;
    }

    public void setPlaybillLites(List<PlaybillLite> playbillLites) {
        this.playbillLites = playbillLites;
    }
}
