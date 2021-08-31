package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybill;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryPlaybillListResponse.java
 * @author: yh
 * @date: 2017-04-25 09:06
 */

public class QueryPlaybillListResponse extends BaseResponse{


    /**
     * result :
     * total : 0
     * channelPlaybills : [""]
     * playbillVersion :
     * extensionFields : [""]
     */

    /**
     *频道总个数。

     说明：
     在查询频道的条件下，不考虑offset/count时符合条件的频道数，方便终端分页。

     */
    @SerializedName("total")
    private String total;

    /**
     *表示节目版本号，取值为JSON格式，格式如下：

     {"channelId": "<频道ID>", "date": "<日期>", "version": "<版本号>"},{"channelId":"<频道ID>","date":"<日期>","version": "<版本号>"},…

     channelId是频道ID

     date是统计日期，格式为yyyyMMdd

     version是频道对应日期的节目版本号；

     由于节目版本号统计到日期一级，所以date和version都是UTC时间。

     注：返回的节目单为空时，本字段不返回

     */
    @SerializedName("playbillVersion")
    private String playbillVersion;

    /**
     *包含多个频道及频道节目单信息，ChannelPlaybill属性请参见“ChannelPlaybill”类型。

     如果没有，不返回该参数。

     */
    @SerializedName("channelPlaybills")
    private List<ChannelPlaybill> channelPlaybills;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPlaybillVersion() {
        return playbillVersion;
    }

    public void setPlaybillVersion(String playbillVersion) {
        this.playbillVersion = playbillVersion;
    }

    public List<ChannelPlaybill> getChannelPlaybills() {
        return channelPlaybills;
    }

    public void setChannelPlaybills(List<ChannelPlaybill> channelPlaybills) {
        this.channelPlaybills = channelPlaybills;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
