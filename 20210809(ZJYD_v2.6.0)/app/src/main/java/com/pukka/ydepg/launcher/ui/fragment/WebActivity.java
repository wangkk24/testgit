package com.pukka.ydepg.launcher.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.profile.ProfileManager;
import com.pukka.ydepg.common.profile.adapter.ConvertDataFromPbsToEpg;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.event.FinishPlayUrlEvent;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewUniPayFragment;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.NewDetailPresenter;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;
import com.pukka.ydepg.moudule.vod.view.NewDetailDataView;
import com.pukka.ydepg.view.PlayView;
import com.pukka.ydepg.view.loadingball.MonIndicator;
import com.pukka.ydepg.xmpp.XmppService;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.pukka.ydepg.common.profile.ProfileManager.PROFILE_RESULT_CODE;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_MANAGE;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_MODIFY;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_SELECT;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_STARTUP;

public class WebActivity extends BaseActivity implements IPlayListener, NewDetailDataView {
    private static final String TAG = WebActivity.class.getSimpleName();
    //自定义loading控件
    private MonIndicator mMonIndicator;
    //播放窗口的父布局
    private RelativeLayout mPlayViewParent;
    private RelativeLayout mPlayViewParentMatch;

    //判断是否是首次加载界面
    private boolean mIsFirstBoot = true;
    //webView布局
    private WebView mWebview;
    //播放器
    private PlayView mPlayView;
    //播放器的X轴坐标
    private int mLocationX = 0;
    //播放器Y轴的坐标
    private int mLocationY = 0;
    //播放器的宽
    private int mWidth = 0;
    //播放器的高
    private int mHeight = 0;
    //播放地址
    private String mPlayUrl;
    //是否已经创建playView布局
    private boolean mIsCreate = false;
    //播放书签
    private long mBookmark = 0;
    //添加请求头
    private Map<String, String> extraHeaders;
    //详情服务器请求适配器
//    private DetailPresenter mDetailPresenter;
    private NewDetailPresenter mDetailPresenter;
    private boolean mIsMigu = true;
    private int mTryCount = 0;
    private boolean isMatchParent = false;
    private boolean isEnable;
    private TextView mTvIcon;
    //广告图片
    private ImageViewExt mAdIcon;

    private String onBackListener;//H5页面处理返回键方法名

    public boolean mCirculate;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int messageId = msg.what;
            switch (messageId) {
                case 1:
                    createVideoElement(mLocationX, mLocationY, mWidth, mHeight);
                    break;
                case 2:
                    createVideoElement(mLocationX, mLocationY, mWidth, mHeight);
//                    setViewSize("kkkkk",468 ,191, 760 ,453,5,20,R.color.white_0, R.color.c34_color);
                    if (!TextUtils.isEmpty(mPlayUrl)) {
                        if (mPlayView != null) {
                            mPlayView.releasePlayer();
                            if (mBookmark > 0) {
                                mPlayView.startPlay(mPlayUrl, mBookmark);
                            } else {
                                mPlayView.startPlay(mPlayUrl);
                            }
                        }
                    }
                    break;
                case 3:
                    mTryCount = 0;
                    if (!TextUtils.isEmpty(mPlayUrl)) {
                        if (mPlayViewParent.getVisibility() == View.GONE) {
                            mPlayViewParent.setVisibility(View.VISIBLE);
                        }
                        if (mPlayView != null) {
                            mPlayView.releasePlayer();
                            if (mBookmark > 0) {
                                mPlayView.startPlay(mPlayUrl, mBookmark);
                            } else {
                                mPlayView.startPlay(mPlayUrl);
                            }
                        }

                    }
                    break;
                case 4:
                    if (mPlayView != null) {
                        if (mIsCreate && mPlayView.getVisibility() == View.VISIBLE) {
                            mPlayView.releasePlayer();
                            mPlayViewParent.setVisibility(View.GONE);
                            SuperLog.error(TAG, "hide playViews");
                        }
                    }
                    break;
                case 5:
                    WebActivity.this.finish();
                    break;
            }
        }
    };

    //用于避免播放窗口跳转H5出现残影
    public String loadUrl = "";
