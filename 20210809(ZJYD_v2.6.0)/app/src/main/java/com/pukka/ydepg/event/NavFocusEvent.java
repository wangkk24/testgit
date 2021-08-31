package com.pukka.ydepg.event;

import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;

/**
 * 设置导航栏落焦
 *
 * @FileName: com.pukka.ydepg.event.NavFocusEvent.java
 * @author: luwm
 * @data: 2018-07-06 11:55
 * @Version V1.0 <描述当前版本功能>
 */
public class NavFocusEvent {
    private int index;
    public NavFocusEvent(int position){
        this.index = position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
