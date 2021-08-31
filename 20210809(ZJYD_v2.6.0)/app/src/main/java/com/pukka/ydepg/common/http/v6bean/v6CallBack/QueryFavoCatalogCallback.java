package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;

import java.util.List;

public interface QueryFavoCatalogCallback extends V6CallBack{
    void queryFavoCatalogSuccess(int total ,List<FavoCatalog> favoCatalogList);
    void queryFavoCatalogFail();
}
