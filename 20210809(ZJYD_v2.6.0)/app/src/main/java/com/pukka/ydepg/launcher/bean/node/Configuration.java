package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the metadata of Configuration
 */
public class Configuration extends Metadata {
    /**
     * field :configType
     */
    @SerializedName("cfgType")
    private String configType;

    /**
     * the range of field configType
     */
    public interface ConfigType {
        /**
         * the configuration of terminal
         */
        String TERMINAL_CONFIGURATION = "0";

        /**
         * the configuration of template
         */
        String TEMPLATE_CONFIGURATION = "1";

        /**
         * the configuration of vsp server
         */
        String VSP_SERVER_CONFIGURATION = "2";

        /**
         * the configuration of the third server
         */
        String THE_THIRD_CONFIGURATION = "3";
    }

    /**
     * the extends info
     */
    @SerializedName("values")
    List<NamedParameter> extensionFieldList;

    /**
     * STB log server url
     */
    @SerializedName("STBLogServerURL")
    private String stbLogServerURL;

    /**
     * STB log upload interval (s)
     */
    @SerializedName("STBLogUploadInterval")
    private String stbLogUploadInterval;

    /**
     * VOD play heartbit interval (s)
     */
    @SerializedName("playHeartBitInterval")
    private String playHeartBitInterval;

    /**
     * Netrix equipment infomation upload url
     */
    @SerializedName("netrixPushServerURL")
    private String netrixPushServerURL;

    /**
     * Skipping time block (s)
     */
    @SerializedName("skippingTimeBlock")
    private String skippingTimeBlock;

    /**
     * VOD favorite limit
     */
    @SerializedName("VODFavLimit")
    private String vodFavouriteLimit;

    /**
     * Channel favorite limit
     */
    @SerializedName("channelFavLimit")
    private String channelFavouriteLimit;

    /**
     * VAS favorite limit
     */
    @SerializedName("VASFavLimit")
    private String vasFavouriteLimit;

    /**
     * Downloaded buffer length (s), default value: 30s
     */
    @SerializedName("downloadedBufferLength")
    private String downloadedBufferLength;

    /**
     * Social url
     */
    @SerializedName("SocialURL")
    private String socialURL;

    /**
     * Favorite limit
     */
    @SerializedName("favouriteLimit")
    private String favouriteLimit;

    /**
     * Bookmark limit
     */
    @SerializedName("bookmarkLimit")
    private String bookmarkLimit;

    /**
     * Lock limit
     */
    @SerializedName("lockLimit")
    private String lockLimit;

    /**
     * Profile limit
     */
    @SerializedName("profileLimit")
    private String profileLimit;

    /**
     * SQM url
     */
    @SerializedName("SQMURL")
    private String sqmURL;

    @SerializedName("UserPwdMinLength")
    private String userPasswordMinLength;

    @SerializedName("UserPwdUpperCaseLetters")
    private String userPasswordUpperCaseLetters;

    @SerializedName("UserPwdLowerCaseLetters")
    private String userPasswordLowerCaseLetters;

    @SerializedName("UserPwdNumbers")
    private String userPasswordNumbers;

    @SerializedName("UserPwdOthersLetters")
    private String userPasswordOthersLetters;

    @SerializedName("UserPwdSupportSpace")
    private String userPasswordSupportSpace;

    @SerializedName("ProfileNameMinLength")
    private String profileNameMinLength;

    @SerializedName("ProfileNameMaxLength")
    private String profileNameMaxLength;

    @SerializedName("ProfileNameUpperCaseLetters")
    private String profileNameUpperCaseLetters;

    @SerializedName("ProfileNameLowerCaseLetters")
    private String profileNameLowerCaseLetters;

    @SerializedName("ProfileNameNumbers")
    private String profileNameNumbers;

    @SerializedName("ProfileNameOthersLetters")
    private String profileNameOthersLetters;

    @SerializedName("ProfileNameSupportSpace")
    private String profileNameSupportSpace;

    @SerializedName("UserIDMinLength")
    private String userIDMinLength;

    @SerializedName("UserIDMaxLength")
    private String userIDMaxLength;

    @SerializedName("UserIDUpperCaseLetters")
    private String userIDUpperCaseLetters;

