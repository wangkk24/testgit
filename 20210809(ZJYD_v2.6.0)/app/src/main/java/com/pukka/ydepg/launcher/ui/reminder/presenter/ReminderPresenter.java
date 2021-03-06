package com.pukka.ydepg.launcher.ui.reminder.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.common.http.v6bean.v6CallBack.GetRelatedContentCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PersonalizedController;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.GetRelatedContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.reminder.beans.RelatedContent;
import com.pukka.ydepg.launcher.ui.reminder.beans.ReminderMessage;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.pukka.ydepg.common.utils.IsTV.isTV;
import static com.pukka.ydepg.common.utils.JsonParse.object2String;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 */

public class ReminderPresenter extends BasePresenter implements QueryBookmarkCallBack, GetRelatedContentCallBack, DetailDataView {
    private static final String TAG = ReminderPresenter.class.getName();
    private RxAppCompatActivity rxAppCompatActivity;
    private PersonalizedController mPersonalizedController;
    private String contentID;
    private DetailPresenter mDetailPresenter;
    private Bookmark bookmark;
    private String vodName;

    public ReminderPresenter(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        mPersonalizedController = new PersonalizedController(rxAppCompatActivity);
        mDetailPresenter = new DetailPresenter(rxAppCompatActivity);
        mDetailPresenter.setDetailDataView(this);
    }

    public void checkReminderRelatedContent(int count, int offset) {
        QueryBookmarkRequest request = new QueryBookmarkRequest();
        request.setCount(count + "");
        request.setOffset(offset + "");
        mPersonalizedController.queryBookmark(request, this, compose(rxAppCompatActivity.bindToLifecycle()));
    }

    @Override
    public void queryBookmarkSuccess(int total, List<BookmarkItem> bookmarks) {
        /*???????????????????????????*/
        VOD vod = bookmarks.get(0).getVOD();
        /*???????????????VOD???????????????????????????*/
        SuperLog.info2SD(TAG, "????????????????????????ID???" + vod.getID() + " ???????????????" + vod.getName() + "??????????????????????????????" + !isTV(vod));
        contentID = vod.getID();
        bookmark = vod.getBookmark();
        vodName = vod.getName();
        if (!isTV(vod)) {
            GetRelatedContentRequest getRelatedContentRequest = new GetRelatedContentRequest();
            getRelatedContentRequest.setContentID(contentID);
            /*?????????????????????VOD??????*/
            getRelatedContentRequest.setContentType("VOD");
            mPersonalizedController.getRelatedContent(getRelatedContentRequest, this, compose(rxAppCompatActivity.bindToLifecycle()));
        }
    }

    @Override
    public void queryBookmarkFail() {

    }

    @Override
    public void getRelatedContentSuccess(RelatedContent[] contents) {
        List<String> contentIDs = new ArrayList<>();
        for (RelatedContent content : contents) {
            if (content != null && content.getVOD() != null && content.getVOD().getID() != null) {
                contentIDs.add(content.getVOD().getID());
            }
        }
        SuperLog.info2SD(TAG, "???????????????????????????" + contentIDs.toString());
        if (isRelatedContent(contentIDs)) {
            PushMessagePresenter.setReminderMessage(object2String(new ReminderMessage(contentID, vodName, bookmark.getSitcomNO())));
            SuperLog.info2SD(TAG, "??????????????????ID???" + contentID + " ???????????????" + vodName + " ??????????????????");
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param contentIDs ?????????VOD ID
     * @return boolean ?????????????????????????????????true???????????????false
     */
    private boolean isRelatedContent(List<String> contentIDs) {
        if (!contentIDs.isEmpty()) {
            if (contentIDs.contains(contentID)) {
                contentIDs.remove(contentID);
                return !contentIDs.isEmpty();
            }
            return !contentIDs.isEmpty();
        }
        return false;
    }

    public void playBookmark(String contentID) {
        if (mDetailPresenter != null) {
            mDetailPresenter.getVODDetail(contentID);
        }
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void showNoContent() { }

    @Override
    public void showError(String message) { }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        mDetailPresenter.setButtonOrderOrSee(true);
        mDetailPresenter.setVODDetail(vodDetail);
        if ("0".equals(vodDetail.getVODType())) {
            mDetailPresenter.playVOD(vodDetail);
        } else {
            List<Episode> episodes = vodDetail.getEpisodes();
            Bookmark bookmark = vodDetail.getBookmark();
            if (bookmark != null) {
                SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
            }
            if (episodes != null && !episodes.isEmpty()) {
                Episode playEpisode = null;
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    for (Episode episode : episodes) {
                        if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                            playEpisode = episode;
                        }
                    }
                } else {
                    playEpisode = episodes.get(0);
                }
                if (null != playEpisode){
                    mDetailPresenter.playVOD(playEpisode);
                }
            } else {
                SuperLog.debug(TAG, "Episodes is null or episodes is Empty");
            }
        }
    }

    @Override
    public void showCollection(boolean isCollection) { }

    @Override
    public void setNewScore(List<Float> newScore) { }

    @Override
    public void showContentNotExit() { }

    @Override
    public void getRelatedContentFail() { }
}