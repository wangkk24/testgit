package com.pukka.ydepg.moudule.mytv.presenter;

import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryFavoCatalogCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PersonalizedController;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6node.Favorite;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoCatalogResponse;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.moudule.mytv.presenter.view.CollectionView;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.mytv.presenter.CollectionPresenter.java
 * @author:xj
 * @date: 2018-01-26 14:15
 */

public class CollectionPresenter extends BasePresenter implements QueryFavoriteCallBack, DeleteFavoriteCallBack, QueryFavoCatalogCallback {
    private RxAppCompatActivity rxAppCompatActivity;

    private PersonalizedController mController;
    private CollectionView mCollectionView;

    public CollectionPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mController =  new PersonalizedController(rxAppCompatActivity);
    }
    public void setDataView(CollectionView collectionView){
        mCollectionView = collectionView;
    }
    public void queryFavorite(int offset,int count,String catalogID){
        QueryFavoriteRequest request = new QueryFavoriteRequest();
        request.setCount(count+"");
        request.setOffset(offset+"");
        request.setSortType("FAVO_TIME:DESC");
        if (!TextUtils.isEmpty(catalogID)){
            request.setCatalogID(catalogID);//从少儿动漫界面进收藏，传少儿动漫收藏文件夹ID
        }
        List<String> contentTypes = new ArrayList<>();
        contentTypes.add("VOD");
        request.setContentTypes(contentTypes);
        mController.queryFavorite(request,this,compose(rxAppCompatActivity.bindToLifecycle()));
    }

    public void deleteFavorites(List<Content> contents, boolean isDeleteAll){
        DeleteFavoriteRequest request = new DeleteFavoriteRequest();
        List<String> typeList = new ArrayList<>();
        if (isDeleteAll && !SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            typeList.add(Favorite.ContentType.VOD);
        } else {
            List<String> idList = new ArrayList<>();
            for (int i = 0; i < contents.size(); i++) {
                Content content = contents.get(i);
                if (null != content) {
                    VOD vod = content.getVOD();
                    if (null != vod) {
                        idList.add(vod.getID() + "");
                    }
                    typeList.add(Favorite.ContentType.VOD);
                }
            }
            request.setContentIDs(idList);
        }
        request.setContentTypes(typeList);
        mController.deleteFavorite(request,this,compose(rxAppCompatActivity.bindToLifecycle()));
    }

    @Override
    public void queryFavoriteSuccess(int total, List<Content> favorites) {
        if (null != mCollectionView){
            mCollectionView.queryFavoriteSuccess(total,favorites);
        }
    }

    @Override
    public void queryFavoriteFail() {
        if (null != mCollectionView){
            mCollectionView.queryFavoriteFail();
        }
    }

    @Override
    public void deleteFavoriteSuccess() {
        if (null != mCollectionView){
            mCollectionView.deleteFavoriteSuccess();
        }
    }

    @Override
    public void deleteFavoriteFail() {
        if (null != mCollectionView){
            mCollectionView.deleteFavoriteFail();
        }
    }

    //查询收藏夹
    public void queryFavoCatalog(int offset,int count){
        QueryFavoCatalogRequest request = new QueryFavoCatalogRequest();
        request.setCount(count+"");
        request.setOffset(offset+"");
        mController.queryFavoCatalog(request,this,compose(rxAppCompatActivity.bindToLifecycle()));
    }

    @Override
    public void queryFavoCatalogSuccess(int total ,List<FavoCatalog> favoCatalogList) {
        if (null != mCollectionView){
            mCollectionView.queryFavoCatalogSuccess(total,favoCatalogList);
        }
    }

    @Override
    public void queryFavoCatalogFail() {
        if (null != mCollectionView){
            mCollectionView.queryFavoCatalogFail();
        }
    }
}
