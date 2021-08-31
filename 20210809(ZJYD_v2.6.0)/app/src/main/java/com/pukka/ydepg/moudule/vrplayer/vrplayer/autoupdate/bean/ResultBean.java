package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean;


public class ResultBean {
    private int stateCode;
    private String body;

    public void setBody(String body) {
        this.body = body;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "stateCode:"+this.getStateCode()+",body:"+this.getBody();
    }
}
