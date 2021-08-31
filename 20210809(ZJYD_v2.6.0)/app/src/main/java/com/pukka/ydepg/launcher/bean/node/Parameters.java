package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;

/**
 * the Metadata of Parameters
 *
 */
public class Parameters extends ExtensionField
{
    private static final String TAG = Parameters.class.getSimpleName();
    /**
     * field:favouriteLimit
     * is controlled by single profile
     */
    @SerializedName("favouriteLimit")
    private String favouriteLimit;

    /**
     * field:bookmarkLimit
     * is controlled by single profile
     */
    @SerializedName("bookmarkLimit")
    private String bookmarkLimit;
    /**
     * field:lockLimit
     * is controlled by all profile
     */
    @SerializedName("lockLimit")
    private String lockLimit;

    /**
     * field:profileLimit
     * is controlled by all profile
     */
    @SerializedName("profileLimit")
    private String profileLimit;
    /**
     * field:favoriteCatalogLimit
     */
    @SerializedName("favoCatalogLimit")
    private String favoriteCatalogLimit;

    /**
     * field:mashUpAddress
     */
    @SerializedName("mashupAddress")
    private String mashUpAddress;

    /**
     * field:tvmsHeartBeatURL
     */
    @SerializedName("TVMSHeartbitURL")
    private String tvmsHeartbeatURL;
    /**
     * field:tvmsVODHeartBeattURL
     */
    @SerializedName("TVMSVodHeartbitURL")
    private String tvmsVODHeartbeatURL;

    /**
     * field:tvmsHeartBeatInterval
     */
    @SerializedName("TVMSHeartbitInterval")
    private String tvmsHeartbeatInterval;

    /**
     * field:tvmsDelayLength
     */
    @SerializedName("TVMSDelayLength")
    private String tvmsDelayLength;

    /**
     * field:isSupportPublicAd
     */
    @SerializedName("isSupportPublicAD")
    private String isSupportPublicAd;

    /**
     * the range of field type
     */
    public interface IsSupportedUserLogCollect
    {
        /**
         * 0:not support
         */
        String UN_SUPPORT = "0";
        /**
         * 1:not support
         */
        String SUPPORT = "1";
    }

    /**
     * field:advertisementPlatformURL
     */
    @SerializedName("ADPlatformURL")
    private String advertisementPlatformURL;

    /**
     * field:advertisementPlatformURL
     */
    @SerializedName("ADPublicStrategyURL")
    private String advertisementPublicStrategyURL;

    /**
     * field:advertisementPlayOverNotifyURL
     */
    @SerializedName("ADPlayOverNotifyURL")
    private String advertisementPlayOverNotifyURL;

    /**
     * field:giftLoyaltyByBrowseAd
     */
    @SerializedName("giftLoyaltyByBrowseAD")
    private String giftLoyaltyByBrowseAd;

    /**
     * field:giftLoyaltyByReceiveAdvertisementWithSMS
     */
    @SerializedName("giftLoyaltyByReceiveADWithSMS")
    private String giftLoyaltyByReceiveAdvertisementWithSMS;

    /**
     * field:giftLoyaltyByReceiveAdvertisementWithSMS
     */
    @SerializedName("giftLoyaltyByReceiveADWithEmail")
    private String giftLoyaltyByReceiveAdvertisementWithEmail;

    /**
     * field:restartTVOffset
     * unit:second
     */
    @SerializedName("PLTVDelay")
    private String pltvDelay;

    /**
     * field:restartTVOffset
     */
    @SerializedName("DVBEnable")
    private String dvbEnable;

    /**
     * the range of field type
     */
    public interface DVBEnable
    {
        /**
         * 0:not support
         */
        String UN_SUPPORT = "0";
        /**
         * 1:not support
         */
        String SUPPORT = "1";

    }

    /**
     * field:restartTVOffset
     */
    @SerializedName("SQMURL")
    private String sqmURL;

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

    public String getFavoriteCatalogLimit()
    {
        return favoriteCatalogLimit;
    }

    public void setFavoriteCatalogLimit(String favoriteCatalogLimit)
    {
        this.favoriteCatalogLimit = favoriteCatalogLimit;
    }

    public String getMashupAddress()
    {
        return mashUpAddress;
    }

    public void setMashUpAddress(String mashUpAddress)
    {
        this.mashUpAddress = mashUpAddress;
    }

    public String getTVMSHeartBeatURL()
    {
        return tvmsHeartbeatURL;
    }

    public void setTVMSHeartBeatURL(String tvmsHeartBeatURL)
    {
        this.tvmsHeartbeatURL = tvmsHeartBeatURL;
    }

    public String getTVMSVODHeartbeatURL()
    {
        return tvmsVODHeartbeatURL;
    }

    public void setTVMSVODHeartbeatURL(String tvmsVODHeartbeatURL)
    {
        this.tvmsVODHeartbeatURL = tvmsVODHeartbeatURL;
    }

    public String getTVMSHeartbeatInterval()
    {
        return tvmsHeartbeatInterval;
    }

