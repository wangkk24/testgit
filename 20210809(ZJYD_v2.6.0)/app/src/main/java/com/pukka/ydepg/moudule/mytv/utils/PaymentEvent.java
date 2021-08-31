package com.pukka.ydepg.moudule.mytv.utils;

import android.os.Bundle;

public class PaymentEvent {


    private int code;

    private Bundle data;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    //查询订单请求的发起者 0:第三方支付 1:(其他)手机支付 2(关联手机)统一支付
    private int source;

    public PaymentEvent(int code,int source) {
        this.code = code;
        this.source = source;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public interface EventCode {
       int CHECK_ORDER_STATUS_BEFORE_CALLBACK = 100860001;
        int CHECK_ORDER_STATUS_AFTER_CALLBACK = 100860002;
        int UPDATE_THIRD_PART_PAYMENT_FRAGMENT_DATA = 10086003;
    }

    public interface EventDataKey {
        /**
         * 发送事件的支付类型
         */
        public static String TRIGGER_PAYMENT = "EVENT_KEY_TRIGGER_PAYMENT";

    }
}
