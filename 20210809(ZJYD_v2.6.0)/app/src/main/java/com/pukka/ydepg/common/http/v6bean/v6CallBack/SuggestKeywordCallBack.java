package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public interface SuggestKeywordCallBack extends V6CallBack {
    void suggestKeywordSuccess(List<String> suggests);
    void suggestKeywordFail();
}