    @SerializedName("UserIDLowerCaseLetters")
    private String userIDLowerCaseLetters;

    @SerializedName("UserIDNumbers")
    private String userIDNumbers;

    @SerializedName("UserIDOthersLetters")
    private String userIDOthersLetters;

    @SerializedName("UserIDSupportSpace")
    private String userIDSupportSpace;

    @SerializedName("DeviceNameMinLength")
    private String deviceNameMinLength;

    @SerializedName("DeviceNameMaxLength")
    private String deviceNameMaxLength;

    @SerializedName("DeviceNameUpperCaseLetters")
    private String deviceNameUpperCaseLetters;

    @SerializedName("DeviceNameLowerCaseLetters")
    private String deviceNameLowerCaseLetters;

    @SerializedName("DeviceNameNumbers")
    private String deviceNameNumbers;

    @SerializedName("DeviceNameOthersLetters")
    private String deviceNameOthersLetters;

    @SerializedName("DeviceNameSupportSpace")
    private String deviceNameSupportSpace;

    @SerializedName("SearchKeyMinLength")
    private String searchKeyMinLength;

    @SerializedName("SearchKeyMaxLength")
    private String searchKeyMaxLength;

    @SerializedName("SearchKeyUpperCaseLetters")
    private String searchKeyUpperCaseLetters;

    @SerializedName("SearchKeyLowerCaseLetters")
    private String searchKeyLowerCaseLetters;

    @SerializedName("SearchKeyNumbers")
    private String searchKeyNumbers;

    @SerializedName("SearchKeyOthersLetters")
    private String searchKeyOthersLetters;

    @SerializedName("SearchKeySupportSpace")
    private String searchKeySupportSpace;

    @SerializedName("BeginOffset")
    private String pvrBeginOffset;

    @SerializedName("EndOffset")
    private String pvrEndOffset;

    @SerializedName("NPVRSpaceStrategy")
    private String npvrSpaceStrategy;

    @SerializedName("OTTIMPURLforIPv6")
    private String ottIMPURLForIPv6;

    @SerializedName("OTTIMPURL")
    private String ottIMPURL;

    @SerializedName("OTTIMPPort")
    private String ottIMPPort;

    @SerializedName("OTTIMPIPforIPv6")
    private String ottIMPIPForIPv6;

    @SerializedName("OTTIMPIP")
    private String ottIMPIP;

    @SerializedName("IPTVIMPURLforIPv6")
    private String iptvIMPURLForIPv6;

    @SerializedName("IPTVIMPURL")
    private String iptvIMPURL;

    @SerializedName("IPTVIMPPort")
    private String iptvIMPPort;

    @SerializedName("IPTVIMPIPforIPv6")
    private String iptvIMPIPForIPv6;

    @SerializedName("IPTVIMPIP")
    private String iptvIMPIP;

    @SerializedName("IMDomain")
    private String imDomain;

    @SerializedName("ImpAuthType")
    private String impAuthType;

    @SerializedName("Default_RecCfgDeleteMode")
    private String pvrDefaultRecCfgDeleteMode;

    @SerializedName("Default_RecCfgSingleOrSeries")
    private String pvrDefaultRecCfgSingleOrSeries;

    @SerializedName("PlaybillLen")
    private String playbillLen;

    @SerializedName("RecPlaybillLen")
    private String recPlaybillLen;

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    public List<NamedParameter> getExtensionFieldList()
    {
        return extensionFieldList;
    }

    public void setExtensionFieldList(List<NamedParameter> extensionFieldList) {
        this.extensionFieldList = extensionFieldList;
    }

    public String getStbLogServerURL()
    {
        return stbLogServerURL;
    }

    public void setStbLogServerURL(String stbLogServerURL) {
        this.stbLogServerURL = stbLogServerURL;
    }

    public String getStbLogUploadInterval()
    {
        return stbLogUploadInterval;
    }

    public void setStbLogUploadInterval(String stbLogUploadInterval) {
        this.stbLogUploadInterval = stbLogUploadInterval;
    }

    public String getPlayHeartBitInterval()
    {
        return playHeartBitInterval;
    }

    public void setPlayHeartBitInterval(String playHeartBitInterval) {
        this.playHeartBitInterval = playHeartBitInterval;
    }

