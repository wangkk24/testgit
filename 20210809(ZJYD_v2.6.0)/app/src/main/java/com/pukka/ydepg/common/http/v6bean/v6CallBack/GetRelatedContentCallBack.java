package com.pukka.ydepg.common.http.v6bean.v6CallBack;

import com.pukka.ydepg.launcher.ui.reminder.beans.RelatedContent;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 */

public interface GetRelatedContentCallBack {
    /*查询固移融合关联内容接口成功*/
    void getRelatedContentSuccess(RelatedContent[] contents);

    /*查询固移融合关联内容接口失败*/
    void getRelatedContentFail();
}
