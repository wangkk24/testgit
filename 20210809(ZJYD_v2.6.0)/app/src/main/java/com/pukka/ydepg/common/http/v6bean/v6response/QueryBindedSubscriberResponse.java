package com.pukka.ydepg.common.http.v6bean.v6response;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse.java
 * @author:xj
 * @date: 2018-01-09 17:40
 */

public class QueryBindedSubscriberResponse {

    @SerializedName("result")
    private Result result;
    @SerializedName("bindedSubscribers")
    private List<String> bindedSubscribers;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getBindedSubscribers() {
        return bindedSubscribers;
    }

    public void setBindedSubscribers(List<String> bindedSubscribers) {
        this.bindedSubscribers = bindedSubscribers;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
    public boolean isSuccess(){
        if (null != result){
            return TextUtils.equals(result.getRetCode(),Result.RETCODE_OK);
        }
        return false;
    }
}
