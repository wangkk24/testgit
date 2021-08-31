package com.pukka.ydepg.moudule.children.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.EditTextExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.dialog.ChildSwitchPwdDialog;
import com.pukka.ydepg.customui.dialog.ParentSetCenterDialog;
import com.pukka.ydepg.launcher.mvp.contact.SwitchDialogContact;
import com.pukka.ydepg.launcher.mvp.presenter.SwitchDialogPresenter;
import com.pukka.ydepg.launcher.util.SwitchDialogUtil;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.vod.cache.VoddetailUtil;
import com.pukka.ydepg.moudule.vod.presenter.ResetBlockEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eason on 01-Apr-19.
 * 家长设置中心Manage View
 */

public class ParentSetCenterManagerView extends LinearLayoutExt implements View.OnFocusChangeListener , SwitchDialogContact.SwitchView{

    //这些字段改变，必现同时修改ParentControlData类
    public static final String SWITCH = "switch";
    private static final String NAME = "name";
    private static final String GENDER = "gender";
    private static final String BIRTHDAY = "birthday";
    public static final String SINGLETIME = "singletime";
    public  static final String ALLTIME = "alltime";
    private static final String CONTENTCONTROL = "contentcontrol";

    @BindView(R.id.child_luck)
    TextViewExt childLuck;
    //儿童锁开关
    @BindView(R.id.layout_lock_switch)
    RelativeLayoutExt layoutLockSwitch;
    @BindView(R.id.child_luck_switch)
    TextViewExt tvLockSwitch;
    @BindView(R.id.tv_child_name_hint)
    TextViewExt tvChildNameHint;
    //宝贝昵称
    @BindView(R.id.tv_child_name)
    EditTextExt tvChildName;
    //昵称layout
    @BindView(R.id.relative_child_name)
    RelativeLayoutExt layoutChildName;
    @BindView(R.id.tv_child_gender_hint)
    TextViewExt tvChildGenderHint;
    //性别Value
    @BindView(R.id.tv_child_gender)
    TextViewExt tvChildGender;
    //性别
    @BindView(R.id.relative_child_gender)
    RelativeLayoutExt layoutChildGender;
    @BindView(R.id.tv_child_birthday_hint)
    TextViewExt tvChildBirthdayHint;
    //生日Value
    @BindView(R.id.tv_child_birthday)
    TextViewExt tvChildBirthday;
    //选择生日
    @BindView(R.id.relative_child_birthday)
    RelativeLayoutExt layoutChildrenBirthday;
    @BindView(R.id.tv_child_single_time_hint)
    TextViewExt tvChildSingleTimeHint;
    //单次时长
    @BindView(R.id.tv_child_single_time_name)
    TextViewExt tvChildSingleTimeName;
    //选择单次时长
    @BindView(R.id.relative_single_time)
    RelativeLayoutExt layoutSingleTime;
    @BindView(R.id.tv_child_all_time_hint)
    TextViewExt tvChildAllTimeHint;
    //总时长
    @BindView(R.id.tv_child_all_time)
    TextViewExt tvChildAllTime;
    //选择总时长
    @BindView(R.id.relative_child_all_time)
    RelativeLayoutExt layoutChildAllTime;
    @BindView(R.id.tv_child_control_hint)
    TextViewExt tvChildControlHint;
    @BindView(R.id.tv_child_control_content)
    TextViewExt tvChildControlContent;
    @BindView(R.id.relative_child_control)
    RelativeLayoutExt relativeChildControl;

    @BindView(R.id.relative_child_pwd)
    RelativeLayoutExt relativeChildPwd;
    @BindView(R.id.tv_child_pwd_hint)
    TextViewExt tvChildPwdHint;
    @BindView(R.id.tv_child_pwd)
    TextViewExt tvChildPwd;

    //儿童锁开关Flag，1=开；2=关
    private int switchInt = 2;

    //当前页面的数据
    private HashMap<String, String> mDataMap = new HashMap<>();



    private String oldLockState;


    private int  oldSingleTime;

    private int  oldAllTime;

    /*private Drawable drawableLeftWhite = getResources().getDrawable(R.drawable.parent_swithch_left_white);
    private Drawable drawableRightWhite = getResources().getDrawable(R.drawable.parent_swithch_right_white);*/

    private Context mContext;

    private SwitchDialogPresenter presenter;

