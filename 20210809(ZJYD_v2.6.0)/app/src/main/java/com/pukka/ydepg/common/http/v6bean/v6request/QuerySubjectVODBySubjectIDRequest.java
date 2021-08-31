package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QuerySubjectVODBySubjectIDRequest.java
 * @author: yh
 * @date: 2017-04-25 10:49
 */

public class QuerySubjectVODBySubjectIDRequest {
    /**
     * subjectID :
     * subjectSortType :
     * subjectCount : 1
     * VODSortType :
     * VODCount : 12
     * offset : 12
     * extensionFields : [""]
     */

    /**
     *栏目ID(只支持父栏目，不能是叶子栏目)，
     * 查询此栏目下的子栏目及子栏目下VOD列表。
     * 如不传该参数，表示查询根栏目下所有子栏目及子栏目下的VOD列表。

     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *子栏目的排序方式。以下列方式指定排序方式：

     ID:ASC：按子栏目ID升序排序
     ID:DESC：按子栏目ID降序排序
     NAME:ASC：按子栏目名称升序排序
     NAME:DESC：按子栏目名称降序排序
     如果不指定，平台先按照栏目自定义序号降序排序，再按照栏目ID降序排序。

     */
    @SerializedName("subjectSortType")
    private String subjectSortType;

    /**
     *一次查询的子栏目的总条数，不能设置为-1，
     * 调用者一定要指定获取数据的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误。

     */
    @SerializedName("subjectCount")
    private String subjectCount;

    /**
     *子栏目下VOD的排序方式。以下列方式指定排序方式：

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
     */
    @SerializedName("VODSortType")
    private String VODSortType;

    /**
     *一次查询的子栏目中VOD的总条数，
     * 不能设置为-1，终端一定要指定获取数据的总条数，
     * 最大条数默认不超过50，最大条数可配置，
     * 超过最大条数返回错误
     */
    @SerializedName("VODCount")
    private String VODCount;

    /**
     *查询的子栏目的起始位置。从0开始（即0表示第一个）。

     */
    @SerializedName("offset")
    private String offset;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public QuerySubjectVODBySubjectIDRequest() {
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectSortType() {
        return subjectSortType;
    }

    public void setSubjectSortType(String subjectSortType) {
        this.subjectSortType = subjectSortType;
    }

    public String getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(String subjectCount) {
        this.subjectCount = subjectCount;
    }

    public String getVODSortType() {
        return VODSortType;
    }

    public void setVODSortType(String VODSortType) {
        this.VODSortType = VODSortType;
    }

    public String getVODCount() {
        return VODCount;
    }

    public void setVODCount(String VODCount) {
        this.VODCount = VODCount;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
