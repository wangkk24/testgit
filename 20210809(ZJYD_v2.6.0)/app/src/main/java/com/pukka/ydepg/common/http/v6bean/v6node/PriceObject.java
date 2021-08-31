package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class PriceObject {

    /**
     * ID :
     * type :
     * contentType :
     * extensionFields :
     */

    /**
     *定价对象ID
     */
    @SerializedName("ID")
    private String ID;

    /**
     *定价对象类型。
     * 取值范围：
     * 1：内容&媒资特性[预留]
     * 2：内容
     * 3：栏目
     * 4：媒资[预留]
     * 5：全局特性[预留]
     */
    @SerializedName("type")
    private String type;

    /**
     *type=2时，表示内容对应的内容类型，具体取值包括：
     * VOD：点播
     * CHANNEL：频道
     * PROGRAM：节目单
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
