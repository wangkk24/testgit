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
 * @FileName: QueryChannelSubjectListResponse.java
 * @author: yh
 * @date: 2017-04-24 17:23
 */

public class QueryChannelSubjectListResponse {


    /**
     * result :
     * total : 123
     * subjects : [""]
     * extensionFields : [""]
     */

    /**
     *返回结果。
     */
    @SerializedName("result")
    private Result result;

    /**
     *子栏目总个数。
     */
    @SerializedName("total")
    private String total;

    /**
     *频道栏目列表，频道栏目属性请参见“Subject”类型。

     如果查询结果为空，不返回该参数。
     */
    @SerializedName("subjects")
    private List<Subject> subjects;

    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
