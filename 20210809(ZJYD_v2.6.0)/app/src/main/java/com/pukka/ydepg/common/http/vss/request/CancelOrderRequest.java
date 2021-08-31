package com.pukka.ydepg.common.http.vss.request;

public class CancelOrderRequest {
    /**
     * 消息唯一标识
     */
    private String messageID;

    /**
     * 订单ID
     */
    private String orderID;

    /**
     * 用户ID，第三方支付场景下必填
     */
    private String userID;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
