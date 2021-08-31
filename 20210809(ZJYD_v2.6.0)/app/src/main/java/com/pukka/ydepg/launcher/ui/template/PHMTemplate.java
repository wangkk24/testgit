package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommend;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollVerticalTextview;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.home.view.MarqueeText;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.moudule.home.ui.templet.BaseTempletView.java
 * @date: 2018-01-22 9:22
 * @version: V1.0 描述当前版本功能
 */
public class PHMTemplate extends RelativeLayout {
    private static final String SCROLLTEXTNUM = "scrollTextNum";
    private static final String SCROLLTEXT_PRE = "scrollText_";

    private static final String APPOINTEDID = "appointedId";

    protected ReflectRelativeLayout lastView;   //模板最后一个View
    private static final String NO_NAME = "noname"; //包名是NO_NAME隐藏包名
    protected ImageView ivBackgroud;
    private String backgroud;
    protected Context context;
    protected int layoutId;
    AutoScrollVerticalTextview scrollVerticalTextview;//垂直滚动文字
    protected TextView mTitleTv;
    protected MarqueeText marqueeText;
    protected ImageViewExt mTitleIv;
    protected ImageView mClipImage;//测试子控件CLip父控件
    protected View mViewView;
    protected List<ReflectRelativeLayout> elementViews = new ArrayList<>();
    protected static final String TAG = "CommonTemplate";
    private boolean hasTitle = false;  //有标题
    private List<VOD> vods= new ArrayList<>();  //动态数据
    protected PHMFragment fragment;
    protected View.OnFocusChangeListener onFocusChangeListener;

    private Group mGroup;

    public PHMTemplate(Context context) {
        super(context);
    }

    public boolean isTitleNull(GroupElement groupElement){
        if( null == groupElement.getGroup().getNameDialect() || 0 == groupElement.getGroup().getNameDialect().size()){
            return true;
        }
        String title = groupElement.getGroup().getNameDialect().get(0).getValue();
        if (TextUtils.isEmpty(title) || TextUtils.equals(title, NO_NAME) || title.contains("Group") || title.contains("group")) {
            return true;
        } else {
            return false;
        }
    }


