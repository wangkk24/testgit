package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

import java.util.List;

/**
 * Created by liudo on 2018/4/12.
 */

public class GetStartPictureRequest {

    public static final String ACTION="getpicture";


    public static final String ANY="ANY";


    /**
     * 行为类型：getpicture
     */
    @SerializedName("action")
    private String action;
    /**
     * 开机图片类型
     *  LOAD: 机顶盒开机时首次展示的画面
     * AUTHEN: 开机画面，用户认证时展示的背景
     * 默认值：AUTHEN
     *
     */
    @SerializedName("picType")
    private String picType;
    /**
     * 设备类型，具体的终端型号。
     */
    @SerializedName("deviceModel")
    private String deviceModel;
    /**
     *扩展信息
     */

    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
