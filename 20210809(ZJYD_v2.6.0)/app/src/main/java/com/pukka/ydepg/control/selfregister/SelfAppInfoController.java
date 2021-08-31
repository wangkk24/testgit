package com.pukka.ydepg.control.selfregister;

import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6Controller.BaseController;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6request.GetProductMutExRelaRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetProductMutExRelaResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryProductResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.control.selfregister.AppInfoCallBack.QueryProductInfoCallBack;
import com.pukka.ydepg.launcher.session.SessionService;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;


/**
 * Created by wgy on 2017/4/19.
 */

public class SelfAppInfoController extends BaseController {

    private Disposable mProductInfo;

    public SelfAppInfoController(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    /**
     * 查询已订购产品列表
     */
    public void queryProductInfo(DSVQuerySubscription queryProductInfoRequest, ObservableTransformer<QueryProductInfoResponse, QueryProductInfoResponse> transformer, QueryProductInfoCallBack queryProductInfoCallBack) {
        mProductInfo = HttpApi.getInstance().getService().queryProductInfo(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_SUBSCRIPTION, queryProductInfoRequest)
                .compose(transformer).subscribe(queryProductInfoResponse -> {

                    if (null != queryProductInfoResponse.getResult() &&
                            (TextUtils.equals(queryProductInfoResponse.getResult().getRetCode(), ErrorCodeConstant.HTTP_OK) ||
                                    TextUtils.equals(queryProductInfoResponse.getResult().getRetCode(), Result.RETCODE_OK))) {
                        if (null != queryProductInfoCallBack) {
                            queryProductInfoCallBack.queryProductInfoSuccess(queryProductInfoResponse);
                        }
                    } else {
                        handleError(queryProductInfoResponse.getResult().getRetCode(), HttpConstant.QUERY_SUBSCRIPTION);
                        if (null != queryProductInfoCallBack) {
                            queryProductInfoCallBack.queryProductInfoFail();
                        }
                    }
                }, throwable -> {
                    if (null != queryProductInfoCallBack) {
                        queryProductInfoCallBack.queryProductInfoFail();
                        EpgToast.showToast(OTTApplication.getContext(), "服务暂不可用");
                    }
                });
    }

    public void cancelProductInfo(){
        if(mProductInfo != null){
            mProductInfo.dispose();
        }
    }

    /**
     * 查询已订购产品价格
     */
    public void queryProduct(ObservableTransformer<QueryProductResponse, QueryProductResponse> transformer, QueryProductInfoCallBack queryProductInfoCallBack,QueryProductInfoResponse queryProductInfoResponse){
        QueryProductRequest request=new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        if(null!=queryProductInfoResponse&&null!=queryProductInfoResponse.getProducts()&&queryProductInfoResponse.getProducts().size()>0){
            List<Subscription> subscriptions=queryProductInfoResponse.getProducts();
            List<String> productIds=new ArrayList<>();
            for(int i=0;i<subscriptions.size();i++){
                if(!TextUtils.isEmpty(subscriptions.get(i).getProductID())){
                    productIds.add(subscriptions.get(i).getProductID());
                }
            }
            request.setProductIds(productIds);
        }else{
            if (null != queryProductInfoCallBack) {
                queryProductInfoCallBack.queryProductByPriceSuccess(queryProductInfoResponse);
                return;
            }
        }
        HttpApi.getInstance().getService().queryProduct(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERY_PRODUCT, request)
                .compose(transformer).subscribe(queryProductResponse -> {

            if (null != queryProductResponse.getResult() &&
                    (TextUtils.equals(queryProductResponse.getResult().getRetCode(), ErrorCodeConstant.HTTP_OK) ||
                            TextUtils.equals(queryProductResponse.getResult().getRetCode(), Result.RETCODE_OK))) {

                List<Product> products= queryProductResponse.getProductList();
                if(null!=queryProductInfoResponse&&null!=queryProductInfoResponse.getProducts()&&queryProductInfoResponse.getProducts().size()>0){
                    List<Subscription> subscriptions=queryProductInfoResponse.getProducts();
                    if(null!=products&&!products.isEmpty()){
                        for(int i=0;i<products.size();i++){
                                for(int j=0;j<subscriptions.size();j++){
                                      if(null!=products.get(i)&&null!=subscriptions.get(j)&&products.get(i).getID().equals(subscriptions.get(j).getProductID())){
                                          subscriptions.get(j).setPrice(products.get(i).getPrice());
                                      }
                                }
                        }
                    }
                    queryProductInfoResponse.setProducts(subscriptions);
                }

                if (null != queryProductInfoCallBack) {
                    queryProductInfoCallBack.queryProductByPriceSuccess(queryProductInfoResponse);
                }

            } else {
                handleError(queryProductResponse.getResult().getRetCode(), HttpConstant.QUERY_SUBSCRIPTION);
                if (null != queryProductInfoCallBack) {
                    queryProductInfoCallBack.queryProductInfoFail();
                }
            }
        }, throwable -> {
            if (null != queryProductInfoCallBack) {
                queryProductInfoCallBack.queryProductInfoFail();
                EpgToast.showToast(OTTApplication.getContext(), "服务暂不可用");
            }
        });




    }

    /**
     * 查询已订购产品列表
     */
    public void unsubscribe(CancelSubscribeRequest cancelSubscribeRequest, ObservableTransformer<SubscribeDeleteResponse, SubscribeDeleteResponse> transformer, QueryProductInfoCallBack queryProductInfoCallBack) {
        mProductInfo = HttpApi.getInstance().getService().unsubscribe(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.CANCEL_SUBSCRIBE, cancelSubscribeRequest)
                .compose(transformer).subscribe(subscribeDeleteResponse -> {

                    if (null != subscribeDeleteResponse.getResult() &&
                            (TextUtils.equals(subscribeDeleteResponse.getResult().getRetCode(), ErrorCodeConstant.HTTP_OK) ||
                                    TextUtils.equals(subscribeDeleteResponse.getResult().getRetCode(), Result.RETCODE_OK))) {
                        if (null != queryProductInfoCallBack) {
                            queryProductInfoCallBack.unsubscribeSuccess(subscribeDeleteResponse);
                        }

                    } else {
                        handleError(subscribeDeleteResponse.getResult().getRetCode(), HttpConstant.QUERY_SUBSCRIPTION);
                        if (null != queryProductInfoCallBack) {
                            if(!OTTErrorWindowUtils.needShowErrorDialog()) {
                                queryProductInfoCallBack.unsubscribeFail();
                            }
                        }
                    }
                }, throwable -> {
                    if (null != queryProductInfoCallBack) {
                        queryProductInfoCallBack.unsubscribeFail();
                        EpgToast.showToast(OTTApplication.getContext(), "服务暂不可用");
                    }
                });
    }

    /**
     * 查询产品包互斥关系
     */
    public void getProductMutExRela(GetProductMutExRelaRequest request, ObservableTransformer<GetProductMutExRelaResponse, GetProductMutExRelaResponse> transformer, GetProductMutExRelaCallBack callBack) {
        mProductInfo = HttpApi.getInstance().getService().getProductMutExRela(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + HttpConstant.GET_PRODUCT_MUT_EX_RELA,request)
                .compose(transformer).subscribe(response -> {

                    if (null != response.getResult() &&
                            (TextUtils.equals(response.getResult().getResultCode(), ErrorCodeConstant.HTTP_OK) ||
                                    TextUtils.equals(response.getResult().getResultCode(), Result.RETCODE_OK))) {
                        if (null != callBack) {
                            callBack.GetProductMutExRelaCallBackSuccess(response);
                        }

                    } else {
                        if (null != response.getResult()){
                            handleError(response.getResult().getResultCode(), HttpConstant.GET_PRODUCT_MUT_EX_RELA);
                        }
                        if (null != callBack) {
                            if(!OTTErrorWindowUtils.needShowErrorDialog()) {
                                callBack.GetProductMutExRelaCallBackFailed();
                            }
                        }
                    }
                }, throwable -> {
                    if (null != callBack) {
                        callBack.GetProductMutExRelaCallBackFailed();
                        EpgToast.showToast(OTTApplication.getContext(), "服务暂不可用");
                    }
                });
    }

    public interface GetProductMutExRelaCallBack {
        void GetProductMutExRelaCallBackSuccess(GetProductMutExRelaResponse response);
        void GetProductMutExRelaCallBackFailed();
    }
}