package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public interface SearchHotKeyCallBack extends V6CallBack {
    void searchHotKeySuccess(int total,List<String> hotKeys);

    void searchHotKeyFail();

    void searchHotKeyCancle();
}
