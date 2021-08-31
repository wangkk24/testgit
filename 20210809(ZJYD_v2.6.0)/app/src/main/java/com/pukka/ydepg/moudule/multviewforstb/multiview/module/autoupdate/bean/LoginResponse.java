package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean;

import java.util.List;

public class LoginResponse extends SrvResponse{
    /**
     * upgradeDomain : http://101.91.206.13:33500/UPGRADE
     * vspHttpsURL : https://101.91.205.29:33207
     * vspURL : http://101.91.205.29:33200
     * rootCerAddr : http://101.91.205.29:33200
     * NTPDomain : http://101.91.206.13
     * parameters : {"MQMCURL":"10.10.10.10","giftLoyaltyByBrowseAD":"10","isSupportPublicAD":"0","ADPlatformURL":"http://www.ADPlatformUrl.com","giftLoyaltyByReceiveADWithEmail":"10","favoCatalogLimit":"10","TVMSHeartbitURL":"http://101.91.205.29:33207/query.do","ADPlayOverNotifyURL":"http://101.91.205.29:33200/EPG/XML/AdPlayOverNotify","profileLimit":"5","TVMSDelayLength":"60","lockLimit":"8","PLTVDelay":"10","favouriteLimit":"200","DVBEnable":"0","TVMSVodHeartbitURL":"http://101.91.205.29:33207/query.do","mashupAddress":"http://mashup","TVMSHeartbitInterval":"600","bookmarkLimit":"200","giftLoyaltyByReceiveADWithSMS":"10","ADPublicStrategyURL":"http://101.91.205.29:33200/EPG/XML/AdPublicStrategy","SQMURL":"10.10.10.10"}
     * terminalParm : {"DeviceNameMinLength":"1","UserPwdMinLength":"6","UserIDMaxLength":"36","SearchKeyNumbers":"1","ProfileNameUpperCaseLetters":"1","DeviceNameNumbers":"1","UserIDLowerCaseLetters":"1","UserPwdSupportSpace":"0","UserPwdUpperCaseLetters":"0","PlaylistNameNumbers":"1","PlaylistNameUpperCaseLetters":"1","UserIDNumbers":"1","ProfileNameMinLength":"1","UserPwdLowerCaseLetters":"1","PlaylistNameMinLength":"1","PlaylistNameMaxLength":"36","ProfileNameMaxLength":"256","DeviceNameSupportSpace":"0","UserPwdNumbers":"1","SearchKeyMinLength":"1","SearchKeySupportSpace":"1","SearchKeyLowerCaseLetters":"1","SearchKeyMaxLength":"36","UserIDUpperCaseLetters":"1","PlaylistNameLowerCaseLetters":"1","UserIDSupportSpace":"0","ProfileNameNumbers":"1","DeviceNameUpperCaseLetters":"1","values":[{"values":["VREPGVOD"],"key":"VREPGVOD"},{"values":["VREPGRECM"],"key":"VREPGRECM"},{"values":["VREPGLIVE"],"key":"VREPGLIVE"},{"values":["VREPGVOD360"],"key":"VREPGVOD_360"},{"values":["VREPGVODFOV"],"key":"VREPGVOD_FOV"},{"values":["VREPGVODIMAX"],"key":"VREPGVOD_IMAX"},{"values":["VREPGDETAILRECM"],"key":"VREPGDETAILRECM"},{"values":["ALL"],"key":"VREPGROOT"},{"values":["IMAXvr,3Dvr,vrlivetv2,360vr,vrlivetv1,imaxvrtest"],"key":"VREPG_RECM_COL"}],"SearchKeyUpperCaseLetters":"1","cfgType":"0","ProfileNameSupportSpace":"0","UserIDMinLength":"1","PlaylistNameSupportSpace":"0","DeviceNameMaxLength":"36","DeviceNameLowerCaseLetters":"1","ProfileNameLowerCaseLetters":"1"}
     */

    private String upgradeDomain;
    private String vspHttpsURL;
    private String vspURL;
    private String rootCerAddr;
    private String NTPDomain;
    private ParametersBean parameters;
    private TerminalParmBean terminalParm;

    public String getUpgradeDomain() {
        return upgradeDomain;
    }

