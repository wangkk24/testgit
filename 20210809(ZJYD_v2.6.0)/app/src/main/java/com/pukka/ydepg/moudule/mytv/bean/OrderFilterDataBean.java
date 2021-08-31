package com.pukka.ydepg.moudule.mytv.bean;

/**
 * Created by hasee on 2017/8/28.
 */

public class OrderFilterDataBean {
    public  enum FILTERTYPE{
        TIME,
        TYPE
    }
    private FILTERTYPE mType;

    private String fromDateTime;

    private String toDateTime;
    /**
     *
     •1 ：按次
     •0 ：包周期，比如包天、包周、包月等
     */
    private String productType;

    private String name;

    public FILTERTYPE getmType() {
        return mType;
    }

    public String getName() {
        return name;
    }
    public String getFromDateTime() {
        return fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public void setmType(FILTERTYPE mType) {
        this.mType = mType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }
}
