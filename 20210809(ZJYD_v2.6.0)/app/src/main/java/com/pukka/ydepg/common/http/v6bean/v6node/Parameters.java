package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class Parameters {

    /**
     * favouriteLimit :
     * bookmarkLimit :
     * lockLimit :
     * profileLimit :
     * favoCatalogLimit :
     * mashupAddress :
     * TVMSHeartbitURL :
     * TVMSVodHeartbitURL :
     * TVMSHeartbitInterval :
     * TVMSDelayLength :
     * isSupportPublicAD :
     * ADPlatformURL :
     * ADPublicStrategyURL :
     * ADPlayOverNotifyURL :
     * giftLoyaltyByBrowseAD :
     * giftLoyaltyByReceiveADWithSMS :
     * giftLoyaltyByReceiveADWithEmail :
     * PLTVDelay :
     * DVBEnable :
     * SQMURL :
     * MQMCURL :
     * extensionFields :
     */

    /**
     * 每个Profile支持的收藏上限。
     * <p>
     * 说明：
     * VSP平台支持按收藏总数控制，也支持按内容类型控制每种内容的收藏上限，此参数表示收藏总数上限
     */
    @SerializedName("favouriteLimit")
    private String favouriteLimit;

    /**
     * 每个Profile支持的书签上限。
     * <p>
     * 说明：
     * VSP平台支持按书签总数控制，也支持按书签类型控制每种内容的书签上限，此参数表示书签总数上限
     */
    @SerializedName("bookmarkLimit")
    private String bookmarkLimit;

    /**
     * 每个订户支持的童锁上限
     */
    @SerializedName("lockLimit")
    private String lockLimit;

    /**
     * 用户Profile个数上限
     */
    @SerializedName("profileLimit")
    private String profileLimit;

    /**
     * 收藏夹分类个数
     */
    @SerializedName("favoCatalogLimit")
    private String favoCatalogLimit;

    /**
     * Mashup平台地址
     */
    @SerializedName("mashupAddress")
    private String mashupAddress;

    /**
     * TVMS发送心跳消息的地址。
     * <p>
     * 例如：http://ip:port/gateway/queryxml.do
     * <p>
     * IP地址支持V4或V6
     */
    @SerializedName("TVMSHeartbitURL")
    private String TVMSHeartbitURL;

    /**
     * TVMS发送VOD消息的地址。
     * <p>
     * 例如：http://ip:port/gateway/queryxml.do
     * <p>
     * IP地址支持V4或V6.
     */
    @SerializedName("TVMSVodHeartbitURL")
    private String TVMSVodHeartbitURL;

    /**
     * TVMS的心跳周期。
     * <p>
     * 单位：秒。
     */
    @SerializedName("TVMSHeartbitInterval")
    private String TVMSHeartbitInterval;

    /**
     * TVMS的消息冲突时的展示延迟时长。
     * <p>
     * 单位：秒。
     */
    @SerializedName("TVMSDelayLength")
    private String TVMSDelayLength;

    /**
     * 是否支持公众广告。
     * <p>
     * 取值范围：
     * <p>
     * 1：支持
     * 0：不支持
     */
    @SerializedName("isSupportPublicAD")
    private String isSupportPublicAD;

    /**
     * 广告平台URL。
     * <p>
     * IP地址支持V4或V6
     */
    @SerializedName("ADPlatformURL")
    private String ADPlatformURL;

    /**
     * 获取广告策略URL。
     * <p>
     * IP地址支持V4或V6。
     */
    @SerializedName("ADPublicStrategyURL")
    private String ADPublicStrategyURL;

    /**
     * 广告播放完成通知地址。
     * <p>
     * IP地址支持V4或V6
     */
    @SerializedName("ADPlayOverNotifyURL")
    private String ADPlayOverNotifyURL;

    /**
     * 查看广告内容详细信息赠送多少积分
     */
    @SerializedName("giftLoyaltyByBrowseAD")
    private String giftLoyaltyByBrowseAD;

    /**
     * 通过短信接收广告信息赠送多少积分。
     */
    @SerializedName("giftLoyaltyByReceiveADWithSMS")
    private String giftLoyaltyByReceiveADWithSMS;

    /**
     * 通过邮件接收广告信息赠送多少积分。
     */
    @SerializedName("giftLoyaltyByReceiveADWithEmail")
    private String giftLoyaltyByReceiveADWithEmail;

    /**
     * 频道启动本地时移的延时。
     * <p>
     * 单位：秒。
     */
    @SerializedName("PLTVDelay")
    private String PLTVDelay;

    /**
     * Envision Video系统是否支持维护DVB。
     * <p>
     * 取值范围：
     * <p>
     * 0：不支持
     * 1：支持
     */
    @SerializedName("DVBEnable")
    private String DVBEnable;

    /**
     * IPTV终端对接的第三方业务质量管理系统地址。
     * <p>
     * IP地址支持V4或V6。
     */
    @SerializedName("SQMURL")
    private String SQMURL;

    /**
     * OTT终端对接的第三方业务质量管理系统地址。
     * <p>
     * IP地址支持V4或V6。
     */
    @SerializedName("MQMCURL")
    private String MQMCURL;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getFavouriteLimit() {
        return favouriteLimit;
    }

    public void setFavouriteLimit(String favouriteLimit) {
        this.favouriteLimit = favouriteLimit;
    }

    public String getBookmarkLimit() {
        return bookmarkLimit;
    }

    public void setBookmarkLimit(String bookmarkLimit) {
        this.bookmarkLimit = bookmarkLimit;
    }

    public String getLockLimit() {
        return lockLimit;
    }

    public void setLockLimit(String lockLimit) {
        this.lockLimit = lockLimit;
    }

    public String getProfileLimit() {
        return profileLimit;
    }

    public void setProfileLimit(String profileLimit) {
        this.profileLimit = profileLimit;
    }

    public String getFavoCatalogLimit() {
        return favoCatalogLimit;
    }

    public void setFavoCatalogLimit(String favoCatalogLimit) {
        this.favoCatalogLimit = favoCatalogLimit;
    }

    public String getMashupAddress() {
        return mashupAddress;
    }

    public void setMashupAddress(String mashupAddress) {
        this.mashupAddress = mashupAddress;
    }

    public String getTVMSHeartbitURL() {
        return TVMSHeartbitURL;
    }

    public void setTVMSHeartbitURL(String TVMSHeartbitURL) {
        this.TVMSHeartbitURL = TVMSHeartbitURL;
    }

    public String getTVMSVodHeartbitURL() {
        return TVMSVodHeartbitURL;
    }

    public void setTVMSVodHeartbitURL(String TVMSVodHeartbitURL) {
        this.TVMSVodHeartbitURL = TVMSVodHeartbitURL;
    }

    public String getTVMSHeartbitInterval() {
        return TVMSHeartbitInterval;
    }

    public void setTVMSHeartbitInterval(String TVMSHeartbitInterval) {
        this.TVMSHeartbitInterval = TVMSHeartbitInterval;
    }

    public String getTVMSDelayLength() {
        return TVMSDelayLength;
    }

    public void setTVMSDelayLength(String TVMSDelayLength) {
        this.TVMSDelayLength = TVMSDelayLength;
    }

    public String getIsSupportPublicAD() {
        return isSupportPublicAD;
    }

    public void setIsSupportPublicAD(String isSupportPublicAD) {
        this.isSupportPublicAD = isSupportPublicAD;
    }

    public String getADPlatformURL() {
        return ADPlatformURL;
    }

    public void setADPlatformURL(String ADPlatformURL) {
        this.ADPlatformURL = ADPlatformURL;
    }

    public String getADPublicStrategyURL() {
        return ADPublicStrategyURL;
    }

    public void setADPublicStrategyURL(String ADPublicStrategyURL) {
        this.ADPublicStrategyURL = ADPublicStrategyURL;
    }

    public String getADPlayOverNotifyURL() {
        return ADPlayOverNotifyURL;
    }

    public void setADPlayOverNotifyURL(String ADPlayOverNotifyURL) {
        this.ADPlayOverNotifyURL = ADPlayOverNotifyURL;
    }

    public String getGiftLoyaltyByBrowseAD() {
        return giftLoyaltyByBrowseAD;
    }

    public void setGiftLoyaltyByBrowseAD(String giftLoyaltyByBrowseAD) {
        this.giftLoyaltyByBrowseAD = giftLoyaltyByBrowseAD;
    }

    public String getGiftLoyaltyByReceiveADWithSMS() {
        return giftLoyaltyByReceiveADWithSMS;
    }

    public void setGiftLoyaltyByReceiveADWithSMS(String giftLoyaltyByReceiveADWithSMS) {
        this.giftLoyaltyByReceiveADWithSMS = giftLoyaltyByReceiveADWithSMS;
    }

    public String getGiftLoyaltyByReceiveADWithEmail() {
        return giftLoyaltyByReceiveADWithEmail;
    }

    public void setGiftLoyaltyByReceiveADWithEmail(String giftLoyaltyByReceiveADWithEmail) {
        this.giftLoyaltyByReceiveADWithEmail = giftLoyaltyByReceiveADWithEmail;
    }

    public String getPLTVDelay() {
        return PLTVDelay;
    }

    public void setPLTVDelay(String PLTVDelay) {
        this.PLTVDelay = PLTVDelay;
    }

    public String getDVBEnable() {
        return DVBEnable;
    }

    public void setDVBEnable(String DVBEnable) {
        this.DVBEnable = DVBEnable;
    }

    public String getSQMURL() {
        return SQMURL;
    }

    public void setSQMURL(String SQMURL) {
        this.SQMURL = SQMURL;
    }

    public String getMQMCURL() {
        return MQMCURL;
    }

    public void setMQMCURL(String MQMCURL) {
        this.MQMCURL = MQMCURL;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
