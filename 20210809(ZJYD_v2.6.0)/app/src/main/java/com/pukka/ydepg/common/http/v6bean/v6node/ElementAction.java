package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 */

public class ElementAction implements Serializable
{
    /**
     * actionType :
     * actionURL :
     * appParam :
     */
    @SerializedName("actionType")
    private String actionType;
    @SerializedName("actionURL")
    private String actionURL;
    @SerializedName("appParam")
    private String appParam;

//    @SerializedName("extraData")
//    private HashMap<String, String> extraDataMap;

    public String getActionType() { return actionType;}

    public void setActionType(String actionType) { this.actionType = actionType;}

    public String getActionURL() { return actionURL;}

    public void setActionURL(String actionURL) { this.actionURL = actionURL;}

    public String getAppParam() { return appParam;}

    public void setAppParam(String appParam) { this.appParam = appParam;}

//    public HashMap<String, String> getExtraDataMap()
//    {
//        return extraDataMap;
//    }

//    public void setExtraDataMap(HashMap<String, String> extraDataMap)
//    {
//        this.extraDataMap = extraDataMap;
//    }
}