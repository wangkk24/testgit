package com.pukka.ydepg.customui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;
import com.pukka.ydepg.launcher.util.SwitchDialogUtil;
import com.pukka.ydepg.moudule.children.activity.ParentSetCenterActivity;
import com.pukka.ydepg.moudule.children.addpter.ParentSetCenterDurationAdapter;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.presenter.DeblockingEvent;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;

import org.greenrobot.eventbus.EventBus;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 家长中心
 * Dialog
 */
public class ParentSetCenterDialog extends Dialog implements BaseAdapter.OnItemListener {
    /* TAG */
    private static final String TAG = ParentSetCenterDialog.class.getName();

    private static final long MIN_CLICK_INTERVAL = 2500L;

    //单次时长
    @BindView(R.id.linear_single_time)
    LinearLayoutExt layoutSingleTime;
    //小公主
    @BindView(R.id.tv_gender_female)
    TextViewExt tvGenderFemale;
    //小王子
    @BindView(R.id.tv_gender_male)
    TextViewExt tvGenderMale;
    //性别选择Layout
    @BindView(R.id.linear_gender)
    LinearLayoutExt layoutGender;
    //生日选择—年
    @BindView(R.id.list_year)
    FocusVerticalGridView mBirYearRecycleView;
    //生日选择—月
    @BindView(R.id.list_month)
    FocusVerticalGridView mBirMonthRecycleView;
    //生日选择—日
    @BindView(R.id.list_day)
    FocusVerticalGridView mBirDayRecycleView;
    //生日选择—Layout
    @BindView(R.id.linear_birthday)
    RelativeLayoutExt layoutBirthday;
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
    TextViewExt tvNumDelete;
    @BindView(R.id.tv_result_error)
    TextViewExt tvResultError;
    //计算题目
    @BindView(R.id.tv_count)
    TextViewExt tvCount;
    //计算结果
    @BindView(R.id.tv_count_result)
    TextViewExt tvCountResult;
    /*焦点效果*/
    private FocusEffectWrapper mFocusEffectWrapper;
    //标题
    @BindView(R.id.tv_title_children)
    TextViewExt tvTitle;
    //副标题
    @BindView(R.id.tv_subtitle)
    TextViewExt tvSubtitle;
    //VerticalGridView
    @BindView(R.id.list_time)
    FocusVerticalGridView mDurationVerticalGridview;
    //切换到普通版
    @BindView(R.id.im_ordinary)
    ImageViewExt imOrdinary;

    @BindView(R.id.rl_ordinary)
    RelativeLayoutExt rlOrdinary;
    //切换到儿童版
    @BindView(R.id.im_children_epg)
    ImageViewExt imChildrenEpg;
    //切换到老人版
    @BindView(R.id.im_simple_epg)
    ImageViewExt imSimpleEpg;
    //儿童验证界面
    @BindView(R.id.layout_unlock)
    LinearLayoutExt layoutUnlock;
    //儿童验证界面
    @BindView(R.id.layout_parent_set_center)
    LinearLayoutExt layoutParentSetCenter;
    //切换界面layout
    @BindView(R.id.layout_parent_switch)
    LinearLayoutExt layoutParentSwitch;

    //选择时长Adapter
    private ParentSetCenterDurationAdapter mDurationAdapter;

    //时长List
    private List<String> mDurationList;

    //月
    private List<String> mBirMonthList = new ArrayList<>();
    //日
    private List<String> mBirDayList = new ArrayList<>();
    //年
    private List<String> mBirYearList = new ArrayList<>();

    private ParentSetCenterDurationAdapter mBirYearAdapter;
    private ParentSetCenterDurationAdapter mBirMonthAdapter;
    private ParentSetCenterDurationAdapter mBirDayAdapter;

    private Context context;

    private TextViewExt mTextView;
    private String mTextViewValue;

    private Calendar calendar= Calendar.getInstance();
    //某年某月的天数，默认31天
    private int mMaxDayForYearMonth = 31;

    private int num1, num2, intResult;

    private int thisYear,thisMonth,thisDay;

    /**
     * 是否打开儿童锁界面
     */
    private boolean isOpenParentControl;


    /**
     * viewType:
     * 1:单次时长
     * 2:总时长
     * 3:生日
     * 4:性别
     * 5:切换界面
     * 6:unlock解锁界面数学题
     */
    private int viewType = 0;

