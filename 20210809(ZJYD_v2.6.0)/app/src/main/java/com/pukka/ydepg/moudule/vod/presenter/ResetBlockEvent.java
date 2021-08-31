package com.pukka.ydepg.moudule.vod.presenter;

public class ResetBlockEvent
{

    private String type;

    public ResetBlockEvent(String type){
        this.type=type;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
