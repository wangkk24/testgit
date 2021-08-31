package com.pukka.ydepg.customui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.event.RefreshLauncherEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;
import com.pukka.ydepg.launcher.util.SwitchDialogUtil;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;
import com.pukka.ydepg.service.NetworkReceiver;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.greenrobot.eventbus.EventBus;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2018/12/7.
 * ------------------
 */
public class ManualRefreshDialog extends Dialog {
    /* TAG */
    private static final String TAG = ManualRefreshDialog.class.getName();
    private static final long MIN_CLICK_INTERVAL = 2500L;
    /*焦点效果*/
    private FocusEffectWrapper mFocusEffectWrapper;
    /*网络监听*/
    private NetworkReceiver networkReceiver;
    /*网络图标*/
    private ImageView imNetwork;
    /*儿童版或者普通版*/
    private TextView tvChildrenOrOrdinary;
    private ImageView ivChildrenOrOrdinary;
    /*简版或者普通版*/
    private TextView tvSimpleOrOrdinary;
    private ImageView ivSimpleOrOrdinary;

    //true:执行首页刷新  false:执行二级页面刷新
    private boolean isMainActivity = true;

    /*筛选栏目id*/
    private static final String SETTING_FILTER_CATEGORY_ID = "setting_filter_category_id";

    private Context context;

