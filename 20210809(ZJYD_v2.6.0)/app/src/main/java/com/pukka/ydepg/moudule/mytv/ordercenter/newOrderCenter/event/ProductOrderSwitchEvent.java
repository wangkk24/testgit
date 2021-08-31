package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event;

public class ProductOrderSwitchEvent {
    //订购页面刷新页面事件
    private int position = -1;

    public ProductOrderSwitchEvent(int position) {
        this.position = position;
    }

    //产品包的索引
    public int getPostion() {
        return position;
    }

    public void setPostion(int position) {
        this.position = position;
    }
}
