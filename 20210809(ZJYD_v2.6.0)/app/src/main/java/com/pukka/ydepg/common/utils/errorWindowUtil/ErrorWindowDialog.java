package com.pukka.ydepg.common.utils.errorWindowUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.xmpp.utils.Base64;

import static com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils.ERROR_QRCODE_URL;

/*panjw
 * 界面重写*/
public class ErrorWindowDialog extends Dialog {

    private static final String TAG = "ErrorWindowDialog";

    private String errorCode;

    private String errorDesc;
    private String operate;
    private String suggest;

    //=========== text ===========
    //错误信息
    TextViewExt mErrMessage;
    //错误码
    TextViewExt mErrCode;
    //建议
    TextViewExt mSuggest_tip;
    TextViewExt mSuggest;

    //=========== button ===========
    //网络设置
    TextViewExt mNetSetting;
    //重启
    TextViewExt mReboot;
    //返回
    TextViewExt mTvBack;


    public ErrorWindowDialog(@NonNull Context context) {
        super(context);
    }

    public ErrorWindowDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setErrInfo(String errorCode, String errorDesc, String errorType, String suggest) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.operate = errorType;
        this.suggest = suggest;
    }

    protected ErrorWindowDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_error_window);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        initView();
    }

    private void initView() {

        mErrMessage = findViewById(R.id.err_message);
        mErrCode = findViewById(R.id.err_code);
        mReboot = findViewById(R.id.tv_reboot);
        mTvBack = findViewById(R.id.tv_back);

        //如果没有建议则不展示建议
        mSuggest = findViewById(R.id.suggest);
        mSuggest_tip = findViewById(R.id.suggest_tip);
        if (TextUtils.isEmpty(suggest)) {
            mSuggest.setVisibility(View.GONE);
            mSuggest_tip.setVisibility(View.GONE);
        } else {
            mSuggest.setVisibility(View.VISIBLE);
            mSuggest_tip.setVisibility(View.VISIBLE);
        }

        //根据错误类型来判断是否显示网络设置
        mNetSetting = findViewById(R.id.tv_net_setting);
        try {
            if (!TextUtils.isEmpty(operate)) {
                SuperLog.info2SD(TAG, "operate=" + operate);
                String[] type = operate.split(",");
                // 这里逻辑为返回按钮始终显示，枚举值如下(ps：确认目前没有重启功能)
                //  0 返回
                //  1 立即重启
                //  2 网络设置(支持返回多项，用逗号分隔)
                for (String operate : type) {
                    if ("2".equals(operate)) {
                        mNetSetting.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }

        mNetSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.startSettingActivity(getContext());//小米电视适配要求这里启动Setting页面须满足移动设置页面启动规范
                dismiss();
            }
        });
        mNetSetting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mNetSetting.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.white_0));
                } else {
                    mNetSetting.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.mytv_history_downlayout_bg));

                }
            }
        });
        mNetSetting.requestFocus();
        mReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReboot();

            }
        });
        mReboot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mReboot.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.white_0));
                } else {
                    mReboot.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.mytv_history_downlayout_bg));
                }
            }
        });
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvBack.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTvBack.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.white_0));
                } else {
                    mTvBack.setTextColor(OTTApplication.getContext().getResources().getColor(R.color.mytv_history_downlayout_bg));
                }
            }
        });
        mErrMessage.setText(String.format(Strings.getInstance().getString(R.string.err_message), errorDesc));
        mErrCode.setText(String.format(Strings.getInstance().getString(R.string.err_code), errorCode));
        mSuggest.setText(suggest);

        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        String paramsMessage = buildErrorParams(errorCode, errorDesc, userInfo.getIP(), userInfo.getPort(), userInfo.getUserId());
        String url = CommonUtil.getConfigValue(ERROR_QRCODE_URL) + "?params=" + Base64.encode(GZipUtils.compress(paramsMessage));

        ImageView mImage = findViewById(R.id.window_error_image);
        mImage.setImageBitmap(ZXingUtils.createQRImage(url
                , OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.window_error_image_parent_size)
                , OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.window_error_image_parent_size)));
    }

    private void onReboot() {
        Intent reboot = new Intent(Intent.ACTION_REBOOT);
        reboot.putExtra("nowait", 1);
        reboot.putExtra("interval", 1);
        reboot.putExtra("window", 0);
        getContext().sendBroadcast(reboot);
    }


    private String buildErrorParams(String errorCode, String errorDesc, String ip, String port, String userID) {
        String systemModel = DeviceInfo.getSystemInfo(Constant.DEVICE_RAW);
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        ErrorParams errorParams = new ErrorParams();
        errorParams.setApkVersion(BuildConfig.VERSION_NAME);
        errorParams.setIp(ip);
        errorParams.setPort(port);
        errorParams.setBoxModel(systemModel);
        errorParams.setBoxSerial(CommonUtil.getSTBID());
        errorParams.setCode(errorCode);
        errorParams.setDesc(errorDesc);
        errorParams.setLicence("huawei");//根据客户要求写死华为
        String billId = SessionService.getInstance().getSession().getAccountName();
        String broadbandAccount = SessionService.getInstance().getSession().getBillId();
        String systemTime = DateCalendarUtils.convertToUTC(System.currentTimeMillis());
        StringBuilder token = new StringBuilder()
                .append(userID)          //电视账号userId
                .append("/")
                .append(billId)          //电视账号billId
                .append("/")
                .append(broadbandAccount)//家宽账号
                .append("/")
                .append(systemTime);     //系统当前时间(YYYYMMDDHH24MMSS)
        try {
            errorParams.setToken(RSAUtils.publicEncrypt(token.toString(), RSAUtils.getPublicKey()));
        } catch (Exception e) {
            DebugLog.error(TAG, e);
        }
        return errorParams.getParams();
    }
}