package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.VRSSubscription;

import java.util.List;

public class VRSQuerySubscriptionResponse {
    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    @SerializedName("subscriptions")
    private List<VRSSubscription> subscriptions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VRSSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<VRSSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
