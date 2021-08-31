package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the Metadata of ExtensionField
 *
 */
public class ExtensionField
{
    /**
     * field:extensionFieldList
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFieldList;

    public List<NamedParameter> getExtensionFieldList()
    {
        return extensionFieldList;
    }

    public void setExtensionFieldList(List<NamedParameter> extensionFieldList)
    {
        this.extensionFieldList = extensionFieldList;
    }

    @Override
    public String toString()
    {
        return "ExtensionField{" +
                "extensionFieldList=" + extensionFieldList +
                '}';
    }
}