package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryDynamicRecmContent;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryRecmContentRequest {

    /**
     * queryDynamicRecmContent :
     * extensionFields :
     */

    /**
     * 推荐场景参数
     */
    @SerializedName("queryDynamicRecmContent")
    private QueryDynamicRecmContent queryDynamicRecmContent;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public QueryDynamicRecmContent getQueryDynamicRecmContent() {
        return queryDynamicRecmContent;
    }

    public void setQueryDynamicRecmContent(QueryDynamicRecmContent queryDynamicRecmContent) {
        this.queryDynamicRecmContent = queryDynamicRecmContent;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
