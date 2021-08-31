package com.pukka.ydepg.launcher.bean.request;

import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BaseRequest extends RootRequest
{
    public BaseRequest()
    {

    }

    /**
     * field:extensionFields
     * the extends info of request
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFieldList;

    public List<NamedParameter> getExtensionFieldList()
    {
        return extensionFieldList;
    }

    public BaseRequest setExtensionFieldList(List<NamedParameter> extensionFieldList)
    {
        this.extensionFieldList = extensionFieldList;
        return this;
    }

    @Override
    public String toString()
    {
        return "BaseRequest{" +
                "extensionFieldList=" + extensionFieldList +
                '}';
    }
}