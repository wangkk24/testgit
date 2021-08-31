package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ElementData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Launcher;
import com.pukka.ydepg.common.http.v6bean.v6node.ResStrategyData;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.playview.PlayViewMainEpg;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.common.viewpage2.ViewPage2Constant;
import com.pukka.ydepg.common.viewpage2.view.ViewPage2MainEpg;
import com.pukka.ydepg.customui.tv.PlayViewWindow;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.node.AppInfo;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.ui.template.DataLoaderAdapter;
import com.pukka.ydepg.launcher.ui.template.DataLoaderFactory;
import com.pukka.ydepg.launcher.ui.template.MixVideoTemplate;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.SwitchDialogUtil;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;
import com.pukka.ydepg.moudule.search.utils.ViewPage2MainEpgUtil;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 模板的element
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.ReflectRelativeLayout.java
 * @date: 2018-01-22 17:55
 * @version: V1.0 描述当前版本功能
 */
public class ReflectRelativeLayout extends RoundShadowLayout implements View.OnClickListener {
    private Context mContext;
    private static final String TAG = ReflectRelativeLayout.class.getSimpleName();

    private static final long MIN_CLICK_INTERVAL = 2500L;

    //九天推荐数据 上报所需的TrackerUrl
    private String mClickTrackerUrl;
    private String mActionUrl;
    private String mActionType;
    private ElementData mElementData;
    private Element mElement;
    private Group mGroup;
    private int mNavIndex = 0;
    private TextView mPosterTitleTv, mPosterContentTv;
    private ShimmerImageView mPosterImgView;
    private VerticalScrollTextView mTvNoticeVerScroll;
    private RelativeLayout mRelaForViewPage;
    private ViewPager2 mViewPager2;
    private LinearLayout mPointGroupLinear;
    private VOD vodData;

    private String recommendType = RecommendData.RecommendType.HAND_TYPE;
    private String appointedId = null;
    private boolean isRecommend = false;
    private String sceneId;

    private ImageView mLeftSuperScript;   //左侧角标
    private ImageView mRightSuperScript;  //右侧角标

    private ImageView mBgFocusEffect;  //焦点框

    private int mPosterType;
    private Map<String, String> extraData;
    private PHMFragment mFragment;
    private String mVodId;
    private View mLayer;
    private View mLinearLayout;
    private VOD mVod;
    public static final int BANNER = 0;
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    public static final int HORIZONTAL_WIDE = 3;
    public static final int HORIZONTAL_TWO = 4;//2.33

    //视频播放窗口
    private static final String VIDEO_WIN = "1";
    //普通窗口
    private static final String NORMAL_WIN = "0";

    /* 焦点的控件高度 */
    private int widgetHeight;
    /* 是否是自定义圆角类型 */
    private boolean mPosterRadioType = false;
    String contentURL;
    private boolean isDefaultData;

    //=true:加载和彩云数据，海报填充模式使用centerInside
    private boolean mIsCloudTvData = false;

    //每个资源位支持播放
    public static final boolean IS_PLAY_WINDOW_EVERY_REF = true;

    private PlayViewWindow mPlayView;
    private RelativeLayoutExt mRlPV;

    /**
     * 是否使用策略数据
     */
    private ResStrategyData mResStrategyData;

    private boolean canStartShimmer = false;

    private boolean needStartShimmer = false;

    private boolean isShimmerStarted = false;

    private final RRLCallBack callBack = new RRLCallBack();

    private ViewPage2MainEpg viewPage2MainEpg = null;

    private PlayViewMainEpg playViewMainEpg = null;
    private String playUrl;

    public void setPlayUrl(String playUrl){
        this.playUrl = playUrl;
    }
    public String getPlayUrl(){
        return playUrl;
    }

    public ReflectRelativeLayout(Context context) {
        super(context);
    }

