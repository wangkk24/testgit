package com.pukka.ydepg.moudule.vod.presenter;

/**
 * Created by liudo on 2019/4/16.
 */

public class DeblockingEvent
{

    private String type;

    public DeblockingEvent(String type){
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