    public void setUpgradeDomain(String upgradeDomain) {
        this.upgradeDomain = upgradeDomain;
    }

    public String getVspHttpsURL() {
        return vspHttpsURL;
    }

    public void setVspHttpsURL(String vspHttpsURL) {
        this.vspHttpsURL = vspHttpsURL;
    }

    public String getVspURL() {
        return vspURL;
    }

    public void setVspURL(String vspURL) {
        this.vspURL = vspURL;
    }

    public String getRootCerAddr() {
        return rootCerAddr;
    }

    public void setRootCerAddr(String rootCerAddr) {
        this.rootCerAddr = rootCerAddr;
    }

    public String getNTPDomain() {
        return NTPDomain;
    }


    public void setNTPDomain(String NTPDomain) {
        this.NTPDomain = NTPDomain;
    }

    public ParametersBean getParameters() {
        return parameters;
    }

    public void setParameters(ParametersBean parameters) {
        this.parameters = parameters;
    }

    public TerminalParmBean getTerminalParm() {
        return terminalParm;
    }

    public void setTerminalParm(TerminalParmBean terminalParm) {
        this.terminalParm = terminalParm;
    }

    public static class ParametersBean {
        /**
         * MQMCURL : 10.10.10.10
         * giftLoyaltyByBrowseAD : 10
         * isSupportPublicAD : 0
         * ADPlatformURL : http://www.ADPlatformUrl.com
         * giftLoyaltyByReceiveADWithEmail : 10
         * favoCatalogLimit : 10
         * TVMSHeartbitURL : http://101.91.205.29:33207/query.do
         * ADPlayOverNotifyURL : http://101.91.205.29:33200/EPG/XML/AdPlayOverNotify
         * profileLimit : 5
         * TVMSDelayLength : 60
         * lockLimit : 8
         * PLTVDelay : 10
         * favouriteLimit : 200
         * DVBEnable : 0
         * TVMSVodHeartbitURL : http://101.91.205.29:33207/query.do
         * mashupAddress : http://mashup
         * TVMSHeartbitInterval : 600
         * bookmarkLimit : 200
         * giftLoyaltyByReceiveADWithSMS : 10
         * ADPublicStrategyURL : http://101.91.205.29:33200/EPG/XML/AdPublicStrategy
         * SQMURL : 10.10.10.10
         */

        private String MQMCURL;
        private String giftLoyaltyByBrowseAD;
        private String isSupportPublicAD;
        private String ADPlatformURL;
        private String giftLoyaltyByReceiveADWithEmail;
        private String favoCatalogLimit;
        private String TVMSHeartbitURL;
        private String ADPlayOverNotifyURL;
        private String profileLimit;
        private String TVMSDelayLength;
        private String lockLimit;
        private String PLTVDelay;
        private String favouriteLimit;
        private String DVBEnable;
        private String TVMSVodHeartbitURL;
        private String mashupAddress;
        private String TVMSHeartbitInterval;
        private String bookmarkLimit;
        private String giftLoyaltyByReceiveADWithSMS;
        private String ADPublicStrategyURL;
        private String SQMURL;

        public String getMQMCURL() {
            return MQMCURL;
        }

        public void setMQMCURL(String MQMCURL) {
            this.MQMCURL = MQMCURL;
        }

        public String getGiftLoyaltyByBrowseAD() {
            return giftLoyaltyByBrowseAD;
        }