    public void setTitle(Group group,int position) {
        mGroup = group;
        navIndex = position;
        String url = mGroup.getNameDialect().get(0).getImage();
        if (null == mTitleIv){
            mTitleIv = findViewById(R.id.iv_home_title);
        }
        RequestOptions options = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
        mTitleIv.setVisibility(VISIBLE);
        Glide.with(context).load(GlideUtil.getUrlForJsonPicAddress(url)).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                SuperLog.debug(TAG,"load group title image failed and to load title txt, URL="+model);
                if (null != mTitleIv){
                    mTitleIv.setVisibility(GONE);
                }
                setTitle(mGroup.getNameDialect().get(0).getValue(),navIndex,mGroup.getExtraData());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                SuperLog.debug(TAG,"load group title image success");
                if (null != mTitleIv){
                    hasTitle = true;
                    mTitleIv.setVisibility(VISIBLE);
                }
                return false;
            }
        }).into(mTitleIv);
    }
    /**
     * 模板有标题而且标题有内容就显示。标题名是groupname
     *
     * @param title
     */
    public void setTitle(String title,int position,ExtraData extraData) {
        SuperLog.debug(TAG,"load group title txt,title="+title);
        if (null == mTitleTv) {
            mTitleTv = findViewById(R.id.tv_home_title);
        }
        if (mTitleTv != null) {
            if (!TextUtils.isEmpty(title) && !TextUtils.equals(title, NO_NAME)) {
                if (title.contains("Group") || title.contains("group")) {
                    hideTitle();
                    return;
                }
                Typeface typeface = OTTApplication.getTypeFace();
                if (typeface != null) {
                    mTitleTv.setTypeface(typeface);
                }
                hasTitle = true;
                if (mTitleTv.getVisibility() == View.GONE) {
                    mTitleTv.setVisibility(View.VISIBLE);
                    if (null != mViewView) {
                        mViewView.setVisibility(GONE);
                    }
                }

                //儿童版页面标题字体特殊处理
                if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                    Drawable titleLeftIcon = getResources().getDrawable(
                            R.drawable.children_title_icon);
                    titleLeftIcon.setBounds(0, 0, titleLeftIcon.getMinimumWidth(), titleLeftIcon.getMinimumHeight());
                    mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                    mTitleTv.setCompoundDrawablesWithIntrinsicBounds(titleLeftIcon, null, null, null);
                    mTitleTv.setCompoundDrawablePadding(6);
                }
                mTitleTv.setText(title);
                Utils.setGroupTitleColor(extraData, mTitleTv);
            } else {
                hideTitle();
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 解决快速滚动中向右或者向左焦点框出错的问题
        if (fragment.getRecyclerViewTV().isScrolling() && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 是否有标题
     *
     * @return
     */
    public boolean hasTitle() {
        return hasTitle;
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitle() {
        hasTitle = false;
        if (mTitleTv != null) {
            mTitleTv.setVisibility(View.GONE);
            marqueeText.setVisibility(View.GONE);
        }
    }

    public PHMTemplate(Context context, int layoutId, PHMFragment fragment, View.OnFocusChangeListener focusChangeListener) {
        super(context);
        this.fragment = fragment;
        this.onFocusChangeListener = focusChangeListener;
        this.layoutId = layoutId;
        setFocusable(false);
        initView(context, layoutId);
        setElements(fragment);
    }


    public PHMTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PHMTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化v布局
     *
     * @param context
     * @param layoutId
     */
    protected void initView(Context context, int layoutId) {
        this.context = context;
        //SuperLog.debug(TAG, "layoutId:" + layoutId);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(layoutId, this, true);
        setClipChildren(false);
        setClipToPadding(false);
        mTitleTv = (TextView) findViewById(R.id.tv_home_title);
        marqueeText = (MarqueeText) findViewById(R.id.tv_marquee_title);
        mViewView = findViewById(R.id.view_view);
        mClipImage = (ImageView) findViewById(R.id.clip_image);
    }


    /**
     * viewpager右翻下一页第一个显示的焦点
     *
     * @return
     */
    public View getFirstView() {
        if (CollectionUtil.isEmpty(elementViews)) {
            return null;
        } else {
            return elementViews.get(0);
        }
    }

    /**
     * viewpager左翻上一页第一个显示的焦点
     *
     * @return
     */
    public View getLastView() {
        if (lastView == null && elementViews.size() > 0) {
            lastView = elementViews.get(elementViews.size() - 1);
        }
        return lastView;
    }

    /**
     * 模块的layoutId
     *
     * @return
     */
    public int getLayoutId() {
        return layoutId;
    }

    private String display_tracker;
    private StringBuilder sbRecommendId = new StringBuilder();
    private StringBuilder sbRecommendItemId = new StringBuilder();
    private StringBuilder sbAppointedId = new StringBuilder();
    private StringBuilder sbScenId = new StringBuilder();
    private StringBuilder sbrecommendType = new StringBuilder();

    //加载elementd的静态数据
    private void setDefaultElementData() {
        List<Element> elements = mGroupElement.getElement();
        Group group = mGroupElement.getGroup();
        if (CollectionUtil.isEmpty(elements)) {
            return;
        }
        for (int i = 0; i < elements.size(); i++) {//如果是本地数据，设置本地显示
            if (!recommendMap.containsKey(i) && i < elementViews.size()) { //防止数组越界
                if (!CollectionUtil.isEmpty(elements.get(i).getElementDataList())) {
                    int finalI = i;
                    ReflectRelativeLayout reflectRelativeLayout = elementViews.get(finalI);
                    if (TextUtils.equals(elements.get(i).getForceDefaultData(), "true")) {
                        fragment.getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reflectRelativeLayout.setIsRecommend(false,null, RecommendData.RecommendType.HAND_TYPE);
                                reflectRelativeLayout.setElement(elements.get(finalI));
                                reflectRelativeLayout.setGroup(group,navIndex);
                                reflectRelativeLayout.setDefaultData(true);
                                reflectRelativeLayout.setElementData(elements.get(finalI));
                            }
                        });
                    } else {
                        if (null != vods && vods.size() > i) {
                            fragment.getMainActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    reflectRelativeLayout.setDefaultData(false);
                                    reflectRelativeLayout.setIsRecommend(false,null, RecommendData.RecommendType.HAND_TYPE);
                                    reflectRelativeLayout.setElement(elements.get(finalI));
                                    reflectRelativeLayout.setGroup(group,navIndex);
                                    reflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(finalI));
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * 数据绑定元素
     *
     * @param dataList
     * @param position
     */
    public <T> void bindSubjectVOD(List<T> dataList, DataLoaderAdapter adapter, int position) {
        this.navIndex = position;
        this.mAdapter = adapter;
        this.vods = (List<VOD>) dataList;
        int realIndex = 0;
        int size = elementViews.size();
        if (!TextUtils.isEmpty(appointId)){
            return;
        }

        for (int i = 0; i < size; i++) {
            ReflectRelativeLayout reflectRelativeLayout = elementViews.get(i);
            if (reflectRelativeLayout.isDefaultData()) {
                SuperLog.debug(TAG, "i:" + i);
                continue;
            }
            if (null != dataList && realIndex < dataList.size()) {
                reflectRelativeLayout.parseVOD(adapter, dataList.get(realIndex));
                realIndex++;
            } else {
                reflectRelativeLayout.setEmptyContent();
                reflectRelativeLayout.setOnClickListener(null);
            }
        }
    }


    /**
     * 设置elements到list
     */
    public void setElements(PHMFragment fragment) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof ReflectRelativeLayout) {
                ((ReflectRelativeLayout) view).setFragment(fragment);
                if (onFocusChangeListener != null) {
                    view.setOnFocusChangeListener(onFocusChangeListener);
                }
                elementViews.add((ReflectRelativeLayout) view);
                if (fragment.getLoadLayoutIndex() == 0 && i == 1 && view.getId() == R.id.rl_topItem_container_0_11){
                    fragment.setFirstChildView(view);
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        recordMainRecommend(sbRecommendId.toString());
        recordMainRecommendJiuTian(sbRecommendItemId.toString());
        fragment.addFunctionTemplate(navIndex, this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        fragment.removeVideoTemplate(navIndex);
    }

    public void onFragmentPause() {
        if (!CollectionUtil.isEmpty(elementViews)){
            for (ReflectRelativeLayout relativeLayout : elementViews){
                relativeLayout.onPause();
            }
        }
    }

    public void onFragmentResume() {
        if (!CollectionUtil.isEmpty(elementViews)){
            for (ReflectRelativeLayout relativeLayout : elementViews){
                relativeLayout.onResume();
            }
        }
    }

    public int getNavIndex(){
        return navIndex;
    }

    private DataLoaderAdapter mAdapter;
    private GroupElement mGroupElement;
    private int navIndex;

    private TabItemPresenter tabItemPresenter = new TabItemPresenter();

    //混合推荐 推荐ID
    private String appointId = "";

    private PBSRemixRecommendListener pbsRemixRecommendListener = new PBSRemixRecommendListener();

    public void setElementData(GroupElement groupElement,int navIndex) {
        this.mGroupElement = groupElement;
        this.navIndex = navIndex;

        appointId = getApponitId(mGroupElement.getElement(),mGroupElement.getGroup());

        //推荐模板 假导航栏不需判断请求智能推荐数据
         if (layoutId != R.layout.nav_view_layout && !TextUtils.isEmpty(appointId)){
            //mDetailPresenter.queryPBSDuplicate(vodDuplicateCallBack, sceneId,fragment.getContentIdList());
            if (null != recommendMap && recommendMap.size() > 0){
                loadRecommendData();
                return;
            }
            if (null != tabItemPresenter){
                tabItemPresenter.queryPBSRemixRecommend(fragment.getContentIdList(),pbsRemixRecommendListener,appointId,"6");
            }
        }else{
            List<Element> elements = groupElement.getElement();
            Group group = groupElement.getGroup();
            if (CollectionUtil.isEmpty(elements)) {
                return;
            }
            //非推荐模板  走正常逻辑
            for (int i = 0; i < elements.size(); i++) {  //如果是本地数据，设置本地显示
                if (i < elementViews.size()) { //防止数组越界
                    ReflectRelativeLayout reflectRelativeLayout = elementViews.get(i);
                    if (TextUtils.equals(elements.get(i).getForceDefaultData(), "true")) {
                        reflectRelativeLayout.setDefaultData(true);
                        if (!CollectionUtil.isEmpty(elements.get(i).getElementDataList())) {
                            reflectRelativeLayout.setGroup(group);
                            reflectRelativeLayout.setElementData(elements.get(i));
                            reflectRelativeLayout.setGroup(group,navIndex);
                        }
                    } else {
                        reflectRelativeLayout.setDefaultData(false);
                        reflectRelativeLayout.setGroup(group);
                        reflectRelativeLayout.setElementData(elements.get(i));
                        reflectRelativeLayout.setGroup(group,navIndex);
                    }
                }
            }
        }
    }

    public Map<Integer,String> getElementAppMap(){
        return elementAppMap;
    }

    private Map<Integer,String> elementAppMap = new HashMap<>();
    public String getApponitId(List<Element> elementList,Group group) {
        StringBuffer sb = new StringBuffer();
        if (null != elementList && elementList.size() > 0) {
            for (int i = 0; i < elementList.size(); i++) {
                if (null != elementList.get(i).getExtraData() && !TextUtils.isEmpty(elementList.get(i).getExtraData().get(APPOINTEDID))) {
                    elementAppMap.put(i,elementList.get(i).getExtraData().get(APPOINTEDID));
                    if (i == 0 || TextUtils.isEmpty(sb.toString())){
                        sb.append(elementList.get(i).getExtraData().get(APPOINTEDID));
                    }else{
                        sb.append(",");
                        sb.append(elementList.get(i).getExtraData().get(APPOINTEDID));
                    }
                }
            }
        }
        if (null != group.getExtraData()){
            if (!TextUtils.isEmpty(sb.toString()) && !TextUtils.isEmpty(group.getExtraData().getAppointedId())){
                sb.append(",");
                sb.append(group.getExtraData().getAppointedId());
            }else if (TextUtils.isEmpty(sb.toString()) && !TextUtils.isEmpty(group.getExtraData().getAppointedId())){
                sb.append(group.getExtraData().getAppointedId());
            }
        }
        return sb.toString();
    }


    @Override
    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }


    public PHMFragment getFragment() {
        return fragment;
    }

    //适配DPI==240,x1.5
    private int resetData(int value) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (dm.densityDpi == 240){
            return (int) (value*1.5);
        }else{
            return value;
        }
    }

    public void setExpendGroupParams(ExtraData extraData, String bgroud) {
        this.backgroud = bgroud;
        // 设置背景图
        if (!TextUtils.isEmpty(backgroud)) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
            this.measure(w, h);
            int width = context.getResources().getDisplayMetrics().widthPixels;//resetData(1280);//this.getMeasuredWidth();//
            int height = this.getMeasuredHeight();
            String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
            // String launcherLink = "/PHS/11055";
            if (!TextUtils.isEmpty(backgroud) && !TextUtils.isEmpty(launcherLink)) {
                backgroud = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo().getPort() + launcherLink + backgroud;
                SuperLog.debug(TAG, backgroud);
                if (null == ivBackgroud) {
                    ivBackgroud = new ImageView(context);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
                    ivBackgroud.setLayoutParams(params);
                    ivBackgroud.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivBackgroud.setFocusable(false);
                    addView(ivBackgroud, 0);
                }
                if (fragment != null && !backgroud.equalsIgnoreCase("null")) {
                    RequestOptions options  = new RequestOptions()
                            .override(width, height);

                    Glide.with(fragment).load(backgroud).apply(options).into(ivBackgroud);
                }
            }
        } else {
            if (null != ivBackgroud) {
                ivBackgroud.setImageDrawable(null);
                removeView(ivBackgroud);
                ivBackgroud = null;
            }
        }

        // 设置跑马灯，只有标题显示的时候才显示跑马灯
        if (null != mTitleTv && mTitleTv.getVisibility() == VISIBLE && null != extraData) {
            MarqueeText mTextView = (MarqueeText) findViewById(R.id.tv_marquee_title);
            String marqueeStr = extraData.getMarquee();
            if (!TextUtils.isEmpty(marqueeStr)) {
                //跑马灯不为空
                if (null != mTextView) {
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(marqueeStr);
                }
            } else {
                mTextView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 为静态资源位设置垂直滚动文字
     *
     * @param reflectRelativeLayout
     * @param extraData
     */
    public void setScrollTexts(ReflectRelativeLayout reflectRelativeLayout, Map<String, String> extraData) {
        reflectRelativeLayout.getPosterImgView().setVisibility(GONE);
        reflectRelativeLayout.setBackgroundColor(getResources().getColor(R.color.scroll_static_resource_item_bg));
        if (null == extraData) {
            return;
        }
        if (null != scrollVerticalTextview) {
            scrollVerticalTextview.stopAutoScroll();
            scrollVerticalTextview.setText("");
            scrollVerticalTextview.setVisibility(GONE);
            scrollVerticalTextview = null;
        }
        List<String> strs = new ArrayList<String>();
        strs = getTextsFromExtraData(extraData);

        scrollVerticalTextview = new AutoScrollVerticalTextview(context);
        RelativeLayout container = new RelativeLayout(context);
        LayoutParams rlParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtil.getDimension(context, R.dimen.pan_scroll_vertical_text_container_height));
        rlParams.bottomMargin = 40;
        rlParams.leftMargin = 20;
        rlParams.rightMargin = 20;
        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        container.setClipChildren(false);
        container.setClipToPadding(false);
        reflectRelativeLayout.addView(container, rlParams);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtil.getDimension(context, R.dimen.pan_scroll_vertical_text_height));
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        scrollVerticalTextview.setFocusable(false);
        container.addView(scrollVerticalTextview, params);
        scrollVerticalTextview.setClipToPadding(false);
        scrollVerticalTextview.setClipChildren(false);
        container.setFocusable(false);

        scrollVerticalTextview.setText(20, 0, getResources().getColor(R.color.color_template_title));
        scrollVerticalTextview.setTextList(strs);

        String timeStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.SCROLLTEXT_SWITCHING_TIME);
        long time = 3L * 1000;//默认切换时间
        if (!TextUtils.isEmpty(timeStr)) {
            time = Long.parseLong(timeStr) * 1000;
        }
        scrollVerticalTextview.setTextStillTime(time);
        scrollVerticalTextview.setAnimTime(800);
        scrollVerticalTextview.startAutoScroll();
    }

    private List<String> getTextsFromExtraData(Map<String, String> extraData) {
        List<String> strs = new ArrayList<String>();
        String scrollTextNum = extraData.get(SCROLLTEXTNUM);
        if (!TextUtils.isEmpty(scrollTextNum)) {
            int num = Integer.parseInt(scrollTextNum);
            for (int i = 0; i < num; i++) {
                String text = extraData.get(SCROLLTEXT_PRE + i);
                if (!TextUtils.isEmpty(text)) {
                    strs.add(text);
                }
            }
        }
        return strs;
    }

    private void setRecommends(PBSRemixRecommendResponse response){
        recommends = new ArrayList<>();
        recommendMap = new HashMap<>();
        sbRecommendId = new StringBuilder();
        sbRecommendItemId = new StringBuilder();
        sbAppointedId = new StringBuilder();
        sbrecommendType = new StringBuilder();
        sbScenId = new StringBuilder();
        for (int i = 0; i < response.getRecommends().size(); i++) {
            Recommend recommend = response.getRecommends().get(i);
            if (TextUtils.isEmpty(recommend.getSceneType())) {
                SuperLog.info2SD(TAG, "智能推荐 SceneType()=null，无法判断智能推荐类型，当成无推荐数据处理");
                continue;
            }
            if ("0".equalsIgnoreCase(recommend.getSceneType())) {
                //内容
                if (null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                    for (int j = 0; j < recommend.getVODs().size(); j++) {
                        Recommend recommendTem = new Recommend();
                        recommendTem.setJtdataType(recommend.getJtdataType());
                        recommendTem.setDisplay_tracker(recommend.getDisplay_tracker());
                        recommendTem.setSceneId(recommend.getSceneId());
                        recommendTem.setAppointedId(recommend.getAppointedId());
                        recommendTem.setSceneType(recommend.getSceneType());
                        recommendTem.setIdentifyType(recommend.getIdentifyType());
                        List<VOD> vodList = new ArrayList<>();
                        vodList.add(recommend.getVODs().get(j));
                        recommendTem.setVODs(vodList);
                        //setRecomMap(recommendTem);
                        recommends.add(recommendTem);
                        //setRecommendId(recommend.getVODs().get(j).getID());
                    }
                }
            } else if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
                //处理H5/专题 页面
                if (null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                    for (int j = 0; j < recommend.getTopics().size(); j++) {
                        Recommend recommendTem = new Recommend();
                        recommendTem.setJtdataType(recommend.getJtdataType());
                        recommendTem.setDisplay_tracker(recommend.getDisplay_tracker());
                        recommendTem.setSceneId(recommend.getSceneId());
                        recommendTem.setAppointedId(recommend.getAppointedId());
                        recommendTem.setSceneType(recommend.getSceneType());
                        recommendTem.setIdentifyType(recommend.getIdentifyType());
                        List<Topic> topics = new ArrayList<>();
                        topics.add(recommend.getTopics().get(j));
                        recommendTem.setTopics(topics);
                        //setRecomMap(recommendTem);
                        recommends.add(recommendTem);
                        /*if (TopicActivity.TOPIC_H5.equalsIgnoreCase(recommend.getTopics().get(j).getType()) || TopicActivity.TOPIC_ACTIVITY.equalsIgnoreCase(recommend.getTopics().get(j).getType())){
                            setRecommendId(recommend.getTopics().get(j).getTopicURL());
                        }else if (recommend.getTopics().get(j).getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){
                            setRecommendId(recommend.getTopics().get(j).getId());
                        }*/
                    }
                }
            } else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                //apk
                if (null != recommend.getApks() && recommend.getApks().size() > 0) {
                    for (int j = 0; j < recommend.getApks().size(); j++) {
                        Recommend recommendTem = new Recommend();
                        recommendTem.setJtdataType(recommend.getJtdataType());
                        recommendTem.setDisplay_tracker(recommend.getDisplay_tracker());
                        recommendTem.setSceneId(recommend.getSceneId());
                        recommendTem.setAppointedId(recommend.getAppointedId());
                        recommendTem.setSceneType(recommend.getSceneType());
                        recommendTem.setIdentifyType(recommend.getIdentifyType());
                        List<Apk> apks = new ArrayList<>();
                        apks.add(recommend.getApks().get(j));
                        recommendTem.setApks(apks);
                        //setRecomMap(recommendTem);
                        recommends.add(recommendTem);
                        //setRecommendId(recommend.getApks().get(j).getId());
                    }
                }
            }
        }
    }

    private void setRecommendMap(){
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < mGroupElement.getElement().size(); i++) {
            Map<Integer, String> elementAppMap = getElementAppMap();
            if (null != elementAppMap && elementAppMap.containsKey(i)) {
                for (int j = 0;j < recommends.size();j++) {
                    if (recommends.get(j).getAppointedId().equalsIgnoreCase(elementAppMap.get(i))){
                        stringList.add(elementAppMap.get(i));
                        recommendMap.put(i, recommends.get(j));
                        break;
                    }
                }
            }
        }
        for (int j = 0;j < recommends.size();j++) {
            if (!stringList.contains(recommends.get(j).getAppointedId())){
                for (int i = 0;i < mGroupElement.getElement().size();i++){
                    if (!recommendMap.containsKey(i)){
                        recommendMap.put(i, recommends.get(j));
                        break;
                    }
                }
            }
        }

        for (Recommend recommend : recommendMap.values()){
            display_tracker = recommend.getDisplay_tracker();
            setAppointedId(recommend.getAppointedId());
            setRecommendType(getIdentifyType(recommend.getIdentifyType()));
            if ("0".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getVODs() && recommend.getVODs().size() > 0){
                setRecommendId(recommend.getVODs().get(0).getID());
                setRecommendItemId(recommend.getVODs().get(0).getItemid());
                setScenId(recommend.getSceneId());
            }else if (("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType()))
                    && null != recommend.getTopics() && recommend.getTopics().size() > 0){
                if (TopicActivity.TOPIC_H5.equalsIgnoreCase(recommend.getTopics().get(0).getType()) || TopicActivity.TOPIC_ACTIVITY.equalsIgnoreCase(recommend.getTopics().get(0).getType())){
                    setRecommendId(recommend.getTopics().get(0).getTopicURL());
                    setScenId(recommend.getSceneId());
                }else if (recommend.getTopics().get(0).getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){
                    setRecommendId(recommend.getTopics().get(0).getRelationSubjectId());
                    setScenId(recommend.getSceneId());
                }
            }else if ("3".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getApks() && recommend.getApks().size() > 0){
                setRecommendId(recommend.getApks().get(0).getId());
                setScenId(recommend.getSceneId());
            }
        }
    }

    private void loadRecommendData() {
        for (int j = 0;j < mGroupElement.getElement().size();j++) {
            if (recommendMap.containsKey(j)){
                Recommend recommend = recommendMap.get(j);
                if ("0".equalsIgnoreCase(recommend.getSceneType())) {
                    //内容
                    if (null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                        if (null != elementViews && j < elementViews.size()) {
                            loadVod(elementViews.get(j), j, recommend.getVODs().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                        }
                    }
                } else if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理H5/专题 页面
                    if (null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                        if (null != elementViews && j < elementViews.size()) {
                            loadH5AndTopic(elementViews.get(j), j, recommend.getTopics().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                        }
                    }
                }else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理apk,跳转内部界面和三方app
                    if (null != recommend.getApks() && recommend.getApks().size() > 0) {
                        if (null != elementViews && j < elementViews.size()) {
                            loadApk(elementViews.get(j), j, recommend.getApks().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                        }
                    }
                }
            }else if (null != elementViews && j < elementViews.size()) {
                loadDefaultData(elementViews.get(j), j);
            }
        }
    }

    private List<Recommend> recommends = new ArrayList<>();
    private Map<Integer,Recommend> recommendMap = new HashMap<>();
    //查询跑马灯
    private class PBSRemixRecommendListener implements EpgTopFunctionMenu.OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse response) {
            //查询成功 返回值notice及跳转连接
            /**
             * 推荐类型（sceneType）：0 内容，1 专题，2 活动，3 apk（含自有和第三方）
             * */

            if (null != response && null != response.getRecommends() && response.getRecommends().size() > 0) {

                setRecommends(response);
                setRecommendMap();
                loadRecommendData();

                //setDefaultElementData();
                recordMainRecommend(sbRecommendId.toString());
                recordMainRecommendJiuTian(sbRecommendItemId.toString());
                /* else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理三方app/页面内部  跳转
                    loadApk(response);
                }*/
            }else{
                setDefaultElementData();
            }
        }

        @Override
        public void getRemixRecommendDataFail() {
            setDefaultElementData();
        }
    }

    private void loadVod(ReflectRelativeLayout reflectRelativeLayout,int index,VOD vod,String sceneId,String identifyType,String appointedId) {
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != vod.getFeedback() && !TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                }
                mGroupElement.getElement().get(index).setNotice(vod.getNotice());
                mGroupElement.getElement().get(index).setRecommendType(identifyType);
                mGroupElement.getElement().get(index).setRecommendId(vod.getID());
                mGroupElement.getElement().get(index).setSceneId(sceneId);
                mGroupElement.getElement().get(index).setAppointedId(appointedId);
                reflectRelativeLayout.setDefaultData(false);
                reflectRelativeLayout.setElement(mGroupElement.getElement().get(index));
                reflectRelativeLayout.setIsRecommend(true, sceneId, identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), navIndex);
                reflectRelativeLayout.parseVOD(mAdapter, vod);
            }
        });
    }

    private void loadApk(ReflectRelativeLayout reflectRelativeLayout, int realIndex, Apk apk,String sceneId,String identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        Element element = mGroupElement.getElement().get(realIndex);

        element.setNotice(apk.getNotice());
        //设置海报
        element.getElementDataList().get(0).setContentURL(apk.getPosterURL());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(apk.getName());
        nameDialect.add(dialect);
        element.getElementDataList().get(0).setNameDialect(nameDialect);
        String actionURL = element.getElementDataList().get(0).getElementAction().getActionURL();
        //H5专题和活动推荐 url设置给actionUrl即可
        if (apk.getPkg().equalsIgnoreCase(EpgTopFunctionMenu.EPG_PKG)){
            //内部页面跳转
            StringBuffer sb = new StringBuffer();
            //sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");
            SuperLog.debug(TAG,"actionUrl_apk.getCls()="+apk.getCls());
            String actionUrl = getActionUrlForEpgInner(apk.getCls());
            SuperLog.debug(TAG,"actionUrl="+actionUrl);
            if (actionUrl.contains("ContentType=PAGE")){
                element.getElementDataList().get(0).getElementAction().setActionType(ZJVRoute.LauncherElementActionType.TYPE_2);
            }
            sb.append(actionUrl);
            element.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
        }else {
            //三方App跳转 AppPkg=com.syntc.mushroomjump&ContentType=APK&Version=6
            StringBuffer sb = new StringBuffer();
            sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");
            sb.append(ZJVRoute.LauncherElementContentType.APK);
            sb.append("&"+ZJVRoute.ActionUrlKeyType.APP_PKG+"=");
            sb.append(apk.getPkg());
            sb.append("&"+ZJVRoute.ActionUrlKeyType.APP_CLASS+"=");
            sb.append(apk.getCls());
            //set actionurl
            element.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
            List<com.pukka.ydepg.launcher.bean.node.NamedParameter> extras = apk.getExtras();
            if (null != extras && extras.size() > 0){
                Map extraData = element.getExtraData();
                if (null == extraData){
                    extraData = new HashMap();
                }
                for (com.pukka.ydepg.launcher.bean.node.NamedParameter namedParameter : extras){
                    if (!extraData.containsKey(namedParameter.getKey())){
                        extraData.put(namedParameter.getKey(),namedParameter.getFistItemFromValue());
                    }
                }
                element.setExtraData(extraData);
            }
        }

        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != apk.getFeedback() && !TextUtils.isEmpty(apk.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(apk.getFeedback().getClick_tracker());
                }
                element.setRecommendType(identifyType);
                element.setRecommendId(apk.getId());
                element.setSceneId(sceneId);
                element.setAppointedId(appointedId);
                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setIsRecommend(true,sceneId,identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), navIndex);
                reflectRelativeLayout.setElementData(element);
                element.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }

    public String getActionUrlForEpgInner(String cls) {
        if (cls.contains("ContentType=PAGE")){
            //ContentID=103051&ContentType=PAGE&Action=View
            String contentId = ZJVRoute.getContentValue(cls,ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            if (!TextUtils.isEmpty(contentId)){
                StringBuilder sb = new StringBuilder();
                sb.append(SharedPreferenceUtil.getInstance().getLauncherDeskTopIdForChild())
                        .append("_")
                        .append(contentId)
                        .append("@")
                        .append(SharedPreferenceUtil.getInstance().getLauncherVersionForChild());
                return cls.replaceAll(contentId,sb.toString());
            }else {
                return cls;
            }
        }else{
            return cls;
        }
    }

    private void loadH5AndTopic(ReflectRelativeLayout reflectRelativeLayout,int index,Topic topic,String sceneId,String identifyType,String appointedId) {
            //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
            Element element = mGroupElement.getElement().get(index);
            //设置海报
            element.getElementDataList().get(0).setContentURL(topic.getPosterUrl());
            element.setNotice(topic.getNotice());
            //设置标题Title
            List<Dialect> nameDialect = new ArrayList<>();
            Dialect dialect = new Dialect();
            dialect.setLanguage("zh");
            dialect.setValue(topic.getName());
            nameDialect.add(dialect);
            element.getElementDataList().get(0).setNameDialect(nameDialect);
            String actionURL = element.getElementDataList().get(0).getElementAction().getActionURL();
            //H5专题和活动推荐 url设置给actionUrl即可
            if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_H5) || topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_ACTIVITY)){
                element.getElementDataList().get(0).getElementAction().setActionURL(topic.getTopicURL());
            }else if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){//普通专题
                //native原生专题，用推荐接口请求回来的数据拼接actionUrl,set到Reflect中，后面走统一逻辑
                StringBuffer buffer = new StringBuffer();
                buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
                buffer.append("=");
                buffer.append(ZJVRoute.LauncherElementContentType.VOD_CATEGORY);
                buffer.append("&");
                buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_ID);
                buffer.append("=");
                buffer.append(topic.getRelationSubjectId());//本地专题列表存储是以RelationSubjectId为key
                element.getElementDataList().get(0).getElementAction().setActionURL(buffer.toString());
            }

            //set组织好的element
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != topic.getFeedback() && !TextUtils.isEmpty(topic.getFeedback().getClick_tracker())){
                        reflectRelativeLayout.setClickTrackerUrl(topic.getFeedback().getClick_tracker());
                    }
                    element.setRecommendType(identifyType);
                    element.setRecommendId(topic.getId());
                    element.setSceneId(sceneId);
                    element.setAppointedId(appointedId);
                    reflectRelativeLayout.setDefaultData(true);
                    reflectRelativeLayout.setIsRecommend(true,sceneId,identifyType);
                    reflectRelativeLayout.setAppointedId(appointedId);
                    reflectRelativeLayout.setGroup(mGroupElement.getGroup(), navIndex);
                    reflectRelativeLayout.setElementData(element);
                    element.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
                }
            });
    }

    private void loadDefaultData(ReflectRelativeLayout reflectRelativeLayout, int index) {
        Element element = mGroupElement.getElement().get(index);
        if (!CollectionUtil.isEmpty(element.getElementDataList())) {
            if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reflectRelativeLayout.setIsRecommend(false,null, RecommendData.RecommendType.HAND_TYPE);
                        reflectRelativeLayout.setElement(element);
                        reflectRelativeLayout.setGroup(mGroupElement.getGroup(),navIndex);
                        reflectRelativeLayout.setDefaultData(true);
                        reflectRelativeLayout.setElementData(element);
                    }
                });
            } else {
                if (null != vods && vods.size() > index) {
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reflectRelativeLayout.setDefaultData(false);
                            reflectRelativeLayout.setIsRecommend(false,null, RecommendData.RecommendType.HAND_TYPE);
                            reflectRelativeLayout.setElement(element);
                            reflectRelativeLayout.setGroup(mGroupElement.getGroup(),navIndex);
                            reflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(index));
                        }
                    });
                }
            }
        }
    }

    private void recordMainRecommend(String recommendId) {
        if (!TextUtils.isEmpty(recommendId)) {
            UBDRecommend.recordMainRecommend(recommendId, sbrecommendType.toString(), sbScenId.toString(),sbAppointedId.toString());
            PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.IMPRESSION,null,sbAppointedId.toString(),
                    sbrecommendType.toString(),sbScenId.toString(),recommendId,sbrecommendType.toString()));
        }
    }

    private void recordMainRecommendJiuTian(String itemId) {
        if (!TextUtils.isEmpty(itemId) && !TextUtils.isEmpty(display_tracker)){
            JiutianService.reportDisplay(display_tracker,itemId);
        }
    }

    private void setRecommendId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbRecommendId.toString())){
            sbRecommendId.append(id);
        }else{
            sbRecommendId.append(";");
            sbRecommendId.append(id);
        }
    }
    private void setRecommendItemId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbRecommendItemId.toString())){
            sbRecommendItemId.append(id);
        }else{
            sbRecommendItemId.append(";");
            sbRecommendItemId.append(id);
        }
    }
    private void setScenId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbScenId.toString())){
            sbScenId.append(id);
        }else{
            sbScenId.append(";");
            sbScenId.append(id);
        }
    }
    private void setAppointedId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbAppointedId.toString())){
            sbAppointedId.append(id);
        }else{
            sbAppointedId.append(";");
            sbAppointedId.append(id);
        }
    }
    private void setRecommendType(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbrecommendType.toString())){
            sbrecommendType.append(id);
        }else{
            sbrecommendType.append(";");
            sbrecommendType.append(id);
        }
    }

    public String getIdentifyType(Integer identifyType){
        if (null != identifyType){
            return String.valueOf(identifyType);
        }else{
            return " ";
        }
    }
}