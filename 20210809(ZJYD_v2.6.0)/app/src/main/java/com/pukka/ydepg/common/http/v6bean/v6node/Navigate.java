package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Navigate
{
    @SerializedName("id")
    private String id;
    @SerializedName("img")
    private String image;//默认图片
    @SerializedName("name")
    private List<Dialect> nameDialect;
    @SerializedName("description")
    private List<Dialect> descriptionDialect;
    @SerializedName("focusImg")
    private String focusImage;//落焦图片
    @SerializedName("secFocusImg")
    private String secFocusImg;//次落焦图片
    @SerializedName("actionURL")
    private String actionURL;
    @SerializedName("index")
    private String index;
    @SerializedName("focusColor")
    private String focusColor;
    @SerializedName("pageList")
    private List<Page> pageList;

    @SerializedName("extraData")
    private Map<String,String> ExtraData;

    public Map<String, String> getExtraData() {
        return ExtraData;
    }

    public void setExtraData(Map<String, String> extraData) {
        ExtraData = extraData;
    }

    public String getSecondaryTitleImg() {
        return secFocusImg;
    }

    public void setSecondaryTitleImg(String secondaryTitleImg) {
        this.secFocusImg = secondaryTitleImg;
    }

    public String getFocusColor() {
        return focusColor;
    }

    public void setFocusColor(String focusColor) {
        this.focusColor = focusColor;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getImage() { return image;}

    public void setImage(String image) { this.image = image;}

    public String getFocusImage() { return focusImage;}

    public void setFocusImage(String focusImage) { this.focusImage = focusImage;}

    public List<Page> getPageList() { return pageList;}

    public void setPageList(List<Page> pageList) { this.pageList = pageList;}

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

    public void setDescriptionDialect(List<Dialect> descriptionDialect)
    {
        this.descriptionDialect = descriptionDialect;
    }

    public String getActionURL()
    {
        return actionURL;
    }

    public void setActionURL(String actionURL)
    {
        this.actionURL = actionURL;
    }


}