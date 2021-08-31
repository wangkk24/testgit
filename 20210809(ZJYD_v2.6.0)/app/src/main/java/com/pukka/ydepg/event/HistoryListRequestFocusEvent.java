package com.pukka.ydepg.event;

public class HistoryListRequestFocusEvent {

    //落焦到Activity中左侧标签（观看记录，收藏）,给NewMyMovieActivity接收
    public static final String REQUEST_FOCUS_ACTIVITY = "0";

    //落焦到Fragment中右侧清空按钮处, 给NewHistoryListFragment接收
    public  static final String REQUEST_FOCUS_LIST_FRAGMENT = "1";

    //落焦到当前标签（宽带电视，咪咕爱看）处，给NewHistoryFragment接收
    public  static final String REQUEST_FOCUS_FRAGMENT = "2";

    String ViewNeedFocus;

    //记录焦点移动到删除按钮前的焦点位置
    int num;

    public HistoryListRequestFocusEvent(String viewNeedFocus) {
        ViewNeedFocus = viewNeedFocus;
    }

    public String getViewNeedFocus() {
        return ViewNeedFocus;
    }

    public void setViewNeedFocus(String viewNeedFocus) {
        ViewNeedFocus = viewNeedFocus;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
