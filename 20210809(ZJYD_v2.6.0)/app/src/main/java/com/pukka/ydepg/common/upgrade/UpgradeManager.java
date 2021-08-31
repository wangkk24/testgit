package com.pukka.ydepg.common.upgrade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.istv.ystframework.client.InstallClient;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6request.UpgradeRequest;
import com.pukka.ydepg.common.report.ubd.scene.UBDUpgrade;
import com.pukka.ydepg.common.upgrade.data.UpgradeConfig;
import com.pukka.ydepg.common.upgrade.download.DownloaderFactory;
import com.pukka.ydepg.common.upgrade.download.IDownloadListener;
import com.pukka.ydepg.common.upgrade.ui.ConfirmSelfDialog;
import com.pukka.ydepg.common.upgrade.ui.DownloadDialog;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.DeleteUtil;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.ThreadManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import static com.pukka.ydepg.common.http.interceptor.LogInterceptor.UTF8;

/*
 *  下载所必要的权限
 *  <uses-permission android:name="android.permission.INTERNET" />
 *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 */
public class UpgradeManager {

    private static final String TAG = UpgradeRequest.class.getSimpleName();

    private static final String KEY_UPGRADE_FILE = "upgrade_apk_name";

    public static final String COMMON_TAG = "Upgrade";

    private Activity context;

    //升级服务器要求HTTP请求头域Content-Type设置为此值,否则接口不通
    public final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private UpgradeConfig config;

    private IUpgradeListener ul;

    private DownloadDialog dialog;

    private static boolean hasUpgraded = false;

    private interface STRATEGY {
        String MANDATORY = "1"; //强制升级
        String OPTIONAL  = "0"; //非强制升级
    }

    private interface Reason {
        String GET_CONFIG_URL_FAILED     = "Get upgrade config server url failed";
        String GET_CONFIG_URL_EXCEPTION  = "Get upgrade config server url exception";
        String GET_CONFIG_FILE_FAILED    = "Get upgrade config file failed";
        String GET_CONFIG_FILE_EXCEPTION = "Get upgrade config file exception";
        String USER_CANCEL               = "User cancel";
        String LOCAL_VERSION_NEW         = "It's the latest version";
        String GET_DOWNLOAD_URL_FAILED   = "Get download url failed";
        String DOWNLOAD_FAILED           = "Download failed";
        String INSTALL_FAILED            = "Install failed";
        String INSTALL_EXCEPTION         = "Install exception";
    }

    private UpgradeManager(Activity context,IUpgradeListener ul){
        this.context = context;
        this.ul = ul;
    }

    public static void upgrade(Activity context, IUpgradeListener ul){
//        //测试代码跳过开机升级
//        if(context instanceof MainActivity){
//            ul.onFinish();
//            return;
//        }

//        //测试代码
//        ConfirmSelfDialog.show(context, new ConfirmSelfDialog.ConfirmSelfDialogListener() {
//            @Override
//            public void onClickOk() {
//                //非强制升级流程用户选择升级,进入下载流程
//                ul.onFinish();
//            }
//
//            @Override
//            public void onClickCancel() {
//                //非强制升级流程用户选择不升级,升级结束
//                ul.onFinish();
//            }
//        });

        SuperLog.info2SD(TAG,"Upgrade flow, hasUpgraded=" + hasUpgraded+"(false=need to upgrade)");
        UpgradeManager um = new UpgradeManager(context,ul);
        if(!hasUpgraded){
            //升级前删除历史升级文件
            um.removeOldApk();
            //启动升级流程
            um.sendUpgradeEdsRequest();
            hasUpgraded = true;
        } else {
            SuperLog.info2SD(TAG,"No need to upgrade. Back to MainActivity and continue");
            um.finishBeforeDownload(null,null,Reason.LOCAL_VERSION_NEW);
        }
    }

    //删除上次更新存储在本地的apk
    private void removeOldApk() {
        //获取上次升级APK的存储路径
        String downloadPath = getUpgradeFilePath();
        SuperLog.info2SD(TAG,"Begin to delete cached upgrade file, PATH=" + downloadPath);
        deleteAllDownloadApk(downloadPath);
    }