    public ParentSetCenterManagerView(Context context) {
        super(context);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public ParentSetCenterManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
        loadData();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    private void loadData() {
        presenter = new SwitchDialogPresenter();
        presenter.attachView(this);
        presenter.queryUserAttrs();
    }

    /**
     * viewType:
     * 1:单次时长
     * 2:总时长
     * 3:生日
     * 4:性别
     * 5:切换界面
     */
    @OnClick({R.id.layout_lock_switch, R.id.relative_child_name, R.id.relative_single_time, R.id.relative_child_all_time,
            R.id.relative_child_birthday, R.id.relative_child_gender, R.id.relative_child_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            //儿童锁开关
            case R.id.layout_lock_switch:
                /*if(switchInt == 1){//开
                    switchInt = 2;
                    tvLockSwitch.setText("未开启");
                }else if (switchInt == 2){//关
                    switchInt = 1;
                    tvLockSwitch.setText("已开启");
                }*/
                break;
            //宝贝昵称
            case R.id.relative_child_name:

                break;
            //选择单次时长
            case R.id.relative_single_time:
                dialogSingleTime = new ParentSetCenterDialog(mContext, 1);
                dialogSingleTime.setTextView(tvChildSingleTimeName);
                dialogSingleTime.setTextViewValue(tvChildSingleTimeName.getText().toString());
                dialogSingleTime.show();
                break;
            //选择总时长
            case R.id.relative_child_all_time:
                dialogAllTime = new ParentSetCenterDialog(mContext, 2);
                dialogAllTime.setTextView(tvChildAllTime);
                dialogAllTime.setTextViewValue(tvChildAllTime.getText().toString());
                dialogAllTime.show();
                break;
            //选择生日
            case R.id.relative_child_birthday:
                dialogBirthday = new ParentSetCenterDialog(mContext, 3);
                dialogBirthday.setTextView(tvChildBirthday);
                dialogBirthday.setTextViewValue(tvChildBirthday.getText().toString());
                dialogBirthday.show();
                break;
            //选择性别
            case R.id.relative_child_gender:
                if (null == dialogGender) {
                    dialogGender = new ParentSetCenterDialog(mContext, 4);
                }
                dialogGender.setTextView(tvChildGender);
                dialogGender.setTextViewValue(tvChildGender.getText().toString());
                dialogGender.show();
                break;
            case R.id.relative_child_pwd:
                //TODO 打开设置密码界面
                if (tvChildPwd.getText().toString().equalsIgnoreCase(mContext.getResources().getString(R.string.reset_pwd))) {
                    //先检测是否开通统一支付账号
                    presenter.queryUniPayInfo(new QueryUniInfoRequest(),mContext);
                }else{
                    //设置密码
                    ChildSwitchPwdDialog dialog = new ChildSwitchPwdDialog(mContext, 1, "");
                    dialog.setIsFromParentSetCenter(1);
                    dialog.setParentSetCenterManagerView(this);
                    dialog.show();
                }
                break;
        }
    }

    ParentSetCenterDialog dialogGender, dialogBirthday, dialogSingleTime, dialogAllTime;

    @SuppressLint("SetTextI18n")
    private void initView() {
        //初始化默认数据

        mDataMap = SharedPreferenceUtil.getInstance().getParentCenterData();

        //开关
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(SWITCH))) {
            switchInt = Integer.parseInt(mDataMap.get(SWITCH));
            if (switchInt == 1) {
                tvLockSwitch.setText(mContext.getResources().getString(R.string.parent_switch_on));
            } else {
                tvLockSwitch.setText(mContext.getResources().getString(R.string.parent_switch_off));
            }
        } else {
            tvLockSwitch.setText(mContext.getResources().getString(R.string.parent_switch_off));
        }
        oldLockState=tvLockSwitch.getText().toString();
        //昵称
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(NAME))) {
            tvChildName.setText(mDataMap.get(NAME));
        } else {
            tvChildName.setText(mContext.getResources().getString(R.string.parent_default_name));
        }

        //性别
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(GENDER))) {
            tvChildGender.setText(mDataMap.get(GENDER));
        } else {
            tvChildGender.setText(mContext.getResources().getString(R.string.parent_noset));
        }

        //生日
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(BIRTHDAY))) {
            tvChildBirthday.setText(mDataMap.get(BIRTHDAY));
        } else {
            tvChildBirthday.setText(mContext.getResources().getString(R.string.parent_noset));
        }

        //单次时长
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(SINGLETIME))) {
            tvChildSingleTimeName.setText(ChildrenConstant.mSingleDurationMap.get(mDataMap.get(SINGLETIME)));
            oldSingleTime=Integer.parseInt(mDataMap.get(SINGLETIME));
        } else {
            tvChildSingleTimeName.setText("不限制");
            oldSingleTime=-1;
        }

        //总时长
        if (null != mDataMap && !TextUtils.isEmpty(mDataMap.get(ALLTIME))) {
            tvChildAllTime.setText(ChildrenConstant.mAllDurationMap.get(mDataMap.get(ALLTIME)));
            oldAllTime=Integer.parseInt(mDataMap.get(ALLTIME));
        } else {
            tvChildAllTime.setText("不限制");
            oldAllTime=-1;
        }

        tvChildName.setOnFocusChangeListener(this);
    }

    public void setTvChildPwdText(){
        tvChildPwd.setText(mContext.getResources().getString(R.string.reset_pwd));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.tv_child_name:
                layoutChildName.setSelected(hasFocus);
                break;
        }
    }

    public boolean onKeyDown() {
        //当儿童锁有焦点时，按左右键打开或关闭儿童锁开关
        if (layoutLockSwitch.hasFocus()) {
            if (switchInt == 1) {//开
                switchInt = 2;
                tvLockSwitch.setText(mContext.getResources().getString(R.string.parent_switch_off));
            } else if (switchInt == 2) {//关
                switchInt = 1;
                tvLockSwitch.setText(mContext.getResources().getString(R.string.parent_switch_on));
            }
            return true;
        }
        return false;
    }

    //退出家长中心时，保存当前页面的数据
    public void onDestroy() {
        if (null == mDataMap) {
            mDataMap = new HashMap<>();
        }
        mDataMap.put(SWITCH, String.valueOf(switchInt));
        mDataMap.put(NAME, tvChildName.getText().toString());
        mDataMap.put(GENDER, tvChildGender.getText().toString());
        mDataMap.put(BIRTHDAY, tvChildBirthday.getText().toString());
        mDataMap.put(SINGLETIME, (String)ChildrenConstant.getKey(ChildrenConstant.mSingleDurationMap,tvChildSingleTimeName.getText().toString()));
        mDataMap.put(ALLTIME, (String)ChildrenConstant.getKey(ChildrenConstant.mAllDurationMap,tvChildAllTime.getText().toString()));
        SharedPreferenceUtil.getInstance().setParentCenterData(mDataMap);
        int newSingleTime=Integer.parseInt((String)ChildrenConstant.getKey(ChildrenConstant.mSingleDurationMap,tvChildSingleTimeName.getText().toString()));
        int newAllTime=Integer.parseInt((String)ChildrenConstant.getKey(ChildrenConstant.mAllDurationMap,tvChildAllTime.getText().toString()));
        if(newSingleTime!=oldSingleTime&&(newSingleTime==-1||newSingleTime>oldSingleTime)){
            VoddetailUtil.getInstance().resetSingleTime();
            stopRestTime();
            EventBus.getDefault().post(new ResetBlockEvent(VoddetailUtil.REST_SETTING_SINGLE));
        }
        if(newAllTime!=oldAllTime&&(newAllTime==-1||newAllTime>oldAllTime)){
            VoddetailUtil.getInstance().resetAllTime();
            VoddetailUtil.getInstance().resetSingleTime();
            VoddetailUtil.getInstance().resetEpsiodeTime();
            stopRestTime();
            EventBus.getDefault().post(new ResetBlockEvent(VoddetailUtil.REST_SETTING_TODAY));
        }
        if(!tvLockSwitch.getText().toString().equals(oldLockState)){
            VoddetailUtil.getInstance().resetAllTime();
            VoddetailUtil.getInstance().resetSingleTime();
            VoddetailUtil.getInstance().resetEpsiodeTime();
            stopRestTime();
            EventBus.getDefault().post(new ResetBlockEvent(VoddetailUtil.REST_SETTING_ALL));
        }
    }

    public void stopRestTime(){
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getlockScreenType())&&!VoddetailUtil.REST_BY_EPSIODE.equals(SharedPreferenceUtil.getInstance().getlockScreenType())){
            VoddetailUtil.getInstance().stopRestTime(SharedPreferenceUtil.getInstance().getlockScreenType());
        }
    }

    private void uniPayInfoSuccess(){
        //已开通统一支付账号
        ChildSwitchPwdDialog dialog;
        if (tvChildPwd.getText().toString().equalsIgnoreCase(mContext.getResources().getString(R.string.reset_pwd))) {
            dialog = new ChildSwitchPwdDialog(mContext, 3, "");
        } else {
            dialog = new ChildSwitchPwdDialog(mContext, 1, "");
        }
        dialog.setIsFromParentSetCenter(1);
        dialog.setParentSetCenterManagerView(this);
        dialog.show();
    }
    private void uniPayInfoFail(){
        //未开通统一支付账号
        ChildSwitchPwdDialog dialog = new ChildSwitchPwdDialog(mContext,4,"");
        dialog.setIsFromParentSetCenter(1);
        dialog.setParentSetCenterManagerView(this);
        dialog.show();
    }

    @Override
    public void checkVerifiedCodeSuccess() {

    }

    @Override
    public void checkVerifiedCodeFail() {

    }

    @Override
    public void queryUniPayInfoSuccess(QueryUniPayInfoResponse response) {

        UniPayInfo uniPayInfo = SwitchDialogUtil.resolveMainUniPayInfo(response);
        if (null != uniPayInfo) {
            if (TextUtils.isEmpty(uniPayInfo.getBillID())) {
                //统一账号不存在
                uniPayInfoFail();
            } else {
                //已开通统一支付账号
                uniPayInfoSuccess();
            }
        }else{
            //未开通统一支付手机号
            uniPayInfoFail();
        }

    }

    @Override
    public void queryUniPayInfoFail() {
        //未开通统一支付账号
        uniPayInfoFail();
    }

    @Override
    public void modifyUserAttrSuccess() {

    }

    @Override
    public void modifyUserAttrFail() {

    }

    @Override
    public void queryUserAttrsSuccess(String pwdValue) {
        tvChildPwd.setText(mContext.getResources().getString(R.string.reset_pwd));
    }

    @Override
    public void queryUserAttrsFail() {
        tvChildPwd.setText(mContext.getResources().getString(R.string.no_set_pwd));
    }

    @Override
    public void querySubscriberInfoSucc(QuerySubscriberResponse response) {

    }

    @Override
    public void querySubscriberInfoError() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }
}
