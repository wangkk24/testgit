package com.pukka.ydepg.event;

/**
 * Created by Eason on 10/15/2018.
 */

public class OrderCenterFocusEvent
{
    /**
     * 我要订购
     */
    public static final int GO_ORDER=1112;
    /**
     * 我已经订购
     */
    public static final int MY_ORDER=1113;

    private  int  orderStatic;

    public OrderCenterFocusEvent(int orderStatic){
        this.orderStatic=orderStatic;
    }

    public int getOrderStatic()
    {
        return orderStatic;
    }
}
