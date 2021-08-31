package com.pukka.ydepg.event;

import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;

import java.util.List;

public class DeleteHistoryEvent {
    List<BookmarkItem> bookmarkItems;
    boolean isDeleteAll;
    //删除时的焦点位置
    int section;
    int postion;

    public DeleteHistoryEvent(List<BookmarkItem> bookmarkItems, boolean isDeleteAll, int section, int postion) {
        this.bookmarkItems = bookmarkItems;
        this.isDeleteAll = isDeleteAll;
        this.section = section;
        this.postion = postion;
    }

    public List<BookmarkItem> getBookmarkItems() {
        return bookmarkItems;
    }

    public boolean isDeleteAll() {
        return isDeleteAll;
    }

    public int getSection() {
        return section;
    }

    public int getPostion() {
        return postion;
    }
}
