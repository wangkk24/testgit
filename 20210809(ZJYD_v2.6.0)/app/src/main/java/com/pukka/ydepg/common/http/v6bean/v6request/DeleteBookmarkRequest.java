package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class DeleteBookmarkRequest {

    /**
     * bookmarkTypes :
     * itemIDs :
     * deleteType :
     * extensionFields :
     */

    /**
     * 书签类型，枚举值参考Bookmark对象的bookmarkType属性
     */
    @SerializedName("bookmarkTypes")
    private List<String> bookmarkTypes;

    /**
     * 书签内容ID。
     * <p>
     * 如果是VOD书签，取值为VOD内容ID。
     * 如果是Catch-up TV书签，取值为节目单ID。
     * 如果是NPVR书签，取值是录制计划ID。
     * 如果是CPVR书签，取值是录制计划ID。
     * 说明：
     * 由于不同书签类型的itemID可能冲突(主要是后续提供的PVR任务ID和内容ID不唯一)，如果终端请求包含itemIDS，必须同时携带bookmarkTypes，而且itemIDS和bookmarkTypes元素是1对1的关系
     */
    @SerializedName("itemIDs")
    private List<String> itemIDs;

    /**
     * 删除类型。
     * <p>
     * 0 : 按书签内容ID删除，根据传入的书签内容ID删除指定的书签数据
     * <p>
     * 1 : 按书签内容ID反选删除，保留传入的书签内容ID，其他的书签数据全部删除
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

    public List<String> getBookmarkTypes() {
        return bookmarkTypes;
    }

    public void setBookmarkTypes(List<String> bookmarkTypes) {
        this.bookmarkTypes = bookmarkTypes;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
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
