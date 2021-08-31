package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.RecmVODFilter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/23.
 */

public class QueryRecmVODListRequest {
    /**
     * 一次查询的VOD的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误。
     */
    @SerializedName("count")
    private String count;
    /**
     *查询的子栏目的起始位置。从0开始（即0表示第一个）。
     */
    @SerializedName("offset")
    private String offset;
    /**
     * 推荐类型，取值范围：
     1：今日更新影片
     2：最新上线
     3：即将下线
     4：热点排行
     5：周排行
     6：强档推荐
     */
    @SerializedName("action")
    private String action;
    /**
     VOD排序方式。只在“action”为“4”和“5”时有效。
     PLAYTIMES:DESC：按VOD点击率降序排序
     AVERAGESCORE:DESC：按评价均值降序排序
     如果不传该参数，默认按照VOD上线时间降序排列。
     */
    @SerializedName("sortType")
    private String sortType;
    /**
     * 栏目ID，如果携带subjectID则查询属于该栏目的所有推荐影片。该栏目可以是父栏目。如不携带subjectID，则查询所有的推荐影片。
     */
    @SerializedName("subjectID")
    private String subjectID;
    /**
     * 推荐VOD的展示位置。
     “position”不为空时，则首先查询对应位置配置的推荐内容列表，然后根据“action”查询对应的内容列表，并组合起来返回给客户端。
     如果“position”为空时，根据“action”查询对应的内容列表，并返回给客户端。
     说明：
     该参数只在“action”为2,3,4,6时有效，和展示内容的维护功能对应。
     */
    @SerializedName("position")
    private String position;
    /**VOD类型。取值范围：
     0：非电视剧
     1：普通连续剧
     2：季播剧父集
     3：季播剧
     说明：
     该参数为空表示查所有
     *
     */
    @SerializedName("VODTypes")
    private List<String> VODTypes;
    /**
     * 推荐过滤条件。
     RecmVODFilter参考“RecmVODFilter”。
     */
    @SerializedName("recmVODFilter")
    private RecmVODFilter recmVODFilter;


    public String getCount() {
        return count;
    }

    public String getOffset() {
        return offset;
    }

    public String getAction() {
        return action;
    }

    public String getSortType() {
        return sortType;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public String getPosition() {
        return position;
    }

    public List<String> getVODTypes() {
        return VODTypes;
    }

    public RecmVODFilter getRecmVODFilter() {
        return recmVODFilter;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setVODTypes(List<String> VODTypes) {
        this.VODTypes = VODTypes;
    }

    public void setRecmVODFilter(RecmVODFilter recmVODFilter) {
        this.recmVODFilter = recmVODFilter;
    }
}
