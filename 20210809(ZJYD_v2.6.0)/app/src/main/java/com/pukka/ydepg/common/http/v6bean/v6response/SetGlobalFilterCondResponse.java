package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by hasee on 2017/5/19.
 */

public class SetGlobalFilterCondResponse {
    @SerializedName("result")
    private Result result;
    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public void setResult(Result result) {
        this.result = result;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public Result getResult() {
        return result;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }
}
