package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryVODSubjectListResponse.java
 * @author: yh
 * @date: 2017-04-25 10:45
 */

public class QueryVODSubjectListResponse extends BaseResponse{


    /**
     * result : Result
     * total : 12
     * subjects : [""]
     * extensionFields : [""]
     */

    /**
     *子栏目总个数。

     */
    @SerializedName("total")
    private String total;

    /**
     *VOD栏目列表，VOD栏目属性请参见 “Subject”类型。

     如果查询结果为空，不返回该参数

     */
    @SerializedName("subjects")
    private List<Subject> subjects;

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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
