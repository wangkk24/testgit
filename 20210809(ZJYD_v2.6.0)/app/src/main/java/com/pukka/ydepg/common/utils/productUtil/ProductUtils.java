package com.pukka.ydepg.common.utils.productUtil;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pukka.ydepg.common.http.bean.node.SubProdInfo;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectForVSS;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.GetProductMutExRelaRequest;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.control.selfregister.AppInfoCallBack.QueryProductInfoCallBack;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.service.NtpTimeService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductUtils {
    private static final String TAG = "ProductUtils";
    public static final String TARGET_PRODUCT_ID = "target_product_id";
    public static final String SUBSCRIBED_PRODUCT_ID = "subscribed_product_id";
    private static List<String> productIDs = new ArrayList<>();
    private static String targetProductIDCache;
    private static ProductPresenter productPresenter;
    private static final String PAGESIZE = "30";
    private static ProductUtilCallback productUtilCallback;

    private static List<String> cacheProductIDs=new ArrayList<>();

    /**
     * ??????????????????
     * @param subscribeProductID ????????????????????? ?????????????????????
     * @param targetProductID    ???????????????????????? ????????????????????????
     * @return ???????????????0???????????????
     *                 1????????????????????????????????????
     *                 2?????????subscribeProduct,??????targetProduct
     */
    private static int checkSubscribeProductType(String subscribeProductID, @NonNull String targetProductID){
        if(null == subscribeProductID){
            //??????????????????????????????????????????????????????
            return 0;   //????????????
        }
        //???????????????????????????
        Map<String, List<String>> productPackageRelationship =  SessionService.getInstance().getSession().getTerminalConfigurationProductPackageRelationship();
        if(null != productPackageRelationship) {
            List<String> listChildOfSubscribe = productPackageRelationship.get(subscribeProductID);//???????????????????????????
            List<String> listChildOfTarget    = productPackageRelationship.get(targetProductID);   //???????????????????????????
            if(!CollectionUtil.isEmpty(listChildOfSubscribe) && listChildOfSubscribe.contains(targetProductID)){
                //?????????????????????????????????????????????
                return 1;   //?????????????????????????????????
            } else if(!CollectionUtil.isEmpty(listChildOfTarget) && listChildOfTarget.contains(subscribeProductID)){
                //?????????????????????????????????????????????
                return 2;   //??????subscribeProduct,??????targetProduct
            } else {
                //????????????????????????????????????????????????
                return 0;   //????????????
            }
        } else {
            //??????????????????????????????????????????????????????
            return 0;   //????????????
        }
    }

    private static void checkSubscribeProductsType(List<String> subscribeProducts, @NonNull String targetProductID){
        Set<String> set=new HashSet(subscribeProducts);
        subscribeProducts=new ArrayList<>(set);
        Map<String, List<String>> params = new HashMap<>();
        List<String> targetProducts = new ArrayList<>();
        targetProducts.add(targetProductID);
        params.put(TARGET_PRODUCT_ID, targetProducts);
        List<String> unsubscribeProductIDs = new ArrayList<>();
        if(null != subscribeProducts && subscribeProducts.size() > 0){
            for(String subscribeProductID:subscribeProducts){
                switch (checkSubscribeProductType(subscribeProductID, targetProductID)){
                    case 1:
                        productUtilCallback.onCheckPackageRelationshipSuccess(null);
                        return;
                    case 2:
                        unsubscribeProductIDs.add(subscribeProductID);
                        break;
                    default:
                        break;
                }
            }
        }
        params.put(SUBSCRIBED_PRODUCT_ID, unsubscribeProductIDs);
        productUtilCallback.onCheckPackageRelationshipSuccess(params);
    }

    public static void checkSubscribeProductsType(RxAppCompatActivity rxAppCompatActivity, ProductUtilCallback callback, @NonNull String targetProductID) {
        SuperLog.debug(TAG, "checkSubscribeProductsType");
        productPresenter = new ProductPresenter(rxAppCompatActivity);
        SuperLog.debug(TAG, "!targetProductID.equals(targetProductIDCache)");
        targetProductIDCache = targetProductID;
        productIDs.clear();
        cacheProductIDs.clear();
        productUtilCallback = callback;
        DSVQuerySubscription request = new DSVQuerySubscription();
        request.setOffset("0");
        request.setCount(PAGESIZE);
        productPresenter.queryProductInfo(request, queryProductInfoCallBack);
    }

    public static void checkSubscribeProductsPages() {
        if( null != productPresenter ) {
            DSVQuerySubscription request = new DSVQuerySubscription();
            request.setOffset(cacheProductIDs.size() + "");
            request.setCount(PAGESIZE);
            productPresenter.queryProductInfo(request, queryProductInfoCallBack);
        }
    }

    private static QueryProductInfoCallBack queryProductInfoCallBack = new QueryProductInfoCallBack() {
        @Override
        public void queryProductInfoSuccess(QueryProductInfoResponse queryProductInfoResponse) {
            SuperLog.debug(TAG, "queryProductInfoSuccess");
            if(null != queryProductInfoResponse.getProducts() && queryProductInfoResponse.getProducts().size() > 0){
                for(Subscription subscription:queryProductInfoResponse.getProducts()){
                    SuperLog.debug(TAG, "subscription.getProductID() is :" +subscription.getProductID());
                        if(!TextUtils.isEmpty(subscription.getEndTime())){
                            Long endTime=Long.parseLong(subscription.getEndTime());
                            if(endTime> NtpTimeService.queryNtpTime())
                            {
                                productIDs.add(subscription.getProductID());
                            }
                        }
                    cacheProductIDs.add(subscription.getProductID());
                }
                if(cacheProductIDs.size() < Integer.parseInt(queryProductInfoResponse.getTotal())){
                    checkSubscribeProductsPages();
                }
                else{
                    checkSubscribeProductsType(productIDs, targetProductIDCache);
                }
            }else{
                if(null!=productUtilCallback)
                productUtilCallback.onQueryProductEmpty();
            }
        }

        @Override
        public void queryProductInfoFail() {
            productUtilCallback.onQueryProductInfoFailed();
        }

        @Override
        public void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse) { }

        @Override
        public void unsubscribeFail() { }

        @Override
        public void queryProductByPriceSuccess(QueryProductInfoResponse queryProductInfoResponse) { }
    };

    //?????????????????????????????? done by weicy 2020.8.13
    public static void getProductMutExRela(RxAppCompatActivity rxAppCompatActivity, SelfAppInfoController.GetProductMutExRelaCallBack callback, @NonNull Product targetProduct, VODDetail vodDetail){
        GetProductMutExRelaRequest request = new GetProductMutExRelaRequest();
        productPresenter = new ProductPresenter(rxAppCompatActivity);

        SubProdInfo subProdInfo = new SubProdInfo();
        subProdInfo.setAmount("1");
        subProdInfo.setProductId(targetProduct.getID());
        if (targetProduct.getProductType().equals("0")){
            //????????????????????????
            subProdInfo.setRenewFlag(isAutoExtend(targetProduct));
            subProdInfo.setType(SubProdInfo.SubProdInfoType.PERIOD_PRODUCT);
        }else if (targetProduct.getProductType().equals("1")) {
            PriceObjectForVSS pb = new PriceObjectForVSS();
            if (null != vodDetail)
            {
                pb.setID(vodDetail.getCode());
                pb.setType("200");
            }
            subProdInfo.setObject(pb);
            //?????????????????????
            subProdInfo.setRenewFlag(Subscribe.IsAutoExtend.NO_RENEW);
            subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
        }
        request.setSubInfo(subProdInfo);

        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserID(mUserInfo.getUserId());

        productPresenter.getProductMutExRela(request,callback);
    }

    /**
     * ????????????????????????
     *
     * @param product ??????
     */
    public static String isAutoExtend(Product product) {
        if (product == null) {
            return "0";
        }
        //???????????? > 0????????????; 1?????????
        //???????????????????????????......
        return product.getIsAutoExtend();
    }





}