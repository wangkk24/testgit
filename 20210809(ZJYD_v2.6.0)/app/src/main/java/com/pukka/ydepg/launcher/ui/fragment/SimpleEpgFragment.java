package com.pukka.ydepg.launcher.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ElementData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollViewPager;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollViewPagerAdapter;
import com.pukka.ydepg.customui.tv.widget.ReflectItemView;
import com.pukka.ydepg.event.AutoScrollViewEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.ForceLifecycleEvent;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.mvp.contact.TabItemContact;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.template.TypeThreeLoader;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.ui.LiveTVPlayFragment;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.SimpleEpgFragment.java
 * @author: luwm
 * @data: 2018-08-21 10:36
 * @Version V1.0 <描述当前版本功能>
 */
public class SimpleEpgFragment extends BaseMvpFragment<TabItemPresenter> implements TabItemContact.ITabItemView {
    private static final String TAG = "SimpleEpgFragment";
    protected String playUrl;
    private View focusedView;
    private boolean isStartPicFinished = true;
    List<View> group1Views = new ArrayList<>();
    List<View> group4Views = new ArrayList<>();
    @BindView(R.id.iv_simple_search)
    ImageViewExt ivSearch;
    @BindView(R.id.ri_simple_search)
    ReflectItemView riSearch;
    @BindView(R.id.rl_simple_epg_group1)
    RelativeLayout group1;
    @BindView(R.id.rl_simple_epg_group2)
    RelativeLayoutExt group2;
    @BindView(R.id.rl_simple_epg_group3)
    RelativeLayoutExt group3;
    @BindView(R.id.rl_simple_epg_group4)
    RelativeLayout group4;
    @BindView(R.id.rl_simple_epg_content)
    RelativeLayout content;
    @BindView(R.id.simple_epg_item10_pic)
    ReflectRelativeLayout picItem;
    PlayViewWindow videoView;
    @BindView(R.id.simple_epg_item11)
    AutoScrollViewPager autoScrollViewPager;
    @BindView(R.id.simple_epg_shimmer_imageview)
    ShimmerImageView shimmerImageView;

    //是否是手动点击刷新
    private boolean isRefresh = false;

    //是否要重新开个simpleEpgFragment
    private boolean isRestartNew=false;

    //是否已经请求了中间视频播放的数据
    private boolean isQuerying = false;

    private String navId;
    private List<GroupElement> groupElements;
    private Group mGroup;
    private AutoScrollViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new AutoScrollViewPagerAdapter.OnAutoViewPagerItemClickListener()
    {
        @Override
        public void onItemClick(Element element, VOD vod, boolean forceDefaultData)
        {
            if (isStartPicFinished)
            {
                String mActionUrl = "";
                if (forceDefaultData)
                {
                    ElementData elementData = element.getElementDataList().get(0);
                    if (!TextUtils.isEmpty(elementData.getElementAction().getActionURL()))
                    {
                        mActionUrl = elementData.getElementAction().getActionURL();
                        String mActionType = elementData.getElementAction().getActionType();
                        ZJVRoute.route(SimpleEpgFragment.this.getActivity(), ZJVRoute
                                        .LauncherElementDataType.STATIC_ITEM, mActionUrl, mActionType,
                                "", null, element.getExtraData());
                    }
                } else {
                    if (null == vod)
                    {
                        return;
                    }
                    ZJVRoute.route(SimpleEpgFragment.this.getActivity(), ZJVRoute.LauncherElementDataType.VOD, null, null, vod.getID(), vod, element.getExtraData());
                }
                UBDSwitch.getInstance().recordMainOnclick(mActionUrl,element,mGroup,vod,null);
            }
        }
    };

    @Override
    public void loadVODData(List<SubjectVodsList> subjectVODLists) {
        for (int i = 0; i < 4; i++) {
            if (i < groupElements.size()) {
                int dataIndex = groupElements.get(i).getDataIndex();
                List<VOD> vods = new ArrayList<>();
                if (-1 != dataIndex && null != subjectVODLists && subjectVODLists.size() > 0) {
                    for (SubjectVodsList vodsList : subjectVODLists){
                        if (vodsList.getSubjectID().equalsIgnoreCase(groupElements.get(i).getGroup().getCategoryCode())){
                            vods = vodsList.getVodList();
                        }
                    }
                }
                loadData(i, vods);
            }
        }
        //切换桌面时视频窗口获取焦点
        new Handler().postDelayed(()-> {
            group2.requestFocus();
            ((MainActivity)getActivity()).focusManageEpgTop(true);
        },300);
    }