    public ManualRefreshDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        this.context = context;
        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置全屏无标题头*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_manual_refresh);
        ButterKnife.bind(this);
        /*设置宽度全屏*/
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        /*注册网络监听*/
        registerNetWorkListener();
        /*设置首页焦点 Start*/
        MainUpView mainUpView = new MainUpView(context);
        ViewGroup view = (ViewGroup) findViewById(R.id.manual_refresh);
        mainUpView.attach2View(view);
        mFocusEffectWrapper = new FocusEffectWrapper.FocusEffectBuilder().effectNoDrawBridge(new RecyclerViewBridge()).mainUpView(mainUpView).build();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener((View oldFocus, View newFocus) -> {
            mFocusEffectWrapper.setIsHideMainUpView(true);
            mFocusEffectWrapper.drawFocusEffect(oldFocus, newFocus);
        });
        /*设置首页焦点 End*/
        RelativeLayoutExt rl_refresh = (RelativeLayoutExt) findViewById(R.id.rl_refresh);
        /*RelativeLayoutExt rl_setting = (RelativeLayoutExt) findViewById(R.id.rl_setting);
        RelativeLayoutExt rl_filter = (RelativeLayoutExt) findViewById(R.id.rl_filter);
        RelativeLayoutExt rl_children_epg = (RelativeLayoutExt) findViewById(R.id.rl_children_epg);
        rl_refresh.addFocusEffect();
        rl_setting.addFocusEffect();
        rl_filter.addFocusEffect();
        rl_children_epg.addFocusEffect();*/

        imNetwork = (ImageView) findViewById(R.id.im_network);

        /*设置默认焦点*/
        new Handler().postDelayed(() -> {
            rl_refresh.requestFocus();
        }, 300);

        tvChildrenOrOrdinary = (TextView) findViewById(R.id.tv_childrenorepg);
        tvSimpleOrOrdinary = (TextView) findViewById(R.id.tv_simpleorepg);
        ivChildrenOrOrdinary = (ImageView)findViewById(R.id.im_children_epg);
        ivSimpleOrOrdinary = (ImageView)findViewById(R.id.im_epg);

        initView();
    }

    private void initView(){
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            ivChildrenOrOrdinary.setImageResource(R.drawable.ordinary);
            ivSimpleOrOrdinary.setImageResource(R.drawable.simple);
            tvChildrenOrOrdinary.setText(R.string.normal_epg);
            tvSimpleOrOrdinary.setText(R.string.simple_epg);
        }else if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            ivChildrenOrOrdinary.setImageResource(R.drawable.children);
            ivSimpleOrOrdinary.setImageResource(R.drawable.ordinary);
            tvChildrenOrOrdinary.setText(R.string.children_epg);
            tvSimpleOrOrdinary.setText(R.string.normal_epg);
        }else{
            ivChildrenOrOrdinary.setImageResource(R.drawable.children);
            ivSimpleOrOrdinary.setImageResource(R.drawable.simple);
            tvChildrenOrOrdinary.setText(R.string.children_epg);
            tvSimpleOrOrdinary.setText(R.string.simple_epg);
        }
    }

    /**
     * 点击处理刷新、设置、过滤逻辑
     */
    @OnClick({R.id.rl_refresh, R.id.rl_setting, R.id.rl_filter,R.id.rl_children_epg,R.id.rl_epg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_refresh:
                SuperLog.debug(TAG, "执行刷新");
                EventBus.getDefault().post(new RefreshLauncherEvent(isMainActivity));
                dismiss();
                break;
            case R.id.rl_setting:
                SuperLog.debug(TAG, "执行设置");
                try {
                    CommonUtil.startSettingActivity(context);//小米电视适配要求这里启动Setting页面须满足移动设置页面启动规范
                    MessageDataHolder.get().setIsFocusVideo(true);
                    dismiss();
                } catch (Exception e) {
                    SuperLog.error(TAG, e);
                }
                break;
            case R.id.rl_filter:
                SuperLog.debug(TAG, "执行过滤");

                Intent intent = new Intent(context, VodMainActivity.class);
                if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    List<Subject>  subjects=SessionService.getInstance().getSession().getTerminalConfigurationSettingFilterSubjects();
                    if(null==subjects||subjects.isEmpty()){
                        return;
                    }
                    intent.putExtra(VodMainActivity.IS_CHILD_FILTER_MODE,true);
                }else{
                    String categoryId = SessionService.getInstance().getSession().getTerminalConfigurationValue(SETTING_FILTER_CATEGORY_ID);
                    if ("".equals(categoryId) || categoryId == null) {
                        SuperLog.debug(TAG, "CategoryId is Null, Please setting.");
                        return;
                    }
                    intent.putExtra(VodMainActivity.CATEGORY_ID, categoryId);
                }
                context.startActivity(intent);
                MessageDataHolder.get().setIsFocusVideo(true);
                dismiss();
                break;
            case R.id.rl_children_epg:
                if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    SuperLog.debug(TAG, "切换EPG");

                    showCheckDialog(1);

                    /*SharedPreferenceUtil.getInstance().setIsChildrenEpg(false);
                    ((MainActivity)context).switchLauncher(0);
                    MessageDataHolder.get().setIsFocusVideo(true);*/
                }else{
                    //儿童版无数据，弹框提示
                    if (TextUtils.isEmpty(LauncherService.getInstance().getNavIdChildrenEpg()) || null == LauncherService.getInstance().getChildrenEpgData()
                            || LauncherService.getInstance().getChildrenEpgData().size() == 0){
                        EpgToast.showLongToast(context,context.getResources().getString(R.string.toast_child_no_data));
                    }else{
                        SuperLog.debug(TAG, "切换儿童版");
                        ((MainActivity)context).switchLauncher(Constant.DesktopType.CHILD);
                        MessageDataHolder.get().setIsFocusVideo(true);
                    }
                    dismiss();
                }
                break;
            case R.id.rl_epg:
                if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
                    SuperLog.debug(TAG, "切换EPG");
                    ((MainActivity)context).switchLauncher(Constant.DesktopType.NORMAL);
                    MessageDataHolder.get().setIsFocusVideo(true);
                    dismiss();
                }else {
                    SuperLog.debug(TAG, "切换Simple");
                    if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                        showCheckDialog(2);
                    } else {
                        ((MainActivity) context).switchLauncher(Constant.DesktopType.SIMPLE);
                        MessageDataHolder.get().setIsFocusVideo(true);
                        dismiss();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(networkReceiver, intentFilter);
        networkReceiver.setNetWorkStateListener(new NetworkReceiver.NetWorkStateListener() {
            @Override
            public void changeToWire() {
                imNetwork.setBackground(ContextCompat.getDrawable(context, R
                        .drawable.wire_children));
            }

            @Override
            public void changeToWifi() {
                imNetwork.setBackground(ContextCompat.getDrawable(context, R
                        .drawable.wifi));
            }

            @Override
            public void changeToError() {
                imNetwork.setBackground(ContextCompat.getDrawable(context, R
                        .drawable.wire_error));
            }
        });
    }

    private void showCheckDialog(int type){
        if (!ClickUtil.isFastDoubleClick(TAG)){
            SwitchDialogUtil switchDialogUtil = new SwitchDialogUtil(context);
            switchDialogUtil.setOnclickType(type);
            switchDialogUtil.setTypeForToActivity(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN);
            switchDialogUtil.queryUserAttrs();
        }

        /*ParentSetCenterDialog parentSetCenterDialog = new ParentSetCenterDialog(context, ChildrenConstant.VIEWTYPE.UNLOCK);
        parentSetCenterDialog.setOnclickType(type);
        parentSetCenterDialog.setTypeForToActivity(ZJVRoute.LauncherElementContentType.SWITCH_EPG_FROM_CHILDREN);
        parentSetCenterDialog.show();*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },200);
    }

    /**
     * 注册网络状态监听
     */
    private void registerNetWorkListener() {
        networkReceiver = new NetworkReceiver();
    }
}
