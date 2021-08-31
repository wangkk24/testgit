package com.pukka.ydepg.common.http.vss.request;

import com.google.gson.annotations.SerializedName;

public class QueryResultRequest {
    @SerializedName("messageID")
    String messageID;

    @SerializedName("addressCode")
    String addressCode;

    @SerializedName("interfaceName")
    String interfaceName;

    @SerializedName("headofGeneralBossQueryRequest")
    String headofGeneralBossQueryRequest;

    @SerializedName("bodyofGeneralBossQueryRequest")
    BodyofGeneralBossQueryRequest bodyofGeneralBossQueryRequest;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getHeadofGeneralBossQueryRequest() {
        return headofGeneralBossQueryRequest;
    }

    public void setHeadofGeneralBossQueryRequest(String headofGeneralBossQueryRequest) {
        this.headofGeneralBossQueryRequest = headofGeneralBossQueryRequest;
    }

    public BodyofGeneralBossQueryRequest getBodyofGeneralBossQueryRequest() {
        return bodyofGeneralBossQueryRequest;
    }

    public void setBodyofGeneralBossQueryRequest(BodyofGeneralBossQueryRequest bodyofGeneralBossQueryRequest) {
        this.bodyofGeneralBossQueryRequest = bodyofGeneralBossQueryRequest;
    }
}
