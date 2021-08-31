package com.pukka.ydepg.moudule.mytv.presenter.view;

import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;

import java.util.List;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView.java
 * @author: luwm
 * @data: 2018-08-15 15:16
 * @Version V1.0 <描述当前版本功能>
 */
public interface OrderCenterView extends IBaseContact.IBaseView {
    void querySubscriberSucess(String userId);

    void loadSubscription(QueryProductInfoResponse response);

    void queryProductInfoFail();

    void loadProducts(List<Product> products);

}
