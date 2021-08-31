package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryVODSubjectListRequest.java
 * @author: yh
 * @date: 2017-04-25 10:45
 */

public class QueryVODSubjectListRequest {


    /**
     * subjectID :
     * contentTypes : [""]
     * count : 1
     * offset : 1
     * extensionFields : [""]
     */

    /**
     *父栏目ID，查询此栏目下的子栏目列表。如果为空，标示查询 “-1”根栏目下的栏目列表。
     */
    @SerializedName("subjectID")
    private String subjectID;

    /**
     *一次查询的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误。

     */
    @SerializedName("count")
    private String count;

    /**
     *一次查询的总条数，不能设置为-1，调用者一定要指定获取数据的总条数，最大条数默认不超过50，最大条数可配置，超过最大条数返回错误。

     */
    @SerializedName("offset")
    private String offset;

    /**
     *栏目类型，具体取值包括：

     VOD：点播
     AUDIO_VOD：音频点播
     VIDEO_VOD：视频点播
     默认值是VOD。

     说明：
     VOD类型的栏目下可以包含音频点播和视频点播，而AUDIO_VOD类型的栏目下只能包含音频点播、VIDEO_VOD类型的栏目下只能包含视频点播。

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
