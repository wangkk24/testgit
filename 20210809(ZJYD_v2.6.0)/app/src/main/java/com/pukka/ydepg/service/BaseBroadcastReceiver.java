package com.pukka.ydepg.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.base.BaseActivity;

public class BaseBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BaseBroadcastReceiver";

    private BaseActivity activity;

    public BaseBroadcastReceiver(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SuperLog.info(TAG, "intent action =  " + intent.getAction());
        String action = intent.getAction();
        switch (action) {
            case BroadCastConstant.COM_HUAWEI_OTT_APP_EXIT:
                activity.finish();
                break;
            default:
                SuperLog.debug(TAG, "get in the default case");
                break;
        }
    }
}