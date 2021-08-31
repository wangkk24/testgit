package com.pukka.ydepg.common.fcc;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserAttrsRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MulticastTool {

    private static final String TAG = MulticastTool.class.getSimpleName();

    private static MulticastTool mInstance;

    private MediaPlayer mediaPlayer;

    private PlayResultCallback callback;

    public static synchronized MulticastTool getInstance() {
        if (mInstance == null) {
            mInstance = new MulticastTool();
        }
        return mInstance;
    }

    //检测机顶盒是否支持FCC
    public void startTestFcc(@NonNull PlayResultCallback callback) {
        this.callback = callback;
        queryUserAttrs();
    }

    private void testFcc(){
        SuperLog.info2SD(TAG,"Begin to test FCC.");
        if(!SharedPreferenceUtil.getInstance().getMulticastSwitch()){
            SuperLog.info2SD(TAG,"Multicast switch is close, no need to check multicast capability and set [false].");
            OTTApplication.getContext().setSupportMulticast(false);
            callback.onFinish();
            return;
        }

        mediaPlayer = new MediaPlayer();
        //如果是白名单直接返回true,如果是黑名单直接返回false
        //都没有则去进行检测
        List<String> black = CommonUtil.getListConfigValue(Constant.BLACKLIST_FOR_FCC);
        List<String> white = CommonUtil.getListConfigValue(Constant.WHITELIST_FOR_FCC);
        if (isListContainsDevice(black)) {
            SuperLog.info2SD(TAG, "Current device is in blacklist");
            OTTApplication.getContext().setSupportMulticast(false);
            callback.onFinish();
        } else if (isListContainsDevice(white)) {
            SuperLog.info2SD(TAG, "Current device is in whitelist");
            OTTApplication.getContext().setSupportMulticast(true);
            callback.onFinish();
        } else {
            init();
        }
    }

    private boolean isListContainsDevice(List<String> list) {
        String currentDevice = CommonUtil.getDeviceType();
        SuperLog.info2SD(TAG, "CurrentDevice=" + currentDevice + "\tlist=" + list);
        if (CollectionUtil.isEmpty(list) || TextUtils.isEmpty(currentDevice)){
            SuperLog.info2SD(TAG, "list is null or currentDevice is null, list not contain current device.");
            return false;
        }

        for(String configDevice : list){
            //2020/09/14新增如果黑名单包含all，则直接认为配置全部设备，且只有黑名单会存在all
            if (configDevice.contains(currentDevice)||configDevice.equalsIgnoreCase("all")) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        String URL = CommonUtil.getConfigValue(Constant.PLAY_FCC_URL);
        if (TextUtils.isEmpty(URL)) {
            URL = "igmp://233.249.3.11:10086";
        }
        SuperLog.info2SD(TAG, "Test multicast URL is :" + URL);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SuperLog.info2SD(TAG, "Multicast test successfully in onPrepare.");
                mediaPlayer.start();
                if (callback != null) {
                    OTTApplication.getContext().setSupportMulticast(true);
                    callback.onFinish();
                }
                mediaPlayer.release();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                SuperLog.info2SD(TAG, "Multicast test failed.");
                if (callback != null) {
                    OTTApplication.getContext().setSupportMulticast(false);
                    callback.onFinish();
                }
                mediaPlayer.release();
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                SuperLog.info2SD(TAG, "Multicast test successfully in onCompletion.");
                if (callback != null) {
                    OTTApplication.getContext().setSupportMulticast(true);
                    callback.onFinish();
                }
                mediaPlayer.release();
            }
        });
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放声音类型
            //mediaPlayer.setDataSource("http://hwltc.tv.cdn.zj.chinamobile.com/110000000101/m0063030000000000000000005854434/index.m3u8");
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            SuperLog.error(TAG, e);
            SuperLog.info2SD(TAG, "Multicast test exception.");
            if (callback != null) {
                OTTApplication.getContext().setSupportMulticast(false);
                callback.onFinish();
            }
        }
    }

    public interface PlayResultCallback {
        void onFinish();
    }

    /**
     * 查询组播开关 默认false  关闭状态
     */
    @SuppressLint("CheckResult")
    public void queryUserAttrs() {
        SuperLog.info2SD(TAG,"Begin to query multicast switch.");
        QueryUserAttrsRequest request = new QueryUserAttrsRequest();
        request.setUserId(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
        request.setAttrNames(new ArrayList<String>(Arrays.asList(com.pukka.ydepg.launcher.Constant.TVMULTICASTSWITCH)));
        HttpApi.getInstance().getService().queryUserAttrs(HttpUtil.getVssUrl(HttpConstant.QUERY_USER_ATTRS), request).subscribe(queryUserAttrsResponse -> {
            String orderingSwitch = "0"; //0关 1开
            if (queryUserAttrsResponse.getReturnCode() == Integer.parseInt(Result.RETCODE_OK_TWO)) {
                List<QueryUserAttrsResponse.UserAttr> attrs = queryUserAttrsResponse.getAttrs();
                if (!CollectionUtil.isEmpty(attrs)){
                    for (QueryUserAttrsResponse.UserAttr userAttr : attrs){
                        if (com.pukka.ydepg.launcher.Constant.TVMULTICASTSWITCH.equalsIgnoreCase(userAttr.getAttrName())){
                            orderingSwitch = userAttr.getAttrValue();
                            SuperLog.info2SD(TAG,"Get multicast switch from vss successfully.");
                        }
                    }
                }
            }
            boolean multicastSwitch = orderingSwitch.equals("1");
            SuperLog.info2SD(TAG,"queryMulticastSwitch success,   multicastSwitch="+multicastSwitch);
            SharedPreferenceUtil.getInstance().setMulticastSwitch(multicastSwitch);
            testFcc();
        }, throwable -> {
            SuperLog.error(TAG,  "queryMulticastSwitch exception, multicastSwitch=false");
            SharedPreferenceUtil.getInstance().setMulticastSwitch(false);
            testFcc();
        });
    }
}