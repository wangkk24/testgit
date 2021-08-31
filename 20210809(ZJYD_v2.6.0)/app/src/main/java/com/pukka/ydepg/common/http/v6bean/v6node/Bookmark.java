package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Bookmark.java
 * @author: yh
 * @date: 2017-04-21 15:44
 */

public class Bookmark implements Serializable {

    public static final String BOOKMARKTYPE_VOD = "VOD";
    public static final String BOOKMARKTYPE_PROGRAM = "PROGRAM";
    public static final String BOOKMARKTYPE_NPVR = "NPVR";
    public static final String BOOKMARKTYPE_CPVR = "CPVR";

    /**
     * bookmarkType :
     * itemID :
     * rangeTime : 123
     * subContentID :
     * subContentType :
     * updateTime : 123
     * sitcomNO : 123
     * extensionFields : [""]
     */

    /**
     *书签类型，取值包括：

     VOD：点播书签
     PROGRAM：回看节目单书签
     NPVR：NPVR书签
     CPVR：CPVR书签
     */
    @SerializedName("bookmarkType")
    private String bookmarkType;

    /**
     *书签内容ID。

     如果是VOD书签，取值为VOD内容ID。
     如果是PROGRAM书签，取值为节目单ID。
     如果是NPVR或者CPVR书签，取值是录制计划ID。
     */
    @SerializedName("itemID")
    private String itemID;

    /**
     *书签时间点，也就是中途退出播放的断点距离节目开始的秒数。
     */
    @SerializedName("rangeTime")
    private String rangeTime;

    /**
     *如果是VOD书签且itemID是连续剧，此属性表示子集ID。
     */
    @SerializedName("subContentID")
    private String subContentID;

    /**
     *subContentID对应的内容类型，取值包括：

     VOD：点播

     说明：
     如果终端请求包含subContentID，subContentType也必须同时携带。
     平台后续会增加一种特殊的VAS书签，
     此VAS会关联多个VOD、频道和节目单，
     当用户播放VAS下的VOD、频道或者节目单时中途退出，
     平台会保存此中途退出的内容，
     所以subContentType会为此书签预留，目前只存在VOD。
     */
    @SerializedName("subContentType")
    private String subContentType;

    /**
     *书签更新时间，取值为距离1970年1月1号的毫秒数。

     此参数不需要终端设置，当终端查询书签时，VSP会返回。
     */
    @SerializedName("updateTime")
    private String updateTime;

    /**
     *返回subContentID的集号。

     此参数不需要终端设置，当终端查询连续剧书签时，VSP会返回。
     */
    @SerializedName("sitcomNO")
    private String sitcomNO;

    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getBookmarkType() {
        return bookmarkType;
    }

    public void setBookmarkType(String bookmarkType) {
        this.bookmarkType = bookmarkType;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getRangeTime() {
        return rangeTime;
    }

    public void setRangeTime(String rangeTime) {
        this.rangeTime = rangeTime;
    }

    public String getSubContentID() {
        return subContentID;
    }

    public void setSubContentID(String subContentID) {
        this.subContentID = subContentID;
    }

    public String getSubContentType() {
        return subContentType;
    }

    public void setSubContentType(String subContentType) {
        this.subContentType = subContentType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public Bookmark() {
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
