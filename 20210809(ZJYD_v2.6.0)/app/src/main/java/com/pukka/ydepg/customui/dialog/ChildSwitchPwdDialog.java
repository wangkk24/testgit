package com.pukka.ydepg.customui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.QRCode;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyUserAttrRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.scene.UBDSMSVerify;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.mvp.contact.SwitchDialogContact;
import com.pukka.ydepg.launcher.mvp.presenter.SwitchDialogPresenter;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.SwitchDialogUtil;
import com.pukka.ydepg.moudule.children.activity.ParentSetCenterActivity;
import com.pukka.ydepg.moudule.children.addpter.ParentSetCenterDurationAdapter;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.children.view.ParentSetCenterManagerView;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.mytv.ZjYdUniAndPhonePayActivty;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewSecondConfirmationFragment;
import com.pukka.ydepg.moudule.mytv.presenter.AccountManagerPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.AccountManagerDataView;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.presenter.DeblockingEvent;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.pukka.ydepg.moudule.search.presenter.BasePresenter.bindToLifecycle;
import static com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent;

/**
 * 家长中心
 * Dialog
 */
public class ChildSwitchPwdDialog extends Dialog implements View.OnFocusChangeListener, AccountManagerDataView, SwitchDialogContact.SwitchView {
    /* TAG */
    private static final String TAG = ChildSwitchPwdDialog.class.getName();
    //单次时长
    @BindView(R.id.tv_num_one)
    TextViewExt tvNumOne;
    @BindView(R.id.tv_num_two)
    TextViewExt tvNumTwo;
    @BindView(R.id.tv_num_three)
    TextViewExt tvNumThree;
    @BindView(R.id.tv_num_four)
    TextViewExt tvNumFour;
    @BindView(R.id.tv_num_five)
    TextViewExt tvNumFive;
    @BindView(R.id.tv_num_six)
    TextViewExt tvNumSix;
    @BindView(R.id.tv_num_seven)
    TextViewExt tvNumSeven;
    @BindView(R.id.tv_num_eight)
    TextViewExt tvNumEight;
    @BindView(R.id.tv_num_night)
    TextViewExt tvNumNight;
    @BindView(R.id.tv_num_zero)
    TextViewExt tvNumZero;
    @BindView(R.id.tv_num_delete)
    RelativeLayoutExt tvNumDelete;
    @BindView(R.id.im_clear)
    ImageViewExt im_clear;
    @BindView(R.id.tv_clear)
    RelativeLayoutExt tv_clear;
    @BindView(R.id.im_delete)
    ImageViewExt im_delete;

    @BindView(R.id.tv_result_1)
    TextViewExt tv_result_1;
    @BindView(R.id.tv_result_2)
    TextViewExt tv_result_2;
    @BindView(R.id.tv_result_3)
    TextViewExt tv_result_3;
    @BindView(R.id.tv_result_4)
    TextViewExt tv_result_4;

    //错误信息提示
    @BindView(R.id.tv_result_error)
    TextViewExt tv_result_error;

    //布局title
    @BindView(R.id.tv_title)
    TextViewExt tv_title;

    //忘记密码按钮
    @BindView(R.id.tv_forget_pwd)
    TextViewExt tv_forget_pwd;

    //验证码界面
    @BindView(R.id.layout_unlock)
    LinearLayoutExt layoutUnlock;

    //输入密码 设置密码 找回密码界面
    @BindView(R.id.ll_pwd)
    LinearLayoutExt ll_pwd;

    //手机号验证码布局
    @BindView(R.id.ll_code)
    LinearLayoutExt ll_code;
    //手机号
    @BindView(R.id.tv_phone_num)
    TextViewExt tv_phone_num;
    //发送验证码按钮
    @BindView(R.id.tv_send_code)
    TextViewExt tv_send_code;
    //验证输入结果
    @BindView(R.id.tv_code)
    TextViewExt tv_code;

    //二维码界面
    @BindView(R.id.ll_qr_code)
    LinearLayoutExt ll_qr_code;
    //二维码
    @BindView(R.id.rl_qrcode)
    RelativeLayoutExt rl_qrcode;
    //二维码图片
    @BindView(R.id.im_qr_code)
    ImageViewExt im_qr_code;

    //验证成功提示
    @BindView(R.id.rl_check_code_success)
    RelativeLayoutExt rl_check_code_success;

