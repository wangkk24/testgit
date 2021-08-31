package com.pukka.ydepg.launcher.bean.request;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.bean.request.TopicRequest.java
 * @date: 2018-03-11 09:34
 * @version: V1.0 描述当前版本功能
 */


public class TopicRequest extends BaseRequest {
    @SerializedName("version")
    private String version;
    @SerializedName("desktopType")
    private String desktopType;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }
}
