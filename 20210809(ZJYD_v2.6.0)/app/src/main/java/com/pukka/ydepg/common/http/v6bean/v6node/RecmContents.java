package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: RecmContents.java
 * @author: yh
 * @date: 2017-04-24 13:04
 */

public class RecmContents {


    /**
     * total : 12
     * recmContentType : 123
     * recmVODs : [""]
     * recmChannels : [""]
     * recmPrograms : [""]
     * extensionFields : [""]
     */

    /**
     *推荐记录总数。

     若实际条数少于请求推荐的条目数，则返回实际条目数。

     */
    @SerializedName("total")
    private String total;

    /**
     *推荐内容类型，取值包括：

     VOD：点播内容
     CHANNEL：频道内容
     PROGRAM：节目单内容

     */
    @SerializedName("recmContentType")
    private String recmContentType;

    /**
     *点播内容描述，当推荐内容类型为VOD时有效。

     */
    @SerializedName("recmVODs")
    private List<VOD> recmVODs;

    /**
     *频道内容描述，当推荐内容类型为CHANNEL时有效
     */
    @SerializedName("recmChannels")
    private List<Channel> recmChannels;

    /**
     *1.节目单内容描述，当推荐内容类型为PROGRAM 时有效。

     2.推荐结果返回热点直播节目推荐列表时（即针对推荐位名称entrance= Program_Whats_On或推荐类型recmType=5：正在热播节目），推荐的直播节目单列表按照节目播放次数从高到低排序

     */
    @SerializedName("recmPrograms")
    private List<ChannelPlaybill> recmPrograms;

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

    public String getRecmContentType() {
        return recmContentType;
    }

    public void setRecmContentType(String recmContentType) {
        this.recmContentType = recmContentType;
    }

    public List<VOD> getRecmVODs() {
        return recmVODs;
    }

    public void setRecmVODs(List<VOD> recmVODs) {
        this.recmVODs = recmVODs;
    }

    public List<Channel> getRecmChannels() {
        return recmChannels;
    }

    public void setRecmChannels(List<Channel> recmChannels) {
        this.recmChannels = recmChannels;
    }

    public List<ChannelPlaybill> getRecmPrograms() {
        return recmPrograms;
    }

    public void setRecmPrograms(List<ChannelPlaybill> recmPrograms) {
        this.recmPrograms = recmPrograms;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
