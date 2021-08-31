package com.pukka.ydepg.moudule.vrplayer.vrplayer.module;

public class Picture {

    /**
     * 首页海报的url
     */
    private  String posterUrl;

    public String getPosterUrl() {return posterUrl;}

    public void setPosterUrl(String posterUrl) {this.posterUrl = posterUrl; }

    @Override
    public String toString() {
        return "Picture{" +
                "posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
