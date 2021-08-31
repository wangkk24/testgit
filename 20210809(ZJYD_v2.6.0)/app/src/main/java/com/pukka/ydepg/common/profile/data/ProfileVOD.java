package com.pukka.ydepg.common.profile.data;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;

import java.util.List;

public class ProfileVOD {

    //VOD类型。取值包括：0: 非电视剧,1: 普通连续剧,2: 季播剧父集,3: 季播剧
    @SerializedName("VODType")
    private String vodType;

    //如果VODType是连续剧/季播剧的类型，字段必填。取值包括：0: 普通连续剧,1: Movie Series
    private String seriesType;

    //内容归属的内容提供商ID
    private String cpId;

    //中途退出播放的断点距离节目开始的秒数
    private String rangeTime;

    private Picture picture;

    //内容ID
    @SerializedName("ID")
    private String id;

    //第三方系统分配的内容Code
    private String code;

    //内容名称
    private String name;

    /**
     *VOD的扩展属性，其中扩展属性的Key由局点CMS定制。

     */
    @SerializedName("customFields")
    protected List<NamedParameter> customFields;

    /**
     *书签。

     */
    @SerializedName("bookmark")
    protected Bookmark bookmark;

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public String getVodType() {
        return vodType;
    }

    public void setVodType(String vodType) {
        this.vodType = vodType;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getRangeTime() {
        return rangeTime;
    }

    public void setRangeTime(String rangeTime) {
        this.rangeTime = rangeTime;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
