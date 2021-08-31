package com.pukka.ydepg.common.http.v6bean.v6response;


import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybillContext;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillContextResponse.java
 * @author: yh
 * @date: 2017-06-23 00:34
 */

public class QueryPlaybillContextResponse extends BaseResponse{

    /**
     * result : result
     * total : total
     * channelPlaybillContexts : ["extensionFields"]
     * extensionFields : ["extensionFields"]
     */

    @SerializedName("total")
    private String total;
    @SerializedName("channelPlaybillContexts")
    private List<ChannelPlaybillContext> channelPlaybillContexts;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ChannelPlaybillContext> getChannelPlaybillContexts() {
        return channelPlaybillContexts;
    }

    public void setChannelPlaybillContexts(List<ChannelPlaybillContext> channelPlaybillContexts) {
        this.channelPlaybillContexts = channelPlaybillContexts;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