        public void setGiftLoyaltyByBrowseAD(String giftLoyaltyByBrowseAD) {
            this.giftLoyaltyByBrowseAD = giftLoyaltyByBrowseAD;
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

        public String getGiftLoyaltyByReceiveADWithEmail() {
            return giftLoyaltyByReceiveADWithEmail;
        }

        public void setGiftLoyaltyByReceiveADWithEmail(String giftLoyaltyByReceiveADWithEmail) {
            this.giftLoyaltyByReceiveADWithEmail = giftLoyaltyByReceiveADWithEmail;
        }

        public String getFavoCatalogLimit() {
            return favoCatalogLimit;
        }

        public void setFavoCatalogLimit(String favoCatalogLimit) {
            this.favoCatalogLimit = favoCatalogLimit;
        }

        public String getTVMSHeartbitURL() {
            return TVMSHeartbitURL;
        }

        public void setTVMSHeartbitURL(String TVMSHeartbitURL) {
            this.TVMSHeartbitURL = TVMSHeartbitURL;
        }

        public String getADPlayOverNotifyURL() {
            return ADPlayOverNotifyURL;
        }

        public void setADPlayOverNotifyURL(String ADPlayOverNotifyURL) {
            this.ADPlayOverNotifyURL = ADPlayOverNotifyURL;
        }

        public String getProfileLimit() {
            return profileLimit;
        }

        public void setProfileLimit(String profileLimit) {
            this.profileLimit = profileLimit;
        }

        public String getTVMSDelayLength() {
            return TVMSDelayLength;
        }

        public void setTVMSDelayLength(String TVMSDelayLength) {
            this.TVMSDelayLength = TVMSDelayLength;
        }

        public String getLockLimit() {
            return lockLimit;
        }

        public void setLockLimit(String lockLimit) {
            this.lockLimit = lockLimit;
        }

        public String getPLTVDelay() {
            return PLTVDelay;
        }

        public void setPLTVDelay(String PLTVDelay) {
            this.PLTVDelay = PLTVDelay;
        }

        public String getFavouriteLimit() {
            return favouriteLimit;
        }

        public void setFavouriteLimit(String favouriteLimit) {
            this.favouriteLimit = favouriteLimit;
        }

        public String getDVBEnable() {
            return DVBEnable;
        }

        public void setDVBEnable(String DVBEnable) {
            this.DVBEnable = DVBEnable;
        }

        public String getTVMSVodHeartbitURL() {
            return TVMSVodHeartbitURL;
        }

        public void setTVMSVodHeartbitURL(String TVMSVodHeartbitURL) {
            this.TVMSVodHeartbitURL = TVMSVodHeartbitURL;
        }

        public String getMashupAddress() {
            return mashupAddress;
        }

        public void setMashupAddress(String mashupAddress) {
            this.mashupAddress = mashupAddress;
        }

        public String getTVMSHeartbitInterval() {
            return TVMSHeartbitInterval;
        }

        public void setTVMSHeartbitInterval(String TVMSHeartbitInterval) {
            this.TVMSHeartbitInterval = TVMSHeartbitInterval;
        }

        public String getBookmarkLimit() {
            return bookmarkLimit;
        }

        public void setBookmarkLimit(String bookmarkLimit) {
            this.bookmarkLimit = bookmarkLimit;
        }

        public String getGiftLoyaltyByReceiveADWithSMS() {
            return giftLoyaltyByReceiveADWithSMS;
        }

        public void setGiftLoyaltyByReceiveADWithSMS(String giftLoyaltyByReceiveADWithSMS) {
            this.giftLoyaltyByReceiveADWithSMS = giftLoyaltyByReceiveADWithSMS;
        }

        public String getADPublicStrategyURL() {
            return ADPublicStrategyURL;
        }

        public void setADPublicStrategyURL(String ADPublicStrategyURL) {
            this.ADPublicStrategyURL = ADPublicStrategyURL;
        }

        public String getSQMURL() {
            return SQMURL;
        }

        public void setSQMURL(String SQMURL) {
            this.SQMURL = SQMURL;
        }
    }

