package com.pukka.ydepg.launcher.http.hecaiyun.data;

import com.google.gson.annotations.SerializedName;

public class CloudContent {
    @SerializedName("bigthumbnailURL")
    private String bigthumbnailURL;

    @SerializedName("contentID")
    private String contentID;

    @SerializedName("contentName")
    private String contentName;

    @SerializedName("contentSize")
    private String contentSize;

    @SerializedName("contentSuffix")
    private String contentSuffix;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("extInfo")
    private ExtInfo extInfo;

    @SerializedName("midthumbnailURL")
    private String midthumbnailURL;

    @SerializedName("modifier")
    private String modifier;

    @SerializedName("parentCatalogID")
    private String parentCatalogID;

    @SerializedName("presentHURL")
    private String presentHURL;

    @SerializedName("presentLURL")
    private String presentLURL;

    @SerializedName("presentURL")
    private String presentURL;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public String getBigthumbnailURL() {
        return bigthumbnailURL;
    }

    public void setBigthumbnailURL(String bigthumbnailURL) {
        this.bigthumbnailURL = bigthumbnailURL;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentSize() {
        return contentSize;
    }

    public void setContentSize(String contentSize) {
        this.contentSize = contentSize;
    }

    public String getContentSuffix() {
        return contentSuffix;
    }

    public void setContentSuffix(String contentSuffix) {
        this.contentSuffix = contentSuffix;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ExtInfo getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(ExtInfo extInfo) {
        this.extInfo = extInfo;
    }

    public String getMidthumbnailURL() {
        return midthumbnailURL;
    }

    public void setMidthumbnailURL(String midthumbnailURL) {
        this.midthumbnailURL = midthumbnailURL;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getParentCatalogID() {
        return parentCatalogID;
    }

    public void setParentCatalogID(String parentCatalogID) {
        this.parentCatalogID = parentCatalogID;
    }

    public String getPresentHURL() {
        return presentHURL;
    }

    public void setPresentHURL(String presentHURL) {
        this.presentHURL = presentHURL;
    }

    public String getPresentLURL() {
        return presentLURL;
    }

    public void setPresentLURL(String presentLURL) {
        this.presentLURL = presentLURL;
    }

    public String getPresentURL() {
        return presentURL;
    }

    public void setPresentURL(String presentURL) {
        this.presentURL = presentURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
