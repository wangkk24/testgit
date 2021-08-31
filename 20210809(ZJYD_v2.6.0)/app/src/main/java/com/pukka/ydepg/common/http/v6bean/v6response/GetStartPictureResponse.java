package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * Created by liudo on 2018/4/12.
 */

public class GetStartPictureResponse extends BaseResponse {
    /**
     * 终端所需要的开机画面的名称
     */
    @SerializedName("picturename")
    private String picturename;
    /**
     * 开机动画的图片下载地址
     */
    @SerializedName("url")
    private String url;
    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}



