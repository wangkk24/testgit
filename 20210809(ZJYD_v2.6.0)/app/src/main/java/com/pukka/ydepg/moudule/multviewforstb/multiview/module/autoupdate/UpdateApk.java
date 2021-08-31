package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.LoginRequest;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.LoginResponse;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.LoginRouteResponse;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.UpdateBean;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http.BaseResultCall;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http.HttpConnection;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.util.UpgradeHelper;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.FileUtils;

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
            } else if (url.contains("UPGRADE")) {
                upgradeConfig(url);
            } else {
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
                Toast.makeText(context, context.getString(R.string.network_error_tip), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LoginRequest(String loginUrl) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceModel("STB_ZYSJ");
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
                Toast.makeText(context, context.getString(R.string.network_error_tip), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upgradeConfig(String url) {
        UpgradeHelper.setUpgradeDelegate(new UpdateIUpgrade());
        UpgradeHelper.startUpgrade(url);
    }

    /**
     * 当前不是最新版本，询问是否下载安装最新版本  dialog展示
     *
     * @param version
     * @param filename
     */
    public void showUpdateAskDialog(String version, final String filename) {
        AlertDialog.Builder askDialogBuilder = new AlertDialog.Builder(context);
        askDialogBuilder.setCancelable(false);
        askDialogBuilder.setMessage(context.getString(R.string.update_checkupdate_tip));
        askDialogBuilder.setNegativeButton(context.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showApkDownloadDialog();
                UpgradeHelper.setUpgradeDelegate(new UpdateIUpgrade());
                UpgradeHelper.downloadApk(filename);
            }
        });
        askDialogBuilder.setPositiveButton(context.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        askDialogBuilder.show();

    }

    /**
     * 当前不是最新版本，但必须强制更新强制更新版本
     *
     * @param version
     * @param filename
     */
    public void showForceUpdateDialog(String version, final String filename) {
        AlertDialog.Builder forceDialogBuilder = new AlertDialog.Builder(context);
        forceDialogBuilder.setCancelable(false);
        forceDialogBuilder.setMessage(context.getString(R.string.update_checkupdate_tip));
        forceDialogBuilder.setNegativeButton(context.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
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
     * 展示下载进度的dialog
     */
    public void showApkDownloadDialog() {
        DebugLog.debug(TAG, "showApkDownloadDialog");
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.update_downloading));//对话框消息
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);//对话框是否可以取消
        progressDialog.show();//显示对话
    }

    /**
     * 更新下载进度条
     *
     * @param total
     * @param downloadSize
     */
    public void updateApkDownloadProgress(long total, long downloadSize) {
        if (progressDialog != null && total > 0 && downloadSize > 0) {
            int progress = (int) ((((double) downloadSize / (double) total)) * 100);
            if (progress <= 100) {
                DebugLog.debug(TAG, "testProgress:" + progress);
                progressDialog.setMax(100);
                progressDialog.setProgress(progress);
            }
        }
    }

    public void dismissApkProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private class UpdateIUpgrade implements UpgradeHelper.IUpgrade {
        private long lastTime = 0;
        private long nowTime = 0;

        @Override
        public void onConfigFileDownloadFinished(String filePath) {
            DebugLog.debug(TAG, "[onConfigFileDownloadFinished] " + filePath);
            if (!TextUtils.isEmpty(filePath)) {
                //进行文件的读取
                String responseBody = FileUtils.getFromSDCard(filePath);
                if (!TextUtils.isEmpty(responseBody)) {
                    DebugLog.debug(TAG, "[ConfigInfo]" + responseBody);
                    UpdateBean dataBean = UpdateBean.getDataBean(responseBody);
                    TVApplication.getInstance().setUpdateBean(dataBean);
                    //如果当前不是最新版本，弹出更新请求框
                    if (dataBean != null && dataBean.hasUpdate()) {
                        if (dataBean.isForceUpdate()) {
                            showForceUpdateDialog(dataBean.getVersion(), dataBean.getFileName());
                        } else {
                            showUpdateAskDialog(dataBean.getVersion(), dataBean.getFileName());
                        }
                    } else {
                        DebugLog.debug(TAG, context.getString(R.string.update_tip_last));
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
        public void onDownloadProgress(final long totalSize,final long downloadSize) {
            if (lastTime == 0) {
                lastTime = nowTime = System.currentTimeMillis();
            } else {
                nowTime = System.currentTimeMillis();
            }
            if (nowTime - lastTime >= 200) {
                DebugLog.debug(TAG, "totalSize:" + totalSize + "  downloadSize:" + downloadSize);
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
            Toast.makeText(context, context.getString(R.string.update_file_download_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
