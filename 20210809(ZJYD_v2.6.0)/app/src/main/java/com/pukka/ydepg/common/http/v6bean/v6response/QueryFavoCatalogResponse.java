package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6Entity.FavroitesData;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;

import java.util.List;

public class QueryFavoCatalogResponse extends BaseResponse {

    @SerializedName("total")
    private  int total;

    @SerializedName("catalogs")
    private List<FavoCatalog>  catalogs;

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public List<FavoCatalog> getCatalogs()
    {
        return catalogs;
    }

    public void setCatalogs(List<FavoCatalog> catalogs)
    {
        this.catalogs = catalogs;
    }
}
