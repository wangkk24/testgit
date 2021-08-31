package com.pukka.ydepg.event;

/**
 * Created by Eason on 2018/5/18.
 * BaseActivity弹框
 *
 */
public class MessageDialogEvent {

    private boolean isShow = true;
    private String body = "";
    private boolean canToShow = false;
    private boolean isToOtherPage = false;

    public boolean isToOtherPage() {
        return isToOtherPage;
    }

    public void setToOtherPage(boolean toOtherPage) {
        isToOtherPage = toOtherPage;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isCanToShow() {
        return canToShow;
    }

    public void setCanToShow(boolean canToShow) {
        this.canToShow = canToShow;
    }
}
