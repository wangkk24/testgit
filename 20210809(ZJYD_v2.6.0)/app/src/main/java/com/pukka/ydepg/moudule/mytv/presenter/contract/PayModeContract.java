package com.pukka.ydepg.moudule.mytv.presenter.contract;

import android.content.Context;
import android.os.Bundle;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.vss.request.CancelOrderRequest;
import com.pukka.ydepg.common.http.vss.node.OrderInfo;
import com.pukka.ydepg.common.http.vss.request.QueryOrderInfoRequest;
import com.pukka.ydepg.common.http.vss.response.CancelOrderResponse;
import com.pukka.ydepg.common.http.vss.response.QueryOrderInfoResponse;
import com.pukka.ydepg.common.utils.RxTimerUtil;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;
import io.reactivex.Observable;

public interface PayModeContract {
    public interface View extends BaseView {

        public void cancelOrderSuccess(CancelOrderResponse response, Bundle extraData);

        public void cancelOrderFailed();

        public void initPresenter();

        public void orderProductSuccess(OrderInfo response);

        public void orderProductFailed();

        public void unfinishedOrderExist(OrderInfo orderInfo);

        public void checkUnfinishedOrderFailed();

    }

    public abstract class Presenter extends BasePresenter<View> {

        public abstract Observable<QueryOrderInfoResponse> vssQueryOrderInfo(QueryOrderInfoRequest request, Context context);

        public abstract void vssCancelOrder(CancelOrderRequest request, Bundle extraData, Context context);

        public abstract boolean isOrderExpired(String expireTime);

        public abstract void vssOrderProduct(Context context, VODDetail vodDetail, Product product);

        public abstract void updateThirdPartPaymentFragmentData(Bundle data);

        public abstract void checkOrderStatusBeforeCallback(RxTimerUtil.IRxNext task);

        public abstract void checkOrderStatusAfterCallback(RxTimerUtil.IRxNext task);

        public abstract void checkOrderStatusBeforeExecutePay(Context context, String productId, Bundle extraData);
    }
}
