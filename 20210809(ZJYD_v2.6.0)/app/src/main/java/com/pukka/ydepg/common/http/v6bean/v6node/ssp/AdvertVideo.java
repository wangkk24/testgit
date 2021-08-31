package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertVideo {
    @SerializedName("curl")
    private String curl;

    @SerializedName("events")
    private List<Event> events;

    public String getCurl()
    {
        return curl;
    }

    public void setCurl(String curl)
    {
        this.curl = curl;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
