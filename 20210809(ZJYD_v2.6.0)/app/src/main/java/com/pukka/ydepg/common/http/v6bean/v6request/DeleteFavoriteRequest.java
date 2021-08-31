package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class DeleteFavoriteRequest {

    /**
     * catalogID :
     * contentTypes :
     * contentIDs :
     * deleteType :
     * extensionFields :
     */

    /**
     * 删除指定收藏夹下的内容。
     * 如果没有传值，表示删除所有收藏夹下的收藏内容。
     * 如果取值是-1，表示删除默认收藏夹下的收藏内容。
     */
    @SerializedName("catalogID")
    private String catalogID;

    /**
     * 删除指定内容类型的收藏。
     * <p>
     * 枚举值参考Favorite对象的contentType
     */
    @SerializedName("contentTypes")
    private List<String> contentTypes;

    /**
     * 删除指定内容ID的收藏
     */
    @SerializedName("contentIDs")
    private List<String> contentIDs;

    /**
     * 删除类型。
     * <p>
     * 0 : 按收藏内容ID删除，根据传入的收藏内容ID删除指定的收藏（在同一个contentType下）
     * <p>
     * 1 : 按收藏内容ID反选删除，保留传入的收藏内容ID，此收藏夹的其余收藏数据全部删除（在同一个contentType下）
     * <p>
     * 此字段默认为0
     */
    @SerializedName("deleteType")
    private String deleteType = "0";

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public void addContentTypes(String string) {
        if (getContentTypes() == null)
            contentTypes = new ArrayList<>();
        contentTypes.add(string);
    }

    public List<String> getContentIDs() {
        return contentIDs;
    }

    public void setContentIDs(List<String> contentIDs) {
        this.contentIDs = contentIDs;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