//    public boolean isFirst = true;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "WebActivity onResume: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

//        mDetailPresenter = new DetailPresenter(this);
        mDetailPresenter = new NewDetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);

        mPlayView = new PlayView(this);
        mPlayView.setShouldAutoPlay(true);
        mPlayView.setOnPlayCallback(this);
        mPlayView.setControllViewState(View.GONE, false);
        extraHeaders = new HashMap<String, String>();
        mTvIcon = findViewById(R.id.tv_icon);
        mAdIcon = findViewById(R.id.iv_ad_icon);
        mPlayViewParent = (RelativeLayout) findViewById(R.id.playview_parent);
        mPlayViewParentMatch = (RelativeLayout) findViewById(R.id.playview_parent_match);
        mWebview = (WebView) findViewById(R.id.webview);
        mMonIndicator = (MonIndicator) findViewById(R.id.monIndicator);
        mWebview.setBackgroundColor(0);
        mWebview.setBackgroundResource(R.drawable.default_detail_bg);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        this.loadUrl = url;
        WebSettings webSettings = mWebview.getSettings();
        mWebview.setWebChromeClient(new WebChromeClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowContentAccess(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setGeolocationEnabled(false);
        webSettings.setDomStorageEnabled(true);

        //安全整改要求 之前为true,改为false,如果功能异常则改回来
        webSettings.setAllowFileAccess(false);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebview.addJavascriptInterface(new javascriptCallback(this), "playparam");
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: loadurl "+url);
//                if (!loadUrl.equals(url) && !isFirst){
//                    mWebview.setBackgroundColor(getResources().getColor(R.color.lb_view_dim_mask_color));
//                }
//                isFirst = false;
                view.loadUrl(url, extraHeaders);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mIsFirstBoot) {
                    mMonIndicator.setVisibility(View.VISIBLE);
                    mIsFirstBoot = false;
                } else {
                    if (mMonIndicator.getVisibility() == View.VISIBLE) {
                        mMonIndicator.setVisibility(View.GONE);
//                        mWebview.setBackgroundResource(0);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mWebview.setVisibility(View.VISIBLE);
                if (mMonIndicator.getVisibility() == View.VISIBLE) {
                    mMonIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                SuperLog.error(TAG, "onReceivedSslError=" + error);
                handler.cancel();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);//以后如果发现有问题,删除这句代码
                SuperLog.error(TAG, "onReceivedError=" + error);
            }
        });

        if (!TextUtils.isEmpty(url)) {
            SuperLog.info2SD(TAG, "H5 page URL = " + url);
            if (url.startsWith("http://aikanvod.miguvideo.com")
                    || url.startsWith("http://guaikanvod.miguvideo.com")) { //guaikanvod为N5华为测试环境PBS服务器域名
                SuperLog.info2SD(TAG, "This is a PBS url.\tURL=" + url);
                extraHeaders.put("epgSession", getCookie());//(前面是key，后面是value)
                extraHeaders.put("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL());
                mWebview.loadUrl(url, extraHeaders);
                if (url.equals(URL_PROFILE_MANAGE) || url.equals(URL_PROFILE_SELECT) || url.equals(URL_PROFILE_MODIFY) || url.equals(URL_PROFILE_STARTUP)) {
                    setResult(PROFILE_RESULT_CODE);
                } else if (url.startsWith(NewUniPayFragment.giftUrl)) {
                    setResult(RESULT_OK);
                }
            } else if (url.startsWith("https://app.m.zj.chinamobile.com") //生产1
                    || url.startsWith("https://tv.zj.chinamobile.com")    //生产2
                    || url.startsWith("http://app-hd.zj.chinamobile.com") //灰度
                    || url.startsWith("http://218.205.68.77:8080/")       //非上线准发布
                    || url.startsWith("http://20.26.38.204:8888")         //非上线准发布
                    || url.startsWith("http://20.26.20.81:8183")) {       //20200224手机服务端朱晓明要求增加
                SuperLog.info2SD(TAG, "This is a tv-business-hall url.\tURL=" + url);
                String location = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL();
                String userToken = SessionService.getInstance().getSession().getUserToken();
                extraHeaders.put("Location", location);
                extraHeaders.put("userToken", userToken);

                //20200224手机服务端朱晓明要求将认证信息放入Url中以提升页面加载速度
                if (url.contains("?")) {
                    //针对URL中携带?的情况，如https://app.m.zj.chinamobile.com/zjtv-business-hall/tvlogin?menuId=BroadbandRenewal
                    url = url + "&Location=" + location + "&userToken=" + userToken;
                } else {
                    //针对URL中不带?的情况，如https://app.m.zj.chinamobile.com/zjtv-business-hall/tvlogin
                    url = url + "?Location=" + location + "&userToken=" + userToken;
                }
                SuperLog.debug(TAG, "New tv-business-hall url = " + url);
                mWebview.loadUrl(url, extraHeaders);
            } else {
                SuperLog.info2SD(TAG, "This is a common url.");
                mWebview.loadUrl(url);
            }
            //缓存PageName,用于UBD上报
            UBDPurchase.cachePageName(url);
        } else {
            SuperLog.error(TAG, "URL is null.");
        }
    }

    private String getCookie() {
        String jsessionid = "";
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getSeesionId())) {
            jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();

            if (!jsessionid.contains("JSESSIONID=")) {
                jsessionid = "JSESSIONID=" + jsessionid;
            }

            String csrfsession = SessionService.getInstance().getSession().getCookie();

            if (!TextUtils.isEmpty(csrfsession)) {
                jsessionid = jsessionid + "; " + csrfsession;
            }

            String xCSRFToken = SessionService.getInstance().getSession().getCSRFToken();

            if (!TextUtils.isEmpty(xCSRFToken)) {
                jsessionid = jsessionid + "; " + xCSRFToken;
            }
        }
        return jsessionid;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SuperLog.debug(TAG, "onBackPressed->isMatchParent :" + isMatchParent);
            if (!TextUtils.isEmpty(onBackListener)) {
                SuperLog.info2SD(TAG, "H5 will handle back key instead of Android system.");
                StringBuilder jsFunction = new StringBuilder("javascript:").append(onBackListener).append("()");
                mWebview.loadUrl(jsFunction.toString());
                return true;
            }

            if (isMatchParent) {
                //返回小屏播放
                mPlayViewParentMatch.removeAllViews();
                addPlayView(mPlayViewParent);
                mPlayViewParentMatch.setVisibility(View.GONE);
                isMatchParent = false;
                isEnable = false;
                return true;
            }
            if (mWebview.canGoBack()) {
                mWebview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPlayState(int playbackState) {
    }

    @Override
    public void onPrepared(int videoType) {
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
    }

    @Override
    public void onPlayCompleted() {
        //播放完成的回调
        SuperLog.info2SD(TAG, "ncirculate =" + mCirculate);
        if (mCirculate) {
            mPlayView.releasePlayer();
            mPlayView.startPlay(mPlayUrl);
        }
    }

    @Override
    public void onDetached(long time) {
    }

    @Override
    public void onAttached() {
    }

    @Override
    public void onTryPlayForH5() {
        if (null != mPlayView && mTryCount < 3 && !TextUtils.isEmpty(mPlayUrl)) {
            SuperLog.error(TAG, "mPlayView=" + mPlayView + ",mTryCount=" + mTryCount + ",mPlayUrl=" + mPlayUrl);
            mPlayView.releasePlayer();
            mPlayView.startPlay(mPlayUrl);
            mTryCount++;
        } else {
            SuperLog.error(TAG, "mTryCount=" + mTryCount);
            mTryCount = 0;
        }
    }

    @Override
    public void onAdVideoEnd() {

    }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        if (mIsMigu) {
            MiguQRViewPopWindow miguQRViewPopWindow = new MiguQRViewPopWindow(WebActivity.this, vodDetail.getCode(), MiguQRViewPopWindow.mSearchResultType);
            miguQRViewPopWindow.showPopupWindow(WebActivity.this.mWebview);
        } else {
            mDetailPresenter.setButtonOrderOrSee(true);
            mDetailPresenter.setVODDetail(vodDetail);
            mDetailPresenter.playVOD(vodDetail);
        }
    }

    @Override
    public void showCollection(boolean isCollection) {
    }

    @Override
    public void setNewScore(List<Float> newScore) {
    }

    @Override
    public void showContentNotExit() {
        EpgToast.showToast(WebActivity.this, "访问的内容不存在！");
    }

    @Override
    public void startPlay() {
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showNoContent() {
    }

    @Override
    public void showError(String message) {
    }

    public class javascriptCallback {
        Context mContext;

        private javascriptCallback(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void createVideoView(final int x, final int y, final int width, final int height) {
            SuperLog.debug(TAG, "createVideoView->x:" + x + "|y:" + y + "|width:" + width + "|height:" + height);
            mLocationX = x;
            mLocationY = y;
            mWidth = width;
            mHeight = height;
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void createVideoView(final int x, final int y, final int width, final int height, final String playUrl) {
            SuperLog.debug(TAG, "createVideoView->x:" + x + "|y:" + y + "|width:" + width + "|height:" + height + "|playurl:" + playUrl);
            mLocationX = x;
            mLocationY = y;
            mWidth = width;
            mHeight = height;
            mPlayUrl = playUrl;
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);

        }

        @JavascriptInterface
        public void playVideo(final int x, final int y, final int width, final int height, final String playUrl, final long bookmark) {
            SuperLog.debug(TAG, "playVideo->x:" + x + "|y:" + y + "|width:" + width + "|height:" + height + "|playurl:" + playUrl + "|bookmark:" + bookmark);
            mLocationX = x;
            mLocationY = y;
            mWidth = width;
            mBookmark = bookmark;
            mHeight = height;
            mPlayUrl = playUrl;
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);

        }

        @JavascriptInterface
        public void playVideo(final String playUrl, final long bookmark) {
            SuperLog.debug(TAG, "playVideo->playurl:" + playUrl + "|bookmark:" + bookmark);
            mPlayUrl = playUrl;
            mBookmark = bookmark;
            Message msg = new Message();
            msg.what = 3;
            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void playVideo(final String playUrl) {
            SuperLog.debug(TAG, "playVideo->playUrl:" + playUrl);
            mPlayUrl = playUrl;
            Message msg = new Message();
            msg.what = 3;
            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void releasePlayView() {
            SuperLog.debug(TAG, "releasePlayView");
            Message msg = new Message();
            msg.what = 4;
            mHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void finish() {
            SuperLog.debug(TAG, "finish");
            Message msg = new Message();
            msg.what = 5;
            mHandler.sendMessage(msg);
        }

        //订购结束回调
        @JavascriptInterface
        public void orderFinish() {
            JumpToH5OrderUtils.getInstance().H5orderFinish((NewDetailDataView) mContext);
        }

        /**
         * @param packageName:包名
         * @param className:跳转界面的classname
         * @param json：跳转的入参json数据
         * @method:界面跳转的统一方法
         */
        @JavascriptInterface
        public void startSecondActivity(String packageName, String className, String json) {
//            json= "{\"playUrl\": \" http: //hwltc.tv.cdn.zj.chinamobile.com/TVOD/88888888/224/3221228318/42329859.smil/index.m3u8?playseek=20180905161300-20180905162400&timezone=UTC&rrsip=hwltc.tv.cdn.zj.chinamobile.com&zoneoffset=0&fmt=ts2hls&EnableTrick=1&servicetype=3&recType=1&icpid=&accounttype=1&limitflux=-1&limitdur=-1&tenantId=8601&accountinfo=w3Wwa58dRgWUxG4tcWxgNRuTfdcLr0ilzXeU21LNjauzyQjr2MKiI2rz76IQOJP7W/OIhZqCLBHbC13tKeaV6sWiZuzp1yPKryn3gTIgert/jDuYsCIQ1w+eg0Ia9KeI2/P1Y4702HwTCQpMOhNiBw==:20180906044002:UTC,1171757496,112.17.121.3,20180906044002,30CP23012018090599gj00,1171757496,-1,1,300,,,4,600000439142,,,5,125988907,0,125989163,0042030008038930164950016BD0B7F6,,,2,1,END&GuardEncType=2\",\"vodName\": \"\",\"bookmark\": \"\",\"vodType\": \"PROGRAM \"}";
//            className="com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity";
            if (!TextUtils.isEmpty(className)) {
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(packageName)) {
                    intent.setClassName(packageName, className);
                } else {
                    intent.setClassName(mContext, className);
                }
                if (json != null) {
                    SuperLog.error(WebActivity.class.getSimpleName(), "jsonData=" + json);
                    JSONObject jsonData = null;
                    try {
                        jsonData = new JSONObject(json);
                    } catch (JSONException e) {
                        SuperLog.error(TAG, e);
                    }
                    if (null != jsonData) {
                        Iterator iterator = jsonData.keys();
                        while (iterator.hasNext()) {
                            try {
                                String key = iterator.next() + "";
                                String value = jsonData.get(key).toString();
                                intent.putExtra(key, value);
                                SuperLog.error(WebActivity.class.getSimpleName(), "key=" + key + "---value=" + value);
                            } catch (JSONException e) {
                                SuperLog.error(TAG, e);
                            }
                        }
                    } else {
                        SuperLog.error(TAG, "jsonData is null");
                    }
                    mContext.startActivity(intent);
                }
            } else {
                SuperLog.error(TAG, "className is null");
            }

        }

        @JavascriptInterface
        public void startSecondActivity(String className) {
            if (!TextUtils.isEmpty(className)) {
                Intent intent = new Intent();
                intent.setClassName(mContext, className);
                mContext.startActivity(intent);
            } else {
                SuperLog.error(TAG, "action is null");
            }

        }

        @JavascriptInterface
        public void startPlayActivity(final String contentType, final String contentID,
                                      final String playURL, final String name, final String srcch) {

            UBDPurchase.setSrcch(srcch);
            SuperLog.debug(TAG, "add Srcch");
            startPlayActivity(contentType, contentID, playURL, name);

        }

        @JavascriptInterface
        public void startPlayActivity(final String contentType, final String contentID,
                                      final String playURL, final String name) {
            if (isEnable) {
                return;
            }
            isEnable = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /**
                     * contentType:内容类型；contentID:内容ID；playURL：播放地址;name:资源名称
                     * 浙江移动webView页，定义一个js调用方法：startPlayActivity（String contentType,String contentID ,String playURL）
                     contentType 区值 VOD,CHANNEL,PLAY_VOD,优先判断playURL是否为空，
                     如果为空，判断类型contentType，如果是点播获取详情，鉴权播放，
                     如果是直播，获取根据id找到对应的频道，任何鉴权播放；
                     如果playURL不为空，直接播放。
                     */
                    SuperLog.error(TAG, "contentType=" + contentType + ",contentID=" + contentID + ",playURL=" + playURL + ",name=" + name);
                    if (TextUtils.isEmpty(playURL)) {
                        if (!TextUtils.isEmpty(contentType)) {
                            if (contentType.equals("VOD")) {
                                if (!TextUtils.isEmpty(contentID)) {
                                    //点播获取详情，鉴权播放,跳转详情
                                    Intent intent;
                                    if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                                        intent = new Intent(mContext, ChildModeVodDetailActivity.class);
                                    } else {
                                        intent = new Intent(mContext, NewVodDetailActivity.class);
                                    }
                                    if (VodUtil.isMiguVod(contentID)) {
                                        mIsMigu = true;
                                        if (!TextUtils.isEmpty(contentID)) {
                                            mDetailPresenter.setFullScreen(true,contentID);
                                            mDetailPresenter.getVODDetail(contentID);
                                        } else {
                                            SuperLog.error(TAG, "contentID is null");
                                        }
                                        return;
                                    }
                                    intent.putExtra(NewVodDetailActivity.VOD_ID, contentID);
                                    mContext.startActivity(intent);
                                } else {
                                    SuperLog.error(TAG, "contentID is null");
                                }
                            } else if (contentType.equals("CHANNEL")) {
                                //如果是直播，获取根据id找到对应的频道，任何鉴权播放
                                String mediaID = "";
                                if (!TextUtils.isEmpty(contentID)) {
                                    Schedule schedule = LiveUtils.findScheduleById(contentID);
                                    if (null != schedule) {
                                        mediaID = schedule.getMediaID();
                                    }
                                }
                                //记录准备播放的频道ID和mediaID
                                if (!TextUtils.isEmpty(contentID) && !TextUtils.isEmpty(mediaID)) {
                                    LiveTVCacheUtil.getInstance().recordPlayChannelInfo(contentID, mediaID);
                                }
                                Intent intent = new Intent(WebActivity.this, LiveTVActivity.class);
                                intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
                                startActivity(intent);
                            } else if (contentType.equals("PLAY_VOD")) {
                                //不跳转详情界面，直接鉴权播放
                                mIsMigu = false;
                                if (!TextUtils.isEmpty(contentID)) {
                                    mDetailPresenter.setFullScreen(true,contentID);
                                    mDetailPresenter.getVODDetail(contentID);
                                } else {
                                    SuperLog.error(TAG, "contentID is null");
                                }
                            }
                        }
                    } else {
                        //如果playURL不为空，直接播放，跳转播放界面
                        if (contentType.equals("CHANNEL")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createVideoElementMatch();
                                }
                            });
                        } else if (contentType.equals("VOD")) {
                            Intent intent = new Intent(mContext, OnDemandVideoActivity.class);
                            PlayVodBean playVodBean = new PlayVodBean();
                            playVodBean.setPlayUrl(playURL);
                            playVodBean.setVodName(name);
//                            intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, JsonParse.object2String(playVodBean));
                            LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(playVodBean));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }).start();
        }

        //注册back键监听回调
        @JavascriptInterface
        public void setOnBackPressedListener(String listener) {
            SuperLog.debug(TAG, "H5 register back press event,callback function name : " + listener);
            onBackListener = listener;
        }

        //获取设备分辨率信息
        @JavascriptInterface
        public String getDisplayMetrics() {
            DisplayMetrics dm = context().getResources().getDisplayMetrics();
            StringBuilder sb = new StringBuilder();
            sb.append(dm.widthPixels).append("*").append(dm.heightPixels);
            SuperLog.debug(TAG, "屏幕分辨率 : " + sb.toString());
            return sb.toString();
        }

        //获取XMPP连接状态
        @JavascriptInterface
        public String checkXmppConnection() {
            return XmppService.getInstance().checkConnection();
        }

        //重新链接XMPP服务
        @JavascriptInterface
        public void reConnectXmpp() {
            XmppService.getInstance().reConnectXmpp();
        }

        //显示提示窗口 description:文字描述 x:x轴坐标 y:y轴坐标 width：
        @JavascriptInterface
        public void showAdIcon(String description, int x, int y, int width, int height, int location, int textsize, int color, int backgroudColor) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    setViewSize(description, x, y, width, height, location, textsize, color, backgroudColor);
                }
            });
        }

        //显示提示窗口 description:图片下载地址 x:x轴坐标 y:y轴坐标 width：图片位宽 height：图片位高
        @JavascriptInterface
        public void showAdIcon(String imageUrl, int x, int y, int width, int height) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setViewSize(imageUrl, x, y, width, height);
                }
            });
        }

        @JavascriptInterface
        public void HideAdIcon() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvIcon.setVisibility(View.GONE);
                }
            });

        }

        //检测网络状态  //0:无网络连接 1：WIFI 2:WIRED
        @JavascriptInterface
        public int getNetworkStatus() {
            return HttpUtil.getNetworkStatus();
        }

        //跳转第三方apk
        @JavascriptInterface
        public void goCpAPK(String pkgName, String className, String extra) {
            CpRoute.startApk(pkgName, className, extra);
        }

        // circulate 是否是循环播放
        @JavascriptInterface
        public void isCirculate(boolean circulate) {
            mCirculate = circulate;
        }

        // Profile更新后回调(包括登录/切换/更新昵称等)
        @JavascriptInterface
        public void onProfileUpdate(String id, String type, String alias, String headIcon) {
            ProfileManager.onProfileUpdate(id, type, alias, headIcon);
        }

        // Profile更新后回调(包括登录/切换/更新昵称等)
        @JavascriptInterface
        public void updateAikanBackground(String pic) {
            SuperLog.info2SD(TAG,"User set AiKan background,pic url = " + pic);
            //TODO 将新背景更新到本地背景数据中
            List<Navigate> navigateList = LauncherService.getInstance().getLauncher().getNavigateList();
            if (null != navigateList && LauncherService.getInstance().getFirstIndexForNormal() + 1 < navigateList.size()){
                Navigate navigate = navigateList.get(LauncherService.getInstance().getFirstIndexForNormal() + 1);
                if (navigate.getId().equalsIgnoreCase(ConvertDataFromPbsToEpg.AIKAN_NAV_ID) && null != navigate.getPageList() && navigate.getPageList().size() > 0){
                    navigate.getPageList().get(0).setBackground(pic);
                }
            }
            //当前是爱看栏目更新爱看背景
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(ProfileManager.isAiKanFragment()){
                        OTTApplication.getContext().getMainActivity().loadBackGroup(pic);
                    }
                }
            });
        }

        // 获取EPG版本号或版本名称
        @JavascriptInterface
        public String getEpgVersion(final int type) {
            if(type == 0){
                return CommonUtil.getVersionNameExcludeDebug();
            } else {
                return String.valueOf(CommonUtil.getVersionCode());
            }
        }

        // 获取机顶盒类型
        @JavascriptInterface
        public String getStbType() {
            return CommonUtil.getDeviceType();
        }

        // 获取STBID
        @JavascriptInterface
        public String getStbId() {
            return CommonUtil.getSTBID();
        }

        // 获取订购接口响应
        @JavascriptInterface
        public String getPBSOrderInfoResponse() {
            return getOrderInfoResponse();
        }
    }

    private String getOrderInfoResponse(){
        if (null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils()
                && null != OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse()){
            String STBOrderInfoResponseStr = JsonParse.object2String(OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils().getmPlayVodquerySTBOrderInfoResponse());

            if (!TextUtils.isEmpty(STBOrderInfoResponseStr)){
                return STBOrderInfoResponseStr;
            }
        }
        SuperLog.debug(TAG,"STBOrderInfoResponse为空");
        return "";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有cookie
        if (mPlayView != null) {
            mPlayView.onDestory();
            mPlayView.removeAllViews();
        }
        mHandler.removeCallbacksAndMessages(null);
        CookieSyncManager.createInstance(WebActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();

        mWebview.setWebChromeClient(null);
        mWebview.setWebViewClient(null);
        mWebview.getSettings().setJavaScriptEnabled(false);
        mWebview.clearCache(true);
        mHandler.removeCallbacksAndMessages(null);
        SuperLog.error(TAG, "<<<---onDestroy--->>>");

        //清除掉UBDTool用于H5订购上报的Srcch
        UBDPurchase.clearH5Info();

        //将跳转H5订购单例类中的是否正在订购标志位还原
        JumpToH5OrderUtils.getInstance().setDoingOrder(false);
        JumpToH5OrderUtils.getInstance().setDoingFinish(false);
    }

    private void createVideoElement(int x, int y, int width, int height) {
        SuperLog.debug(TAG, "width=" + width + ",height=" + height + ",x=" + x + ",y=" + y);
        if (mMonIndicator.getVisibility() == View.VISIBLE) {
            mMonIndicator.setVisibility(View.GONE);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPlayViewParent.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mPlayViewParent.setLayoutParams(lp);
        if (mPlayViewParent.getChildCount() > 0) {
            mPlayViewParent.removeAllViews();
        }
        addPlayView(mPlayViewParent);
        setLayout(mPlayViewParent, x, y);
    }

    private void createVideoElementMatch() {
        if (mMonIndicator.getVisibility() == View.VISIBLE) {
            mMonIndicator.setVisibility(View.GONE);
        }
        mPlayViewParentMatch.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPlayViewParentMatch.getLayoutParams();
        mPlayViewParentMatch.setLayoutParams(lp);
        if (mPlayViewParentMatch.getChildCount() > 0) {
            mPlayViewParentMatch.removeAllViews();
        }
        mPlayViewParent.removeAllViews();
        addPlayView(mPlayViewParentMatch);
        isMatchParent = true;
    }

    private void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
        mIsCreate = true;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "WebActivity onResume: ");
        mPlayView.releasePlayer();
        isEnable = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "WebActivity onResume: ");
        mPlayView.rePlay();
        super.onResume();
    }

    public void addPlayView(RelativeLayout parent) {
        //必须要把mPlayView的父布局去除掉
        if (null != mPlayView) {
            ViewParent parentView = mPlayView.getParent();
            if (null != parentView) {
                ((ViewGroup) parentView).removeAllViews();
            }
            parent.addView(mPlayView);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isMatchParent) {
            if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void setViewSize(String str, int x, int y, int width, int height, int location, int textsize, int color, int backgroudColor) {
        mTvIcon.setTextSize(textsize);
        mTvIcon.setTextColor(color);
        mTvIcon.setBackgroundColor(backgroudColor);
        mTvIcon.setGravity(Gravity.RIGHT);
        mTvIcon.setText(str);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(mTvIcon.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.width = width;
        layoutParams.height = height;
        mTvIcon.setLayoutParams(layoutParams);
        switch (location) {
            case 1:
                mTvIcon.setGravity(Gravity.LEFT);
                break;
            case 2:
                mTvIcon.setGravity(Gravity.TOP);
                break;
            case 3:
                mTvIcon.setGravity(Gravity.RIGHT);
                break;
            case 4:
                mTvIcon.setGravity(Gravity.BOTTOM);
                break;
            case 5:
                mTvIcon.setGravity(Gravity.CENTER);
                break;
        }
        mTvIcon.setVisibility(View.VISIBLE);
    }


    private void setViewSize(String str, int x, int y, int width, int height) {
        SuperLog.info2SD(TAG, "imageUrl=" + str + "---x=" + x + "---y=" + y + "---width=" + width + "---height=" + height);
        GlideApp.with(WebActivity.this).load(str).into(mAdIcon);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(mTvIcon.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        layoutParams.width = width;
        layoutParams.height = height;
        mAdIcon.setLayoutParams(layoutParams);
        mAdIcon.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(FinishPlayUrlEvent event) {
        if (JumpToH5OrderUtils.getInstance().isDoingFinish()) {
            finish();
            JumpToH5OrderUtils.getInstance().setDoingFinish(false);
        }

    }


}