package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class ReportQrCodeStateResponse {

    /**
     * result :
     * extensionFields : [""]
     */

    /*
    *   返回结果
    */
    @SerializedName("result")
    private Result result;

    /*
    * 扩展信息
    */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ReportQrCodeStateResponse{" +
                "result=" + result +
                ", extensionFields=" + extensionFields +
                '}';
    }
}
