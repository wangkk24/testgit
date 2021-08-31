package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class SuggestKeywordResponse extends BaseResponse{

    /**
     * result :
     * suggests :
     * extensionFields :
     */

    /**
     *
     */
    @SerializedName("suggests")
    private List<String> suggests;

    /**
     *
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<String> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<String> suggests) {
        this.suggests = suggests;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
