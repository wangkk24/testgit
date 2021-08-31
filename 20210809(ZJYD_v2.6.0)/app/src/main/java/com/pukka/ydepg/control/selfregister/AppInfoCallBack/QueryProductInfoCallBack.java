package com.pukka.ydepg.control.selfregister.AppInfoCallBack;

import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;

/**
 * Created by wgy on 2017/4/20.
 */
public interface QueryProductInfoCallBack{
    void queryProductInfoSuccess(QueryProductInfoResponse queryProductInfoResponse);

    void queryProductInfoFail();

    void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse);

    void unsubscribeFail();

    void queryProductByPriceSuccess(QueryProductInfoResponse queryProductInfoResponse);
}