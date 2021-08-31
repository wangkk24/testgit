package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.node.PayChnRstInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.vss.node.OrderInfo;
import com.pukka.ydepg.common.http.vss.request.CancelOrderRequest;
import com.pukka.ydepg.common.http.vss.response.CancelOrderResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.mytv.presenter.PayModePresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayModeContract;
import com.pukka.ydepg.moudule.mytv.utils.PaymentEvent;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.pukka.ydepg.moudule.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity.PAYMENT_THIRD_PART_PAY;
import static com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity.PAYMENT_UNI_PAY;

public abstract class NewPaymentFragment extends BaseFragment implements PayModeContract.View {

    protected final String TAG=this.getClass().getName();

    private PayModePresenter mPayModePresenter;

    //TODO
    private VODDetail mVodDetail;
    private Product   mProduct;

    private  int checkedPayment=PAYMENT_UNI_PAY;

    /**
     * 第三方支付加载成功后的orderId
     */
    protected String successOrderId;

    /**
     * 是否可以点击返回
     */
    protected boolean isCanPressBack=true;

    /**
     * 第三方支付信息
     */
    protected OrderInfo mOrderInfo = null;

    public abstract void      executePay();
    public abstract VODDetail initVodDetail();
    public abstract Product   initProduct();
    public abstract int       initCheckedPayment();
    public abstract boolean   istrySee();
    public abstract boolean   isOrdercenter();
    public abstract Product   getMarketProduct();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPayModePresenter = new PayModePresenter();
        mPayModePresenter.attachView(this);
    }

    public void  checkOrderBeforeSubcribe(){
        mVodDetail =initVodDetail();
        mProduct=initProduct();
        checkedPayment=initCheckedPayment();
        if(mProduct==null){
            return;
        }

        if(NewMyPayModeActivity.cachaOrderInfo != null&&!mPayModePresenter.isOrderExpired(NewMyPayModeActivity.cachaOrderInfo.getExpireTime())&&checkedPayment == PAYMENT_THIRD_PART_PAY) {
            //第三方支付如果没有加载出来
            SuperLog.info2SD(TAG,"Thirdpay->cacheOrder expireTime:"+NewMyPayModeActivity.cachaOrderInfo.getExpireTime());
            if(!NewMyPayModeActivity.cachaOrderInfo.getOrderID().equals(successOrderId)){
                unfinishedOrderExist(NewMyPayModeActivity.cachaOrderInfo);
            }else{
                // 页面加载成功后启动定时器，轮训订单状态
                PaymentEvent event = new PaymentEvent(PaymentEvent.EventCode.CHECK_ORDER_STATUS_BEFORE_CALLBACK,NewMyPayModeActivity.PAYMENT_THIRD_PART_PAY);
                EventBus.getDefault().post(event);
            }
        }else {
            SuperLog.info2SD(TAG,"Thirdpay->queryOrderInfo request");
            mPayModePresenter.checkOrderStatusBeforeExecutePay(OTTApplication.getContext(), mProduct.getID(), null);
        }
    }

    @Override
    public void cancelOrderSuccess(CancelOrderResponse response, Bundle extraData) {
        NewMyPayModeActivity.cachaOrderInfo=null;
        if(PAYMENT_THIRD_PART_PAY == checkedPayment){
            Log.i(TAG, "cancelOrderSuccess: 12345");
            mPayModePresenter.setOrderCenter(isOrdercenter());
            mPayModePresenter.setTrySee(istrySee());
            mPayModePresenter.setMarketProduct(getMarketProduct());
            mPayModePresenter.vssOrderProduct(OTTApplication.getContext(), mVodDetail, mProduct);
        } else {
            executePay();
        }
    }

    @Override
    public void cancelOrderFailed() {
        EpgToast.showLongToast(OTTApplication.getContext(), getString(R.string.subscirbe_fail));
    }

    @Override
    public void initPresenter() {}

    public boolean isCanPressBack() {
        return isCanPressBack;
    }

    @Override
    public void orderProductSuccess(OrderInfo orderInfo) {
        NewMyPayModeActivity.cachaOrderInfo = orderInfo;
        mOrderInfo=orderInfo;
        executePay();
    }

    @Override
    public void orderProductFailed() {
        EpgToast.showLongToast(OTTApplication.getContext(), getString(R.string.subscirbe_fail));
    }

    @Override
    public void unfinishedOrderExist(OrderInfo orderInfo) {
        if (orderInfo != null) {
            NewMyPayModeActivity.cachaOrderInfo = orderInfo;
            // 判断订单类型，以及当前用户选中的支付类型，如果订单的支付类型和当前选择的支付类型一致，那么直接使用该订单发起第三方支付
            // 否则先取消掉未完成订单然后重新下单支付
            List<PayChnRstInfo> listPayChnRstInfo = NewMyPayModeActivity.cachaOrderInfo.getPayChannel();
            if (!(listPayChnRstInfo == null || listPayChnRstInfo.isEmpty())) {
                PayChnRstInfo payChnRstInfo = listPayChnRstInfo.get(0);
                String channelType = payChnRstInfo.getChannelType();
                if (PAYMENT_THIRD_PART_PAY == checkedPayment && Constant.CHANNEL_TYPE_THIRD_PART_PAYMENT.equals(channelType)) {
                    // 更新Fragment的订单数据
                    mOrderInfo=orderInfo;
                    // 发起订购
                    executePay();
                } else {
                    // 取消订单
                    CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
                    cancelOrderRequest.setUserID(AuthenticateManager.getInstance().getUserInfo().getUserId());
                    cancelOrderRequest.setMessageID(PaymentUtils.generateMessageID());
                    cancelOrderRequest.setOrderID(NewMyPayModeActivity.cachaOrderInfo.getOrderID());
                    mPayModePresenter.vssCancelOrder(cancelOrderRequest, null, OTTApplication.getContext());
                }
            }
        } else {
            // 发起订购
            // 如果是第三方支付，需要先创建订单
            if (PAYMENT_THIRD_PART_PAY == checkedPayment) {
                SuperLog.info2SD(TAG,"Thirdpay-> create orderProduct request");
                Log.i(TAG, "unfinishedOrderExist:  12345");
                mPayModePresenter.setOrderCenter(isOrdercenter());
                mPayModePresenter.setTrySee(istrySee());
                mPayModePresenter.setMarketProduct(getMarketProduct());
                mPayModePresenter.vssOrderProduct(OTTApplication.getContext(), mVodDetail, mProduct);
            } else {
                executePay();
            }
        }
    }

    @Override
    public void checkUnfinishedOrderFailed() {
        if(checkedPayment == PAYMENT_THIRD_PART_PAY) {
            EpgToast.showToast(OTTApplication.getContext(),getString(R.string.third_part_payment_load_failed_notification_message));
        } else {
            EpgToast.showToast(OTTApplication.getContext(),getString(R.string.subscirbe_fail));
        }
    }

    @Override
    public void onDestroy() {
        successOrderId=null;
        super.onDestroy();
    }

    @Override
    public <Z> LifecycleTransformer<Z> bindToLife()
    {
        return null;
    }
}