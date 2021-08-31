package com.pukka.ydepg.moudule.mytv.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.PayChnRstInfo;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.vss.node.*;
import com.pukka.ydepg.common.http.vss.request.CancelOrderRequest;
import com.pukka.ydepg.common.http.vss.request.OrderProductRequest;
import com.pukka.ydepg.common.http.vss.request.QueryOrderInfoRequest;
import com.pukka.ydepg.common.http.vss.response.CancelOrderResponse;
import com.pukka.ydepg.common.http.vss.response.OrderProductResponse;
import com.pukka.ydepg.common.http.vss.response.QueryOrderInfoResponse;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxTimerUtil;
import com.pukka.ydepg.common.utils.base64Utils.AepAuthUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.bean.LogDescExtInfo;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayModeContract;

import com.pukka.ydepg.service.NtpTimeService;
import io.reactivex.Observable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayModePresenter extends PayModeContract.Presenter {

    private boolean isTrySee;
    private boolean isOrderCenter;
    private Product marketProduct;

    private static final String TAG = PayModePresenter.class.getSimpleName();
    /**
     *  失效时间，秒
     */
    private  long expiredTimeDuration=4*60+30L;

    @Override
    public Observable<QueryOrderInfoResponse> vssQueryOrderInfo(QueryOrderInfoRequest request, Context context) {
        String interfaceName = HttpConstant.QUERY_ORDER_INFO;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        return HttpApi.getInstance().getService().vssQueryOrderInfo(url, request).compose(compose(getBaseView().bindToLife()));
    }

    @Override
    public void vssCancelOrder(CancelOrderRequest request, Bundle extraData, Context context) {
        String interfaceName = HttpConstant.CANCEL_ORDER;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().vssCancelOrder(url, request).compose(compose(getBaseView().bindToLife()))
                .subscribe(new RxCallBack<CancelOrderResponse>(HttpConstant.CANCEL_ORDER, context) {
                    @Override
                    public void onSuccess(CancelOrderResponse cancelOrderResponse) {
                        if (!isViewAttached()) {
                            SuperLog.error(TAG, "The view is not attached");
                            return;
                        }
                        if (null != cancelOrderResponse
                                && (TextUtils.equals(cancelOrderResponse.getCode(), Result.RETCODE_OK)
                                || TextUtils.equals(cancelOrderResponse.getCode(), Result.RETCODE_OK_TWO))) {
                            getBaseView().cancelOrderSuccess(cancelOrderResponse, extraData);
                        } else {
                            SuperLog.info2SD(TAG, "Cancel Order failed. The response is: [" + JsonParse.object2String(cancelOrderResponse) + "]");
                            getBaseView().cancelOrderFailed();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG, e);
                    }
                });
    }

    @Override
    public boolean isOrderExpired(String expireTime) {
        if (TextUtils.isEmpty(expireTime)) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Calendar expireTimeCalendar = Calendar.getInstance();
            expireTimeCalendar.setTime(formatter.parse(expireTime));
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            return expireTimeCalendar.before(now);
        } catch (ParseException e) {
            return true;
        }
    }

    @Override
    public void vssOrderProduct(Context context, VODDetail vodDetail, Product product) {
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo) {
            OrderProductRequest request = new OrderProductRequest();

            request.setTransactionId(AepAuthUtil.getNonce());

            User mUser = new User();
            mUser.setId(mUserInfo.getUserId());
            request.setUserID(mUser);

            SubProdInfo subProdInfo = new com.pukka.ydepg.common.http.vss.node.SubProdInfo();
            if (product.getProductType().equals("0")){
                subProdInfo.setType(SubProdInfo.SubProdInfoType.PERIOD_PRODUCT);
            }else{
                subProdInfo.setType(SubProdInfo.SubProdInfoType.ONCE_PRODUCT);
                // 产品权益列表
                PriceObject object = new PriceObject();
                if (null!= vodDetail){
                    object.setId(vodDetail.getCode());
                }
                object.setType("200");

                subProdInfo.setObject(object);
            }

            if (null != vodDetail){
                Map<String,String> temMap = new HashMap<>();
                temMap.put("trafficeAttractionContentId",vodDetail.getCode());
                subProdInfo.setExtensionInfo(temMap);
            }

            subProdInfo.setProductId(product.getID());

            subProdInfo.setRenewFlag(com.pukka.ydepg.common.http.v6bean.v6node.Subscribe.IsAutoExtend.NO_RENEW);

            request.setSubInofo(subProdInfo);

            PayInfo payInfo = new PayInfo();
            payInfo.setPayType("1");
            payInfo.setCurreny("CNY");
            List<PayChannel> payChannels = new ArrayList<PayChannel>();
            PayChannel payChannel = new PayChannel();
            payChannel.setChannelType("100");
            payChannel.setCurrency("CNY");
            payChannels.add(payChannel);
            payInfo.setPayChannels(payChannels);
            request.setPayInfo(payInfo);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);

            //内容code统计需求
            LogDescExtInfo IdInfo = null;
            if (null != vodDetail){
                IdInfo = new LogDescExtInfo();
                IdInfo.setKey("contentID");
                IdInfo.setValue(vodDetail.getCode());
            }

            //来源统计需求
            LogDescExtInfo fromInfo = new LogDescExtInfo();
            fromInfo.setKey("fromPage");
            if (!TextUtils.isEmpty(UBDPurchase.getSrcch())){
                fromInfo.setValue("4");
            }else if (isOrderCenter){
                fromInfo.setValue("3");
            }else if (isTrySee){
                fromInfo.setValue("2");
            }else{
                fromInfo.setValue("1");
            }

            List<LogDescExtInfo> list = new ArrayList<>();

            list.add(fromInfo);
            if (null != IdInfo){
                list.add(IdInfo);
            }

            String listStr = JsonParse.object2String(list);

            Map<String,String> map = new HashMap<>();

            //营销活动
            if (null != marketProduct && null != marketProduct.getMarketing())
            {
                List<Marketing> marketlist = new ArrayList<>();
                Marketing marketing = marketProduct.getMarketing();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                marketing.setStartTime(str);
                marketing.setEndTime(null);
                marketlist.add(marketing);
                String marketingStr = JsonParse.object2String(marketlist);
                if (null != marketingStr){
                    map.put("promListBoss",marketingStr);
                }
            }

            if (null != listStr){
                map.put("logDescExtInfo",listStr);
            }

            request.setExtentionInfo(map);

            request.setEffectTime(sdf.format(new Date(NtpTimeService.queryNtpTime())));
            String interfaceName = HttpConstant.ORDER_PRODUCT;
            String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
            Log.i(TAG, "vssOrderProduct:  12345 发请求");
            HttpApi.getInstance().getService().vssOrderProduct(url, request)
                    .compose(compose(getBaseView().bindToLife())).subscribe(new RxCallBack<OrderProductResponse>(HttpConstant.ORDER_PRODUCT, context) {
                @Override
                public void onSuccess(OrderProductResponse orderProductResponse) {
                    Log.i(TAG, "onSuccess:  12345 response");
                    if (null != orderProductResponse
                            && (TextUtils.equals(orderProductResponse.getResult().getResultCode(), Result.RETCODE_OK)
                            || TextUtils.equals(orderProductResponse.getResult().getResultCode(), Result.RETCODE_OK_TWO))) {
                        Log.i(TAG, "onSuccess: 12345 success");
                        OrderInfo orderInfo = new OrderInfo();
                        orderInfo.setOrderID(orderProductResponse.getOrderId());
                        orderInfo.setUserID(mUserInfo.getUserId());
                        orderInfo.setCreateTime(orderProductResponse.getCreateTime());
                        SuperLog.info2SD(TAG,"Thirdpay ->create orderProduct success->CreateTime:"+orderProductResponse.getCreateTime());
                        if(!TextUtils.isEmpty(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION))){
                           expiredTimeDuration=Long.parseLong(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION));
                        }
                        try
                        {
                            long  expiredTime= sdf.parse(orderProductResponse.getCreateTime()).getTime()+expiredTimeDuration*1000;
                            orderInfo.setExpireTime(sdf.format(new Date(expiredTime)));
                        }
                        catch (ParseException e)
                        {
                            SuperLog.error(TAG,e);
                        }
                        orderInfo.setOrderStatus(orderProductResponse.getStatus());
                        orderInfo.setFee(orderProductResponse.getFee());
                        orderInfo.setCallbackURL(orderProductResponse.getCallbackURL());
                        List<PayChnRstInfo> payChnRstInfos = orderProductResponse.getPayChanResInfos();
                        orderInfo.setPayChannel(payChnRstInfos);
                        SuperLog.debug(TAG,"orderProduct Success");
                        getBaseView().orderProductSuccess(orderInfo);
                    } else {
                        //添加判断是互斥产品包的逻辑
                        Log.i(TAG, "onSuccess: 12345 "+ orderProductResponse.getResult().getResultCode() + " "+ Result.RETCODE_MUTEX);
                        if (null != orderProductResponse && TextUtils.equals(orderProductResponse.getResult().getResultCode(), Result.RETCODE_MUTEX)){
                            String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
                            if (null != mutexStr && mutexStr.length()>0){
                                EpgToast.showLongToast(OTTApplication.getContext(),
                                        mutexStr);
                            }else{
                                EpgToast.showLongToast(OTTApplication.getContext(),
                                        context.getResources().getString(R.string.big_small_product_mutex));
                            }
                            return;
                        }


                        SuperLog.error(TAG,"Order product failed");
                        //错误提示
                        EpgToast.showToast(context, "订购失败");
                    }
                }

                @Override
                public void onFail(Throwable e) {
                }
            });
        }
    }

    @Override
    public void updateThirdPartPaymentFragmentData(Bundle data) {

    }

    @Override
    public void checkOrderStatusBeforeCallback(RxTimerUtil.IRxNext task) {
        String intervalString = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.CHECK_ORDER_STATUS_INTERVAL);
        Integer interval;
        try {
            interval = Integer.parseInt(intervalString) * 1000;
        } catch (NumberFormatException e) {
            interval = 30000;
            SuperLog.warn(TAG, "Get the terminal value [check_order_status_interval] failed, use default value:[" + interval / 1000 + " second]");
        }
        RxTimerUtil.interval(interval, task);
    }

    @Override
    public void checkOrderStatusAfterCallback(RxTimerUtil.IRxNext task) {
        String intervalString = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.CHECK_ORDER_STATUS_AFTER_CALLBACK_INTERVAL);
        Integer interval ;
        try {
            interval = Integer.parseInt(intervalString) * 1000;
        } catch (NumberFormatException e) {
            interval = 2000;
        }
        RxTimerUtil.interval(interval, task);
    }

    @Override
    public void checkOrderStatusBeforeExecutePay(Context context, String productId, Bundle extraData) {
        SuperLog.debug(TAG,"queryOrderInfo request");
        QueryOrderInfoRequest request = new QueryOrderInfoRequest();
        if(null!=AuthenticateManager.getInstance().getUserInfo()) {
            request.setUserID(AuthenticateManager.getInstance().getUserInfo().getUserId());
        }
        request.setProductID(productId);
        request.setStatus("1,2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Observable<QueryOrderInfoResponse> observable = vssQueryOrderInfo(request, context);
        observable.subscribe(new RxCallBack<QueryOrderInfoResponse>(HttpConstant.QUERY_ORDER_INFO, context) {
            @Override
            public void onSuccess(QueryOrderInfoResponse queryOrderInfoResponse) {
                if (null != queryOrderInfoResponse
                        && (TextUtils.equals(queryOrderInfoResponse.getCode(), Result.RETCODE_OK)
                        || TextUtils.equals(queryOrderInfoResponse.getCode(), Result.RETCODE_OK_TWO))) {
                    List<OrderInfo> orders = queryOrderInfoResponse.getOrderList();
                    OrderInfo unfinishedOrder = null;
                    if (orders != null && !orders.isEmpty()) {
                        unfinishedOrder = orders.get(0);
                    }
                    if(null!=unfinishedOrder&&TextUtils.isEmpty(unfinishedOrder.getExpireTime())&&!TextUtils.isEmpty(unfinishedOrder.getCreateTime())){
                        if(!TextUtils.isEmpty(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION))){
                            expiredTimeDuration=Long.parseLong(SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.THIRD_PAY_EXPIRED_TIME_DURATION));
                        }
                        try
                        {
                            long  expiredTime= sdf.parse(unfinishedOrder.getCreateTime()).getTime()+expiredTimeDuration*1000;
                            unfinishedOrder.setExpireTime(sdf.format(new Date(expiredTime)));
                        }
                        catch (ParseException e)
                        {
                            SuperLog.error(TAG,e);
                        }
                    }
                    SuperLog.info2SD(TAG,"Thirdpay-> queryOrderInfo success");
                    getBaseView().unfinishedOrderExist(unfinishedOrder);
                } else {
                    SuperLog.error(TAG, String.format(Locale.CHINA
                            , "Query order info  failed. The response is: %s"
                            , JsonParse.object2String(queryOrderInfoResponse)));
                    getBaseView().checkUnfinishedOrderFailed();
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, e);
                getBaseView().checkUnfinishedOrderFailed();
            }
        });
    }

    public boolean isTrySee() {
        return isTrySee;
    }

    public void setTrySee(boolean trySee) {
        isTrySee = trySee;
    }

    public boolean isOrderCenter() {
        return isOrderCenter;
    }

    public void setOrderCenter(boolean orderCenter) {
        isOrderCenter = orderCenter;
    }

    public Product getMarketProduct() {
        return marketProduct;
    }

    public void setMarketProduct(Product marketProduct) {
        this.marketProduct = marketProduct;
    }
}
