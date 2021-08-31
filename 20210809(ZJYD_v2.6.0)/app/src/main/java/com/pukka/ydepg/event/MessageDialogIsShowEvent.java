package com.pukka.ydepg.event;

/**
 * Created by Eason on 2018/5/18.
 * 推送弹框是否正在显示
 *
 */

public class MessageDialogIsShowEvent {

    private boolean isShow = true;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
