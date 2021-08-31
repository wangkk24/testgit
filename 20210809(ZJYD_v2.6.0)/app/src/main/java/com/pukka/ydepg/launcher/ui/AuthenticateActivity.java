package com.pukka.ydepg.launcher.ui;

import android.os.Bundle;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.mvp.contact.AuthenticateContact;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.launcher.mvp.presenter.AuthenticatePresenter;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * 认证基类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.AuthenticateActivity.java
 * @date: 2018-01-09 17:35
 * @version: V1.0 认证和超时重新认证都在这里完成
 */
public abstract class AuthenticateActivity<T extends IBaseContact.IBasePresenter> extends RxAppCompatActivity implements AuthenticateContact.IAuthenticateView {
    private final String TAG = AuthenticateActivity.class.getSimpleName();
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onStart() {super.onStart();}

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 默认实现为AuthenticatePresenter，使用mvp的子类自行实例化
     * 如果子类要使用mvp时的Presenter是AuthenticatePresenter子类，需要覆写此方法，否则会抛转型异常
     */
    protected void initPresenter() {}

    /**
     * 管理rxjava生命周期，根据ui生命周期调用oncomplete
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    /**
     * 重新认证
     */
    public void reAuthenticate() {
        SuperLog.info2SD(TAG,"Begin to reAuthenticate()");
        AuthenticatePresenter authenticatePresenter = new AuthenticatePresenter();
        authenticatePresenter.attachView(this);
        authenticatePresenter.reLogin(this);
    }

    /**
     * 切换账号后重新认证
     */
    public void reAuthenticateAfterSwitch() {
        SuperLog.info2SD(TAG,"Begin to reAuthenticateAfterSwitch");
        AuthenticatePresenter authenticatePresenter = new AuthenticatePresenter();
        authenticatePresenter.attachView(this);
        authenticatePresenter.reLoginAfterSwitchProfile(this);
    }
}