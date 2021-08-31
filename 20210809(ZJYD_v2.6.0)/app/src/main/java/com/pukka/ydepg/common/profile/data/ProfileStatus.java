package com.pukka.ydepg.common.profile.data;

public class ProfileStatus {

    private String result;

    //profileType 0:Main主账号，1:Sub子账号
    private String profileType = "0";

    private String profileId   = "main";

    private String profileName = "管理员";

    //profile头像
    private String icon;

    public ProfileStatus(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}