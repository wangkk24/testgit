package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6node.QueryPlaybillContext.java
 * @author: yh
 * @date: 2017-06-23 00:38
 */

public class QueryPlaybillContext {


    /**
     * data :
     * type :
     * preNumber :
     * nextNumber :
     * isFillProgram :
     * extensionFields : ["name"]
     */

    @SerializedName("date")
    private String date;
    @SerializedName("type")
    private String type;
    @SerializedName("preNumber")
    private String preNumber;
    @SerializedName("nextNumber")
    private String nextNumber;
    @SerializedName("isFillProgram")
    private String isFillProgram;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(String nextNumber) {
        this.nextNumber = nextNumber;
    }

    public String getIsFillProgram() {
        return isFillProgram;
    }

    public void setIsFillProgram(String isFillProgram) {
        this.isFillProgram = isFillProgram;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
