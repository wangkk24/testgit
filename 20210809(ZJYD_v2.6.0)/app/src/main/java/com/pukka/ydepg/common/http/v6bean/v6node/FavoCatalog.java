package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liudo on 2019/4/18.
 */

public class FavoCatalog
{
    @SerializedName("catalogID")
    private String catalogID;

    @SerializedName("catalogName")
    private String catalogName;

    public String getCatalogID()
    {
        return catalogID;
    }

    public void setCatalogID(String catalogID)
    {
        this.catalogID = catalogID;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    public void setCatalogName(String catalogName)
    {
        this.catalogName = catalogName;
    }
}