    //算术题成功以后所要跳往的界面————1:普通版EPG；2:简版EPG
    private int onclickType = 0;

    /**
     * 1:家长中心
     * 2:退出儿童模式
     */
    private String typeForToActivity = "";

    public ParentSetCenterDialog(@NonNull Context context, int viewType) {
        super(context, R.style.dialog);
        this.context = context;
        this.viewType = viewType;
        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置全屏无标题头*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_parent_set_center);
        ButterKnife.bind(this);

        /*设置宽度全屏*/
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

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

    public boolean isOpenParentControl()
    {
        return isOpenParentControl;
    }


    private void initView() {

        if (viewType == 6) {//儿童验证界面计算题
            unLock();
        } else if (viewType == 1) {//单次时长
            layoutParentSetCenter.setVisibility(View.VISIBLE);
            layoutUnlock.setVisibility(View.GONE);
            layoutSingleTime.setVisibility(View.VISIBLE);
            layoutParentSwitch.setVisibility(View.GONE);
            layoutGender.setVisibility(View.GONE);
            layoutBirthday.setVisibility(View.GONE);
            tvTitle.setText(R.string.single_duration);
            tvSubtitle.setText(R.string.single_duration_hint);

            new LinearSnapHelper().attachToRecyclerView(mDurationVerticalGridview);

            mDurationList = new ArrayList<>(ChildrenConstant.mSingleDurationMap.values());
            int position = 0;
            if (!TextUtils.isEmpty(mTextViewValue) && !mTextViewValue.equalsIgnoreCase(context.getResources().getString(R.string.parent_not_limited))) {
                position = Integer.valueOf(mTextViewValue.substring(0, 2)) / 15 == 6 ? mDurationList.size() - 1 : Integer.valueOf(mTextViewValue.substring(0, 2)) / 15;
                mDurationVerticalGridview.scrollToPosition(position + 6 * 100);
                //mDurationAdapter.setDefaulFocus(position + 6 * 100);
            } else {
                mDurationVerticalGridview.scrollToPosition(6 * 100);
                //mDurationAdapter.setDefaulFocus(6 * 100);
            }
            mDurationAdapter = new ParentSetCenterDurationAdapter(context, mDurationList, viewType,position + 6 * 100);
            mDurationAdapter.setOnItemListener(this);
            mDurationVerticalGridview.setAdapter(mDurationAdapter);

        } else if (viewType == 2) {//总时长

            new LinearSnapHelper().attachToRecyclerView(mDurationVerticalGridview);

            layoutParentSetCenter.setVisibility(View.VISIBLE);
            layoutUnlock.setVisibility(View.GONE);
            layoutSingleTime.setVisibility(View.VISIBLE);
            layoutParentSwitch.setVisibility(View.GONE);
            layoutGender.setVisibility(View.GONE);
            layoutBirthday.setVisibility(View.GONE);
            tvTitle.setText(R.string.single_all_duration);
            tvSubtitle.setText(R.string.single_all_duration_hint);
            mDurationList = new ArrayList<>(ChildrenConstant.mAllDurationMap.values());
            //mDurationAdapter = new ParentSetCenterDurationAdapter(context, mDurationList, viewType,position + 6  * 100);
            int position = 0;
            if (!TextUtils.isEmpty(mTextViewValue) && !mTextViewValue.equalsIgnoreCase(context.getResources().getString(R.string.parent_not_limited))) {
                if (mTextViewValue.length() == 6) {
                    position = Integer.valueOf(mTextViewValue.substring(0, 3)) / 30 == 6 ? mDurationList.size() - 1 : Integer.valueOf(mTextViewValue.substring(0, 3)) / 30;
                } else {
                    position = Integer.valueOf(mTextViewValue.substring(0, 2)) / 30 == 6 ? mDurationList.size() - 1 : Integer.valueOf(mTextViewValue.substring(0, 2)) / 30;
                }
                mDurationVerticalGridview.scrollToPosition(position + 6 * 100);
                //mDurationAdapter.setDefaulFocus(position + 6  * 100);
            } else {
                mDurationVerticalGridview.scrollToPosition(6 * 100);
                //mDurationAdapter.setDefaulFocus(6 * 100);
            }
            mDurationAdapter = new ParentSetCenterDurationAdapter(context, mDurationList, viewType,position + 6  * 100);
            mDurationAdapter.setOnItemListener(this);
            mDurationVerticalGridview.setAdapter(mDurationAdapter);


        } else if (viewType == 3) {//生日
            layoutParentSetCenter.setVisibility(View.VISIBLE);
            layoutUnlock.setVisibility(View.GONE);
            layoutBirthday.setVisibility(View.VISIBLE);
            layoutSingleTime.setVisibility(View.GONE);
            layoutGender.setVisibility(View.GONE);
            layoutParentSwitch.setVisibility(View.GONE);
            tvTitle.setText(R.string.parent_birthday);

            setBirthdayData();


        } else if (viewType == 4) {//性别
            layoutParentSetCenter.setVisibility(View.VISIBLE);
            layoutUnlock.setVisibility(View.GONE);
            layoutGender.setVisibility(View.VISIBLE);
            layoutParentSwitch.setVisibility(View.GONE);
            layoutSingleTime.setVisibility(View.GONE);
            layoutBirthday.setVisibility(View.GONE);
            tvTitle.setText(R.string.parent_gender);
            if (context.getResources().getString(R.string.parent_female).equalsIgnoreCase(mTextViewValue)) {
                tvGenderFemale.requestFocus();
            } else {
                tvGenderMale.requestFocus();

            }

        } else if (viewType == 5) {//切换界面
            switchEpg();
        }

    }

