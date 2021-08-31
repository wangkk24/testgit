package com.pukka.ydepg.moudule.mytv.presenter;

import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QrCodeCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBindedSubscriberCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryQrCodeStatusCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QuitQrCodeCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.UnBindSubsrciberCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.AuthenticateController;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.pukka.ydepg.moudule.mytv.presenter.view.AccountManagerDataView;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.AccountManagerPresenter.java
 * @author:xj
 * @date: 2018-01-24 15:59
 */

public class AccountManagerPresenter extends BasePresenter implements QrCodeCallBack, QueryQrCodeStatusCallBack, QuitQrCodeCallBack, QueryBindedSubscriberCallBack, UnBindSubsrciberCallBack {

    private final RxAppCompatActivity mRxAppCompatActivity;
    AuthenticateController mController;
    private AccountManagerDataView mDataView;

    public AccountManagerPresenter(RxAppCompatActivity rxAppCompatActivity) {
        mRxAppCompatActivity = rxAppCompatActivity;
        mController = new AuthenticateController(mRxAppCompatActivity);
    }
    public void setDataView(AccountManagerDataView dataView){
        mDataView  = dataView;
    }

    /**
     * 请求刷新二维码
     */
    public void qrCodeAuthenticate() {
        mController.qrCodeAuthenticate(this,compose(bindToLifecycle(mRxAppCompatActivity)));
    }

    /**
     * 检查二维码状态
     */
    public void qrCodeAuthenticateStatus() {
        mController.queryQrCodeStatus(this,compose(bindToLifecycle(mRxAppCompatActivity)));
    }

    /**
     * 查询绑定列表
     */
    public void queryBindedSubscriber() {
        mController.queryBindedSubscriber(this,compose(bindToLifecycle(mRxAppCompatActivity)));
    }

    /**
     * 解绑账号
     */
    public void handleUnbindSubscriber(String subscriberID) {
        mController.unBindSubscriber(subscriberID, this,compose(bindToLifecycle(mRxAppCompatActivity)));
    }

    /**
     * 清除账号状态
     */
    public void quitQrCodeAuthenticate() {
        mController.quitQrCodeAuthenticate(this,compose(bindToLifecycle(mRxAppCompatActivity)));
    }
    /**
     * 设置当前二维码认证用户
     *
     */
    public void setQrCodeAuthenticatedSubscriber(String subscriberId){

        mController.setQrCodeAuthenticatedSubscriber(subscriberId,compose(bindToLifecycle(mRxAppCompatActivity)));

    }

    @Override
    public void qrCodeCallBackSuccess(QrCodeAuthenticateResponse authenticateResponse) {
        if (null != mDataView){
            mDataView.qrCodeCallBackSuccess(authenticateResponse);
        }

    }

    @Override
    public void qrCodeCallBackFailed() {
        if (null != mDataView){
            mDataView.qrCodeCallBackFailed();
        }

    }

    @Override
    public void queryQrCodeStatusSuccess(QueryQrCodeStatusResponse qrCodeStatusResponse) {
        if (null != mDataView){
            mDataView.queryQrCodeStatusSuccess(qrCodeStatusResponse);
        }
    }

    @Override
    public void queryQrCodeStatusFailed(ErrorMessage message) {
        if (null != mDataView){
            mDataView.queryQrCodeStatusFailed(message);
        }
    }

    @Override
    public void quitQrCodeSuccess() {
        if (null != mDataView){
            mDataView.quitQrCodeSuccess();
        }
    }

    @Override
    public void quitQrCodeFailed() {
        if (null != mDataView){
            mDataView.quitQrCodeFailed();
        }
    }

    @Override
    public void qeryBindedSubscriberSuccess(QueryBindedSubscriberResponse queryBindedSubscriberResponse) {
        if (null != mDataView){
            mDataView.qeryBindedSubscriberSuccess(queryBindedSubscriberResponse);
        }
    }

    @Override
    public void qeryBindedSubscriberFailed(ErrorMessage message) {
        if (null != mDataView){
            mDataView.qeryBindedSubscriberFailed(message);
        }
    }

    @Override
    public void unBindSubsrciberSuccess() {
        if (null != mDataView){
            mDataView.unBindSubsrciberSuccess();
        }
    }

    @Override
    public void unBindSubsrciberFailed() {
        if (null != mDataView){
            mDataView.unBindSubsrciberFailed();
        }
    }
}