    public static class TerminalParmBean {
        /**
         * DeviceNameMinLength : 1
         * UserPwdMinLength : 6
         * UserIDMaxLength : 36
         * SearchKeyNumbers : 1
         * ProfileNameUpperCaseLetters : 1
         * DeviceNameNumbers : 1
         * UserIDLowerCaseLetters : 1
         * UserPwdSupportSpace : 0
         * UserPwdUpperCaseLetters : 0
         * PlaylistNameNumbers : 1
         * PlaylistNameUpperCaseLetters : 1
         * UserIDNumbers : 1
         * ProfileNameMinLength : 1
         * UserPwdLowerCaseLetters : 1
         * PlaylistNameMinLength : 1
         * PlaylistNameMaxLength : 36
         * ProfileNameMaxLength : 256
         * DeviceNameSupportSpace : 0
         * UserPwdNumbers : 1
         * SearchKeyMinLength : 1
         * SearchKeySupportSpace : 1
         * SearchKeyLowerCaseLetters : 1
         * SearchKeyMaxLength : 36
         * UserIDUpperCaseLetters : 1
         * PlaylistNameLowerCaseLetters : 1
         * UserIDSupportSpace : 0
         * ProfileNameNumbers : 1
         * DeviceNameUpperCaseLetters : 1
         * values : [{"values":["VREPGVOD"],"key":"VREPGVOD"},{"values":["VREPGRECM"],"key":"VREPGRECM"},{"values":["VREPGLIVE"],"key":"VREPGLIVE"},{"values":["VREPGVOD360"],"key":"VREPGVOD_360"},{"values":["VREPGVODFOV"],"key":"VREPGVOD_FOV"},{"values":["VREPGVODIMAX"],"key":"VREPGVOD_IMAX"},{"values":["VREPGDETAILRECM"],"key":"VREPGDETAILRECM"},{"values":["ALL"],"key":"VREPGROOT"},{"values":["IMAXvr,3Dvr,vrlivetv2,360vr,vrlivetv1,imaxvrtest"],"key":"VREPG_RECM_COL"}]
         * SearchKeyUpperCaseLetters : 1
         * cfgType : 0
         * ProfileNameSupportSpace : 0
         * UserIDMinLength : 1
         * PlaylistNameSupportSpace : 0
         * DeviceNameMaxLength : 36
         * DeviceNameLowerCaseLetters : 1
         * ProfileNameLowerCaseLetters : 1
         */

        private String DeviceNameMinLength;
        private String UserPwdMinLength;
        private String UserIDMaxLength;
        private String SearchKeyNumbers;
        private String ProfileNameUpperCaseLetters;
        private String DeviceNameNumbers;
        private String UserIDLowerCaseLetters;
        private String UserPwdSupportSpace;
        private String UserPwdUpperCaseLetters;
        private String PlaylistNameNumbers;
        private String PlaylistNameUpperCaseLetters;
        private String UserIDNumbers;
        private String ProfileNameMinLength;
        private String UserPwdLowerCaseLetters;
        private String PlaylistNameMinLength;
        private String PlaylistNameMaxLength;
        private String ProfileNameMaxLength;
        private String DeviceNameSupportSpace;
        private String UserPwdNumbers;
        private String SearchKeyMinLength;
        private String SearchKeySupportSpace;
        private String SearchKeyLowerCaseLetters;
        private String SearchKeyMaxLength;
        private String UserIDUpperCaseLetters;
        private String PlaylistNameLowerCaseLetters;
        private String UserIDSupportSpace;
        private String ProfileNameNumbers;
        private String DeviceNameUpperCaseLetters;
        private String SearchKeyUpperCaseLetters;
        private String cfgType;
        private String ProfileNameSupportSpace;
        private String UserIDMinLength;
        private String PlaylistNameSupportSpace;
        private String DeviceNameMaxLength;
        private String DeviceNameLowerCaseLetters;
        private String ProfileNameLowerCaseLetters;
        private List<ValuesBean> values;

        public String getDeviceNameMinLength() {
            return DeviceNameMinLength;
        }

        public void setDeviceNameMinLength(String DeviceNameMinLength) {
            this.DeviceNameMinLength = DeviceNameMinLength;
        }

        public String getUserPwdMinLength() {
            return UserPwdMinLength;
        }

        public void setUserPwdMinLength(String UserPwdMinLength) {
            this.UserPwdMinLength = UserPwdMinLength;
        }

        public String getUserIDMaxLength() {
            return UserIDMaxLength;
        }

        public void setUserIDMaxLength(String UserIDMaxLength) {
            this.UserIDMaxLength = UserIDMaxLength;
        }

        public String getSearchKeyNumbers() {
            return SearchKeyNumbers;
        }

        public void setSearchKeyNumbers(String SearchKeyNumbers) {
            this.SearchKeyNumbers = SearchKeyNumbers;
        }

        public String getProfileNameUpperCaseLetters() {
            return ProfileNameUpperCaseLetters;
        }

        public void setProfileNameUpperCaseLetters(String ProfileNameUpperCaseLetters) {
            this.ProfileNameUpperCaseLetters = ProfileNameUpperCaseLetters;
        }

