package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VRPlayerApplictaion;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.LoginRequest;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.LoginResponse;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.LoginRouteResponse;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.ResultBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.UpdateBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.http.BaseResultCall;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.http.HttpConnection;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.util.UpgradeHelper;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.FileUtils;


public class UpdateApk {
    private static String TAG = UpdateApk.class.getSimpleName();
//    public static String upgrade = "http://124.70.66.212:33500/UPGRADE";
    Context context;
    Gson gson = new Gson();

    ProgressDialog progressDialog;

    public UpdateApk(Context context) {
        this.context = context;
    }

    public void startLogin(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("EDS")) {
                LoginRouteRequest(url);
            }else if(url.contains("UPGRADE")){
                upgradeConfig(url);
            }else{
                LoginRequest(url);
            }
        }
    }

    public void LoginRouteRequest(String loginRouteUrl) {
        HttpConnection.postRequest(loginRouteUrl, null, new BaseResultCall() {
            @Override
            public void onSuccess(ResultBean response) {
                super.onSuccess(response);
                try {
                    LoginRouteResponse loginRouteResponse = gson.fromJson(response.getBody(), LoginRouteResponse.class);
                    String LoginUrl = loginRouteResponse.getVspURL() + "/VSP/V3/Login";
                    DebugLog.debug(TAG, LoginUrl);
                    LoginRequest(LoginUrl);
                } catch (Exception e) {
                    DebugLog.error(TAG, "[LoginRouteRequest]" + e.toString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoginRequest(String loginUrl) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceModel("STB_VR");
        HttpConnection.postRequest(loginUrl, loginRequest, new BaseResultCall() {
            @Override
            public void onSuccess(ResultBean response) {
                super.onSuccess(response);
                try {
                    LoginResponse loginResponse = gson.fromJson(response.getBody(), LoginResponse.class);
                    upgradeConfig(loginResponse.getUpgradeDomain());
                } catch (Exception e) {
                    DebugLog.error(TAG, "[LoginRequest]" + e.toString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upgradeConfig(String url){
            UpgradeHelper.setUpgradeDelegate(new UpdateIUpgrade());
        UpgradeHelper.startUpgrade(url);
    }

    /**
     * ???????????????????????????????????????????????????????????????  dialog??????
     *
     * @param version
     * @param filename
     */
    public void showUpdateAskDialog(String version, final String filename) {
        AlertDialog.Builder askDialogBuilder = new AlertDialog.Builder(context);
        askDialogBuilder.setCancelable(false);
        askDialogBuilder.setMessage("????????????????????????????");
        askDialogBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showApkDownloadDialog();
                UpgradeHelper.setUpgradeDelegate(new UpdateIUpgrade());
                UpgradeHelper.downloadApk(filename);

            }
        });
        askDialogBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        askDialogBuilder.show();

    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param version
     * @param filename
     */
    public void showForceUpdateDialog(String version, final String filename) {
        AlertDialog.Builder forceDialogBuilder = new AlertDialog.Builder(context);
        forceDialogBuilder.setCancelable(false);
        forceDialogBuilder.setMessage("????????????????????????????");
        forceDialogBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showApkDownloadDialog();
                UpgradeHelper.setUpgradeDelegate(new UpdateIUpgrade());
                UpgradeHelper.downloadApk(filename);

            }
        });
        forceDialogBuilder.show();
    }


    /**
     * ?????????????????????dialog
     */
    public void showApkDownloadDialog() {
        DebugLog.debug(TAG,"showApkDownloadDialog");
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("?????????????????????...");//???????????????
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);//???????????????????????????
        progressDialog.show();//????????????
    }

    /**
     * ?????????????????????
     *
     * @param total
     * @param downloadSize
     */
    public void updateApkDownloadProgress(long total, long downloadSize) {
        if (progressDialog != null && total > 0 && downloadSize > 0) {
            int progress = (int) ((((double) downloadSize / (double) total)) * 100);
            if (progress <= 100) {
                progressDialog.setProgress(progress);
            }
        }
    }

    public void dismissApkProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public class UpdateIUpgrade implements UpgradeHelper.IUpgrade {
        private long lastTime = 0;
        private long nowTime = 0;

        @Override
        public void onConfigFileDownloadFinished(String filePath) {
            DebugLog.debug(TAG, "[onConfigFileDownloadFinished] " + filePath);
            if (!TextUtils.isEmpty(filePath)) {
                //?????????????????????
                String responseBody = FileUtils.getFromSDCard(filePath);
                if (!TextUtils.isEmpty(responseBody)) {
                    DebugLog.debug(TAG, "[ConfigInfo]" + responseBody);
                    UpdateBean dataBean = UpdateBean.getDataBean(responseBody);
                    VRPlayerApplictaion.getInstance().setUpdateBean(dataBean);
                    //??????????????????????????????????????????????????????
                    if (dataBean != null && dataBean.hasUpdate()) {
                        if (dataBean.isForceUpdate()) {
                            showForceUpdateDialog(dataBean.getVersion(), dataBean.getFileName());
                        } else {
                            showUpdateAskDialog(dataBean.getVersion(), dataBean.getFileName());
                        }
                    } else {
                        DebugLog.debug(TAG, "????????????????????????");
                    }
                } else {
                    DebugLog.error(TAG, "config is empty");
                }
            } else {
                DebugLog.error(TAG, "config file path is empty");
            }
        }

        @Override
        public void onApkDownloadFinished(String var1) {
            if (var1 != null) {
                UpgradeHelper.installApk(var1);
            }
            dismissApkProgress();
        }

        @Override
        public void onDownloadProgress(long totalSize, long downloadSize) {
            if(lastTime == 0){
                lastTime = nowTime = System.currentTimeMillis();
            }else{
                nowTime = System.currentTimeMillis();
            }
            if(nowTime - lastTime >= 200){
                DebugLog.debug(TAG,"totalSize:" + totalSize + "  downloadSize:" + downloadSize);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateApkDownloadProgress(totalSize, downloadSize);
                    }
                }).start();

                lastTime = nowTime;
            }
        }


        @Override
        public void onRequestException(String log) {
            if (progressDialog != null) {
                dismissApkProgress();
            }
            Toast.makeText(context, "????????????!", Toast.LENGTH_SHORT).show();
        }
    }
}
