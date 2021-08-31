package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PriceObjectForVSS {
    /**
     * ID :
     * type :
     * contentType :
     * extensionFields :
     */

    /**
     *定价对象ID
     */
    @SerializedName("id")
    private String ID;

    /**
     =0，通用商品
     =1，虚拟币（游戏）
     =2，游戏应用
     =3，应用内游戏道具
     =4，内容包
     =5，会员权益
     =6，定向流量
     =7，通用流量
     =50内容目录
     =100 套餐类营销活动
     =200 点播内容
     =201 直播内容
     =400 服务
     填写200或不填
     */
    @SerializedName("type")
    private String type;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