    //对接应用商城的升级方法,当前已经废弃,代码保留备用查看
    public static void upgrade(Context context){
        Intent intent = new Intent();
        intent.setAction("com.istv.appStore.INS_LIST_CHANGED");//必填,启动商城升级launcher功能的action
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);//必填
        intent.setPackage("com.istv.appstore");//必填，商城包名
        intent.putExtra("packageName", "com.pukka.ydepg");//必填，要升级的launcher包名
        intent.putExtra("version", CommonUtil.getVersionCode());//必填，要升级的Launcher版本号
        context.sendBroadcast(intent);
    }

    //判断一个地址是否为升级调度服务地址
    public static boolean isUpgradeEdsUrl(String url){
        if(url.contains("EDS/jsp/upgrade.jsp")){
            return true;
        } else {
            return false;
        }
    }

    //判断一个地址是否为升级配置文件服务地址
    public static boolean isUpgradeConfigUrl(String url){
        return url.contains("UPGRADE/jsp/upgrade.jsp");
    }

    private String getUpgradeEdsServerUrl(){
        //TODO 当前写死
        return "http://aikanlive.miguvideo.com:8082/EDS/jsp/upgrade.jsp";
    }

    //向升级调度服务器发送请求,获取升级配置服务器地址
    @SuppressWarnings("CheckResult")
    private void sendUpgradeEdsRequest(){
        UpgradeRequest request = new UpgradeRequest();
        request.setTYPE(CommonUtil.getDeviceType());
        request.setMAC(CommonUtil.getMac());
        request.setSTBID(CommonUtil.getSTBID());
        request.setUSER(SessionService.getInstance().getSession().getUserId());
        request.setVER(String.valueOf(CommonUtil.getVersionCode()));

        //不设置RedirectType默认采用302方式重定向
        request.setRedirectType("1");//1：采用200方式重定向，body中携带URL

        HttpApi.getInstance().getService().upgrade(getUpgradeEdsServerUrl(),request.getRequest())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                responseBody -> {
                    SuperLog.info2SD(TAG,"Get upgrade [EDS] response successfully.");
                    //升级配置文件服务器地址即升级文件服务器地址
                    String configServerUrl = getUpgradeServerUrl(responseBody);
                    if(TextUtils.isEmpty(configServerUrl)){
                        SuperLog.error(TAG,"Get upgrade config server URL failed. Upgrade operation will not be executed.");
                        //获取升级配置文件服务器地址失败,升级结束
                        SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : Get upgrade config server url failed.");
                        finishBeforeDownload(null,null,Reason.GET_CONFIG_URL_FAILED);
                    } else {
                        sendUpgradeConfigRequest(configServerUrl);
                    }
                },
                throwable -> {
                    SuperLog.error(TAG,throwable);
                    //获取升级配置文件服务器地址失败,升级结束
                    SuperLog.error(TAG,"Upgrade operation finished. Reason : Get upgrade config server url exception.");
                    finishBeforeDownload(null,null,Reason.GET_CONFIG_URL_EXCEPTION);
                }
        );
    }

    //获取升级服务器地址(升级配置文件/升级文件都从此地址下载)
    private String getUpgradeServerUrl(ResponseBody responseBody){
        BufferedSource source = responseBody.source();
        return source.getBuffer().clone().readString(UTF8);
    }

    //向升级配置服务器发送请求,获取升级配置文件
    @SuppressWarnings("CheckResult")
    private void sendUpgradeConfigRequest(String configServerUrl){
        UpgradeRequest request = new UpgradeRequest();
        request.setTYPE(CommonUtil.getDeviceType());
        request.setMAC(CommonUtil.getMac());
        request.setSTBID(CommonUtil.getSTBID());
        request.setUSER(SessionService.getInstance().getSession().getUserId());
        request.setVER(String.valueOf(CommonUtil.getVersionCode()));

        HttpApi.getInstance().getService().upgrade(configServerUrl,request.getRequest())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                responseBody -> {
                    config = getUpgradeConfig(responseBody);
                    if(isConfigValid(config)){
                        //经过格式确认的config对象一定可以获取到下载地址,不存在为空情况
                        String downloadUrl = getDownloadUrl(config,configServerUrl);
                        onGetUpgradeConfig(downloadUrl);
                    } else {
                        SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : Upgrade config file is invalid");
                        finishBeforeDownload(null,null,Reason.GET_CONFIG_FILE_FAILED);
                    }
                },
                throwable -> {
                    SuperLog.error(TAG,throwable);
                    //获取升级配置文件失败,升级结束
                    SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : Get upgrade config file exception.");
                    finishBeforeDownload(null,null,Reason.GET_CONFIG_FILE_EXCEPTION);
                }
        );
    }

    //判断是否需要升级
    private boolean isServerVersionNew(UpgradeConfig upgradeConfig){
        try{
            int localVersion = CommonUtil.getVersionCode();

            //优先判断[上次强制升级版本号]
            if(!TextUtils.isEmpty(upgradeConfig.getLastForceUpdateVersion())){
                int lastForceVersion = Integer.parseInt(upgradeConfig.getLastForceUpdateVersion());
                if(localVersion<lastForceVersion){
                    //本地版本低于[上次强制升级版本号],进入强制升级流程
                    upgradeConfig.setForceUpgrade(STRATEGY.MANDATORY);
                    return true;
                } else {
                    //如果本地版本高于或等于上次强制升级版本,再判断是否低于[升级版本号]
                    if(TextUtils.isEmpty(upgradeConfig.getVersion())) {
                        //[需升级版本号]为空,不需要升级
                        return false;
                    } else {
                        int upgradeVersion = Integer.parseInt(upgradeConfig.getVersion());
                        if(localVersion < upgradeVersion ){
                            //如果本地版本低于[升级版本号],进入强制升级流程
                            upgradeConfig.setForceUpgrade(STRATEGY.OPTIONAL);
                            return true;
                        } else {
                            //如果本地版本高于或等于[升级版本号],不需要升级
                            return false;
                        }
                    }
                }
            } else {
                //[上次强制升级版本号]不存在,直接判断[升级版本号]
                if(TextUtils.isEmpty(upgradeConfig.getVersion())) {
                    //[升级版本号]为空,不需要升级
                    return false;
                } else {
                    int upgradeVersion = Integer.parseInt(upgradeConfig.getVersion());
                    if(localVersion < upgradeVersion ){
                        //如果本地版本低于[上次强制升级版本号],进入强制升级流程
                        upgradeConfig.setForceUpgrade(STRATEGY.OPTIONAL);
                        return true;
                    } else {
                        //如果本地版本高于或等于[上次强制升级版本号],不需要升级
                        return false;
                    }
                }
            }
        } catch (Exception e){
            SuperLog.error(TAG,"Get Upgrade version failed, no need to upgrade.");
            SuperLog.error(TAG,e);
            return false;
        }
    }

    private void onGetUpgradeConfig(String downloadUrl){
        SuperLog.info2SD(TAG,"Get upgrade [Config] response successfully.");
        if(isServerVersionNew(config)){
            if( STRATEGY.OPTIONAL.equals(config.getForceUpgrade()) ) {
                ul.onOptionalUpgrade();
                //升级策略为非强制升级,弹出提示框由用户决定是否升级
                ConfirmSelfDialog.show(context, new ConfirmSelfDialog.ConfirmSelfDialogListener() {
                    @Override
                    public void onClickOk() {
                        //非强制升级流程用户选择升级,进入下载流程
                        downloadUpgradeApk(downloadUrl);
                    }

                    @Override
                    public void onClickCancel() {
                        //非强制升级流程用户选择不升级,升级结束
                        SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : User cancel.");
                        finishBeforeDownload(config.getVersion(),STRATEGY.OPTIONAL,Reason.USER_CANCEL);
                    }
                });
            } else {
                //升级策略为强制升级,直接开始下载升级文件
                downloadUpgradeApk(downloadUrl);
            }
        } else {
            //版本号判断为不需要更新,不需要上报UBD升级事件
            SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : There is no new version apk.");
            ul.onFinish();
            //finishBeforeDownload(config.getVersion(),config.getForceUpgrade(),Reason.LOCAL_VESION_NEW);
        }
    }

    private String getConfigValue(String str,String key){
        String value = null;
        try{
            int startPos = str.indexOf(key) + key.length() + 1;//1是"="的长度，这是所需要的value的第一个字符的位置
            String temp = str.substring(startPos);//移除"key=value"中包括"key="在内的之前所有的内容
            int endPos = temp.indexOf("\n");//移除"key="后的字符串中第一个换行的位置
            value = temp.substring(0,endPos).replaceAll("(\r\n|\r|\n|\n\r)","");
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
        return value;
    }

    //判断是否需要升级
    //升级配置文件字符串样例
    //[UPDATEAPP]
    //Version=380
    //FileName=ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
    //DownloadAddress=http://117.148.130.74:33500/UPGRADE/jsp/ftp/guwangapk
    //desc=固网EPG APK升级测试
    //forceUpgrade=1

    //新设计配置文件
    //[UPDATEAPP]
    //Version=500
    //LastForceUpdateVersion=390
    //FileName=ZJYD_Upgrade_CODE500_20200420_debug.apk
    //DownloadAddress=http://117.148.130.74:33500/UPGRADE/jsp/ftp/guwangapk/ZJYD_Upgrade_CODE500_20200420_debug.apk
    private UpgradeConfig getUpgradeConfig(ResponseBody responseBody){
        String responseString = responseBody.source().getBuffer().clone().readString(UTF8);
        SuperLog.info2SD(TAG,"Config response is as followed:>>>\n\t" + responseString);

        //内部版本测试升级功能
        //responseString = tempGetConfig();

        UpgradeConfig config = new UpgradeConfig();
        config.setVersion(getConfigValue(responseString,"Version"));
        config.setFileName(getConfigValue(responseString,"FileName"));
        config.setDownloadAddress(getConfigValue(responseString,"DownloadAddress"));
        config.setLastForceUpdateVersion(getConfigValue(responseString,"LastForceUpdateVersion"));
        return config;
    }

    private String tempGetConfig(){
        String path = context.getFilesDir().getAbsolutePath() + "/config";
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            //fis = new FileInputStream("d:/a.txt"); // 内容是：abc
            fis = new FileInputStream(path);

            int temp;
            //当temp等于-1时，表示已经到了文件结尾，停止读取
            while ((temp = fis.read()) != -1) {
                sb.append((char) temp);
            }
            System.out.println(sb);
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        } finally {
            try {
                //这种写法，保证了即使遇到异常情况，也会关闭流对象。
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }

        }
        return sb.toString();
    }

    private boolean isConfigValid(UpgradeConfig config){
        //[升级版本号]和[最近一次强制升级版本号]不能同时为空
        if(TextUtils.isEmpty(config.getVersion()) && TextUtils.isEmpty(config.getLastForceUpdateVersion())){
            return false;
        }

        //[下载文件名]和[备用下载地址]不能同时为空
        if(TextUtils.isEmpty(config.getFileName()) && TextUtils.isEmpty(config.getDownloadAddress())){
            return false;
        }
        return true;
    }

    //获取版本升级文件下载地址
    private String getDownloadUrl(UpgradeConfig upgradeConfig,String upgradeUrl){
        String url = null;
        if(!TextUtils.isEmpty(upgradeConfig.getFileName())){
            //fileName不为空，使用 [升级服务器地址] + [FileName]d作为下载地址
            url = upgradeUrl;
        }

        else if (TextUtils.isEmpty(upgradeConfig.getFileName()) && !TextUtils.isEmpty(upgradeConfig.getDownloadAddress())){
            //fileName为空，downloadAddress不为空,  使用 downloadAddress作为下载地址
            url = upgradeConfig.getDownloadAddress();
        }

        else {
            //无法获取下载地址
            SuperLog.error(TAG,"Get Upgrade download address failed, no need to upgrade.");
        }
        return url;
    }

    //下载版本升级文件,下载完成后会回调本类中MyDownloadControl的onComplete方法
    private void downloadUpgradeApk(String downloadUrl){
        SuperLog.info2SD(TAG,"Begin to download upgrade apk, URL="+downloadUrl);
        dialog = DownloadDialog.buildDialog(context);
        dialog.show();
        UpgradeRequest request = new UpgradeRequest();
        request.setTYPE(CommonUtil.getDeviceType());
        request.setFILENAME(config.getFileName());
        DownloaderFactory.getDownloader(DownloaderFactory.SELF,new DownloadCompleteListener(),context).download(downloadUrl,request);
    }

    private class DownloadCompleteListener implements IDownloadListener {
        //file格式:/storage/emulated/0/Download/ZJYD/ZJYD.apk
        @Override
        public void onComplete(String file) {
            //可能需要延时启动更新,保证用户能看到下载完成界面
            SuperLog.info2SD(TAG,"Begin to install upgrade apk ["+file+"]");

            //下载成功无需关闭下载进度对话框,安装完成后应用会重启
            dialog.close(false);

            //记录升级文件名,用于升级后下次启动应用时删除文件,务必在下载完成时记录,因此如果有重名文件会被自动改名
            saveUpgradeFilePath(file);
            //安装新版本
            install(file);
        }

        @Override
        public void onFail() {
            //下载新版本失败,升级结束
            SuperLog.info2SD(TAG,"Upgrade operation finished. Reason : Download new version package failed.");
            finishAfterDownloadInMainThread(Reason.DOWNLOAD_FAILED);
        }

        @Override
        public void onProgress(int progress,int max){
            //dialog.updateProgress(progress);
            dialog.updateProgress2(progress,context);
        }
    }

    private void install(String file){
        //installApk方法中有耗时操作,需放子线程执行
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //执行易视腾提供的静默升级方法,升级完成后应用会自动重启
                try{
                    if(!InstallClient.getInstance(context).installApk(file)){
                        //升级失败
                        SuperLog.error(TAG,"Upgrade operation finished. Reason : Install new version package failed.");
                        finishAfterDownloadInMainThread(Reason.INSTALL_FAILED);
                    }
                } catch (Exception e){
                    SuperLog.error(TAG,e);
                    InstallClient.getInstance(context).unbindServiceImpl();
                    //升级失败
                    SuperLog.error(TAG,"Upgrade operation finished. Reason : Install new version package exception.");
                    finishAfterDownloadInMainThread(Reason.INSTALL_EXCEPTION);
                }
            }
        });
    }

    //保存升级文件路径,用于删除下载并安装完成的升级文件
    //输入:/storage/emulated/0/Download/ZJYD/ZJYD.apk
    //保存:/storage/emulated/0/Download/ZJYD
    private void saveUpgradeFilePath(String url){
        String path = null;
        try{
            int pos = url.lastIndexOf("/");
            if( 0 == pos ){
                path = "/";
            } else {
                path = url.substring(0,pos);
            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
        } finally {
            //路径名保存在文件中,供下次启动时调用删除下载文件
            SuperLog.info2SD(TAG,"Download file path : " + path);
            SharedPreferenceUtil.getInstance().putString(KEY_UPGRADE_FILE,path);
        }
    }

    //获取升级文件路径,用于删除下载并安装完成的升级文件
    private String getUpgradeFilePath(){
        return SharedPreferenceUtil.getStringData(KEY_UPGRADE_FILE,null);
    }


    /** 删除指定目录下全部APK文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private boolean deleteAllDownloadApk(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            return false;
        }
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;

        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            SuperLog.error(TAG, "删除升级文件失败：目录 [" + filePath + "] 不存在!");
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除文件名中包含".apk"和"ZJYD"的子文件
            if (file.isFile() && file.getName().contains(".apk") && file.getName().contains("ZJYD")) {
                flag = DeleteUtil.deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            SuperLog.error(TAG, "删除升级文件失败!");
            return false;
        } else {
            return true;
        }
    }

    private void finishBeforeDownload(String version, String force, String reason){
        UBDUpgrade.record(version,force,reason);
        ul.onFinish();
    }

    private void finishAfterDownloadInMainThread(String reason){
        UBDUpgrade.record(config.getVersion(),config.getForceUpgrade(),reason);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.close(true);
                if(BuildConfig.DEBUG){
                    //UI操作必须在主线程执行
                    Toast.makeText(context,"升级失败",Toast.LENGTH_LONG).show();
                }
                //回调主逻辑,必须在主线程执行
                ul.onFinish();
            }
        });
    }
}