        public String getDeviceNameNumbers() {
            return DeviceNameNumbers;
        }

        public void setDeviceNameNumbers(String DeviceNameNumbers) {
            this.DeviceNameNumbers = DeviceNameNumbers;
        }

        public String getUserIDLowerCaseLetters() {
            return UserIDLowerCaseLetters;
        }

        public void setUserIDLowerCaseLetters(String UserIDLowerCaseLetters) {
            this.UserIDLowerCaseLetters = UserIDLowerCaseLetters;
        }

        public String getUserPwdSupportSpace() {
            return UserPwdSupportSpace;
        }

        public void setUserPwdSupportSpace(String UserPwdSupportSpace) {
            this.UserPwdSupportSpace = UserPwdSupportSpace;
        }

        public String getUserPwdUpperCaseLetters() {
            return UserPwdUpperCaseLetters;
        }

        public void setUserPwdUpperCaseLetters(String UserPwdUpperCaseLetters) {
            this.UserPwdUpperCaseLetters = UserPwdUpperCaseLetters;
        }

        public String getPlaylistNameNumbers() {
            return PlaylistNameNumbers;
        }

        public void setPlaylistNameNumbers(String PlaylistNameNumbers) {
            this.PlaylistNameNumbers = PlaylistNameNumbers;
        }

        public String getPlaylistNameUpperCaseLetters() {
            return PlaylistNameUpperCaseLetters;
        }

        public void setPlaylistNameUpperCaseLetters(String PlaylistNameUpperCaseLetters) {
            this.PlaylistNameUpperCaseLetters = PlaylistNameUpperCaseLetters;
        }

        public String getUserIDNumbers() {
            return UserIDNumbers;
        }

        public void setUserIDNumbers(String UserIDNumbers) {
            this.UserIDNumbers = UserIDNumbers;
        }

        public String getProfileNameMinLength() {
            return ProfileNameMinLength;
        }

        public void setProfileNameMinLength(String ProfileNameMinLength) {
            this.ProfileNameMinLength = ProfileNameMinLength;
        }

        public String getUserPwdLowerCaseLetters() {
            return UserPwdLowerCaseLetters;
        }

        public void setUserPwdLowerCaseLetters(String UserPwdLowerCaseLetters) {
            this.UserPwdLowerCaseLetters = UserPwdLowerCaseLetters;
        }

        public String getPlaylistNameMinLength() {
            return PlaylistNameMinLength;
        }

        public void setPlaylistNameMinLength(String PlaylistNameMinLength) {
            this.PlaylistNameMinLength = PlaylistNameMinLength;
        }

        public String getPlaylistNameMaxLength() {
            return PlaylistNameMaxLength;
        }

        public void setPlaylistNameMaxLength(String PlaylistNameMaxLength) {
            this.PlaylistNameMaxLength = PlaylistNameMaxLength;
        }

        public String getProfileNameMaxLength() {
            return ProfileNameMaxLength;
        }

        public void setProfileNameMaxLength(String ProfileNameMaxLength) {
            this.ProfileNameMaxLength = ProfileNameMaxLength;
        }

        public String getDeviceNameSupportSpace() {
            return DeviceNameSupportSpace;
        }

        public void setDeviceNameSupportSpace(String DeviceNameSupportSpace) {
            this.DeviceNameSupportSpace = DeviceNameSupportSpace;
        }

        public String getUserPwdNumbers() {
            return UserPwdNumbers;
        }

        public void setUserPwdNumbers(String UserPwdNumbers) {
            this.UserPwdNumbers = UserPwdNumbers;
        }

        public String getSearchKeyMinLength() {
            return SearchKeyMinLength;
        }

        public void setSearchKeyMinLength(String SearchKeyMinLength) {
            this.SearchKeyMinLength = SearchKeyMinLength;
        }

        public String getSearchKeySupportSpace() {
            return SearchKeySupportSpace;
        }

        public void setSearchKeySupportSpace(String SearchKeySupportSpace) {
            this.SearchKeySupportSpace = SearchKeySupportSpace;
        }

        public String getSearchKeyLowerCaseLetters() {
            return SearchKeyLowerCaseLetters;
        }

