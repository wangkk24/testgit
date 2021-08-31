package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6node.ExtraData.java
 * @date: 2018-03-15 15:25
 * @version: V1.0 描述当前版本功能
 */


public class ExtraData {
    //跑马灯
    @SerializedName("Marquee")
    private String Marquee;
    //时间轴时间显示的位置left/middle
    @SerializedName("timeline_position")
    private String timeline_position;
    //桌面智能推荐位场景类型，枚举值为202：内容推荐、203：专题推荐、204：活动推荐
    @SerializedName("sceneId")
    private String sceneId;

    //2.3 智能推荐Id
    @SerializedName("appointedId")
    private String appointedId;

    //2.4  通栏运营位仅对指定用户展示 1:指定用户；=0：不指定用户，默认为0
    @SerializedName("use_cimp_data")
    private String use_cimp_data;

    //2.5  控件标题颜色
    @SerializedName("titleColor")
    private String titleColor;

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getUse_cimp_data() {
        return use_cimp_data;
    }

    public void setUse_cimp_data(String use_cimp_data) {
        this.use_cimp_data = use_cimp_data;
    }

    public String getSceneId() {
        return sceneId;
    }

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedIds) {
        this.appointedId = appointedIds;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getTimeline_position() {
        return timeline_position;
    }

    public void setTimeline_position(String timeline_position) {
        this.timeline_position = timeline_position;
    }

    public String getMarquee() {
        return Marquee;
    }

    public void setMarquee(String marquee) {
        this.Marquee = marquee;
    }
}
