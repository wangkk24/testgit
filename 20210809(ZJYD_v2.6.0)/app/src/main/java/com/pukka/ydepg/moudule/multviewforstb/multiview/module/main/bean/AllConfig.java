package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean;


import java.util.ArrayList;
import java.util.List;

public class AllConfig{
    private List<AllVideoConfig> allVideoConfig;

    /**
     * 背景图片的Url
     */
    private String background;

    /**
     * 是否展示播放器的版本号和屏幕文本大小
     */
    private boolean isShowPlayVersion;

    /**
     * 检查更新的播放调度url
     */
    private String loginRouteForUpgrade = "";

    /**
     * 播放器捕获Crash开关
     */
    private String crashPolicy;

    /**
     * 是否展示网速信息
     */
    private boolean isShowSpeed;

    /**
     * 是否展示json数据和UI日志
     */
    private boolean showLogcat;

    /**
     * 是否显示自由视角机位位置的圆圈视图
     */
    private boolean showCameraView;

    public List<AllVideoConfig> getAllVideoConfig() {
        return allVideoConfig != null ? allVideoConfig:new ArrayList<AllVideoConfig>();
    }

    public void setAllVideoConfig(List<AllVideoConfig> allVideoConfig) {
        this.allVideoConfig = allVideoConfig;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isShowPlayVersion() {
        return isShowPlayVersion;
    }

    public void setShowPlayVersion(boolean showPlayVersion) {
        isShowPlayVersion = showPlayVersion;
    }

    public String getLoginRouteForUpgrade() {
        return loginRouteForUpgrade;
    }

    public void setLoginRouteForUpgrade(String loginRouteForUpgrade) {
        this.loginRouteForUpgrade = loginRouteForUpgrade;
    }

    public String getCrashPolicy() {
        return crashPolicy;
    }

    public void setCrashPolicy(String crashPolicy) {
        this.crashPolicy = crashPolicy;
    }

    public boolean isShowSpeed() {
        return isShowSpeed;
    }

    public void setShowSpeed(boolean showSpeed) {
        isShowSpeed = showSpeed;
    }

    public boolean isShowLogcat() {
        return showLogcat;
    }

    public void setShowLogcat(boolean showLogcat) {
        this.showLogcat = showLogcat;
    }

    public boolean isShowCameraView() {
        return showCameraView;
    }

    public void setShowCameraView(boolean showCameraView) {
        this.showCameraView = showCameraView;
    }
}
