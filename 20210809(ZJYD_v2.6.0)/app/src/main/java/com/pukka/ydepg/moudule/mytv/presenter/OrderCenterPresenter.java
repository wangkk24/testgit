package com.pukka.ydepg.moudule.mytv.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryProductResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.control.selfregister.ErrorCodeConstant;
import com.pukka.ydepg.launcher.mvp.presenter.BasePresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import okhttp3.OkHttpClient;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter.java
 * @author: luwm
 * @data: 2018-08-15 15:22
 * @Version V1.0 <描述当前版本功能>
 */
public class OrderCenterPresenter extends BasePresenter<OrderCenterView> {
    private static final String TAG = "OrderCenterPresenter";

    private OkHttpClient client;

    public OrderCenterPresenter() {
        client = new OkHttpClient();
    }

    @SuppressLint("CheckResult")
    public void querySubscribe() {
        QueryUniInfoRequest request = new QueryUniInfoRequest();
        String interfaceName = HttpConstant.QUERY_SUBSCRIBE_INFO;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().querySubscriberInfo(url, request).compose(mView.bindToLife()).subscribe(querySubscriberResponse -> {
            String retCode = querySubscriberResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                List<NamedParameter> customFields = querySubscriberResponse.getSubscriber().getCustomFields();
                String userId = null;
                if (!CollectionUtil.isEmpty(customFields)) {
                    for (NamedParameter namedParameter : customFields) {
                        if ("BILL_ID".equals(namedParameter.getKey()) && !CollectionUtil.isEmpty(namedParameter.getValues())) {
                            userId = namedParameter.getValues().get(0);
                            SessionService.getInstance().getSession().setAccountName(userId);
                        }
                    }

                }
                mView.querySubscriberSucess(userId);
            }
        }, throwable -> {
        });
    }

    public void queryChannelProduct(List<String> ids, int count, int offset, Context context) {
        QueryProductRequest request = new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        request.setProductIds(ids);
        request.setCount(count);
        request.setOffset(offset);
        HttpApi.getInstance().getService().queryProduct(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_PRODUCT, request)
                .compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<QueryProductResponse>(context) {
            @Override
            public void onSuccess(QueryProductResponse queryProductResponse) {
                if (null != queryProductResponse && null != queryProductResponse.getResult() && TextUtils.equals(Result.RETCODE_OK, queryProductResponse.getResult().getRetCode())) {
                    List<Product> products = queryProductResponse.getProductList();
                    if (!CollectionUtil.isEmpty(products)) {
                        mView.loadProducts(products);
                    }
                    else{
                        mView.queryProductInfoFail();
                    }
                }
                else{
                    mView.queryProductInfoFail();
                }
            }

            @Override
            public void onFail(Throwable e) {
                mView.queryProductInfoFail();
            }
        });
    }

    public void queryProduct(List<String> ids, int count, int offset, Context context) {
        QueryProductRequest request = new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        request.setProductIds(ids);
        request.setCount(count);
        request.setOffset(offset);
        HttpApi.getInstance().getService().queryProduct(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_PRODUCT, request)
                .compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<QueryProductResponse>(context) {
            @Override
            public void onSuccess(QueryProductResponse queryProductResponse) {
                if (null != queryProductResponse && null != queryProductResponse.getResult() && TextUtils.equals(Result.RETCODE_OK, queryProductResponse.getResult().getRetCode())) {
                    List<Product> products = queryProductResponse.getProductList();
                    if (!CollectionUtil.isEmpty(products)) {
                        mView.loadProducts(products);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }

    public void queryProductInfo(DSVQuerySubscription request, Context context) {
        HttpApi.getInstance().getService().queryProductInfo(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_SUBSCRIPTION, request)
                .compose(onCompose(mView.bindToLife())).subscribe(queryProductInfoResponse -> {

            if (null != queryProductInfoResponse.getResult() &&
                    (TextUtils.equals(queryProductInfoResponse.getResult().getRetCode(), ErrorCodeConstant.HTTP_OK) ||
                            TextUtils.equals(queryProductInfoResponse.getResult().getRetCode(), Result.RETCODE_OK))) {
                if (CollectionUtil.isEmpty(queryProductInfoResponse.getProducts())) {
                    mView.loadSubscription(queryProductInfoResponse);
                    return;
                }
                queryProduct(onCompose(mView.bindToLife()), queryProductInfoResponse);
            } else {
                handleError(queryProductInfoResponse.getResult().getRetCode(), HttpConstant.QUERY_SUBSCRIPTION, context);
                mView.queryProductInfoFail();
            }
        }, throwable -> {
            mView.queryProductInfoFail();
        });
    }

    /**
     * 查询已订购产品价格
     */
    public void queryProduct(ObservableTransformer<QueryProductResponse, QueryProductResponse> transformer, QueryProductInfoResponse queryProductInfoResponse) {
        QueryProductRequest request = new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        if (null != queryProductInfoResponse && null != queryProductInfoResponse.getProducts() && queryProductInfoResponse.getProducts().size() > 0) {
            List<Subscription> subscriptions = queryProductInfoResponse.getProducts();
            List<String> productIds = new ArrayList<>();
            for (int i = 0; i < subscriptions.size(); i++) {
                if (!TextUtils.isEmpty(subscriptions.get(i).getProductID())) {
                    productIds.add(subscriptions.get(i).getProductID());
                }
            }
            request.setProductIds(productIds);
        }
        HttpApi.getInstance().getService().queryProduct(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_PRODUCT, request)
                .compose(transformer).subscribe(queryProductResponse -> {

            if (null != queryProductResponse.getResult() &&
                    (TextUtils.equals(queryProductResponse.getResult().getRetCode(), ErrorCodeConstant.HTTP_OK) ||
                            TextUtils.equals(queryProductResponse.getResult().getRetCode(), Result.RETCODE_OK))) {

                List<Product> products = queryProductResponse.getProductList();
                if (null != queryProductInfoResponse && null != queryProductInfoResponse.getProducts() && queryProductInfoResponse.getProducts().size() > 0) {
                    List<Subscription> subscriptions = queryProductInfoResponse.getProducts();
                    if (null != products && !products.isEmpty()) {
                        for (int i = 0; i < products.size(); i++) {
                            for (int j = 0; j < subscriptions.size(); j++) {
                                if (null != products.get(i) && null != subscriptions.get(j) && products.get(i).getID().equals(subscriptions.get(j).getProductID())) {
                                    subscriptions.get(j).setPrice(products.get(i).getPrice());
                                    subscriptions.get(j).setExtensionFields(products.get(i).getCustomFields());
                                }
                            }
                        }
                    }
                    queryProductInfoResponse.setProducts(subscriptions);
                }

                mView.loadSubscription(queryProductInfoResponse);

            } else {
                mView.queryProductInfoFail();
            }
        }, throwable -> {
            mView.queryProductInfoFail();
        });
    }

    public void unsuscribe(CancelSubscribeRequest cancelSubscribeRequest, RxCallBack callBack) {
        HttpApi.getInstance().getService().unsubscribe(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.CANCEL_SUBSCRIBE, cancelSubscribeRequest)
                .compose(onCompose(mView.bindToLife())).subscribe(callBack);
    }
}
