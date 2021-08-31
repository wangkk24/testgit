package com.pukka.ydepg.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.event.BookmarkChangeEvent;
import com.pukka.ydepg.event.FavoriteChangeEvent;
import com.pukka.ydepg.moudule.catchup.presenter.TVODPresenter;
import com.pukka.ydepg.service.presenter.HeartBeatPresenter;
import com.pukka.ydepg.service.presenter.contract.HeartBeatContract;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 心跳 Service
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: HeartBeatService
 * @Package com.pukka.ydepg.service
 * @date 2018/1/17 下午2:38
 */
public class HeartBeatService extends Service implements HeartBeatContract.HeartBeatView {

    private static final String TAG = HeartBeatService.class.getSimpleName();

    private Timer mTimer = new Timer(true);

    private HeartBinder mHeartBinder = new HeartBinder();

    public boolean isHeartBitStop() {
        return isHeartBitStop;
    }

    private boolean isHeartBitStop = false;

    private static HeartBeatService mContext;

    private HeartBeatPresenter mPresenter = new HeartBeatPresenter();

    public HeartBeatService() {
        mContext = this;
    }

    public static HeartBeatService getInstance() {
        return mContext;
    }

    public IBinder onBind(Intent intent) {
        return mHeartBinder;
    }

    public void onCreate() {
        SuperLog.info2SD(TAG, "onCreate()");
        super.onCreate();
        mPresenter.attachView(this);
        // 15 seconds after starting the mTimer [after a successful login there are many things
        // that need to be addressed, to avoid competition for the task after login cpu, so here
        // is set to 15 seconds]
        long seconds = 0;//15;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startHeartBeat(true);
            }
        }, seconds * 1000L);
    }

    private void newHeartBeatTask(long delaySeconds) {
        if (null == mTimer) {
            mTimer = new Timer(true);
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startHeartBeat(false);
            }
        }, delaySeconds * 1000L);
    }

    /**
     * 心跳
     * @param isFirst 从onCreate中发起的心跳为true；心跳周期发起的心跳都是false；
     */
    private void startHeartBeat(final boolean isFirst) {
        if (isHeartBitStop) {
            return;
        }
        mPresenter.startHeartBeatService(isFirst, this);
    }

    @Override
    public void onDestroy() {
        SuperLog.info2SD(TAG, "onDestroy()");
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        if(null!=mPresenter){
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 停止心跳（停止定时器）
     */
    public void stopHeartBit() {
        isHeartBitStop = true;
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 立马执行心跳和launcher更新查询
     */
    public void startHeartBit() {
        isHeartBitStop = false;
        newHeartBeatTask(0);
    }

    /**
     * 刷新频道信息
     */
    public void refreshChannelInfo(){
        onChannelVersionChange(true,"-2","-2");
    }

    /**
     * 刷新频道动态参数属性CHANNEL_NO
     */
    public void refreshChannelDynamicProtites(){
        mPresenter.queryAllChannelDynamicProperties("-2", this);
    }

    /**
     * binder
     */
    public class HeartBinder extends Binder {
        public HeartBeatService getService() {
            return HeartBeatService.this;
        }
    }

    /**
     * 心跳中维护的各个接口的Version
     */
    public interface VersionType {
        String VERSION_CHANNEL   = "zjyd_heart_channel";
        String VERSION_FAVORITE  = "favorite";
        String VERSION_BOOKMARK  = "bookmark";
        String VERSION_SUBSCRIBE = "subscribe";
    }

    @Override public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }

    /**
     * 心跳成功回调
     * @param nextCallInterval 心跳周期
     */
    @Override public void onStartHeartBeatSucc(String nextCallInterval) {
        //存储
        HeartBeatUtil.getInstance().refreshNextCallInterval(nextCallInterval);
        //开始定时下一个心跳周期
        newHeartBeatTask(Long.parseLong(nextCallInterval));
    }

    /**
     * 心跳出错了
     * @param isFirst
     */
    @Override public void onStartHeartBeatError(boolean isFirst) {
        SuperLog.error(TAG,"onLineHeartBeat failed, isFirst="+isFirst);
        //心跳出错,重置周期
        HeartBeatUtil.getInstance().refreshNextCallInterval("");
        if (isFirst) {
            //心跳接口第一次调用就出错,通过type获取场景的version都是-1,
            //需要请求频道和频道动态参数接口,更新频道信息;否则后续场景中直播没有数据;
            //如果本次场景接口尝试也失败,那么只能等到下次心跳周期到来,才能重新调用;
            onChannelVersionChange(true,"-2","-2");
        }
        newHeartBeatTask(HeartBeatUtil.getInstance().getNextCallInterval());
    }

    @Override public void onBookmarkVersionChange(String bookmarkVersion) {
        SuperLog.info2SD(TAG,"Bookmark Version change.");
        EventBus.getDefault().post(new BookmarkChangeEvent());
    }

    @Override public void onFavoriteVersionChange(String favoriteVersion) {
        SuperLog.info2SD(TAG,"Favorite Version change.");
        EventBus.getDefault().post(new FavoriteChangeEvent());
    }

    /**
     * 频道版本号变化
     * @param isChange true表示版本号有变化
     * @param channelVersion
     * @param subscribeVersion
     */
    @Override public void onChannelVersionChange(boolean isChange,String channelVersion,String subscribeVersion) {
        SuperLog.info2SD(TAG,"Version change :"+isChange+"(true=queryAllChannel false=queryAllChannelDynamicProperties)\n\tchannelVersion:"+channelVersion+"\n\tsubscribeVersion:"+subscribeVersion);
        if(isChange){
            mPresenter.queryAllChannel(channelVersion,subscribeVersion, this);
        }else{
            // 订购关系变化:
            // 心跳返回的频道版本号格式是:"channelVersion|subscribeVersion"
            // 如果心跳中频道版本号没有变化:
            // 比较当前本地的动态属性版本号和心跳中解析出来的版本号;
            // 不一致查询频道动态属性接口;用来更新channelNO
            String currentSubscribeVersion = HeartBeatUtil.getInstance().getVersion(HeartBeatService.VersionType.VERSION_SUBSCRIBE);
            if( !TextUtils.isEmpty(subscribeVersion) && !TextUtils.equals(subscribeVersion,currentSubscribeVersion) ){
                SuperLog.info2SD(TAG,"subscribeVersion is changed, begin to queryAllChannelDynamicProperties.");
                mPresenter.queryAllChannelDynamicProperties(subscribeVersion, this);
            } else {
                SuperLog.info2SD(TAG,"subscribeVersion is not changed(or null),begin to queryChannelSubjectList.");
                //获得可回看的频道列表，语音回看进行限制
                TVODPresenter presenter=new TVODPresenter(this);
                presenter.queryChannelSubjectList();
            }
        }
        mPresenter.querySuperScript();
    }
}