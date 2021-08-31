package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6Controller.SubmitDeviceInfoController;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.report.error.ErrorInfoReport;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.report.ubd.scene.UBDVersion;
import com.pukka.ydepg.common.start.StartPictureManager;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PayUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.bean.response.QueryCustomizeConfigResponse;
import com.pukka.ydepg.launcher.mvp.contact.LauncherContact;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.ThreadManager;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.search.utils.ChineseUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


/**
 * 首页数据业务类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.LauncherPresenter.java
 * @date: 2017-12-15 14:41
 * @version: V1.0 描述当前版本功能
 */
public class LauncherPresenter extends AuthenticatePresenter<LauncherContact.ILauncherView> implements LauncherContact.ILauncherPresenter {
    private static final String TAG = LauncherPresenter.class.getSimpleName();

    public LauncherPresenter() {}

    /**
     * 首次登录实现
     * 1.基础认证(获取token->ZJLogin->querySubscribe->queryCustomizeConfig->queryBindedSubscriber->getStartPicture->queryUniPayInfo)
     * 2.firstCheckUpdate(调用queryLauncher,成功后回调MainActivity::loadLauncherData方法,通知MainActivity登录成功,然后启动BaseActivity::startServer
     * 3.submitDeviceInfo(结束)
     */
    //firstCheckUpdate完成后回调 public void loadLauncher(Context context)
    @Override
    public void firstLogin(Context context) {
        doBaseAuthenticate(context, false)
            .subscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool()))
            .unsubscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool()))
            .subscribe(new RxCallBack<QueryCustomizeConfigResponse>(context) {
                @Override
                public void onSuccess(@NonNull QueryCustomizeConfigResponse queryCustomizeConfig) {
                    //查询桌面文件,登录流程从这里延续
                    LauncherService.getInstance().queryLauncherLogin(LauncherPresenter.this,context);
                    //提交设备类型,登录流程与此无关
                    SubmitDeviceInfoController.submitDeviceInfo(context);
                }

                @Override public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG,e);
            }
        });
    }

    private void doAfterLogin(Context context) {
        afterAuthenticate(context).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(
            subjectVODLists -> {
                List<Subject> subjectList = new ArrayList<>();
                for (SubjectVODList vodList : subjectVODLists) {
                    subjectList.add(vodList.getSubject());
                }
                //加密缓存栏目数据,TVGUIDE界面直接取缓存的栏目数据
                LiveTVCacheUtil.getInstance().cacheColumnList(subjectList);
            },
            throwable ->SuperLog.error(TAG, throwable)
        );
    }

    /**
     * 登录并加载桌面，直接加载桌面，如果登录成功展示桌面
     */
    @Override
    public void loginAndLoadLauncher(Context context) {
        SuperLog.info2SD(TAG, "Begin to loginAndLoadLauncher");
        firstLogin(context);
    }

    /**
     * 加载桌面
     */
    public Observable<LauncherService> parseLauncher() {
        return Observable.create(new ObservableOnSubscribe<LauncherService>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<LauncherService> subscribe) {
                LauncherService.getInstance().parseLauncher();
                subscribe.onNext(LauncherService.getInstance());
            }
        });
    }

    @Override
    public void loadLauncher(Context context) {
        parseLauncher()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new RxCallBack<LauncherService>(context) {
            @Override
            public void onSuccess(@NonNull LauncherService launcherService) {
                if (null == launcherService || null == launcherService.getLauncher()){
                    SuperLog.info2SD(TAG, "Parse Launcher fail,download launcher again.");
                    //存本地版本号为-1，确保重新下载更新
                    SharedPreferenceUtil.getInstance().saveLauncherVersion("-1");
                    MessageDataHolder.get().setRefreshLauncherData(true);
                    if (null != launcherService){
                        launcherService.checkAndUpdate(context,0);
                    }
                    return;
                }
                SuperLog.info2SD(TAG, "[Login-13]Parse Launcher successfully.");
                if (mView != null) {
                    //每次桌面更新时，重置简版epg序号
                    launcherService.setSimpleEpgIndex(-1);
                    launcherService.parseGroupElements();//组装GroupElement

                    //回调MainActivity，加载首页数据
                    doBefore();
                    mView.loadLauncherData(launcherService.getLauncher().getNavigateList(), true);
                    mView.startService();
                    doAfter();
                    TopicService.getInstance().checkAndUpdate(context); //启动Topic更新
                }
                doAfterLogin(context);
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(TAG,e);
            }
        });
    }

    private void doBefore(){
        //===========非耗时操作或者已经在子线程中执行的操作===========
        //启动用户行为上报
        UBDTool.getInstance().init();//此代码务必放到 mView.loadLauncherData(launcherService.getLauncher().getNavigateList(), true); 之前执行
        UBDVersion.record();
    }

    private void doAfter(){
        //===========需要在子线程执行的操作(耗时操作等)===========
        new Thread(() -> {
            //读取本地文件保存的支付区域信息映射表缓存到文件中
            PayUtil.getAreaCodeJsonObject();
            //加载错误码信息,用于上报错误信息
            ErrorInfoReport.getInstance().loadReportErrorInfo();
            //初始化搜索关键字高亮用拼音第三方库,在子线程执行,解决MGV2000机顶盒在搜索页搜索时,容易引发ANR(初始化阻塞主线程且耗时较长)
            ChineseUtil.getFirstSpell("初始化拼音工具");//此步骤必须放置在子线程执行,否则可能会造成ANR
            new StartPictureManager().getStartPicture();
        }).start();

        //===========非耗时操作或者已经在子线程中执行的操作===========
    }
}