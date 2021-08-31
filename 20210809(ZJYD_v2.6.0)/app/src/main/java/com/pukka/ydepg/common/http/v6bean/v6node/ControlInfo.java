package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

/**
 */

public class ControlInfo
{

    /**
     * controlID : Banner
     * showRefresh : 0
     * showMore : 0
     */

    @SerializedName("controlID")
    private String controlId;

    /**
     * the range of showRefresh
     */
    public interface ControlId
    {
        String BANNER = "Banner";
        String HCList = "HCList";
        String PList = "PList";
        String HList = "HList";
        String FixedList = "FixedList";
        String Topic = "Topic";
        String Metro = "Metro";
        String OList = "OList";
        String SList = "SList";
        String DisList = "DisList";
        String FilterList = "FilterList";
        String DisItemList = "DisItemList";
        String DisMoreList = "DisMoreList";
        String VScrollList = "VScrollList";
        String HScrollList = "HScrollList";
        String SpecialList = "SpecialList";
        String PSixList = "PSixList";
    }

    @SerializedName("showRefresh")
    private String showRefresh;

    /**
     * the range of showRefresh
     */
    public interface ShowRefresh
    {
        String REFRESH = "1";
        String NOT_REFRESH = "0";
    }

    @SerializedName("showMore")
    private String showMore;
    @SerializedName("pageId")
    private String pageId;


    /**
     * the range of showMore
     */
    public interface ShowMore
    {
        String MORE = "1";
        String NOT_MORE = "0";
    }

    public String getControlId() { return controlId;}

    public void setControlId(String controlId) { this.controlId = controlId;}

    public String getShowRefresh() { return showRefresh;}

    public void setShowRefresh(String showRefresh) { this.showRefresh = showRefresh;}

    public String getShowMore() { return showMore;}

    public void setShowMore(String showMore) { this.showMore = showMore;}

    public String getPageId()
    {
        return pageId;
    }

    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }

}