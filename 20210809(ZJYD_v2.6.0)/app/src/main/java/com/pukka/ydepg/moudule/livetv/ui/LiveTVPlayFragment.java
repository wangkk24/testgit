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
package com.pukka.ydepg.moudule.livetv.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.FrameLayoutExt;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.bean.node.XmppMessage;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.SubMutex;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetProductMutExRelaResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.profile.ProfileManager;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Detail;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Live;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.productUtil.ProductUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.control.selfregister.SelfAppInfoController;
import com.pukka.ydepg.event.DisableTVGuideKeyEvent;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.event.TVGuideEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BasePlayFragment;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.AvoidRepeatPaymentUtils;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.mytv.presenter.OrderCenterPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.OrderUserGroupPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.ProductOrderPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.contract.ProductOrderContract;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderCenterView;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;
import com.pukka.ydepg.moudule.voice.VoiceEvent;
import com.pukka.ydepg.service.NtpTimeService;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.xmpp.bean.XmppSuccessEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.pukka.ydepg.event.PlaySubListNo1ChannelEvent;

/**
 * 直播视频播放界面,可以通过遥控器中的BTV(直播),TVOD(点播)进入
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: LiveTVPlayFragment
 * @Package com.pukka.ydepg.moudule.livetv
 * @date 2017/12/21 11:05
 */
public class LiveTVPlayFragment extends BasePlayFragment implements IPlayListener, ProductOrderContract.View, OrderUserGroupPresenter.GetUserGroupCallback, OrderCenterView {

    private static final String TAG = LiveTVPlayFragment.class.getSimpleName();

    /**
     * 隐藏infobar
     */
    private static final int MSG_HIDE_CHANNEL_MENU = 0x119;

    /**
     * 显示频道页TVGUIDE
     */
    private static final int MSG_SHOW_EPG_VIEW = 0x120;

    /**
     * 隐藏频道号
     */
    private static final int MSG_HIDE_CHANNELNO = 0x121;

    /**
     * 数字选台
     */
    private static final int NUMBER_SWITCH_CHANNEL = 0x122;

    /**
     * 上下键快速切台
     */
    private static final int FAST_SWITCH_CHANNEL = 0x123;

    /**
     * 定时显示TVGUIDE页面
     */
    private static final int SHOW_TVGUIDE_DELAY_TIME = 1000;

    /**
     * 定时隐藏定点选台的台号
     */
    private static final int DELAY_HIDE_CHANNELNO = 2000;

    /**
     * 快速延时
     */
    private static final int FAST_DELAY_TIME = 200;

    /**
     * 显示订购界面
     */
    private static final int SHOW_ORDER_LAYOUT = 0x124;

    /**
     * infoView视图区域
     */
    @BindView(R.id.fm_channel_menu)
    FrameLayoutExt mChannelMenu;

    /**
     * 节目单名字
     */
    @BindView(R.id.tv_playbill)
    TextView mPlaybillName;

    /**
     * 总时长
     */
    @BindView(R.id.tv_totaltime)
    TextView mTotalTime;

    /**
     * 频道名字
     */
    @BindView(R.id.tv_channelname)
    TextView mChannelName;

    /**
     * infobar分割线
     */
    @BindView(R.id.tv_line)
    TextView mInfoBarLine;

    /**
     * 切换频道显示在右上角的频道号
     */
    @BindView(R.id.tv_switch_channel)
    TextView mSwitchChannel;

    @BindView(R.id.rl_order_container_layout)
    RelativeLayout mProductOrderLayout;

    @BindView(R.id.player_order_product_name)
    TextView mProductName;

    @BindView(R.id.player_order_product_price)
    TextView mProductPrice;

    @BindView(R.id.player_order_product_old_price)
    TextView mProductOldPrice;

    @BindView(R.id.player_order_product_desc)
    TextView mProductDesc;

    @BindView(R.id.player_order_product_button)
    ImageView mProductOrderButton;

    @BindView(R.id.player_order_product_price_unit)
    TextView mProductPriceUnit;

    @BindView(R.id.player_order_product_cancel_button)
    ImageView mProductOrderCancelButton;
    @BindView(R.id.live_order_bg)
    ImageView mLiveOrderBg;

    private LiveHandler mHandler = new LiveHandler(this);

    /**
     * 切换的频道号索引位置
     */
    private volatile int mSwitchIndex = 0;

    /**
     * 当前是不是切换到了数字键
     */
    private volatile boolean isSwitchNumberKey;

    /**
     * 是否禁用按键事件
     */
    private volatile boolean isDisableKeyEvent;

    private boolean isWitchMultiCast;

    /**
     * 视频流是否准备完成
     */
    private volatile boolean isPrepared;

    private List<ChannelDetail> mChannelPlayList;

    /**
     * 定时隐藏infobar时间,默认市场5秒
     */
    private int mDelayHideChannelInfoBarTime = 5000;
    //用来记录数字键选择的台号
    //private String mStrChannelNoForKeyCode;

    //是否是切台
    private boolean mIsSwitchChannel = false;

    //channel上报

    //播放/切换前的频道ID。
    private String mChannelID;
    //如果action=2，表示切换后的频道ID，此时必传。
    private String mNextChannelID;
    //播放/切换前的媒资ID。
    private String mMediaID;
    //如果action=2，表示切换后的媒资ID，此时必传。
    private String mNextMediaID;
    //鉴权通过的产品ID
    private String mProductID;
    //记录上一个节目id
    private String mPreChannelID;
    //记录上一个媒资id
    private String mPreMediaID;

    //当前正在播放直播的第三方应用内容code
    private String contentCode;

    //点击资源位，==true:只播放某一个频道，不能切台，不现实EPG。。。
    private boolean mIsOnlyChannel = false;


    /**
     * 当前正在播放的url,甩屏需要。
     */
    private String currentUrl;

    private String currentAttchUrl;

    private boolean isPlayAttchUrl = false;


    private int currentPosition;

    //连续快进
    private long rightTime;

    //连续快退
    private long leftTime;

    //快进速度控制
    private int countForward;

    //总时长
    private long totalTimes;
    /**
     * 手动停止
     */
    private boolean isKeyStop;

    private long rewindRate = 60 * 1000L;

    private int videoType;

    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(PlayUtil.TIME_FORMAT_N);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
     * 营销活动产品
     */
    private Product marketingProduct;

    private Marketing productMarketing;

    //展示营销活动的用户分组id
    private String groupId;

    //用户已经订购的订购offerid 和 tag id
    private List<OfferInfo> offerInfos;

    //是否已经订购
    private boolean isOrdered;
    //是否是第一次进入
    private boolean firstInto = true;

    @Override
    public <Z> LifecycleTransformer<Z> bindToLife() {
        return bindToLifecycle();
    }

    private static class LiveHandler extends Handler {

        private WeakReference<BasePlayFragment> mReference;


        LiveHandler(BasePlayFragment basePlayFragment) {
            mReference = new WeakReference<>(basePlayFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mReference && null != mReference.get()) {
                LiveTVPlayFragment tvPlayFragment = (LiveTVPlayFragment) mReference.get();
                if (msg.what == MSG_HIDE_CHANNEL_MENU) {
                    //隐藏频道菜单
                    tvPlayFragment.mChannelMenu.setVisibility(View.GONE);
                } else if (msg.what == MSG_SHOW_EPG_VIEW) {
                    //显示TVGuide
                    EventBus.getDefault().post(new TVGuideEvent(true));
                } else if (msg.what == MSG_HIDE_CHANNELNO) {
                    //清空台号
                    tvPlayFragment.mSwitchChannel.setText("");
                    tvPlayFragment.delayInfoBar();
                } else if (msg.what == NUMBER_SWITCH_CHANNEL) {
                    tvPlayFragment.switchChannel(true);
                } else if (msg.what == FAST_SWITCH_CHANNEL) {
                    tvPlayFragment.switchChannel(false);
                } else if (msg.what == ViewkeyCode.VIEW_KEY_CONTINUE_PLAY) {
                    tvPlayFragment.mPlayView.resumePlay();
                    tvPlayFragment.mPlayView.showControlView(true);
                } else if (msg.what == ViewkeyCode.VIEW_KEY_EXIT) {
                    if (null != tvPlayFragment.getActivity()) {
                        tvPlayFragment.getActivity().finish();
                    }
                } else if (msg.what == SHOW_ORDER_LAYOUT) {
                    tvPlayFragment.showSubscribeLayout();
                    if (null != tvPlayFragment.mPlayView) {
                        SuperLog.info2SD(TAG, "release Player");
                        tvPlayFragment.mPlayView.pausePlay();
                    }
                }
            }
        }
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     */
    private void sendEmptyMessageDelayed(int what, long delayMillis) {
        mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    private void removeMessages(int what) {
        mHandler.removeMessages(what);
    }

    @Override
    protected void initPresenter() {
        mOrderCenterPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (null != getArguments().get(LiveTVActivity.KEY_SUBJECT_ID)) {
            mSubjectId = getArguments().getString(LiveTVActivity.KEY_SUBJECT_ID);
            Log.e("gwptest", "subjectID is " + mSubjectId);
            mIsQuerySubject = getArguments().getString(LiveTVActivity.KEY_QUERY_SUBJECT);
            mIsShowChannelNo = getArguments().getString(LiveTVActivity.KEY_SHOW_CHANNEL_NO);
        }

        initListener();
        return view;
    }

    private boolean canClick = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProductOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mProductOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //防止短时间快速点击
                if (!canClick) {
                    return;
                }
                canClick = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canClick = true;
                    }
                }, 5000);

                //判断是否重复订购
                boolean canpay = AvoidRepeatPaymentUtils.getInstance().canPay(mProductInfo.getID());
                if (!canpay) {
                    EpgToast.showToast(getActivity(), "正在订购……");
                    return;
                }

                mPlayingChannelId = "";
