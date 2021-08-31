package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public interface QueryBookmarkCallBack extends V6CallBack {
    void queryBookmarkSuccess(int total,List<BookmarkItem> bookmarks);
    void queryBookmarkFail();
}
