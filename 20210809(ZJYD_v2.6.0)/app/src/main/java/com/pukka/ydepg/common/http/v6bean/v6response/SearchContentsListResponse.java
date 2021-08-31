package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.SearchContentsListResponse.java
 * @author:xj
 * @date: 2017-12-18 14:38
 */

public class SearchContentsListResponse extends BaseResponse{
    /**
     * 热门搜索内容总个数，空的时候为0。
     */
    @SerializedName("total")
    private String total;
    /**
     * 热门搜索内容列表。如果查询结果为空，不返回该参数
     */
    @SerializedName("hotContent")
    private List<Content> hotContent;
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

    public List<Content> getHotContent() {
        return hotContent;
    }

    public void setHotContent(List<Content> hotContent) {
        this.hotContent = hotContent;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
