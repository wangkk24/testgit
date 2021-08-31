package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.vss.node.OrderInfo;

import java.io.Serializable;
import java.util.List;

public class QueryOrderInfoResponse extends VSSBaseResponse implements Serializable {

    private static final long serialVersionUID = -5825517214040549776L;

    /**
     * 订单信息列表
     */
    @SerializedName("orderList")
    private List<OrderInfo> orderList;

    public List<OrderInfo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfo> orderList) {
        this.orderList = orderList;
    }
}
