package com.pukka.ydepg.event;

public class OnDemandBackEvent {
    private String url;
    private String id;

    public OnDemandBackEvent(String url, String id){
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