    private void loadData(int i, List<VOD> vods) {

        switch (i) {
            case 0:
                loadGroupData(vods, groupElements.get(0).getElement(),groupElements.get(0).getGroup(), group1Views);
                break;
            case 1:
                if (!isQuerying){
                    isQuerying = true;
                    loadGroup2Data(vods, groupElements.get(1), group2);
                }
                break;
            case 2:
                loadGroup3Data(vods, groupElements.get(2).getElement(), groupElements.get(2).getGroup(),group3);
                mGroup = groupElements.get(2).getGroup();
                break;
            default:
                loadGroupData(vods, groupElements.get(3).getElement(),groupElements.get(3).getGroup(), group4Views);
                break;
        }
    }

    /**
     * 加载中部大图数据
     * 包含播放窗口
     *
     * @param vods
     * @param group2
     */
    private void loadGroup2Data(List<VOD> vods, GroupElement groupElement, RelativeLayoutExt group2) {
        Element firstElement = groupElement.getElement().get(0);
        if (TextUtils.equals(firstElement.getForceDefaultData(), "true")) {
            picItem.setDefaultData(true);
            picItem.setElementData(firstElement);
            group2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isStartPicFinished) {
                        String mActionUrl = firstElement.getElementDataList().get(0).getElementAction().getActionURL();
                        String mActionType = firstElement.getElementDataList().get(0).getElementAction().getActionType();
                        ZJVRoute.route(getActivity(), ZJVRoute.LauncherElementDataType.STATIC_ITEM, mActionUrl, mActionType, "", null, firstElement.getExtraData());
                        UBDSwitch.getInstance().recordMainOnclick(mActionUrl,firstElement,groupElement.getGroup(),null,null);
                    }
                }
            });
            String actionUrl = firstElement.getElementDataList().get(0).getElementAction().getActionURL();
            String contentType = ZJVRoute.getContentValue(actionUrl, ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
            String contentId = ZJVRoute.getContentValue(actionUrl, ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            String multiCastId = ZJVRoute.getContentValue(actionUrl, ZJVRoute.ActionUrlKeyType.MULTI_CAST_ID);
            //如果配置静态资源位且为跳转频道，则开始播放
            if (contentType.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.CHANNEL)) {
                ChannelDetail channelDetail;
                if (SharedPreferenceUtil.getInstance().getMulticastSwitch()){
                    channelDetail = LiveUtils.findScheduleFromCanPlayById(multiCastId);
                }else{
                    channelDetail = LiveUtils.findScheduleFromCanPlayById(contentId);
                }
                if (null == channelDetail) {
                    //1.10版本安全整改，升级1.10之前的版本查不到频道时默认播放CCTV1---2019.08.22
                    //2021.03.17,解决组播开关切换，简版播放窗口无法进入直播问题，注释播放默认频道代码逻辑
                    /*schedule = new Schedule();
                    contentId = "42329858";
                    schedule.setMediaID("42329859");*/
                    isQuerying = false;
                    return;
                }
                String mediaId = channelDetail.getPhysicalChannels().get(0).getID();
                PlayChannelRequest playChannelRequest = new PlayChannelRequest();
                playChannelRequest.setChannelID(channelDetail.getID());
                playChannelRequest.setMediaID(mediaId);
                playChannelRequest.setBusinessType(V6Constant.ChannelURLType.BTV);
                playChannelRequest.setURLFormat("1");//HLS
                presenter.playChannel(playChannelRequest, new RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, getActivity()) {
                    @Override
                    public void onSuccess(PlayChannelResponse playChannelResponse) {
//                        videoView.setVideoType(PlayUtil.VideoType.TV);
                        isQuerying = false;
                        playUrl = StringUtils.splicingPlayUrl(playChannelResponse.getPlayURL());

                        SuperLog.info2SDDebug(TAG, "ChannelUrl=" + playUrl + "\tAttachUrl=" + playChannelResponse.getAttachedPlayURL());

                        //播放失败，时移地址转直播地址
                        if (!playUrl.startsWith("http")){
                            playUrl = LiveTVPlayFragment.changeTSTVUrlToTVUrl(playChannelResponse.getAttachedPlayURL());
                        }
                        //开机动画结束后开始播放
                        if (!TextUtils.isEmpty(playUrl) && ((MainActivity) getActivity()).isStartPicFinished() && !isRefresh) {
                            initPlayView();
                            videoView.startPlay(playUrl);
                        }else{
                            isRefresh = false;
                            if (null != videoView && !TextUtils.isEmpty(playUrl) && !videoView.isPlaying()) {
                                videoView.startPlay(playUrl);
                            }
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        isQuerying = false;
                    }
                });
            }
        } else {
            isQuerying = false;
            //提供对配置了autoplay属性的vod对象进行播放的设置
            picItem.setDefaultData(false);
            if (!CollectionUtil.isEmpty(vods) && vods.size() > 0) {
                picItem.parseVOD(new TypeThreeLoader(), vods.get(0));
                if (null != firstElement.getExtraData() && TextUtils.equals("true", firstElement.getExtraData().get(Constant.AUTO_PLAY)) && ((MainActivity) getActivity()).isStartPicFinished()) {
                    if (null == videoView) {
                        initPlayView();
                    }
                    playVod(vods.get(0));
                }
                group2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isStartPicFinished) {
                            ZJVRoute.route(getActivity(), ZJVRoute.LauncherElementDataType.VOD, null, null, vods.get(0).getID(), vods.get(0), firstElement.getExtraData());

                            UBDSwitch.getInstance().recordMainOnclick("",firstElement,groupElement.getGroup(),vods.get(0),null);
                        }
                    }
                });
            }
        }
    }

    private void initPlayView() {
        picItem.hideAllView();
        videoView = (PlayViewWindow) findViewById(R.id.playview_simpleEpg);
        videoView.setVisibility(View.VISIBLE);
        videoView.setResizeMode(PlayView.RESIZE_MODE_FIT);
    }

    /**
     * 中部窗口播放视频
     * 先请求Voddetail然后请求播放地址
     *
     * @param vod
     */
    private void playVod(VOD vod) {
        presenter.getVODDetail(vod.getID(), new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, getActivity()) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                presenter.getPlayUrl(getVODDetailResponse.getVODDetail(), new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, getActivity()) {
                    @Override
                    public void onSuccess(PlayVODResponse playVODResponse) {
//                        videoView.setVideoType(PlayUtil.VideoType.VOD);
                        playUrl = StringUtils.splicingPlayUrl(playVODResponse.getPlayURL());
                        startPlayByUrl(playUrl, vod);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG, "simpleEpg playVod fail, " + e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "simpleEpg getVodDetail fail ," + e.getLocalizedMessage());
            }
        });
    }

    private void startPlayByUrl(String playURL, VOD vod) {
        if (TextUtils.isEmpty(playURL)) {
            return;
        }
        videoView.releasePlayer();
        videoView.startPlay(playURL);
        videoView.setOnPlayCallback(new IPlayListener() {
            @Override
            public void onPlayState(int playbackState) {
                if (playbackState == IPlayState.PLAY_STATE_HASMEDIA || playbackState == IPlayState.PLAY_STATE_BUFFERING) {
                    picItem.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPrepared(int  Videotype) { }

            @Override
            public void onRelease() { }

            @Override
            public void onPlayError(String msg, int errorCode, int playerType) { }

            @Override
            public void onPlayCompleted() {
                startPlayByUrl(playUrl, vod);
            }

            @Override
            public void onDetached(long time) { }

            @Override
            public void onAttached() { }

            @Override
            public void onTryPlayForH5() { }

            @Override
            public void onAdVideoEnd() {

            }
        });
    }

    private boolean isOpenShimmer = false;

    private void loadGroup3Data(List<VOD> vods, List<Element> elements,Group group, RelativeLayoutExt group3) {

        if (null != elements)
        {
            if (elements.size() > 0&&null != elements.get(0).getExtraData() && !TextUtils.isEmpty(elements.get(0).getExtraData().get("Is_Shimmer")) && elements.get(0).getExtraData().get("Is_Shimmer").equalsIgnoreCase("true")){
                isOpenShimmer = true;
            }
            AutoScrollViewPagerAdapter adapter = new AutoScrollViewPagerAdapter(getActivity(), group3, listener, elements, group,vods, elements.size() > 4 ? 4 : elements.size(), R.layout.item_simple_epg_scroll_layout);
            autoScrollViewPager.setAdapter(adapter);
        }
        String time = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.BANNER_SWITCHING_TIME);
        if (!TextUtils.isEmpty(time)) {
            long newTime = Long.parseLong(time) * 1000;
            autoScrollViewPager.setTime(newTime);
        } else {
            autoScrollViewPager.onResume();
        }
    }

    private void loadGroupData(List<VOD> vods, List<Element> elements,Group mGroup, List<View> parent) {
        int realIndex = 0;
        for (int i = 0; i < parent.size(); i++) {
            ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) parent.get(i);
            relativeLayout.setGroup(mGroup);
            relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    relativeLayout.setTextViewEffect(hasFocus);
                }
            });
            if (i < elements.size()) {
                Element element = elements.get(i);
                if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                    relativeLayout.setDefaultData(true);
                    relativeLayout.setElementData(element);
                } else {
                    relativeLayout.setElement(element);
                    relativeLayout.setDefaultData(false);
                    if (null != vods && vods.size() > realIndex) {
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(realIndex));
                    }
                    realIndex++;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        //解决从其他界面返回时概率性丢失焦点的问题
