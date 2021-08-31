package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Cast.java
 * @author: yh
 * @date: 2017-04-21 15:54
 */

public class Cast implements Serializable {


    /**
     * castID :
     * castName :
     * castCode :
     * picture :
     * extensionFields : [""]
     */

    /**
     *演职员编号。
     */
    @SerializedName("castID")
    private String castID;

    /**
     *演职员名称。

     */
    @SerializedName("castName")
    private String castName;

    /**
     *第三方系统分配的Code。

     */
    @SerializedName("castCode")
    private String castCode;

    /**
     *演职员图片。

     */
    @SerializedName("picture")
    private Picture picture;

    /**
     *扩展字段。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCastID() {
        return castID;
    }

    public void setCastID(String castID) {
        this.castID = castID;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getCastCode() {
        return castCode;
    }

    public void setCastCode(String castCode) {
        this.castCode = castCode;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public Cast() {
    }
}
