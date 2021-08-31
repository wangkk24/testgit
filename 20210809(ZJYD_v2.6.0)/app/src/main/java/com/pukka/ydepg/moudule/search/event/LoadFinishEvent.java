package com.pukka.ydepg.moudule.search.event;

/**
 * Created by liudo on 2018/5/24.
 */
public class LoadFinishEvent {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LoadFinishEvent(String key){
        this.key=key;
    }
}