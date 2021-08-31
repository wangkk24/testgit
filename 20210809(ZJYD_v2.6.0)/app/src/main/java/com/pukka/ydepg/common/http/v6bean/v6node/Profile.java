package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class Profile {

    /**
     * ID :
     * name :
     * profileType :
     * familyRole :
     * introduce :
     * password :
     * loginAccounts :
     * quota :
     * logoURL :
     * ratingID :
     * ratingName :
     * subjectIDs :
     * channelIDs :
     * VASIDs :
     * isShowMessage :
     * templateName :
     * lang :
     * mobilePhone :
     * isReceiveSMS :
     * isNeedSubscribePIN :
     * email :
     * isDisplayInfoBar :
     * channelListType :
     * isSendSMSForReminder :
     * reminderInterval :
     * leadTimeForSendReminder :
     * isDefaultProfile :
     * deviceID :
     * birthday :
     * nationality :
     * receiveADType :
     * profilePINEnable :
     * multiscreenEnable :
     * purchaseEnable :
     * loginName :
     * isOnline :
     * subscriberID :
     * location :
     * sign :
     * createTime :
     * isFilterLevel :
     * hasCollectUserPreference :
     * pushStatus :
     * customFields :
     * extensionFields :
     */

    /**
     * Profile对象的编号。
     * ModifyProfile接口中，此字段为必填
     */
    @SerializedName("ID")
    private String ID;

    /**
     * Profile名称
     */
    @SerializedName("name")
    private String name;

    /**
     * 成员类型。
     * 取值范围：0：家长1：成员
     */
    @SerializedName("profileType")
    private String profileType;

    /**
     * the range of field profileType
     */
    public interface ProfileType{
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
     * 用户角色，指用户在家庭中的角色。
     * 取值范围：
     * 1：Dad2：Mom3：Son4：Daughter5：Grandpa6：Grandma7：Maid8：Driver9：Other
     */
    @SerializedName("familyRole")
    private String familyRole;

    /**
     * Profile简介。
     */
    @SerializedName("introduce")
    private String introduce;

    /**
     * 对于AddProfile接口，携带明文密码
     */
    @SerializedName("password")
    private String password;

    /**
     * 登录帐号列表
     * 注：仅支持查询接口中有值。
     */
    @SerializedName("loginAccounts")
    private List<String> loginAccounts;

    /**
     * 消费额度，-1表示不限制，但是受订户信用额度的限制。
     * 说明：这里的信用额度表示帐户的信用额度，针对每个Profile每月消费额度的限制。
     */
    @SerializedName("quota")
    private String quota;

    /**
     * 头像地址，只需要携带文件名，如0.png。
     * 说明：VSP模板或者多屏本地内置几个图片给用户选择，服务器只是保存用户选择的本地的文件名，
     * 平台不支持终端从本地上传头像到服务器
     */
    @SerializedName("logoURL")
    private String logoURL;

    /**
     * 观看级别ID（rating），一个Profile只有一个级别。
     * 当Profile的父母控制级别高于或等于内容的父母控制级别时，可正常观看内容。
     * 当Profile的父母控制级别低于内容的父母控制级别时，则必须输入父母控制密码才能观看。
     * 说明：若为游客，则取系统配置的游客Profile的ratingID。
     */
    @SerializedName("ratingID")
    private String ratingID;

    /**
     * 观看级别名称。
     * 说明：仅在查询接口QueryProfile中有效返回。
     */
    @SerializedName("ratingName")
    private String ratingName;

    /**
     * 受限的栏目ID列表，此列表中的栏目，该Profile被限制访问。
     */
    @SerializedName("subjectIDs")
    private List<String> subjectIDs;

    /**
     * 受限的频道ID列表，此列表中的频道，该Profile被限制播放
     */
    @SerializedName("channelIDs")
    private List<String> channelIDs;

    /**
     * 受限的VAS ID列表，此列表中的VAS，该Profile被限制访问
     */
    @SerializedName("VASIDs")
    private List<String> VASIDs;

    /**
     * 是否展示TVMS消息。
     * 取值范围：0：不展示1：展示
     * 默认值为0。
     */
    @SerializedName("isShowMessage")
    private String isShowMessage;

    /**
     * VSP模板名称，局点如果提供多种风格的VSP模板，Profile可以根据喜好切换到对应的模板。
     */
    @SerializedName("templateName")
    private String templateName;

    /**
     * 用户语种缩写
     * 采用统一为ISO639-1缩写，如en。
     */
    @SerializedName("lang")
    private String lang;

    /**
     * 用户联系方式（手机号码）。
     */
    @SerializedName("mobilePhone")
    private String mobilePhone;

    /**
     * 是否接收短信通知。只有IPTV系统支持短信发送时才有意义。
     * 取值范围：0：不接收1：接收
     * 默认值为0。
     */
    @SerializedName("isReceiveSMS")
    private String isReceiveSMS;

    /**
     * 订购产品时是否需要输入订购密码。
     * 取值范围：0：不需要1：需要
     * 默认值为1。
     */
    @SerializedName("isNeedSubscribePIN")
    private String isNeedSubscribePIN;

    /**
     * 邮箱地址
     */
    @SerializedName("email")
    private String email;

    /**
     * 切换频道时客户端是否展示Info Bar。
     * 取值范围：0：不展示1：展示
     * 默认值为0。
     */
    @SerializedName("isDisplayInfoBar")
    private String isDisplayInfoBar;

    /**
     * 默认频道列表类型。
     * 取值范围：1：系统频道列表2：自定义频道列表
     * 默认值为1。
     */
    @SerializedName("channelListType")
    private String channelListType;

    /**
     * 是否支持短信提醒。只有IPTV系统支持短信发送时才有意义。
     * 取值范围：0：不支持1：支持
     * 默认值为0。
     */
    @SerializedName("isSendSMSForReminder")
    private String isSendSMSForReminder;

    /**
     * Reminder间隔。单位：分钟。默认值为5。
     */
    @SerializedName("reminderInterval")
    private String reminderInterval;

    /**
     * 提前发送Reminder的时间。单位：分钟。默认值为5。

     */
    @SerializedName("leadTimeForSendReminder")
    private String leadTimeForSendReminder;

    /**
     * 和deviceID配合使用。是否设置为deviceID对应的设备的默认Profile。在修改profile和查询profile的时候该参数有效。
     * 0：否1：是
     * 默认值为0。
     * 说明：在修改profile的时候，该属性为空 或 该属性为1且deviceID为空时，不处理设备的默认Profile。
     */
    @SerializedName("isDefaultProfile")
    private String isDefaultProfile;

    /**
     * Profile绑定的设备ID。
     * 说明：当isDefaultProfile=1时，该属性生效。表示设置该Profile是设备的Default Profile
     */
    @SerializedName("deviceID")
    private String deviceID;

    /**
     * 出生年月，格式为yyyyMMdd。
     */
    @SerializedName("birthday")
    private String birthday;

    /**
     * 用户国籍，类型定义参考ISO 3166-2，比如CN表示中国，UK表示英国。
     */
    @SerializedName("nationality")
    private String nationality;

    /**
     * 广告接收方式。
     * 取值范围：
     * 0：通过短信接收
     * 1：通过Email接收
     * 如果有多个值，使用英文逗号分隔，如果不携带，表示用户只在客户端上浏览广告
     */
    @SerializedName("receiveADType")
    private String receiveADType;

    /**
     * 登录时是否需要输入Profile密码。
     * 取值范围：
     * 0：不输入Profile密码
     * 1：输入Profile密码
     * 默认值为1
     */
    @SerializedName("profilePINEnable")
    private String profilePINEnable;

    /**
     * 是否允许多个终端同时登录。
     * 取值范围：
     * 0：允许
     * 1：不允许
     * 默认值为0。
     */
    @SerializedName("multiscreenEnable")
    private String multiscreenEnable;

    /**
     * 是否支持订购，取值范围：
     * 0：不支持。
     * 1：支持。
     * 默认值为1
     */
    @SerializedName("purchaseEnable")
    private String purchaseEnable;

    /**
     * 全局唯一的用户注册名，取值由终端用户定义。
     * 如果终端用户未定义，平台默认和Profile ID取值保持一致
     */
    @SerializedName("loginName")
    private String loginName;

    /**
     * Profile是否在线，
     * 取值范围：
     * 0：不在线
     * 1：在线
     * 说明：目前只有查询Profile接口QueryProfile的请求参数type为4时才支持返回该参数，
     * 即只有按ProfileId查询时才需要返回此参数。后续如果有其他查询类型也需要返回，再放开限制。
     */
    @SerializedName("isOnline")
    private String isOnline;

    /**
     * Profile对应的订户ID。
     * 在addProfile、modifyProfile接口入参中，此字段可空；在QueryProfile接口返回的查询结果中，此字段不可空。
     */
    @SerializedName("subscriberID")
    private String subscriberID;

    /**
     * 用户地址。
     */
    @SerializedName("location")
    private String location;

    /**
     * 个性化签名
     */
    @SerializedName("sign")
    private String sign;

    /**
     * Profile的创建时间，取值为距离1970年1月1号的毫秒数。仅查询接口QueryProfile支持。
     */
    @SerializedName("createTime")
    private String createTime;

    /**
     * 是否过滤掉高级别的内容，取值包括：
     * 0：不过滤
     * 1：过滤
     * 默认值为0
     */
    @SerializedName("isFilterLevel")
    private String isFilterLevel;

    /**
     * 是否反馈用户初始偏好，取值范围：
     * 0：未反馈
     * 1：反馈
     * 默认值为0。
     */
    @SerializedName("hasCollectUserPreference")
    private String hasCollectUserPreference;

    /**
     * 是否接收普通消息，取值包括：0：不接收 1：接收
     * 默认值为1。
     */
    @SerializedName("pushStatus")
    private String pushStatus;

    /**
     * 透传的扩展字段。
     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getFamilyRole() {
        return familyRole;
    }

    public void setFamilyRole(String familyRole) {
        this.familyRole = familyRole;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getLoginAccounts() {
        return loginAccounts;
    }

    public void setLoginAccounts(List<String> loginAccounts) {
        this.loginAccounts = loginAccounts;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public List<String> getSubjectIDs() {
        return subjectIDs;
    }

    public void setSubjectIDs(List<String> subjectIDs) {
        this.subjectIDs = subjectIDs;
    }

    public List<String> getChannelIDs() {
        return channelIDs;
    }

    public void setChannelIDs(List<String> channelIDs) {
        this.channelIDs = channelIDs;
    }

    public List<String> getVASIDs() {
        return VASIDs;
    }

    public void setVASIDs(List<String> VASIDs) {
        this.VASIDs = VASIDs;
    }

    public String getIsShowMessage() {
        return isShowMessage;
    }

    public void setIsShowMessage(String isShowMessage) {
        this.isShowMessage = isShowMessage;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getIsReceiveSMS() {
        return isReceiveSMS;
    }

    public void setIsReceiveSMS(String isReceiveSMS) {
        this.isReceiveSMS = isReceiveSMS;
    }

    public String getIsNeedSubscribePIN() {
        return isNeedSubscribePIN;
    }

    public void setIsNeedSubscribePIN(String isNeedSubscribePIN) {
        this.isNeedSubscribePIN = isNeedSubscribePIN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsDisplayInfoBar() {
        return isDisplayInfoBar;
    }

    public void setIsDisplayInfoBar(String isDisplayInfoBar) {
        this.isDisplayInfoBar = isDisplayInfoBar;
    }

    public String getChannelListType() {
        return channelListType;
    }

    public void setChannelListType(String channelListType) {
        this.channelListType = channelListType;
    }

    public String getIsSendSMSForReminder() {
        return isSendSMSForReminder;
    }

    public void setIsSendSMSForReminder(String isSendSMSForReminder) {
        this.isSendSMSForReminder = isSendSMSForReminder;
    }

    public String getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(String reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public String getLeadTimeForSendReminder() {
        return leadTimeForSendReminder;
    }

    public void setLeadTimeForSendReminder(String leadTimeForSendReminder) {
        this.leadTimeForSendReminder = leadTimeForSendReminder;
    }

    public String getIsDefaultProfile() {
        return isDefaultProfile;
    }

    public void setIsDefaultProfile(String isDefaultProfile) {
        this.isDefaultProfile = isDefaultProfile;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReceiveADType() {
        return receiveADType;
    }

    public void setReceiveADType(String receiveADType) {
        this.receiveADType = receiveADType;
    }

    public String getProfilePINEnable() {
        return profilePINEnable;
    }

    public void setProfilePINEnable(String profilePINEnable) {
        this.profilePINEnable = profilePINEnable;
    }

    public String getMultiscreenEnable() {
        return multiscreenEnable;
    }

    public void setMultiscreenEnable(String multiscreenEnable) {
        this.multiscreenEnable = multiscreenEnable;
    }

    public String getPurchaseEnable() {
        return purchaseEnable;
    }

    public void setPurchaseEnable(String purchaseEnable) {
        this.purchaseEnable = purchaseEnable;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsFilterLevel() {
        return isFilterLevel;
    }

    public void setIsFilterLevel(String isFilterLevel) {
        this.isFilterLevel = isFilterLevel;
    }

    public String getHasCollectUserPreference() {
        return hasCollectUserPreference;
    }

    public void setHasCollectUserPreference(String hasCollectUserPreference) {
        this.hasCollectUserPreference = hasCollectUserPreference;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
