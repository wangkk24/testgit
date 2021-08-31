package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hasee on 2017/11/8.
 */

public class Lock implements Serializable {
    @SerializedName("profileID")
    String profileID;
    @SerializedName("lockType")
    String lockType;
    @SerializedName("itemID")
    String itemID;
    @SerializedName("extensionFields")
    List<NamedParameter> extensionFields;

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
    public interface LockType{
        String CHANNEL = "CHANNEL";
        String SUBJECT = "SUBJECT";
        String VOD = "VOD";
        String PROGRAM = "PROGRAM";
        String SERIES = "SERIES";
        String GENRE = "GENRE";
    }
}
