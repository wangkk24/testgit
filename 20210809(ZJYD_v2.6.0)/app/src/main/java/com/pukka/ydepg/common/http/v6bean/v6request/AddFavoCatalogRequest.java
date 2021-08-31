package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

public class AddFavoCatalogRequest {

    @SerializedName("catalog")
    private FavoCatalog catalog;


    /**

     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;



    public FavoCatalog getCatalog()
    {
        return catalog;
    }

    public void setCatalog(FavoCatalog catalog)
    {
        this.catalog = catalog;
    }

    public List<NamedParameter> getExtensionFields()
    {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields)
    {
        this.extensionFields = extensionFields;
    }




}
