package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayChannelHeartbeatResponse.java
 * @author: yh
 * @date: 2017-04-25 10:45
 */

public class PlayChannelHeartbeatResponse extends BaseResponse{

    /**
     * result :
     * isValid : 12
     * interrupter :
     * extensionFields :
     */

    /**
     *播放是否合法，取值包括：

     0：非法
     1：合法
     */
    @SerializedName("isValid")
    private int isValid;

    /**
     *如果播放会话是被其他设备中断，返回发起中断请求的ProfileID。

     */
    @SerializedName("interrupter")
    private String interrupter;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getInterrupter() {
        return interrupter;
    }

    public void setInterrupter(String interrupter) {
        this.interrupter = interrupter;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
