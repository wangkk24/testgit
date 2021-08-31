package com.pukka.ydepg.moudule.player.util;

import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.node.EpisodesBean;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.PlayURLController;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Detail;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.cache.VodDetailCacheService;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * @author liudong Email :liudong@easier.cn
 * @desc
 * @date 2018/1/31.
 */
public class OffScreenUtils extends BasePresenter {

    public static final String TAG = OffScreenUtils.class.getName();

    private String vodId;
    private String childVodId;
    private String index;
    private PlayVodBean mPlayVodBean;
    private VODListController mVODListController;
    private PlayURLController mPlayVODControl;
    private RxAppCompatActivity mContext;
    private VODDetail vodDetail;
    private VODDetail fatherVodDetail;

    private OffScreenListener mOffScreenListener;

    private PlayVODRequest mPlayVODRequest;

    private Episode  mEpisode;

    private  int mPreviewDuration;

    public interface OffScreenListener{

         void offScreenSuccess(PlayVodBean playVodBean);
         void  offScreenFail(VODDetail vodDetail);
    }

    public OffScreenUtils(XmppMessage xmmpMessage, RxAppCompatActivity context,OffScreenListener mOffScreenListener) {
        mContext = context;
        this.mOffScreenListener=mOffScreenListener;
        mVODListController = new VODListController(context);
        mPlayVODControl = new PlayURLController(context, playURLCallBack);
        String type = xmmpMessage.getMediaType();
        if (!TextUtils.isEmpty(type) && ("2".equals(type))) { // VOD

            mPlayVodBean = new PlayVodBean();
            mPlayVodBean.setVodType("VOD");
            mPlayVodBean.setXmppFrom(xmmpMessage.getActionSource());
            mPlayVodBean.setXmppPush("1");
            String mediaCode = xmmpMessage.getMediaCode();
            String[] str = mediaCode.split(",");
            if (str != null) {
                vodId = str[0];
                if (str.length > 1) {
                    childVodId = mediaCode.split(",")[1];
                }
            }
            mPlayVodBean.setVodId(vodId);
            String playBookmark = xmmpMessage.getPlayByBookMark();
            if ("1".equals(playBookmark)) {
                mPlayVodBean.setBookmark(xmmpMessage.getPlayByTime());
            }
        }
        startPlayVod();
    }


    public VODDetail getVodDetail() {
        return vodDetail;
    }

    public void setVodDetail(VODDetail vodDetail) {
        this.vodDetail = vodDetail;
    }
    private void startPlayVod() {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(vodId);
        mVODListController.getVODDetail(getVODDetailRequest,compose(bindToLifecycle(mContext)),mVODDetailCallback);
    }

    public  static void getSPVodDetail(String mediaCode,VODListController controller,RxAppCompatActivity context,VODDetailCallBack mVODDetailCallback)
    {
        String vodId="";
        String[] str = mediaCode.split(",");
        if (str != null) {
            vodId = str[0];
        }
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(vodId);
        controller.getVODDetail(getVODDetailRequest,compose(bindToLifecycle(context)),mVODDetailCallback);
    }

    /**
     * 查询父级详情内容
     */
    private VODDetailCallBack mFatherVODDetailCallback=new VODDetailCallBack() {
        @Override
        public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
            EpisodesBean Bean=new EpisodesBean(vodDetail.getID(),vodDetail.getEpisodes());
            BrowseTVPlayFragment.setEpisodesBean(Bean);
            fatherVodDetail=vodDetail;
            mPlayVodBean.setVodName(vodDetail.getName());
            mPlayVodBean.setEpisodeId(OffScreenUtils.this.vodDetail.getID());
            mPlayVodBean.setSitcomNO(OffScreenUtils.this.vodDetail.getSeries().get(0).getSitcomNO());
            switchSonPlay(OffScreenUtils.this.vodDetail);
        }

        @Override
        public void getVODDetailFailed() {
            EpgToast.showToast(mContext,mContext.getResources().getString(R.string.message_network_disconnect));
        }

