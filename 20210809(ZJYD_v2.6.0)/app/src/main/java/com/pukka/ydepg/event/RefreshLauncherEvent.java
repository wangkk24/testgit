package com.pukka.ydepg.event;

/**
 * Created by Eason on 12/13/2018.
 * 刷新Launcher
 */

public class RefreshLauncherEvent {

    //true:执行首页刷新  false:执行二级页面刷新
    private boolean isMainActivity = true;

    public RefreshLauncherEvent(boolean isMainActivity) {
        this.isMainActivity = isMainActivity;
    }

    public boolean isMainActivity() {
        return isMainActivity;
    }
}
