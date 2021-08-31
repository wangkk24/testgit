package com.pukka.ydepg.common.http.bean.node;


import com.google.gson.annotations.SerializedName;




public class Profile
{

    public static final int SUPER_PROFILE = 0;

    @SerializedName("userId")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("quota")
    private String quota;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("ratingID")
    private String ratingId;

    @SerializedName("ratingName")
    private String ratingName;

    @SerializedName("profileType")
    private int profileType;

    @SerializedName("lang")
    private int lang;

    @SerializedName("isDefault")
    private String isDefault;

    @SerializedName("loginName")
    private String loginName;

    public String isDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public boolean isSuper()
    {
        return profileType == SUPER_PROFILE;
    }

    public int getLang()
    {
        return lang;
    }

    public void setLang(int lang)
    {
        this.lang = lang;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String ID)
    {
        this.userId = ID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQuota()
    {
        return quota;
    }

    public void setQuota(String quota)
    {
        this.quota = quota;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getRatingId()
    {
        return ratingId;
    }

    public void setRatingId(String ratingId)
    {
        this.ratingId = ratingId;
    }

    public String getRatingName()
    {
        return ratingName;
    }

    public void setRatingName(String ratingName)
    {
        this.ratingName = ratingName;
    }

    public int getProfileType()
    {
        return profileType;
    }

    public void setProfileType(int profileType)
    {
        this.profileType = profileType;
    }

}
