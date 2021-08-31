package com.pukka.ydepg.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chinamobile.middleware.auth.TokenManager;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

/**
 * 接收第三方中间件的token状态广播
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.receiver.TokenReceiver.java
 * @date: 2018-01-10 09:04
 * @version: V1.0 描述当前版本功能
 */


public class TokenReceiver extends BroadcastReceiver {
    private static final String TAG = TokenReceiver.class.getSimpleName();
    private TokenStateListener mTokenStateListener;

    public TokenReceiver(TokenStateListener tokenStateListener) {
        this.mTokenStateListener = tokenStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        SuperLog.info2SD(TAG," ========== Received token update broadcast ========== ");
        if(mTokenStateListener != null && TextUtils.equals(action, TokenManager.TOKEN_ACTION)){
            mTokenStateListener.onStateChange(action,intent);
        }
    }

   public interface TokenStateListener{
        void onStateChange(String action,Intent intent);
    }
}
