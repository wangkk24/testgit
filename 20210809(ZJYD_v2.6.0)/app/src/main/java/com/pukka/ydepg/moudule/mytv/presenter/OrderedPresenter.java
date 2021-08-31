package com.pukka.ydepg.moudule.mytv.presenter;

import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.control.selfregister.AppInfoCallBack.QueryProductInfoCallBack;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderedView;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.OrderedPresenter.java
 * @author:xj
 * @date: 2018-01-26 10:06
 */
public class OrderedPresenter extends BasePresenter implements QueryProductInfoCallBack {

    private RxAppCompatActivity   rxAppCompatActivity;
    private SelfAppInfoController mSelfAppInfoController;
    private OrderedView           mDataView;

    public OrderedPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mSelfAppInfoController = new SelfAppInfoController(rxAppCompatActivity);
    }

    public void setDataView(OrderedView dataView) {
        mDataView = dataView;
    }

    public void queryProductInfo(DSVQuerySubscription request, int count) {
        request.setCount(count + "");
        mSelfAppInfoController.queryProductInfo(request, compose(rxAppCompatActivity.bindToLifecycle()), this);

    }

    public void cancelProductInfo() {
        mSelfAppInfoController.cancelProductInfo();
    }

    public void unsuscribe(CancelSubscribeRequest cancelSubscribeRequest) {
        mSelfAppInfoController.unsubscribe(cancelSubscribeRequest, compose(rxAppCompatActivity.bindToLifecycle()), this);
    }

    @Override
    public void queryProductInfoSuccess(QueryProductInfoResponse queryProductInfoResponse) {
        mSelfAppInfoController.queryProduct(compose(rxAppCompatActivity.bindToLifecycle()),this,queryProductInfoResponse);
    }

    @Override
    public void queryProductInfoFail() {
        if (null != mDataView) {
            mDataView.queryProductInfoFail();
        }
    }

    @Override
    public void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse) {
        if (null != mDataView) {
            mDataView.unsubscribeSuccess(subscribeDeleteResponse);
        }
    }

    @Override
    public void unsubscribeFail() {
        if (null != mDataView) {
            mDataView.unsubscribeFail();
        }
    }

    @Override
    public void queryProductByPriceSuccess(QueryProductInfoResponse queryProductInfoResponse) {
        if (null != mDataView) {
            mDataView.queryProductInfoSuccess(queryProductInfoResponse);
        }
    }
}
