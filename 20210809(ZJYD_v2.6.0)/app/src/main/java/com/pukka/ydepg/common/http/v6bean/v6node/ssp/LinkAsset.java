package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import java.util.List;

public class LinkAsset {
    private String url;

    private String urlfb;

    private List<String> trkr;

    private String ext;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrlfb()
    {
        return urlfb;
    }

    public void setUrlfb(String urlfb)
    {
        this.urlfb = urlfb;
    }

    public List<String> getTrkr()
    {
        return trkr;
    }

    public void setTrkr(List<String> trkr)
    {
        this.trkr = trkr;
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