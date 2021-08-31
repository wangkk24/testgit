package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLabelsByUserIdRequest {

//    public static final String SCENE_ID = "05921e0e8a57419a8773f88a6b55ac1e";
    public static final String SCENE_ID = "dd5402a1396c48e6835723669b8265e3";

    public static final int TYPE = 10;

    public static final int VT = 9;

    /**
     *场景编码
     */
    @SerializedName("sceneId")
    private String sceneId ;

    /**
     *1为传入的userId代表手机号码
     */
    @SerializedName("type")
    private int type;

    /**
     *页面版式，传9表示以json格式返回
     */
    @SerializedName("vt")
    private int vt;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVt() {
        return vt;
    }

    public void setVt(int vt) {
        this.vt = vt;
    }
}
