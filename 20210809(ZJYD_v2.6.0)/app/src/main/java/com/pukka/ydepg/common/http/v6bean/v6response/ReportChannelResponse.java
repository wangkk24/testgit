package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ReportChannelResponse.java
 * @author: yh
 * @date: 2017-04-25 10:43
 */

public class ReportChannelResponse extends BaseResponse{


    /**
     * result :
     * playSessionKey :
     * extensionFields : [""]
     */

    /**
     *播放会话ID，在入参action为0,2,3的时候返回。

     */
    @SerializedName("playSessionKey")
    private String playSessionKey;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getPlaySessionKey() {
        return playSessionKey;
    }

    public void setPlaySessionKey(String playSessionKey) {
        this.playSessionKey = playSessionKey;
    }

    @Override
    public String toString() {
        return "ReportChannelResponse{" +
                ", playSessionKey='" + playSessionKey + '\'' +
                ", extensionFields=" + extensionFields +
                '}';
    }
}