    private StringBuilder sb = new StringBuilder();

    private String firstPwdString;

    //private AccountManagerPresenter mPresenter;
    private SwitchDialogPresenter switchDialogPresenter;

    //密码
    private String pwdValue="";

    /**
     * 是否是从家长中心打开的此弹框
     * =0:不是；=1：是
     * */
    private int isFromParentSetCenter = 0;

    private ParentSetCenterManagerView mParentSetCenterManagerView;

    private String testName = "统一支付办理";

    /**
     * viewType:
     * 1:设置密码
     * 2:输入密码
     * 3:输入新的家长密码
     * 4:展示二维码
     * 5:手机号输入验证码界面
     */
    private int viewType = 2;
    private int viewSubType = 1;

    //算术题成功以后所要跳往的界面————1:普通版EPG；2:简版EPG
    private int onclickType = 0;

    private Context context;

    /**
     * 二维码有效期时间
     */
    private long mValidTime = 900 * 1000L;

    private String mUserID;

    /**
     * 1:家长中心
     * 2:退出儿童模式
     */
    private String typeForToActivity = "";

    private static final String DEFAULT_LINK_URL = "https://app.m.zj.chinamobile" + "" +
            ".com/zjweb/sjyytv4/kuandaiTVThirdPay/index.html";

    public ChildSwitchPwdDialog(@NonNull Context context, int viewType,String pwdValue) {
        super(context, R.style.dialog);
        this.context = context;
        this.pwdValue = pwdValue;
        this.viewType = viewType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置全屏无标题头*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_child_switch_pwd);
        ButterKnife.bind(this);

        /*设置宽度全屏*/
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

        switchDialogPresenter = new SwitchDialogPresenter();
        switchDialogPresenter.attachView(this);

