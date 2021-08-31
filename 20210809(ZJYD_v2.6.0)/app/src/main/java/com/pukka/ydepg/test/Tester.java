package com.pukka.ydepg.test;

import android.content.Intent;
import android.view.KeyEvent;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.launcher.ui.activity.AllAppInfoActivity;

public class Tester {

    public static void test(int keyCode){
        if(BuildConfig.DEBUG){
            switch (keyCode){
                case KeyEvent.KEYCODE_5:
                    //XmppTester.sendPushMessage();
                    //OTTErrorWindowUtils.getErrorInfoFromPbs("曹苏杭测试接口","144020000",null);
                    startAuthUI();
                    break;
                case KeyEvent.KEYCODE_6:
                    startSelfInstallApp();
                    break;
                default:
                    break;
            }

        }
    }

    private static void startAuthUI(){
        Intent intent = new Intent("com.chinamobile.middleware.auth.loginui");
        intent.putExtra("type", "jar");
        OTTApplication.getContext().getCurrentActivity().startActivity(intent);
    }

    private static void startSelfInstallApp(){
        Intent intent = new Intent();
        intent.setClass(OTTApplication.getContext().getCurrentActivity(), AllAppInfoActivity.class);
        OTTApplication.getContext().getCurrentActivity().startActivity(intent);
    }
}
