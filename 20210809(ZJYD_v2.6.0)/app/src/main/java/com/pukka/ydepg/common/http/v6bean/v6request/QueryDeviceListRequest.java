package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/27.
 */

public class QueryDeviceListRequest {

    /**
     * subscriberID :
     * profileID :
     * deviceType :
     * extensionFields :
     */

    /**
     * 订户ID。
     * 说明：
     * VSP平台支持通过subscriberID或profileID查询设备列表，如果接口请求中同时传入subscriberID和profileID参数，平台以subscriberID为准，查询对应的设备列表。
     */
    @SerializedName("subscriberID")
    private String subscriberID;
    /**
     * 帐户ID。首先获取Profile所属的Subscriber，再获取用户绑定的设备
     */
    @SerializedName("profileID")
    private String profileID;
    /**
     * 设备类型分组，多值使用分号分隔。
     * 取值范围：
     * 0：STB
     * 2：OTT设备（包括PC Plugin、iOS和Andriod device）
     * 3：Mobile(注意这里的Mobile是MTV解决方案的设备)
     * 4：all
     * 如果未携带这个参数则默认取登录终端的设备类型分组
     */
    @SerializedName("deviceType")
    private String deviceType;
    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