        @Override
        public void onError() {
            EpgToast.showToast(mContext,mContext.getResources().getString(R.string.message_network_disconnect));
        }
    };



    public void switchSonPlay(VODDetail vodDetail){
        List<VODMediaFile> vodMediaFiles = vodDetail.getMediaFiles();
        if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
            VODMediaFile vodMediaFile = vodMediaFiles.get(0);
            String definition = vodMediaFile.getDefinition();
            if (VodUtil.canPlay("2".equals(definition))) {
                playVod(vodMediaFile, vodId, vodMediaFile.getElapseTime());
            }
            else{
                EpgToast.showToast(mContext, Strings.getInstance().getString(R.string.details_4k_warnning));
            }
        }
    }


    private VODDetailCallBack mVODDetailCallback = new VODDetailCallBack() {
        @Override
        public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
            SuperLog.debug(TAG, "第一次detail为空返回");
            if (null != vodDetail) {
                OffScreenUtils.this.vodDetail=vodDetail;
                mPlayVodBean.setVodName(vodDetail.getName());
                if (childVodId != null && childVodId.indexOf("index") > -1) {
                    index = childVodId.substring(5);
                    List<VODMediaFile> clipfiles = vodDetail.getClipfiles();
                    VODMediaFile vodMediaFile = clipfiles.get(Integer.parseInt(index));
                    String definition = vodMediaFile.getDefinition();
                    if (VodUtil.canPlay(!"2".equals(definition))) {
                        playVod(vodMediaFile, vodId,vodMediaFile.getElapseTime());
                    }
                    else{
                        EpgToast.showToast(mContext, Strings.getInstance().getString(R.string.details_4k_warnning));
                    }
                } else {
                    if (vodDetail.getVODType().equals("0")) {
                        //如果是电视剧子集的
                        if(null!=vodDetail.getSeries()&&vodDetail.getSeries().size()>0){
                            GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
                            getVODDetailRequest.setVODID(vodDetail.getSeries().get(0).getVODID());
                            mVODListController.getVODDetail(getVODDetailRequest,compose(bindToLifecycle(mContext)),mFatherVODDetailCallback);
                        }else{
                            //只是电影
                            switchSonPlay(vodDetail);
                        }

                    } else {
                        //剧集
                        List<Episode> episodes = vodDetail.getEpisodes();
                        if(null!=episodes&&episodes.size()>0) {
                            for (Episode episode : episodes) {
                                VOD vod = episode.getVOD();
                                if (vod.getID().equals(childVodId)) {
                                    mEpisode=episode;
                                    mPlayVodBean.setEpisodeId(vod.getID());
                                    mPlayVodBean.setSitcomNO(episode.getSitcomNO());
                                    List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                                    if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                                        VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                        String definition = vodMediaFile.getDefinition();
                                        if (VodUtil.canPlay("2".equals(definition))) {
                                            playVod(vodMediaFile, vod.getID(), vodMediaFile.getElapseTime());
                                        }
                                        else{
                                            EpgToast.showToast(mContext, Strings.getInstance().getString(R.string.details_4k_warnning));
                                        }
                                        return;
                                    }
                                }
                            }
                            if(null!=vodDetail.getBookmark()&&!TextUtils.isEmpty(vodDetail.getBookmark().getSitcomNO()))
                            {
                                for (Episode episode : episodes)
                                {
                                    VOD vod = episode.getVOD();
                                    if (episode.getSitcomNO().equals(vodDetail.getBookmark().getSitcomNO()))
                                    {
                                        mEpisode=episode;
                                        mPlayVodBean.setEpisodeId(vod.getID());
                                        mPlayVodBean.setSitcomNO(episode.getSitcomNO());
                                        List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                                        if (vodMediaFiles != null && vodMediaFiles.size() != 0)
                                        {
                                            VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                            String definition = vodMediaFile.getDefinition();
                                            if (VodUtil.canPlay("2".equals(definition)))
                                            {
                                                playVod(vodMediaFile, vod.getID(), vodMediaFile
                                                    .getElapseTime());
                                            }
                                            else
                                            {
                                                EpgToast.showToast(mContext, Strings.getInstance
                                                    ().getString(R.string.details_4k_warnning));
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                            VOD vod = episodes.get(0).getVOD();
                            mEpisode=episodes.get(0);
                            if(null!=vod) {
                                mPlayVodBean.setEpisodeId(vod.getID());
                                mPlayVodBean.setSitcomNO(episodes.get(0).getSitcomNO());
                                List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                                if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                                    VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                                    String definition = vodMediaFile.getDefinition();
                                    if (VodUtil.canPlay("2".equals(definition))) {
                                        playVod(vodMediaFile, vod.getID(), vodMediaFile.getElapseTime());
                                    }
                                    else{
                                        EpgToast.showToast(mContext, Strings.getInstance().getString(R.string.details_4k_warnning));
                                    }
                                }
                            }

                        }
                    }
                }
            }

        }

        @Override
        public void getVODDetailFailed() {
            EpgToast.showToast(mContext,mContext.getResources().getString(R.string.message_network_disconnect));
        }

        @Override
        public void onError() {
            EpgToast.showToast(mContext,mContext.getResources().getString(R.string.message_network_disconnect));
        }
    };
    private void playVod(VODMediaFile vodMediaFile, String id,String elapseTime) {
        if (vodDetail != null && !vodDetail.getVODType().equals("0")&&null!=mEpisode) {
            mPreviewDuration = VodDetailCacheService.getPreviewDuration(vodDetail.getCustomFields(), mEpisode.getVOD() == null ? null : mEpisode.getVOD().getCustomFields(), VodDetailCacheService.getVodTimeByMediaFiles(mEpisode.getVOD() == null ? null : mEpisode.getVOD().getMediaFiles()));
        }else if(vodDetail !=null ){
            mPreviewDuration= VodDetailCacheService.getPreviewDuration(vodDetail.getCustomFields(),null, VodDetailCacheService.getVodTimeByMediaFiles(vodDetail.getMediaFiles()));
        }
        mPlayVODRequest = new PlayVODRequest();
        mPlayVODRequest.setVODID(id);
        mPlayVODRequest.setMediaID(vodMediaFile.getID());
        mPlayVODRequest.setURLFormat("1");
        mPlayVODRequest.setIsReturnProduct("1");
        mPlayVODControl.getVODPlayUrlEPG(mPlayVODRequest, compose(bindToLifecycle(mContext)),elapseTime);
    }

    private PlayURLCallBack playURLCallBack = new PlayURLCallBack() {
        @Override
        public void getVODPlayUrlSuccess(String playUlr, String bookmark, String productId,String elapseTime) {
            if (!TextUtils.isEmpty(playUlr)) {
                mPlayVodBean.setPlayUrl(playUlr);
                if (vodDetail.getSeries() != null && vodDetail.getSeries().size() > 0) {
                    mPlayVodBean.setFatherSitcomNO(vodDetail.getSeries().get(0).getSitcomNO());
                    mPlayVodBean.setFatherVODId(vodDetail.getSeries().get(0).getVODID());
                }
                if(null!=mPlayVODRequest){
                    mPlayVodBean.setMediaId(mPlayVODRequest.getMediaID());
                }

                if(null!=vodDetail.getSeries()&&vodDetail.getSeries().size()>0&&vodDetail.getVODType().equals("0")){
                    mPlayVodBean.setIsFilm("1");
                }else{
                    mPlayVodBean.setIsFilm(vodDetail.getVODType());
                    BrowseTVPlayFragment.setEpisodesBean(new EpisodesBean(vodDetail.getID(), vodDetail.getEpisodes()));
                }
                mPlayVodBean.setElapseTime(elapseTime);
                mPlayVodBean.setProductId(productId);
                if(null!=fatherVodDetail){
                    mPlayVodBean.setIsSeries(isShowSeriesLayout(fatherVodDetail.getSubjectIDs())?0:1);
                }else{
                    mPlayVodBean.setIsSeries(isShowSeriesLayout(vodDetail.getSubjectIDs())?0:1);
                }

                mPlayVodBean.setTryToSeeFlag(0);
                if(null!=vodDetail) {
                    mPlayVodBean.setDetailStr(VodUtil.getSimpleVoddetail(vodDetail,mPreviewDuration));
                }
                if(null!=mOffScreenListener){
                    mOffScreenListener.offScreenSuccess(mPlayVodBean);
                }
            } else {
                EpgToast.showToast(mContext, "播放地址为空！");
            }
        }

        @Override
        public void getChannelPlayUrlSuccess(String url, String attachedPlayURL, String bookmark) { }

        @Override
        public void playFail() {
            EpgToast.showToast(mContext,mContext.getResources().getString(R.string.message_network_disconnect));
        }

        @Override
        public void playFail(PlayVODResponse playVODResponse) {
            if(null!=mOffScreenListener){
                mOffScreenListener.offScreenFail(vodDetail);
            }
            SuperLog.debug(TAG, "鉴权失败" + playVODResponse.getResult().toString());
            String returnCode = playVODResponse.getResult().getRetCode();
            if (!TextUtils.isEmpty(returnCode) && (returnCode.equals("144020008") || returnCode.equals("504") || returnCode.equals("114020006"))) {
                String playUrl = playVODResponse.getPlayURL();
                if (!TextUtils.isEmpty(playUrl)&&null!=vodDetail) {
                    mPlayVodBean.setPlayUrl(playUrl);
                    mPlayVodBean.setVodId(vodDetail.getID());
                    if (vodDetail.getSeries() != null && vodDetail.getSeries().size() > 0) {
                        mPlayVodBean.setFatherSitcomNO(vodDetail.getSeries().get(0).getSitcomNO());
                        mPlayVodBean.setFatherVODId(vodDetail.getSeries().get(0).getVODID());
                    }
                    vodDetail.setPreviewDuration(mPreviewDuration);
                    if(null!=mPlayVODRequest){
                        mPlayVodBean.setMediaId(mPlayVODRequest.getMediaID());
                    }
                    mPlayVodBean.setVodType("VOD");
                    if(null!=vodDetail.getSeries()&&vodDetail.getSeries().size()>0&&vodDetail.getVODType().equals("0")){
                        mPlayVodBean.setIsFilm("1");
                    }else{
                        mPlayVodBean.setIsFilm(vodDetail.getVODType());
                        BrowseTVPlayFragment.setEpisodesBean(new EpisodesBean(mPlayVodBean.getVodId(), vodDetail.getEpisodes()));
                    }
                    mPlayVodBean.setTryToSeeFlag(1);// 1 试看  鉴权不通过
                    mPlayVodBean.setAuthResult(JsonParse.object2String(playVODResponse.getAuthorizeResult()));
                    if(null!=vodDetail) {
                        mPlayVodBean.setDetailStr(VodUtil.getSimpleVoddetail(vodDetail,mPreviewDuration));
                    }
                    if(null!=fatherVodDetail){
                        mPlayVodBean.setIsSeries(isShowSeriesLayout(fatherVodDetail.getSubjectIDs())?0:1);
                    }else{
                        mPlayVodBean.setIsSeries(isShowSeriesLayout(vodDetail.getSubjectIDs())?0:1);
                    }
                    SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + mPlayVodBean.getVodId() + ",epsodeId:" + mPlayVodBean.getEpisodeId() + ",sitcomNO:" + mPlayVodBean.getSitcomNO() + ",breakpoint:" + mPlayVodBean.getBookmark());
                    Intent intent = new Intent(mContext, OnDemandVideoActivity.class);
                    // 将剧集传入播控
//                            List<Episode> episodes = mVODDetail.getEpisodes();
//                            List<Episode> episodeList;
//                            episodeList = episodes;
//                            if (episodes != null && episodes.size() != 0) {
//                                if (episodes.size() > 1 && Integer.parseInt(episodes.get(0).getSitcomNO()) > Integer.parseInt(episodes.get(1).getSitcomNO())) {
//                                    episodeList = StringUtils.deepCopy(episodes);
//                                    Collections.reverse(episodeList);
//                                }
//                            }

                    if(null!=mOffScreenListener){
                        mOffScreenListener.offScreenSuccess(mPlayVodBean);
                    }
                } else {
                    AuthorizeResult authorizeResult = playVODResponse.getAuthorizeResult();
                    if (null != authorizeResult) {
                        List<Product> products = authorizeResult.getPricedProducts();
                        if (SessionService.getInstance().getSession().isHotelUser()) {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(mContext, R.string.notice_no_orderable_product);
                                return;
                            }
                            for (int i = products.size()-1; i >= 0; i--) {
                                Product mProductInfo = products.get(i);
                                if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                                    products.remove(i);
                                }

                            }
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(mContext, R.string.notice_no_orderable_product);
                                return;
                            }
                            authorizeResult.setPricedProducts(products);

                        } else {
                            if (products == null || products.size() == 0) {
                                EpgToast.showToast(mContext, R.string.notice_no_orderable_product);
                                return;
                            }
                        }

                    } else {
                        EpgToast.showToast(mContext, R.string.notice_no_orderable_product);
                        return;
                    }
                    String authResult = JsonParse.object2String(authorizeResult);

                    String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
                    if (null != needJumpToH5Order && needJumpToH5Order.equals("1")){
                        JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(authorizeResult.getPricedProducts(),mContext,true,false,null,vodDetail);
                        //pbs点击上报
                        SuperLog.debug(TAG, "PbsUaService: "+vodDetail.getID());
                        PbsUaService.report(Detail.getPurchaseData(vodDetail.getID()));
                    }else{
                        Intent intent = new Intent(mContext, NewProductOrderActivity.class);
                        intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, authResult);
                        intent.putExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE,true);
                        intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL,VodUtil.getSimpleVoddetail(vodDetail, 5 * 60));
                        mContext.startActivity(intent);
                    }
                }

            } else if (!TextUtils.isEmpty(returnCode) && returnCode.equals("144020000")) {
                EpgToast.showToast(mContext, R.string.notice_no_orderable_product);
            } else {
                EpgToast.showToast(mContext, "媒资鉴权失败！");
            }
        }

        @Override
        public void onPlaycancel() { }

        @Override
        public void getVODDownloadUrlSuccess(String vodID, String url, String postURL, String switchNum, String name) { }

        @Override
        public void getVODDownloadUrlFailed(String vodID, String episodeID) { }
    };

    public boolean isShowSeriesLayout(List<String> SubjectIds) {
        boolean isSeries = false;
        List<String> terminalSubjectIds = SessionService.getInstance().getSession().getTerminalConfigurationSubjectIDS();
        if (null != SubjectIds && SubjectIds.size() != 0) {
            for (int i = 0; i < terminalSubjectIds.size(); i++) {
                for (int j = 0; j < SubjectIds.size(); j++) {
                    if (terminalSubjectIds.get(i).equals(SubjectIds.get(j))) {
                        return true;
                    }
                }
            }
        }
        return isSeries;
    }

}