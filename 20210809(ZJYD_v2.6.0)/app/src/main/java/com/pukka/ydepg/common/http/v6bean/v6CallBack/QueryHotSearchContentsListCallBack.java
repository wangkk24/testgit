package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;

import java.util.List;

/**
 * 热搜内容查询回调
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryHotSearchContentsListCallBack.java
 * @author:xj
 * @date: 2017-12-18 15:08
 */

public interface QueryHotSearchContentsListCallBack extends V6CallBack {
    void queryHotContentListSuccess(List<Content> contents);
    void queryHotContentListFailed();
    void queryHotContentListCancel();
}
