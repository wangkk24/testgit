package com.pukka.ydepg.moudule.mytv.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 终端配置参数中用来展示"我要订购"中产品列表的实体类
 *
 * @FileName: com.pukka.ydepg.moudule.mytv.bean.OrderItemBean.java
 * @author: luwm
 * @data: 2018-08-16 16:25
 * @Version V1.0 <描述当前版本功能>
 */
public class OrderItemBean {
    //颜色枚举值
    public final static String COLOR_RED = "red";
    public final static String COLOR_YELLOW = "yellow";

    @SerializedName("id")
    private String id;
    @SerializedName("superscript")
    private String suprScript;
    //新增一个颜色
    @SerializedName("color")
    private String color;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuprScript() {
        return suprScript;
    }

    public void setSuprScript(String suprScript) {
        this.suprScript = suprScript;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
