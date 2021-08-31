package com.pukka.ydepg.moudule.vrplayer.vrplayer;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.UpdateApk;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected  boolean isFromEPG = false;
    protected boolean hasCheckUpgrade = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCheckUpgrade = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFromEPG && getIntent() != null && !hasCheckUpgrade){
            String url = getIntent().getStringExtra("loginRouteForUpgrade");
            if(!TextUtils.isEmpty(url)){
                UpdateApk updateApk = new UpdateApk(this);
                updateApk.startLogin(url);
                hasCheckUpgrade=false;
            }else {
                DebugLog.debug(TAG,"无升级接口");
            }
        }
    }
}
