package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.huawei.cloudplayer.sdk.HCPGlobalConfig;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.UpdateApk;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected boolean isFromEPGGo2There = false;
    protected boolean hasCheckUpdate = false;
    protected String crashPolicyConfig = HCPGlobalConfig.CrashPolicy.OPEN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCheckUpdate = false;
        if (getIntent() != null) {
            String crashPolicy = getIntent().getStringExtra("crashPolicy");
            DebugLog.info(TAG,"[getCrashPolicy] crashPolicy="+crashPolicy);
            if(!TextUtils.isEmpty(crashPolicy)){
                crashPolicyConfig = crashPolicy;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFromEPGGo2There && !hasCheckUpdate) {
            if (getIntent() != null) {
                final String url = getIntent().getStringExtra("loginRouteForUpgrade");
                if (!TextUtils.isEmpty(url)) {
                    UpdateApk updateApk = new UpdateApk(BaseActivity.this);
                    updateApk.startLogin(url);
//                    updateApk.startLogin("http://124.70.66.212:33500/UPGRADE");
                    hasCheckUpdate = true;
                } else {
                    DebugLog.debug(TAG, getString(R.string.update_tip_nointface));
                }
            }
        }
    }
}