    public void setTVMSHeartBeatInterval(String tvmsHeartbeatInterval)
    {
        this.tvmsHeartbeatInterval = tvmsHeartbeatInterval;
    }

    public String getTVMSDelayLength()
    {
        return tvmsDelayLength;
    }

    public void setTVMSDelayLength(String tvmsDelayLength)
    {
        this.tvmsDelayLength = tvmsDelayLength;
    }

    public String getIsSupportPublicAD()
    {
        return isSupportPublicAd;
    }

    public void setIsSupportPublicAD(String isSupportPublicAd)
    {
        this.isSupportPublicAd = isSupportPublicAd;
    }

    public String getAdvertisementPlatformURL()
    {
        return advertisementPlatformURL;
    }

    public void setAdvertisementPlatformURL(String advertisementPlatformURL)
    {
        this.advertisementPlatformURL = advertisementPlatformURL;
    }

    public String getAdvertisementPublicStrategyURL()
    {
        return advertisementPublicStrategyURL;
    }

    public void setAdvertisementPublicStrategyURL(String advertisementPublicStrategyURL)
    {
        this.advertisementPublicStrategyURL = advertisementPublicStrategyURL;
    }

    public String getAdvertisementPlayOverNotifyURL()
    {
        return advertisementPlayOverNotifyURL;
    }

    public void setAdvertisementPlayOverNotifyURL(String advertisementPlayOverNotifyURL)
    {
        this.advertisementPlayOverNotifyURL = advertisementPlayOverNotifyURL;
    }

    public String getGiftLoyaltyByBrowseAD()
    {
        return giftLoyaltyByBrowseAd;
    }

    public void setGiftLoyaltyByBrowseAD(String giftLoyaltyByBrowseAd)
    {
        this.giftLoyaltyByBrowseAd = giftLoyaltyByBrowseAd;
    }

    public String getGiftLoyaltyByReceiveAdvertisementWithSMS()
    {
        return giftLoyaltyByReceiveAdvertisementWithSMS;
    }

    public void setGiftLoyaltyByReceiveAdvertisementWithSMS(String
            giftLoyaltyByReceiveAdvertisementWithSMS)
    {
        this.giftLoyaltyByReceiveAdvertisementWithSMS = giftLoyaltyByReceiveAdvertisementWithSMS;
    }

    public String getGiftLoyaltyByReceiveAdvertisementWithEmail()
    {
        return giftLoyaltyByReceiveAdvertisementWithEmail;
    }

    public void setGiftLoyaltyByReceiveAdvertisementWithEmail(String
            giftLoyaltyByReceiveAdvertisementWithEmail)
    {
        this.giftLoyaltyByReceiveAdvertisementWithEmail =
                giftLoyaltyByReceiveAdvertisementWithEmail;
    }


    public String getPLTVDelay()
    {
        return pltvDelay;
    }

    public void setPLTVDelay(String pltvDelay)
    {
        this.pltvDelay = pltvDelay;
    }

    public String getDVBEnable()
    {
        return dvbEnable;
    }

    public void setDVBEnable(String dvbEnable)
    {
        this.dvbEnable = dvbEnable;
    }

    public String getSQMURL()
    {
        return sqmURL;
    }

    public void setSQMURL(String sqmURL)
    {
        this.sqmURL = sqmURL;
    }

    @Override
    public String toString()
    {
        return "Parameters{" +
                "favouriteLimit='" + favouriteLimit + '\'' +
                ", bookmarkLimit='" + bookmarkLimit + '\'' +
                ", lockLimit='" + lockLimit + '\'' +
                ", profileLimit='" + profileLimit + '\'' +
                ", favoriteCatalogLimit='" + favoriteCatalogLimit + '\'' +
                ", mashUpAddress='" + mashUpAddress + '\'' +
                ", tvmsHeartbeatURL='" + tvmsHeartbeatURL+ '\'' +
                ", tvmsVODHeartbeatURL='" + tvmsVODHeartbeatURL + '\'' +
                ", tvmsHeartbeatInterval='" + tvmsHeartbeatInterval + '\'' +
                ", tvmsDelayLength='" + tvmsDelayLength + '\'' +
                ", isSupportPublicAd='" + isSupportPublicAd + '\'' +
                ", advertisementPlatformURL='" + advertisementPlatformURL + '\'' +
                ", advertisementPublicStrategyURL='" + advertisementPublicStrategyURL + '\'' +
                ", advertisementPlayOverNotifyURL='" + advertisementPlayOverNotifyURL + '\'' +
                ", giftLoyaltyByBrowseAd='" + giftLoyaltyByBrowseAd + '\'' +
                ", giftLoyaltyByReceiveAdvertisementWithSMS='" +
                giftLoyaltyByReceiveAdvertisementWithSMS + '\'' +
                ", giftLoyaltyByReceiveAdvertisementWithEmail='" +
                giftLoyaltyByReceiveAdvertisementWithEmail + '\'' +
                ", pltvDelay='" + pltvDelay + '\'' +
                ", dvbEnable='" + dvbEnable + '\'' +
                ", sqmURL='" + sqmURL + '\'' +
                '}';
    }
}