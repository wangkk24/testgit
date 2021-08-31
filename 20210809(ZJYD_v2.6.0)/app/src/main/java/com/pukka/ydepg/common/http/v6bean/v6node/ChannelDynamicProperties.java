package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/11/6.
 */

public class ChannelDynamicProperties {
    @SerializedName("ID")
    private String ID;

    @SerializedName("channelNO")
    private int channelNO;

    @SerializedName("favorite")
    private Favorite favorite;

    @SerializedName("isLocked")
    private int isLocked;

    @SerializedName("physicalChannelsDynamicProperties")
    private List<PhysicalChannelDynamicProperties> physicalChannelsDynamicProperties;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getChannelNO() {
        return channelNO;
    }

    public void setChannelNO(int channelNO) {
        this.channelNO = channelNO;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }

    public List<PhysicalChannelDynamicProperties> getPhysicalChannelsDynamicProperties() {
        return physicalChannelsDynamicProperties;
    }

    public void setPhysicalChannelsDynamicProperties(List<PhysicalChannelDynamicProperties> physicalChannelsDynamicProperties) {
        this.physicalChannelsDynamicProperties = physicalChannelsDynamicProperties;
    }
}
