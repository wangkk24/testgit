package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.cloudplayer.sdk.HuaweiCloudMultiPlayer;
import com.huawei.cloudplayer.sdk.HuaweiCloudPlayer;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.BookMarkSwitchs;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.presenter.BookmarkEvent;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：panjw on 2021/6/28 14:40
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class ReportBookmarkForMultUtils {

    private static final String TAG = ReportBookmarkForMultUtils.class.getSimpleName();
    private DetailPresenter mDetailPresenter;
    private int tryToSeeFlag = 0;
    private PlayVodBean playVodBean;
    private String sitcomNO;
    private String subContentId;
    private boolean isComplete;
    private long currentTimes;

    public ReportBookmarkForMultUtils(DetailPresenter mDetailPresenter) {
        this.mDetailPresenter = mDetailPresenter;
        playVodBean = new PlayVodBean();
    }

//    public void reportBookmark(String reportType, HuaweiCloudPlayer player, String videoId, String vodType, long currentTimes, String isFilm, String sitcomNO, String subContentId, long totalTimes, boolean isComplete, VODDetail mVODDetail) {
    public void reportBookmark(String reportType, String videoId, String vodType, long currentTimes, String isFilm, String sitcomNO, String subContentId, long totalTimes, boolean isComplete, VODDetail mVODDetail) {
        this.sitcomNO = sitcomNO;
        this.subContentId = subContentId;
        this.currentTimes = currentTimes;
        this.isComplete = isComplete;
        String switchs = SessionService.getInstance().getSession().getTerminalConfigurationValue("add_bookmark_switchs");
        BookMarkSwitchs bookMarkSwitchs = null;
        if (!TextUtils.isEmpty(switchs)) {
            bookMarkSwitchs = JsonParse.json2Object(switchs, BookMarkSwitchs.class);
        }

        if (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT) || (bookMarkSwitchs != null && TextUtils.equals("0", bookMarkSwitchs.getBookmarkSwitchsValue(reportType)))) {
            //彻底收回播放器资源
            if (!PlayerAttriUtil.isEmpty(videoId) && !TextUtils.isEmpty(vodType)) {
                long currentSecond = 0;
                if (!isComplete) {
                    currentSecond = currentTimes / 1000;
                }
                Bookmark bookMarkItem = new Bookmark();
                bookMarkItem.setBookmarkType(vodType);
                bookMarkItem.setItemID(videoId);
                Log.i(TAG, "1reportBookmark: currentSecond");
                bookMarkItem.setRangeTime(currentSecond + "");
                // 非0的时候是电视剧
                if (!TextUtils.isEmpty(isFilm)) {
                    if (!isFilm.equals("0")) {
                        bookMarkItem.setSubContentID(subContentId);
                        bookMarkItem.setSubContentType("VOD");
                        bookMarkItem.setSitcomNO(sitcomNO);
                    }
                }
                ArrayList<Bookmark> bookmarks = new ArrayList<>();
                bookmarks.add(bookMarkItem);
                createBookmark(bookmarks, mVODDetail, isFilm, sitcomNO);
            }

        }
        if (TextUtils.equals(reportType, BookMarkSwitchs.START)) {
            //增加切集时，刷新详情页子集列表
            playVodBean.setSitcomNO(sitcomNO);
            playVodBean.setEpisodeId(subContentId);
            SuperLog.debug(TAG, "event send Bookmark refresh");
//            if (null != mActivity && !mActivity.isFinishing()) {
            Log.i(TAG, "定位坚果点击剧集卡顿 reportBookmark: ");
            EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
        }
    }

    public void createBookmark(ArrayList<Bookmark> bookmarks, VODDetail mVODDetail, String isFilm, String sitcomNO) {
        CreateBookmarkRequest request = new CreateBookmarkRequest();
        List<String> str = new ArrayList<>();
        str.add("1");
        NamedParameter np = new NamedParameter("playFromTerminal", str);
        NamedParameter childNp = new NamedParameter(Constant.BOOKMARK_CHILD_MODE, str);
        List<NamedParameter> customFields = new ArrayList<>();
        customFields.add(np);
        if (VoddetailUtil.getInstance().isChildVod(mVODDetail)) {
            customFields.add(childNp);
        }

        // 非0的时候是电视剧
        if (!TextUtils.isEmpty(isFilm)) {
            if (!isFilm.equals("0")) {
                List<String> bookmark = new ArrayList<>();

                bookmark.add(sitcomNO);
                NamedParameter bookmarkNp = new NamedParameter(Constant.DETAIL_ZJ_BOOKMARK, bookmark);
                customFields.add(bookmarkNp);
            }
        }

        for (Bookmark bookmark : bookmarks) {
            bookmark.setCustomFields(customFields);
        }
        request.setBookmarks(bookmarks);
        mDetailPresenter.createBookmark(request, createBookmarkCallBack);
    }

    CreateBookmarkCallBack createBookmarkCallBack = new CreateBookmarkCallBack() {
        @Override
        public void createBookmarkSuccess() {
            SuperLog.debug(TAG, "createBookmarkSuccess");
            if (playVodBean != null && tryToSeeFlag == 0) {
                playVodBean.setSitcomNO(sitcomNO);
                playVodBean.setEpisodeId(subContentId);
                if (currentTimes != 0) {
                    long currentSecond = 0;
                    if (!isComplete) {
                        currentSecond = currentTimes / 1000;
                    }
                    playVodBean.setBookmark(currentSecond + "");
                }
                SuperLog.debug(TAG, "event send Bookmark refresh");
                Log.i(TAG, "createBookmarkSuccess:  " + sitcomNO + " " + subContentId + " " + playVodBean.getBookmark());
                EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));

            }

        }

        @Override
        public void createBookmarkFail() {
            SuperLog.error(TAG, "createBookmarkFail");
        }
    };
}
