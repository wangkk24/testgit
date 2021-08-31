package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybillContext;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillContextRequest.java
 * @author: yh
 * @date: 2017-06-23 00:34
 */

public class QueryPlaybillContextRequest {

    /**
     * queryChannel : queryChannel
     * queryPlaybillContext : queryPlaybillContext
     * needChannel : needChannel
     * extensionFields : ["extensionFields"]
     */

    @SerializedName("queryChannel")
    private QueryChannel queryChannel;
    @SerializedName("queryPlaybillContext")
    private QueryPlaybillContext queryPlaybillContext;
    @SerializedName("needChannel")
    private String needChannel;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public QueryChannel getQueryChannel() {
        return queryChannel;
    }

    public void setQueryChannel(QueryChannel queryChannel) {
        this.queryChannel = queryChannel;
    }

    public QueryPlaybillContext getQueryPlaybillContext() {
        return queryPlaybillContext;
    }

    public void setQueryPlaybillContext(QueryPlaybillContext queryPlaybillContext) {
        this.queryPlaybillContext = queryPlaybillContext;
    }

    public String getNeedChannel() {
        return needChannel;
    }

    public void setNeedChannel(String needChannel) {
        this.needChannel = needChannel;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
