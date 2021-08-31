package com.pukka.ydepg.moudule.mytv.presenter;

import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PersonalizedController;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.moudule.mytv.presenter.view.HistoryDataView;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.presenter.HistoryPresenter.java
 * @author:xj
 * @date: 2018-01-26 11:09
 */

public class HistoryPresenter extends BasePresenter implements QueryBookmarkCallBack, DeleteBookmarkCallBack {
    private RxAppCompatActivity rxAppCompatActivity;
    /**
     * 查询书签控制器
     */
    PersonalizedController mPersonalizedController;
    private HistoryDataView mDataView;

    public HistoryPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mPersonalizedController = new PersonalizedController(rxAppCompatActivity);
    }
    public void setDataView(HistoryDataView dataView){
        mDataView = dataView;
    }
    public void  queryBookmark(int count ,int offset){
        QueryBookmarkRequest request = new QueryBookmarkRequest();
        request.setCount(count+"");
        request.setOffset(offset+"");
        mPersonalizedController.queryBookmark(request,this,compose(rxAppCompatActivity.bindToLifecycle()));
    }

    public void deleteBookmark(List<BookmarkItem> bookmarks, boolean deleteAll){
        DeleteBookmarkRequest request = new DeleteBookmarkRequest();
        List<String> typeList = new ArrayList<>();
        if (deleteAll && !SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            typeList.add(BookmarkItem.BookmarkType.VOD);
        } else {
            List<String> idList = new ArrayList<>();
            for (int i = 0; i < bookmarks.size(); i++) {
                BookmarkItem bookmarkItem = bookmarks.get(i);
                if (!TextUtils.isEmpty(bookmarkItem.getBookmarkType())) {
                    typeList.add(bookmarkItem.getBookmarkType());
                }
                if (null != bookmarkItem.getVOD()) {
                    if(bookmarkItem.getVOD().getID()==null)
                    {
                        idList.remove(bookmarkItem.getVOD().getID());
                    }else {
                        idList.add(bookmarkItem.getVOD().getID());
                    }

                }
            }
            request.setItemIDs(idList);
        }
        request.setBookmarkTypes(typeList);
        mPersonalizedController.deleteBookMark(request,this,compose(rxAppCompatActivity.bindToLifecycle()));
    }

    @Override
    public void queryBookmarkSuccess(int total, List<BookmarkItem> bookmarks) {

        if (null != mDataView){
            mDataView.queryBookmarkSuccess(total,bookmarks);
        }

    }

    @Override
    public void queryBookmarkFail() {
        if (null != mDataView){
            mDataView.queryBookmarkFail();
        }
    }

    @Override
    public void deleteBookmarkSuccess() {
        if (null != mDataView){
            mDataView.deleteBookmarkSuccess();
        }
    }

    @Override
    public void deleteBookmarkFail() {
        if (null != mDataView){
            mDataView.deleteBookmarkFail();
        }
    }
}