    //设置焦点框
    private void setFocusEffect() {

        /*设置首页焦点 Start*/
        MainUpView mainUpView = new MainUpView(context);
        ViewGroup view = (ViewGroup) findViewById(R.id.layout_parent_switch);
        mainUpView.attach2View(view);
        mFocusEffectWrapper = new FocusEffectWrapper.FocusEffectBuilder().effectNoDrawBridge(new RecyclerViewBridge()).mainUpView(mainUpView).build();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener((View oldFocus, View newFocus) -> {
            if (null != newFocus && (newFocus.getId() == R.id.rl_simple_epg || newFocus.getId() == R.id.rl_children_epg || newFocus.getId() == R.id.rl_ordinary)){
                mFocusEffectWrapper.setIsHideMainUpView(true);
            }
            if (null != mFocusEffectWrapper && viewType == 5) {
                mFocusEffectWrapper.drawFocusEffect(oldFocus, newFocus);
            } else if (null != mFocusEffectWrapper) {
                mFocusEffectWrapper.clearEffectBridge(newFocus);
                mFocusEffectWrapper = null;
            }
        });
    }

    private int mYearPositon = 1;
    private int mMonthPositon = 1;
    private int mDayPositon = 1;

    @Override
    public void onItemSelect(String value, int position) {
        if (viewType == 3) {
            if (mBirYearRecycleView.hasFocus()) {
                mYearPositon = position;
            } else if (mBirMonthRecycleView.hasFocus()) {
                if (mMonthPositon!=position%12){
                    mTextViewValue = "";
                    mMonthPositon = position%12;
                    mDayPositon = 0;
                    getMaxDayForYearMonth(mBirYearList.get(mYearPositon % mBirYearList.size()),mBirMonthList.get(mMonthPositon % mBirMonthList.size()));
                }
            } else if (mBirDayRecycleView.hasFocus()) {
                mDayPositon = position;
            }
        }
    }

