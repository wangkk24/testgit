package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QuerySubjectVODBySubjectIDResponse.java
 * @author: yh
 * @date: 2017-04-25 10:49
 */

public class QuerySubjectVODBySubjectIDResponse extends BaseResponse{


    /**
     * result : Result
     * total : 12
     * subjectVODLists : [""]
     * extensionFields : [""]
     */

    /**
     *子栏目总个数。

     */
    @SerializedName("total")
    private String total;

    /**
     *子栏目及栏目下VOD列表信息，如果查询结果为空，不返回该参数。

     */
    @SerializedName("subjectVODLists")
    private List<SubjectVODList> subjectVODLists;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<SubjectVODList> getSubjectVODLists() {
        return subjectVODLists;
    }

    public void setSubjectVODLists(List<SubjectVODList> subjectVODLists) {
        this.subjectVODLists = subjectVODLists;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
