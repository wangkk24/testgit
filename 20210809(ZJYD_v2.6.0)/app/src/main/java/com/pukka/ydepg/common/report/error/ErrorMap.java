package com.pukka.ydepg.common.report.error;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ErrorMap
{
    public ArrayList<ErrorInfo> getListErrorCodeMap() {
        return listErrorCodeMap;
    }

    public void setListErrorCodeMap(ArrayList<ErrorInfo> listErrorCodeMap) {
        this.listErrorCodeMap = listErrorCodeMap;
    }

    @SerializedName("error_code_map")
    private ArrayList<ErrorInfo> listErrorCodeMap;
}