    public ReflectRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ReflectRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.TVTemplate);//
            // 获取配置属性
            mPosterType = tArray.getInteger(R.styleable.TVTemplate_posterType, HORIZONTAL);
            mPosterRadioType = tArray.getBoolean(R.styleable.TVTemplate_posterRadioType, false);
            widgetHeight = tArray.getDimensionPixelSize(R.styleable.TVTemplate_posterSpecialHeight, 0);
            tArray.recycle();
        }
        this.mContext = context;
    }

    public void initView() {
        SuperLog.debug(TAG,"initView()");
        View view1 = getChildAt(0);
        if (mPosterRadioType) {
            View viewBackGround = getChildAt(1);
            viewBackGround.setBackground(getResources().getDrawable(R.drawable.phm_shadow_layer_scroll));
        }
        setDrawShape(true);
        if (view1 instanceof LinearLayout) {
            mPosterImgView = (ShimmerImageView) ((LinearLayout) view1).getChildAt(0);
            int[] location = new int[2];
            mPosterImgView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            Rect rect = new Rect(x, -y + 50, x + mPosterImgView.getMeasuredWidth(), mPosterImgView.getMeasuredHeight() + y - 50);
            mPosterImgView.setClipBounds(rect);
            SuperLog.info2SD(TAG, ">>>>>" + rect.left + "," + rect.top + "," + rect.right + "," + rect.bottom);
        } else {
            mPosterImgView = (ShimmerImageView) getChildAt(0); //每个element必然有一个imageView，而且是第一个
        }

        mLayer = getChildAt(1);
        mLinearLayout = getChildAt(2);
        if (mLinearLayout instanceof LinearLayout) {
            mPosterTitleTv = (TextView) ((LinearLayout) mLinearLayout).getChildAt(0);
            mPosterContentTv = (TextView) ((LinearLayout) mLinearLayout).getChildAt(1);

            if (getChildCount() > 2 && getChildAt(3) instanceof RelativeLayout){
                mRelaForViewPage = (RelativeLayout) getChildAt(3);
                if (mRelaForViewPage.getChildCount() > 0 && mRelaForViewPage.getChildAt(0) instanceof ViewPager2){
                    //资源位支持轮播.优先支持轮播展示，其次展示notice
                    mViewPager2 = (ViewPager2) mRelaForViewPage.getChildAt(0);
                    mPointGroupLinear = (LinearLayout) mRelaForViewPage.getChildAt(1);
                }
            }else if (getChildCount() > 3 && getChildAt(3) instanceof LinearLayout){
                //只能推荐notice处理
                LinearLayout childAt = (LinearLayout) getChildAt(3);
                if (childAt.getChildCount() > 0 && childAt.getChildAt(0) instanceof VerticalScrollTextView){
                    mTvNoticeVerScroll = (VerticalScrollTextView) childAt.getChildAt(0);
                }
            }

        } else if (getChildCount() > 2) {
            mPosterTitleTv = (TextView) getChildAt(2);
            if (getChildCount() >= 4) {
                mPosterContentTv = (TextView) getChildAt(2);
                if (getChildAt(3) instanceof TextView){
                    mPosterTitleTv = (TextView) getChildAt(3);
                }
            }
        }

        if (null != extraData && extraData.containsKey(Constant.RESOURCE_TYPE) && VIDEO_WIN.equalsIgnoreCase(extraData.get(Constant.RESOURCE_TYPE))) {
            //if (IS_PLAY_WINDOW_EVERY_REF && null == mRlPV && null != mPosterImgView && mPosterImgView.getId() == R.id.test_shimmer){
            if (IS_PLAY_WINDOW_EVERY_REF && null == mRlPV && isFreeLayoutTemplate()) {
                mRlPV = (RelativeLayoutExt) LayoutInflater.from(mContext).inflate(R.layout.play_view_for_window, null);
                mPlayView = mRlPV.findViewById(R.id.play_view_for_window);
                addView(mRlPV);
                mRlPV.setVisibility(GONE);
            }
        }

        if (mPosterTitleTv != null) {
            mPosterTitleTv.setHorizontallyScrolling(true);
        }
        if (mPosterContentTv != null) {
            mPosterContentTv.setHorizontallyScrolling(true);
        }
        if (null != mElement){
            setNoticeTv(mElement.getNotice());
            if (isDefaultData && null != mViewPager2
                    && (!TextUtils.isEmpty(ViewPage2MainEpgUtil.getElementExtra(mElement, ViewPage2Constant.CAROUSELID)) || !TextUtils.isEmpty(ViewPage2MainEpgUtil.getElementExtra(mElement, ViewPage2Constant.CLOUD_TV)))){
            //if (null != mViewPager2 && this.getId() == R.id.rl_topItem_container03_viewpage){
                isHideNotBelongToViewPage(true);
                //setEmptyContent();
                SuperLog.debug(TAG,"ViewPage2Present.initViewPage");
                if (viewPage2MainEpg == null){
                    SuperLog.debug(TAG,"new ViewPage2MainEpg()");
                    viewPage2MainEpg = new ViewPage2MainEpg();
                    viewPage2MainEpg.initViewPage(mViewPager2,mPointGroupLinear,mContext,this,mGroup,mElement);
                }
            }

            //处理视频播放控件
            if (null != mPlayView){
                SuperLog.debug(TAG,"new PlayViewMainEpg()");
                playViewMainEpg = new PlayViewMainEpg(mPlayView,mRlPV,mContext,this,mGroup,mElement,mFragment);
            }
        }

        addFocusEffect();
    }

    //是否是自由编排模板
    private boolean isFreeLayoutTemplate(){
        if (null == mGroup || null == mGroup.getControlInfo() || TextUtils.isEmpty(mGroup.getControlInfo().getControlId())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 异形海报功能
     * 使子布局突破父布局展示
     */
    private void addSpecialPosterView() {
        setClipChildren(false);
        View viewBackGround = getChildAt(1);
        viewBackGround.setBackground(getResources().getDrawable(R.drawable.phm_shadow_layer_special));
        removeViewAt(0);
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.BOTTOM);
        addView(linearLayout, 0);
        View view = getChildAt(0);
        if (view instanceof LinearLayout) {
            ShimmerImageView specialImageView = new ShimmerImageView(mContext);
            specialImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            specialImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    widgetHeight + ScreenUtil.getDimension(mContext, R.dimen.over_special_poster_view)));
            //specialImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ((LinearLayout) view).addView(specialImageView, 0);
            Log.d(TAG, "Special Poster Create Finish");
        }
    }

    public void addLeftCorner() {
        mLeftSuperScript = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_30));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_10),0,0,0);
        mLeftSuperScript.setLayoutParams(params);
        mLeftSuperScript.setScaleType(ImageView.ScaleType.FIT_START);
        addView(mLeftSuperScript);
    }

    public void addRightCorner() {
        if (null == mRightSuperScript){
            mRightSuperScript = new ImageView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mRightSuperScript.setLayoutParams(params);
            mRightSuperScript.setId(R.id.right_update_super_script);
            mRightSuperScript.setScaleType(ImageView.ScaleType.FIT_START);
            addView(mRightSuperScript);
        }
        mRightSuperScript.setImageResource(R.drawable.app_update_subscript);
    }

    public void addFocusEffect() {
        //常用功能键使用自己的焦点框，不走统一逻辑
        if (null == mBgFocusEffect && getId() != R.id.rr_item_my_function){
            mBgFocusEffect = new ImageView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mBgFocusEffect.setLayoutParams(params);
            mBgFocusEffect.setImageResource(R.drawable.select_ref_main);
            addView(mBgFocusEffect);
        }
    }

    public void setTextViewEffect(boolean hasFocus) {
        if (mPosterTitleTv != null) {
            if (!TextUtils.isEmpty(mPosterTitleTv.getText())) {
                mPosterTitleTv.setSelected(hasFocus);
                if (hasFocus) {
                    mPosterTitleTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                } else {
                    mPosterTitleTv.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
            if (null != mTvNoticeVerScroll && !TextUtils.isEmpty(mTvNoticeVerScroll.getText())){
                mTvNoticeVerScroll.setSelected(hasFocus);
                if (hasFocus) {
                    mTvNoticeVerScroll.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                } else {
                    mTvNoticeVerScroll.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        }

        if (mPosterContentTv != null) {
            if (!TextUtils.isEmpty(mPosterContentTv.getText())) {
                mPosterContentTv.setSelected(hasFocus);
                if (hasFocus) {
                    mPosterContentTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                } else {
                    mPosterContentTv.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        }
    }

    //是否开启异形海报
    private boolean isAlienType(){
        return null != extraData && !TextUtils.isEmpty(extraData.get("Is_Alien")) && !extraData.get("Is_Alien").equals("false");
    }

    public void setElementData(Element element) {
        if (null == element)return;
        this.mElement = element;
        this.extraData = element.getExtraData();
        if (null != element.getElementDataList() && element.getElementDataList().size() > 0){
            this.mElementData = element.getElementDataList().get(0);
        }

        /**
         * 扩展参数Is_Alien 为异形的时候
         * 才会显示异形海报
         * 目前共有六组模板支持显示异形海报
         * pannel_poster_13m、pannel_poster_13、pannel_poster_0_12_half、pannel_poster_0_222_13、pannel_poster_12、pannel_poster_0_11
         * */
        /*if (null == extraData || TextUtils.isEmpty(extraData.get("Is_Alien")) || extraData.get("Is_Alien").equals("false")){
            mPosterIsAlienType = false;
        }*/
        if (null == mPosterImgView){
            initView();
        }

        if (mIsCloudTvData && null != mPosterImgView){
            mPosterImgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPosterTitleTv != null) {
                    mPosterTitleTv.setText("");
                }
                if (mPosterContentTv != null) {
                    mPosterContentTv.setText("");
                    mPosterContentTv.setVisibility(GONE);
                }
            }
        });

        //使用本地数据
        //if (isDefaultData && null == viewPage2MainEpg) {
        if (isDefaultData) {
            vodData = null;
            //获取该资源位对应的资源位策略详情
            mResStrategyData = LauncherService.getInstance().getResourceDataById(element.getId());
            if (null != extraData) {
                //设置静态角标信息
                setSuperScript(SuperScriptUtil.getInstance().getSuperScriptByElement(extraData));

                //如果是剧集，设置静态资源位更新至第几集。。
                SuperScriptUtil.getInstance().setStaticEpisodeNum(extraData,mPosterContentTv);

            }
            loadLocal(mElementData, mPosterImgView, mPosterTitleTv);

            //如果是第三方应用资源位，判断配置版本好是否大于当前版本号，用来控制是否显示更新角标
            if (!TextUtils.isEmpty(ZJVRoute.getContentValue(mActionUrl, "ContentType")) &&
                    ZJVRoute.getContentValue(mActionUrl, "ContentType").equalsIgnoreCase(ZJVRoute.LauncherElementContentType.APK)) {
                List<AppInfo> appInfoList = MessageDataHolder.get().getAppInfoList();
                if (appInfoList.size() == 0){//本地未安装，显示更新角标
                    //显示应用更新角标
                    addRightCorner();
                }else if (!TextUtils.isEmpty(ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.VERSION))){
                    //PHM配置的版本号
                    String version = ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.VERSION);
                    for (AppInfo appInfo : appInfoList) {
                        //判断包名是否相同，再对比版本号
                        if (appInfo.getPackageName().equalsIgnoreCase(ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.APP_PKG))
                                && Integer.parseInt(version) > appInfo.getVersion()) {
                            //显示应用更新角标
                            addRightCorner();
                        }
                    }
                }
            }
        } else {
            //为了防止view复用，填充静态资源位时对于非静态资源为清空其内容
            setEmptyContent();
        }
    }

    //点击下载有新版的三方apk,移除“有更新”图标
    private void removeUpdateIconForApk(){
        //mRightSuperScript = findViewById(R.id.right_update_super_script);
        if (null != mRightSuperScript){
            List<AppInfo> appInfoList = MessageDataHolder.get().getAppInfoList();
            String version = ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.VERSION);
            if (appInfoList.size() > 0 && !TextUtils.isEmpty(version)){
                boolean isShowUpdateIcon = false;
                for (AppInfo appInfo : appInfoList) {
                    //判断包名是否相同，再对比版本号
                    if (appInfo.getPackageName().equalsIgnoreCase(ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.APP_PKG))
                            && Integer.parseInt(version) > appInfo.getVersion()) {
                        isShowUpdateIcon = true;
                    }
                }
                if (!isShowUpdateIcon){
                    mRightSuperScript.setVisibility(GONE);
                }
            }
        }
    }

    //isHide = true,隐藏原有海报标题等，显示轮播内容；=false,反之
    public void isHideNotBelongToViewPage(boolean isHide){
        OTTApplication.getContext().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLayer != null) {
                    mLayer.setVisibility(isHide ? GONE : VISIBLE);
                }
                if (mPosterImgView != null) {
                    mPosterImgView.setVisibility(isHide ? GONE : VISIBLE);
                }
                if (mLinearLayout != null) {
                    mLinearLayout.setVisibility(isHide ? GONE : VISIBLE);
                }
                if (null != mRelaForViewPage){
                    mRelaForViewPage.setVisibility(isHide ? VISIBLE : GONE);
                }
            }
        });
    }

    /**
     * 隐藏所有内容
     */
    public void setEmptyContent() {
        if (mLayer != null) {
            mLayer.setVisibility(VISIBLE);
        }
        if (null != mPosterImgView) {
            mPosterImgView.setVisibility(GONE);
        }
        if (null != mPosterTitleTv) {
            mPosterTitleTv.setText("");
        }
        if (null != mPosterContentTv) {
            mPosterContentTv.setText("");
            mPosterContentTv.setVisibility(GONE);
        }
        if (null != mLeftSuperScript) {
            mLeftSuperScript.setImageDrawable(null);
        }
        if (null != mRightSuperScript) {
            mRightSuperScript.setImageDrawable(null);
        }
    }

    //加载PBS接口返回的角标
    public void setSuperScript(String contentURL) {
        if (!TextUtils.isEmpty(contentURL) && !contentURL.equalsIgnoreCase("null")) {
            //contentURL = Constant.PBS_SCRIPT_URL + contentURL;
            if (null == mLeftSuperScript) {
                addLeftCorner();
            }
            Glide.with(mContext).load(contentURL).into(mLeftSuperScript);
        } else {
            if (null != mLeftSuperScript) {
                mLeftSuperScript.setVisibility(GONE);
            }
        }
    }

    public void setClickTrackerUrl(String clickTrackerUrl){
        mClickTrackerUrl = clickTrackerUrl;
    }

    public String getClickTrackerUrl(){
         return mClickTrackerUrl;
    }

    /**
     * 解析请求回来的Vod数据
     *
     * @param adapter 模板解析类
     * @param data    数据
     */
    public <T> void parseVOD(DataLoaderAdapter adapter, T data) {
        if (null == adapter){
            adapter = DataLoaderFactory.getInstance().getDataLoaderAdapter("3");
        }
        vodData = (VOD) data;
        if (null == mPosterImgView){
            initView();
        }

        mPosterImgView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (null == vodData) {
            loadDefaultPic(mPosterImgView);
            return;
        }
        //使用本地数据
        if (isDefaultData) {
            return;
        }
        if (null != mLayer) {
            mLayer.setVisibility(VISIBLE);
        }
        if (null != adapter){
            adapter.loadData(mPosterTitleTv, mPosterContentTv,mLayer, mPosterImgView, data,ReflectRelativeLayout.this);
        }
        //加载动态vod角标
        if (!CollectionUtil.isEmpty(((VOD) data).getCustomFields())) {
            setSuperScript(SuperScriptUtil.getInstance().getSuperScriptByVod((VOD) data, true));
        }
    }

    /**
     * 隐藏内部所有内容
     */
    public void hideAllView() {
        if (null != mLayer) {
            mLayer.setVisibility(GONE);
        }
        if (null != mPosterImgView) {
            mPosterImgView.setVisibility(GONE);
        }
        if (null != mPosterContentTv) {
            mPosterContentTv.setVisibility(GONE);
        }
        if (null != mPosterTitleTv) {
            mPosterTitleTv.setVisibility(GONE);
        }
        if (null != mLeftSuperScript) {
            mLeftSuperScript.setVisibility(GONE);
        }
        if (null != mRightSuperScript) {
            mRightSuperScript.setVisibility(GONE);
        }
    }

    public String getTitleText(){
        return null != mPosterTitleTv ? mPosterTitleTv.getText().toString() : "";
    }

    public ImageView getBgFocusEffect(){
        return mBgFocusEffect;
    }

    /**
     * 加载本地数据
     *
     * @param elementData
     * @param imageView
     * @param textView
     */
    private void loadLocal(ElementData elementData, ImageView imageView, TextView textView) {
        //imageView.setVisibility(VISIBLE);
        contentURL = "";
        if (null != elementData) {
             contentURL = elementData.getContentURL();
             mActionUrl = elementData.getElementAction().getActionURL();
             mActionType = elementData.getElementAction().getActionType();
            //加载资源位策略，如果存在覆盖json中配置的资源位内容
            loadStrategyData();
            if (null != textView) {
                setTextViewText(textView,"");
            }
            loadLocalPoster(imageView, contentURL);
            //静态资源位显示名称时展示蒙版----解决配置异形图片时展示蒙版、白色背景有名称时文字看不清的问题
            if (null != mLayer) {
                //mLayer.setVisibility(GONE);
                setViewVisible(mLayer,View.GONE);
            }
            List<Dialect> nameList = elementData.getNameDialect();
            String name = null;
            if (null != nameList && nameList.size() != 0) {
                for (Dialect s : nameList) {
                    if (null != s && !TextUtils.isEmpty(s.getLanguage())) {
                        if (s.getLanguage().equals("zh")) {
                            name = s.getValue();
                            break;
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(name) && null != textView) {
                if (!"noname".equalsIgnoreCase(name)) {
                    //textView.setText(name);
                    setTextViewText(textView,name);
                    if (mLayer != null) {
                        setViewVisible(mLayer,View.VISIBLE);
                    }
                }
            }
        }
        setOnClickListener(this);
        String contentId = ZJVRoute.getContentValue(mActionUrl, "ContentID");
        String contentType = ZJVRoute.getContentValue(mActionUrl, "ContentType");
        // 加载资源位时提前下载二级桌面launcher,加快二级桌面加载速度
        if (!isRecommend && !TextUtils.isEmpty(contentId) && ZJVRoute.LauncherElementActionType.TYPE_2.equalsIgnoreCase(mActionType) && ZJVRoute
                .LauncherElementContentType.PAGE.equalsIgnoreCase(contentType)) {
            downloadLauncher(contentId);
        }
    }

    private void setViewVisible(View view,int visible) {
        view.setVisibility(visible);
    }

    private void setTextViewText(TextView textView,String string) {
        textView.setText(string);
    }

    /**
     * 加载配置的资源位策略
     */
    private void loadStrategyData() {
        if (null != mResStrategyData) {
            if (!TextUtils.isEmpty(mResStrategyData.getContentURL())) {
                contentURL = mResStrategyData.getContentURL();
            }
            if (!TextUtils.isEmpty(mResStrategyData.getActionURL())) {
                mActionUrl = mResStrategyData.getActionURL();
            }
            mActionType = String.valueOf(mResStrategyData.getActionType());
        }
    }

    /**
     * @param imageView
     * @param contentURL json中配置的图片url，需要拼接其他内容
     */
    private void loadLocalPoster(ImageView imageView, String contentURL) {
        if (!TextUtils.isEmpty(contentURL)) {
            if (contentURL.contains("/") || contentURL.contains("http")) {
                try {
                    if (isRecommend){
                        if (!contentURL.contains("http")){
                            EpgToast.showLongToast(mContext,"isRecommend = true,contentURL=\"+contentURL");
                            SuperLog.info2SDDebug(TAG,"isRecommend = true,contentURL="+contentURL);
                        }else{
                            loadPosterUrl(contentURL, imageView);
                        }
                    }else{
                        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
                        if (!TextUtils.isEmpty(launcherLink)) {
                            contentURL = "http://" + AuthenticateManager.getInstance().getUserInfo()
                                    .getIP() + ":" + AuthenticateManager.getInstance().getUserInfo()
                                    .getPort() + launcherLink + GlideUtil.getUrl(contentURL);
                            //SuperLog.debug(TAG, "launcherLink:" + contentURL);
                            loadPosterUrl(contentURL, imageView);
                        } else {
                            EpgToast.showLongToast(mContext,"Fatal error,launcherLink is empty");
                            SuperLog.error(TAG,"Fatal error,launcherLink is empty.");
                        }
                    }
                } catch (Exception ex) {
                    loadDefaultPic(imageView);
                    SuperLog.error(TAG,ex);
                }
            } else {
                loadDefaultPic(imageView);
            }
        } else {
            loadDefaultPic(imageView);
        }
    }

    private void downloadLauncher(String contentId) {
        if (!TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) && LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("1")){
            List<String> desktopIDs = new ArrayList<>();
            desktopIDs.add(contentId);
            LauncherService.getInstance().getChildLauncherLink(mContext, desktopIDs, new TabItemPresenter.OnQueryLauncherListener() {
                @Override
                public void onVersionChange(boolean change, String launcherName) {
                    if (change){
                        downloadChildLauncher(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()+launcherName,contentId);
                    }
                }
            });
        }else if (TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) || LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("0")){
            String launcherLink = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() +
                    SharedPreferenceUtil.getInstance().getLauncherLink() + "/pages/" + contentId + ""
                    + ".json";
            downloadChildLauncher(launcherLink,contentId);
        }
    }

    private void downloadChildLauncher(String launcherLink,String contentId){
        if (TextUtils.isEmpty(launcherLink) || launcherLink.contains("null")){
            return;
        }
        String SAVED_FILE = OTTApplication.getCachePath();
        if (contentId.toLowerCase().endsWith(".json")) {
            SAVED_FILE = SAVED_FILE + contentId;
        } else {
            SAVED_FILE = SAVED_FILE + contentId + ".json";
        }
        SuperLog.debug(TAG, "begin download child launcher in ReflectRelativeLayout ,Url : " +
                launcherLink);
        String finalSAVED_FILE = SAVED_FILE;
        File file = new File(finalSAVED_FILE);
        if (file.exists()) {
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(launcherLink).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SuperLog.debug(TAG, "Early download child launcher fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String content = response.body().string();
                    if (!file.exists()) {
                        FileUtil.fileCreate(file);
                    }
                    FileUtil.saveContentToFile(finalSAVED_FILE, content);
                    Launcher launcher = JsonParse.json2Object(content, Launcher.class);
                    LauncherService.getInstance().addChildLauncher(contentId, launcher);
                    SuperLog.debug(TAG, "download child launcher Success in ReflectRelativeLayout");
                }
            }
        });
    }

    public void loadPosterUrl(String imageUrl, ImageView imageView) {
        if(!TextUtils.isEmpty(imageUrl) && isAlienType()){
            addSpecialPosterView();
            View view = getChildAt(0);
            mPosterImgView = (ShimmerImageView) ((LinearLayout) view).getChildAt(0);
            setDrawShape(false);
        }

        RequestOptions options = new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.default_poster_bg));
        ImageView showView = imageView;
        if ((!TextUtils.isEmpty(imageUrl) && isAlienType()) || mPosterRadioType) {
            //异形海报
            //options = options.transform(new CornersTransform(ScreenUtil.getDimensionF(mContext, R.dimen.my_moreItem_radius)));
            showView = mPosterImgView;
        }
        if (!TextUtils.isEmpty(imageUrl) && !imageUrl.equalsIgnoreCase("null")){
            tmpGlide(imageUrl,options,showView);
        }
    }

    public void loadDefaultPic(ImageView imageView) {
        RequestOptions options  = new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.default_poster_bg));
        if (mPosterRadioType) {
            //options = options.transform(new CornersTransform(ScreenUtil.getDimensionF(mContext, R.dimen.my_moreItem_radius)));
        }
        tmpGlide("",options,imageView);
    }

    private void tmpGlide(String url,RequestOptions options,ImageView imageView){
        if (imageView instanceof ShimmerImageView){
            ((ShimmerImageView)imageView).setImageResourcesUrl(url,false);
            ((ShimmerImageView)imageView).setIsRecommend(isRecommend);
        }
        GlideUtil.load(mContext, url,options,callBack,imageView);
    }

    private class RRLCallBack implements GlideUtil.GlideCallBack{

        @Override
        public void onLoadFailed(GlideException e,Object model) {
            SuperLog.debug(TAG,"Load resource failed, URL="+model);
        }

        @Override
        public void onResourceReady() {
            canStartShimmer = true;
            if(needStartShimmer){
                startShimmer();
            }
        }
    }
    /**
     * 元素点击事件，只处理本地数据点击事件，如果是网络返回的数据交由loaderAdapter处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        SuperLog.debug(TAG,"onClick isDefaultData="+isDefaultData+";mActionUrl="+mActionUrl);
        if (isDefaultData && !TextUtils.isEmpty(mActionUrl)) {

            if (isRecommend && !TextUtils.isEmpty(mClickTrackerUrl)){
                mActionUrl = mActionUrl+"&"+ NewVodDetailActivity.JIUTIAN_TRACKER_URL+"="+mClickTrackerUrl;
            }

            //如果点击应用，隐藏右上角更新角标
            if (!TextUtils.isEmpty(ZJVRoute.getContentValue(mActionUrl, "ContentType"))
                    &&ZJVRoute.getContentValue(mActionUrl, "ContentType").equalsIgnoreCase(ZJVRoute.LauncherElementContentType.APK)){
                //mRightSuperScript = v.findViewById(R.id.right_update_super_script);
                if (null != mRightSuperScript){
                    mRightSuperScript.setVisibility(GONE);
                }
            }

            if (null != playViewMainEpg){
                playViewMainEpg.onClick();
            }else if (MessageDataHolder.get().getIsMixVideoPostImage()) {
                if (v.getParent().getParent() instanceof MixVideoTemplate) {
                    ((MixVideoTemplate) (v.getParent().getParent())).onToPlayFullScreenClick();
                }
            }else if (ZJVRoute.getContentValue(mActionUrl, "ContentType").equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_TO_PARENT)){
                ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);
                if (!ClickUtil.isFastDoubleClick(TAG)){
                    SwitchDialogUtil switchDialogUtil = new SwitchDialogUtil(mContext);
                    switchDialogUtil.setTypeForToActivity(ZJVRoute.getContentValue(mActionUrl, "ContentType"));
                    switchDialogUtil.setIntentData(ZJVRoute.LauncherElementDataType.STATIC_ITEM, mActionUrl, mActionType, mVodId, mVod, extraData);
                    switchDialogUtil.queryUserAttrs();
                }
            }else{
                ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.STATIC_ITEM, mActionUrl, mActionType, mVodId, mVod, extraData);
            }

            UBDSwitch.getInstance().getMainExtensionField().setSceneId(sceneId);
            UBDSwitch.getInstance().getMainExtensionField().setRecommendType(recommendType);
            UBDSwitch.getInstance().getMainExtensionField().setAppointedId(appointedId);

            //静态资源位点击事件上报UBD
            UBDSwitch.getInstance().recordMainOnclick(mActionUrl,mElement,mGroup,null,null);

            if (isRecommend){
                if (!TextUtils.isEmpty(mClickTrackerUrl)){
                    JiutianService.reportClick(mClickTrackerUrl);
                }
                PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.CLICK,mElement.getRecommendId(),mElement.getAppointedId(),
                        mElement.getRecommendType(),mElement.getSceneId(),mElement.getRecommendId(),mElement.getRecommendType()));
            }

            //资源位跳转到导航栏，UBD上报
            String contentType = ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
            String contentId = ZJVRoute.getContentValue(mActionUrl, ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            if (contentType.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.NAVIGATE) && !TextUtils.isEmpty(contentId)){
                UBDSwitch.getInstance().setFromActivity(MainActivity.class.getSimpleName());
                UBDSwitch.getInstance().reportInBaseActivity(MainActivity.class.getSimpleName()+"_"+contentId);
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (null != mBgFocusEffect){
            mBgFocusEffect.setSelected(gainFocus);
        }

        if (gainFocus) {
            bringToFront();
            if (null != playViewMainEpg) {
                playViewMainEpg.startPlay();
            }
        } else {
            if (null != playViewMainEpg) {
                playViewMainEpg.stopPlay(true);
            }
        }
    }

    public void startShimmer(){
        //扩展参数“Is_Shimmer"为True时，才展示光效，默认不展示光效
        if (null == extraData || TextUtils.isEmpty(extraData.get("Is_Shimmer")) || !extraData.get("Is_Shimmer").equals("true"))
            return;

        if(canStartShimmer && !isShimmerStarted) {
            if (null != mPosterImgView){
                mPosterImgView.startShimmer();
            }
            needStartShimmer = false;
            isShimmerStarted = true;
        } else{
            needStartShimmer = true;
            isShimmerStarted = false;
        }
    }

    public void stopShimmer(){
        if (null != mPosterImgView){
            mPosterImgView.stopShimmer();
        }
        isShimmerStarted = false;
        needStartShimmer = false;
    }

    public String getActionUrl(){
        return mActionUrl;
    }

    public void setElement(Element element){
        this.mElement = element;
    }

    public Element getElement(){
        return mElement;
    }
    public void setGroup(Group group){
        this.mGroup = group;
        addFocusEffect();
    }

    public void setGroup(Group group,int navIndex){
        this.mGroup = group;
        this.mNavIndex = navIndex;
        addFocusEffect();
    }

    public Group getGroup(){
        return mGroup;
    }

    public void setDefaultData(boolean defaultData) {
        isDefaultData = defaultData;
    }

    public boolean isDefaultData() {
        return isDefaultData;
    }

    public void setIsCloudTvData(boolean isCloudTvData) {
        mIsCloudTvData = isCloudTvData;
    }

    public boolean isVideoWinDow() {
        return null != playViewMainEpg;
    }

    private void setNoticeTv(String notice){
        if (null != mTvNoticeVerScroll){
            if (!TextUtils.isEmpty(notice)){
                mTvNoticeVerScroll.setText(notice);
            }
        }
    }


    public void setFragment(PHMFragment fragment) {
        this.mFragment = fragment;
    }
    public PHMFragment getFragment() {
        return mFragment;
    }

    public void setIsRecommend(boolean isRecommend,String sceneId,String recommendType){
        this.isRecommend = isRecommend;
        this.recommendType = recommendType;
        this.appointedId = appointedId;
        this.sceneId = sceneId;
    }

    public boolean getIsRecommend(){
        return isRecommend;
    }
    public String getRecommendTyp(){
        return recommendType;
    }
    public void setAppointedId(String appointedId){
        this.appointedId = appointedId;
    }
    public String getAppointedId(){
        return appointedId;
    }
    public String getSceneId(){
        return sceneId;
    }


    public int getPosterType() {
        return mPosterType;
    }

    public int getNavIndex(){
        return mNavIndex;
    }

    public boolean getPosterIsAlienType() {
        return isAlienType();
    }

    public Map<String, String> getExtraData() {
        return extraData;
    }
    public String getActionType() {
        return mActionType;
    }

    //绑定的VOD对象
    public VOD getVodData() {
        return vodData;
    }

    //绑定的Element对象
    public ElementData getElementData() {
        return mElementData;
    }

    public TextView getPosterTitleTv(){
        return mPosterTitleTv;
    }

    public ShimmerImageView getPosterImgView(){
        return mPosterImgView;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            onResume();
        }
        else if(visibility == INVISIBLE || visibility == GONE){
            onPause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
    }

    public void onResume(){
        if (null != viewPage2MainEpg) {
            SuperLog.debug(TAG, "viewPage2---start()");
            viewPage2MainEpg.start();
        }

        if (null != playViewMainEpg){
            SuperLog.debug(TAG,"playViewMainEpg---onResume()");
            playViewMainEpg.onResume();
        }

        removeUpdateIconForApk();
    }
    public void onPause(){
        if (null != viewPage2MainEpg){
            SuperLog.debug(TAG,"viewPage2---onStop()");
            viewPage2MainEpg.onStop();
        }

        if (null != playViewMainEpg){
            SuperLog.debug(TAG,"playViewMainEpg---onPause()");
            playViewMainEpg.onPause();
        }
    }

    public void onClickForViewPage(String sequence) {
        if (isDefaultData){
            if (isRecommend && !TextUtils.isEmpty(mClickTrackerUrl)){
                mActionUrl = mActionUrl+"&"+ NewVodDetailActivity.JIUTIAN_TRACKER_URL+"="+mClickTrackerUrl;
            }
            SuperLog.debug("eason","isDefaultData="+isDefaultData+";mActionUrl="+mActionUrl);
        }else{
            SuperLog.debug("eason","isDefaultData="+isDefaultData+";mActionUrl="+mActionUrl+";VodName="+vodData.getName()+"Notice="+vodData.getNotice());
        }
        if (isDefaultData && !TextUtils.isEmpty(mActionUrl)) {
            ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.STATIC_ITEM,mActionUrl,mActionType, null, null,extraData);

            UBDSwitch.getInstance().getMainExtensionField().setSceneId(sceneId);
            UBDSwitch.getInstance().getMainExtensionField().setRecommendType(recommendType);
            //静态资源位点击事件上报UBD
            UBDSwitch.getInstance().recordMainOnclick(mActionUrl,mElement,mGroup,null,sequence);

            if (isRecommend){
                if (!TextUtils.isEmpty(mClickTrackerUrl)){
                    JiutianService.reportClick(mClickTrackerUrl);
                }
                PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.CLICK,mElement.getRecommendId(),mElement.getAppointedId(),
                        mElement.getRecommendType(),mElement.getSceneId(),mElement.getRecommendId(),mElement.getRecommendType()));
            }
        } else if (null != vodData) {
            ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.VOD, null, null,vodData.getID(),vodData, null);

            UBDSwitch.getInstance().getMainExtensionField().setSceneId(sceneId);
            UBDSwitch.getInstance().getMainExtensionField().setRecommendType(recommendType);
            //静态资源位点击事件上报UBD
            UBDSwitch.getInstance().recordMainOnclick(mActionUrl,mElement,mGroup,vodData,sequence);

            if (isRecommend){
                if (!TextUtils.isEmpty(mClickTrackerUrl)){
                    JiutianService.reportClick(mClickTrackerUrl);
                }
                PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.CLICK,mElement.getRecommendId(),mElement.getAppointedId(),
                        mElement.getRecommendType(),mElement.getSceneId(),mElement.getRecommendId(),mElement.getRecommendType()));
            }
        }
    }
}