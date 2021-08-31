package com.pukka.ydepg.common.screensaver;

import android.content.Context;

import com.pukka.ydepg.common.screensaver.model.ScreenAdvertContent;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;

import java.util.List;

public interface ScreenContract {

    interface IModel {

        //获取本地保存的屏保广告数据
        List<ScreenAdvertContent> getAdvertContent();

        //查询服务端屏保广告数据
        void queryScreenAdvert();

        //获取单张屏保展示时长
        long getBannerInterval();

        //获取屏保展示时长(具体功能取决于业务逻辑)
        long getAdvertDuration();
    }

    interface IView{
        //屏保展示
        void open(Context context);

        //屏保关闭
        void close();

        //展示广告
        void showSaverBanner(ScreenAdvertContent advertContent);

        //屏保是否在展示中
        boolean isShow();
    }

    interface IPresenter {

        //启动屏保
        void start();

        //退出屏保
        void exit();

        //ScreenContract.IModel调用queryScreenAdvert()方法后的回调
        // hasConfig=true 包含纯运营广告
        // hasSsp=true 包含SSP广告(SSP不足5张时会填充运营广告)
        void onQueryAdvertFinished(boolean hasSsp,boolean hasConfig);

        //获取下一张展示的屏保广告图片
        ScreenAdvertContent getNextAdvert();

        //点击广告后跳转到广告对应的页面
        void gotoAdPage(ScreenAdvertContent advert);

        //屏保消失时回调
        void onSaverDismiss();

        //获取单张屏保展示时长
        long getBannerInterval();

        //对外提供的方法,屏保是否在展示中
        boolean isShowing();

        //单张屏保展示后回调
        void onShowSingleAdvert(ScreenAdvertContent content);

        //判断一个广告是否支持跳转
        boolean isSupportClick(ScreenAdvertContent content);

        //获取当前播放的屏保类型是否是SSP广告
        boolean isPlaySsp();

        //设置当前播放屏保的类型是否是SSP广告
        void setIsSsp(boolean isSsp);
    }
}