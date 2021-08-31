package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the Metadata of Profile
 */
public class Profile extends ExtensionField
{

    private static final String TAG = Profile.class.getSimpleName();
    /**
     * field:id
     */
     @SerializedName("ID")
    private String id;

    /**
     * field:name
     */
     @SerializedName("name")
    private String name;

    /**
     * field:profileType
     */
     @SerializedName("profileType")
    private String profileType;

    /**
     * the range of field profileType
     */
    public interface ProfileType
    {
        /**
         * Parent profile type
         */
        String ADMIN = "0";

        /**
         * remeber profile type
         */
        String GENERAL = "1";
    }

    /**
     * field:familyRole
     */
     @SerializedName("familyRole")
    private String familyRole;

    /**
     * the range of field familyRole
     */
    public interface FamilyRole
    {
        /**
         * 1:Dad
         */
        String DAD = "1";
        /**
         * 2:Mom
         */
        String MOM = "2";
        /**
         * 3:Son
         */
        String SON = "3";
        /**
         * 4:Daughter
         */
        String DAUGHTER = "4";
        /**
         * 5:Grandpa
         */
        String GRANDPA = "5";
        /**
         * 6:Grandma
         */
        String GRANDMA = "6";
        /**
         * 7:Maid
         */
        String MAID = "7";
        /**
         * 8:Driver
         */
        String DRIVER = "8";
        /**
         * 9:Other
         */
        String OTHER = "9";

    }

    /**
     * field:introduce
     */
     @SerializedName("introduce")
    private String introduce;

    /**
     * field:password
     */
     @SerializedName("password")
    private String password;

    /**
     * field:quota
     */
     @SerializedName("quota")
    private String quota;

    /**
     * field:logoURL
     */
     @SerializedName("logoURL")
    private String logoURL;

    /**
     * field:ratingID
     */
     @SerializedName("ratingID")
    private String ratingId;

    /**
     * field:ratingName
     */
     @SerializedName("ratingName")
    private String ratingName;

    /**
     * field:subjectIDs
     */
     @SerializedName("subjectIDs")
    private List<String> subjectIdList;

    /**
     * field:channelIDs
     */
     @SerializedName("channelIDs")
    private List<String> channelIdList;

    /**
     * field:channelIDs
     */
     @SerializedName("VASIDs")
    private List<String> vasIdList;

    /**
     * field:isShowMessage
     */
     @SerializedName("isShowMessage")
    private String isShowMessage;

    /**
     * the range of field isShowMessage
     */
    public interface IsShowMessage
    {
        /**
         * not show
         */
        String UN_SHOW = "0";

        /**
         * show
         */
        String SHOW = "1";
    }

    /**
     * field:templateName
     */
     @SerializedName("templateName")
    private String templateName;

    /**
     * field:lang
     */
     @SerializedName("lang")
    private String lang;

    /**
     * field:mobilePhone
     */
     @SerializedName("mobilePhone")
    private String mobilePhone;

    /**
     * field:isReceiveSMS
     * the default value is IsReceiveSMS.UN_RECEIVE
     */
     @SerializedName("isReceiveSMS")
    private String isReceiveSMS;

    /**
     * the range of field isReceiveSMS
     */
    public interface IsReceiveSMS
    {
        /**
         * receive message
         */
        String RECEIVE = "1";

        /**
         * do not receive message
         */
        String UN_RECEIVE = "0";
    }

    /**
     * field:isNeedSubscribePIN
     * the default value is IsNeedSubscribePIN.NEED
     */
     @SerializedName("isNeedSubscribePIN")
    private String isNeedSubscribePIN;

    /**
     * the range of field isNeedSubscribePIN
     */
    public interface IsNeedSubscribePIN
    {
        /**
         * need PIN
         */
        String NEED = "1";

        /**
         * not need PIN
         */
        String NEEDLESS = "0";
    }

    /**
     * field:email
     */
     @SerializedName("email")
    private String email;

    /**
     * field:isDisplayInfoBar
     * the default value is IsDisplayInfoBar.UN_SHOW
     */
     @SerializedName("isDisplayInfoBar")
    private String isDisplayInfoBar;

    /**
     * the range of field familyRole
     */
    public interface IsDisplayInfoBar
    {
        /**
         * not show
         */
        String UN_SHOW = "0";

