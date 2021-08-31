package com.pukka.ydepg.moudule.mytv.presenter.view;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoCatalogResponse;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.view.CollectionView.java
 * @author:xj
 * @date: 2018-01-26 14:25
 */

public interface CollectionView {
    void queryFavoriteSuccess(int total, List<Content> favorites);

    void queryFavoriteFail();

    void deleteFavoriteSuccess();

    void deleteFavoriteFail();

    void queryFavoCatalogSuccess(int total ,List<FavoCatalog> favoCatalogList);

    void queryFavoCatalogFail();
}