//        if (null != focusedView&&!isRestartNew) {
//            focusedView.postDelayed(() -> {
//                if(null != focusedView){
//                    focusedView.requestFocus();
//                    focusedView = null;
//                }
//            }, 800);
//        }
        if (null != autoScrollViewPager) {
            autoScrollViewPager.onResume();
        }
        SuperLog.debug(TAG,"onResum");
        if(!isRestartNew) {
            if (null != videoView && !TextUtils.isEmpty(playUrl) && !videoView.isPlaying()) {
                if(CommonUtil.isJMGODevice()) {
                    videoView.setPlayerNull();
                }
                videoView.startPlay(playUrl);
            }
        }else{
            onDestroy();
        }
    }

    public void resume() {
        SuperLog.debug(TAG,"resume");
        initPlayView();
        if (!TextUtils.isEmpty(playUrl)) {
            videoView.startPlay(playUrl);
        }
        if (null != autoScrollViewPager) {
            autoScrollViewPager.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        focusedView = getActivity().getWindow().getDecorView().findFocus();
        if (null != videoView) {
            videoView.releasePlayer();
        }
        if (null != autoScrollViewPager) {
            autoScrollViewPager.onDestroy();
        }

    }

    @Override
    protected void initView(View view) {
        navId = LauncherService.getInstance().getNavIdSimpleEpg();
        groupElements = LauncherService.getInstance().getSimpleEpgData();
        initGroupViews();
        if (null != groupElements) {
            presenter.queryHomeEpg(navId, groupElements, getActivity());
        }

        if (TextUtils.isEmpty(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.HOME_TV_LOG_RIGHT))){

            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(getContext().getResources().getDimensionPixelOffset(R.dimen.launcher_search_size),getContext().getResources().getDimensionPixelOffset(R.dimen.launcher_search_iv_size));
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.rightMargin=getContext().getResources().getDimensionPixelOffset(R.dimen.margin_330);
            lp.topMargin=getContext().getResources().getDimensionPixelOffset(R.dimen.launcher_logo2_margin_top);
            riSearch.setLayoutParams(lp);

        }
        ivSearch.setVisibility(View.GONE);
        riSearch.setVisibility(View.GONE);
        ivSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (null == ivSearch){
                return;
            }
            if (hasFocus) {
                riSearch.setBackgroundResource(R.drawable.top_focus);
            } else {
                riSearch.setBackgroundResource(0);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStartPicFinished) {
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                }
            }
        });
    }

    private void initGroupViews() {
        group1Views.add(findViewById(R.id.simple_epg_item1));
        group1Views.add(findViewById(R.id.simple_epg_item2));
        group1Views.add(findViewById(R.id.simple_epg_item3));
        group1Views.add(findViewById(R.id.simple_epg_item4));
        group1Views.add(findViewById(R.id.simple_epg_item5));
        group1Views.add(findViewById(R.id.simple_epg_item6));
        group1Views.add(findViewById(R.id.simple_epg_item7));
        group1Views.add(findViewById(R.id.simple_epg_item8));
        group1Views.add(findViewById(R.id.simple_epg_item9));
        group4Views.add(findViewById(R.id.simple_epg_item12));
        group4Views.add(findViewById(R.id.simple_epg_item13));
        group4Views.add(findViewById(R.id.simple_epg_item14));
        group4Views.add(findViewById(R.id.simple_epg_item15));
        if (null != group2)
            group2.setOnFocusChangeListener((fv, hasFocus) -> {
                if (null != picItem){
                    picItem.setTextViewEffect(hasFocus);
                }
            });
    }

    @Override
    protected int attachLayoutRes() {
        //解决切换老人桌面，我的页面没有走onPause方法，导致一直在播放的情况
        EventBus.getDefault().post(new ForceLifecycleEvent(ForceLifecycleEvent.PAUSE));
        return R.layout.simple_epg_content_layout;
    }

    @Override
    protected void initPresenter() {
        presenter = new TabItemPresenter();
    }

    public void setIsStartPicFinished(boolean isStartPicFinished){
        this.isStartPicFinished = isStartPicFinished;
        //解决:创维电视开机无焦点问题，延时请求焦点
        if (isStartPicFinished && null != group2 && !group2.hasFocus()){
            new Handler().postDelayed(()-> group2.requestFocus(),300);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new ForceLifecycleEvent(ForceLifecycleEvent.RESUME));
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(AutoScrollViewEvent event){
        if (event.isStartShimmer() && isOpenShimmer){
            shimmerImageView.startShimmer();
        }else{
            shimmerImageView.stopShimmer();
        }
    }

    public void setRestartNew(boolean restartNew) {
        isRestartNew = restartNew;
    }
}
