package com.pukka.ydepg.common.screensaver.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.scene.UBDAdvert;
import com.pukka.ydepg.common.screensaver.ScreenConstant;
import com.pukka.ydepg.common.screensaver.ScreenContract;
import com.pukka.ydepg.common.screensaver.model.ScreenAdvertContent;
import com.pukka.ydepg.common.screensaver.model.ScreenModel;
import com.pukka.ydepg.common.screensaver.view.ScreensaverDialog;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.pukka.ydepg.common.screensaver.model.ScreenAdvertContent.TYPE_SSP;

public class ScreenPresenter implements ScreenContract.IPresenter {

    private ScreenContract.IModel screenModel = new ScreenModel(this);

    private ScreenContract.IView screenView;

    //保存下一个要展示的广告index
    private int index  = 0;

    //屏保图片总数
    private int number = 0;

    //SSP广告播放时间控制
    private Timer timer;

    //是否播放含有SSP广告的屏保广告
    private boolean isPlaySsp = true;

    //对外部使用者开放的方法,启动屏保展示
    @Override
    public void start() {
        //屏保展示中则不再展示屏保,防止重复展示
        if( isShowing() ){
            SuperLog.debug(ScreenConstant.TAG,"Screensaver is showing now.");
            return;
        }

        //屏保开关关闭则不展示屏保
        if(!enableScreensaver()){
            return;
        }

        //每次进入屏保直接查询
        //screenModel = new ScreenModel(this);
        SuperLog.debug(ScreenConstant.TAG," ========== Screensaver is showing now. ========== ");
        screenModel.queryScreenAdvert();

        //指定时间后只播放运营配置广告,关闭SSP广告
        removeSspAdvertByTime();
    }
    //此方法结束后->onQueryAdvertFinished


    //对外部使用者开放的方法,关闭屏保
    @Override
    public void exit() {
        if( screenView != null && screenView.isShow()){
            //Dialog调用dismiss时会通过onDismissListener调用Presenter的onSaverDismiss()
            screenView.close();
        } else {
            //调用exit方法时并不一定有屏保,如果没有屏保,则不会走onSaverDismiss回调,但此场景也属于有用户操作,也需要更新用户无操作定时器
            RefreshManager.getInstance().updateUserAction(true, RefreshManager.TimerType.SCREENSAVER);
        }
    }

    //对外部使用者开放的方法,获取屏保展示状态
    @Override
    public boolean isShowing() {
        if(screenView == null){
            return false;
        } else {
            return screenView.isShow();
        }
    }

