package com.pukka.ydepg.moudule.mytv.presenter.view;

import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.view.OrderedView.java
 * @author:xj
 * @date: 2018-01-26 10:39
 */

public interface OrderedView {
    void queryProductInfoSuccess(QueryProductInfoResponse queryProductInfoResponse);

    void queryProductInfoFail();


    void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse);

    void unsubscribeFail();
}
