/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Detail;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.dialog.ReminderDialog;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.livetv.presenter.PlayUrlPresenter;
import com.pukka.ydepg.moudule.livetv.presenter.contract.PlayUrlContract;
import com.pukka.ydepg.moudule.livetv.ui.LiveTVPlayFragment;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.inf.ControlKeyDownListener;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.service.NtpTimeService;
import com.pukka.ydepg.view.PlayView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 播放器视图界面基类
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: BasePlayFragment
 * @Package com.pukka.ydepg.moudule.base
 * @date 2017/12/21 11:00
 */
public abstract class BasePlayFragment<T extends IBaseContact.IBasePresenter> extends BaseFragment
        implements ControlKeyDownListener, ReminderDialog.OnReminderConfirmListener,
        View.OnClickListener, PlayUrlContract.View, IBaseContact.IBaseView {

    private static final String TAG = "BasePlayFragment";


    public static final String ISPLAYBACK = "ISPLAYBACK";

    /**
     * 处理不同播放页面实现的自身相关业务的Presenter
     */
    protected T presenter;

    /**
     * 提醒对话框
     */
    protected ReminderDialog mReminderDialog;

    /**
     * 订购提示区域
     */
    @BindView(R.id.rl_order_container)
    public RelativeLayout mRlOrderRemind;

    /**
     * 订购按钮
     */
    @BindView(R.id.rl_order_btn)
    public RelativeLayout mBtnOrder;

    /**
     * 播放器视图
     */
    @BindView(R.id.play_view)
    public PlayView mPlayView;

    /**
     * 鉴权失败返回的result信息,内部含可订购列表数据;
     */
    protected String mAuthorizeResult;

    /**
     * VOD详情
     */
    protected VODDetail mVODDetail;

    /**
     * 当前是第几集
     */
    private String mEpisodeIndex;

    /**
     * 当前剧集父集ID
     */
    private String mCurrentSeriesID;

    /**
     * 影片|电视剧名称
     */
    private String mVideoName;

    /**
     * 当前正在播放的频道ID
     */
    protected String mPlayingChannelId;

    private boolean isXmpp;

    private PlayUrlPresenter mPresenter = new PlayUrlPresenter();

    //如果从指定的栏目进入频道，携带栏目ID，用于Reporter按栏目统计用户行为。
    protected String mSubjectID;

    //配置一个栏目id，播放界面只展示频道列表
    protected String mSubjectId;

    //1代表查询子栏目，非1代表不查
    protected String mIsQuerySubject;

    /**
     * 1代表展示频道号，非1代表不展示
     *  频道号非channelNo,而是频道集合的position，从1开始，同时切台的频道号按照此postion，并非之前的channelNo,(勿影响之前原有逻辑)。
     * */
    protected String mIsShowChannelNo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //设置遥控器按键事件监听
        if (activity instanceof LiveTVActivity) {
            ((LiveTVActivity) activity).setOnControlKeyDownListener(this);
        } else if (activity instanceof OnDemandVideoActivity) {
            ((OnDemandVideoActivity) activity).setOnControlKeyDownListener(this);
        }else if (activity instanceof NewVodDetailActivity){
            ((NewVodDetailActivity) activity).setOnControlKeyDownListener(this);
        }
        initPresenter();
        if(presenter != null) {
            presenter.attachView(this);
        }

    }

    protected abstract void initPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReminderDialog = new ReminderDialog(getActivity(), this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mPresenter.detachView();
        if (presenter != null) {
            presenter.detachView();
        }
        mVODDetail = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: 走进Base里面");
        return false;
    }

    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {
    }

    @Override
    public void onConfirm() {
    }

    @OnClick({R.id.rl_order_btn})
    @Override
    public void onClick(View v) {

        String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
        if (null != needJumpToH5Order && needJumpToH5Order.equals("1")){
            boolean isLiveTV = getClass().getName().equals(LiveTVPlayFragment.class.getName());
            AuthorizeResult result= JsonParse.json2Object(mAuthorizeResult,AuthorizeResult.class);
            if (null == result){
                return;
            }
            if (isLiveTV){
                mPlayingChannelId = "";
                JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(result.getPricedProducts(),getActivity(),false,false,LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo(),null);
            }else{
                JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(result.getPricedProducts(),getActivity(),true,false,null,mVODDetail);
            }
            //pbs点击上报
            Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
            PbsUaService.report(Detail.getPurchaseData(mVODDetail.getID()));
        }else{
            Intent intent = new Intent(getActivity(), NewProductOrderActivity.class);
            intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, mAuthorizeResult);
            boolean isLiveTV = getClass().getName().equals(LiveTVPlayFragment.class.getName());
            if (isLiveTV) {
                //点击回看视频播放鉴权失败的时候,不在直播视频播放器界面上面显示订购按钮
                //if (null != mVODDetail) {
                //  SuperLog.info(TAG, "[回看->订购]");
                //  intent.putExtra(ZjYdUniPayActivity.ISTVOD_SUBSCRIBE, true);
                //  intent.putExtra(ZjYdUniPayActivity.VOD_DETAIL,JsonParse.object2String(mVODDetail));
                //} else {
                SuperLog.info(TAG, "[直播->订购]");
                intent.putExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID,
                        LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo());
                //订购回来后，需要重新鉴权，不在判断 频道相同，不在判断业务
                mPlayingChannelId = "";
                //}
            } else {
                SuperLog.info(TAG, "[点播->订购]");
                intent.putExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, true);
                intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60));
            }
            startActivity(intent);
        }
    }

    /**
     * 此处只做直播界面的LIVETV和TVOD鉴权
     */
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEvent(PlayUrlEvent event) {
        SuperLog.debug(TAG, "receiver broadcast->PlayUrlEvent channel");
        CurrentChannelPlaybillInfo playbillInfo = event.getPlaybillInfo();
        if (null != playbillInfo) {
            //频道ID
            String channelID = playbillInfo.getChannelId();
            //媒体资源ID
            String mediaID = playbillInfo.getChannelMediaId();
            //节目单ID
            String playbillID = playbillInfo.getPlaybillId();
            if (!TextUtils.isEmpty(mPlayingChannelId) && mPlayingChannelId.equals(channelID)) {
                //选择了相同的频道时候,鉴权时需要保存频道位置和栏目位置
                LiveDataHolder.get().setMiniEpg(false);
                //选择了相同的频道时候,不做重新鉴权播放操作
                return;
            }
            //临时记录当前正在播放的频道ID
            mPlayingChannelId = channelID;
            //如果当前视频正在播放,释放播放器
            if (null != mPlayView) {
                mPlayView.fastReleasePrepare();
            }
            //记录频道ID,MediaID 用于下次进直播时,播放鉴权的频道信息
            if (!LiveDataHolder.get().getIsShowingSkip())//如果直播播放固定频道，则不记录固定频道的ID
            LiveTVCacheUtil.getInstance().recordPlayChannelInfo(channelID, mediaID);
            //鉴权request
            PlayChannelRequest playChannelRequest = new PlayChannelRequest();
            playChannelRequest.setChannelID(channelID);
            if (!TextUtils.isEmpty(playbillID) && !TextUtils.isEmpty(playbillInfo.getChannelProgramName())) {
                //节目单ID
                playChannelRequest.setPlaybillID(playbillID);
            }
            playChannelRequest.setMediaID(mediaID);
//      playChannelRequest.setIsReturnProduct("1");
            playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
            playChannelRequest.setURLFormat("1");//HLS
            //检索当前切换的频道索引位置
            mPresenter.parseChannelNOIndex(channelID);
            //频道节目单鉴权
            if(event.isTVGuide()){
                mSubjectID = event.getSubjectID();
            }
            mPresenter.playChannel(false, null, playChannelRequest, getActivity(), mSubjectId, mIsQuerySubject, mSubjectID);
            List<String> channelIds = new ArrayList<>();
            channelIds.add(playbillInfo.getChannelId());
            //频道鉴权的同时,获取当前的播放的epg节目用于展示在infobar上
            mPresenter.queryInfoBarPlaybillLite(channelIds, String.valueOf(NtpTimeService.queryNtpTime()), getActivity());
        } else if (event.isTVODSubscribe()) {
            //回看订购完,开始鉴权,注意:这个是订购完才会执行的分支
            mPresenter.playVOD(mEpisodeIndex, mCurrentSeriesID, mVODDetail, getActivity());
        } else if (event.isVODSubscribe()) {
            subscribeAuthoritize();
        } else {
            //回看VOD播放鉴权,注意:这个是播放回看内容的时候鉴权
            mVODDetail = event.getVODDetail();
            if (null != mVODDetail) {
                mEpisodeIndex = event.getSitcomNO();
                mVideoName = event.getVideoName();
                mCurrentSeriesID = event.getSeriesID();
                mPresenter.playVOD(mEpisodeIndex, mCurrentSeriesID, mVODDetail, getActivity());
            }
        }
    }

    public void subscribeAuthoritize() {

    }

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int layoutId();

    /**
     * 点播鉴权成功
     *
     * @param url       播放地址 （鉴权成功返回）
     * @param bookmark  书签
     * @param productId 产品ID
     */
    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId) {
        EventBus.getDefault().post(new FinishPlayUrlEvent());
        SuperLog.info2SD(TAG, "[getVODPlayUrlSuccess] productId:" + productId);
        mAuthorizeResult = "";
        //隐藏订购区域
        mRlOrderRemind.setVisibility(View.GONE);
        //启动点播视频播放
        Intent intent = new Intent(getActivity(), OnDemandVideoActivity.class);
        PlayVodBean playVodBean = new PlayVodBean();
        playVodBean.setPlayUrl(url);
        playVodBean.setVodName(mVideoName);
        if (!TextUtils.isEmpty(mEpisodeIndex) && null != mVODDetail) {
            Episode episode = mVODDetail.getEpisodes().get(Integer.parseInt(mEpisodeIndex));
            if (null != episode && null != episode.getVOD()) {
                playVodBean.setEpisodeId(episode.getVOD().getID());
                if (null != episode.getVOD() && null != episode.getVOD().getMediaFiles() && episode.getVOD().getMediaFiles().size() > 0) {
                    playVodBean.setMediaId(episode.getVOD().getMediaFiles().get(0).getID());
                }
            }
        } else if (null != mVODDetail) {
            playVodBean.setVodId(mVODDetail.getID());
            if (null != mVODDetail.getMediaFiles() && mVODDetail.getMediaFiles().size() > 0) {
                playVodBean.setMediaId(mVODDetail.getMediaFiles().get(0).getID());
            }
        }
//        intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, JsonParse.object2String(playVodBean));
        LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(playVodBean));
        intent.putExtra(LiveTVPlayFragment.ISPLAYBACK, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * URL鉴权失败
     *
     * @param isVOD           是不是回看VOD
     * @param authorizeResult AuthorizeResult转成的jsonString
     */
    @Override
    public void getPlayUrlFailed(String channelId, boolean isVOD, AuthorizeResult authorizeResult, String contentId, String url, String attchUrl) {
        EventBus.getDefault().post(new FinishPlayUrlEvent());
        if (null != authorizeResult) {
            List<Product> products = authorizeResult.getPricedProducts();
            if (SessionService.getInstance().getSession().isHotelUser()) {
                if (products == null || products.size() == 0) {
                    EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                    return;
                }
                for (int i = products.size() - 1; i >= 0; i--) {
                    Product mProductInfo = products.get(i);
                    if (!StringUtils.isSubscribeByDay(mProductInfo)) {
                        products.remove(i);
                    }

                }
                if (CollectionUtil.isEmpty(products)) {
                    EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                    return;
                }
                authorizeResult.setPricedProducts(products);

            } else {
                if (CollectionUtil.isEmpty(products)) {
                    EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
                    return;
                }
            }

        } else {
            EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
            return;
        }

        mAuthorizeResult = JsonParse.object2String(authorizeResult);
        if (!isVOD) {
            SuperLog.info2SD(TAG, "直播播放鉴权失败 >> 释放播放器,显示订购按钮");
            showSubscribeRegion();
            if (null != mPlayView) {
                SuperLog.info2SD(TAG, "release Player");
                mPlayView.releasePlayer();
            }
            //鉴权失败,不是VOD就是直播鉴权失败,在获取鉴权结果的同时,清空voddetail
            mVODDetail = null;
        } else {
            //直接跳转到产品订购列表
            String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
            SuperLog.info2SD(TAG, "回看播放鉴权失败 >> [回看->直接跳转到产品订购列表页]");
            if (null != needJumpToH5Order && needJumpToH5Order.equals("1")){
                JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(authorizeResult.getPricedProducts(),getActivity(),false,true,null,mVODDetail);
                //pbs点击上报
                Log.i(TAG, "PbsUaService: "+mVODDetail.getID());
                PbsUaService.report(Detail.getPurchaseData(mVODDetail.getID()));
            }else{
                Intent intent = new Intent(getActivity(), NewProductOrderActivity.class);
                intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, mAuthorizeResult);
                intent.putExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, true);
                intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, null != mVODDetail ? VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60) : "");
                startActivity(intent);
            }
        }
    }

    /**
     * 显示订购区域
     */
    private void showSubscribeRegion() {
        mRlOrderRemind.setVisibility(View.VISIBLE);
        //设置按钮获取焦点
        mBtnOrder.setFocusable(true);
        mBtnOrder.requestFocus();
    }

    /**
     * playUrlError
     */
    @Override
    public void playUrlError() {
        SuperLog.error(TAG, "playUrlError()");
        mRlOrderRemind.setVisibility(View.GONE);
        EventBus.getDefault().post(new FinishPlayUrlEvent());
        if (null != mPlayView) {
            SuperLog.info2SD(TAG, "release Player");
            mPlayView.releasePlayer();
        }
    }

    /**
     * 直播播放URL鉴权成功
     *
     * @param channelId 频道ID
     * @param url       播放地址
     * @param bookmark  书签
     */
    @Override
    public void getChannelPlayUrlSuccess(String channelId, String url, String bookmark,String attchUrl) {
        EventBus.getDefault().post(new FinishPlayUrlEvent());
    }

    @Override
    public void onCurrentChannelPlayBillSucc(String programName, String syTime, String channelName) { }

    @Override
    public void onCurrentChannelPlayBillError() { }

    @Override
    public void onSwitchChannelNO(int switchIndex, String channelNO) { }

    @Override
    public void onSwitchChannelIndex(int switchIndex) { }


    public PlayUrlPresenter getPresenter(){
        return mPresenter;
    }

    public void setIsXmpp(boolean boo){
        isXmpp = boo;
    }

    public boolean getIsXmpp(){
        return isXmpp;
    }
}