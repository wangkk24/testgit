package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkFilter;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryBookmarkRequest {


    public static final String MAX_COUNT = "50";

    /**
     * bookmarkTypes :
     * filter :
     * count :
     * offset :
     * sortType :
     * extensionFields :
     */

    /**
     * 书签类型，枚举值参考Bookmark对象的bookmarkType属性
     */
    @SerializedName("bookmarkTypes")
    private List<String> bookmarkTypes;

    /**
     * 过滤条件
     */
    @SerializedName("filter")
    private BookmarkFilter filter;

    /**
     * 一次查询的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误
     */
    @SerializedName("count")
    private String count = "50";

    /**
     * 查询的起始位置，从0开始（即0表示第一个
     */
    @SerializedName("offset")
    private String offset = "0";

    /**
     * 排序方式。取值范围：
     * <p>
     * UPDATE_TIME:ASC：按更新时间升序排列
     * UPDATE_TIME:DESC：按更新时间降序排列
     * 默认值为UPDATE_TIME:DESC
     */
    @SerializedName("sortType")
    private String sortType;

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

    public BookmarkFilter getFilter() {
        return filter;
    }

    public void setFilter(BookmarkFilter filter) {
        this.filter = filter;
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
