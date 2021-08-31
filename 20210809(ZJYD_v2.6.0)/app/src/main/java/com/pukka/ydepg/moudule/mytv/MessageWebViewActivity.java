package com.pukka.ydepg.moudule.mytv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.view.loadingball.MonIndicator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eason on 2018/6/1.
 * 推送消息Activity
 * 用于打开推送过来的linkUrl
 */

public class MessageWebViewActivity extends BaseActivity {

    public static final String LINKURL = "linkurl";
    private static final String TAG = MessageWebViewActivity.class.getSimpleName();
    private Map<String, String> extraHeaders = new HashMap<>();
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_webview);
        mWebview = (WebView) findViewById(R.id.web_view);
        MonIndicator monIndicator = (MonIndicator) findViewById(R.id.monIndicator);
        Intent intent = getIntent();
        String url = intent.getStringExtra(LINKURL);
//        String url = "http://aikanvod.miguvideo.com:8858/pvideo/p/AC_educate.jsp?user=guest";
        WebSettings webSettings = mWebview.getSettings();
        mWebview.setWebChromeClient(new WebChromeClient());
        webSettings.setAppCacheEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        mWebview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //涉及安全,如果功能测试不正常,后续需要修改
        webSettings.setGeolocationEnabled(false);
        webSettings.setAllowContentAccess(false);
        webSettings.setAllowFileAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);

        mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (url.startsWith("http://aikanvod.miguvideo.com")) {
                    SuperLog.info2SD(TAG, "This is a PBS url.\tURL=" + url);
                    extraHeaders.put("epgSession", HttpUtil.getCookie());//(前面是key，后面是value)
                    extraHeaders.put("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL());
                    view.loadUrl(url, extraHeaders);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                monIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                monIndicator.setVisibility(View.GONE);
            }
        });

        if (url.startsWith("http://aikanvod.miguvideo.com")) {
            SuperLog.info2SD(TAG, "This is a PBS url.\tURL=" + url);
            extraHeaders.put("epgSession", HttpUtil.getCookie());//(前面是key，后面是value)
            extraHeaders.put("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL());
            mWebview.loadUrl(url, extraHeaders);
        } else {
            mWebview.loadUrl(url);
        }
    }
}