//                ProductUtils.checkSubscribeProductsType((LiveTVActivity) getActivity(), new ProductUtilCallback() {
//                    @Override
//                    public void onCheckPackageRelationshipSuccess(Map<String, List<String>> params) {
//                        //大包已订购，不能订购
//                        if (null == params) {
////                            EpgToast.showToast(OTTApplication.getContext(),
////                                    getResources().getString(R.string.big_small_product_conflict));
//                            String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
//                            if (null != mutexStr && mutexStr.length() > 0) {
//                                EpgToast.showToast(OTTApplication.getContext(),
//                                        mutexStr);
//                            } else {
//                                EpgToast.showToast(OTTApplication.getContext(),
//                                        getResources().getString(R.string.big_small_product_mutex));
//                            }
//                            return;
//                        } else {
//                            unsubProdInfoIds = params.get(ProductUtils.SUBSCRIBED_PRODUCT_ID);
//                        }
//                        productOrderPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
//
//                    }
//
//                    @Override
//                    public void onQueryProductInfoFailed() {
//                    }
//
//                    @Override
//                    public void onQueryProductEmpty() {
//                        productOrderPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
//                    }
//                }, mProductInfo.getID());

                if (null != unsubProdInfoIds) {
                    unsubProdInfoIds.clear();
                } else {
                    unsubProdInfoIds = new ArrayList<>();
                }
                if (mProductInfo.getProductType().equals("0")) {
                    //包周期产品判断互斥关系
                    ProductUtils.getProductMutExRela((RxAppCompatActivity) getActivity(), new SelfAppInfoController.GetProductMutExRelaCallBack() {
                        @Override
                        public void GetProductMutExRelaCallBackSuccess(GetProductMutExRelaResponse response) {
                            List<SubMutex> list = response.getSubMutexs();
                            if (null != list && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    SubMutex subMutex = list.get(i);
                                    if (subMutex.getRuleType().equals(SubMutex.RULE_TYPE_MUTEX)) {
                                        String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
                                        if (null != mutexStr && mutexStr.length() > 0) {
                                            EpgToast.showToast(OTTApplication.getContext(),
                                                    mutexStr);
                                        } else {
                                            EpgToast.showToast(OTTApplication.getContext(),
                                                    getResources().getString(R.string.big_small_product_mutex));
                                        }
                                        return;
                                    } else if (subMutex.getRuleType().equals(SubMutex.RULE_TYPE_REPLACE)) {
                                        unsubProdInfoIds.add(subMutex.getProductID());
                                    }
                                }
                            }

                            productOrderPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
                        }

                        @Override
                        public void GetProductMutExRelaCallBackFailed() {
                        }
                    }, mProductInfo, mVODDetail);
                } else {
                    productOrderPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
                }
            }
        });

        mProductOrderCancelButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderCancelSelected())) {
                        GlideApp.with(LiveTVPlayFragment.this).load(LiveDataHolder.get().getmLiveOrderCancelSelected()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mProductOrderCancelButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_cancle_selected));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mProductOrderCancelButton.setImageDrawable(resource);
                                return false;
                            }
                        }).into(mProductOrderCancelButton);
                    } else {
                        mProductOrderCancelButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_cancle_selected));
                    }

                } else {
                    if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderCancelNormal())) {
                        GlideApp.with(LiveTVPlayFragment.this).load(LiveDataHolder.get().getmLiveOrderCancelNormal()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mProductOrderCancelButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_cancle_normal));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mProductOrderCancelButton.setImageDrawable(resource);
                                return false;
                            }
                        }).into(mProductOrderCancelButton);
                    } else {
                        mProductOrderCancelButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_cancle_normal));
                    }

                }
            }
        });

        mProductOrderButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderSureSelected())) {
                        GlideApp.with(LiveTVPlayFragment.this).load(LiveDataHolder.get().getmLiveOrderSureSelected()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mProductOrderButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_selected));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mProductOrderButton.setImageDrawable(resource);
                                return false;
                            }
                        }).into(mProductOrderButton);
                    } else {
                        mProductOrderButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_selected));
                    }

                } else {
                    if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderSureNormal())) {
                        GlideApp.with(LiveTVPlayFragment.this).load(LiveDataHolder.get().getmLiveOrderSureNormal()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mProductOrderButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_normal));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                mProductOrderButton.setImageDrawable(resource);
                                return false;
                            }

                        }).into(mProductOrderButton);
                    } else {
                        mProductOrderButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_normal));
                    }

                }
            }
        });

        mProductOrderCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductOrderLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productOrderPresenter.attachView(this);
    }

    /**
     * 布局Id
     */
    @Override
    protected int layoutId() {
        return R.layout.fragment_livetv;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDisableKeyEvent = true;
        if (null != getArguments().get(LiveTVActivity.CHANNEL_FINAL)) {
            mIsOnlyChannel = (boolean) getArguments().get(LiveTVActivity.CHANNEL_FINAL);
        }
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_EPG_VIEW, SHOW_TVGUIDE_DELAY_TIME);
        String infobarDelayTime = SessionService.getInstance().getSession()
                .getTerminalConfigurationValue(Configuration.Key.LIVE_TV_INFOBAR_DELAY_TIME);
        if (!TextUtils.isEmpty(infobarDelayTime)) {
            int delayTime;
            try {
                delayTime = Integer.parseInt(infobarDelayTime);
            } catch (NumberFormatException e) {
                SuperLog.error(TAG, e);
                delayTime = mDelayHideChannelInfoBarTime;
            }
            mDelayHideChannelInfoBarTime = delayTime;
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mPlayView.setShouldAutoPlay(true);
        mPlayView.setOnPlayCallback(this);
        if (!TextUtils.isEmpty(getArguments().getString(LiveTVActivity.PLAY_URL_ONLY))) {
            if (mPlayView != null) {
                mPlayView.releasePlayer();
                mPlayView.startPlay(getArguments().getString(LiveTVActivity.PLAY_URL_ONLY));
            }
        } else {
            mChannelPlayList = LiveDataHolder.get().getChannelPlay();
            //播放鉴权默认频道
            //mPresenter.playDefaultChannel();
            getPresenter().queryColumnList(getActivity(), mSubjectId, mIsQuerySubject);
        }
//        mPlayView.removeDefaultPlayControlView();

    }

    @Override
    public void onPause() {
        if (isTSTVState()) {
            mPlayView.pausePlay();
        } else {
            mPlayView.releasePlayer();
        }
        super.onPause();
        refreshLIVEStatus(false);
    }

    @Override
    public void onResume() {
        isPrepared = false;
        if (mProductOrderLayout.getVisibility() != View.VISIBLE) {
            if (isTSTVState()) {
                if (!isKeyStop) {
                    mPlayView.resumePlay();
                }
            } else {
                mPlayView.rePlay();
            }
        }
        super.onResume();
        refreshLIVEStatus(true);
    }

    /**
     * 数字切台
     * 换台,变得是channelNo
     *
     * @param codeValue 按键值
     */
    private void numberSwitchChannel(String codeValue) {
        removeMessages(FAST_SWITCH_CHANNEL);
        removeMessages(MSG_HIDE_CHANNELNO);
        if (mSwitchChannel.length() >= 4 || !isSwitchNumberKey) {
            //超出4位或者当前上一个按键不是按的数字键,先清空然后重新append台号
            mSwitchChannel.setText("");
        }
        isSwitchNumberKey = true;
        mSwitchChannel.append(String.valueOf(codeValue));
        removeMessages(NUMBER_SWITCH_CHANNEL);
        //发送清空台号的message
        sendEmptyMessageDelayed(NUMBER_SWITCH_CHANNEL, DELAY_HIDE_CHANNELNO);
    }

    /**
     * 是否显示
     *
     * @return
     */
    public boolean isCanShowSeekBar() {

        return PlayUtil.VideoType.TSTV == videoType && mChannelMenu.getVisibility() != View.VISIBLE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int codeValue = RemoteKeyEvent.getInstance().getVODKeyCodeValue(keyCode);
        if (isDisableKeyEvent) {
            if (mIsOnlyChannel && RemoteKeyEvent.getInstance().getBTVKeyCodeValue(keyCode) == RemoteKeyEvent.CHANNEL_MENU) {
                delayInfoBar();
            }
            return false;
        }
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            if (mIsOnlyChannel) return true;
            //切换台号
            if (TextUtils.equals(mIsShowChannelNo, "1") || TextUtils.isEmpty(mSubjectId)) {
                mChannelMenu.setVisibility(View.GONE);
                numberSwitchChannel(String.valueOf(RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode)));
            }
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER ||
                codeValue == RemoteKeyEvent.MEDIA_PAUSE_PLAY || codeValue == KeyEvent
                .KEYCODE_MEDIA_STOP) && isCanShowSeekBar()) {
//            if(mPlayView.isPlaying()&&null!=mPlayBackView&&!mPlayBackView.isShowing())
//            {
//                mPlayBackView.showPlayBack(getView(), true, null);
//            }
            if (mPlayView.isPlaying()) {
                isKeyStop = true;
            } else {
                isKeyStop = false;
            }
            if (isOrdered) {
                mPlayView.playerOrPause();
                mPlayView.showControlView(true);
            }

        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent
                .KEYCODE_PAGE_DOWN) && isCanShowSeekBar()) {
            if (isOrdered) {
                //快进
                SuperLog.debug(TAG, "keycode_drap_right");
                countForward += 1;//快进速度控制
                if (totalTimes <= 0) {
                    return false;
                }
                keyCodeDpadRight();
            }

        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP) && isCanShowSeekBar()) {
            if (isOrdered) {
                //快退
                countForward += 1;//快退速度控制
                if (totalTimes <= 0) {
                    return false;
                }
                keyCodeDpadLeft();
            }
        } else {//其他情况的keyCode事件处理
            int liveCode = RemoteKeyEvent.getInstance().getBTVKeyCodeValue(keyCode);
            if (liveCode == RemoteKeyEvent.CHANNEL_MENU) {
                delayInfoBar();
            } else if (liveCode == RemoteKeyEvent.CHANNEL_DOWN) {
                if (mIsOnlyChannel) return true;
                //切换频道,上一个频道
                if (!TextUtils.isEmpty(mSubjectId)) {
                    getPresenter().switchChannelNO(false, mSwitchIndex, mSubjectID, null);
                } else {
                    getPresenter().switchChannelNO(false, mSwitchIndex);
                }
            } else if (liveCode == RemoteKeyEvent.CHANNEL_UP) {
                if (mIsOnlyChannel) return true;
                //切换频道,一下个频道
                if (!TextUtils.isEmpty(mSubjectId)) {
                    getPresenter().switchChannelNO(true, mSwitchIndex, mSubjectID, null);
                } else {
                    getPresenter().switchChannelNO(true, mSwitchIndex);
                }
            }
        }
        return false;
    }

    private void keyCodeDpadLeft() {

        if (countForward < 3) {
            leftTime = leftTime + rewindRate;
        } else if (countForward < 5) {
            leftTime = leftTime + rewindRate * 2;
        } else if (countForward < 7) {
            leftTime = leftTime + rewindRate * 3;
        } else if (countForward < 9) {
            leftTime = leftTime + rewindRate * 4;
        } else if (countForward < 11) {
            leftTime = leftTime + rewindRate * 5;
        } else {
            leftTime = leftTime + rewindRate * 6;
        }
        mPlayView.showControlView(true);
        currentPosition = (int) mPlayView.getCurrentPosition();
        if (currentPosition - leftTime <= 0) {
            mPlayView.dragProgress(1);
        } else {
            mPlayView.dragProgress((int) (currentPosition - leftTime));
        }
    }


    private void keyCodeDpadRight() {

        if (countForward < 3) {
            rightTime = rightTime + rewindRate;
        } else if (countForward < 5) {
            rightTime = rightTime + rewindRate * 2;
        } else if (countForward < 7) {
            rightTime = rightTime + rewindRate * 3;
        } else if (countForward < 9) {
            rightTime = rightTime + rewindRate * 4;
        } else if (countForward < 11) {
            rightTime = rightTime + rewindRate * 5;
        } else {
            rightTime = rightTime + rewindRate * 6;
        }
        currentPosition = (int) mPlayView.getCurrentPosition();


        if (currentPosition + rightTime >= totalTimes) {
            mPlayView.dragProgress((int) totalTimes);
        } else {
            mPlayView.dragProgress((int) (currentPosition + rightTime));

        }
        mPlayView.showControlView(true);
    }


    /**
     * 此处只针对上下键快速切台
     */
    @Override
    public void onKeyUp(int keyCode, KeyEvent event) {
        if (isDisableKeyEvent) return;
        if (!(keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)) {
            int liveCode = RemoteKeyEvent.getInstance().getBTVKeyCodeValue(keyCode);
            if (liveCode == RemoteKeyEvent.CHANNEL_DOWN || liveCode == RemoteKeyEvent.CHANNEL_UP) {
                removeMessages(FAST_SWITCH_CHANNEL);
                //延时发送快速切台
                sendEmptyMessageDelayed(FAST_SWITCH_CHANNEL, FAST_DELAY_TIME);
            }
        }
        countForward = 0;
        if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_PAGE_DOWN) &&
                isCanShowSeekBar() && rightTime != 0) {
            if (isOrdered) {
                //            if(currentPosition + rightTime >= totalTimes){
//                rightTime=totalTimes-currentPosition;
//            }
                forWard(rightTime / 1000);
                mPlayView.restartUpdateProgress();
            }

        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_PAGE_UP)
                && isCanShowSeekBar() && leftTime != 0) {
            if (isOrdered) {
                if (currentPosition - leftTime <= 0) {
                    leftTime = currentPosition;
                }
                backForward(leftTime / 1000);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPlayView.restartUpdateProgress();
                    }
                }, 1000);
            }
        }
    }

    //快进多少秒
    public void forWard(long time) {
        isProgressSeek = true;
        mPlayView.fastForward(time * 1000);
        if (!mPlayView.isPlaying() && isPrepared) {
            mPlayView.resumePlay();
        }
//        getCurrentPlayBill(totalTimes-currentPosition-rightTime);
        rightTime = 0;

    }

    //后退多少秒
    public void backForward(long time) {
        isProgressSeek = true;
        mPlayView.showControlView(true);
        mPlayView.rewind(time * 1000);
        if (!mPlayView.isPlaying() && isPrepared) {
            mPlayView.resumePlay();
        }
        leftTime = 0;
    }


    Disposable mSpDisposable;

    @Subscribe
    public void onEvent(VoiceEvent event) {
        if (event.getAction().equals(VoiceEvent.SWITCH_CHANNEL)) {
            if (null != mSpDisposable) {
                mSpDisposable.dispose();
                mSpDisposable = null;
            }
            mSpDisposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> observableEmitter) throws Exception {
                    boolean isExist = false;
                    String[] infos = LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo();
                    if (null != infos && infos.length == 2) {
                        String channelId = infos[0];
                        List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();
                        int channelIndex = LiveUtils.parseChannelDetailIndex(channelId, channelPlay);
                        if (channelIndex != -1) {
                            isExist = true;
                        } else {
                            isExist = false;
                        }
                        observableEmitter.onNext(isExist);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean isExist) throws Exception {
                            if (isExist) {
                                getPresenter().playDefaultChannel(getActivity(), mSubjectId, mIsQuerySubject);
                                EventBus.getDefault().post(new TVGuideEvent(true));
                            } else {
                                EpgToast.showLongToast(OTTApplication.getContext(), "该用户无权限观看此频道");
                            }
                        }
                    });
        }
    }

    @Override
    public void onPlayState(int playbackState) {
        SuperLog.info2SD(TAG, "Time[OnPlayState]=" + System.currentTimeMillis() + " playstate=" + playbackState);
        if(TextUtils.isEmpty(getArguments().getString(LiveTVActivity.PLAY_URL_ONLY))) {
            if (!isPrepared && playbackState == IPlayState.PLAY_STATE_HASMEDIA) {
                isPrepared = true;
                totalTimes = mPlayView.getDuration();
                if (firstInto) {
                    delayInfoBar();
                    firstInto = false;
                }
            } else if (playbackState == IPlayState.PLAY_STATE_NOMEDIA) {


                SuperLog.info2SDDebug(TAG, "currentUrl.startsWith(\"http\") is " + (currentUrl.startsWith("http")));
                SuperLog.info2SDDebug(TAG, "isPlayAttchUrl is " + isPlayAttchUrl);
                if (!currentUrl.startsWith("http") && !isPlayAttchUrl) {

                    isWitchMultiCast = true;

                    LiveDataHolder.get().multicastPlayFail();

                    Schedule schedule = LiveUtils.findScheduleById(mNextChannelID == null ? mChannelID : mNextChannelID);
                    //默认时移时长2小时,此处不是需求,是解决静态静态检查问题,如果测试发现问题,需要修改默认时移时长
//                mPlayView.setIptvlength( schedule == null ? 2L*60*60*1000 : schedule.getPltvLength()*1000L);
//                SuperLog.info2SDDebug(TAG, "currentAttachUrl is " + currentAttchUrl);
//                StringBuilder sb = new StringBuilder().append(currentAttchUrl).append("&").append(PlayUtil.REQUEST_PARAMETER).append(mSimpleDateFormat.format(new Date(NtpTimeService.queryNtpTime())));
//                PlayUtil.setIptvUrl(sb.toString());
//                SuperLog.info2SDDebug(TAG, "IptvUrl is " + PlayUtil.getIptvUrl());
//                mPlayView.restartTSTVurl();
//                mInfoBarLine.setText(" | ");
//                isPlayAttchUrl = true;

                    currentUrl = changeTSTVUrlToTVUrl(currentAttchUrl);

                    if (currentUrl.contains(MulticastOpen)) {
                        currentUrl = currentUrl.replace(MulticastOpen, MulticastClose);
                    } else if (!currentUrl.contains(MulticastClose)) {
                        if (currentUrl.contains("?")) {
                            currentUrl = currentUrl + "&" + MulticastClose;
                        } else {
                            currentUrl = currentUrl + "?" + MulticastClose;
                        }
                    }

                    //拼接profileId
                    if (!currentUrl.contains(getProfileid())) {
                        currentUrl = currentUrl + getProfileid();
                    }

                    mPlayView.setIptvlength(schedule == null ? 2L * 60 * 60 * 1000 : schedule.getPltvLength() * 1000L);
                    SuperLog.info2SDDebug(TAG, "currentAttachUrl is " + currentAttchUrl);
                    StringBuilder sb = new StringBuilder().append(currentAttchUrl).append("&").append(PlayUtil.REQUEST_PARAMETER).append(mSimpleDateFormat.format(new Date(NtpTimeService.queryNtpTime())));
                    PlayUtil.setIptvUrl(sb.toString());
                    mInfoBarLine.setText(" | ");
                    SuperLog.info2SDDebug(TAG, "IptvUrl is " + PlayUtil.getIptvUrl());

                    if (isOldChannelTSTVstate) {
                        mPlayView.releasePlayer();
                        mPlayView.startPlay(currentUrl);
                    } else {
                        if (!mIsSwitchChannel && DeviceInfo.isMGV2000()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SuperLog.debug(TAG, "is MGV2000,Delay 500ms to play");
                                    mPlayView.releasePlayer();
                                    mPlayView.fastPlayVideo(currentUrl);
                                }
                            }, 500);
                        } else {
                            mPlayView.releasePlayer();
                            mPlayView.fastPlayVideo(currentUrl);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPrepared(int videoType) {
        SuperLog.info2SD(TAG, "mIsSwitchChannel=" + mIsSwitchChannel + "---isWitchMultiCast=" + isWitchMultiCast + "---isCurrentChannelTSTVState=" + isCurrentChannelTSTVState + "----issupportFcc=" + OTTApplication.getContext().isSupportMulticast());
        if (mIsSwitchChannel) {
            //切换频道
            //当前频道赋值
            boolean isSwitch = !TextUtils.equals(mNextChannelID, mPreChannelID);
            mPreChannelID = mNextChannelID;
            mPreMediaID = mNextMediaID;
            SuperLog.info2SD(TAG, "isSwitch=" + isSwitch + "---isWitchMultiCast=" + isWitchMultiCast + "----isTSTVState()=" + isTSTVState());
            if (isSwitch) {
                //reportChannel(ReportChannelRequest.ACTION_SWITCH);
                if (isWitchMultiCast) {
                    reportChannel(ReportChannelRequest.ACTION_TSTV);
                    isWitchMultiCast = false;
                } else {
                    //如果是时移action传3
                    if (isTSTVState()) {
                        reportChannel(ReportChannelRequest.ACTION_TSTV);
                    } else {
                        reportChannel(ReportChannelRequest.ACTION_SWITCH);
                    }
                }
            } else if ((isCurrentChannelTSTVState && videoType != PlayUtil.VideoType.TSTV) || (!isCurrentChannelTSTVState && videoType == PlayUtil.VideoType.TSTV) || isProgressSeek) {
                isCurrentChannelTSTVState = isTSTVState();
                isProgressSeek = false;
                reportChannel(ReportChannelRequest.ACTION_TSTV);
            }
        } else {
            //开始播放
            if (isWitchMultiCast) {
                reportChannel(ReportChannelRequest.ACTION_TSTV);
                isWitchMultiCast = false;

            } else {
                reportChannel(ReportChannelRequest.ACTION_PLAY);
            }
        }
        mIsSwitchChannel = true;
    }

    @Override
    public void onRelease() {
        //停止播放
//        if (mIsSwitchChannel) {
//            reportChannel(ReportChannelRequest.ACTION_STOP);
//        }
    }

    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
//        EpgToast.showLongToast(getContext(), "on play error, current url is http");
        if (!getIsXmpp()) {
            EpgToast.showToast(OTTApplication.getContext(), msg);
        }
    }

    @Override
    public void onPlayCompleted() {
    }

    @Override
    public void onDetached(long time) {
    }

    @Override
    public void onAttached() {
    }

    @Override
    public void onTryPlayForH5() {
    }

    @Override
    public void onAdVideoEnd() {

    }

    boolean isOldChannelTSTVstate = false;
    boolean isProgressSeek = false;
    boolean isCurrentChannelTSTVState = false;

    /**
     * 频道鉴权成功
     *
     * @param channelId 频道ID
     * @param url       播放地址
     * @param bookmark  书签
     */
    @Override
    public void getChannelPlayUrlSuccess(String channelId, String url, String bookmark, String attchUrl) {
        super.getChannelPlayUrlSuccess(channelId, url, bookmark, attchUrl);
        isOrdered = true;
        SuperLog.info2SDDebug(TAG, "isSupportFcc()==" + OTTApplication.getContext().isSupportMulticast());
        if (OTTApplication.getContext().isSupportMulticast()) {
            //FCC功能
            url = addFccStr(channelId, url);
        } else {
            if (url.contains("?")) {
                url = url + "&" + MulticastClose;
            } else {
                url = url + "?" + MulticastClose;
            }
        }

        //拼接profileId
        url = url + getProfileid();

        mHandler.removeMessages(SHOW_ORDER_LAYOUT);
        SuperLog.info2SDDebug(TAG, "ChannelUrl=" + url + "\tAttachUrl=" + attchUrl + "\tchannelId=" + channelId);
        SuperLog.info2SD(TAG, "Time[startPlayBegin]=" + System.currentTimeMillis());
        currentUrl = url;
        currentAttchUrl = attchUrl;
        isPlayAttchUrl = false;
        String[] infos = LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo();
        String mediaID = "";
        if (null != infos && infos.length == 2) {
            mediaID = infos[1];
        }
        //第一次播放
        if (!mIsSwitchChannel) {
            mMediaID = mediaID;
            mChannelID = channelId;
            mPreMediaID = mediaID;
            mPreChannelID = channelId;
        }
        //切换频道播放
        else {
            mChannelID = mPreChannelID;
            mNextChannelID = channelId;
            mMediaID = mPreMediaID;
            mNextMediaID = mediaID;
        }
        mAuthorizeResult = "";
        mRlOrderRemind.setVisibility(View.GONE);
        mProductOrderLayout.setVisibility(View.GONE);
        isPrepared = false;
        isOldChannelTSTVstate = isTSTVState();
        isProgressSeek = false;
        isCurrentChannelTSTVState = false;

        Schedule schedule = LiveUtils.findScheduleById(channelId);
        if (null != schedule) {
            SuperLog.debug(TAG, "getChannelPlayUrlSuccess：PltvLength" + schedule.getPltvLength());
        }
        if (!TextUtils.isEmpty(currentUrl) && !currentUrl.startsWith("http") && !isPlayAttchUrl) {
            mInfoBarLine.setText(" || ");
        } else {
            mInfoBarLine.setText(" | ");
        }
        setScheduleState(schedule, attchUrl);
        if (videoType == PlayUtil.VideoType.TSTV) {
            mPlayView.setIptvlength(schedule.getPltvLength() * 1000);
            StringBuilder sb = new StringBuilder().append(attchUrl).append("&").append(PlayUtil.REQUEST_PARAMETER).append(mSimpleDateFormat.format(new Date(NtpTimeService.queryNtpTime())));
            PlayUtil.setIptvUrl(sb.toString());
        }

        SuperLog.info2SD(TAG, "test append1=" + currentUrl.startsWith("http") + "---2=" + (!SharedPreferenceUtil.getBoolData("MULTICAST_SWITCH", false) || !LiveDataHolder.get().canPlayMulticast() || !OTTApplication.getContext().isSupportMulticast()) + "---3=" + !SharedPreferenceUtil.getBoolData("MULTICAST_SWITCH", false) + "---4=" + !LiveDataHolder.get().canPlayMulticast() + "----5=" + !OTTApplication.getContext().isSupportMulticast());

        if (!currentUrl.startsWith("http") && (!SharedPreferenceUtil.getBoolData("MULTICAST_SWITCH", false) || !LiveDataHolder.get().canPlayMulticast() || !OTTApplication.getContext().isSupportMulticast())) {
            //默认时移时长2小时,此处不是需求,是解决静态静态检查问题,如果测试发现问题,需要修改默认时移时长
//            mPlayView.setIptvlength( schedule == null ? 2L*60*60*1000 : schedule.getPltvLength()*1000L);
//            SuperLog.info2SDDebug(TAG, "currentAttachUrl is " + currentAttchUrl);
//            StringBuilder sb = new StringBuilder().append(currentAttchUrl).append("&").append(PlayUtil.REQUEST_PARAMETER).append(mSimpleDateFormat.format(new Date(NtpTimeService.queryNtpTime())));
//            PlayUtil.setIptvUrl(sb.toString());
//            mInfoBarLine.setText(" | ");
//            SuperLog.info2SDDebug(TAG, "IptvUrl is " + PlayUtil.getIptvUrl());
//            mPlayView.restartTSTVurl();
//            isPlayAttchUrl = true;

            currentUrl = changeTSTVUrlToTVUrl(attchUrl);

            if (url.contains("?")) {
                currentUrl = currentUrl + "&" + MulticastClose;
            } else {
                currentUrl = currentUrl + "?" + MulticastClose;
            }

            //拼接profileId
            currentUrl = currentUrl + getProfileid();

            mPlayView.setIptvlength(schedule == null ? 2L * 60 * 60 * 1000 : schedule.getPltvLength() * 1000L);
            SuperLog.info2SDDebug(TAG, "currentAttachUrl is " + currentAttchUrl);
            StringBuilder sb = new StringBuilder().append(currentAttchUrl).append("&").append(PlayUtil.REQUEST_PARAMETER).append(mSimpleDateFormat.format(new Date(NtpTimeService.queryNtpTime())));
            PlayUtil.setIptvUrl(sb.toString());
            mInfoBarLine.setText(" | ");
            SuperLog.info2SDDebug(TAG, "IptvUrl is " + PlayUtil.getIptvUrl());

            if (isOldChannelTSTVstate) {
                mPlayView.releasePlayer();
                mPlayView.startPlay(currentUrl);
            } else {
                if (!mIsSwitchChannel && DeviceInfo.isMGV2000()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SuperLog.debug(TAG, "is MGV2000,Delay 500ms to play");
                            mPlayView.fastPlayVideo(currentUrl);
                        }
                    }, 500);
                } else {
                    mPlayView.fastPlayVideo(currentUrl);
                }
            }
        } else if (isOldChannelTSTVstate) {
            mPlayView.releasePlayer();
            mPlayView.startPlay(url);
        } else {
            if (!mIsSwitchChannel && DeviceInfo.isMGV2000()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SuperLog.debug(TAG, "is MGV2000,Delay 500ms to play");
                        mPlayView.fastPlayVideo(currentUrl);
                    }
                }, 500);
            } else {
                mPlayView.fastPlayVideo(url);
            }
        }
        //解析频道位置
        List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();
        if (null != channelPlay) {
            int channelIndex = LiveUtils.parseChannelDetailIndex(channelId, channelPlay);
            if (channelIndex != -1) {
                if (channelPlay.get(channelIndex).getSubjectIDs() != null &&
                        channelPlay.get(channelIndex).getSubjectIDs().size() > 0) {
                    if (TextUtils.isEmpty(mSubjectID)) {
                        mSubjectID = getCurrentSubjectID(channelPlay.get(channelIndex).getSubjectIDs(), LiveDataHolder.get().getTargetChildSubject(mSubjectId));
                    } else {
                        for (int i = 0; i < channelPlay.get(channelIndex).getSubjectIDs().size(); i++) {
                            if (TextUtils.equals(mSubjectID, channelPlay.get(channelIndex).getSubjectIDs().get(i))) {
                                break;
                            }
                            if (i == channelPlay.get(channelIndex).getSubjectIDs().size() - 1) {
                                mSubjectID = getCurrentSubjectID(channelPlay.get(channelIndex).getSubjectIDs(), LiveDataHolder.get().getTargetChildSubject(mSubjectId));
                            }
                        }
                    }
                } else {
                    mSubjectID = "";
                }
                contentCode = channelPlay.get(channelIndex).getCode();
            }
        }
        SuperLog.info2SD(TAG, "Time[startPlayEnd]=" + System.currentTimeMillis());

    }

    private String getCurrentSubjectID(List<String> subjectIDs, List<Subject> subjects) {
        // listSubjectID：得到栏目Id数组，用于判断根据频道id确定栏目位置
        for (Subject subject : subjects) {
            if (subjectIDs.contains(subject.getID())) {
                return subject.getID();
            }
        }
        return "";
    }

    public String getContentCode() {
        return contentCode;
    }

    private int trySeeTime = 0;

    @Override
    public void getPlayUrlFailed(String channelId, boolean isVOD, AuthorizeResult authorizeResult, String contentId, String url, String attchUrl) {
        isOrdered = true;
        mHandler.removeMessages(SHOW_ORDER_LAYOUT);
        mProductOrderLayout.setVisibility(View.GONE);
        EventBus.getDefault().post(new FinishPlayUrlEvent());

        //解析频道位置
        List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();
        if (null != channelPlay) {
            int channelIndex = LiveUtils.parseChannelDetailIndex(channelId, channelPlay);
            if (channelIndex != -1) {
                if (channelPlay.get(channelIndex).getSubjectIDs() != null &&
                        channelPlay.get(channelIndex).getSubjectIDs().size() > 0) {
                    if (TextUtils.isEmpty(mSubjectID)) {
                        mSubjectID = getCurrentSubjectID(channelPlay.get(channelIndex).getSubjectIDs(), LiveDataHolder.get().getTargetChildSubject(mSubjectId));
                    } else {
                        for (int i = 0; i < channelPlay.get(channelIndex).getSubjectIDs().size(); i++) {
                            if (TextUtils.equals(mSubjectID, channelPlay.get(channelIndex).getSubjectIDs().get(i))) {
                                break;
                            }
                            if (i == channelPlay.get(channelIndex).getSubjectIDs().size() - 1) {
                                mSubjectID = getCurrentSubjectID(channelPlay.get(channelIndex).getSubjectIDs(), LiveDataHolder.get().getTargetChildSubject(mSubjectId));
                            }
                        }
                    }
                } else {
                    mSubjectID = "";
                }
                contentCode = channelPlay.get(channelIndex).getCode();
            }
        }

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

            mProductInfo = products.get(0);
            mProductInfos = products;

            //查询用户分组
            marketingProduct = null;
            productMarketing = null;
            initProductLayout();
            Log.e("producttest", "LivePlay hash is " + hashCode());
            queryMultiqry();

        } else {
            EpgToast.showToast(getActivity(), R.string.notice_no_orderable_product);
            return;
        }

        mAuthorizeResult = JsonParse.object2String(authorizeResult);
        if (!isVOD) {
            SuperLog.info2SD(TAG, "直播播放鉴权失败 >> 释放播放器,显示订购按钮");
            String trySee = SessionService.getInstance().getSession().getTerminalConfigurationValue("tv_watch_validTime");
            SuperLog.info2SD(TAG, "tv_watch_validTime is " + trySee);
            trySeeTime = 5 * 60 * 1000;
            if (!TextUtils.isEmpty(trySee)) {
                try {
                    trySeeTime = Integer.parseInt(trySee) * 1000;
                } catch (Exception e) {
                    trySeeTime = 5 * 60 * 1000;
                }
            }

            if (trySeeTime > 0) {

                getChannelPlayUrlSuccess(channelId, url, "0", attchUrl);
//            showSubscribeLayout();
//            if (null != mPlayView) {
//                SuperLog.info2SD(TAG, "release Player");
//                mPlayView.pausePlay();
//            }
                //鉴权失败,不是VOD就是直播鉴权失败,在获取鉴权结果的同时,清空voddetail
                mVODDetail = null;
            } else {
                mPlayView.releasePlayer();
            }
        } else {
            //直接跳转到产品订购列表
            String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
            SuperLog.info2SD(TAG, "回看播放鉴权失败 >> [回看->直接跳转到产品订购列表页]");
            if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
                AuthorizeResult result = JsonParse.json2Object(mAuthorizeResult, AuthorizeResult.class);
                if (null != result) {
                    JumpToH5OrderUtils.getInstance().jumpToH5OrderFromVOD(result.getPricedProducts(), getActivity(), false, true, null, mVODDetail);
                    //pbs点击上报
                    Log.i(TAG, "PbsUaService: " + mVODDetail.getID());
                    PbsUaService.report(Detail.getPurchaseData(mVODDetail.getID()));
                    //pbs点击上报
                    String promotionId = "";
                    String groupId = "";
                    if (null != marketingProduct && null != productMarketing && !TextUtils.isEmpty(this.groupId)) {
                        promotionId = productMarketing.getId();
                        groupId = this.groupId;
                    }
                    Log.i(TAG, "PbsUaService:CLICK  ProductId " + mProductInfo.getID() + " promotionId " + promotionId + " groupId " + groupId);
                    PbsUaService.report(Live.getPurchaseData(PbsUaConstant.ActionType.CLICK, mProductInfo.getID(), promotionId, groupId));
                }
            } else {
                Intent intent = new Intent(getActivity(), NewProductOrderActivity.class);
                intent.putExtra(NewProductOrderActivity.AUTHORIZE_RESULT, mAuthorizeResult);
                intent.putExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, true);
                intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL, null != mVODDetail ? VodUtil.getSimpleVoddetail(mVODDetail, 5 * 60) : "");
                startActivity(intent);
            }
        }

    }

    @Override
    public void playUrlError() {
        super.playUrlError();
        mHandler.removeMessages(SHOW_ORDER_LAYOUT);
        mProductOrderLayout.setVisibility(View.GONE);
    }

    @Override
    public void getVODPlayUrlSuccess(String url, String bookmark, String productId) {
        super.getVODPlayUrlSuccess(url, bookmark, productId);
        isOrdered = true;
        mProductOrderLayout.setVisibility(View.GONE);
    }

    /*
     * 请求用户栏目-频道数据成功
     * */
    @Override
    public void onChannelColumn() {
        //播放鉴权默认频道
        getPresenter().playDefaultChannel(getActivity(), mSubjectId, mIsQuerySubject);
    }

    @Override
    public void onChannelIndex(int index) {
        if (index >= 0) {
            mSwitchIndex = index;
        }
    }

    @Override
    public void getChannelPlayKey(String playKey) {
    }

    @Override
    public void getProductID(String productID) {
        mProductID = productID;
    }

    /**
     * 返回当前正在播放的频道和节目单信息
     *
     * @param programName 节目单
     * @param syTime      剩余时间
     * @param channelName 频道名称
     */
    @Override
    public void onCurrentChannelPlayBillSucc(String programName, String syTime, String channelName) {
        mPlaybillName.setText(programName);
        mPlaybillName.setSelected(true);
        mChannelName.setText(channelName);
        mTotalTime.setText(syTime);
        if (!TextUtils.isEmpty(syTime)) {
            mInfoBarLine.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(currentUrl) && !currentUrl.startsWith("http") && !isPlayAttchUrl) {
                mInfoBarLine.setText(" || ");
            } else {
                mInfoBarLine.setText(" | ");
            }
        } else {
            mInfoBarLine.setVisibility(View.GONE);
        }
        if (!LiveUtils.isShowChannelName()) {
            mInfoBarLine.setVisibility(View.GONE);
            mChannelName.setVisibility(View.GONE);
        } else {
            mChannelName.setVisibility(View.VISIBLE);
//            if(isSwitchNumberKey)
//            {
            delayInfoBar();
//            }
        }
    }

    @Override
    public void onCurrentChannelPlayBillError() {
    }

    /**
     * 从缓存的所有的频道中取频道号,依次展示
     *
     * @param switchIndex 切换的索引位置++
     * @param channelNO   当前切换的频道号
     */
    @Override
    public void onSwitchChannelNO(int switchIndex, String channelNO) {
        onSwitchChannelIndex(switchIndex);
        isSwitchNumberKey = false;
        //这里是上下键切台返回的频道信息
        //下面只展示台号和清空台号操作,我们在onKeyUp事件中操作playChannel：
        //心跳没有取到频道号,就不展示
        if (TextUtils.isEmpty(mSubjectId)) {
            mChannelMenu.setVisibility(View.GONE);
            mSwitchChannel.setText(TextUtils.isEmpty(channelNO) ? "" : String.valueOf(channelNO));
        } else if (TextUtils.equals(mIsShowChannelNo, "1")) {
            mChannelMenu.setVisibility(View.GONE);
            mSwitchChannel.setText(switchIndex + 1 + "");
        } else {
            mSwitchChannel.setText("");
        }
        removeMessages(MSG_HIDE_CHANNELNO);
        //发送清空台号的message
        sendEmptyMessageDelayed(MSG_HIDE_CHANNELNO, DELAY_HIDE_CHANNELNO);
    }

    /**
     * 返回当前频道号对应的位置
     *
     * @param switchIndex 位置
     */
    @Override
    public void onSwitchChannelIndex(int switchIndex) {
        mSwitchIndex = switchIndex;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        getPresenter().detachView();
        if (null != mPlayView) {
            mPlayView.onDestory();
        }
        productOrderPresenter.detachView();
        mOrderCenterPresenter.detachView();
    }

    /**
     * 刷新讯飞语音状态
     *
     * @param isResume 是不是onResume
     */
    private void refreshLIVEStatus(boolean isResume) {
        Intent intent = new Intent();
        intent.setAction("com.iflytek.xiri.action.LIVE.status");
        intent.putExtra("cp", "zjmhw");
        intent.putExtra("live", isResume);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 是否禁用当前按键事件
     *
     * @param event event.isShow()
     *              true表示此时要禁用keyevent;false表示开放keyevent响应;
     */
    @Subscribe
    public void onEvent(DisableTVGuideKeyEvent event) {
        isDisableKeyEvent = event.isShow();
    }

    /**
     * 延时隐藏infobar
     */
    private void delayInfoBar() {
        removeMessages(MSG_HIDE_CHANNEL_MENU);
        if (!isTSTVState()) {
            //显示节目单的时候清空台号
            mSwitchChannel.setText("");
            mPlaybillName.setSelected(true);
            mChannelMenu.setVisibility(View.VISIBLE);
            //修改为默认5S
//            sendEmptyMessageDelayed(MSG_HIDE_CHANNEL_MENU, mDelayHideChannelInfoBarTime);
            sendEmptyMessageDelayed(MSG_HIDE_CHANNEL_MENU, 5000);
        }
    }

    /**
     * 切换频道
     *
     * @param isClearChannelInfo 是否清空台号
     */
    private void switchChannel(boolean isClearChannelInfo) {
        SuperLog.info2SD(TAG, "Time[BeginToClick]=" + System.currentTimeMillis());
        if (!TextUtils.isEmpty(mSubjectId)) {
            if (!TextUtils.equals(mIsQuerySubject, "1")) {
                mChannelPlayList = LiveDataHolder.get().getTargetChannelPlay(mSubjectId, TextUtils.equals(mIsQuerySubject, "1"));
            } else {
                mChannelPlayList = LiveDataHolder.get().getTargetChannelPlay(mSubjectID, false);
            }
        } else {
            mChannelPlayList = LiveDataHolder.get().getChannelPlay();
        }
        String channelNO = mSwitchChannel.getText().toString();
        Schedule schedule = null;

        ChannelDetail channelDetail = null;
        ChannelDetail channelDetailByChannelNO = getChannelDetail(channelNO, mChannelPlayList);

        if (isClearChannelInfo) {

            if (TextUtils.equals(mIsShowChannelNo, "1")) {
                int channelPosition = Integer.parseInt(channelNO) - 1;
                if (null != mChannelPlayList && channelPosition >= 0) {
                    if (channelPosition < mChannelPlayList.size()) {
                        channelDetail = mChannelPlayList.get(channelPosition);
                    }
                    // channelDetail转schedule
                    if (channelDetail != null) {
                        schedule = setChannelToSchedule(channelDetail);
                    }

                }

                //清空台号
                mSwitchChannel.setText("");
                if (null == schedule) {
                    EpgToast.showToast(OTTApplication.getContext(),
                            OTTApplication.getContext().getString(R.string.no_channel));
                    return;
                }
            } else {
                //根据频道号取schedule
                schedule = LiveUtils.findScheduleByChannelNO(channelNO);
                if (schedule != null) {
                    if (channelDetailByChannelNO == null) {
                        mSwitchChannel.setText("");
                        EpgToast.showToast(OTTApplication.getContext(),
                                OTTApplication.getContext().getString(R.string.no_play_channel));
                        return;
                    }
                }

                if (!TextUtils.isEmpty(channelNO) && null != mChannelPlayList) {
                    for (ChannelDetail channelDetail1 : mChannelPlayList) {
                        if (null != channelDetail1) {
                            String channelNo = channelDetail1.getChannelNO();
                            if (!TextUtils.isEmpty(channelNo) && channelNo.equals(channelNO)) {
                                schedule = setChannelToSchedule(channelDetail1);
                                break;
                            }
                        }
                    }
                }

                //清空台号
                mSwitchChannel.setText("");
                if (null == schedule) {
                    EpgToast.showToast(OTTApplication.getContext(),
                            OTTApplication.getContext().getString(R.string.no_channel));
                    return;
                }
            }
        } else {
            if (null != mChannelPlayList) {
                if (mSwitchIndex < mChannelPlayList.size()) {
                    channelDetail = mChannelPlayList.get(mSwitchIndex);
                }
                // channelDetail转schedule
                if (channelDetail != null) {
                    schedule = setChannelToSchedule(channelDetail);
                }

            }
        }
        if (null != getPresenter()) {
            mHandler.removeMessages(SHOW_ORDER_LAYOUT);
            getPresenter().playChannel(false, schedule, null, getActivity(), mSubjectId, mIsQuerySubject, mSubjectID);
        }
    }

    /*channelDetail转schedule*/
    private Schedule setChannelToSchedule(ChannelDetail channelDetail) {
        Schedule schedule = new Schedule();
        schedule.setId(channelDetail.getID());
        schedule.setChannelNo(channelDetail.getChannelNO());
        //媒体ID
        String mediaId = "";
        List<Integer> maxList = new ArrayList<>();
        if (channelDetail.getPhysicalChannels() != null
                && channelDetail.getPhysicalChannels().size() > 0) {
            if (channelDetail.getPhysicalChannels().size() == 1) {
                mediaId = channelDetail.getPhysicalChannels().get(0).getID();
            } else {
                for (int i = 0; i < channelDetail.getPhysicalChannels().size(); i++) {
                    maxList.add(Integer.parseInt(channelDetail.getPhysicalChannels().get(i).getDefinition()));
                }
                //高清标清标识,取高清度最大的
                mediaId = channelDetail.getPhysicalChannels()
                        .get(maxList.indexOf(Collections.max(maxList)))
                        .getID();
            }
            schedule.setMediaID(mediaId);

        }
        return schedule;
    }

    /**
     * 甩屏不提示
     *
     * @param event
     */
    @Subscribe
    public void xmppSuccess(XmppSuccessEvent event) {
        setIsXmpp(true);
    }

    /*
     * 根据频道号查看数字输入频道是个可看
     * */
    private ChannelDetail getChannelDetail(String channelNO, List<ChannelDetail> schedules) {
        if (!TextUtils.isEmpty(channelNO) && null != schedules) {
            for (ChannelDetail schedule : schedules) {
                String channelNo = schedule.getChannelNO();
                if (!TextUtils.isEmpty(channelNo) && channelNo.equals(channelNO)) {
                    return schedule;
                }
            }
        }
        return null;
    }

    @Override
    public void onStop() {
        super.onStop();
        //如果是xmpp推送消息，点击进入，则按照停止播放来处理
        mIsSwitchChannel = false;
        reportChannel(ReportChannelRequest.ACTION_STOP);

    }

    private int reportAction;
    private String reportChannelID;

    //直播行为上传
    private void reportChannel(int action) {
        if (null != getPresenter() && (action != reportAction || TextUtils.equals(reportChannelID, mChannelID) || !mIsSwitchChannel)) {

            getPresenter().reportChannel(action, mChannelID, mNextChannelID, mMediaID, mNextMediaID, "BTV", 0, mProductID, mSubjectID);
        }
    }

    public String getCurrentUrl() {
        return currentUrl;
    }


    /**
     * 判断频道是否支持时移
     *
     * @param schedule
     * @return
     */
    public void setScheduleState(Schedule schedule, String currentUrl) {
        SuperLog.debug(TAG, "setScheduleState:" + schedule);
        String isOpenTSTV = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.OPEN_LIVE_TVTS);
        if ("1".equals(isOpenTSTV) && null != schedule && schedule.getPltvLength() > 0 && !TextUtils.isEmpty(currentUrl)) {
            mPlayView.setVideoType(PlayUtil.VideoType.TSTV);
            videoType = PlayUtil.VideoType.TSTV;
            PlayUtil.setNtpTime(NtpTimeService.queryNtpTime());
            PlayUtil.setLastElapsedRealtime(SystemClock.elapsedRealtime());
            mPlayView.setControllViewState(View.GONE, false);
        } else {
            mPlayView.setVideoType(PlayUtil.VideoType.TV);
            videoType = PlayUtil.VideoType.TV;
            mPlayView.setControllViewState(View.GONE, false);
        }
    }

    /**
     * 当前是否是时移状态
     *
     * @return
     */
    public boolean isTSTVState() {
        return videoType == PlayUtil.VideoType.TSTV && null != mPlayView && mPlayView.getCurrentPosition() != mPlayView.getDuration();
    }

    private List<String> unsubProdInfoIds;

    private ProductOrderPresenter productOrderPresenter = new ProductOrderPresenter();

    private OrderUserGroupPresenter getUserGroupPresenter = new OrderUserGroupPresenter();

    private OrderCenterPresenter mOrderCenterPresenter = new OrderCenterPresenter();

    private static final int REQUEST_CODE = 110;

    private String[] mChannelMediaIds = null;

    private UniPayInfo mainUniPayInfo;

    private Product mProductInfo;
    private List<Product> mProductInfos = new ArrayList<>();

    private void checkSubscriberProduct() {
//        mPresenter.generateProductList(mAuthorizeResult);
//        ProductUtils.checkSubscribeProductsType(getActivity(), new ProductUtilCallback() {
//            @Override
//            public void onCheckPackageRelationshipSuccess(Map<String, List<String>> params) {
//                //大包已订购，不能订购
//                if (null == params) {
//                    EpgToast.showToast(OTTApplication.getContext(),
//                            getResources().getString(R.string.big_small_product_conflict));
//                    return;
//                } else {
//                    unsubProdInfoIds = params.get(ProductUtils.SUBSCRIBED_PRODUCT_ID);
//                }
//                mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
//
//            }
//
//            @Override
//            public void onQueryProductInfoFailed() {
//            }
//
//            @Override
//            public void onQueryProductEmpty() {
//                mPresenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());
//            }
//        }, mProductInfo.getID());
    }

    @Override
    public void generateProductListSucc(List<Product> productList) {
///*判断产品列表是否大于4个，是否同时包含可自动续订和不可自动续订产品
//          产品包大于4个且同同时包含可自动续订和不可自动续订产品时
//          两排展示，否则一排展示*/
//        boolean isContain = false;
//        boolean containOnce = false;
//        boolean containCycle = false;
//        for (int i = 0; i < productList.size(); i++) {
//            Product product = productList.get(i);
//            if ((!TextUtils.isEmpty(product.getIsAutoExtend()) && product.getIsAutoExtend().equals("0"))) {
//                //是按次订购产品
//                containOnce = true;
//            }
//            if ((!TextUtils.isEmpty(product.getIsAutoExtend()) && product.getIsAutoExtend().equals("1"))) {
//                //是包周期产品
//                containCycle = true;
//            }
//        }
//
//        if (containOnce && containCycle) {
//            //同时包含包周期和按次订购产品
//            isContain = true;
//        }
//
//        if (productList.size() > 4 && isContain) {
//            showSingleList = false;
//        }
//
//        if (showSingleList) {
//            //展示一排
//            recyclerviewLayoutSingle.setVisibility(View.VISIBLE);
//            this.productList.clear();
//            if (productList.size() > 0) {
//                this.productList.addAll(productList);
//                mProductInfo = this.productList.get(0);
//                freshUI();
//                if (this.productList.size() > 5) {
//                    orderListListAuto.setVisibility(View.GONE);
//                } else {
//                    orderListList.setVisibility(View.GONE);
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        } else {
//            //展示两排,对数据进行分类
//            onceProductList.clear();
//            cycleProductList.clear();
//            for (int i = 0; i < productList.size(); i++) {
//                Product product = productList.get(i);
//                if ((!TextUtils.isEmpty(product.getProductType()) && product.getIsAutoExtend().equals("0"))) {
//                    //是单次产品
//                    onceProductList.add(product);
//                } else if ((!TextUtils.isEmpty(product.getProductType()) && product.getIsAutoExtend().equals("1"))) {
//                    //是包周期产品
//                    cycleProductList.add(product);
//                }
//            }
//
//            recyclerviewLayoutMulti.setVisibility(View.VISIBLE);
//            mProductInfo = cycleProductList.get(0);
//            freshUI();
//            if (cycleProductList.size() > 4) {
//                orderListListUpAuto.setVisibility(View.GONE);
//            } else {
//                orderListListUp.setVisibility(View.GONE);
//            }
//            if (onceProductList.size() > 4) {
//                orderListListDownAuto.setVisibility(View.GONE);
//            } else {
//                orderListListDown.setVisibility(View.GONE);
//            }
//            mutiAdapterForCycle.notifyDataSetChanged();
//            mutiAdapterForOnce.notifyDataSetChanged();
//        }
    }

    @Override
    public void generateProductListError() {

    }

    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {
        mainUniPayInfo = productOrderPresenter.resolveMainUniPayInfo(response);
        if (null != mainUniPayInfo) {
            if (TextUtils.isEmpty(mainUniPayInfo.getBillID())) {
                //统一账号不存在
                switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
            } else {
                productOrderPresenter.queryUserOrderingSwitch();
            }
        } else {
            //跳转到开通统一支付扫码连接界面
            switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.QRCODE_FLAG);
        }
    }

    @Override
    public void queryUniPayInfoError() {
        //统一账号不存在
        switchPhonePayActivity(null, ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT);
    }

    @Override
    public void querySubscriberSucess(String orderingSwitch) {

        //跳转到支付界面
        if (!TextUtils.isEmpty(orderingSwitch) && "1".equals(orderingSwitch)) {
            switchPhonePayActivity(mainUniPayInfo,
                    ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG);
        } else {
            switchPhonePayActivity(mainUniPayInfo, ZjYdUniAndPhonePayActivty.UNIPAY_FLAG);
        }
    }

    @Override
    public void querySubscriberfail() {
        switchPhonePayActivity(mainUniPayInfo, ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG);
    }

    @Override
    public void queryMultiqrySuccess(QueryMultiqryResponse response) {
        offerInfos = response.getOfferList();
        getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(), this);
    }

    @Override
    public void queryMultiqryFail() {
        getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(), this);
    }

    @Override
    public void queryMultiUserInfoSuccess(QueryMultiUserInfoResponse response, Intent intent) {

    }

    @Override
    public void queryMultiUserInfoFail(Intent intent) {

    }

    /**
     * 跳转到支付页面
     */
    public void switchPhonePayActivity(UniPayInfo mainUniPayInfo, String payType) {
        String needJumpToH5Order = SessionService.getInstance().getSession().getTerminalConfigurationNeedJumpToH5Order();
        if (null != needJumpToH5Order && needJumpToH5Order.equals("1")) {
            List<Product> products = new ArrayList<>();
//            products.add(mProductInfo);
            products = mProductInfos;
            XmppMessage mXmppMessage = null;
            if (null != getArguments() && getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
                mXmppMessage = (XmppMessage) getArguments().getSerializable(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE);
            }
            JumpToH5OrderUtils.getInstance().jumpToH5PayProducts(products, getActivity(), LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo(), getIsXmpp(), mXmppMessage);
            //pbs点击上报
            String promotionId = "";
            String groupId = "";
            if (null != marketingProduct && null != productMarketing && !TextUtils.isEmpty(this.groupId)) {
                promotionId = productMarketing.getId();
                groupId = this.groupId;
            }
            Log.i(TAG, "PbsUaService:CLICK  ProductId " + mProductInfo.getID() + " promotionId " + promotionId + " groupId " + groupId);
            PbsUaService.report(Live.getPurchaseData(PbsUaConstant.ActionType.CLICK, mProductInfo.getID(), promotionId, groupId));
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(getActivity().getPackageName(), NewMyPayModeActivity.class.getName());
        if (SessionService.getInstance().getSession().isHotelUser()) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.SHOW_ITEM_MODE,
                    ZjYdUniAndPhonePayActivty.HOTEL_MODE);
        }
        if (isMouthProduct(mProductInfo)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.SHOW_ITEM_MODE,
                    ZjYdUniAndPhonePayActivty.MONTH_PRODUCT_MODE);
        }
        if (ZjYdUniAndPhonePayActivty.QRCODE_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_CODE_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNIPAY_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_PHONE_PAY_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNIPAY_PHONE_CODE_FLAG.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNI_CHILDREN_PAY_MODE);
        } else if (ZjYdUniAndPhonePayActivty.UNONPENED_ACCOUNT.equals(payType)) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNI_PAY_TYPE,
                    ZjYdUniAndPhonePayActivty.UNONPENED_UNI_MODE);
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.PRODUCT_INFO,
                JsonParse.object2String(mProductInfo));
