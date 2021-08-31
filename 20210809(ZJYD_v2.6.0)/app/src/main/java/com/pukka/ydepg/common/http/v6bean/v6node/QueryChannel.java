package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryChannel.java
 * @author: yh
 * @date: 2017-04-25 09:42
 */

public class QueryChannel {


    /**
     * subjectID :
     * channelIDs : [""]
     * channelNOs : [12]
     * count : 12
     * offset : 12
     * contentType :
     * channelFilter :
     * extensionFields : [""]
     */

    /**
     *栏目ID。

     取值仅支持：

     -1
     叶子栏目
     其中栏目ID不传、或传-1时，表示查询所有频道，不是按照栏目递归查询频道；当栏目ID为叶子栏目时，表示获取此叶子栏目下的频道列表。

     说明：
     如果传入了channelIDs或者channelNOs，则忽略该参数

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *一次查询的频道总条数。

     不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过30，最大条数可配置，超过最大条数返回错误。

     说明：
     针对根据栏目ID查询频道列表的场景有效。

     */
    @SerializedName("count")
    private String count;

    /**
     *查询的起始位置。默认值为0，表示从第一个频道开始。

     说明：
     针对根据栏目ID查询频道列表的场景有效。

     */
    @SerializedName("offset")
    private String offset;

    /**
     *内容类型。

     取值范围：

     CHANNEL
     AUDIO_CHANNEL
     VIDEO_CHANNEL
     该参数不传，表示所有频道都进行查询。

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *搜索条件。

     */
    @SerializedName("channelFilter")
    private ChannelFilter channelFilter;

    /**
     *频道ID列表，channelID和channelNOs二选一，如果这两个参数有入参取值，则channelID、channelNOs、count之外的其他查询条件全部忽略。

     个数不超过count的限制。

     */
    @SerializedName("channelIDs")
    private List<String> channelIDs;

    /**
     *频道号列表，channelID和channelNOs二选一，如果这两个字段取值，则channelID、channelNOs、count之外的其他查询条件全部忽略。

     个数不超过count的限制。

     */
    @SerializedName("channelNOs")
    private List<String> channelNOs;

    @SerializedName("isReturnAllMedia")
    private String isReturnAllMedia;

    public String getIsReturnAllMedia() {
        return isReturnAllMedia;
    }

    public void setIsReturnAllMedia(String isReturnAllMedia) {
        this.isReturnAllMedia = isReturnAllMedia;
    }

    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ChannelFilter getChannelFilter() {
        return channelFilter;
    }

    public void setChannelFilter(ChannelFilter channelFilter) {
        this.channelFilter = channelFilter;
    }

    public List<String> getChannelIDs() {
        return channelIDs;
    }

    public void setChannelIDs(List<String> channelIDs) {
        this.channelIDs = channelIDs;
    }

    public List<String> getChannelNOs() {
        return channelNOs;
    }

    public void setChannelNOs(List<String> channelNOs) {
        this.channelNOs = channelNOs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