        /**
         * show
         */
        String SHOW = "1";
    }

    /**
     * field:channelListType
     * the default value is ChannelListType.SYSTEM_CHANNEL
     */
     @SerializedName("channelListType")
    private String channelListType;

    /**
     * the range of field familyRole
     */
    public interface ChannelListType
    {
        /**
         * not show
         */
        String SYSTEM_CHANNEL = "1";

        /**
         * show
         */
        String CUSTOMIZE_CHANNEL = "2";
    }

    /**
     * field:isSendSMSForReminder
     * the default value is IsSendSMSForReminder.UN_SUPPORT
     */
     @SerializedName("isSendSMSForReminder")
    private String isSendSMSForReminder;

    /**
     * the range of field familyRole
     */
    public interface IsSendSMSForReminder
    {
        /**
         * not show
         */
        String SUPPORT = "1";

        /**
         * show
         */
        String UN_SUPPORT = "0";
    }

    /**
     * field:reminderInterval
     * the default value is 5 min
     */
     @SerializedName("reminderInterval")
    private String reminderInterval;

    /**
     * field:leadTimeForSendReminder
     * the default value is 5 min
     */
     @SerializedName("leadTimeForSendReminder")
    private String leadTimeForSendReminder;

    /**
     * field:deviceId
     */
     @SerializedName("deviceID")
    private String deviceId;
    /**
     * field:birthday
     * the format is yyyyMMdd
     */
     @SerializedName("birthday")
    private String birthday;

    /**
     * field:nationality
     */
     @SerializedName("nationality")
    private String nationality;

    /**
     * field:receiveAdvertisementType
     */
     @SerializedName("receiveADType")
    private String receiveAdvertisementType;

    /**
     * the range of field familyRole
     */
    public interface ReceiveAdvertisementType
    {
        /**
         * sms
         */
        String BY_SMS = "0";

        /**
         * email
         */
        String BY_EMAIL = "1";
    }

    /**
     * field:profilePINEnable
     */
     @SerializedName("profilePINEnable")
    private String profilePINEnable;

    /**
     * the range of field familyRole
     */
    public interface ProfilePINEnable
    {
        /**
         * no need input pin
         */
        String NEEDLESS_INPUT = "0";

        /**
         * need input pin
         */
        String NEED_INPUT = "1";
    }

    /**
     * field:multiScreenEnable
     */
     @SerializedName("multiscreenEnable")
    private String multiScreenEnable;

    /**
     * the range of field familyRole
     */
    public interface MultiScreenEnable
    {
        /**
         * allow
         */
        String ALLOW = "0";

        /**
         * not allow
         */
        String NOT_ALLOW = "1";
    }

    /**
     * field:purchaseEnable
     * the default is purchaseEnable.SUPPORT
     */
     @SerializedName("purchaseEnable")
    private String purchaseEnable;

    /**
     * the range of field familyRole
     */
    public interface PurchaseEnable
    {
        /**
         * allow
         */
        String UN_SUPPORT = "0";

        /**
         * not allow
         */
        String SUPPORT = "1";
    }

    /**
     * field:loginName
     */
     @SerializedName("loginName")
    private String loginName;

    /**
     * field:isOnline
     */
     @SerializedName("isOnline")
    private String isOnline;

    /**
     * the range of field isOnline
     */
    public interface IsOnline
    {
        /**
         * offline
         */
        String OFFLINE = "0";

        /**
         * online
         */
        String ONLINE = "1";
    }

    /**
     * field:subscriberId
     */
     @SerializedName("subscriberID")
    private String subscriberId;

    /**
     * field:location
     */
     @SerializedName("location")
    private String location;

    /**
     * field:sign
     */
     @SerializedName("sign")
    private String sign;

    /**
     * field:createTime
     * unit:ms
     */
     @SerializedName("createTime")
    private String createTime;

    /**
     * field:isFilterLevel
     * the default value is IsFilterLevel.UN_FILTER
     */
     @SerializedName("isFilterLevel")
    private String isFilterLevel;

    /**
     * the range of field isFilterLevel
     */
    public interface IsFilterLevel
    {
        /**
         * offline
         */
        String UN_FILTER = "0";

        /**
         * online
         */
        String FILTER = "1";
    }

