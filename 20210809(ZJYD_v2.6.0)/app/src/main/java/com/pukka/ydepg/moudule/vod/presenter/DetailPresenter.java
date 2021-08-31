package com.pukka.ydepg.moudule.vod.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.bean.node.EpisodesBean;
import com.pukka.ydepg.common.http.bean.request.QueryDuplicateRequest;
import com.pukka.ydepg.common.http.bean.request.QueryRecommendRequest;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.AddFavoCatalogCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateContentScoreCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.CreateFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteBookmarkCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.DeleteFavoriteCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryFavoCatalogCallback;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDuplicateCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PersonalizedController;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PlayURLController;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.ContentScore;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.FavoCatalog;
import com.pukka.ydepg.common.http.v6bean.v6node.Favorite;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6request.AddFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateContentScoreRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Detail;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SubscriptControl;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.moudule.vod.view.VodDetailDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ld
 * @date: 2017-12-19
 */

public class DetailPresenter extends BasePresenter implements Presenter {
    public static final String TAG = DetailPresenter.class.getName();
    private DetailDataView mDetailDataView;
    private VODListController mVODListController;
    private VODDetail mVODDetail;
    private PersonalizedController mPersonalizedController;
    private PlayURLController mPlayVODControl;
    private Episode episode;
    private RxAppCompatActivity rxAppCompatActivity;

    //0 剧集，1未综艺
    private int isSeries;
    private int isReverse;

    private boolean isClickEpisode;

    //是否是儿童模式
    private boolean isChildMode;

    //观看时长
    private String elapseTime;

    //是否添加收藏
    private boolean isCollection;

    private boolean isXiri = false;

    private String lastPlayUrl;
    private String lastPlayID;


    public String getElapseTime() {
        return elapseTime;
    }

    private PlayVodBean playVodBean = new PlayVodBean();

    public void setPlayVodBean(PlayVodBean playVodBean) {
        this.playVodBean = playVodBean;
    }

    public void setChildMode(boolean childMode) {
        isChildMode = childMode;
    }

    public boolean isClickEpisode() {
        return isClickEpisode;
    }

    public void setClickEpisode(boolean clickEpisode) {
        isClickEpisode = clickEpisode;
    }

    public void setIsSeries(int isSeries) {
        this.isSeries = isSeries;
    }

    public void setIsReverse(int isReverse) {
        this.isReverse = isReverse;
    }

    /**
     * 是否支持试看
     */
    private boolean mOrderOrSee;

    private PlayVODRequest mPlayVodRequest;

    private String subjectId;

    private String recommendType;


    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public DetailPresenter(RxAppCompatActivity rxAppCompatActivity) {
        mVODListController = new VODListController(rxAppCompatActivity);
        mPlayVODControl = new PlayURLController(rxAppCompatActivity, playURLCallBack);
        mPersonalizedController = new PersonalizedController(rxAppCompatActivity);
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

    /**
     * 是否支持试看
     *
     * @param orderOrSee
     */
    public void setButtonOrderOrSee(boolean orderOrSee) {
        mOrderOrSee = orderOrSee;
    }


    public void setDetailDataView(DetailDataView detailDataView) {
        mDetailDataView = detailDataView;
    }

    public VODDetail getVODDetail() {
        return mVODDetail;
    }

    public void setVODDetail(VODDetail vodDetail) {
        mVODDetail = vodDetail;
    }

    public void getVODDetail(String id) {
        SuperLog.error(TAG, "getVODDetail:" + id);
        if(mDetailDataView instanceof VodDetailDataView){
            ((VodDetailDataView)mDetailDataView).showLoading();
        }
        getVODDetail(id, mVODDetailCallback);
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public void getVODDetail(String id, VODDetailCallBack mVODDetailCallback) {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(id);
        mVODListController.getVODDetail(getVODDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), mVODDetailCallback);

    }

    public void getVODDetail(String id, String idType, VODDetailCallBack mVODDetailCallback) {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(id);
        getVODDetailRequest.setIDType(idType);
        mVODListController.getVODDetail(getVODDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), mVODDetailCallback);

    }


