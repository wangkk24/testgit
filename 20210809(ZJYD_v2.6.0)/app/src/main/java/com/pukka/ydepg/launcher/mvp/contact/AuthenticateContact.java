package com.pukka.ydepg.launcher.mvp.contact;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;

import java.util.List;

/**
 * 认证和登录
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.contact.AuthenticateContact.java
 * @date: 2018-01-09 13:53
 * @version: V1.0 描述当前版本功能
 */
public interface AuthenticateContact {
    interface IAuthenticateView extends IBaseContact.IBaseView {
        void startService();                                    //第一次登录后启动service
        void loadAdvertContentSuccess(List<AdvertContent> listAdvertContent);
        void loadAdvertFail();

    }
    interface IAuthenticatePresenter <T extends IBaseContact.IBaseView>extends IBaseContact.IBasePresenter<T>{
        void reLogin(Context context);      //重新登录
        void firstLogin(Context context);   //首次登录
    }
}
