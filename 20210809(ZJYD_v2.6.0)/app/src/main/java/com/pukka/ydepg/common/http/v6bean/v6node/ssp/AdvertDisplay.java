package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import java.util.List;

public class AdvertDisplay {
    private String mime;

    private String api;

    private String type;

    private int width;

    private int height;

    private int wratio;

    private int hratio;

    private String adm;

    private String  curl;

    private AdvertBanner banner;

    private List<Event> events;

    private String ext;

    public String getMime()
    {
        return mime;
    }

    public void setMime(String mime)
    {
        this.mime = mime;
    }

    public String getApi()
    {
        return api;
    }

    public void setApi(String api)
    {
        this.api = api;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWratio()
    {
        return wratio;
    }

    public void setWratio(int wratio)
    {
        this.wratio = wratio;
    }

    public int getHratio()
    {
        return hratio;
    }

    public void setHratio(int hratio)
    {
        this.hratio = hratio;
    }

    public String getAdm()
    {
        return adm;
    }

    public void setAdm(String adm)
    {
        this.adm = adm;
    }

    public String getCurl()
    {
        return curl;
    }

    public void setCurl(String curl)
    {
        this.curl = curl;
    }

    public AdvertBanner getBanner() {
        return banner;
    }

    public void setBanner(AdvertBanner banner)
    {
        this.banner = banner;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }
}