    public String getNetrixPushServerURL()
    {
        return netrixPushServerURL;
    }

    public void setNetrixPushServerURL(String netrixPushServerURL) {
        this.netrixPushServerURL = netrixPushServerURL;
    }

    public String getSkippingTimeBlock()
    {
        return skippingTimeBlock;
    }

    public void setSkippingTimeBlock(String skippingTimeBlock) {
        this.skippingTimeBlock = skippingTimeBlock;
    }

    public String getDownloadedBufferLength()
    {
        return downloadedBufferLength;
    }

    public void setDownloadedBufferLength(String downloadedBufferLength) {
        this.downloadedBufferLength = downloadedBufferLength;
    }

    public String getSocialURL()
    {
        return socialURL;
    }

    public void setSocialURL(String socialURL)
    {
        this.socialURL = socialURL;
    }

    public String getFavouriteLimit()
    {
        return favouriteLimit;
    }

    public void setFavouriteLimit(String favouriteLimit)
    {
        this.favouriteLimit = favouriteLimit;
    }

    public String getBookmarkLimit()
    {
        return bookmarkLimit;
    }

    public void setBookmarkLimit(String bookmarkLimit)
    {
        this.bookmarkLimit = bookmarkLimit;
    }

    public String getLockLimit()
    {
        return lockLimit;
    }

    public void setLockLimit(String lockLimit)
    {
        this.lockLimit = lockLimit;
    }

    public String getProfileLimit()
    {
        return profileLimit;
    }

    public void setProfileLimit(String profileLimit)
    {
        this.profileLimit = profileLimit;
    }

    public String getSQMURL()
    {
        return sqmURL;
    }

    public void setSQMURL(String sqmURL)
    {
        this.sqmURL = sqmURL;
    }

    public String getVODFavouriteLimit()
    {
        return vodFavouriteLimit;
    }

    public void setVODFavouriteLimit(String vodFavouriteLimit) {
        this.vodFavouriteLimit = vodFavouriteLimit;
    }

    public String getChannelFavouriteLimit()
    {
        return channelFavouriteLimit;
    }

    public void setChannelFavouriteLimit(String channelFavouriteLimit) {
        this.channelFavouriteLimit = channelFavouriteLimit;
    }

    public String getVASFavouriteLimit()
    {
        return vasFavouriteLimit;
    }

    public void setVASFavouriteLimit(String vasFavouriteLimit) {
        this.vasFavouriteLimit = vasFavouriteLimit;
    }

    public String getUserPasswordMinLength()
    {
        return userPasswordMinLength;
    }

    public void setUserPasswordMinLength(String userPasswordMinLength) {
        this.userPasswordMinLength = userPasswordMinLength;
    }

    public String getUserPasswordUpperCaseLetters()
    {
        return userPasswordUpperCaseLetters;
    }

    public void setUserPasswordUpperCaseLetters(String userPasswordUpperCaseLetters) {
        this.userPasswordUpperCaseLetters = userPasswordUpperCaseLetters;
    }

    public String getUserPasswordLowerCaseLetters()
    {
        return userPasswordLowerCaseLetters;
    }

    public void setUserPasswordLowerCaseLetters(String userPasswordLowerCaseLetters) {
        this.userPasswordLowerCaseLetters = userPasswordLowerCaseLetters;
    }

    public String getUserPasswordNumbers()
    {
        return userPasswordNumbers;
    }

    public void setUserPasswordNumbers(String userPasswordNumbers) {
        this.userPasswordNumbers = userPasswordNumbers;
    }

    public String getUserPasswordOthersLetters()
    {
        return userPasswordOthersLetters;
    }

    public void setUserPasswordOthersLetters(String userPasswordOthersLetters) {
        this.userPasswordOthersLetters = userPasswordOthersLetters;
    }

    public String getUserPasswordSupportSpace()
    {
        return userPasswordSupportSpace;
    }

    public void setUserPasswordSupportSpace(String userPasswordSupportSpace) {
        this.userPasswordSupportSpace = userPasswordSupportSpace;
    }

    public String getPVRBeginOffset()
    {
        return pvrBeginOffset;
    }

    public void setPVRBeginOffset(String pvrBeginOffset)
    {
        this.pvrBeginOffset = pvrBeginOffset;
    }