    /**
     * field:isDefaultProfile
     */
     @SerializedName("isDefaultProfile")
    private String isDefaultProfile;

    public interface IsDefaultProfile
    {
        /**
         * offline
         */
        String NO = "0";

        /**
         * online
         */
        String YES = "1";
    }

    /**
     * field:hasCollectUserPreference
     */
     @SerializedName("hasCollectUserPreference")
    private String hasCollectUserPreference;

    /**
     * the range of field hasCollectUserPreference
     */
    public interface HasCollectUserPreference
    {
        /**
         * not feedback
         */
        String NOT_FEEDBACK = "0";

        /**
         * Has feedback
         */
        String FEEDBACK = "1";
    }

    /**
     * field:pushStatus
     */
     @SerializedName("pushStatus")
    private String pushStatus;

    /**
     * the range of field pushStatus
     */
    public interface PushStatus
    {
        /**
         * not receive
         */
        String NOT_RECEIVE = "0";

        /**
         * receive
         */
        String RECEIVE = "1";
    }

    /**
     * field:extensionFieldList
     */
     @SerializedName("customFields")
    private List<NamedParameter> customFieldList;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProfileType()
    {
        return profileType;
    }

    public void setProfileType(String profileType)
    {
        this.profileType = profileType;
    }

    public String getFamilyRole()
    {
        return familyRole;
    }

    public void setFamilyRole(String familyRole)
    {
        this.familyRole = familyRole;
    }

