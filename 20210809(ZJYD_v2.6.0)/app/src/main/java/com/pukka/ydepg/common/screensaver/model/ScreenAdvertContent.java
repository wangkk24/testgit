package com.pukka.ydepg.common.screensaver.model;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;

public class ScreenAdvertContent {

    //SSP平台类型的屏保广告
    public final static String TYPE_SSP = "1";

    //运营配置类型的屏保广告
    public final static String TYPE_VSP = "0";

    //0 [公共字段]当前广告是否上报了曝光话单,用于防止广告循环播放时重复上报话单的问题
    private boolean isReport = false;



    //1 [公共字段]广告类型 1:SSP广告 0:VSP广告
    private String type;

    //2 [公共字段]广告图片的Url
    private String bannerUrl;

    //3 [公共字段]广告点击跳转H5页面链接
    private String clickUrl;

    //4-1[SSP广告]SSP广告完整数据,用于上报SSP话单
    private AdvertContent sspContent;

    //4-2[配置广告]UBD上报广告
    private String contentID;

    //5-2[配置广告]UBD上报广告
    private String contentName;

    //6-2[配置广告]广告点击跳转自有/第三方APK页面的参数(没有安装支持自动下载安装)
    private String pkg;
    private String cls;
    private String extra;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public boolean isReported() {
        return isReport;
    }

    public void setReported(boolean report) {
        isReport = report;
    }

    public AdvertContent getSspContent() {
        return sspContent;
    }

    public void setSspContent(AdvertContent sspContent) {
        this.sspContent = sspContent;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}