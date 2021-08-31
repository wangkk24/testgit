package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class UserLoginHistoryItem {

    /**
     * subscriberSN :
     * profileSN :
     * logindate :
     * clientIP :
     */

    /**
     * 订户键值
     */
    @SerializedName("subscriberSN")
    private String subscriberSN;

    /**
     * Profile键值
     */
    @SerializedName("profileSN")
    private String profileSN;

    /**
     * 登录日期，取值为距离1970年1月1号的毫秒数
     */
    @SerializedName("logindate")
    private String logindate;

    /**
     * 终端IP
     */
    @SerializedName("clientIP")
    private String clientIP;

    public String getSubscriberSN() {
        return subscriberSN;
    }

    public void setSubscriberSN(String subscriberSN) {
        this.subscriberSN = subscriberSN;
    }

    public String getProfileSN() {
        return profileSN;
    }

    public void setProfileSN(String profileSN) {
        this.profileSN = profileSN;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
}
