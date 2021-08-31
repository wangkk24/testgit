package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: SearchFilter.java
 * @author: yh
 * @date: 2017-04-21 17:40
 */
public class SearchFilter {

    /**
     * 流派名称。
     */
    @SerializedName("genreName")
    private String genreName;

    /**
     * 出品国家的缩写或者地区编号。
     */
    @SerializedName("produceZoneID")
    private String produceZoneID;

    /**
     * 对内容名称首字母进行过滤，取值为对应用户语系的字母和0-9的10个数字。
     */
    @SerializedName("initial")
    private String initial;

    /**
     * 搜索节目单时(含直播节目单和回看节目单)指定节目单搜索范围，包括：
     *
     * 0：所有频道
     * 1：已订购频道
     * 2：未订购频道
     * 3：指定频道ID
     * 默认值是0，该条件不支持多值，对于已订购频道的判断逻辑和搜索已订购频道的实现一致，即只要订购了直播特性，频道就算已订购，同时已订购的判断不需要检查产品的限制条件是否满足
     */
    @SerializedName("channelScope")
    private String channelScope;

    /**
     * 如果搜索VOD内容，指定搜索的VOD类型范围。
     *
     * 0：非电视剧
     * 1：普通连续剧
     * 2：季播剧父集
     * 3：季播剧
     * 如果为空，表示不进行此维度的过滤。
     */
    @SerializedName("VODTypes")
    private List<Integer> VODTypes;

    //如果ChannelScope=3，指定频道ID。
    @SerializedName("channelIDs")
    private List<String> channelIDs;

    //内容CPID列表。仅对VOD生效
    @SerializedName("cpIDList")
    private List<String> cpIDList;

    //栏目ID。如果需要根据根栏目ID进行过滤，可以先查出对应的栏目ID再传入此字段。
    @SerializedName("subjectID")
    private List<String> subjectIDs;

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getProduceZoneID() {
        return produceZoneID;
    }

    public void setProduceZoneID(String produceZoneID) {
        this.produceZoneID = produceZoneID;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getChannelScope() {
        return channelScope;
    }

    public void setChannelScope(String channelScope) {
        this.channelScope = channelScope;
    }

    public List<Integer> getVODTypes() {
        return VODTypes;
    }

    public void setVODTypes(List<Integer> VODTypes) {
        this.VODTypes = VODTypes;
    }

    public List<String> getChannelIDs() {
        return channelIDs;
    }

    public void setChannelIDs(List<String> channelIDs) {
        this.channelIDs = channelIDs;
    }

    public List<String> getCpIDList() {
        return cpIDList;
    }

    public void setCpIDList(List<String> cpIDList) {
        this.cpIDList = cpIDList;
    }

    public List<String> getSubjectIDs() {
        return subjectIDs;
    }

    public void setSubjectIDs(List<String> subjectIDs) {
        this.subjectIDs = subjectIDs;
    }
}
