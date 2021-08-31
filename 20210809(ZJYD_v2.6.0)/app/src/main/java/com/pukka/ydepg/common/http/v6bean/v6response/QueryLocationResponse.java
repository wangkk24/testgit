package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QueryLocationResponse.java
 * @author: yh
 * @date: 2017-09-06 09:10
 */

public class QueryLocationResponse {

    /**
     * result :
     * location :
     * ip :
     * extensionFields : []
     */

    @SerializedName("result")
    private Result result;
    @SerializedName("location")
    private String location;
    @SerializedName("ip")
    private String ip;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public QueryLocationResponse() {
    }

    @Override
    public String toString() {
        return "QueryLocationResponse{" +
                "result=" + result +
                ", location='" + location + '\'' +
                ", ip='" + ip + '\'' +
                ", extensionFields=" + extensionFields +
                '}';
    }
}
