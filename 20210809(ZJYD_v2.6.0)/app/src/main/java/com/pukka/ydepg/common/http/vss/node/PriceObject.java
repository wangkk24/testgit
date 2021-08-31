package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PriceObject implements Serializable {

    private static final long serialVersionUID = 3813691752866294040L;

    /**
     * 定价对象Id
     */
    @SerializedName("id")
    private String id;

    /**
     * 类型
     */
    @SerializedName("type")
    private String type;

    /**
     * 扩展字段
     */
    @SerializedName("extensionInfo")
    private List<NamedParameter> extensionInfo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NamedParameter> getExtensionInfo() {
        return extensionInfo;
    }

    public void setExtensionInfo(List<NamedParameter> extensionInfo) {
        this.extensionInfo = extensionInfo;
    }
}
