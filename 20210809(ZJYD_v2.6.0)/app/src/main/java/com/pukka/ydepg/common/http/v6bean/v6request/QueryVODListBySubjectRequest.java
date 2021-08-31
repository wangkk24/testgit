package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryVODListBySubjectRequest.java
 * @author: yh
 * @date: 2017-04-25 10:46
 */

public class QueryVODListBySubjectRequest {


    /**
     * subjectID :
     * sortType :
     * count : 1
     * offset : 1
     * extensionFields : [""]
     * VODFilter :
     * VODExcluder :
     */

    /**
     *栏目ID（可以是父栏目或者叶子栏目），查询此栏目下的VOD列表。如不传该参数，表示查询根栏目下所有叶子栏目(包括父栏目下的叶子栏目)下绑定的普通VOD和父集VOD。

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *VOD排序方式。以下列方式指定排序方式：

     STARTTIME:ASC：按VOD上线时间升序排序
     STARTTIME:DESC：按VOD上线时间降序排序
     AVGSCORE:ASC：按VOD评分均值升序排序
     AVGSCORE:DESC：按VOD评分均值降序排序
     PLAYTIMES:ASC：按VOD点击率升序排序
     PLAYTIMES:DESC：按VOD点击率降序排序
     CNTARRANGE：按栏目下内容编排顺序排序，仅对subjectid表示的栏目是叶子栏目时才有效
     VODNAME:ASC：按VOD名称字典升序排序
     VODNAME:DESC：按VOD名称字典降序排序
     说明：
     当subjectID为叶子栏目且sortType未携带，sortType默认取CNTARRANGE:ASC。
     如果subjectID非叶子栏目且sortType未携带，sortType默认取STARTTIME:DESC。
     平台不支持对父栏目按照内容自定义的顺序排序，自定义顺序只针对叶子栏目。

     */
    @SerializedName("sortType")
    private String sortType;

    /**
     *一次查询的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误。

     */
    @SerializedName("count")
    private String count;

    /**
     *查询的起始位置。从0开始（即0表示第一个）。

     */
    @SerializedName("offset")
    private String offset;

    /**
     *搜索条件。

     VODFilter参考“VODFilter”

     说明：
     如果VODFilters和VODExcluders参数中包含相同的过滤条件，则以VODFilters中包含的为准，忽略VODExcluders中的同名过滤条件。

     */
    @SerializedName("VODFilter")
    private com.pukka.ydepg.common.http.v6bean.v6node.VODFilter VODFilter;

    /**
     *排他条件。

     VODExcluder参考“VODExcluder”

     */
    @SerializedName("VODExcluder")
    private com.pukka.ydepg.common.http.v6bean.v6node.VODExcluder VODExcluder;

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

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setVODFilter(com.pukka.ydepg.common.http.v6bean.v6node.VODFilter VODFilter) {
        this.VODFilter = VODFilter;
    }

    public void setVODExcluder(com.pukka.ydepg.common.http.v6bean.v6node.VODExcluder VODExcluder) {
        this.VODExcluder = VODExcluder;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public String getSortType() {
        return sortType;
    }

    public String getCount() {
        return count;
    }

    public String getOffset() {
        return offset;
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VODFilter getVODFilter() {
        return VODFilter;
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VODExcluder getVODExcluder() {
        return VODExcluder;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }


    /**
     * OrderType parameter set
     */
    public interface SortType
    {
        String CNTARRANGE = "CNTARRANGE";
        /**
         * VOD on-line time ascending order
         */
        String START_TIME_ASC = "STARTTIME:ASC";

        /**
         * VOD on-line time descending order
         */
        String START_TIME_DESC = "STARTTIME:DESC";

        /**
         * VOD mean score in ascending order
         */
        String AVERAGE_SCORE_ASC = "AVGSCORE:ASC";

        /**
         * VOD mean score in descending order
         */
        String AVERAGE_SCORE_DESC = "AVGSCORE:DESC";

        /**
         * VOD hits sort ascending order
         */
        String PLAY_TIMES_ASC = "PLAYTIMES:ASC";

        /**
         * VOD hits sort descending order
         */
        String PLAY_TIMES_DESC = "PLAYTIMES:DESC";

        /**
         * The Content Arrangement order under the column in ascending order
         */
        String CONTENT_ARRANGE_ASC = "CNTARRANGE:ASC";

        /**
         * The Content Arrangement order under the column in descending order
         */
        String CONTENT_ARRANGE_DESC = "CNTARRANGE:DESC";
        /**
         * The VOD name in ascending order
         */
        String VODNAME_ASC = "VODNAME:ASC";
        /**
         * The VOD name in descending order
         */
        String VODNAME_DESC = "VODNAME:DESC";


    }
}
