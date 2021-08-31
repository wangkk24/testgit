package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.utils.GlideUtil;

import java.util.List;

public class Group {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private List<Dialect> nameDialect;
    @SerializedName("description")
    private List<Dialect> descriptionDialect;
    @SerializedName("type")
    private String type;
    @SerializedName("extraData")
    private ExtraData extraData;
    @SerializedName("background")
    private String backgroud;
    @SerializedName("index")
    private String index;
    @SerializedName("apiURL")
    private String apiURL;
    @SerializedName("categoryCode")
    private String categoryCode;
    @SerializedName("controlInfo")
    private ControlInfo controlInfo;
    @SerializedName("groupHeight")
    private String groupHeight;


    public String getGroupHeight() {
        return groupHeight;
    }

    public void setGroupHeight(String groupHeight) {
        this.groupHeight = groupHeight;
    }


    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public List<Dialect> getNameDialect()
    {
        return nameDialect;
    }

    public void setNameDialect(List<Dialect> nameDialect)
    {
        this.nameDialect = nameDialect;
    }

    public List<Dialect> getDescriptionDialect()
    {
        return descriptionDialect;
    }

    public void setDescriptionDialect(List<Dialect> descriptionDialect) {
        this.descriptionDialect = descriptionDialect;
    }

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public String getBackgroud() {
        return GlideUtil.getUrl(backgroud);
    }

    public void setBackgroud(String backgroud) {
        this.backgroud = backgroud;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getApiURL() { return apiURL;}

    public void setApiURL(String apiURL) { this.apiURL = apiURL;}

    public String getCategoryCode() { return categoryCode;}

    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode;}

    public ControlInfo getControlInfo()
    {
        return controlInfo;
    }

    public void setControlInfo(ControlInfo controlInfo)
    {
        this.controlInfo = controlInfo;
    }
}