    /**
     * viewType:
     * 1:单次时长
     * 2:总时长
     * 3:生日
     * 4:性别
     * 5:切换界面
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClickListener(String value, int position) {
        if (null == mTextView) return;
        if (viewType == 1 || viewType == 2) {
            mTextView.setText(mDurationList.get(position % mDurationList.size()));
            dismiss();
        } else if (viewType == 3) {
            if ((Integer.parseInt(mBirYearList.get(mYearPositon % mBirYearList.size())) < thisYear)
                    || ((Integer.parseInt(mBirYearList.get(mYearPositon % mBirYearList.size())) == thisYear) && Integer.parseInt(mBirMonthList.get(mMonthPositon % mBirMonthList.size())) < thisMonth)
                    || ((Integer.parseInt(mBirYearList.get(mYearPositon % mBirYearList.size())) == thisYear) && Integer.parseInt(mBirMonthList.get(mMonthPositon % mBirMonthList.size())) == thisMonth && Integer.parseInt(mBirDayList.get(mDayPositon % mBirDayList.size())) <= thisDay)) {
                mTextView.setText(mBirYearList.get(mYearPositon % mBirYearList.size()) + "-" + mBirMonthList.get(mMonthPositon % mBirMonthList.size())
                        + "-" + mBirDayList.get(mDayPositon % mBirDayList.size()));
                dismiss();
            } else {
                EpgToast.showLongToast(context, context.getResources().getString(R.string.parent_center_birthday_select_error_toast));
            }
        }
    }

    @OnClick({R.id.rl_ordinary, R.id.rl_children_epg, R.id.rl_simple_epg, R.id.tv_gender_female, R.id.tv_gender_male})
    public void onClick(View view) {
        switch (view.getId()) {
            //切换到普通版EPG
            case R.id.rl_ordinary:
                if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    //当前是儿童版 验证后切换界面
                    onclickType = 1;
                    if (!ClickUtil.isFastDoubleClick(TAG)){
                        SwitchDialogUtil switchDialogUtil = new SwitchDialogUtil(context);
                        switchDialogUtil.setOnclickType(onclickType);
                        switchDialogUtil.setParentSetCenterDialog(this);
                        switchDialogUtil.setisFromDialog(1);
                        switchDialogUtil.setTypeForToActivity(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN);
                        switchDialogUtil.queryUserAttrs();
                    }
                    //unLock();
                } else if(SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
                    //当前是简版 直接切换界面
                    switchToNormalEpg();
                } else {
                    //当前是普通版 不做处理关闭对话框
                    dismiss();
                }
                break;
            //切换到儿童版
            case R.id.rl_children_epg:
                if (!SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                    //非儿童版时才具备切儿童版能力
                    if ( TextUtils.isEmpty(LauncherService.getInstance().getNavIdChildrenEpg())
                            || null == LauncherService.getInstance().getChildrenEpgData()
                            || LauncherService.getInstance().getChildrenEpgData().size() == 0 ){
                        EpgToast.showLongToast(context,context.getResources().getString(R.string.toast_child_no_data));
                    } else {
                        //儿童版页面有数据
                        ((MainActivity) context).switchLauncher(Constant.DesktopType.CHILD);
                    }
                }
                dismiss();
                break;
            //切换到简版
            case R.id.rl_simple_epg:
                if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    //当前是儿童版 验证后切换界面
                    onclickType = 2;
                    if (!ClickUtil.isFastDoubleClick(TAG)){
                        SwitchDialogUtil switchDialogUtil = new SwitchDialogUtil(context);
                        switchDialogUtil.setOnclickType(onclickType);
                        switchDialogUtil.setParentSetCenterDialog(this);
                        switchDialogUtil.setisFromDialog(1);
                        switchDialogUtil.setTypeForToActivity(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN);
                        switchDialogUtil.queryUserAttrs();
                    }
                    //unLock();
                } else if(SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
                    //当前是简版 不做处理关闭对话框
                    dismiss();
                } else {
                    //当前是普通版 直接切换界面
                    switchToSimpleEpg();
                }
                break;
            //性别选择小公主
            case R.id.tv_gender_female:
                if (null != mTextView) {
                    mTextView.setText(context.getResources().getString(R.string.parent_female));
                }
                dismiss();
                break;
            //性别选择小王子
            case R.id.tv_gender_male:
                if (null != mTextView) {
                    mTextView.setText(context.getResources().getString(R.string.parent_male));
                }
                dismiss();
                break;
        }
    }

    //先跳往其他界面，再关闭当前Dialog
    public void dismissDialogPar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },200);
    }

    //year month day of data
    @SuppressLint("SimpleDateFormat")
    private void setBirthdayData() {
            mBirYearList = new ArrayList<>();
            mBirMonthList = new ArrayList<>();
            Date date = new Date();
            thisYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
            thisMonth = Integer.parseInt(new SimpleDateFormat("MM").format(date));
            thisDay = Integer.parseInt(new SimpleDateFormat("dd").format(date));
            //从1900到今年
            for (int i = 1900; i <= thisYear; i++) {
                if (context.getResources().getString(R.string.parent_noset).equalsIgnoreCase(mTextViewValue)|| TextUtils.isEmpty(mTextViewValue)){
                    mYearPositon = thisYear % 1900;
                }else if (i == Integer.valueOf(mTextViewValue.substring(0, 4))){
                    mYearPositon = i % 1900;
                }
                mBirYearList.add(i + "");
            }
            // 1月到12月
            for (int j = 1; j <= 12; j++) {
                if (context.getResources().getString(R.string.parent_noset).equalsIgnoreCase(mTextViewValue)|| TextUtils.isEmpty(mTextViewValue)){
                    mMonthPositon = 0;
                }else if (j == Integer.valueOf(mTextViewValue.substring(5, 7))){
                    mMonthPositon = j - 1;
                }
                if (j < 10) {
                    mBirMonthList.add("0" + j);
                } else {
                    mBirMonthList.add(j + "");
                }
            }

        setDayForYearMonth(mMaxDayForYearMonth);

        mBirMonthAdapter = new ParentSetCenterDurationAdapter(context, mBirMonthList, viewType,0);
        mBirDayAdapter = new ParentSetCenterDurationAdapter(context, mBirDayList, viewType,0);
        mBirYearAdapter = new ParentSetCenterDurationAdapter(context, mBirYearList, viewType,mYearPositon + 1200);
        mBirMonthRecycleView.scrollToPosition(mMonthPositon + 12 * 100);
        mBirDayRecycleView.scrollToPosition(mDayPositon + mMaxDayForYearMonth * 100);
        mBirYearRecycleView.scrollToPosition(mYearPositon + 1200);
//        mBirYearAdapter.setDefaulFocus(mYearPositon + 1200);
        mBirYearAdapter.setOnItemListener(this);
        mBirMonthAdapter.setOnItemListener(this);
        mBirDayAdapter.setOnItemListener(this);
        mBirMonthRecycleView.setAdapter(mBirMonthAdapter);
        mBirDayRecycleView.setAdapter(mBirDayAdapter);
        mBirYearRecycleView.setAdapter(mBirYearAdapter);
    }

    public void setTextView(TextViewExt textView) {
        this.mTextView = textView;
    }

    public void setTextViewValue(String value) {
        this.mTextViewValue = value;
    }

    //计算题解锁界面成功后所要跳往的界面
    public void setTypeForToActivity(String typeForToActivity) {
        this.typeForToActivity = typeForToActivity;
    }

    public void setOnclickType(int onclickType) {
        this.onclickType = onclickType;
    }

    @OnClick({R.id.tv_num_one, R.id.tv_num_two, R.id.tv_num_three, R.id.tv_num_four, R.id.tv_num_five, R.id.tv_num_six, R.id.tv_num_seven
            , R.id.tv_num_eight, R.id.tv_num_night, R.id.tv_num_zero, R.id.tv_num_delete})
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
            case R.id.tv_num_delete:
                deleteCountResult(tvCountResult.getText().toString());
                break;
        }
    }

    //输入结果
    @SuppressLint("SetTextI18n")
    private void setCountResult(String value) {

        if (intResult > 9){
            if (tvCountResult.getText().toString().equals(context.getResources().getString(R.string.unlock_hint_2line))){
                tvResultError.setVisibility(View.INVISIBLE);
                tvCountResult.setText(value+"_");
            }else{
                tvCountResult.setText(tvCountResult.getText().toString().substring(0,1) + value);
            }
        }else{
            tvCountResult.setText(value);
        }
    }

    //删除结果
    @SuppressLint("SetTextI18n")
    private void deleteCountResult(String resultString) {
        if (!TextUtils.isEmpty(resultString)) {

            if (intResult > 9) {
                if (resultString.contains(context.getResources().getString(R.string.unlock_hint_1line))){
                    tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_2line));
                    tvResultError.setVisibility(View.INVISIBLE);
                }else if (!resultString.contains(context.getResources().getString(R.string.unlock_hint_1line))){
                    tvCountResult.setText(resultString.substring(0, 1)+context.getResources().getString(R.string.unlock_hint_1line));
                }
            }else{
                tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_1line));
            }
        }
    }

    //计算题回答成功，跳转界面所需的参数
    private String dataType, actionUrl, actionType, contentId;
    private VOD vod;
    private Map<String, String> extraData;

    public void setIntentData(Context context, String dataType, String actionUrl, String actionType, String contentId, VOD vod, Map<String, String> extraData) {
        this.context = context;
        this.dataType = dataType;
        this.actionUrl = actionUrl;
        this.actionType = actionType;
        this.contentId = contentId;
        this.vod = vod;
        this.extraData = extraData;
    }

    //切换界面
    private void switchEpg() {
        setFocusEffect();

        layoutParentSetCenter.setVisibility(View.GONE);
        layoutUnlock.setVisibility(View.GONE);
        layoutParentSwitch.setVisibility(View.VISIBLE);
        layoutSingleTime.setVisibility(View.GONE);
        layoutGender.setVisibility(View.GONE);
        layoutBirthday.setVisibility(View.GONE);
        tvTitle.setText(R.string.switch_epg);
        new Handler().postDelayed(() -> {
            rlOrdinary.requestFocus();
        }, 200);
    }

    //切换到普通版EPG
    private void switchToNormalEpg() {
        if ( context instanceof MainActivity ){
            ((MainActivity) context).switchLauncher(Constant.DesktopType.NORMAL);
        }
        dismissDialogPar();
    }

    //切换到简版EPG
    private void switchToSimpleEpg() {
        if ( context instanceof MainActivity ) {
            ((MainActivity) context).switchLauncher(Constant.DesktopType.SIMPLE);
        }
        dismissDialogPar();
    }

    //儿童版验证计算题界面
    @SuppressLint("SetTextI18n")
    public void unLock() {

        viewType = 6;

        layoutUnlock.setVisibility(View.VISIBLE);
        layoutParentSetCenter.setVisibility(View.GONE);
        layoutParentSwitch.setVisibility(View.GONE);
        tvNumOne.requestFocus();

        Random random =  new SecureRandom();
        num1 = random.nextInt(8)+2;
        num2 = random.nextInt(8)+2;
        intResult = num1 * num2;
        tvCount.setText(num1 + " x " + num2 + " = ");
        if (intResult > 9) {
            tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_2line));
        } else {
            tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_1line));
        }

        tvCountResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && !s.toString().contains(context.getResources().getString(R.string.unlock_hint_1line))) {
                    //计算结果成功，跳往家长中心
                    if (Integer.parseInt(s.toString()) == intResult) {
                        if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_TO_PARENT)) {
                            ZJVRoute.route(context, dataType, actionUrl,
                                    actionType, contentId, vod, extraData);
                            dismissDialogPar();
                        } else if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN)
                                ||typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.SWITCH_EPG)) {//计算结果成功,切换-1屏
                            if (onclickType == 1) {
                                switchToNormalEpg();
                            } else if (onclickType == 2) {
                                switchToSimpleEpg();
                            } else {
                                dismissDialogPar();
                            }
                        } else if (typeForToActivity.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.VOD_DETAIL_SWITCH_PARENT)) {//voddetailtoParentcenter
                            isOpenParentControl=true;
                            Intent intent = new Intent(context, ParentSetCenterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.getApplicationContext().startActivity(intent);
                            dismissDialogPar();
                        } else {
                            EventBus.getDefault().post(new DeblockingEvent(typeForToActivity));
                            dismiss();
                        }
                    } else {
                        if (intResult > 9) {//答案大于9，则是两位数
                            tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_2line));
                        } else {
                            tvCountResult.setText(context.getResources().getString(R.string.unlock_hint_1line));
                        }
                        tvResultError.setVisibility(View.VISIBLE);
                    }
                }/* else {
                    tvResultError.setVisibility(View.INVISIBLE);
                }*/
            }
        });
    }

    //返回某年某月的最大天数
    private void getMaxDayForYearMonth(String year,String month){
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        //Calendar.MONTH是从0到11的
        calendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
        //mMaxDayForYearMonth  某年某月的最大月天数
        mMaxDayForYearMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setDayForYearMonth(mMaxDayForYearMonth);

        mBirDayAdapter.clearAll();
        mBirDayAdapter.bindData(mBirDayList);
//        mBirDayRecycleView.scrollToPosition(mMaxDayForYearMonth == 31 ? mMaxDayForYearMonth * 100 + 1 : mMaxDayForYearMonth * 100 - 1);
        mBirDayRecycleView.scrollToPosition(mMaxDayForYearMonth * 100);
    }

    //组织每月的天数
    private void setDayForYearMonth(int maxDayForYearMonth){
        mBirDayList = new ArrayList<>();
        // 1日到31日
        for (int k = 1; k <= maxDayForYearMonth; k++) {
            if (!TextUtils.isEmpty(mTextViewValue) && !mTextViewValue.equalsIgnoreCase(context.getResources().getString(R.string.parent_noset)) && k == Integer.valueOf(mTextViewValue.substring(8, 10))) {
                mDayPositon = k - 1;
            }else{
                mDayPositon = 0;
            }
            if (k < 10) {
                mBirDayList.add("0" + k);
            } else {
                mBirDayList.add(k + "");
            }
        }
    }

}
