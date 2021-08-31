package com.pukka.ydepg.common.profile.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ControlInfo {

    //列表类型 枚举值：0人工推荐，1智能推荐, 2观看记录
    private String types;

    //楼层中文名称
    private String subjectName;

    //样式ID
    private String controlID;

    //控件标题颜色
    @SerializedName("titleColor")
    private String titleColor;

    //楼层数据数组
    @SerializedName("VODs")
    private List<ProfileVOD> vodList;


    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getControlID() {
        return controlID;
    }

    public void setControlID(String controlID) {
        this.controlID = controlID;
    }

    public List<ProfileVOD> getVodList() {
        return vodList;
    }

    public void setVodList(List<ProfileVOD> vodList) {
        this.vodList = vodList;
    }
}
