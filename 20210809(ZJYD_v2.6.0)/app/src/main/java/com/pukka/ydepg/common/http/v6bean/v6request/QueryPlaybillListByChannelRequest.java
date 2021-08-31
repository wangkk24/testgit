package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryChannelContext;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybill;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryPlaybillListRequest.java
 * @author: yh
 * @date: 2017-04-25 09:06
 */

public class QueryPlaybillListByChannelRequest {


    /**
     * queryChannel :
     * queryPlaybill :
     * needChannel : 11
     * extensionFields : [""]
     */

    /**
     *频道查询条件。

     */
    @SerializedName("queryChannelContext")
    private QueryChannelContext queryChannelContext;

    /**
     *节目单查询条件。

     该条件为空时，不返回频道关联的节目单。

     */
    @SerializedName("queryPlaybill")
    private QueryPlaybill queryPlaybill;

    /**
     *在返回对象中是否需要返回频道信息（即ChannelPlaybill中要不要封装频道信息）。

     1：需要，
     0：不需要
     默认为1。

     */
    @SerializedName("needChannel")
    private String needChannel;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public QueryChannelContext getQueryChannelContext() {
        return queryChannelContext;
    }

    public void setQueryChannelContext(QueryChannelContext queryChannelContext) {
        this.queryChannelContext = queryChannelContext;
    }

    public QueryPlaybill getQueryPlaybill() {
        return queryPlaybill;
    }

    public void setQueryPlaybill(QueryPlaybill queryPlaybill) {
        this.queryPlaybill = queryPlaybill;
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
