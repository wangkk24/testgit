package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ChannelLogo.java
 * @author: yh
 * @date: 2017-04-21 15:59
 */

public class ChannelLogo implements Serializable {


    /**
     * url :
     * display :
     * location :
     * size :
     */

    /**
     * 台标URL。

     如：http://10.12.2.25:8080/VSP/jsp/image/a.jpg。

     VSP IP地址支持IPv4或IPv6，
     平台根据终端接入的网络地址类型返回对应的HTTP地址。
     */
    @SerializedName("url")
    private String url;

    /**
     *台标显示，隐藏时长，格式为“显示时长隐藏时长”。

     单位：秒。
     */
    @SerializedName("display")
    private String display;

    /**
     *台标位置，格式为“X,Y”，单位为像素。
     */
    @SerializedName("location")
    private String location;

    /**
     *台标大小，格式为“高度,宽度”，单位为像素。
     */
    @SerializedName("size")
    private String size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