        initView();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        //监听数字键盘用于输入计算结果
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            setCountResult(String.valueOf(RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode)));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //计算题回答成功，跳转界面所需的参数
    private String dataType, actionUrl, actionType, contentId;
    private VOD vod;
    private Map<String, String> extraData;

    public void setIntentData(String dataType, String actionUrl, String actionType, String contentId, VOD vod, Map<String, String> extraData) {
        this.dataType = dataType;
        this.actionUrl = actionUrl;
        this.actionType = actionType;
        this.contentId = contentId;
        this.vod = vod;
        this.extraData = extraData;
    }

    private void initView() {

        tvNumZero.setOnFocusChangeListener(this);
        tvNumOne.setOnFocusChangeListener(this);
        tvNumTwo.setOnFocusChangeListener(this);
        tvNumThree.setOnFocusChangeListener(this);
        tvNumFour.setOnFocusChangeListener(this);
        tvNumFive.setOnFocusChangeListener(this);
        tvNumSix.setOnFocusChangeListener(this);
        tvNumSeven.setOnFocusChangeListener(this);
        tvNumEight.setOnFocusChangeListener(this);
        tvNumNight.setOnFocusChangeListener(this);
        tv_forget_pwd.setOnFocusChangeListener(this);
        tvNumDelete.setOnFocusChangeListener(this);
        tv_clear.setOnFocusChangeListener(this);

        tv_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() == 6) {
                    //TODO 验证验证码
                    //switchDialogPresenter.checkVerifiedCode(s.toString(),tv_phone_num.getText().toString(),context);
                    //switchDialogPresenter.checkClientVerifiedCode(s.toString(),tv_phone_num.getText().toString(),context);
                    UpdateUserRegInfoRequest request = new UpdateUserRegInfoRequest();
                    request.setLoginName(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
                    request.setMsgType(UpdateUserRegInfoRequest.MsgType.RESET_PHONE);
                    request.setVerifiyCode(s.toString());
                    request.setNewValue(tv_phone_num.getText().toString());
                    switchDialogPresenter.updateUserRegInfo(request,context);
                    /*if (Integer.parseInt(s.toString()) == 111111) {
                        checkCodeSuccess();
                    }else{
                        checkCodeFail();
                    }*/
                }
            }
        });

        if (viewType == 1 || viewType == 2 || viewType == 3){
            initViewType123();
        }else if (viewType == 4){
            initViewType4();
        }else if (viewType == 5){
            //手机号输入验证码界面
            initViewType5();
        }

        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //验证码验证成功弹出蒙层时，按返回键先隐藏蒙层，再次按返回键走系统逻辑
                    if (rl_check_code_success.getVisibility() == View.VISIBLE){
                        rl_check_code_success.setVisibility(View.GONE);
                        return true;
                    }
                    return false;
                } else {
                    return false; //默认返回 false
                }
            }
        });

    }

    private void initViewType4() {
        //mPresenter = new AccountManagerPresenter((RxAppCompatActivity)context);
        //mPresenter.setDataView(this);
        //mPresenter.qrCodeAuthenticate();
        //二维码有效期
        String validTime = SessionService.getInstance().getSession().getTerminalConfigurationValue("epg_bill_qrcode_validTime");
        if (!TextUtils.isEmpty(validTime)) {
            mValidTime = Long.parseLong(validTime) * 1000;
        }
        switchDialogPresenter.querySubscriberInfo(new QueryUniInfoRequest(),context);
        //getQr();
        //二维码界面
        ll_qr_code.setVisibility(View.VISIBLE);
        layoutUnlock.setVisibility(View.GONE);
    }
    private void initViewType123() {
        ll_code.setVisibility(View.GONE);
        layoutUnlock.setVisibility(View.VISIBLE);
        ll_pwd.setVisibility(View.VISIBLE);
        sb = new StringBuilder();
        setResultEff();
        tvNumOne.requestFocus();
        if (viewType == 1) {
            tv_title.setText(context.getResources().getString(R.string.set_pwd));
            tv_forget_pwd.setVisibility(View.GONE);

        } else if (viewType == 2) {
            tv_forget_pwd.setVisibility(View.VISIBLE);
            tv_title.setText(context.getResources().getString(R.string.input_pwd_));

        } else if (viewType == 3) {
            tv_title.setText(context.getResources().getString(R.string.set_new_pwd_));
            tv_forget_pwd.setVisibility(View.GONE);
        }
    }
    private void initViewType5(){
        //手机号输入验证码界面
        //tv_phone_num.setText("15757129279");
        viewType = 5;
        switchDialogPresenter = new SwitchDialogPresenter();
        switchDialogPresenter.attachView(this);
        setUpFocus();
        tv_result_error.setText(context.getResources().getString(R.string.input_right_check_code));
        tv_result_error.setVisibility(View.INVISIBLE);
        layoutUnlock.setVisibility(View.VISIBLE);
        ll_code.setVisibility(View.VISIBLE);
        tv_forget_pwd.setVisibility(View.GONE);
        ll_pwd.setVisibility(View.GONE);
        tvNumOne.requestFocus();
    }

    private void setUpFocus() {
        tvNumOne.setNextFocusUpId(R.id.tv_result_error);
        tvNumTwo.setNextFocusUpId(R.id.tv_result_error);
        tvNumThree.setNextFocusUpId(R.id.tv_result_error);
        tvNumFour.setNextFocusUpId(R.id.tv_result_error);
        tv_result_error.setNextFocusUpId(R.id.tv_num_one);
    }

    //先跳往其他界面，再关闭当前Dialog
    private void dismissDialogChild(){
        mHandler.removeMessages(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },200);
    }

    //计算题解锁界面成功后所要跳往的界面
    public void setTypeForToActivity(String typeForToActivity) {
        this.typeForToActivity = typeForToActivity;
    }

    public void setOnclickType(int onclickType) {
        this.onclickType = onclickType;
    }

    public void setIsFromParentSetCenter(int isFromParentSetCenter) {
        this.isFromParentSetCenter = isFromParentSetCenter;
    }

    public void setParentSetCenterManagerView(ParentSetCenterManagerView parentSetCenterManagerView) {
        this.mParentSetCenterManagerView = parentSetCenterManagerView;
    }

    //输入结果
    @SuppressLint("SetTextI18n")
    private void setCountResult(String value) {
        if (viewType == 5){
            //输入验证码界面
            if (sb.length() < 6) {
                tv_result_error.setVisibility(View.INVISIBLE);
                sb.append(value);
                tv_code.setText(sb.toString());
            }
        }else if (viewSubType == 1 || viewSubType == 2 || viewSubType == 3){
            if (sb.length() == 3){
                sb.append(value);
                //==1 设置密码
                if (viewType == 1){
                    if (viewSubType == 1){
                        //设置密码
                        viewSubType = 2;
                        tv_title.setText(context.getResources().getString(R.string.set_pwd_confirm));
                        firstPwdString = sb.toString();
                        tvNumOne.requestFocus();
                        sb = new StringBuilder();
                        setResultEff();
                    }else{
                        //确认设置的密码
                        if (firstPwdString.equalsIgnoreCase(sb.toString())){
                            //TODO:两次密码一致，设置密码
                            setPwd();
                            //Toast.makeText(context,"设置密码",Toast.LENGTH_LONG).show();
                        }else{
                            //两次密码不一致
                            //viewSubType = 1;
                            //firstPwdString = "";
                            //tv_title.setText(context.getResources().getString(R.string.set_pwd));
                            //tvNumOne.requestFocus();
                            sb = new StringBuilder();
                            setResultEff();
                            tv_result_error.setVisibility(View.VISIBLE);
                            tv_result_error.setText(context.getResources().getString(R.string.pwd_error));

                        }

                    }
                }else if (viewType == 2) {
                    //输入密码
                    if (pwdValue.equalsIgnoreCase(sb.toString())) {
                        //TODO:输入密码正确，进行跳转
                        pwdIsRightAndToDo();
                        /*Toast.makeText(context,"跳转",Toast.LENGTH_LONG).show();
                        dismissDialogChild();*/
                    } else {
                        //密码不对
                        tvNumOne.requestFocus();
                        sb = new StringBuilder();
                        setResultEff();
                        tv_result_error.setVisibility(View.VISIBLE);
                        tv_result_error.setText(context.getResources().getString(R.string.pwd_error_hint));

                    }
                }else if (viewType == 3) {
                    //输入新的家长密码
                    if (viewSubType == 1){
                        //设置密码
                        viewSubType = 2;
                        tv_title.setText(context.getResources().getString(R.string.set_new_pwd_confirm));
                        firstPwdString = sb.toString();
                        tvNumOne.requestFocus();
                        sb = new StringBuilder();
                        setResultEff();
                    }else{
                        //确认设置的密码
                        if (firstPwdString.equalsIgnoreCase(sb.toString())){
                            //TODO:两次密码一致，修改密码
                            setPwd();
                        }else{
                            //两次密码不一致
                            //viewSubType = 1;
                            //firstPwdString = "";
                            //tv_title.setText(context.getResources().getString(R.string.set_new_pwd_));
                            //tvNumOne.requestFocus();
                            sb = new StringBuilder();
                            setResultEff();
                            tv_result_error.setVisibility(View.VISIBLE);
                            tv_result_error.setText(context.getResources().getString(R.string.pwd_error));
                        }
                    }
                }
            }else if (sb.length() < 3){
                setErrorHintVis(false);
                sb.append(value);
                setResultEff();
            }
        }
    }

    //输入密码正确，进行后续操作
    private void pwdIsRightAndToDo(){
        if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_TO_PARENT)) {
            ZJVRoute.route(context, dataType, actionUrl,
                    actionType, contentId, vod, extraData);
            dismissDialogChild();
        } else if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN)
                ||typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_EPG)) {//计算结果成功,切换-1屏
            if (onclickType == 1) {
                switchToNormalEpg();
            } else if (onclickType == 2) {
                switchToSimpleEpg();
            } else {
                dismissDialogChild();
            }
        } else if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.VOD_DETAIL_SWITCH_PARENT)) {//voddetailtoParentcenter
            //isOpenParentControl=true;
            Intent intent = new Intent(context, ParentSetCenterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
            dismissDialogChild();
        }
    }

    @OnClick({R.id.tv_num_one, R.id.tv_num_two, R.id.tv_num_three, R.id.tv_num_four, R.id.tv_num_five, R.id.tv_num_six, R.id.tv_num_seven
            , R.id.tv_num_eight, R.id.tv_num_night, R.id.tv_num_zero, R.id.tv_num_delete,R.id.tv_clear,R.id.tv_send_code,R.id.tv_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_num_one:
                setCountResult(tvNumOne.getText().toString());
                break;
            case R.id.tv_num_two:
                setCountResult(tvNumTwo.getText().toString());
                break;
            case R.id.tv_num_three:
                setCountResult(tvNumThree.getText().toString());
                break;
            case R.id.tv_num_four:
                setCountResult(tvNumFour.getText().toString());
                break;
            case R.id.tv_num_five:
                setCountResult(tvNumFive.getText().toString());
                break;
            case R.id.tv_num_six:
                setCountResult(tvNumSix.getText().toString());
                break;
            case R.id.tv_num_seven:
                setCountResult(tvNumSeven.getText().toString());
                break;
            case R.id.tv_num_eight:
                setCountResult(tvNumEight.getText().toString());
                break;
            case R.id.tv_num_night:
                setCountResult(tvNumNight.getText().toString());
                break;
            case R.id.tv_num_zero:
                setCountResult(tvNumZero.getText().toString());
                break;
            case R.id.tv_send_code:
                //TODO 发送验证码
                countDown(60);
                //sendCode();
                break;
            case R.id.tv_num_delete:
                if (sb.length() > 0) {
                    String subString = sb.toString().substring(0, sb.length() - 1);
                    sb = new StringBuilder();
                    sb.append(subString);
                    if (viewType == 5) {
                        tv_code.setText(subString);
                    } else if (viewType == 4) {

                    } else {
                        setResultEff();
                    }
                }
                setErrorHintVis(false);
                break;
            case R.id.tv_clear:
                sb = new StringBuilder();
                setErrorHintVis(false);
                if (viewType == 5) {
                    tv_code.setText("");
                } else if (viewType == 4) {

                } else {
                    setResultEff();
                }
                break;
            case R.id.tv_forget_pwd:
                switchDialogPresenter.queryUniPayInfo(new QueryUniInfoRequest(),context);
                //initViewType5();
                break;
        }
    }

    private void setResultEff(){
        if (sb.length() > 0){
            if (sb.length() == 1){
                tv_result_1.setSelected(true);
                tv_result_2.setSelected(false);
                tv_result_3.setSelected(false);
                tv_result_4.setSelected(false);
            }else if (sb.length() == 2){
                tv_result_1.setSelected(true);
                tv_result_2.setSelected(true);
                tv_result_3.setSelected(false);
                tv_result_4.setSelected(false);
            }else if (sb.length() == 3){
                tv_result_1.setSelected(true);
                tv_result_2.setSelected(true);
                tv_result_3.setSelected(true);
                tv_result_4.setSelected(false);
            }else if (sb.length() == 4){
                tv_result_1.setSelected(true);
                tv_result_2.setSelected(true);
                tv_result_3.setSelected(true);
                tv_result_4.setSelected(true);
            }
        }else{
            tv_result_1.setSelected(false);
            tv_result_2.setSelected(false);
            tv_result_3.setSelected(false);
            tv_result_4.setSelected(false);
        }
    }

    private void setErrorHintVis(boolean vis){
        if (vis){
            if (tv_result_error.getVisibility() == View.INVISIBLE){
                tv_result_error.setVisibility(View.VISIBLE);
            }
        }else{
            if (tv_result_error.getVisibility() == View.VISIBLE){
                tv_result_error.setVisibility(View.INVISIBLE);
            }
        }
    }

    //切换到普通版EPG
    private void switchToNormalEpg() {
        if ( context instanceof MainActivity ){
            ((MainActivity) context).switchLauncher(Constant.DesktopType.NORMAL);
        }
        dismissDialogChild();
    }

    //切换到简版EPG
    private void switchToSimpleEpg() {
        if ( context instanceof MainActivity ) {
            ((MainActivity) context).switchLauncher(Constant.DesktopType.SIMPLE);
        }
        dismissDialogChild();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.tv_forget_pwd){
            if (hasFocus){
                tv_forget_pwd.setTextColor(ContextCompat.getColor(context, R.color.color_5EA7FB));
            }else{
                tv_forget_pwd.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_zero){
            if (hasFocus){
                tvNumZero.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumZero.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_one){
            if (hasFocus){
                tvNumOne.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumOne.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_two){
            if (hasFocus){
                tvNumTwo.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumTwo.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_three){
            if (hasFocus){
                tvNumThree.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumThree.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_four){
            if (hasFocus){
                tvNumFour.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumFour.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_five){
            if (hasFocus){
                tvNumFive.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumFive.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_six){
            if (hasFocus){
                tvNumSix.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumSix.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_seven){
            if (hasFocus){
                tvNumSeven.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumSeven.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_eight){
            if (hasFocus){
                tvNumEight.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumEight.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_num_night){
            if (hasFocus){
                tvNumNight.setTextColor(ContextCompat.getColor(context, R.color.white_0));
            }else{
                tvNumNight.setTextColor(ContextCompat.getColor(context, R.color.black_0));
            }
        }
        if (v.getId() == R.id.tv_clear){
            if (hasFocus){
                im_clear.setImageResource(R.drawable.parent_pwd_clear_focus);
            }else{
                im_clear.setImageResource(R.drawable.parent_pwd_clear);
            }
        }
        if (v.getId() == R.id.tv_num_delete){
            if (hasFocus){
                im_delete.setImageResource(R.drawable.parent_pwd_del_focus);
            }else{
                im_delete.setImageResource(R.drawable.parent_pwd_del);
            }
        }
    }

    @Override
    public void qrCodeCallBackSuccess(QrCodeAuthenticateResponse authenticateResponse) {
        if (null != authenticateResponse) {
            ViewGroup.LayoutParams layoutParams = im_qr_code.getLayoutParams();
            int width = 0,height = 0;
            if (layoutParams.width > 0 && layoutParams.height > 0){
                width = layoutParams.width;
                height = layoutParams.height;
            }else if (im_qr_code.getMeasuredWidth() > 0 && im_qr_code.getMeasuredHeight() > 0){
                width = im_qr_code.getMeasuredWidth();
                height = im_qr_code.getMeasuredHeight();
            }else if (im_qr_code.getHeight() > 0 && im_qr_code.getWidth() > 0){
                width = im_qr_code.getWidth();
                height = im_qr_code.getHeight();
            }
            Bitmap bitmap = QRCodeGenerator.genQrCode(authenticateResponse.getQrCode(), width,height);
            if (null != bitmap) {
                im_qr_code.setImageBitmap(bitmap);
            }
        }
    }

    //设置/修改密码
    private void setPwd(){
        ModifyUserAttrRequest request = new ModifyUserAttrRequest();
        QueryUserAttrsResponse.UserAttr modifyAttr = new QueryUserAttrsResponse.UserAttr();
        modifyAttr.setAttrName("parentsPwd");
        modifyAttr.setAttrValue(firstPwdString);
        request.setUserId(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
        request.setActionType("0");
        request.setModifyAttr(modifyAttr);
        switchDialogPresenter.modifyUserAttr(request,context);
    }

    //验证码验证成功
    private void checkCodeSuccess(){
        rl_check_code_success.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_check_code_success.setVisibility(View.GONE);
                sb = new StringBuilder();
                viewType = 3;
                initViewType123();
            }
        },1500);
    }
    //验证码验证失败
    private void checkCodeFail(){
        sb = new StringBuilder();
        setErrorHintVis(true);
        tv_code.setText("");
    }

    private void sendCode(){
        //switchDialogPresenter.sendVerifiedCode(tv_phone_num.getText().toString(),context);
        //switchDialogPresenter.sendBatchSendSms(tv_phone_num.getText().toString(),context);
        SendSmsRequest request = new SendSmsRequest();
        request.setLoginName(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
        request.setDestMobilePhone(tv_phone_num.getText().toString());
        request.setMsgType(SendSmsRequest.MsgType.SMS_RESET_PHONE_CODE);
        switchDialogPresenter.sendSMS(request,context);
    }
    private int count_time = 60;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                count_time--;
                if (count_time != 0) {
                    tv_send_code.setText(String.format("重新发送(%1$s)",
                            String.valueOf(count_time)));
                    sendEmptyMessageDelayed(1, 1000);
                } else {
                    tv_send_code.setText("重新发送");
                    tv_send_code.setFocusable(true);//焦点可以获取
                    removeMessages(1);
                }
            }else if (msg.what == 2){
                getQr(mUserID,testName);
                mHandler.sendEmptyMessageDelayed(2,mValidTime);
            }

        }
    };

    public void countDown(int time) {
        count_time = time;
        tvNumOne.requestFocus();
        tv_send_code.setFocusable(false);
        tv_send_code.setText(String.format("重新发送(%1$s)", String.valueOf(time)));

        //TODO 发送验证码
        sendCode();

        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void getQr(String userId, String productName) {

        SuperLog.info2SD(TAG, "generateQrCode");
        Session session = SessionService.getInstance().getSession();
        String qrCodeUrl = session.getTerminalConfigurationValue("epg_bill_qrcode_url");
        if (TextUtils.isEmpty(qrCodeUrl)) {
            qrCodeUrl = DEFAULT_LINK_URL;
        }
        String deviceId = session.getDeviceId();
        long timestamp = System.currentTimeMillis();
        String orderID = deviceId + timestamp;
        String keyContent = userId + productName+ orderID + "4" + timestamp + "ZJMobile!15112017@OTT";
        String key = sha256(keyContent);
        String qrcodeContent = qrCodeUrl + "?USERID=" + userId + "&PROD=" + productName
                + "&ORDID=" + orderID + "&TIME=" + timestamp + "&KEY=" + key + "&PLAT=4";

        int width = OTTApplication.getContext().getResources()
                .getDimensionPixelSize(R.dimen.margin_350);
        Bitmap bitmap = QRCodeGenerator.genQrCode(qrcodeContent, width, width);
        im_qr_code.setImageBitmap(bitmap);
    }

    private String sha256(final String plainText) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plainText.getBytes("UTF-8"));
            byte byteBuffer[] = messageDigest.digest();

            StringBuilder strHexString = new StringBuilder();
            for (byte aByteBuffer : byteBuffer) {
                String hex = Integer.toHexString(0xff & aByteBuffer);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            return strHexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            SuperLog.error(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    public void qrCodeCallBackFailed() {
        rl_qrcode.setVisibility(View.GONE);
    }

    @Override
    public void queryQrCodeStatusSuccess(QueryQrCodeStatusResponse qrCodeStatusResponse) {

    }

    @Override
    public void queryQrCodeStatusFailed(ErrorMessage message) {

    }

    @Override
    public void quitQrCodeSuccess() {

    }

    @Override
    public void quitQrCodeFailed() {

    }

    @Override
    public void qeryBindedSubscriberSuccess(QueryBindedSubscriberResponse queryBindedSubscriberResponse) {

    }

    @Override
    public void qeryBindedSubscriberFailed(ErrorMessage message) {

    }

    @Override
    public void unBindSubsrciberSuccess() {

    }

    @Override
    public void unBindSubsrciberFailed() {

    }

    @Override
    public void checkVerifiedCodeSuccess() {
        checkCodeSuccess();
    }

    @Override
    public void checkVerifiedCodeFail() {
        checkCodeFail();
    }

    @Override
    public void queryUniPayInfoSuccess(QueryUniPayInfoResponse response) {
        UniPayInfo uniPayInfo = SwitchDialogUtil.resolveMainUniPayInfo(response);
        if (null != uniPayInfo) {
            if (TextUtils.isEmpty(uniPayInfo.getBillID())) {
                //统一账号不存在
                initViewType4();
            } else {
                //已开通统一支付手机号
                tv_phone_num.setText(uniPayInfo.getBillID());
                initViewType5();
            }
        }else{
            //未开通统一支付手机号
            initViewType4();
        }

    }

    @Override
    public void queryUniPayInfoFail() {
        //未开通统一支付手机号
        initViewType4();
    }

    @Override
    public void modifyUserAttrSuccess() {
        if (isFromParentSetCenter == 1){
            if (null != mParentSetCenterManagerView){
                mParentSetCenterManagerView.setTvChildPwdText();
            }
            dismissDialogChild();
        }else{
            pwdValue = firstPwdString;
            viewType = 2;
            initViewType123();
        }
    }

    @Override
    public void modifyUserAttrFail() {
        dismissDialogChild();
    }

    @Override
    public void queryUserAttrsSuccess(String pwdValue) {

    }

    @Override
    public void queryUserAttrsFail() {

    }

    @Override
    public void querySubscriberInfoSucc(QuerySubscriberResponse response) {
        List<NamedParameter> customFields = response.getSubscriber().getCustomFields();
        if (!CollectionUtil.isEmpty(customFields)) {
            for (NamedParameter namedParameter : customFields) {
                if (ZjYdUniAndPhonePayActivty.BILL_ID.equals(namedParameter.getKey()) &&
                        !CollectionUtil.isEmpty(namedParameter.getValues())) {
                    mUserID = namedParameter.getValues().get(0);
                    SuperLog.debug(TAG, "userId=" + mUserID);
                    break;
                }
            }
        }
        //生成二维码
        getQr(mUserID,testName);

        //定时刷新二维码
        mHandler.sendEmptyMessageDelayed(2,mValidTime);
    }

    @Override
    public void querySubscriberInfoError() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mHandler){
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
