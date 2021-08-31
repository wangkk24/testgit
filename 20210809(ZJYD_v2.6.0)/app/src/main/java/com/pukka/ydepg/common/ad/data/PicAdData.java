package com.pukka.ydepg.common.ad.data;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;

public class PicAdData {

    private AdvertContent content;

    private String clickUrl;

    public PicAdData(AdvertContent content, String clickUrl) {
        this.content = content;
        this.clickUrl = clickUrl;
    }

    public AdvertContent getContent() {
        return content;
    }

    public void setContent(AdvertContent content) {
        this.content = content;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }
}