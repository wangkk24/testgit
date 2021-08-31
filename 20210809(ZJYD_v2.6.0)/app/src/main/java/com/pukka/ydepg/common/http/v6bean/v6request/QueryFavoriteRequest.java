package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryFavoriteRequest {

    /**
     * catalogID :
     * contentTypes :
     * count :
     * offset :
     * sortType :
     * extensionFields :
     */

    /**
     * 查询指定收藏夹下的内容。
     * <p>
     * 如果没有传值，表示查询所有收藏夹下的收藏内容。
     * 如果取值是-1，表示查询默认收藏夹下的收藏内容
     */
    @SerializedName("catalogID")
    private String catalogID;

    /**
     * 查询指定内容类型的收藏。
     * <p>
     * 如果不指定，将查询指定收藏夹或者所有收藏夹下的内容。
     * <p>
     * 枚举值参考Favorite对象的contentType
     */
    @SerializedName("contentTypes")
    private List<String> contentTypes;

    /**
     * 一次查询的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误
     */
    @SerializedName("count")
    private String count;

    /**
     * 查询的起始位置，从0开始（即0表示第一个
     */
    @SerializedName("offset")
    private String offset;

    /**
     * 收藏排序方式，取值包括：
     * <p>
     * FAVO_TIME:ASC：按收藏时间升序排列
     * FAVO_TIME:DESC：按收藏时间降序排列
     * SORT_NO:ASC：按照收藏序号升序排列
     * SORT_NO:DESC：按照收藏序号降序排列
     * CHAN_NO:ASC：按照频道号升序排列
     * CHAN_NO:DESC：按照频道号降序排列
     * 默认值为FAVO_TIME:ASC
     */
    @SerializedName("sortType")
    private String sortType;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
