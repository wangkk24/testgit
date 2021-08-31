package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryBookmarkResponse extends BaseResponse{

    /**
     * result :
     * total :
     * version :
     * bookmarks :
     * extensionFields :
     */

    /**
     * 如果查询成功，返回条件匹配的总记录数。
     * 如果查询失败，该参数不返回
     */
    @SerializedName("total")
    private String total;

    /**
     * 如果查询成功，返回书签版本号。
     * 如果查询失败，该参数不返回
     */
    @SerializedName("version")
    private String version;

    /**
     * 如果查询成功，返回个性化书签信息。
     * 如果查询失败，该参数不返回
     */
    @SerializedName("bookmarks")
    private List<BookmarkItem> bookmarks;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<BookmarkItem> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<BookmarkItem> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