//        if (null != mVODDetail) {
//            intent.putExtra(ZjYdUniAndPhonePayActivty.VOD_DETAIL,
//                    JsonParse.object2String(mVODDetail));
//        }
        if (null != marketingProduct) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.MARKETING_PRODUCT,
                    JsonParse.object2String(marketingProduct));
        }
        if (null != unsubProdInfoIds && unsubProdInfoIds.size() > 0) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNSUBPRODINFOIDS,
                    JsonParse.listToJsonString(unsubProdInfoIds));
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.UNIPAY_EXPTIME,
                productOrderPresenter.analyticValidity(mProductInfo));
        if (null != mainUniPayInfo) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.UNIPAY_BILLID, mainUniPayInfo.getBillID());
        }
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISVOD_SUBSCRIBE, false);
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISTVOD_SUBSCRIBE, false);

        intent.putExtra(ZjYdUniAndPhonePayActivty.CHANNELID_MEDIAID, LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo());
        intent.putExtra(ZjYdUniAndPhonePayActivty.IS_TRY_SEE_SUBSCRIBE, true);
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISOFFSCREEN_SUBSCRIBE, getIsXmpp());
        intent.putExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER,
                getActivity().getIntent().getBooleanExtra(ZjYdUniAndPhonePayActivty.ISORDERCENTER, false));

        if (getActivity().getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE) != null) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE,
                    getActivity().getIntent().getSerializableExtra(ZjYdUniAndPhonePayActivty.XMPP_MESSAGE));
        }
        /*观影券需求，产品支付类型只支持按次产品*/
        if (isOrderByOrder(mProductInfo)/* && isSupportPayBenefit*/) {
            intent.putExtra(ZjYdUniAndPhonePayActivty.IS_ORDER_BY_ORDER, true);
        }
        // 2018/10/10 第三方支付需求 ADD START
        // 判断当前产品是否支持第三方支付
        if (PaymentUtils.isProductSupportThirdPartPayment(mProductInfo)) {
            QueryMultiUserInfoRequest request = new QueryMultiUserInfoRequest();
            String billID = SessionService.getInstance().getSession().getAccountName();
            if (!TextUtils.isEmpty(billID)) {
                request.setBillID(billID);
                request.setMessageID(PaymentUtils.generateMessageID());
                productOrderPresenter.vssQueryMultiUserInfo(request, getActivity(), intent);
                return;
            } else {
                SuperLog.error(TAG, String.format("Get QueryMultiUserInfoRequest parameter " +
                        "'billId', the 'billId' is: %s", billID));
                // 当用户状态校验失败时，不展示第三方支付
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_THIRD_PART_PAYMENT, false);
                intent.putExtra(NewMyPayModeActivity.NEED_SHOW_USER_STATE_VALIDATION_FAILED_NOTIFICATION, true);
            }
        }

        // 2018/10/10 第三方支付需求 ADD END
        startActivityForResult(intent, REQUEST_CODE);

    }

    public boolean isMouthProduct(Product mProductInfo) {
        return null != mProductInfo && "0".equals(mProductInfo.getProductType()) && "0".equals(mProductInfo.getChargeMode()) && "1".equals(mProductInfo.getPeriodLength()) && "1".equals(mProductInfo.getIsAutoExtend());
    }

    /**
     * 是否按次订购
     */
    public boolean isOrderByOrder(Product mProductInfo) {
        return null != mProductInfo && "1".equals(mProductInfo.getProductType());
    }

    public void showSubscribeLayout() {
        isOrdered = false;
        //此处加载图片
        mProductOrderLayout.setVisibility(View.VISIBLE);
//        GlideApp.with(getActivity()).load(LiveDataHolder.get().getmLiveOrderBG()).placeholder(R.drawable.live_order_bg).into(mLiveOrderBg);
        if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderBG())) {
            GlideApp.with(LiveTVPlayFragment.this).load(LiveDataHolder.get().getmLiveOrderBG()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mLiveOrderBg.setImageDrawable(getResources().getDrawable(R.drawable.live_order_bg));
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    mLiveOrderBg.setImageDrawable(resource);
                    return false;
                }
            }).into(mLiveOrderBg);
        } else {
            mLiveOrderBg.setImageDrawable(getResources().getDrawable(R.drawable.live_order_bg));
        }
        mProductName.setText(LiveDataHolder.get().getmLiveOrderTitle().replace("\\n", "\n"));
        //1为确定按钮
        if (LiveDataHolder.get().getmFristFocus().equals("1")) {
//            mProductOrderButton.requestFocus();
//            mProductOrderButton.setSelected(true);
//            GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderSureSelected()).placeholder(R.drawable.live_order_selected).into(mProductOrderButton);
//            GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderCancelNormal()).placeholder(R.drawable.live_order_cancle_normal).into(mProductOrderCancelButton);

            if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderCancelNormal())) {
                GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderCancelNormal()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProductOrderCancelButton.setImageDrawable(resource);
                        return false;
                    }
                }).into(mProductOrderCancelButton);
            } else {
                mProductOrderCancelButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_cancle_normal));
            }
            mProductOrderButton.requestFocus();

        } else {
//            mProductOrderCancelButton.requestFocus();
//            mProductOrderCancelButton.setSelected(true);
//            GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderSureNormal()).placeholder(R.drawable.live_order_normal).into(mProductOrderButton);
//            GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderCancelSelected()).placeholder(R.drawable.live_order_cancle_selected).into(mProductOrderCancelButton);

            if (!TextUtils.isEmpty(LiveDataHolder.get().getmLiveOrderSureNormal())) {
                GlideApp.with(this).load(LiveDataHolder.get().getmLiveOrderCancelNormal()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProductOrderButton.setImageDrawable(resource);
                        return false;
                    }
                }).into(mProductOrderButton);
            } else {
                mProductOrderButton.setImageDrawable(getResources().getDrawable(R.drawable.live_order_normal));
            }
            mProductOrderCancelButton.requestFocus();
        }
        //pbs曝光上报
        String promotionId = "";
        String groupId = "";
        if (null != marketingProduct && null != productMarketing && !TextUtils.isEmpty(this.groupId)) {
            promotionId = productMarketing.getId();
            groupId = this.groupId;
        }
        if (null != mProductInfo) {
            Log.i(TAG, "PbsUaService:IMPRESSION  ProductId " + mProductInfo.getID() + " promotionId " + promotionId + " groupId " + groupId);
            PbsUaService.report(Live.getPurchaseData(PbsUaConstant.ActionType.IMPRESSION, mProductInfo.getID(), promotionId, groupId));
        }
    }


    /**
     * 包周期产品的计量方式，取值包括：
     * <p>
     * 0：包月
     * 10：包多天
     * 13：包多月
     * 18 ：包天
     * 19 ：包周
     * 20 ：包多周
     */
    private boolean initProductLayout() {
        if (!isAdded()) {
            // 解决代码逻辑走到此处时，Activity销毁,比如用户按返回键,导致此方法中的getString方法抛异常：
            // java.lang.IllegalStateException: Fragment LiveTVPlayFragment{45b0dc38 (fc9df081-2016-4da1-86c5-6ca00d73c4f1)} not attached to a context.
            return false;
        }
        mProductName.setText(mProductInfo.getName());
        mProductName.setSelected(true);
        if (marketingProduct == null) {
            //产品包价格
            double price = Double.parseDouble(mProductInfo.getPrice()) / 100;
            mProductPrice.setText(decimalFormat.format(price));

            mProductOldPrice.setVisibility(View.GONE);
            mProductDesc.setSelected(true);
            mProductDesc.setText(mProductInfo.getIntroduce());
            if (TextUtils.equals(mProductInfo.getProductType(), "0")) {
                switch (mProductInfo.getChargeMode()) {
                    case "0":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_month, ""));
                        break;
                    case "10":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_day, mProductInfo.getPeriodLength()));
                        break;
                    case "13":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_month, mProductInfo.getPeriodLength()));
                        break;
                    case "18":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_day, ""));
                        break;
                    case "19":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_week, ""));
                        break;
                    case "20":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_week, mProductInfo.getPeriodLength()));
                        break;
                    default:
                        break;
                }
            } else {
                mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_1));
            }
        } else {
            //产品包价格
            double price = Double.parseDouble(marketingProduct.getPrice()) / 100;
            mProductPrice.setText(decimalFormat.format(price));

            String oldPrice = getOldPrice();
            if (TextUtils.isEmpty(oldPrice)) {
                mProductOldPrice.setVisibility(View.GONE);
            } else {
                mProductOldPrice.setVisibility(View.VISIBLE);
                mProductOldPrice.setText(oldPrice);
            }


            mProductDesc.setText(mProductInfo.getIntroduce());
            if (TextUtils.equals(marketingProduct.getProductType(), "0")) {
                switch (marketingProduct.getChargeMode()) {
                    case "0":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_month, ""));
                        break;
                    case "10":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_day, marketingProduct.getPeriodLength()));
                        break;
                    case "13":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_month, marketingProduct.getPeriodLength()));
                        break;
                    case "18":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_day, ""));
                        break;
                    case "19":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_week, ""));
                        break;
                    case "20":
                        mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_0_week, marketingProduct.getPeriodLength()));
                        break;
                    default:
                        break;
                }
            } else {
                mProductPriceUnit.setText(getString(R.string.player_order_price_product_type_1));
            }
        }