    //指定时间后只播放运营配置广告,关闭SSP广告
    private void removeSspAdvertByTime(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                SuperLog.debug(ScreenConstant.TAG," ********** SSP screen played time finished. Begin to play VSP screensaver. ********** ");
                isPlaySsp = false;
                if(CollectionUtil.isEmpty(screenModel.getAdvertContent())){
                    SuperLog.debug(ScreenConstant.TAG,"VSP screen advert list is empty, exit Screensaver.");
                    exit();
                } else {
                    //重置播放总数，播放位置
                    setSaverNumber(screenModel.getAdvertContent().size(),0);
                }

            }
        }, screenModel.getAdvertDuration());
        SuperLog.debug(ScreenConstant.TAG,"SSP screen advert will be limited ***** ["+screenModel.getAdvertDuration()/1000 +"]s *****");
    }

    //对Model开放的回调,查询广告结束后调用
    @Override
    public void onQueryAdvertFinished(boolean hasSsp,boolean hasConfig) {
        SuperLog.info2SD(ScreenConstant.TAG,"Has SSP advert="+hasSsp+"\t Has VSP advert="+hasConfig);
        if(!hasSsp){
            if(timer!=null){
                timer.cancel();
            }
        }

        if(hasSsp||hasConfig){
            //查询屏保广告数据成功,展示屏保
            if(number == 0){ //每次开启屏保的第一次查询number=0 后续number=查询到的屏保数量
                //首次开启屏保请求index=0,非首次开启index为上次展示的屏保index
                setSaverNumber(screenModel.getAdvertContent().size(),index);
                showSaverDialog();
                //屏保启动后关闭监控用户无操作启动屏保
                RefreshManager.getInstance().stopWatchScreensaverUserInteraction();
            } else {
                //V2.6.0需求要求每轮重新请求屏保
                if(index>=screenModel.getAdvertContent().size()){
                    index=0;
                }
                SuperLog.info2SD(ScreenConstant.TAG,"index="+index);
                //index为上次展示的屏保index
                setSaverNumber(screenModel.getAdvertContent().size(),index);
                //此时屏保正在screenView的控制下循环展示中,不需要再次启动展示,只需要更新总数(一般也不变),当前播放位置也保持不变
            }
        } else {
            //查询屏保广告数据失败,不展示屏保
            SuperLog.error(ScreenConstant.TAG,"Can not show screensaver because of get screen advert failed.");
            clear();
        }
    }

    //对View提供的能力,获取下一张要展示的广告对象
    @Override
    public ScreenAdvertContent getNextAdvert(){
        ScreenAdvertContent content = null;
        try{
            content = screenModel.getAdvertContent().get(index);
            index++;
            if( index == number ){
                index = 0;    //取到最后一张,下次从头开始
            }
        } catch (Exception e){
            SuperLog.error(ScreenConstant.TAG,e);
        }
        return content;
    }

    //对Model提供的能力,获取下一张要展示的广告对象
    @Override
    public boolean isPlaySsp(){
        return isPlaySsp;
    }

    //对Model提供的能力,设置当前播放屏保的类型是否是SSP广告
    @Override
    public void setIsSsp(boolean isSsp){
        this.isPlaySsp = isSsp;
    }





    //对View提供的能力,点击广告跳转动作
    @Override
    public void gotoAdPage(ScreenAdvertContent advert) {
        if(ScreenAdvertContent.TYPE_VSP.equals(advert.getType())){
            //Vod对象中customField中Extra字段(key=zjapkextra)value格式样例 {"cpId":"2","albumId":"1051629","chnId":"1000001"}
            //配置广告点击跳转并上报话单
            if(!TextUtils.isEmpty(advert.getClickUrl())){
                //跳转H5
                Context context = OTTApplication.getContext().getCurrentActivity();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", advert.getClickUrl());
                context.startActivity(intent);
                UBDAdvert.reportScreenClick(advert);
                exit();
            } else if(!TextUtils.isEmpty(advert.getPkg())){
                //跳转第三方APK/内部页面
                //跳转详情的扩展参数 zjapkextra : {"vod_id":"2"} zjPkg : com.pukka.ydepg zjCls
//                if("com.pukka.ydepg".equals(advert.getPkg()) && advert.getCls().contains("VodDetailActivity")){
//                    //启动详情页面
//                    Context context = OTTApplication.getContext().getCurrentActivity();
//                    Intent intent = new Intent(context, VodDetailActivity.class);
//                    intent.putExtra(VodDetailActivity.VOD_ID, advert.getExtra());
//                    context.startActivity(intent);
//                }
                CpRoute.startApk(advert.getPkg(),advert.getCls(),advert.getExtra());
                //上报配置广告点击话单
                UBDAdvert.reportScreenClick(advert);
                exit();
            }
            //else {
                //No need to do anything
                //无跳转地址不跳转
            //}
        } else {
            //SSP广告点击跳转H5
            if(!TextUtils.isEmpty(advert.getClickUrl())){
                Context context = OTTApplication.getContext().getCurrentActivity();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", advert.getClickUrl());
                context.startActivity(intent);
                //上报SSP广告点击话单
                AdManager.getInstance().reportAdvert(advert.getSspContent(),AdConstant.AdType.BANNER,AdConstant.ReportActionType.IMPRESSION);
                exit();
            }
        }
    }

    //对View提供的能力,获取单张屏保展示时长
    @Override
    public long getBannerInterval() {
        return screenModel.getBannerInterval();
    }

    //对View提供的能力,检查广告是否支持点击跳转
    @Override
    public boolean isSupportClick(ScreenAdvertContent content) {
        if( !TextUtils.isEmpty(content.getClickUrl() )){
            return true;
        } else {
            if(ScreenAdvertContent.TYPE_VSP.equals(content.getType())){
                if(!TextUtils.isEmpty(content.getPkg()) ){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    //对View提供的回调,展示单张海报时调用
    @Override
    public void onShowSingleAdvert(ScreenAdvertContent content) {
        // 20200904 章文怡要求广告上报要全部数据
        // 20210510 张长顺要求上报指定次数数据
        // 20210624 Hujiekai要求上报全部数据
        //曝光上报
        if(ScreenAdvertContent.TYPE_VSP.equals(content.getType())){
            //配置广告曝光上报话单
            UBDAdvert.reportScreenImpression(content);
        } else {
            //SSP广告曝光上报话单
            AdManager.getInstance().reportAdvert(content.getSspContent(),AdConstant.AdType.BANNER,AdConstant.ReportActionType.IMPRESSION);
        }

        if(index == 0 && isPlaySsp){
            //V2.6.0要求每轮重新请求屏保，设计在在播放SSP广告的最后一张,需要请求新一轮广告
            //这样在最后一张展示完之前理论上请求就完成了，可以无缝衔接下一轮展示
            //运营配置屏保不需要每轮重新请求
            SuperLog.info2SD(ScreenConstant.TAG,"Begin to request SSP screen advert again");
            screenModel.queryScreenAdvert();
        }

        SuperLog.info2SD(ScreenConstant.TAG,"Current screen advert is ["
                + (TYPE_SSP.equals(content.getType())?"SSP":"VSP")
                + "]\tindex = " + (index==0?screenModel.getAdvertContent().size():index));
    }

    //对View提供的回调,非开放方法。屏保关闭时调用
    @Override
    public void onSaverDismiss() {
        SuperLog.info2SD(ScreenConstant.TAG,"Exit screensaver.");
        clear();
        //退出屏保时 1检查桌面是否有更新 2启动用户无操作定时器
        RefreshManager.getInstance().checkDesktopAfterScreensaver();
    }

    //释放资源
    private void clear(){
        screenView = null;
        //screenModel = null;
        if(timer !=null ){
            timer.cancel();
            timer = null;
        }

        number = 0;
        isPlaySsp = true;
        index = 0;
    }





    //[内部方法]判断屏保开关是否开启
    private boolean enableScreensaver(){
        return AdUtil.enableSsp(AdConstant.AdClassify.SCREEN);
    }

    private void showSaverDialog(){
        Activity activity = OTTApplication.getContext().getCurrentActivity();
        if(activity == null){
            SuperLog.error(ScreenConstant.TAG,"Current context is null. Can not show screensaver.");
            return;
        }

        //启动屏保控件并展示首张屏保广告
        refreshAdvertContent(screenModel.getAdvertContent());
        screenView = new ScreensaverDialog(activity,this);
        screenView.open(activity);
        //延时展示下一张屏保广告
    }

    public void setSaverNumber(int size,int index){
        this.number = size;
        this.index  = index;
    }

    //每次启动屏保时调用,保证可以上报曝光话单
    private void refreshAdvertContent(List<ScreenAdvertContent> listContent){
        for(ScreenAdvertContent content : listContent){
            content.setReported(false);
        }
    }
}