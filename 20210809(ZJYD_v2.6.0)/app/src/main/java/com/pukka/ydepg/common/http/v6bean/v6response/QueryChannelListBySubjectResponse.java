package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryChannelListBySubjectResponse.java
 * @author: yh
 * @date: 2017-04-24 17:45
 */

public class QueryChannelListBySubjectResponse {


    /**
     * result :
     * total : 12
     * channelDetails : [""]
     * extensionFields : [""]
     */

    /**
     *返回结果。
     */
    @SerializedName("result")
    private Result result;

    /**
     *频道总个数。
     */
    @SerializedName("total")
    private String total;

    /**
     *频道列表信息。

     如果没有获取到频道，不返回该参数。
     */
    @SerializedName("channelDetails")
    private List<ChannelDetail> channelDetails;

    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ChannelDetail> getChannelDetails() {
        return channelDetails;
    }

    public void setChannelDetails(List<ChannelDetail> channelDetails) {
        this.channelDetails = channelDetails;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