//        mProductOrderButton.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                    return true;
//                }else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
//                    mProductOrderCancelButton.requestFocus();
////                    mProductOrderCancelButton.setSelected(true);
//                }
//                return false;
//            }
//        });
//
//
//        mProductOrderCancelButton.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                    return true;
//                }else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
//                    mProductOrderCancelButton.requestFocus();
////                    mProductOrderCancelButton.setSelected(true);
//                }
//                return false;
//            }
//        });

        return true;
    }

    //组播播放失败，组播地址转单播地址
    public static String changeTSTVUrlToTVUrl(String tstvurl) {
        String tvurl = "";
        if (!TextUtils.isEmpty(tstvurl)) {
            tvurl = tstvurl.replace("&servicetype=2&", "&servicetype=1&");
        }
        return tvurl;
    }

    private List<Subject> getSelectSubject(String subjectID) {
        List<Subject> subjects = LiveDataHolder.get().getSaveSubjectList();
        if (subjects != null && subjects.size() > 0) {
            for (Subject subject : subjects) {
                if (TextUtils.equals(subjectID, subject.getID())) {
                    List<Subject> subjectList = new ArrayList<>();
                    subjectList.add(subject);
                    return subjectList;
                }
            }
        }
        return new ArrayList<>();
    }

    //将用户的分组与产品包支持营销活动的分组比较，取出第一个匹配的营销活动，如果没有，返回null
    private Marketing getmarketingProduct(List<CustomGroup> customGroups, Product product) {
        //取出产品包扩展参数中的营销产品字段
        List<NamedParameter> listNp = product.getCustomFields();
        List<String> marketInfo = CommonUtil.getCustomNamedParameterByKey(listNp, "zjProductOfSales");
        Map<String, Marketing> marketingMap = new HashMap<>();
        if (null != marketInfo && marketInfo.size() > 0) {
            String str = marketInfo.get(0);
            if (!TextUtils.isEmpty(str)) {
                //转换成对象
                marketingMap = JsonParse.json2Object(str, new TypeToken<HashMap<String, Marketing>>() {
                }.getType());
            }
        }
        if (null == marketingMap) {
            return null;
        }
        //遍历用户组
        for (int j = 0; j < customGroups.size(); j++) {
            CustomGroup group = customGroups.get(j);
            //用户是否属于这个用户组 1属于 0不属于
            if (group.getLabelValue().equals("1")) {
                //取产品包的扩展参数里面的营销活动，看能否匹配上
                //产品包中包含用户组的营销活动
                if (null != marketingMap.get(group.getGroupId())) {
                    Marketing marketing = marketingMap.get(group.getGroupId());
                    if (null != marketing) {
                        try {
                            //对比生效时间
                            long endTime = sdf.parse(marketing.getEndTime()).getTime();
                            long startTime = sdf.parse(marketing.getStartTime()).getTime();
                            long currentTime = NtpTimeService.queryNtpTime();
                            if (endTime > currentTime && startTime <= currentTime) {
                                //用户组匹配且在生效时间内的且未订购的营销活动
                                if (!isMarketingOrdered(marketing)) {
                                    groupId = group.getGroupId();
                                    return marketing;
                                }
                            }
                        } catch (ParseException e) {
                            SuperLog.error(TAG, e);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void getLabelsByUserIdSuccess(List<CustomGroup> customGroups) {
        productMarketing = getmarketingProduct(customGroups, mProductInfo);
        if (null != productMarketing && !TextUtils.isEmpty(productMarketing.getId())) {
            List<String> marketProductIds = new ArrayList<>();
            marketProductIds.add(productMarketing.getId());
            mOrderCenterPresenter.queryChannelProduct(marketProductIds, 50, 0, getActivity());
        } else {
            if (initProductLayout()) {
                mHandler.sendEmptyMessageDelayed(SHOW_ORDER_LAYOUT, trySeeTime);
            }
        }
    }

    @Override
    public void getLabelsByUserIdFail() {
        initProductLayout();
        mHandler.sendEmptyMessageDelayed(SHOW_ORDER_LAYOUT, trySeeTime);
    }

    @Override
    public void loadSubscription(QueryProductInfoResponse response) {

    }

    @Override
    public void queryProductInfoFail() {
        initProductLayout();
        mHandler.sendEmptyMessageDelayed(SHOW_ORDER_LAYOUT, trySeeTime);
    }

    @Override
    public void loadProducts(List<Product> products) {
        if (products != null && products.size() > 0) {
            marketingProduct = products.get(0);
            marketingProduct.setMarketing(productMarketing);
        }
        initProductLayout();
        mHandler.sendEmptyMessageDelayed(SHOW_ORDER_LAYOUT, trySeeTime);
    }

    private String getOldPrice() {
        String oldPrice = null;
        if (marketingProduct != null) {
            List<String> orgPrice = CommonUtil.getCustomNamedParameterByKey(marketingProduct.getCustomFields(), "ORIGINAL_PRICE");
            if (orgPrice != null && orgPrice.size() > 0) {
                oldPrice = orgPrice.get(0);
            }
        }
        return oldPrice;
    }


    //判断Marking是否已经订购过
    private boolean isMarketingOrdered(Marketing marketing) {
        if (null != offerInfos && offerInfos.size() > 0) {
            for (int i = 0; i < offerInfos.size(); i++) {
                //如果这个Marking的OfferID和tagID在offerInfos中。返回true
                if ((offerInfos.get(i).getOfferID().equals(marketing.getId()) || offerInfos.get(i).getOfferID().equals(marketing.getTag()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void queryMultiqry() {
        String billId = SessionService.getInstance().getSession().getAccountName();
        //生成可订购的产品列表
        if (!TextUtils.isEmpty(billId)) {
            QueryMultiqryRequest request = new QueryMultiqryRequest();
            //TODO
            request.setMessageID("0000");
            request.setBillID(billId);
            request.setValidType("1");
            productOrderPresenter.queryMultiqry(request);
        } else {
            getUserGroupPresenter.getLabelsByUserId(SessionService.getInstance().getSession().getUserId(), this);
        }
    }

    public String getCurrentChildSubjectID() {
        return mSubjectID;
    }

    //FCC
    private String addFccStr(String channelID, String url) {
        StringBuilder sb = new StringBuilder(url);
        ChannelDetail channelDetail = LiveUtils.findScheduleFromCanPlayById(channelID);
        if (channelDetail != null && !CollectionUtil.isEmpty(channelDetail.getPhysicalChannels())
                && ("1".equals(channelDetail.getPhysicalChannels().get(0).getFccEnable()) || "2".equals(channelDetail.getPhysicalChannels().get(0).getFccEnable()))) {
            StringBuilder fccSb = new StringBuilder();
            String fccIp = CommonUtil.getConfigValue("fcc_append_ip_str");
            String fccPort = CommonUtil.getConfigValue("fcc_append_port_str");
            if (TextUtils.isEmpty(fccIp)) {
                fccIp = "117.148.179.55";  // 默认值117.148.179.55
            }
            if (TextUtils.isEmpty(fccPort)) {
                fccPort = "8027";          // 默认值8027
            }
            fccSb.append("ChannelFCCIP=").append(fccIp).append("&ChannelFCCPort=").append(fccPort);
            if (url.contains("?")) {
                sb.append("&").append(fccSb.toString());
            } else {
                sb.append("?").append(fccSb.toString());
            }

            if (url.contains("?")) {
                sb.append("&").append(MulticastOpen);
            } else {
                sb.append("?").append(MulticastOpen);
            }

        }

        if (!sb.toString().contains(MulticastOpen)) {
            if (url.contains("?")) {
                sb.append("&").append(MulticastClose);
            } else {
                sb.append("?").append(MulticastClose);
            }
        }

        return sb.toString();
    }

    //组播开关为开
    public static final String MulticastOpen = "Multicast=1";
    //组播开关为关
    public static final String MulticastClose = "Multicast=0";

    //获取profileid
    public String getProfileid() {
        String profildid = ProfileManager.getProfileInfo().getProfileId();
        return "&profileid=" + profildid;
    }

    /**
     * 播放频道不存在按照默认逻辑来处理
     *
     * @param event
     */
    @Subscribe
    public void onEvent(PlaySubListNo1ChannelEvent event) {
        //播放鉴权默认频道
        SuperLog.info2SD(TAG,"PlaySubListNo1ChannelEvent send");
        getPresenter().playDefaultChannel(getActivity(), "", mIsQuerySubject);
    }
}