    public String getPVREndOffset()
    {
        return pvrEndOffset;
    }

    public void setPVREndOffset(String pvrEndOffset)
    {
        this.pvrEndOffset = pvrEndOffset;
    }

    public String getNPVRSpaceStrategy()
    {
        return npvrSpaceStrategy;
    }

    public void setNPVRSpaceStrategy(String npvrSpaceStrategy) {
        this.npvrSpaceStrategy = npvrSpaceStrategy;
    }

    public String getOTTIMPURLForIPv6()
    {
        return ottIMPURLForIPv6;
    }

    public void setOTTIMPURLForIPv6(String ottIMPURLForIPv6) {
        this.ottIMPURLForIPv6 = ottIMPURLForIPv6;
    }

    public String getOTTIMPURL()
    {
        return ottIMPURL;
    }

    public void setOTTIMPURL(String ottIMPURL)
    {
        this.ottIMPURL = ottIMPURL;
    }

    public String getOTTIMPPort()
    {
        return ottIMPPort;
    }

    public void setOTTIMPPort(String ottIMPPort)
    {
        this.ottIMPPort = ottIMPPort;
    }

    public String getOTTIMPIPForIPv6()
    {
        return ottIMPIPForIPv6;
    }

    public void setOTTIMPIPForIPv6(String ottIMPIPForIPv6) {
        this.ottIMPIPForIPv6 = ottIMPIPForIPv6;
    }

    public String getOTTIMPIP()
    {
        return ottIMPIP;
    }

    public void setOTTIMPIP(String ottIMPIP)
    {
        this.ottIMPIP = ottIMPIP;
    }

    public String getIPTVIMPURLForIPv6()
    {
        return iptvIMPURLForIPv6;
    }

    public void setIPTVIMPURLForIPv6(String iptvIMPURLForIPv6) {
        this.iptvIMPURLForIPv6 = iptvIMPURLForIPv6;
    }

    public String getIPTVIMPURL()
    {
        return iptvIMPURL;
    }

    public void setIPTVIMPURL(String iptvIMPURL)
    {
        this.iptvIMPURL = iptvIMPURL;
    }

    public String getIPTVIMPPort()
    {
        return iptvIMPPort;
    }

    public void setIPTVIMPPort(String iptvIMPPort)
    {
        this.iptvIMPPort = iptvIMPPort;
    }

    public String getIPTVIMPIPForIPv6()
    {
        return iptvIMPIPForIPv6;
    }

    public void setIPTVIMPIPForIPv6(String iptvIMPIPForIPv6) {
        this.iptvIMPIPForIPv6 = iptvIMPIPForIPv6;
    }

    public String getIPTVIMPIP()
    {
        return iptvIMPIP;
    }

    public void setIPTVIMPIP(String iptvIMPIP)
    {
        this.iptvIMPIP = iptvIMPIP;
    }

    public String getIMDomain()
    {
        return imDomain;
    }

    public void setIMDomain(String imDomain)
    {
        this.imDomain = imDomain;
    }

    public String getIMPAuthType()
    {
        return impAuthType;
    }

    public void setIMPAuthType(String impAuthType)
    {
        this.impAuthType = impAuthType;
    }

    public String getPvrDefaultRecCfgDeleteMode()
    {
        return pvrDefaultRecCfgDeleteMode;
    }

    public void setPvrDefaultRecCfgDeleteMode(String pvrDefaultRecCfgDeleteMode) {
        this.pvrDefaultRecCfgDeleteMode = pvrDefaultRecCfgDeleteMode;
    }

    public String getPvrDefaultRecCfgSingleOrSeries()
    {
        return pvrDefaultRecCfgSingleOrSeries;
    }

    public void setPvrDefaultRecCfgSingleOrSeries(String pvrDefaultRecCfgSingleOrSeries) {
        this.pvrDefaultRecCfgSingleOrSeries = pvrDefaultRecCfgSingleOrSeries;
    }

    public String getProfileNameLowerCaseLetters()
    {
        return profileNameLowerCaseLetters;
    }

    public String getProfileNameMinLength()
    {
        return profileNameMinLength;
    }

    public String getProfileNameMaxLength()
    {
        return profileNameMaxLength;
    }

