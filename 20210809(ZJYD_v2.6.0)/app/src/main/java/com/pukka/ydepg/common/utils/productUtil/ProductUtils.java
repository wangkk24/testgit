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
     * 检测订购方式
     * @param subscribeProductID 已订购的产品包 参数值可以为空
     * @param targetProductID    想要订购的产品包 参数值不可以为空
     * @return 订购方式：0：执行订购
     *                 1：弹出不可订购的错误提示
     *                 2：退订subscribeProduct,订购targetProduct
     */
    private static int checkSubscribeProductType(String subscribeProductID, @NonNull String targetProductID){
        if(null == subscribeProductID){
            //用户当前点击的包和已订购的包没有关系
            return 0;   //执行订购
        }
        //获取大小包互斥关系
        Map<String, List<String>> productPackageRelationship =  SessionService.getInstance().getSession().getTerminalConfigurationProductPackageRelationship();
        if(null != productPackageRelationship) {
            List<String> listChildOfSubscribe = productPackageRelationship.get(subscribeProductID);//当前已订购包的子包
            List<String> listChildOfTarget    = productPackageRelationship.get(targetProductID);   //当前要订购包的子包
            if(!CollectionUtil.isEmpty(listChildOfSubscribe) && listChildOfSubscribe.contains(targetProductID)){
                //想要订购的包是已订购的包的小包
                return 1;   //弹出不可订购的错误提示
            } else if(!CollectionUtil.isEmpty(listChildOfTarget) && listChildOfTarget.contains(subscribeProductID)){
                //想要订购的包是已订购的包的大包
                return 2;   //退订subscribeProduct,订购targetProduct
            } else {
                //想要订购的包和已订购的包没有关系
                return 0;   //执行订购
            }
        } else {
            //用户当前点击的包和已订购的包没有关系
            return 0;   //执行订购
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

    //通过接口获取互斥关系 done by weicy 2020.8.13
    public static void getProductMutExRela(RxAppCompatActivity rxAppCompatActivity, SelfAppInfoController.GetProductMutExRelaCallBack callback, @NonNull Product targetProduct, VODDetail vodDetail){
        GetProductMutExRelaRequest request = new GetProductMutExRelaRequest();
        productPresenter = new ProductPresenter(rxAppCompatActivity);

        SubProdInfo subProdInfo = new SubProdInfo();
        subProdInfo.setAmount("1");
        subProdInfo.setProductId(targetProduct.getID());
        if (targetProduct.getProductType().equals("0")){
            //包周期看产品状态
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
            //按次不支持续订
            subProdInfo.setRenewFlag(Subscribe.IsAutoExtend.NO_RENEW);
            subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
        }
        request.setSubInfo(subProdInfo);

        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserID(mUserInfo.getUserId());

        productPresenter.getProductMutExRela(request,callback);
    }

    /**
     * 是否支持自动续订
     *
     * @param product 产品
     */
    public static String isAutoExtend(Product product) {
        if (product == null) {
            return "0";
        }
        //产品类型 > 0：包周期; 1：按次
        //包周期需要自动续订......
        return product.getIsAutoExtend();
    }





}