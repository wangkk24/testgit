package com.pukka.ydepg.moudule.vrplayer.vrplayer.module;

import java.util.ArrayList;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-05
 */

public class VideoListConfig {

    private ArrayList<VideoBean> configList;

    /**
     * 首页背景图片
     */
    private  String background;



    /**
     * 检查更新的播放调度url
     */
    private String loginRouteForUpgrade;

    public String getLoginRouteForUpgrade() {
        return loginRouteForUpgrade;
    }

    public void setLoginRouteForUpgrade(String loginRouteForUpgrade) {
        this.loginRouteForUpgrade = loginRouteForUpgrade;
    }


    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public VideoListConfig(ArrayList<VideoBean> configList) {
        this.configList = configList;
    }

    public ArrayList<VideoBean> getConfigList() {
        return configList;
    }

    public void setConfigList(ArrayList<VideoBean> configList) {
        this.configList = configList;
    }
}
