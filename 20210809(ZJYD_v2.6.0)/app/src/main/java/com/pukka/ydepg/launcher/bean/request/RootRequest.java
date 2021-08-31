package com.pukka.ydepg.launcher.bean.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created on 2017/4/7.
 */
public class RootRequest
{
    public RootRequest()
    {

    }
    /**
     * this parameter is add by app.is not define in VSP
     */
    @SerializedName("requestURL")
    protected String requestURL = "";





    /**
     * this parameter is used for UI bind data to request, it can be get from the control's
     * callback again.
     * but the parameter did not Serialize to the request packet
     * is not define in VSP
     */
    @SerializedName("extendsParam")
    private HashMap<String, Object> extendsParam = new HashMap<>();

    public HashMap<String, Object> getExtendsParamMap()
    {
        return extendsParam;
    }


    public Object getExtendsParam(String key)
    {
        return extendsParam.get(key);
    }

    public RootRequest putExtendsParam(String key, Object value)
    {
        extendsParam.put(key, value);
        return this;
    }


    public String getRequestURL()
    {
        return requestURL;
    }

    public void setRequestURL(String requestURL)
    {
        this.requestURL = requestURL;
    }
}
