package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

public class AdvertBanner {
    private String img;

    private LinkAsset link;

    private String ext;

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public LinkAsset getLink() {
        return link;
    }

    public void setLink(LinkAsset link)
    {
        this.link = link;
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