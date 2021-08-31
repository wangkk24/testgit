package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class SuggestKeywordRequest {

    /**
     * count :
     * key :
     * extensionFields :
     */

    /**
     * 一次查询的总条数。建议不超过50
     */
    @SerializedName("count")
    private String count;

    /**
     * 搜索关键字
     */
    @SerializedName("key")
    private String key;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
