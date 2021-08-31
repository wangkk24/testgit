package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.report.error.ErrorInfoReport;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter的基类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.BasePresenter.java
 * @date: 2017-12-15 11:39
 * @version: V1.0 转换view类型、管理view生命周期
 */
public class BasePresenter<T extends IBaseContact.IBaseView> implements IBaseContact.IBasePresenter {

    protected T mView;

    /**
     * 注入view
     *
     * @param view
     */
    @Override
    public void attachView(IBaseContact.IBaseView view) {
        this.mView = (T) view;
    }

    /**
     * 注销view
     */
    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * 如果persenter和view保持生命周期一致，调用此方法在view结束时候释放资源
     * 此方法也处理了主线程子线程的切换
     *
     * @param lifecycle
     * @param <R>
     * @return
     */
    public  <R> ObservableTransformer<R, R> onCompose(final LifecycleTransformer<R> lifecycle) {
        return new ObservableTransformer<R, R>() {
            @Override
            public ObservableSource<R> apply(@NonNull Observable<R> upstream) {
                Observable<R> observable = upstream
                    .doOnSubscribe(disposable->{}) //原java语法格式内容可以通过svn日志查看
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
                if (lifecycle == null) {
                    return observable;
                } else {
                    return observable.compose(lifecycle);
                }
            }
        };
    }

    /**
     * 处理有返回结的错误，不包含超时
     *
     * @param errorCode     返回错误码
     * @param interfaceName 接口名，从error.json里面查找，匹配对用户友好的错误提示，如果不提示传null。
     */
    public void handleError(String errorCode, String interfaceName, Context context) {
        String message = ErrorCode.findError(interfaceName, errorCode).getMessage();
        StringBuffer logMessage = new StringBuffer()
                .append("Interface[")
                .append(interfaceName)
                .append("]\tErrorCode[")
                .append(errorCode)
                .append("]\tErrorMessage[")
                .append(message)
                .append("]");
        SuperLog.error(BasePresenter.class.getSimpleName(),logMessage.toString());
        OTTErrorWindowUtils.getErrorInfoFromPbs(interfaceName,errorCode,context);
    }

    /**
     * 处理有返回结的错误，包含超时
     *
     * @param errorCode     返回错误码
     * @param interfaceName 接口名，从error.json里面查找，匹配对用户友好的错误提示，如果不提示传null。
     */
    public void handleErrorIncludeTimeOut(String errorCode, String interfaceName, Context context) {
        //上报错误码到探针中间件
        ErrorInfoReport.getInstance().reportErrorCode(interfaceName,errorCode);
        if (CommonUtil.isSessionError(errorCode)) {  //超时后处理
            SuperLog.error(BasePresenter.class.getSimpleName(), interfaceName + " session timeout!");
            reportTokenTimeOut();
        } else {
            handleError(errorCode, interfaceName, context);
        }
    }

    /**
     * 报告token失效
     */
    private void reportTokenTimeOut() {
        SuperLog.error("BasePresenter","SessionTimeOut,begin to reportTokenStatus(UpdateToken)");
        new Thread(() -> AuthenticateManager.getInstance().reportTokenStatus()).start();
    }
}