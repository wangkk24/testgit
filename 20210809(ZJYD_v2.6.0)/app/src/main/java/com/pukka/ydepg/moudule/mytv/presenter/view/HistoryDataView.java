package com.pukka.ydepg.moudule.mytv.presenter.view;

import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.view.HistoryDataView.java
 * @author:xj
 * @date: 2018-01-26 11:26
 */

public interface HistoryDataView {

     void queryBookmarkSuccess(int total, List<BookmarkItem> bookmarks) ;

     void queryBookmarkFail();

    void deleteBookmarkSuccess();

    void deleteBookmarkFail();
}
