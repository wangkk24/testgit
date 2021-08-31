package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: GetLicenseTrigger.java
 * @author: yh
 * @date: 2017-04-24 15:25
 */

public class GetLicenseTrigger {


    /**
     * licenseURL :
     * customData :
     */

    /**
     * License服务器地址。

     如果地址是IP形式，IP地址支持V4或V6，平台根据终端接入的网络地址类型返回对应的服务器地址

     */
    @SerializedName("licenseURL")
    private String licenseURL;

    /**
     * 存放用户和内容保护信息。

     */
    @SerializedName("customData")
    private String customData;

    public String getLicenseURL() {
        return licenseURL;
    }

    public void setLicenseURL(String licenseURL) {
        this.licenseURL = licenseURL;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }
}
