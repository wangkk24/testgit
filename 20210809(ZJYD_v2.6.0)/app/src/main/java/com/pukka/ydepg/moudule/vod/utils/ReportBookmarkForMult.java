package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;
import android.util.Log;

import com.huawei.cloudplayer.sdk.HuaweiCloudMultiPlayer;
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
public class ReportBookmarkForMult {

    private static final String TAG = ReportBookmarkForMult.class.getSimpleName();
    private long currentTimes;
    private String isFilm;
    private DetailPresenter mDetailPresenter;
    private String sitcomNO;
    private String subContentId;
    private int tryToSeeFlag=0;
    private int totalTimes;
    private boolean isComplete;
    private VODDetail mVODDetail;
    private PlayVodBean playVodBean;

    public ReportBookmarkForMult(long currentTimes, String isFilm, DetailPresenter mDetailPresenter, String sitcomNO, String subContentId, int totalTimes, boolean isComplete, VODDetail mVODDetail, PlayVodBean playVodBean) {
        this.currentTimes = currentTimes;
        this.isFilm = isFilm;
        this.mDetailPresenter = mDetailPresenter;
        this.sitcomNO = sitcomNO;
        this.subContentId = subContentId;
        this.totalTimes = totalTimes;
        this.isComplete = isComplete;
        this.mVODDetail = mVODDetail;
        this.playVodBean = playVodBean;
    }

    public void reportBookmark(String reportType, HuaweiCloudMultiPlayer player, String videoId, PlayVodBean playVodBean) {

//        if (isSwitchVOD) {
//            return;
//        }

        Log.i(TAG, "reportBookmark:  " + reportType);
        if (player != null) {
            currentTimes = player.getCurrentPosition();
        }
        String switchs = SessionService.getInstance().getSession().getTerminalConfigurationValue("add_bookmark_switchs");
        BookMarkSwitchs bookMarkSwitchs = null;
        if (!TextUtils.isEmpty(switchs)) {
            bookMarkSwitchs = JsonParse.json2Object(switchs, BookMarkSwitchs.class);
        }

        if (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT) || (bookMarkSwitchs != null && TextUtils.equals("0", bookMarkSwitchs.getBookmarkSwitchsValue(reportType)))) {
            //彻底收回播放器资源
            if (!PlayerAttriUtil.isEmpty(videoId) && playVodBean != null && !PlayerAttriUtil.isEmpty(playVodBean.getVodType())) {
                long currentSecond = 0;
                if (!isComplete) {
                    currentSecond = currentTimes / 1000;
                }

                Log.d(TAG, "bookmark episodeId:" + playVodBean.getEpisodeId() + "|currentTimes:" + currentTimes + "|totalTimes:" + totalTimes + "|tryToSeeFlag:" + tryToSeeFlag);
                if (tryToSeeFlag == 0 || (tryToSeeFlag == 1 && (TextUtils.equals(reportType, BookMarkSwitchs.DESTORY) || TextUtils.equals(reportType, BookMarkSwitchs.QUIT)))) {
                    if (currentSecond > 0 && totalTimes - currentTimes > 5000) {
                        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                            Bookmark bookMarkItem = new Bookmark();
                            bookMarkItem.setBookmarkType(playVodBean.getVodType());
                            bookMarkItem.setItemID(videoId);
                            if (tryToSeeFlag == 0) {
                                Log.i(TAG, "1reportBookmark: currentSecond");
                                bookMarkItem.setRangeTime(currentSecond + "");
                            } else {
                                Log.i(TAG, "2reportBookmark: currentSecond");
                                bookMarkItem.setRangeTime("1");
                            }
                            // 非0的时候是电视剧
                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // 银河的VOD场景
                            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                                bookMarkItem.setItemID(playVodBean.getFatherVODId());
                                bookMarkItem.setSubContentID(videoId);
                                bookMarkItem.setSubContentType("VOD");
                                bookMarkItem.setSitcomNO(playVodBean.getFatherSitcomNO());
                            }
                            SuperLog.debug(TAG, "create bookmark--->" + "videoId:" + videoId + ",epsodeId:" + playVodBean.getEpisodeId() + ",sitcomNO:" + sitcomNO + ",currentSecond:" + currentSecond);
                            ArrayList<Bookmark> bookmarks = new ArrayList<>();
                            bookmarks.add(bookMarkItem);
                            createBookmark(bookmarks);
                        }
                    } else {
                        if (playVodBean != null && !TextUtils.isEmpty(playVodBean.getVodType()) && !playVodBean.getVodType().equals(Content.PROGRAM)) {
                            Bookmark bookMarkItem = new Bookmark();
                            bookMarkItem.setBookmarkType(playVodBean.getVodType());
                            bookMarkItem.setItemID(videoId);
                            Log.i(TAG, "3reportBookmark: currentSecond");
                            if (tryToSeeFlag == 1) {
                                bookMarkItem.setRangeTime("1");
                            } else {
                                bookMarkItem.setRangeTime(currentSecond + "");
                            }

                            if (!TextUtils.isEmpty(isFilm)) {
                                if (!isFilm.equals("0")) {
                                    bookMarkItem.setSubContentID(subContentId);
                                    bookMarkItem.setSubContentType("VOD");
                                    bookMarkItem.setSitcomNO(sitcomNO);
                                }
                            }
                            // 银河的VOD场景
                            if (!TextUtils.isEmpty(playVodBean.getFatherVODId())) {
                                bookMarkItem.setItemID(playVodBean.getFatherVODId());
                                bookMarkItem.setSubContentID(videoId);
                                bookMarkItem.setSubContentType("VOD");
                                bookMarkItem.setSitcomNO(playVodBean.getFatherSitcomNO());
                            }
                            SuperLog.debug(TAG, "create bookmark--->" + "videoId:" + videoId + ",epsodeId:" + playVodBean.getEpisodeId() + ",sitcomNO:" + sitcomNO + ",currentSecond:" + currentSecond);
                            ArrayList<Bookmark> bookmarks = new ArrayList<>();
                            bookmarks.add(bookMarkItem);
                            createBookmark(bookmarks);
                        }
                    }
                } else {
                    //增加切集切到vip剧集，刷新详情页子集列表
                    playVodBean.setSitcomNO(sitcomNO);
                    playVodBean.setEpisodeId(subContentId);
                    SuperLog.debug(TAG, "event send Bookmark refresh");
//                    if (null != mActivity && !mActivity.isFinishing()) {
                    EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
//                    }
                }
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
//            }
        }
    }

    public void createBookmark(ArrayList<Bookmark> bookmarks) {
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
//                if (null != mActivity && !mActivity.isFinishing()) {
                EventBus.getDefault().post(new BookmarkEvent(JsonParse.object2String(playVodBean)));
//                }

            }

        }

        @Override
        public void createBookmarkFail() {
            SuperLog.error(TAG, "createBookmarkFail");
        }
    };
}
