package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.ChannelFilter;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryChannelListBySubjectRequest.java
 * @author: yh
 * @date: 2017-04-24 17:44
 */

public class QueryChannelListBySubjectRequest {

    /**
     * subjectID :
     * count : 12
     * offset : 0
     * contentTypes : [""]
     * channelNamespace :
     * filter :
     * extensionFields : [""]
     */

    /**
     *栏目ID，取值仅支持：

     -1
     叶子栏目
     其中栏目ID不传、或传-1时，表示查询所有频道；当栏目ID为叶子栏目是，表示获取此叶子栏目下的频道列表。

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *一次查询的总条数，不能设置为-1，终端一定要指定获取数据的总条数，最大条数默认不超过1000，最大条数可配置，超过最大条数返回错误。
     */
    @SerializedName("count")
    private String count;

    /**
     *查询的起始位置。默认值为0，表示从第一个频道开始。
     */
    @SerializedName("offset")
    private String offset;

    /**
     *频道查询使用的命名空间，如果不指定，VSP将按照当前用户会话保存的命名空间返回。
     */
    @SerializedName("channelNamespace")
    private String channelNamespace;

    /**
     *搜索条件。
     */
    @SerializedName("filter")
    private ChannelFilter filter;

    /**
     *内容类型。

     取值范围：

     CHANNEL
     AUDIO_CHANNEL
     VIDEO_CHANNEL
     说明：
     为空时，表示获取所有类型频道
     */
    @SerializedName("contentTypes")
    private List<String> contentTypes;

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

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public ChannelFilter getFilter() {
        return filter;
    }

    public void setFilter(ChannelFilter filter) {
        this.filter = filter;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