    public String getProfileNameNumbers()
    {
        return profileNameNumbers;
    }

    public String getProfileNameOthersLetters()
    {
        return profileNameOthersLetters;
    }

    public String getProfileNameSupportSpace()
    {
        return profileNameSupportSpace;
    }

    public String getProfileNameUpperCaseLetters()
    {
        return profileNameUpperCaseLetters;
    }

    public String getUserIDLowerCaseLetters()
    {
        return userIDLowerCaseLetters;
    }

    public String getUserIDMinLength()
    {
        return userIDMinLength;
    }

    public String getUserIDMaxLength()
    {
        return userIDMaxLength;
    }

    public String getUserIDNumbers()
    {
        return userIDNumbers;
    }

    public String getUserIDOthersLetters()
    {
        return userIDOthersLetters;
    }

    public String getUserIDSupportSpace()
    {
        return userIDSupportSpace;
    }

    public String getUserIDUpperCaseLetters()
    {
        return userIDUpperCaseLetters;
    }

    public String getDeviceNameLowerCaseLetters()
    {
        return deviceNameLowerCaseLetters;
    }

    public String getDeviceNameMinLength()
    {
        return deviceNameMinLength;
    }

    public String getDeviceNameMaxLength()
    {
        return deviceNameMaxLength;
    }

    public String getDeviceNameNumbers()
    {
        return deviceNameNumbers;
    }

    public String getDeviceNameOthersLetters()
    {
        return deviceNameOthersLetters;
    }

    public String getDeviceNameUpperCaseLetters()
    {
        return deviceNameUpperCaseLetters;
    }

    public String getDeviceNameSupportSpace()
    {
        return deviceNameSupportSpace;
    }

    public String getSearchKeyMinLength()
    {
        return searchKeyMinLength;
    }

    public String getSearchKeyMaxLength()
    {
        return searchKeyMaxLength;
    }

    public String getSearchKeyLowerCaseLetters()
    {
        return searchKeyLowerCaseLetters;
    }

    public String getSearchKeyNumbers()
    {
        return searchKeyNumbers;
    }

    public String getSearchKeyOthersLetters()
    {
        return searchKeyOthersLetters;
    }

    public String getSearchKeySupportSpace()
    {
        return searchKeySupportSpace;
    }

    public String getSearchKeyUpperCaseLetters()
    {
        return searchKeyUpperCaseLetters;
    }

    public String getPlaybillLen()
    {
        return playbillLen;
    }

    public void setPlaybillLen(String playbillLen)
    {
        this.playbillLen = playbillLen;
    }

    public String getRecPlaybillLen()
    {
        return recPlaybillLen;
    }

