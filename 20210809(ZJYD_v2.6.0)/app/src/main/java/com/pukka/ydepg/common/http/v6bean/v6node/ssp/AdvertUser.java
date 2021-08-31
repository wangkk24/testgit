package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertUser {

    @SerializedName("userIds")
    private List<UserIdentification> users;

    public List<UserIdentification> getUsers()
    {
        return users;
    }

    public void setUsers(List<UserIdentification> users)
    {
        this.users = users;
    }
}