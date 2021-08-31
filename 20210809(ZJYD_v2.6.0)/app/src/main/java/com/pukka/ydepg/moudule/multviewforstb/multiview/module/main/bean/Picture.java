package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean;

public class Picture {

    /**
     * 首页海报的url
     */
    private String posterUrl;

    /**
     * 播放背景图片的Url
     */
    private String playBackground;

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPlayBackground() {
        return playBackground;
    }

    public void setPlayBackground(String playBackground) {
        this.playBackground = playBackground;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "posterUrl='" + posterUrl + '\'' +
                ", playBackground='" + playBackground + '\'' +
                '}';
    }
}
