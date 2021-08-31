package com.pukka.ydepg.common.profile.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileCustomization {

    //页面头部中文名
    private String pageName;

    //页面背景图片
    @SerializedName("backGround")
    private String background;

    //0：是主账号；1：不是主账号  1使用"我的"页面观看历史效果；0使用两行效果
    @SerializedName("primaryAccount")
    private String primaryAccount;

    //#00c9bd|#FFEED81F  字体颜色|背景色
    @SerializedName("focusColor")
    private String focusColor;

    //页面数据数组
    private List<ControlInfo> controlInfos;

    public String getFocusColor() {
        return focusColor;
    }

    public void setFocusColor(String focusColor) {
        this.focusColor = focusColor;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public List<ControlInfo> getControlInfos() {
        return controlInfos;
    }

    public void setControlInfos(List<ControlInfo> controlInfos) {
        this.controlInfos = controlInfos;
    }
}
