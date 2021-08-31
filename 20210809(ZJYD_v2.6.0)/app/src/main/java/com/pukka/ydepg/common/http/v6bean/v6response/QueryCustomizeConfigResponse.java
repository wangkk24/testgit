package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.ISOCode;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Rating;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.QueryLocationResponse.java
 * @author: yh
 * @date: 2017-09-06 09:10
 */

public class QueryCustomizeConfigResponse {


    /**
     * configurations : [{"cfgType":"0","values":[{"key":"AVSDescriptionUrl","values":["http://www.baidu.com"]},{"key":"ChannelCategoryID","values":["AllChannel"]},{"key":"Default_BeginOffset","values":[null]},{"key":"Default_EndOffset","values":[null]},{"key":"Default_RecCfgDeleteMode","values":[null]},{"key":"Default_RecCfgSingleOrSeries","values":[null]},{"key":"DownloadedBufferLength","values":["45"]},{"key":"ForgetParentPasswordURL","values":["http://www.huawei.com/cn/"]},{"key":"HomeCategoryID","values":["VODMovieRecm"]},{"key":"IMDomain","values":["10"]},{"key":"channel_subject_id","values":["AllChannel"]},{"key":"companionModeUrlForIPad","values":["http://www.huawei.com"]},{"key":"companionModeUrlForIPhone","values":["http://www.huawei.com"]},{"key":"credit_limit_list","values":["-1,100,1000,10000"]},{"key":"currency_symbol","values":["$"]},{"key":"discoveryUrl","values":["http://10.10.12.80:8080"]},{"key":"dropboxForceHttp","values":["1"]},{"key":"enhancedMetaData","values":["1"]},{"key":"enhancedRecommendEngine","values":["1"]},{"key":"familyMemberServiceID4SOL","values":["SOL"]},{"key":"familyMemberServiceID4TINT","values":["TINT"]},{"key":"karakey","values":["http://www.huawei.com/cn/"]},{"key":"RiceUrl","values":["http://10.10.12.80:8080"]},{"key":"SAM3LockTime","values":["70"]},{"key":"SOLSubnetID","values":["SOL"]},{"key":"STBTOPMenuID","values":["mainmenu_epg"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=AndroidPad"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=AndroidPhone"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=Ipad"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=Iphone"]},{"key":"ScallingServerURL","values":[]},{"key":"suggestion_email_dest","values":["diaoyinghu@huawei.com"]},{"key":"vod_subject_id","values":["VODAll"]}]}]
     * currencyAlphCode : $
     * currencyRate : 100
     * languages : [{"ID":"1","fullName":"English","name":"en"},{"ID":"2","fullName":"中文","name":"zh"}]
     * ratings : [{"ratings":[{"ID":"0","code":"1","name":"G"},{"ID":"2","code":"2","name":"PG20"},{"ID":"6","code":"1","name":"PG13"},{"ID":"16","code":"1","name":"15+"}],"systemName":"DTRatingSystem1"},{"ratings":[{"ID":"1","code":"2","name":"PG10"},{"ID":"3","code":"2","name":"20+"},{"ID":"12","code":"1","name":"PG15"},{"ID":"18","code":"1","name":"18+"}],"systemName":"DTRatingSystem2"}]
     * result : {"retCode":"000000000","retMsg":"Successfully"}
     * extensionFields : ["",""]
     * ISOCodes : ["",""]
     */

    /**
     *

     对应的货币类型字母编码，参考ISO 4217。

     取值范围：
     •SGD：新加坡
     •THB：泰国
     •PHP：菲律宾
     •USD：美国
     •CNY：中国

     */
    @SerializedName("currencyAlphCode")
    private String currencyAlphCode;


    /**
     *货币最小单位与展示单位之间的转换率。

     默认值为100。

     */
    @SerializedName("currencyRate")
    private String currencyRate;
    /**
     * retCode : 000000000
     * retMsg : Successfully
     */

