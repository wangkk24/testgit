package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event;

public class ProductOrderMutiSwitchEvent {

    int section = -1;
    int postion = -1;

    public ProductOrderMutiSwitchEvent(int section, int postion) {
        this.section = section;
        this.postion = postion;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
