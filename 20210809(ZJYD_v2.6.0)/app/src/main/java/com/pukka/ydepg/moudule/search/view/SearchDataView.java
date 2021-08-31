package com.pukka.ydepg.moudule.search.view;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.search.view.SearchDataView.java
 * @author:xj
 * @date: 2018-01-23 15:17
 */

public interface SearchDataView {
    void searchHotSuccess(int total, List<String> hotKeys);
    void searchHotKeyFail();
    void queryHotContentListSuccess(List<Content> contents);
    void queryHotContentListFailed();
}
