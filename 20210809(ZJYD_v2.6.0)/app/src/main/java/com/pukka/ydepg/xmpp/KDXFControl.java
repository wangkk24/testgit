package com.pukka.ydepg.xmpp;

import android.content.ComponentName;
import android.content.Intent;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.net.URLDecoder;

/**
 * Created by Liu on 2018/6/21.
 */

public class KDXFControl {
    private static final String TAG = KDXFControl.class.getName();

    private static final String XIRI_PACKAGE = "com.iflytek.xiri";
    private static final String XIRI_ACTION = "com.iflytek.xiri2.START";
    private static final String XIRI_EXTRA_START_MODE = "startmode";
    private static final String XIRI_EXTRA_TEXT = "text";

    public void sendMessageToKDXF(String msg) throws Exception {
        if(null == msg){
            SuperLog.error(TAG,"Get empty KDXF control message(playUrl in XMPP).");
            return;
        }

        Intent intent = new Intent();
        intent.setPackage(XIRI_PACKAGE);
        intent.setAction(XIRI_ACTION);
        intent.putExtra(XIRI_EXTRA_START_MODE,"text");
        //msg示例 keyword%3D%E6%B5%99%E6%B1%9F%E5%8D%AB%E8%A7%86
        String decodeMsg = URLDecoder.decode(msg,"UTF-8");
        //decodeMsg示例 keyword=浙江卫视
        String realMsg = getKeyString(decodeMsg);
        //realMsg示例 浙江卫视
        intent.putExtra(XIRI_EXTRA_TEXT,realMsg);

        ComponentName cpName = OTTApplication.getContext().startService(intent);
        SuperLog.info2SD(TAG,"Send msg[" + realMsg + "] to KDXF finished. " + cpName );
    }

    private String getKeyString(String str){
        return str.replace("keyword=","");
    }
}
