package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class UserLoginHistoryInfo {

    /**
     * latestSuccessItem :
     * curValidLoginItem :
     */

    /**
     *最后一次认证成功信息
     */
    @SerializedName("latestSuccessItem")
    private UserLoginHistoryInfo latestSuccessItem;

    /**
     * 本次登录时，如果检测到有该用户在另外一个设备上登录，则填充其登录时间（格式YYYY-MM-DD）和登录客户端的IP，其余信息无须填充。

     采用数组主要是因为，可能同时有多个登录信息存在

     */
    @SerializedName("curValidLoginItem")
    private List<UserLoginHistoryInfo> curValidLoginItem;

    public UserLoginHistoryInfo getLatestSuccessItem() {
        return latestSuccessItem;
    }

    public void setLatestSuccessItem(UserLoginHistoryInfo latestSuccessItem) {
        this.latestSuccessItem = latestSuccessItem;
    }

    public List<UserLoginHistoryInfo> getCurValidLoginItem() {
        return curValidLoginItem;
    }

    public void setCurValidLoginItem(List<UserLoginHistoryInfo> curValidLoginItem) {
        this.curValidLoginItem = curValidLoginItem;
    }
}
