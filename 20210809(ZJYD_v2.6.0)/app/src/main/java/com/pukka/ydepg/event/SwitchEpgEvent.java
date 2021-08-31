package com.pukka.ydepg.event;

import com.pukka.ydepg.common.constant.Constant.*;

/**
 * Created by Eason on 10-Jun-19.
 * 接收XMPP消息切换桌面
 */
public class SwitchEpgEvent {

    //切换type =0:EPG;=1:简版EPG；=2：少儿动漫
    private DesktopType type = DesktopType.NORMAL;

    public DesktopType getType() {
        return type;
    }

    public void setType(DesktopType type) {
        this.type = type;
    }
}