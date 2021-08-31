package com.pukka.ydepg.launcher.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.bean.node.SubjectIDList;
import com.pukka.ydepg.common.http.v6bean.v6node.ElementData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.playview.PlayViewMainEpg;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.TVLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.mvp.contact.TabItemContact;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.ChildLauncherActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.adapter.PHMAdapter;
import com.pukka.ydepg.launcher.ui.template.FreeLayoutTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollOneTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollSquareTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTimeLineTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTwoTemplate;
import com.pukka.ydepg.launcher.ui.template.MixVideoTemplate;
import com.pukka.ydepg.launcher.ui.template.MyListForProfileTemplate;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;
import com.pukka.ydepg.launcher.ui.template.VerticalScrollTestTemplate;
import com.pukka.ydepg.launcher.ui.template.VideoLiveTemplate;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.FragmentUtils;
import com.pukka.ydepg.launcher.view.PHMDividerItemDecoration;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.PHMFragment.java
 * @date: 2018-03-18 20:32
 * @version: V1.0 描述当前版本功能
 */
public class PHMFragment extends BaseMvpFragment<TabItemContact.ITabItemPresenter>
        implements TabItemContact.ITabItemView, FocusHelperCallBack, View.OnFocusChangeListener {

    protected final String TAG = this.getClass().getSimpleName();

    protected boolean isMain = true;
    protected String pageId  = "";  //二级桌面pageId
    protected RecyclerViewTV mRecyclerViewTV;
    protected List<GroupElement> mGroupElements = new ArrayList<>();
    protected List<String> mElementContentId = new ArrayList<>();
    protected int mPageIndex = 0;
    protected String mNavId;
    protected MainActivity mMainActivity;
    protected ChildLauncherActivity mChildActivity;
    protected boolean isLoadData = false;

    // 通栏运营位仅对指定用户展示GroupElement
    private List<GroupElement> mDesignateGroupElementList = null;
    private final int mDesignateIndex = 0;
    private final TabItemPresenter mTabItemPresenter = new TabItemPresenter();
    private final PBSRemixRecommendListener mPbsRemixRecommendListener = new PBSRemixRecommendListener();

    private final Handler handler = new Handler();

    //该页面中需要保存状态的模板集合
    protected Map<Integer, PHMTemplate> specialTemplateMap = new HashMap<>();

    //轮播模板记录播放位置
    protected Map<Integer, Integer> playPositionMap = new HashMap<>();

    //播放时长记录（暂时没有使用，每次重新播放）
    protected Map<Integer, Long> playTimeMap = new HashMap<>();

    //记录播放的是点播还是直播
    protected Map<Integer, Boolean> playTypeMap = new HashMap<>();

    //记录是否重新播放
    protected Map<Integer, String> mPlayOrNo = new HashMap<>();

    //记录播放地址集合
    protected Map<Integer, Map<Integer, String>> urlsMap = new HashMap<>();

    //记录直播播放地址
    protected Map<Integer,String> mChannelUrlMap = new HashMap<>();

    protected Map<Integer,Long> mVideoWinDurationMap = new HashMap<>();
    protected PHMAdapter mPHMAdapter;

    //横向滚动
    private HorizontalScrollTemplate mHorizontalScrollTemplate;
    private HorizontalScrollSquareTemplate mHorizontalScrollSquareTemplate;
    private HorizontalScrollTimeLineTemplate mHorizontalScrollSquareTimeLineTemplate;
    private HorizontalScrollOneTemplate mHorizontalScrollOneTemplate;
    private HorizontalScrollTwoTemplate mHorizontalScrollTwoTemplate;
    private VerticalScrollTestTemplate mVerticalScrollTestTemplate;

    //一行两列二、一行三列 视频窗口播放
    private final Map<Integer,MixVideoTemplate> mMixVideoTemplateMap = new HashMap<>();
    //首页视频播放窗口全部露出时播放
    private final Map<Integer,PlayViewMainEpg> mVideoTemplateMap = new HashMap<>();
    private Map<Integer,Boolean> mIsPlayCompleteMap = new HashMap<>();
    private VideoLiveTemplate mVideoLiveTemplate;

    //老人桌面,切换账号，为true,不能加载播放
    private boolean NotCanPlayNow;

    private PHMFragmentHandler phmFragmentHandler;

    private PHMChildFragmentHandler mChildrenFragmentHandler;

    public RecyclerViewTV getRecyclerViewTV() {
        return mRecyclerViewTV;
    }

    private boolean isChildrenEpg = false;

    private String mHomeTvLogoImgUrl,mHomeTvLogoRightImgUrl,mChildHomeTvLogoLeftUrl,mChildHomeTvLogoRightUrl,mChildHomeTvLogoMiddleUrl;

    //记录离开页面时最后的焦点View
    private View focusedView;

    private boolean isStartPicFinished = true;

    //请求BatchQueryVODListBySubject 所需的subjectId
    private List<SubjectIDList> mSubjectIDLists;

    private View mFirstChildView = null;

    private int loadLayoutIndex = -1;

    private int mRvScrollY = 0; // 列表滑动距离

    private boolean mIsShowVideoView = false;

    public void setIsShowVideoView(boolean isShowVideoView){mIsShowVideoView = isShowVideoView;}
    public boolean isShowVideoView(){return mIsShowVideoView;}

    public void setFirstChildView(View view){
        mFirstChildView = view;
    }
    public View getFirstChildView(){
        return mFirstChildView;
    }
    public void setLoadLayoutIndex(int index){
        loadLayoutIndex = index;
    }
    public int getLoadLayoutIndex(){
        return loadLayoutIndex;
    }
    public void setIsLoadData(boolean isLoadData){this.isLoadData = isLoadData;};
    public void resetPHMAdapter(){mPHMAdapter = null;};
    public void setIsPlayCompleteMap(int position,boolean isPlayComplete){
        mIsPlayCompleteMap.put(position,isPlayComplete);
    }
    public boolean isPlayComplete(int position){
        return null != mIsPlayCompleteMap.get(position);
    }

    public void setMixVideoTemplate(int index,MixVideoTemplate mixVideoTemplate){
        if (null == mixVideoTemplate){
            mMixVideoTemplateMap.remove(index);
        }else{
            mMixVideoTemplateMap.put(index,mixVideoTemplate);
        }
    }

    public void setVideoTemplate(PlayViewMainEpg playViewMainEpg,int index){
        if (null == playViewMainEpg){
            mVideoTemplateMap.remove(index);
        }else{
            mVideoTemplateMap.put(index,playViewMainEpg);
        }
    }

    public void setVideoLiveTemplate(VideoLiveTemplate videoLiveTemplate){
        mVideoLiveTemplate = videoLiveTemplate;
    }

    //返回当前界面的静态数据的ContentId集合
    public List<String> getContentIdList(){
        return mElementContentId;
    }

    public void setmRvScrollY(int rvScrollY){
        this.mRvScrollY = rvScrollY;
    }
    public int getmRvScrollY(){
        return this.mRvScrollY;
    }

    @Override
    public View getFirstFocusView() {
        PHMTemplate phmTemplate = null;
        if (getActivity() instanceof MainActivity && !isChildrenEpg){
            View nextFocusView;//用于防止短时间内执行两次
            nextFocusView = FocusFinder.getInstance().findNextFocus(mRecyclerViewTV, mRecyclerViewTV.findFocus(), View.FOCUS_DOWN);
            if (null != nextFocusView && (nextFocusView.getId() == R.id.rl_item_my_list || nextFocusView.getId() == R.id.rr_item_timeline
                    || nextFocusView.getId() == R.id.rr_item_vertical_scroll || nextFocusView.getId() == R.id.rr_item_series_vertical_scroll || nextFocusView.getId() == R.id.rr_item_varietyshow_vertical_scroll)) {
                phmTemplate = (PHMTemplate) nextFocusView.getParent().getParent().getParent().getParent();
            } else if (null != nextFocusView && nextFocusView.getId() == R.id.rr_item_my_function && nextFocusView.getParent().getParent().getParent() instanceof PHMTemplate) {
                phmTemplate = (PHMTemplate) nextFocusView.getParent().getParent().getParent();
            } else if (null != nextFocusView && nextFocusView.getParent() instanceof PHMTemplate) {//观看历史--更多历史--按下键Crash
                phmTemplate = (PHMTemplate) nextFocusView.getParent();
            }else if (null != nextFocusView && nextFocusView.getId() == R.id.reflect_group_item && nextFocusView.getParent().getParent() instanceof FreeLayoutTemplate){
                //自由编排，，焦点从导航栏按下键落焦到第一个资源位
                if (null != ((FreeLayoutTemplate)nextFocusView.getParent().getParent()).getFirstView()){
                    return ((FreeLayoutTemplate)nextFocusView.getParent().getParent()).getFirstView();
                }
            }else if (null != nextFocusView && nextFocusView.getId() == R.id.rl_item_profile_bookmark_list && nextFocusView.getParent().getParent().getParent().getParent() instanceof MyListForProfileTemplate){
                //多profile 爱看界面  播放记录模板在顶部，焦点从导航栏按下键 落焦到第一个资源位
                if (null != ((MyListForProfileTemplate)nextFocusView.getParent().getParent().getParent().getParent()).getFirstView()){
                    return ((MyListForProfileTemplate)nextFocusView.getParent().getParent().getParent().getParent()).getFirstView();
                }
            }
            if (null != phmTemplate && phmTemplate.getFirstView() != null) {
                return phmTemplate.getFirstView();
            }
        }
        if (null != mRecyclerViewTV) {// && !isScrollTemplate
            //解决：【长稳】首页焦点不规则移动,随机点击事件出现crash，添加判空保护
            //!isFirstLoad用于解决在儿童版桌面高版本覆盖安装低版本时，落焦的View未放大问题
            if ((isChildrenEpg || !isMain) && null != mRecyclerViewTV.getLayoutManager().findViewByPosition(1)) {
                phmTemplate = (PHMTemplate) mRecyclerViewTV.getLayoutManager().findViewByPosition(1);
            } else if (null != mRecyclerViewTV.getLayoutManager().findViewByPosition(0)) {
                phmTemplate = (PHMTemplate) mRecyclerViewTV.getLayoutManager().findViewByPosition(0);
            }
            if (phmTemplate != null) {
                return phmTemplate.getFirstView();
            }
        }
        if (null != mHorizontalScrollTemplate) {
            RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mHorizontalScrollTemplate.getFirstView();
            View view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            if (null != view) {
                return view;
            }
        }
        if (null != mHorizontalScrollSquareTemplate) {
            RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mHorizontalScrollSquareTemplate.getFirstView();
            View view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            if (null != view) {
                return view;
            }
        }
        if (null != mHorizontalScrollSquareTimeLineTemplate) {
            View view = null;
            if (mHorizontalScrollSquareTimeLineTemplate.getFirstView() instanceof HorizontalGridView) {
                HorizontalGridView horizontalGridView = (HorizontalGridView) mHorizontalScrollSquareTimeLineTemplate.getFirstView();
                view = horizontalGridView.getLayoutManager().findViewByPosition(0);
            } else if (mHorizontalScrollSquareTimeLineTemplate.getFirstView() instanceof RecyclerViewTV) {
                RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mHorizontalScrollSquareTimeLineTemplate.getFirstView();
                view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            }
            if (null != view) {
                return view;
            }
        }
        if (null != mHorizontalScrollOneTemplate) {
            RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mHorizontalScrollOneTemplate.getFirstView();
            View view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            if (null != view) {
                return view;
            }
        }
        if (null != mVerticalScrollTestTemplate) {
            //RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mVerticalScrollTestTemplate.getFirstView();
            //View view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            View view = mVerticalScrollTestTemplate.getFirstView();
            if (null != view) {
                return view;
            }
        }
        if (null != mHorizontalScrollTwoTemplate) {
            RecyclerViewTV recyclerViewTV = (RecyclerViewTV) mHorizontalScrollTwoTemplate.getFirstView();
            View view = recyclerViewTV.getLayoutManager().findViewByPosition(0);
            if (null != view) {
                return view;
            }
        }
        return null;
    }

    public void setNavId(String navId) {
        this.mNavId = navId;
    }
    public String getNavId() {
        return mNavId;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mMainActivity;
    }

    public void setChildActivity(ChildLauncherActivity childActivity) {
        this.mChildActivity = childActivity;
    }
    public void setPageIndex(int pageIndex) {
        this.mPageIndex = pageIndex;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
        isMain = TextUtils.isEmpty(pageId);//只有二级页面有pageId
    }

    public void setGroupElementList(List<GroupElement> groupElementList){
        this.mGroupElements = groupElementList;
    }

    public void setIsChildrenEpg(boolean b){
        isChildrenEpg = b;
        isMain = false;
    }

    public void addUrls(int viewPosition, Map<Integer, String> urls) {
        urlsMap.put(viewPosition, urls);
    }

    public Map<Integer, String> getUrlsByPosition(int position) {
        if (urlsMap.containsKey(position)) {
            return urlsMap.get(position);
        } else {
            return new HashMap<Integer, String>();
        }
    }

    public void setPlayOrNo(int viewPosition,String b){
        if (mPlayOrNo.containsKey(viewPosition) && !mPlayOrNo.get(viewPosition).equals("-1")){
            mPlayOrNo.put(viewPosition,b);
        }
    }

    public String getPlayOrNo(int viewPosition){
        if (mPlayOrNo.containsKey(viewPosition)){
            return mPlayOrNo.get(viewPosition);
        }else{
            return "";
        }
    }

    public void setUrl(int viewPosition,String url) {
        if (!TextUtils.isEmpty(url)){
            mChannelUrlMap.put(viewPosition,url);
        }
    }

    public String getChannelUrl(int viewPosition){
        return mChannelUrlMap.get(viewPosition);
    }

    public void addPlayPosition(int viewPosition, int playPosition) {
        playPositionMap.put(viewPosition, playPosition);
    }

    public int getPlayPosition(int viewPosition) {
        if (playPositionMap.containsKey(viewPosition)) {
            return playPositionMap.get(viewPosition);
        } else {
            return -1;
        }
    }

    /**
     * 对一些需要特殊处理的模板(例如轮播图、视频播放等)
     * 由于其无法感知生命周期，所以用fragment统一管理
     *
     * @param viewPosition
     * @param template
     */
    public void addFunctionTemplate(int viewPosition, PHMTemplate template) {
        specialTemplateMap.put(viewPosition, template);
    }

    public void addPlayTime(int viewPosition, long playTime) {
        playTimeMap.put(viewPosition, playTime);
    }

    public long getPlayTimeByPosition(int position) {
        if (playTimeMap.containsKey(position)) {
            return playTimeMap.get(position);
        } else {
            return 0;
        }
    }

    public void recordDuration(int position, long duration) {
        if (duration >= 0 && getUserVisibleHint() == true){
            mVideoWinDurationMap.put(position, duration);
        }
    }

    public long getDurationByPosition(int position) {
        if (mVideoWinDurationMap.containsKey(position)) {
            return mVideoWinDurationMap.get(position);
        } else {
            return 0;
        }
    }

    public void setPlayType(int viewPosition,boolean playType){
        playTypeMap.put(viewPosition,playType);
    }

    public boolean getPlayType(int viewPosition){
        if (playTypeMap.containsKey(viewPosition)){
            return playTypeMap.get(viewPosition);
        }else{
            return false;
        }
    }

    public void removeVideoTemplate(int key) {
        specialTemplateMap.remove(key);
    }

    @Override
    public void loadVODData(List<SubjectVodsList> subjectVODLists) {
        if (mPHMAdapter == null) {
            mPHMAdapter = new PHMAdapter(mGroupElements, this, subjectVODLists);
            mPHMAdapter.setIsMain(isMain);
            mPHMAdapter.setIsChildren(isChildrenEpg);
            mPHMAdapter.setOnFocusChangeListener(this);
            mPHMAdapter.setHomeLogoUrl(getActivity(),mHomeTvLogoImgUrl, mHomeTvLogoRightImgUrl,mChildHomeTvLogoLeftUrl,mChildHomeTvLogoRightUrl,mChildHomeTvLogoMiddleUrl);
            if (null != mRecyclerViewTV){
                //if(CommonUtil.getDeviceType().contains("M301H") || CommonUtil.getDeviceType().contains("HM201")){//ZJYDEPGAPP-2111 【M301H】普通版首页按下键移动到页面底部 连续按上键移动至上方 多处空窗 详情见附件
                    mRecyclerViewTV.setItemViewCacheSize(0);
                //}
                mRecyclerViewTV.setAdapter(mPHMAdapter);
            }
            requestFocusChildEpg();
        } else {
            mPHMAdapter.addDatas(subjectVODLists, mGroupElements);
            if (((TabItemPresenter)presenter).getPosition() != 0){
                mPHMAdapter.notifyItemChanged(((TabItemPresenter)presenter).getPosition());
            }else{
                mPHMAdapter.notifyDataSetChanged();
            }
        }

        if (isChildrenEpg && null != getMainActivity()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMainActivity().focusManageEpgTop(true);
                }
            },1000);
        }
    }

    private void requestFocusChildEpg(){
        if ((isChildrenEpg || !isMain) && isStartPicFinished){
            scrollToTop();
            long delayMillis = 800;
            if (CommonUtil.isE900V22EDevice()){
                delayMillis = 0;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != getFirstFocusView()){
                        focusedView = getFirstFocusView();
                        getFirstFocusView().requestFocus();
                    }else{
                        requestFocusChildEpg();
                    }
                }
            },delayMillis);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof ReflectRelativeLayout) {
            ((ReflectRelativeLayout) v).setTextViewEffect(hasFocus);
        }
    }

    @Override
    public void scrollToTop() {
        if (null != mHorizontalScrollTemplate){
            mHorizontalScrollTemplate.scrollToTop();
        }
        if (null != mHorizontalScrollSquareTemplate){
            mHorizontalScrollSquareTemplate.scrollToTop();
        }
        if (null != mHorizontalScrollSquareTimeLineTemplate){
            mHorizontalScrollSquareTimeLineTemplate.scrollToTop();
        }
        if (null != mHorizontalScrollTwoTemplate){
            mHorizontalScrollTwoTemplate.scrollToTop();
        }
        if (null != mHorizontalScrollOneTemplate){
            mHorizontalScrollOneTemplate.scrollToTop();
        }
        if (null != mVerticalScrollTestTemplate){
            mVerticalScrollTestTemplate.scrollToTop();
        }
        if (null != mRecyclerViewTV) {
            mRecyclerViewTV.scrollToPosition(0);
        }
    }

    //刷新爱看界面，置空，以加载最新的Group、Element
    public void clearData(){
        if (LauncherService.getInstance().getGroupElements().size() > 0 && mPageIndex < LauncherService.getInstance().getGroupElements().size()){
            mGroupElements = LauncherService.getInstance().getGroupElements().get(mPageIndex);
        }
        isNewPageVodData();
        if (null != mPHMAdapter){
            mPHMAdapter.setDatas(subjectVodsLists,mGroupElements);
        }
        /*if (getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setIsFreshingPbs(false);
        }*/
    }

    public void loadData() {
        SuperLog.debug(TAG,"liuxia loadData"+" nav="+testGetName());
        if(isLoadData){
            //防止重复加载
            SuperLog.debug(TAG,"liuxia loadData returned"+" nav="+testGetName());
            return;
        }
        if (presenter == null) {
            initPresenter();
            presenter.attachView(this);
        }
        resetPlayOrNo();
        if ((null == mGroupElements || mGroupElements.size() == 0) && isMain){
            if (LauncherService.getInstance().getGroupElements().size() > 0 && mPageIndex < LauncherService.getInstance().getGroupElements().size()){
                mGroupElements = LauncherService.getInstance().getGroupElements().get(mPageIndex);
            }
        }
        if (isNewPageVodData()){
            loadVODData(subjectVodsLists);
        }else{
            mDesignateGroupElementList = FragmentUtils.getDesignateUserGroupElement(mGroupElements);
            if (!CollectionUtil.isEmpty(mDesignateGroupElementList)){
                mTabItemPresenter.queryPBSRemixRecommend(getContentIdList(),mPbsRemixRecommendListener,mDesignateGroupElementList.get(mDesignateIndex).getGroup().getExtraData().getAppointedId(),"6");
            }else{
                presenter.queryHomeEpg(mNavId, mGroupElements, getActivity());
            }
        }
        isLoadData = true;
    }
    //通栏运营位仅对指定用户展示,是否有只能推荐数据，没有数据则移除group
    private class PBSRemixRecommendListener implements EpgTopFunctionMenu.OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse) {
            if (null == pbsRemixRecommendResponse || CollectionUtil.isEmpty(pbsRemixRecommendResponse.getRecommends())){
                mGroupElements.remove(mDesignateGroupElementList.get(mDesignateIndex));
            }
            mDesignateGroupElementList.remove(mDesignateIndex);
            if (!CollectionUtil.isEmpty(mDesignateGroupElementList)){
                mTabItemPresenter.queryPBSRemixRecommend(getContentIdList(),mPbsRemixRecommendListener,mDesignateGroupElementList.get(mDesignateIndex).getGroup().getExtraData().getAppointedId(),"6");
            }else{
                presenter.queryHomeEpg(mNavId, mGroupElements, getActivity());
            }
        }

        @Override
        public void getRemixRecommendDataFail() {
            presenter.queryHomeEpg(mNavId, mGroupElements, getActivity());
        }
    }

    private List<SubjectVodsList> subjectVodsLists = new ArrayList<>();

    private boolean isNewPageVodData(){
        subjectVodsLists = new ArrayList<>();
        if (null != mGroupElements && mGroupElements.size() > 0){
            for (GroupElement groupElement : mGroupElements){
                if (null != groupElement.getSubjectVODLists() && groupElement.getSubjectVODLists().size() > 0){
                    subjectVodsLists.addAll(groupElement.getSubjectVODLists());
                }
            }
        }
        if (subjectVodsLists.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    private String testGetName(){
        try{
            return mMainActivity.getmNavigates().get(mPageIndex).getNameDialect().get(0).getValue();
        }catch (Exception e){
            SuperLog.error(TAG,e);
            return "error";
        }
    }

    @Override
    protected void initView(View view) {
        SuperLog.debug(TAG,"initView in onViewCreated,index="+mPageIndex+" nav="+testGetName());
        mRecyclerViewTV = (RecyclerViewTV) view.findViewById(R.id.rv_container_list);
        mRecyclerViewTV.setLayoutManager(new TVLinearLayoutManager(activity, mRecyclerViewTV));
        mRecyclerViewTV.addItemDecoration(new PHMDividerItemDecoration());
        mRecyclerViewTV.setFocusable(false);
        mRecyclerViewTV.setScrollListener(new RecyclerViewTV.ScrollListener() {
            @Override
            public void startScroll(int keyCode) {
                Glide.with(PHMFragment.this).pauseRequests();
            }

            @Override
            public void endScroll(int keyCode) {
                if (mRecyclerViewTV.isFastScroll()) {
                    mRecyclerViewTV.setIsFastScroll(false);
                    Glide.with(PHMFragment.this).resumeRequests();
                }
                PHMAdapter adapter = (PHMAdapter) mRecyclerViewTV.getAdapter();
                if (null != presenter && null != adapter && adapter.getWillShowPosition() % (((TabItemPresenter)presenter).getCount()-1) == 0){
                    mSubjectIDLists = ((TabItemPresenter) presenter).getSubjectIDLists();
                    if (!CollectionUtil.isEmpty(mSubjectIDLists) &&
                            mSubjectIDLists.size() > (((TabItemPresenter)presenter).getOffSet()+1)*((TabItemPresenter)presenter).getCount()){
                        ((TabItemPresenter)presenter).setOffSet();
                        presenter.queryHomeEpg(mNavId, mGroupElements, getActivity());
                    }
                }

                childToTop(keyCode);

                childrenToTop(keyCode);

            }
        });

        mRecyclerViewTV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mMainActivity){
                    if (mRvScrollY == -1){
                        mRvScrollY = 0;
                    }else{
                        RelativeLayout topView = mMainActivity.getTopView();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topView.getLayoutParams();

                        mRvScrollY = mRecyclerViewTV.computeVerticalScrollOffset();
                        //mRvScrollY += dy;
                        if (null != mMainActivity && mMainActivity.isChildrenEpgFragment() && mRvScrollY < 35){
                            layoutParams.setMargins(0,0,0,0);
                        }else{
                            layoutParams.setMargins(0,-mRvScrollY,0,0);
                        }
                        topView.setLayoutParams(layoutParams);
                    }
                }

                if (getActivity() instanceof ChildLauncherActivity) {
                    if (dy > 0){
                        ((ChildLauncherActivity)getActivity()).showBackTopHint(true);
                    }
                }

                if (null != mMainActivity && mMainActivity.isChildrenEpgFragment()){
                    if (dy > 0){
                        mMainActivity.focusManageEpgTop(false);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mRecyclerViewTV.setIsFastScroll(false);
                        mRecyclerViewTV.setIsScrolling(false);
                        Glide.with(PHMFragment.this).resumeRequests();

                        /**
                         * 如果当前页面显示区域有视频播放窗口
                         * 滚动触发首页视频播放窗口去播放（一行两列、一行三列、轮播）
                         * */
                        /*if (null != mMixVideoTemplate  && null != mChannelUrlMap && mChannelUrlMap.size() > 0){
                            mMixVideoTemplate.startPlay(mChannelUrlMap.get(mMixVideoTemplate.getviewPosition()));
                        }*/

                        if (mMixVideoTemplateMap.size() > 0 && null != mChannelUrlMap && mChannelUrlMap.size() > 0) {
                            for (Map.Entry<Integer, MixVideoTemplate> map : mMixVideoTemplateMap.entrySet()) {
                                MixVideoTemplate value = map.getValue();
                                value.startPlay(mChannelUrlMap.get(value.getIndex()));
                            }
                        }

                        if (mVideoTemplateMap.size() > 0){
                            for (Map.Entry<Integer, PlayViewMainEpg> map : mVideoTemplateMap.entrySet()) {
                                PlayViewMainEpg value = map.getValue();
                                value.startPlay();
                            }
                        }

                        if (null != mVideoLiveTemplate) {
                            mVideoLiveTemplate.scrollEndToPlay();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //加载数据中的模版数据不为空，且数据量大于当期页码数
        if (isMain) {
            if (!CollectionUtil.isEmpty(LauncherService.getInstance().getGroupElements()) && LauncherService.getInstance().getGroupElements().size() > mPageIndex) {
                mGroupElements = LauncherService.getInstance().getGroupElements().get(mPageIndex);
            }
        } else {
            if (!TextUtils.isEmpty(pageId) && !CollectionUtil.isEmpty(LauncherService.getInstance().getChildGroupElement(pageId)) && LauncherService.getInstance().getChildGroupElement(pageId).size() > mPageIndex) {
                mGroupElements = LauncherService.getInstance().getChildGroupElement(pageId).get(mPageIndex);
            }
        }

        if (mGroupElements.size() > 0) {
            for (GroupElement groupElement : mGroupElements) {
                for (int i = 0; i < groupElement.getElement().size(); i++) {
                    Group group = groupElement.getGroup();
                    if (null != group && !TextUtils.isEmpty(group.getType()) && group.getType().equalsIgnoreCase("7")){
                        continue;
                    }
                    List<ElementData> elementDataList = groupElement.getElement().get(i).getElementDataList();
                    if (null != elementDataList && elementDataList.size() > 0 && !TextUtils.isEmpty(elementDataList.get(0).getElementAction().getActionURL())) {
                        String contentId = ZJVRoute.getContentValue(elementDataList.get(0).getElementAction().getActionURL(), ZJVRoute.ActionUrlKeyType.CONTENT_ID);
                        if (elementDataList.get(0).getElementAction().getActionURL().startsWith("http")
                                || elementDataList.get(0).getElementAction().getActionURL().startsWith("https")){
                            //mElementContentId.add(elementDataList.get(0).getElementAction().getActionURL());
                        }else if (!TextUtils.isEmpty(contentId) && !mElementContentId.contains(contentId)) {
                            mElementContentId.add(contentId);
                        }
                    }
                }
            }
        }

        loadHomeTvLogo();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_launcher_phm;
    }

    @Override
    protected void initPresenter() {
        presenter = new TabItemPresenter();
    }

    public TabItemPresenter getPresenter() {
        return (TabItemPresenter) presenter;
    }

    public boolean isNoTemplate() {
        if (mRecyclerViewTV == null) {
            return true;
        }
        return mRecyclerViewTV.getChildCount() == 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        focusedView = getActivity().getWindow().getDecorView().findFocus();
        for (int key : specialTemplateMap.keySet()) {
            specialTemplateMap.get(key).onFragmentPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SuperLog.debug(TAG,"onResume liuxia PHMFragment onResume"+" nav="+testGetName());

        //延时请求防止卡顿,时间为经验值,可以根据实际测试结果调整
        handler.postDelayed(r,1000);

        //解决从其他界面返回时概率性丢失焦点的问题
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg() && null != focusedView) {
            focusedView.postDelayed(() -> {
                if(null != focusedView){
                    focusedView.requestFocus();
                    focusedView = null;
                }
            }, 350);
        }

        if(!(NotCanPlayNow&& this instanceof MyPHMFragment)) {
            for (int key : specialTemplateMap.keySet()) {
                specialTemplateMap.get(key).onFragmentResume();
            }
        }
    }

    private final Runnable r = new LoadRunnable(this);

    //使用静态内部类防止内存泄露
    private static class LoadRunnable implements Runnable{
        private final WeakReference<PHMFragment> fragment;

        public LoadRunnable(PHMFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            if(fragment !=null && fragment.get() != null){
                fragment.get().loadData();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            SuperLog.debug(TAG,"setUserVisibleHint liuxia removeCallbacks"+" nav="+testGetName());
            handler.removeCallbacks(r);
            //切换桌面后清空记录的播放记录
            mVideoWinDurationMap = new HashMap<>();
            mIsPlayCompleteMap = new HashMap<>();
        }

        if (isVisibleToUser) {
            SuperLog.debug(TAG,"VisibleToUser"+" nav="+testGetName());
            if (!isLoadData){
                //延时请求防止卡顿,时间为经验值,可以根据实际测试结果调整
                handler.postDelayed(r,1000);
            }
            for (int key : specialTemplateMap.keySet()) {
                specialTemplateMap.get(key).onFragmentResume();
            }
        } else {
            for (int key : specialTemplateMap.keySet()) {
                specialTemplateMap.get(key).onFragmentPause();
            }
            //tab页面隐藏时置空假导航栏的背景图片
            if (null != mPHMAdapter){
                mPHMAdapter.setRecyclerViewTvBackgroundNull();
            }
        }
    }

    public void setHorizontalScrollTemplate(PHMTemplate template) {
        mHorizontalScrollTemplate = (HorizontalScrollTemplate) template;
    }

    public void setHorizontalScrollSquareTemplate(PHMTemplate template) {
        mHorizontalScrollSquareTemplate = (HorizontalScrollSquareTemplate) template;
    }
    public void setHorizontalScrollTimeLineTemplate(PHMTemplate template) {
        mHorizontalScrollSquareTimeLineTemplate = (HorizontalScrollTimeLineTemplate) template;
    }
    public void setHorizontalScrollOneTemplate(PHMTemplate template) {
        mHorizontalScrollOneTemplate = (HorizontalScrollOneTemplate) template;
    }
    public void setVerticalScrollTestTemplate(PHMTemplate template) {
        mVerticalScrollTestTemplate = (VerticalScrollTestTemplate) template;
    }
    public void setHorizontalScrollTwoTemplate(PHMTemplate template) {
        mHorizontalScrollTwoTemplate = (HorizontalScrollTwoTemplate) template;
    }

    public boolean isNotCanPlayNow()
    {
        return NotCanPlayNow;
    }

    /**
     * 刷新数据时，重置VOD自动播放
     * */
    private void resetPlayOrNo(){
        if (null != mPlayOrNo){
            for (Integer key : mPlayOrNo.keySet()){
                mPlayOrNo.put(key,"-1");
            }
        }
    }

    private void loadHomeTvLogo() {

        String imgUrlLeft = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.HOME_TV_LOG_LEFT));
        String imgUrlRight = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.HOME_TV_LOG_RIGHT));

        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
        if (!TextUtils.isEmpty(launcherLink) && null != AuthenticateManager.getInstance().getUserInfo()) {
            String url = "http://" + AuthenticateManager.getInstance().getUserInfo
                    ().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo()
                    .getPort() + launcherLink;
            //首页logo url处理
            if (!TextUtils.isEmpty(imgUrlLeft)){
                mHomeTvLogoImgUrl = url + imgUrlLeft;
            }else{
                mHomeTvLogoImgUrl = "";
            }
            if (!TextUtils.isEmpty(imgUrlRight)){
                mHomeTvLogoRightImgUrl = url + imgUrlRight;
            }else{
                mHomeTvLogoRightImgUrl = "";
            }

            //二级桌面Logo处理
            String imgUrlChildLeft = "";
            String imgUrlChildRight = "";
            String imgUrlChildMiddle = "";
            //pageId = 11292_2224@1572255344994
            //截取二级页面id,拼接key,用于从附属资源中取出配置的图片
            if(!TextUtils.isEmpty(pageId) && pageId.contains("_") && pageId.contains("@")
                    && pageId.indexOf("_") < pageId.indexOf("@")){
                String substring = pageId.substring(pageId.indexOf("_"), pageId.indexOf("@"));
                if (!TextUtils.isEmpty(substring)){
                    imgUrlChildLeft = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.SUB_HOME_TV_LEFT_LOGO + substring));
                    imgUrlChildRight = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.SUB_HOME_TV_RIGHT_LOGO + substring));
                    imgUrlChildMiddle = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(MainActivity.SUB_HOME_TV_MIDDLE_LOGO + substring));
                }

            }

            if (!TextUtils.isEmpty(imgUrlChildLeft)){
                mChildHomeTvLogoLeftUrl = url + imgUrlChildLeft;
            }
            if (!TextUtils.isEmpty(imgUrlChildRight)){
                mChildHomeTvLogoRightUrl = url + imgUrlChildRight;
            }
            if (!TextUtils.isEmpty(imgUrlChildMiddle)){
                mChildHomeTvLogoMiddleUrl = url + imgUrlChildMiddle;
            }
        }

    }

    public void setIsStartPicFinished(boolean isStartPicFinished){
        this.isStartPicFinished = isStartPicFinished;
        //requestFocusChildEpg();
    }

    public boolean getNotCanPlayNow(){
        return NotCanPlayNow;
    }

    public void setNotCanPlayNow(boolean boo){
        NotCanPlayNow = boo;
    }

    private static class PHMFragmentHandler extends Handler{
        private final WeakReference<ChildLauncherActivity> weakReference;
        private final ChildLauncherActivity activity;

        public PHMFragmentHandler(ChildLauncherActivity activity){
            this.weakReference = new WeakReference<>(activity);
            this.activity = activity;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != weakReference.get()){
                switch (msg.what) {
                    case 0:
                        if (activity.getFocusView().getParent() instanceof PHMTemplate){
                            if (msg.obj instanceof  RecyclerViewTV && ((RecyclerViewTV)msg.obj).getChildLayoutPosition((View) activity.getFocusView().getParent()) == 1 && ((RecyclerViewTV)msg.obj).getScollOffScroll() == 0){
                                activity.showBackTopHint(false);
                            }
                        }else if (activity.getFocusView().getParent().getParent() instanceof PHMTemplate){
                            if (msg.obj instanceof  RecyclerViewTV && ((RecyclerViewTV)msg.obj).getChildLayoutPosition((View) activity.getFocusView().getParent().getParent()) == 1 && ((RecyclerViewTV)msg.obj).getScollOffScroll() == 0){
                                activity.showBackTopHint(false);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //儿童版
    private static class PHMChildFragmentHandler extends Handler{
        private final MainActivity mMainActivity;
        private final WeakReference<MainActivity> weakReference;

        public PHMChildFragmentHandler(MainActivity activity){
            this.weakReference = new WeakReference<>(activity);
            this.mMainActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != weakReference.get()){
                switch (msg.what) {
                    case 1:
                        if (mMainActivity.getLauncherFocusHelper().getFocusView().getParent() instanceof PHMTemplate) {
                            if (msg.obj instanceof RecyclerViewTV && ((RecyclerViewTV) msg.obj).getChildLayoutPosition((View) mMainActivity.getLauncherFocusHelper().getFocusView().getParent()) == 1 && ((RecyclerViewTV) msg.obj).getScollOffScroll() == 0) {
                                mMainActivity.focusManageEpgTop(true);
                            }
                        } else if (mMainActivity.getLauncherFocusHelper().getFocusView().getParent().getParent() instanceof PHMTemplate) {
                            if (msg.obj instanceof RecyclerViewTV && ((RecyclerViewTV) msg.obj).getChildLayoutPosition((View) mMainActivity.getLauncherFocusHelper().getFocusView().getParent().getParent()) == 1 && ((RecyclerViewTV) msg.obj).getScollOffScroll() == 0) {
                                mMainActivity.focusManageEpgTop(true);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * EPG无需更新launcher时，点击刷新，刷新效果
     * */
    public void setRecycleViewRefresh(){
        mRecyclerViewTV.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewTV.setVisibility(View.VISIBLE);
            }
        },300);
    }

    //二级桌面
    private void childToTop(int keyCode){
        if (getActivity() instanceof ChildLauncherActivity && keyCode == KeyEvent.KEYCODE_DPAD_UP){
            if (null == phmFragmentHandler){
                phmFragmentHandler = new PHMFragmentHandler((ChildLauncherActivity)getActivity());
            }
            if (phmFragmentHandler.hasMessages(0)){
                phmFragmentHandler.removeMessages(0);
            }

            Message message = new Message();
            message.what = 0;
            message.obj = mRecyclerViewTV;

            phmFragmentHandler.sendMessageDelayed(message,300);
        }
    }

    //儿童版
    private void childrenToTop(int keyCode){
        if (null != mMainActivity && mMainActivity.isChildrenEpgFragment() && keyCode == KeyEvent.KEYCODE_DPAD_UP){
            if (null == mChildrenFragmentHandler){
                mChildrenFragmentHandler = new PHMChildFragmentHandler(mMainActivity);
            }
            if (mChildrenFragmentHandler.hasMessages(1)){
                mChildrenFragmentHandler.removeMessages(1);
            }

            Message message = new Message();
            message.what = 1;
            message.obj = mRecyclerViewTV;

            mChildrenFragmentHandler.sendMessageDelayed(message,300);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SuperLog.debug(TAG,"onStop liuxia removeCallbacks"+" nav="+testGetName());
        handler.removeCallbacks(r);
        if (null != phmFragmentHandler){
            phmFragmentHandler.removeCallbacksAndMessages(null);
            phmFragmentHandler = null;
        }
        if (null != mChildrenFragmentHandler){
            mChildrenFragmentHandler.removeCallbacksAndMessages(null);
            mChildrenFragmentHandler = null;
        }
    }
}