    public String getIntroduce()
    {
        return introduce;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getQuota()
    {
        return quota;
    }

    public void setQuota(String quota)
    {
        this.quota = quota;
    }

    public String getLogoURL()
    {
        return logoURL;
    }

    public void setLogoURL(String logoURL)
    {
        this.logoURL = logoURL;
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

    public List<String> getSubjectIdList()
    {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList)
    {
        this.subjectIdList = subjectIdList;
    }

    public List<String> getChannelIdList()
    {
        return channelIdList;
    }

    public void setChannelIdList(List<String> channelIdList)
    {
        this.channelIdList = channelIdList;
    }

    public List<String> getVASIdList()
    {
        return vasIdList;
    }

    public void setVASIdList(List<String> vasIdList)
    {
        this.vasIdList = vasIdList;
    }

    public String getIsShowMessage()
    {
        return isShowMessage;
    }

    public void setIsShowMessage(String isShowMessage)
    {
        this.isShowMessage = isShowMessage;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone)
    {
        this.mobilePhone = mobilePhone;
    }

    public String getIsReceiveSMS()
    {
        return isReceiveSMS;
    }

    public void setIsReceiveSMS(String isReceiveSMS)
    {
        this.isReceiveSMS = isReceiveSMS;
    }

    public String getIsNeedSubscribePIN()
    {
        return isNeedSubscribePIN;
    }

    public void setIsNeedSubscribePIN(String isNeedSubscribePIN)
    {
        this.isNeedSubscribePIN = isNeedSubscribePIN;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getIsDisplayInfoBar()
    {
        return isDisplayInfoBar;
    }

    public void setIsDisplayInfoBar(String isDisplayInfoBar)
    {
        this.isDisplayInfoBar = isDisplayInfoBar;
    }

    public String getChannelListType()
    {
        return channelListType;
    }

    public void setChannelListType(String channelListType)
    {
        this.channelListType = channelListType;
    }

    public String getIsSendSMSForReminder()
    {
        return isSendSMSForReminder;
    }

    public void setIsSendSMSForReminder(String isSendSMSForReminder)
    {
        this.isSendSMSForReminder = isSendSMSForReminder;
    }

    public String getReminderInterval()
    {
        return reminderInterval;
    }

    public void setReminderInterval(String reminderInterval)
    {
        this.reminderInterval = reminderInterval;
    }

    public String getLeadTimeForSendReminder()
    {
        return leadTimeForSendReminder;
    }

    public void setLeadTimeForSendReminder(String leadTimeForSendReminder)
    {
        this.leadTimeForSendReminder = leadTimeForSendReminder;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getNationality()
    {
        return nationality;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public String getReceiveAdvertisementType()
    {
        return receiveAdvertisementType;
    }

    public void setReceiveAdvertisementType(String receiveAdvertisementType)
    {
        this.receiveAdvertisementType = receiveAdvertisementType;
    }

    public String getProfilePINEnable()
    {
        return profilePINEnable;
    }

    public void setProfilePINEnable(String profilePINEnable)
    {
        this.profilePINEnable = profilePINEnable;
    }

    public String getMultiScreenEnable()
    {
        return multiScreenEnable;
    }

    public void setMultiScreenEnable(String multiScreenEnable)
    {
        this.multiScreenEnable = multiScreenEnable;
    }

    public String getPurchaseEnable()
    {
        return purchaseEnable;
    }

    public void setPurchaseEnable(String purchaseEnable)
    {
        this.purchaseEnable = purchaseEnable;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getIsOnline()
    {
        return isOnline;
    }

    public void setIsOnline(String isOnline)
    {
        this.isOnline = isOnline;
    }

    public String getSubscriberId()
    {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId)
    {
        this.subscriberId = subscriberId;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getIsFilterLevel()
    {
        return isFilterLevel;
    }

    public void setIsFilterLevel(String isFilterLevel)
    {
        this.isFilterLevel = isFilterLevel;
    }

    public List<String> getVasIdList()
    {
        return vasIdList;
    }

    public void setVasIdList(List<String> vasIdList)
    {
        this.vasIdList = vasIdList;
    }

    public String getIsDefaultProfile()
    {
        return isDefaultProfile;
    }

    public void setIsDefaultProfile(String isDefaultProfile)
    {
        this.isDefaultProfile = isDefaultProfile;
    }

    public String getHasCollectUserPreference()
    {
        return hasCollectUserPreference;
    }

    public void setHasCollectUserPreference(String hasCollectUserPreference)
    {
        this.hasCollectUserPreference = hasCollectUserPreference;
    }

    public String getPushStatus()
    {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus)
    {
        this.pushStatus = pushStatus;
    }

    public List<NamedParameter> getCustomFieldList()
    {
        return customFieldList;
    }

    public void setCustomFieldList(List<NamedParameter> customFieldList)
    {
        this.customFieldList = customFieldList;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Profile)
        {
            Profile profile = (Profile) o;
            return this.getId().equals(profile.getId());
        }
        return super.equals(o);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public String toString()
    {
        return "Profile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", profileType='" + profileType + '\'' +
                ", familyRole='" + familyRole + '\'' +
                ", introduce='" + introduce + '\'' +
                ", password='" + password + '\'' +
                ", quota='" + quota + '\'' +
                ", logoURL='" + logoURL + '\'' +
                ", ratingId='" + ratingId + '\'' +
                ", ratingName='" + ratingName + '\'' +
                ", subjectIdList=" + subjectIdList +
                ", channelIdList=" + channelIdList +
                ", vasIdList=" + vasIdList +
                ", isShowMessage='" + isShowMessage + '\'' +
                ", templateName='" + templateName + '\'' +
                ", lang='" + lang + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", isReceiveSMS='" + isReceiveSMS + '\'' +
                ", isNeedSubscribePIN='" + isNeedSubscribePIN + '\'' +
                ", email='" + email + '\'' +
                ", isDisplayInfoBar='" + isDisplayInfoBar + '\'' +
                ", channelListType='" + channelListType + '\'' +
                ", isSendSMSForReminder='" + isSendSMSForReminder + '\'' +
                ", reminderInterval='" + reminderInterval + '\'' +
                ", leadTimeForSendReminder='" + leadTimeForSendReminder + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nationality='" + nationality + '\'' +
                ", receiveAdvertisementType='" + receiveAdvertisementType + '\'' +
                ", profilePINEnable='" + profilePINEnable + '\'' +
                ", multiScreenEnable='" + multiScreenEnable + '\'' +
                ", purchaseEnable='" + purchaseEnable + '\'' +
                ", loginName='" + loginName + '\'' +
                ", isOnline='" + isOnline + '\'' +
                ", subscriberId='" + subscriberId + '\'' +
                ", location='" + location + '\'' +
                ", sign='" + sign + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isFilterLevel='" + isFilterLevel + '\'' +
                ", isDefaultProfile='" + isDefaultProfile + '\'' +
                ", hasCollectUserPreference='" + hasCollectUserPreference + '\'' +
                ", pushStatus='" + pushStatus + '\'' +
                ", customFieldList=" + customFieldList +
                '}';
    }
}