package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class QuerySuperScriptResponse extends BaseResponse {

    @SerializedName("superScripts")
    private List<SuperScript> superScript;

    public List<SuperScript> getSuperScripts() {
        return superScript;
    }

    public void setSuperScripts(List<SuperScript> superScripts) {
        this.superScript = superScripts;
    }
}



