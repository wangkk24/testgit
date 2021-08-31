package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.moudule.featured.bean.VodBean;

import java.util.List;

/**
 * 桌面的Contact接口
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.LauncherContact.java
 * @date: 2017-12-15 11:50
 * @version: V1.0 定义了LauncherView的方法和Presenter方法
 */
public interface MyContact {
    interface IMyView extends IBaseContact.IBaseView {

        /**
         * 加载播放历史信息
         * @param bookMarkList
         * @param bookMarkVODList
         */
        void loadBookmarkData(List<VodBean> bookMarkList,List<VOD> bookMarkVODList);

        /**
         * 加载我的收藏信息
         * @param favoriteList
         * @param favoriteVODList
         */
        void loadFavoriteData(List<VodBean> favoriteList,List<VOD>favoriteVODList);

        /**
         * 加载订户信息
         * @param time
         */
        void loadSubscriptionData(String time);

        /**
         * 加载我的收藏和播放历史失败
         */
        void loadItemDataFail();


        /**
         * 获得业务账户成功
         */
        void querySubscriberSucess(String userId,String orderingSwitch,Subscriber subscriber);


        /**
         * 获得业务账户成功
         */
        void queryUniPayInfoSucc(QueryUniPayInfoResponse response);

        /**
         * 更新订户信息成功
         */
        void updateSubscriberSucess();

        /**
         * 更新订户信息失败
         */
        void updateSubscriberFail();

        /**
         * 验证短信成功
         */

        void updateUserRegInfoSucess();

        /**
         * 验证短信失败
         */
        void updateUserRegInfoFail();

        /**
         * VRS童锁状态查询
         */
        void queryUserOrderingSiwtchSuccess(String orderingSwitch);

        /**
         * VRS童锁状态变更功能
         */
        void changeUserOrderingSiwtchSuccess();

        /**
         * VRS短信验证成功
         */
        void checkVerifiedCodeSuccess();

        //自定义用户属性查询接口
        void queryUserAttrsSuccess(String multiCastSwitch);

    }

    interface IMyPresenter extends IBaseContact.IBasePresenter {
        /**
         * 查询书签，我的页面只查询前5个
         * @param request
         */
        void queryBookMark(QueryBookmarkRequest request, Context context);
        /**
         * 查询收藏，我的页面只查询前5个
         * @param request
         */
        void queryFavorite(QueryFavoriteRequest request, Context context);
        /**
         * 查询统一支付开通情况
         * @param request request
         */
         void queryUniPayInfo(QueryUniInfoRequest request, Context context);
    }
}
