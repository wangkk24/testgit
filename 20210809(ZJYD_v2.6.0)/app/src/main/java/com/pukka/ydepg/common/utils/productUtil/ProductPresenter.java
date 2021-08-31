package com.pukka.ydepg.common.utils.productUtil;

import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6request.GetProductMutExRelaRequest;
import com.pukka.ydepg.control.selfregister.AppInfoCallBack.QueryProductInfoCallBack;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.ProductPresenter.java
 * @author:xj
 * @date: 2018-01-26 10:06
 */
public class ProductPresenter extends BasePresenter{

    private final RxAppCompatActivity   rxAppCompatActivity;
    private final SelfAppInfoController mSelfAppInfoController;

    public ProductPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mSelfAppInfoController = new SelfAppInfoController(rxAppCompatActivity);
    }

    public void queryProductInfo(DSVQuerySubscription request, QueryProductInfoCallBack queryProductInfoCallBack) {
        mSelfAppInfoController.queryProductInfo(request, compose(rxAppCompatActivity.bindToLifecycle()), queryProductInfoCallBack);
    }

    public void getProductMutExRela(GetProductMutExRelaRequest request, SelfAppInfoController.GetProductMutExRelaCallBack callBack){
        mSelfAppInfoController.getProductMutExRela(request, compose(rxAppCompatActivity.bindToLifecycle()),callBack);
    }
}