    public void getVODDetailNotToastHint(String id, VODDetailCallBack mVODDetailCallback) {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(id);
        mVODListController.getVODDetailNotToastHint(getVODDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), mVODDetailCallback);
    }


    public void getAdvertisementSubjectDetail(String subjectId, VODListCallBack mVODListCallBack) {
        QuerySubjectDetailRequest mQuerySubjectDetailRequest = new QuerySubjectDetailRequest();
        List<String> categoryList = new ArrayList<>();
        categoryList.add(subjectId);
        mQuerySubjectDetailRequest.setSubjectIds(categoryList);
        mVODListController.querySubjectDetail(mQuerySubjectDetailRequest, compose(bindToLifecycle(rxAppCompatActivity)), mVODListCallBack);

    }

    /**
     * 播放器上报行为
     *
     * @param action
     * @param vodId
     * @param mediaId
     * @param subjectId
     * @param productId
     */

    public void reportVod(int action, String vodId, String mediaId, String subjectId, String productId) {
        ReportVODRequest request = new ReportVODRequest();
        request.setAction(action);
        request.setVODID(vodId);
        request.setMediaID(mediaId);
        if (!TextUtils.isEmpty(subjectId)) {
            request.setSubjectID(subjectId);
        } else {
            request.setSubjectID("-1");
        }
        request.setProductID(productId);
        mPlayVODControl.reportVod(request, compose(bindToLifecycle(rxAppCompatActivity)));
    }


    public void playVOD(VODDetail detail) {
        SuperLog.debug(TAG, "playVOD");
        List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
        playVod(vodMediaFiles, detail.getID());
    }


    public void playVOD(PlayVODRequest playVODRequest, PlayURLCallBack playURLCallBack, String elapseTime) {
        mPlayVodRequest = playVODRequest;
        PlayURLController playVODControl = new PlayURLController(rxAppCompatActivity);
        playVODControl.setPlayURLCallBack(playURLCallBack);
        this.elapseTime = elapseTime;
        playVODControl.getVODPlayUrlEPG(playVODRequest, compose(bindToLifecycle(rxAppCompatActivity)), elapseTime);
    }

    public void playVOD(VODDetail detail, PlayURLCallBack playURLCallBack) {
        mPlayVODControl.setPlayURLCallBack(playURLCallBack);
        List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
        playVod(vodMediaFiles, detail.getID());
    }


    public void playVOD(Episode episode, PlayURLCallBack playURLCallBack) {
        mPlayVODControl.setPlayURLCallBack(playURLCallBack);
        SuperLog.debug(TAG, "playVOD episode");
        Log.e("ld", "playVOD");
        this.episode = episode;
        VOD vod = episode.getVOD();
        if (vod != null) {
            playVod(vod.getMediaFiles(), vod.getID());
        }
    }

    public void playVOD(Episode episode) {
        SuperLog.debug(TAG, "playVOD episode");
        Log.e("ld", "playVOD");
        this.episode = episode;
        VOD vod = episode.getVOD();
        if (vod != null) {
            playVod(vod.getMediaFiles(), vod.getID());
        }
    }

    public void createBookmark(CreateBookmarkRequest createBookmarkRequest, CreateBookmarkCallBack createBookmarkCallBack) {
        mPersonalizedController.createBookmark(createBookmarkRequest, createBookmarkCallBack, compose(bindToLifecycle(rxAppCompatActivity)));
    }

    public void deleteBookmark(DeleteBookmarkRequest request, DeleteBookmarkCallBack deleteBookmarkCallBack) {
        mPersonalizedController.deleteBookMark(request, deleteBookmarkCallBack, compose(bindToLifecycle(rxAppCompatActivity)));
    }

    private void playVod(List<VODMediaFile> vodMediaFiles, String id) {
        if (vodMediaFiles != null) {
            if (vodMediaFiles.size() != 0) {
                SuperLog.debug(TAG, "playVOD epvodMediaFilesisode");
                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                mPlayVodRequest = new PlayVODRequest();
                mPlayVodRequest.setVODID(id);
                mPlayVodRequest.setMediaID(vodMediaFile.getID());
                mPlayVodRequest.setURLFormat("1");
                mPlayVodRequest.setIsReturnProduct("1");
                this.elapseTime = vodMediaFile.getElapseTime();
                mPlayVODControl.getVODPlayUrlEPG(mPlayVodRequest, compose(bindToLifecycle(rxAppCompatActivity)), vodMediaFile.getElapseTime());
            }
        }
    }


    //vod列表没有过滤条件请求
    public void loadMoviesContent(String subjectId, String offset, String count, VODListCallBack mVODListCallBack) {
        QueryVODListBySubjectRequest request = new QueryVODListBySubjectRequest();
        request.setSubjectID(subjectId);
        request.setCount(count);
        request.setOffset(offset);
        SuperLog.error(TAG, "queryVODSubjectList-> request->" + subjectId);
        VODListController vodListController = new VODListController(mVODListCallBack, rxAppCompatActivity);
        vodListController.queryVODListBySubject(request, compose(bindToLifecycle(rxAppCompatActivity)), subjectId,null);
    }


    public void queryPBSRecommend(String scenarizedType, String id, VODListCallBack mVODListCallBack, String count, String offset) {
        QueryRecommendRequest request = new QueryRecommendRequest();
        request.setCount(count);
        request.setOffset(offset);
        request.setScenarizedType(scenarizedType);
        if (QueryRecommendRequest.SecenarizedType.CONTENT_DETAIL.equals(scenarizedType)) {
            request.setVodId(id);
        } else {
            if (!TextUtils.isEmpty(id)) {
                request.setProudctId(id);
            }
        }
        request.setVt("9");
        VODListController vodListController = new VODListController(mVODListCallBack, rxAppCompatActivity);
        vodListController.queryPBSRecommend(request, compose(bindToLifecycle(rxAppCompatActivity)), mVODListCallBack);
    }

    //请求首页智能推荐位内容
    public void queryPBSDuplicate(VODDuplicateCallBack mVODDuplicateCallBack, String sceneId, List<String> contentIds) {
        //sceneId == 203：专题推荐、204：活动推荐(此接口202：内容推荐不支持)
        //contentIds == 表示需要去重的内容ID集合，ID使用英文“,”进行分割
        QueryDuplicateRequest request = new QueryDuplicateRequest();
        request.setSceneId(sceneId);
        request.setContentIds(contentIds);
        request.setVt("9");
        request.setOffset("0");
        request.setCount("6");
        VODListController vodListController = new VODListController(rxAppCompatActivity);
        vodListController.queryPBSDuplicate(request, mVODDuplicateCallBack);
    }


    public void playClipfile(VODMediaFile mVODMediaFile, String id, String elapseTime) {
        if (mVODMediaFile != null) {
            SuperLog.debug(TAG, "playVOD clipfile");
            mPlayVodRequest = new PlayVODRequest();
            mPlayVodRequest.setVODID(id);
            mPlayVodRequest.setMediaID(mVODMediaFile.getID());
            mPlayVodRequest.setURLFormat("1");
            mPlayVodRequest.setIsReturnProduct("1");
            this.elapseTime = elapseTime;
            mPlayVODControl.getVODPlayUrlEPG(mPlayVodRequest, compose(bindToLifecycle(rxAppCompatActivity)), elapseTime);
        }
    }

    public void commitScore(float score, VODDetail detail) {
        CreateContentScoreRequest request = new CreateContentScoreRequest();
        ContentScore contentScore = new ContentScore();
        contentScore.setContentID(detail.getID());
        contentScore.setContentType("VOD");
        contentScore.setScore(((int) score) + "");
        List<ContentScore> contentScoreList = new ArrayList<>();
        contentScoreList.add(contentScore);
        request.setScores(contentScoreList);
        mPersonalizedController.createContentScore(request, mCreateContentScoreCallBack, compose(bindToLifecycle(rxAppCompatActivity)));
    }

    public void setCollection(VODDetail detail, boolean isCollection) {
        mVODDetail = detail;
        this.isCollection = isCollection;
        if (isCollection) {//已收藏
            deleteFavorite();
        } else {//未收藏
            addFavorite();
        }

    }

    public void deleteFavorite() {
        DeleteFavoriteRequest request2 = new DeleteFavoriteRequest();
        List<String> contentList = new ArrayList<>();
        contentList.add(mVODDetail.getID());
        request2.setContentIDs(contentList);
        List<String> contentTypeList = new ArrayList<>();
        contentTypeList.add("VOD");
        request2.setContentTypes(contentTypeList);
        mPersonalizedController.deleteFavorite(request2, mDeleteFavoriteCallBack, compose(bindToLifecycle(rxAppCompatActivity)));
    }


    public void addFavorite() {
        if (mVODDetail != null) {
            Favorite favorite1 = new Favorite();
            favorite1.setContentID(mVODDetail.getID());
            favorite1.setContentType("VOD");

            List<NamedParameter> customFields = new ArrayList<>();
            List<String> str = new ArrayList<>();
            str.add("1");
            NamedParameter np = new NamedParameter("playFromTerminal", str);
            customFields.add(np);
            favorite1.setCustomFields(customFields);
            CreateFavoriteRequest request1 = new CreateFavoriteRequest();
            List<Favorite> favoriteList = new ArrayList<>();
            if (VoddetailUtil.getInstance().isChildVod(mVODDetail)) {
                SuperLog.debug(TAG, "addFavorite->favoCatologID:" + OTTApplication.getContext().getFavoCatalogID());
                if (!TextUtils.isEmpty(OTTApplication.getContext().getFavoCatalogID())) {
                    favorite1.setCatalogID(OTTApplication.getContext().getFavoCatalogID());
                    favoriteList.add(favorite1);
                    request1.setFavorites(favoriteList);
                    mPersonalizedController.createFavorite(request1, mCreateFavoriteCallBack, compose(bindToLifecycle(rxAppCompatActivity)), recommendType);
                } else {
                    queryFavoCatalog(mQueryFavoCatalogCallback);
                }
            } else {
                favorite1.setCatalogID("-1");
                favoriteList.add(favorite1);
                request1.setFavorites(favoriteList);
                mPersonalizedController.createFavorite(request1, mCreateFavoriteCallBack, compose(bindToLifecycle(rxAppCompatActivity)), recommendType);
            }
        }


    }

    /**
     * 查询收藏夹
     */
    public void queryFavoCatalog(QueryFavoCatalogCallback mQueryFavoCatalogCallback) {
        QueryFavoCatalogRequest request = new QueryFavoCatalogRequest();
        request.setCount("50");
        request.setOffset("0");
        mPersonalizedController.queryFavoCatalog(request, mQueryFavoCatalogCallback, compose(bindToLifecycle(rxAppCompatActivity)));

    }

    /**
     * @param catalogName
     */
    public void createFavoCatalog(String catalogName) {
        AddFavoCatalogRequest request = new AddFavoCatalogRequest();
        FavoCatalog catalog = new FavoCatalog();
        catalog.setCatalogID(Constant.CHILD_FAVROITE_FOLDER_ID);
        catalog.setCatalogName(catalogName);
        request.setCatalog(catalog);
        mPersonalizedController.addFavoCatalog(request, mAddFavoCatalogCallBack, compose(bindToLifecycle(rxAppCompatActivity)));

    }


    AddFavoCatalogCallBack mAddFavoCatalogCallBack = new AddFavoCatalogCallBack() {
        @Override
        public void addFavoCatalogSuccess() {
//            queryFavoCatalog(mQueryFavoCatalogCallback);
            OTTApplication.getContext().setFavoCatalogID(Constant.CHILD_FAVROITE_FOLDER_ID);
            addFavorite();
        }

        @Override
        public void addFavoCatalogFail() {
            EpgToast.showToast(rxAppCompatActivity, rxAppCompatActivity.getResources().getString(R.string.collect_error));
        }
    };


    QueryFavoCatalogCallback mQueryFavoCatalogCallback = new QueryFavoCatalogCallback() {
        @Override
        public void queryFavoCatalogSuccess(int total, List<FavoCatalog> favoCatalogList) {
            if (total > 0 && null != favoCatalogList && favoCatalogList.size() > 0) {
                String catalogID = null;
                for (int i = 0; i < favoCatalogList.size(); i++) {
                    if (Constant.CHILD_FAVROITE_FOLDER_ID.equals(favoCatalogList.get(i).getCatalogID())) {
                        catalogID = favoCatalogList.get(i).getCatalogID();
                        break;
                    }
                }
                if (TextUtils.isEmpty(catalogID)) {
                    createFavoCatalog(Constant.CHILD_FAVROTIE_FOLDER);
                } else {
                    OTTApplication.getContext().setFavoCatalogID(catalogID);
                    addFavorite();
                }
            } else {
                createFavoCatalog(Constant.CHILD_FAVROTIE_FOLDER);
            }
        }

        @Override
        public void queryFavoCatalogFail() {
            createFavoCatalog(Constant.CHILD_FAVROTIE_FOLDER);
        }
    };

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {}

    //vodDetail的回调
    private VODDetailCallBack mVODDetailCallback = new VODDetailCallBack() {
        @Override
        public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
            if(mDetailDataView instanceof VodDetailDataView){
                ((VodDetailDataView)mDetailDataView).hideLoading();
            }
            if (null != vodDetail) {
                mVODDetail = vodDetail;
                BrowseTVPlayFragment.setEpisodesBean(new EpisodesBean(mVODDetail.getID(), mVODDetail.getEpisodes()));
                SuperLog.error(TAG, "getVODDetail-> success");
                mDetailDataView.showDetail(mVODDetail, recmActionID, recmContents);
            }

        }

        @Override
        public void getVODDetailFailed() {
            if(mDetailDataView instanceof VodDetailDataView){
                ((VodDetailDataView)mDetailDataView).hideLoading();
            }
            //接口已经处理了一次错误码，需要弹出二维码时，不在进行第二次处理
            if (!OTTErrorWindowUtils.needShowErrorDialog()) {
                mDetailDataView.showContentNotExit();
            }
        }

        @Override
        public void onError() {
            if(mDetailDataView instanceof VodDetailDataView){
                ((VodDetailDataView)mDetailDataView).hideLoading();
            }
            //接口已经处理了一次错误码，需要弹出二维码时，不在进行第二次处理
            if (!OTTErrorWindowUtils.needShowErrorDialog()) {
                mDetailDataView.showContentNotExit();
            }
        }
    };

    private PlayURLCallBack playURLCallBack = new PlayURLCallBack() {

        @Override
        public void getVODPlayUrlSuccess(String playUlr, String mbookmark, String productId, String elapseTime) {
            SuperLog.debug(TAG, "getVODPlayUrlSuccess url:" + playUlr);
            Log.e("ld", "getVODPlayUrlSuccess");

            if (mVODDetail != null) {
                if (!TextUtils.isEmpty(playUlr)) {
                    playVodBean.clearDate();
                    //传入分辨率标识
                    if(mVODDetail.getVODType().equals("0")) {
                        if (!CollectionUtil.isEmpty(mVODDetail.getMediaFiles())&&null!=mVODDetail.getMediaFiles().get(0)&&null != mVODDetail.getMediaFiles().get(0).getDefinition()) {
                            if (SubscriptControl.iszj4KVOD(mVODDetail)) {
                                if(playUlr.contains("?")) {
                                    playUlr = playUlr + "&definition=30";
                                }else {
                                    playUlr = playUlr + "?definition=30";
                                }

                            } else {
                                if(playUlr.contains("?")) {
                                    playUlr = playUlr + "&definition=" + mVODDetail.getMediaFiles().get(0).getDefinition();
                                }else {
                                    playUlr = playUlr + "?definition=" + mVODDetail.getMediaFiles().get(0).getDefinition();
                                }
                            }
                        }
                    }else if(episode!=null) {
                        if (!CollectionUtil.isEmpty(episode.getVOD().getMediaFiles())&&null!=episode.getVOD().getMediaFiles().get(0)&&null != episode.getVOD().getMediaFiles().get(0).getDefinition()) {
                            if (SubscriptControl.iszj4KVOD(mVODDetail)) {
                                if(playUlr.contains("?")) {
                                    playUlr = playUlr + "&definition=30";
                                }else {
                                    playUlr = playUlr + "?definition=30";
                                }

                            } else {
                                if(playUlr.contains("?")) {
                                    playUlr = playUlr + "&definition=" + episode.getVOD().getMediaFiles().get(0).getDefinition();
                                }else {
                                    playUlr = playUlr + "?definition=" + episode.getVOD().getMediaFiles().get(0).getDefinition();
                                }
                            }
                        }
                    }
                    SuperLog.debug(TAG, "playUlr--->" + playUlr);

                    playVodBean.setPlayUrl(playUlr);
                    playVodBean.setVodId(mVODDetail.getID());
                    playVodBean.setVodName(mVODDetail.getName());

                    if (mVODDetail.getSeries() != null && mVODDetail.getSeries().size() > 0) {
                        playVodBean.setFatherSitcomNO(mVODDetail.getSeries().get(0).getSitcomNO());
                        playVodBean.setFatherVODId(mVODDetail.getSeries().get(0).getVODID());
                    }
                    int mPreviewDuration;
                    if (!mVODDetail.getVODType().equals("0") && episode != null) {
                        Bookmark bk = mVODDetail.getBookmark();
                        if (bk != null) {
                            String sitcomNo = bk.getSitcomNO();
                            if (!TextUtils.isEmpty(sitcomNo) && episode.getSitcomNO().equals(sitcomNo)) {
                                String bookmark = bk.getRangeTime();
                                playVodBean.setBookmark(bookmark);
                                SuperLog.debug("TAG", "bookmark--->" + bookmark + "mbookmark--->" + mbookmark);
                            }
                        }
                        playVodBean.setSitcomNO(episode.getSitcomNO());
                        playVodBean.setEpisodeId(episode.getVOD().getID());
                        mPreviewDuration = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), episode.getVOD() == null ? null : episode.getVOD().getCustomFields(), VodDetailCacheService.getVodTimeByMediaFiles(episode.getVOD() == null ? null : episode.getVOD().getMediaFiles()));
                        SuperLog.debug("TAG", "bookmark  setSitcomNO --->" + episode.getSitcomNO());
                        SuperLog.debug("TAG", "bookmark  episodeId --->" + episode.getVOD().getID());
                    } else {
                        Bookmark bk = mVODDetail.getBookmark();
                        if (bk != null) {
                            String bookmark = bk.getRangeTime();
                            playVodBean.setBookmark(mbookmark);
                            SuperLog.debug("TAG", "bookmark--->" + bookmark + "mbookmark--->" + mbookmark);
                        }
                        mPreviewDuration = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), null, VodDetailCacheService.getVodTimeByMediaFiles(mVODDetail.getMediaFiles()));
                    }

                    if (isXiri) {
                        playVodBean.setIsXiri("1");
                    }
                    playVodBean.setLastPlayUrl(lastPlayUrl);
                    playVodBean.setLastPlayID(lastPlayID);
                    playVodBean.setVodType("VOD");
                    playVodBean.setIsFilm(mVODDetail.getVODType());
                    playVodBean.setElapseTime(elapseTime);
                    playVodBean.setIsSeries(isSeries);
                    playVodBean.setIsReverse(isReverse);
                    playVodBean.setProductId(productId);
                    playVodBean.setTryToSeeFlag(0);
                    playVodBean.setRecommendType(recommendType);
                    if (!TextUtils.isEmpty(subjectId)) {
                        playVodBean.setSubjectID(subjectId);
                    }
                    if (null != mPlayVodRequest) {
                        playVodBean.setMediaId(mPlayVodRequest.getMediaID());
                    }
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + playVodBean.getVodId() + ",epsodeId:" + playVodBean.getEpisodeId() + ",sitcomNO:" + playVodBean.getSitcomNO() + ",breakpoint:" + playVodBean.getBookmark());

                    BrowseTVPlayFragment.setEpisodesBean(new EpisodesBean(playVodBean.getVodId(), mVODDetail.getEpisodes()));
                    if (null != mVODDetail) {
                        playVodBean.setDetailStr(VodUtil.getSimpleVoddetail(mVODDetail, mPreviewDuration));
                    }

                    //获取前贴广告
                    playVodBean.setAdvertVideo(null);
                    VOD requestAdvertVod;
                    if(episode==null){
                        requestAdvertVod = mVODDetail; //电影
                    } else {
                        requestAdvertVod = AdUtil.getVodInfoForEpisode(mVODDetail,episode.getVOD());//连续剧
                    }
                    OwnChooseEvent event = new OwnChooseEvent(false);
                    OrderConfigUtils.getInstance().setEvent(event);
                    AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.VIDEO,iadListener,requestAdvertVod);
                    EventBus.getDefault().post(new FinishPlayUrlEvent());
                } else {
                    EventBus.getDefault().post(new FinishPlayUrlEvent());
                    EpgToast.showToast(OTTApplication.getContext(), "播放地址为空！");
                }
            } else {
                EventBus.getDefault().post(new FinishPlayUrlEvent());
            }
            isXiri = false;
            lastPlayUrl = null;
            lastPlayID = null;
        }

        @Override
        public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) {}

        @Override
        public void playFail() {
            isXiri = false;
            lastPlayUrl = null;
            lastPlayID = null;
            EventBus.getDefault().post(new FinishPlayUrlEvent());
        }

        @Override
        public void playFail(PlayVODResponse response) {
            Log.i(TAG, "playFail: "+JsonParse.object2String(response.getResult()) + " extensionFields : "+ JsonParse.object2String(response.getExtensionFields()));

            EventBus.getDefault().post(new FinishPlayUrlEvent());
            String returnCode = response.getResult().getRetCode();
            SuperLog.debug(TAG, "playFail->returnCode:" + returnCode);
            int mPreviewDuration;
            if (!TextUtils.isEmpty(returnCode) && (returnCode.equals("144020008") || returnCode.equals("504") || returnCode.equals("114020006"))) {
                String playUrl = response.getPlayURL();

                OwnChooseEvent event = new OwnChooseEvent(true);
                if (null != response.getAuthorizeResult() && null != response.getAuthorizeResult().getPricedProducts() && response.getAuthorizeResult().getPricedProducts().size() > 0){
                    List<Product> products = response.getAuthorizeResult().getPricedProducts();
                    event.setPricedProducts(products);
                }
                OrderConfigUtils.getInstance().setEvent(event);

                if (mOrderOrSee) {
//                    mOrderOrSee = false;
                    if (mVODDetail != null) {
                        if (!TextUtils.isEmpty(playUrl)) {
                            playVodBean.clearDate();
                            playVodBean.setPlayUrl(playUrl);
                            playVodBean.setVodId(mVODDetail.getID());
                            playVodBean.setVodName(mVODDetail.getName());
                            playVodBean.setRecommendType(recommendType);
                            if (isXiri) {
                                playVodBean.setIsXiri("1");
                            }
                            playVodBean.setLastPlayUrl(lastPlayUrl);
                            playVodBean.setLastPlayID(lastPlayID);
                            if (mVODDetail.getSeries() != null && mVODDetail.getSeries().size() > 0) {
                                playVodBean.setFatherSitcomNO(mVODDetail.getSeries().get(0).getSitcomNO());
                                playVodBean.setFatherVODId(mVODDetail.getSeries().get(0).getVODID());
                            }

                            if (!mVODDetail.getVODType().equals("0") && episode != null) {
                                //电视剧
                                playVodBean.setSitcomNO(episode.getSitcomNO());
                                playVodBean.setEpisodeId(episode.getVOD().getID());
                                mPreviewDuration = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), episode.getVOD() == null ? null : episode.getVOD().getCustomFields(), VodDetailCacheService.getVodTimeByMediaFiles(episode.getVOD() == null ? null : episode.getVOD().getMediaFiles()));
                                SuperLog.debug("TAG", "bookmark  setSitcomNO --->" + episode.getSitcomNO());
                                SuperLog.debug("TAG", "bookmark  episodeId --->" + episode.getVOD().getID());
                            } else {
                                mPreviewDuration = VodDetailCacheService.getPreviewDuration(mVODDetail.getCustomFields(), null, VodDetailCacheService.getVodTimeByMediaFiles(mVODDetail.getMediaFiles()));
                            }
                            playVodBean.setVodType("VOD");
                            playVodBean.setIsFilm(mVODDetail.getVODType());
                            if (null != mPlayVodRequest) {
                                playVodBean.setMediaId(mPlayVodRequest.getMediaID());
                            }
                            playVodBean.setTryToSeeFlag(1);// 1 试看  鉴权不通过
                            playVodBean.setAuthResult(JsonParse.object2String(response.getAuthorizeResult()));
                            if (null != mPlayVodRequest) {
                                playVodBean.setMediaId(mPlayVodRequest.getMediaID());
                            }
                            if (null != mVODDetail) {
                                playVodBean.setDetailStr(VodUtil.getSimpleVoddetail(mVODDetail, mPreviewDuration));
                            }
                            playVodBean.setIsSeries(isSeries);
                            playVodBean.setIsReverse(isReverse);
                            BrowseTVPlayFragment.setEpisodesBean(new EpisodesBean(playVodBean.getVodId(), mVODDetail.getEpisodes()));

                            //请求前贴广告时，先清除缓存
                            playVodBean.setAdvertVideo(null);
                            VOD requestAdvertVod;
                            if(episode==null){
                                requestAdvertVod = mVODDetail;//电影
                            } else {
                                requestAdvertVod = AdUtil.getVodInfoForEpisode(mVODDetail,episode.getVOD());//连续剧
                            }
                            AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.VIDEO,iadListener,requestAdvertVod);
                        } else {
                            EpgToast.showToast(OTTApplication.getContext(), "本片暂不支持试看");
                        }
                    }
                } else {
                    AuthorizeResult authorizeResult = response.getAuthorizeResult();
                    if (null != authorizeResult) {
                        List<Product> products = authorizeResult.getPricedProducts();
                        if (SessionService.getInstance().getSession().isHotelUser()) {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(OTTApplication.getContext(), R.string.notice_no_orderable_product);
                                return;
                            }
                            for (int i = products.size() - 1; i >= 0; i--) {
                                Product mProductInfo = products.get(i);
                                if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                                    products.remove(i);
                                }
                            }
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(OTTApplication.getContext(), R.string.notice_no_orderable_product);
                                return;
                            }
                            authorizeResult.setPricedProducts(products);

                        } else {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(OTTApplication.getContext(), R.string.notice_no_orderable_product);
                                return;
                            }
                        }

                    } else {
                        EpgToast.showToast(OTTApplication.getContext(), R.string.notice_no_orderable_product);
                        return;
                    }
                    String authResult = JsonParse.object2String(authorizeResult);

                    String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                    if (null != needJumpToH5Order && needJumpToH5Order.equals("1")){
                        JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(authorizeResult.getPricedProducts(),OTTApplication.getContext(),true,false,null,mVODDetail);
                        //pbs点击上报
                        Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                        PbsUaService.report(Detail.getPurchaseData(mVODDetail.getID()));
                    }else{
                        Intent intent = new Intent(OTTApplication.getContext(), NewProductOrderActivity.class);
                        intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, authResult);
                        intent.putExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, true);
                        if (null != mVODDetail) {
                            intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        OTTApplication.getContext().startActivity(intent);
                    }
                }
            } else if (!TextUtils.isEmpty(returnCode) && returnCode.equals("144020000")) {
                EpgToast.showToast(OTTApplication.getContext(), R.string.notice_no_orderable_product);
            }else {
                //EpgToast.showToast(OTTApplication.getContext(), "媒资鉴权失败！");
                OTTErrorWindowUtils.getErrorInfoFromPbs(HttpConstant.PLAYVOD,returnCode,rxAppCompatActivity);
            }
            isXiri = false;
            lastPlayUrl = null;
            lastPlayID = null;
        }

        @Override
        public void onPlaycancel() {}

        @Override
        public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String switchNum, String name) { }

        @Override
        public void getVODDownloadUrlFailed(String vodID, String episodeID) { }
    };

    private void switchPlayerView(){
        //TODO 跳转到播放界面
        Intent intent = new Intent(OTTApplication.getContext(), OnDemandVideoActivity.class);
        /**
         * 只能有一个播放器，不能用singleInstance，盒子有问题
         */
        List<WeakReference<Activity>> activityList = ActivityStackControlUtil.getActivityList();
        if (null != activityList && !activityList.isEmpty()) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (activityList.get(i).get() instanceof OnDemandVideoActivity) {
                    activityList.get(i).get().finish();
                    break;
                }
            }
        }
        if (isChildMode && rxAppCompatActivity != null && rxAppCompatActivity instanceof ChildModeVodDetailActivity) {
            ((ChildModeVodDetailActivity) rxAppCompatActivity).getIntentInfo();
        } else {
            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(playVodBean));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            OTTApplication.getContext().startActivity(intent);
        }
    }

    IADListener iadListener=new IADListener() {
        @Override
        public void onSuccess(List<AdvertContent> listAdvertContent) {
            if(null!=listAdvertContent&&listAdvertContent.size()>0&&listAdvertContent.get(0).getVideo()!=null){
                playVodBean.setAdvertVideo(listAdvertContent.get(0).getVideo());
            }
            switchPlayerView();
        }

        @Override
        public void onFail() {
            switchPlayerView();
        }
    };
    private CreateFavoriteCallBack mCreateFavoriteCallBack = new CreateFavoriteCallBack() {
        @Override
        public void createFavoriteSuccess() {
            if (null != mDetailDataView) {
                mDetailDataView.showCollection(true);
            }
            SuperLog.debug(TAG, "send collectioneEvent->isCollection:true");

            EventBus.getDefault().post(new CollectionEvent(true, mVODDetail.getID()));
            EpgToast.showToast(rxAppCompatActivity, "收藏成功！");
            isCollection = true;
        }

        @Override
        public void createFavoriteFail() { }
    };

    private DeleteFavoriteCallBack mDeleteFavoriteCallBack = new DeleteFavoriteCallBack() {
        @Override
        public void deleteFavoriteSuccess() {
            if (null != mDetailDataView) {
                mDetailDataView.showCollection(false);
            }
            SuperLog.debug(TAG, "send collectioneEvent->isCollection:false");
            EventBus.getDefault().post(new CollectionEvent(false, mVODDetail.getID()));
            EpgToast.showToast(rxAppCompatActivity, "取消收藏成功！");
            isCollection = false;
        }

        @Override
        public void deleteFavoriteFail() { }
    };

    private CreateContentScoreCallBack mCreateContentScoreCallBack = new CreateContentScoreCallBack() {
        @Override
        public void createContentScoreSuccess(List<Float> newScores) {
            if (newScores != null && newScores.size() != 0) {
                mVODDetail.setAverageScore(String.valueOf(newScores.get(0)));
                mDetailDataView.setNewScore(newScores);
                EventBus.getDefault().post(new NewScoresEvent(newScores));
                EpgToast.showToast(OTTApplication.getContext(), "操作成功!");
            }
        }

        @Override
        public void createContentScoreFail() { }
    };

    public void playVOD(String vodID, String mediaID, PlayURLCallBack callBack) {
        mPlayVODControl.setPlayURLCallBack(callBack);
        mPlayVodRequest = new PlayVODRequest();
        mPlayVodRequest.setVODID(vodID);
        mPlayVodRequest.setMediaID(mediaID);
        mPlayVodRequest.setURLFormat("1");
        mPlayVodRequest.setIsReturnProduct("1");
        mPlayVODControl.getVODPlayUrlEPG(mPlayVodRequest, compose(bindToLifecycle(rxAppCompatActivity)), null);
    }

    public void setXiri(boolean isXiri) {
        this.isXiri = isXiri;
    }

    public void setLastPlayUrl(String lastPlayUrl) {
        this.lastPlayUrl = lastPlayUrl;
    }

    public void setLastPlayID(String lastPlayID) {
        this.lastPlayID = lastPlayID;
    }
}