package com.pukka.ydepg.common.http.vss;

public enum ResultCode {
    OK("0", "成功"),
    INEXISTENT_ORDER("E100019","订单不存在"),
    INVALID_ORDER_STATUS("E100020","订单状态不正确"),
    PAYED_ORDER_AND_COULD_NOT_CANCEL("E100030","订单已支付，无法取消"),
    UNKNOWN_SERVER_ERROR("E100030","服务器发生未知的错误");

    private String code;
    private String description;

    ResultCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }


    @Override
    public String toString() {
        return String.format("{\"code\":%s, \"description\":%s}", this.code, this.description);
    }
}
