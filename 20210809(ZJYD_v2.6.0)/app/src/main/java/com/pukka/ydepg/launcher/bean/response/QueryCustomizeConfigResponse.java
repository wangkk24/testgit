package com.pukka.ydepg.launcher.bean.response;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.bean.node.Language;
import com.pukka.ydepg.common.http.v6bean.v6node.ISOCode;
import com.pukka.ydepg.common.http.v6bean.v6node.Rating;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.OTTFormat;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * GetCustomizeConfig response body
 *
 */
public class QueryCustomizeConfigResponse extends BaseResponse
{
    private static final String TAG = QueryCustomizeConfigResponse.class.getSimpleName();
    /**
     * field:configurationList
     */
    @SerializedName("configurations")
    private List<Configuration> configurationList;

    /**
     * field:ratingList
     */
    @SerializedName("ratings")
    private List<RatingSystem> ratingSystemList;

    /**
     * field:languageList
     */
    @SerializedName("languages")
    private List<Language> languageList;

    /**
     * field:ISOCodeList
     */
    @SerializedName("ISOCodes")
    private List<ISOCode> isoCodeList;

    /**
     * field:currencyAlphCode
     */
    @SerializedName("currencyAlphCode")
    private String currencyAlphabetCode;

    /**
     * field:currencyRate
     */
    @SerializedName("currencyRate")
    private String currencyRate;

    /**
     * field:currencyRate
     */
    @SerializedName("Genre")
    private String Genre;

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public List<Configuration> getConfigurationList()
    {
        return configurationList;
    }

    public void setConfigurationList(List<Configuration> configurationList)
    {
        this.configurationList = configurationList;
    }

    public List<RatingSystem> getRatingSystemList()
    {
        if (null == ratingSystemList || ratingSystemList.isEmpty())
        {

        }
        return ratingSystemList;
    }

    public void setRatingSystemList(List<RatingSystem> ratingSystemList)
    {
        this.ratingSystemList = ratingSystemList;
    }

    public List<Language> getLanguageList()
    {
        if (null == languageList || languageList.isEmpty())
        {

        }
        return languageList;
    }

    public void setLanguageList(List<Language> languageList)
    {
        this.languageList = languageList;
    }

    public List<ISOCode> getISOCodeList()
    {
        return isoCodeList;
    }

    public void setISOCodeList(List<ISOCode> isoCodeList)
    {
        this.isoCodeList = isoCodeList;
    }

    public String getCurrencyAlphabetCode()
    {
        return currencyAlphabetCode;
    }

    public void setCurrencyAlphabetCode(String currencyAlphabetCode)
    {
        this.currencyAlphabetCode = currencyAlphabetCode;
    }

    public String getCurrencyRate()
    {
        if(TextUtils.isEmpty(currencyRate) || 0 >= OTTFormat.convertInt(currencyRate))
        {

        }
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate)
    {
        this.currencyRate = currencyRate;
    }

    @Override
    public String toString()
    {
        return "QueryCustomizeConfigResponse{" +
                "configurationList=" + configurationList +
                ", ratingSystemList=" + ratingSystemList +
                ", languageList=" + languageList +
                ", isoCodeList=" + isoCodeList +
                ", currencyAlphabetCode='" + currencyAlphabetCode + '\'' +
                ", currencyRate='" + currencyRate + '\'' +
                '}';
    }
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
}