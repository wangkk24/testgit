package com.pukka.ydepg.launcher.bean.node;

/**
 * Created by liudo on 2018/11/26.
 */
public class ForceLifecycleEvent {
    public static final int RESUME = 111111;

    public static final int PAUSE=222222;

    private int state;

    public int getState()
    {
        return state;
    }

    public ForceLifecycleEvent(int state){
        this.state=state;
    }
}