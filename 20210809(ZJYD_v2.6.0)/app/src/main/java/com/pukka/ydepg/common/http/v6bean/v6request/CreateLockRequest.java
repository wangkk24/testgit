package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.Lock;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/11/7.
 */

public class CreateLockRequest {
    @SerializedName("locks")
    List<Lock> locks;
    @SerializedName("isShare")
    String isShare;
    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