        public void setSearchKeyLowerCaseLetters(String SearchKeyLowerCaseLetters) {
            this.SearchKeyLowerCaseLetters = SearchKeyLowerCaseLetters;
        }

        public String getSearchKeyMaxLength() {
            return SearchKeyMaxLength;
        }

        public void setSearchKeyMaxLength(String SearchKeyMaxLength) {
            this.SearchKeyMaxLength = SearchKeyMaxLength;
        }

        public String getUserIDUpperCaseLetters() {
            return UserIDUpperCaseLetters;
        }

        public void setUserIDUpperCaseLetters(String UserIDUpperCaseLetters) {
            this.UserIDUpperCaseLetters = UserIDUpperCaseLetters;
        }

        public String getPlaylistNameLowerCaseLetters() {
            return PlaylistNameLowerCaseLetters;
        }

        public void setPlaylistNameLowerCaseLetters(String PlaylistNameLowerCaseLetters) {
            this.PlaylistNameLowerCaseLetters = PlaylistNameLowerCaseLetters;
        }

        public String getUserIDSupportSpace() {
            return UserIDSupportSpace;
        }

        public void setUserIDSupportSpace(String UserIDSupportSpace) {
            this.UserIDSupportSpace = UserIDSupportSpace;
        }

        public String getProfileNameNumbers() {
            return ProfileNameNumbers;
        }

        public void setProfileNameNumbers(String ProfileNameNumbers) {
            this.ProfileNameNumbers = ProfileNameNumbers;
        }

        public String getDeviceNameUpperCaseLetters() {
            return DeviceNameUpperCaseLetters;
        }

        public void setDeviceNameUpperCaseLetters(String DeviceNameUpperCaseLetters) {
            this.DeviceNameUpperCaseLetters = DeviceNameUpperCaseLetters;
        }

        public String getSearchKeyUpperCaseLetters() {
            return SearchKeyUpperCaseLetters;
        }

        public void setSearchKeyUpperCaseLetters(String SearchKeyUpperCaseLetters) {
            this.SearchKeyUpperCaseLetters = SearchKeyUpperCaseLetters;
        }

        public String getCfgType() {
            return cfgType;
        }

        public void setCfgType(String cfgType) {
            this.cfgType = cfgType;
        }

        public String getProfileNameSupportSpace() {
            return ProfileNameSupportSpace;
        }

        public void setProfileNameSupportSpace(String ProfileNameSupportSpace) {
            this.ProfileNameSupportSpace = ProfileNameSupportSpace;
        }

        public String getUserIDMinLength() {
            return UserIDMinLength;
        }

        public void setUserIDMinLength(String UserIDMinLength) {
            this.UserIDMinLength = UserIDMinLength;
        }

        public String getPlaylistNameSupportSpace() {
            return PlaylistNameSupportSpace;
        }

        public void setPlaylistNameSupportSpace(String PlaylistNameSupportSpace) {
            this.PlaylistNameSupportSpace = PlaylistNameSupportSpace;
        }

        public String getDeviceNameMaxLength() {
            return DeviceNameMaxLength;
        }

        public void setDeviceNameMaxLength(String DeviceNameMaxLength) {
            this.DeviceNameMaxLength = DeviceNameMaxLength;
        }

        public String getDeviceNameLowerCaseLetters() {
            return DeviceNameLowerCaseLetters;
        }

        public void setDeviceNameLowerCaseLetters(String DeviceNameLowerCaseLetters) {
            this.DeviceNameLowerCaseLetters = DeviceNameLowerCaseLetters;
        }

        public String getProfileNameLowerCaseLetters() {
            return ProfileNameLowerCaseLetters;
        }

        public void setProfileNameLowerCaseLetters(String ProfileNameLowerCaseLetters) {
            this.ProfileNameLowerCaseLetters = ProfileNameLowerCaseLetters;
        }

        public List<ValuesBean> getValues() {
            return values;
        }

        public void setValues(List<ValuesBean> values) {
            this.values = values;
        }

        public static class ValuesBean {
            /**
             * values : ["VREPGVOD"]
             * key : VREPGVOD
             */

            private String key;
            private List<String> values;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public List<String> getValues() {
                return values;
            }

            public void setValues(List<String> values) {
                this.values = values;
            }
        }
    }
}
