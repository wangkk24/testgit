package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
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


public interface SwitchDialogContact {
    interface SwitchView extends IBaseContact.IBaseView {
        /**
         * VRS短信验证成功
         */
        void checkVerifiedCodeSuccess();

        void checkVerifiedCodeFail();

        //查询统一支付开通情况
        void queryUniPayInfoSuccess(QueryUniPayInfoResponse response);

        void queryUniPayInfoFail();

        //变更
        void modifyUserAttrSuccess();

        void modifyUserAttrFail();

        //自定义用户属性查询接口
        void queryUserAttrsSuccess(String pwdValue);

        void queryUserAttrsFail();

        void querySubscriberInfoSucc(QuerySubscriberResponse response);
        void querySubscriberInfoError();

    }
}
