package com.pukka.ydepg.moudule.search.view;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;

import java.util.List;

/**
 * 搜索结果内容回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.search.view.SearchResultDataView.java
 * @author:xj
 * @date: 2018-01-23 15:28
 */

public interface SearchResultDataView {
    void searchContentSuccess(int total, List<Content> contents,String key);
    void searchContentFail(String key);
}
