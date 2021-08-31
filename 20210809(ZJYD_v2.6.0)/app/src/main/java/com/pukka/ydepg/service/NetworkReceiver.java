package com.pukka.ydepg.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.xmpp.XmppService;

/**
 * Created by lzr on 2016/12/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkReceiver.class.getSimpleName();

    private NetWorkStateListener netWorkStateListener;

    public void setNetWorkStateListener(NetWorkStateListener netWorkStateListener) {
        this.netWorkStateListener = netWorkStateListener;
    }

    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        //SuperLog.info2SD(TAG, "WiFi network connected.");
                        if (netWorkStateListener != null) {
                            netWorkStateListener.changeToWifi();
                        }
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        //SuperLog.info2SD(TAG, "Wired network connected.");
                        if (netWorkStateListener != null) {
                            netWorkStateListener.changeToWire();
                        }
                    }
                    //网络连接[恢复]后且登录成功后查询XMPP连接状态,如果断链则重连,登录状态用来防止登录前就链接XMPP
                    if( ! isConnected && OTTApplication.getContext().isLoginSuccess()){
                        SuperLog.info2SD(TAG, "Network connection recovered. Begin to check XMPP connection.");
                        XmppService.getInstance().checkXmppConnection();
                    }

                } else {
                    SuperLog.error(TAG, "Network is disconnected!");
                    if (netWorkStateListener != null) {
                        netWorkStateListener.changeToError();
                    }
                    NetworkExceptionDialog.show(context);
                }

                isConnected = activeNetwork.isConnected();
            } else {   // not connected to the internet
                SuperLog.error(TAG, "NetworkInfo is null, can not get network status.");
                NetworkExceptionDialog.show(context);
                if (netWorkStateListener != null) {
                    netWorkStateListener.changeToError();
                }
                isConnected = false;
            }
        }
    }

    public interface NetWorkStateListener {
        //网络状态变为移动网络
        void changeToWire();

        //网络状态变为wifi
        void changeToWifi();

        //网络状态出错
        void changeToError();
    }
}