    @SerializedName("result")
    private Result result;
    /**
     * cfgType : 0
     * values : [{"key":"AVSDescriptionUrl","values":["http://www.baidu.com"]},{"key":"ChannelCategoryID","values":["AllChannel"]},{"key":"Default_BeginOffset","values":[null]},{"key":"Default_EndOffset","values":[null]},{"key":"Default_RecCfgDeleteMode","values":[null]},{"key":"Default_RecCfgSingleOrSeries","values":[null]},{"key":"DownloadedBufferLength","values":["45"]},{"key":"ForgetParentPasswordURL","values":["http://www.huawei.com/cn/"]},{"key":"HomeCategoryID","values":["VODMovieRecm"]},{"key":"IMDomain","values":["10"]},{"key":"channel_subject_id","values":["AllChannel"]},{"key":"companionModeUrlForIPad","values":["http://www.huawei.com"]},{"key":"companionModeUrlForIPhone","values":["http://www.huawei.com"]},{"key":"credit_limit_list","values":["-1,100,1000,10000"]},{"key":"currency_symbol","values":["$"]},{"key":"discoveryUrl","values":["http://10.10.12.80:8080"]},{"key":"dropboxForceHttp","values":["1"]},{"key":"enhancedMetaData","values":["1"]},{"key":"enhancedRecommendEngine","values":["1"]},{"key":"familyMemberServiceID4SOL","values":["SOL"]},{"key":"familyMemberServiceID4TINT","values":["TINT"]},{"key":"karakey","values":["http://www.huawei.com/cn/"]},{"key":"RiceUrl","values":["http://10.10.12.80:8080"]},{"key":"SAM3LockTime","values":["70"]},{"key":"SOLSubnetID","values":["SOL"]},{"key":"STBTOPMenuID","values":["mainmenu_epg"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=AndroidPad"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=AndroidPhone"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=Ipad"]},{"key":"ScallingServerURL","values":["http://10.10.12.80:8080/EPG/iss.t-online.de/iss/client=Iphone"]},{"key":"ScallingServerURL","values":[]},{"key":"suggestion_email_dest","values":["diaoyinghu@huawei.com"]},{"key":"vod_subject_id","values":["VODAll"]}]
     */

    @SerializedName("configurations")
    private List<Configuration> configurations;
    /**
     * ID : 1
     * fullName : English
     * name : en
     */

    @SerializedName("languages")
    private List<LanguagesBean> languages;
    /**
     * ratings : [{"ID":"0","code":"1","name":"G"},{"ID":"2","code":"2","name":"PG20"},{"ID":"6","code":"1","name":"PG13"},{"ID":"16","code":"1","name":"15+"}]
     * systemName : DTRatingSystem1
     */

    @SerializedName("ratings")
    private List<RatingSystem> ratings;
    @SerializedName("extensionFields")


    private List<NamedParameter> extensionFields;
    @SerializedName("ISOCodes")
    private List<ISOCode> ISOCodes;

    public static class RatingSystem{
        @SerializedName("systemName")
        private String systemName;
        @SerializedName("ratings")
        private List<Rating> ratings;

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public List<Rating> getRatings() {
            return ratings;
        }

        public void setRatings(List<Rating> ratings) {
            this.ratings = ratings;
        }
    }

    public static class LanguagesBean {
        @SerializedName("ID")
        private String ID;
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("name")
        private String name;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "LanguagesBean{" +
                    "ID='" + ID + '\'' +
                    ", fullName='" + fullName + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public String getCurrencyAlphCode() {
        return currencyAlphCode;
    }

    public void setCurrencyAlphCode(String currencyAlphCode) {
        this.currencyAlphCode = currencyAlphCode;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public List<LanguagesBean> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguagesBean> languages) {
        this.languages = languages;
    }

    public List<RatingSystem> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingSystem> ratings) {
        this.ratings = ratings;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public List<ISOCode> getISOCodes() {
        return ISOCodes;
    }

    public void setISOCodes(List<ISOCode> ISOCodes) {
        this.ISOCodes = ISOCodes;
    }

    @Override
    public String toString() {
        return "QueryCustomizeConfigResponse{" +
                "currencyAlphCode='" + currencyAlphCode + '\'' +
                ", currencyRate='" + currencyRate + '\'' +
                ", result=" + result +
                ", configurations=" + configurations +
                ", languages=" + languages +
                ", ratings=" + ratings +
                ", extensionFields=" + extensionFields +
                ", ISOCodes=" + ISOCodes +
                '}';
    }
}
