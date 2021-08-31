package com.pukka.ydepg.common.http.entity;


import com.pukka.ydepg.common.http.HttpService;
import com.pukka.ydepg.common.http.listener.HttpOnNextListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import okhttp3.RequestBody;


/**
 * 请求数据统一封装类
 */
public abstract class BaseEntity<T> {

    //rx生命周期管理
    private RxAppCompatActivity rxAppCompatActivity;

    /*回调*/
    private HttpOnNextListener listener;

    /*是否能取消加载框*/
    private boolean cancel = true;

    /*是否显示错误提示*/
    private boolean showToast = true;

    /*是否显示加载框*/
    private boolean showProgress = true;

    /*接口名称*/
    private String interfaceName = "";

    private volatile int errorCounter=0;

    public BaseEntity(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        errorCounter=0;
    }

    /**
     * 设置请求的方法
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isShowToast() {
        return showToast;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public HttpOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpOnNextListener listener) {
        this.listener = listener;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        errorCounter=0;
        this.interfaceName = interfaceName;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public void setErrorCounter(int errorCounter) {
        this.errorCounter = errorCounter;
    }

    /*
             * 获取当前rx生命周期
             * @return
             */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

    protected RequestBody createRequestBody(String json) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
    }
}
