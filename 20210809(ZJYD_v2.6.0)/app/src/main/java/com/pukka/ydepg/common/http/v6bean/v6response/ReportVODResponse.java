package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ReportVODResponse.java
 * @author: yh
 * @date: 2017-04-25 10:58
 */

public class ReportVODResponse extends BaseResponse{

    ///**
    // * result :
    // * extensionFields : [""]
    // */
    //
    ///**
    // * 返回结果。
    //
    // */
    //@SerializedName("result")
    //private String result;

    /**
     * 扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;
    //
    //public String getResult() {
    //    return result;
    //}
    //
    //public void setResult(String result) {
    //    this.result = result;
    //}

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