    public void setRecPlaybillLen(String recPlaybillLen)
    {
        this.recPlaybillLen = recPlaybillLen;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "configType='" + configType + '\'' +
                ", extensionFieldList=" + extensionFieldList +
                ", stbLogServerURL='" + stbLogServerURL + '\'' +
                ", stbLogUploadInterval='" + stbLogUploadInterval + '\'' +
                ", playHeartBitInterval='" + playHeartBitInterval + '\'' +
                ", netrixPushServerURL='" + netrixPushServerURL + '\'' +
                ", skippingTimeBlock='" + skippingTimeBlock + '\'' +
                ", vodFavouriteLimit='" + vodFavouriteLimit + '\'' +
                ", channelFavouriteLimit='" + channelFavouriteLimit + '\'' +
                ", vasFavouriteLimit='" + vasFavouriteLimit + '\'' +
                ", downloadedBufferLength='" + downloadedBufferLength + '\'' +
                ", socialURL='" + socialURL + '\'' +
                ", favouriteLimit='" + favouriteLimit + '\'' +
                ", bookmarkLimit='" + bookmarkLimit + '\'' +
                ", lockLimit='" + lockLimit + '\'' +
                ", profileLimit='" + profileLimit + '\'' +
                ", sqmURL='" + sqmURL + '\'' +
                ", userPasswordMinLength='" + userPasswordMinLength + '\'' +
                ", userPasswordUpperCaseLetters='" + userPasswordUpperCaseLetters + '\'' +
                ", userPasswordLowerCaseLetters='" + userPasswordLowerCaseLetters + '\'' +
                ", userPasswordNumbers='" + userPasswordNumbers + '\'' +
                ", userPasswordOthersLetters='" + userPasswordOthersLetters + '\'' +
                ", userPasswordSupportSpace='" + userPasswordSupportSpace + '\'' +
                ", profileNameMinLength='" + profileNameMinLength + '\'' +
                ", profileNameMaxLength='" + profileNameMaxLength + '\'' +
                ", profileNameUpperCaseLetters='" + profileNameUpperCaseLetters + '\'' +
                ", profileNameLowerCaseLetters='" + profileNameLowerCaseLetters + '\'' +
                ", profileNameNumbers='" + profileNameNumbers + '\'' +
                ", profileNameOthersLetters='" + profileNameOthersLetters + '\'' +
                ", profileNameSupportSpace='" + profileNameSupportSpace + '\'' +
                ", userIDMinLength='" + userIDMinLength + '\'' +
                ", userIDMaxLength='" + userIDMaxLength + '\'' +
                ", userIDUpperCaseLetters='" + userIDUpperCaseLetters + '\'' +
                ", userIDLowerCaseLetters='" + userIDLowerCaseLetters + '\'' +
                ", userIDNumbers='" + userIDNumbers + '\'' +
                ", userIDOthersLetters='" + userIDOthersLetters + '\'' +
                ", userIDSupportSpace='" + userIDSupportSpace + '\'' +
                ", deviceNameMinLength='" + deviceNameMinLength + '\'' +
                ", deviceNameMaxLength='" + deviceNameMaxLength + '\'' +
                ", deviceNameUpperCaseLetters='" + deviceNameUpperCaseLetters + '\'' +
                ", deviceNameLowerCaseLetters='" + deviceNameLowerCaseLetters + '\'' +
                ", deviceNameNumbers='" + deviceNameNumbers + '\'' +
                ", deviceNameOthersLetters='" + deviceNameOthersLetters + '\'' +
                ", deviceNameSupportSpace='" + deviceNameSupportSpace + '\'' +
                ", searchKeyMinLength='" + searchKeyMinLength + '\'' +
                ", searchKeyMaxLength='" + searchKeyMaxLength + '\'' +
                ", searchKeyUpperCaseLetters='" + searchKeyUpperCaseLetters + '\'' +
                ", searchKeyLowerCaseLetters='" + searchKeyLowerCaseLetters + '\'' +
                ", searchKeyNumbers='" + searchKeyNumbers + '\'' +
                ", searchKeyOthersLetters='" + searchKeyOthersLetters + '\'' +
                ", searchKeySupportSpace='" + searchKeySupportSpace + '\'' +
                ", pvrBeginOffset='" + pvrBeginOffset + '\'' +
                ", pvrEndOffset='" + pvrEndOffset + '\'' +
                ", npvrSpaceStrategy='" + npvrSpaceStrategy + '\'' +
                ", ottIMPURLForIPv6='" + ottIMPURLForIPv6 + '\'' +
                ", ottIMPURL='" + ottIMPURL + '\'' +
                ", ottIMPPort='" + "security.." + '\'' +
                ", ottIMPIPForIPv6='" + ottIMPIPForIPv6 + '\'' +
                ", ottIMPIP='" + ottIMPIP + '\'' +
                ", iptvIMPURLForIPv6='" + iptvIMPURLForIPv6 + '\'' +
                ", iptvIMPURL='" + iptvIMPURL + '\'' +
                ", iptvIMPPort='" + "security.." + '\'' +
                ", iptvIMPIPForIPv6='" + iptvIMPIPForIPv6 + '\'' +
                ", iptvIMPIP='" + iptvIMPIP + '\'' +
                ", imDomain='" + imDomain + '\'' +
                ", impAuthType='" + impAuthType + '\'' +
                ", pvrDefaultRecCfgDeleteMode='" + pvrDefaultRecCfgDeleteMode + '\'' +
                ", pvrDefaultRecCfgSingleOrSeries='" + pvrDefaultRecCfgSingleOrSeries + '\'' +
                ", playbillLen='" + playbillLen + '\'' +
                ", recPlaybillLen='" + recPlaybillLen + '\'' +
                '}';
    }
}