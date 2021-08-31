package com.pukka.ydepg.common.http.v6bean.v6node;


import android.text.TextUtils;

import com.pukka.ydepg.common.utils.OTTFormat;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Launcher
{
    @SerializedName("version")
    private String version;
    @SerializedName("pageSwitchMode")
    private String pageSwitchMode;
    @SerializedName("defaultBackground")
    private String defaultBackground;
    @SerializedName("logo")
    private String logo;
    @SerializedName("groups")
    private List<Group> groupList;
    @SerializedName("shortcuts")
    private List<Shortcut> shortcuts;
    @SerializedName("additionElements")
    private List<Element> additionElement;
    @SerializedName("navs")
    private List<Navigate> navigateList;
    @SerializedName("deskTopName")
    private List<Dialect> dialect;
    @SerializedName("deskTopType")
    private String deskTopType;
    @SerializedName("desktopResolution")
    private String desktopResolution;
    @SerializedName("extraData")
    private Map<String,String> extraData;


    public Map<String, String> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, String> extraData) {
        this.extraData = extraData;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getPageSwitchMode()
    {
        return pageSwitchMode;
    }

    public void setPageSwitchMode(String pageSwitchMode)
    {
        this.pageSwitchMode = pageSwitchMode;
    }

    public String getDefaultBackground()
    {
        return defaultBackground;
    }

    public void setDefaultBackground(String defaultBackground)
    {
        this.defaultBackground = defaultBackground;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    /**
     * sort by group id.
     *
     * @return
     */
    public List<Group> getGroupList()
    {
        if (null != groupList)
        {


            Collections.sort(groupList, new Comparator<Group>()
            {
                @Override
                public int compare(Group lhs, Group rhs)
                {
                    return OTTFormat.convertInt(lhs.getIndex()) - OTTFormat.convertInt(rhs.getIndex());
                }
            });
        }
        return groupList;
    }

    public void setGroups(List<Group> groupList)
    {
        this.groupList = groupList;
    }

    public List<Shortcut> getShortcuts()
    {
        return shortcuts;
    }

    public void setShortcuts(List<Shortcut> shortcuts)
    {
        this.shortcuts = shortcuts;
    }

    public List<Element> getAdditionElement()
    {
        return additionElement;
    }

    public void setAdditionElement(List<Element> additionElement)
    {
        this.additionElement = additionElement;
    }


    public List<Navigate> getNavigateList() {
        //inde从大到小排序
        Collections.sort(navigateList, new Comparator<Navigate>() {
                @Override
                public int compare(Navigate o1, Navigate o2) {
                    if (null == o1 && o2 != null) {
                        return 1;
                    }
                    if (null != o1 && null == o2) {
                        return -1;
                    }
                    if (null == o1 && null == o2) {
                        return 0;
                    }
                    if (TextUtils.isEmpty(o1.getIndex()) && TextUtils.isEmpty(o2.getIndex())) {
                        return 0;
                    }
                    if (TextUtils.isEmpty(o1.getIndex()) && !TextUtils.isEmpty(o2.getIndex())) {
                        return 1;
                    }
                    if (!TextUtils.isEmpty(o1.getIndex()) && TextUtils.isEmpty(o2.getIndex())) {
                        return -1;
                    }
                    int o1No = Integer.parseInt(o1.getIndex());
                    int o2No = Integer.parseInt(o2.getIndex());
                    if (o1No < o2No) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

        return navigateList;
    }

    public void setNavigateList(List<Navigate> navigateList) { this.navigateList = navigateList;}

    public List<Dialect> getDialect()
    {
        return dialect;
    }

    public void setDialect(List<Dialect> dialect)
    {
        this.dialect = dialect;
    }

    public String getDeskTopType()
    {
        return deskTopType;
    }

    public void setDeskTopType(String deskTopType)
    {
        this.deskTopType = deskTopType;
    }

    public String getDesktopResolution()
    {
        return desktopResolution;
    }

    public void setDesktopResolution(String desktopResolution)
    {
        this.desktopResolution = desktopResolution;
    }
}