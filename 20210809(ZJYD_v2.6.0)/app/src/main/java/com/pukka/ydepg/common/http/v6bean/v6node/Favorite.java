package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Favorite.java
 * @author: yh
 * @date: 2017-04-21 17:56
 */

public class Favorite implements Serializable{


    /**
     * contentID :
     * contentType :
     * catalogID :
     * entrance :
     * recmActionID :
     * collectTime : 4123
     * extensionFields : [""]
     */

    /**
     *内容ID。

     */
    @SerializedName("contentID")
    private String contentID;

    /**
     *内容类型，取值包括：

     VOD：点播
     CHANNEL：频道
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *收藏夹ID，如果不携带，VSP使用-1:默认收藏夹。

     */
    @SerializedName("catalogID")
    private String catalogID;

    /**
     *推荐页面入口，用于推荐数据采集功能，如果收藏操作不是从推荐位入口进入的，则该字段默认为空。

     */
    @SerializedName("entrance")
    private String entrance;

    /**
     *用户收藏的内容对应的推荐请求流水号，用于推荐数据采集功能，如果收藏操作不是从推荐位入口进入的，则该字默认为空。

     */
    @SerializedName("recmActionID")
    private String recmActionID;

    /**
     *收藏时间，取值为距离1970年1月1号的毫秒数。

     此参数不需要终端设置，当终端查询收藏时，会返回。

     */
    @SerializedName("collectTime")
    private String collectTime;

    /**
     *扩展字段。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getRecmActionID() {
        return recmActionID;
    }

    public void setRecmActionID(String recmActionID) {
        this.recmActionID = recmActionID;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
    /**
     * the range of field contentType
     */
    public interface ContentType
    {
        String VOD = "VOD";
        String CHANNEL = "CHANNEL";
        String VAS = "VAS";
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    /**
     * VOD的扩展属性，其中扩展属性的Key由局点CMS定制。
     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;
}
