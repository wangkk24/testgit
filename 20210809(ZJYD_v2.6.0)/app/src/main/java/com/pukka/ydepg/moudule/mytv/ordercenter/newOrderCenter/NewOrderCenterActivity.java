package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.BrowseFrameLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.event.OrderCenterFocusEvent;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.OrderCenterTopFocusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewOrderCenterActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnKeyListener{
    private static final String TAG = NewOrderCenterActivity.class.getSimpleName();
    public  static final String SELECT_CARD_IDENTIFICATION = "select_card_Identification";
    public  static final String SELECT_CHILDREN_LOCK = "select_children_lock";

    @BindView(R.id.tv_order_center_base_business_label)
    TextView tvBaseLabel;
//    @BindView(R.id.tv_order_center_go_order_label)
//    TextView tvGoOrderLabel;
//    @BindView(R.id.tv_order_center_my_order_label)
//    TextView tvMyOrderLabel;
    @BindView(R.id.tv_order_center_order_setting_label)
    TextView tvOrderSetting;
    @BindView(R.id.tv_order_center_multicast_switch_label)
    TextView tvmulticastSwitchLabel;
    @BindView(R.id.fl_order_center_content)
    FrameLayout flContent;
    @BindView(R.id.contentView_order_center)
    BrowseFrameLayout contentView;
    @BindView(R.id.rl_order_center_menu)
    RelativeLayout rlMenu;
    /**
     * 基本业务
     */
    NewOrderCenterBusinessFragment businessFragment = new NewOrderCenterBusinessFragment();
    /**
     * 我要订购
     */
    NewOrderCenterGoOrderFragment goOrderFragment = new NewOrderCenterGoOrderFragment();
    /**
     * 我的订购
     */
    NewOrderCenterMyOrderFragment myOrderFragment = new NewOrderCenterMyOrderFragment();
    /**
     * 订购设置
     */
    NewSecondConfirmationFragment settingFragment = new NewSecondConfirmationFragment();
    /**
     * 其他设置
     */
    NewOrderCenterMulticastSwitchFragment otherSettingFragment = new NewOrderCenterMulticastSwitchFragment();
    private int mKeyCode = -1;
    private View lastMenuView;
    /**
     * 当前展示的fragment
     */
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_center_new);
        setmUnBinder(ButterKnife.bind(NewOrderCenterActivity.this));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }

    //修改崩溃问题，不在使用super方法
    @Override
    @SuppressLint("MissingSuperCall")
    protected void onSaveInstanceState(Bundle outState) { }

    private void initView() {
        tvBaseLabel.setOnFocusChangeListener(this);
//        tvGoOrderLabel.setOnFocusChangeListener(this);
//        tvMyOrderLabel.setOnFocusChangeListener(this);
        tvOrderSetting.setOnFocusChangeListener(this);
        tvmulticastSwitchLabel.setOnFocusChangeListener(this);
        tvBaseLabel.setOnKeyListener(this);
//        tvGoOrderLabel.setOnKeyListener(this);
//        tvMyOrderLabel.setOnKeyListener(this);
        tvOrderSetting.setOnKeyListener(this);
        tvmulticastSwitchLabel.setOnKeyListener(this);
        contentView.setOnFocusSearchListener((focused, direction) -> {

            if (direction == View.FOCUS_UP && focused.getId() == R.id.tv_my_order_filter) {
                return lastMenuView;
            }

            if (direction == View.FOCUS_DOWN && focused.getId() == R.id.tv_my_order_filter) {
                if (myOrderFragment.hasData()) {
                    return myOrderFragment.getFirstItem();
                }
            }

            if (direction == View.FOCUS_UP && rlMenu.findFocus() == null ) {
                return lastMenuView;
            }

            return null;
        });
        //支持根据intent中参数跳转到制定页签
        if (null != getIntent()) {
            String selectId = getIntent().getStringExtra(SELECT_CARD_IDENTIFICATION);
            if (TextUtils.equals(SELECT_CHILDREN_LOCK, selectId)) {
                tvOrderSetting.requestFocus();
            } else {
                tvBaseLabel.requestFocus();
//                tvGoOrderLabel.requestFocus();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Fragment toFragment = null;
            switch (v.getId()) {
                case R.id.tv_order_center_base_business_label:
                    toFragment = businessFragment;
                    break;
                case R.id.tv_order_center_go_order_label:
                    toFragment = goOrderFragment;
                    break;
                case R.id.tv_order_center_my_order_label:
                    toFragment = myOrderFragment;
                    break;
                case R.id.tv_order_center_order_setting_label:
                    toFragment = settingFragment;
                    break;
                case R.id.tv_order_center_multicast_switch_label:
                    toFragment = otherSettingFragment;
                    break;
            }
            ((TextView) v).setTextColor(getResources().getColor(R.color.c30_color));
            if (null != toFragment){
                switchFragment(toFragment);
            }
        } else {
            if (KeyEvent.KEYCODE_DPAD_DOWN == mKeyCode) {
                ((TextView) v).setTextColor(getResources().getColor(R.color.c30_color));
            } else {
                ((TextView) v).setTextColor(getResources().getColor(R.color.c29_color));
            }
        }
    }

    private void switchFragment(Fragment toFragment) {
        if (currentFragment != toFragment && !this.isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_order_center_content, toFragment).commitAllowingStateLoss();
            currentFragment = toFragment;
            settingFragment.reload();
        }
    }

    //退订成功，“我的订购获取焦点”
    @Subscribe
    public void onEvent(OrderCenterFocusEvent event) {
//        if(OrderCenterFocusEvent.MY_ORDER==event.getOrderStatic())
//        {
//            tvMyOrderLabel.requestFocus();
//        }else if(OrderCenterFocusEvent.GO_ORDER==event.getOrderStatic()){
//            tvGoOrderLabel.requestFocus();
//        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        mKeyCode = keyCode;

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {

            lastMenuView = v;
//            if (v == tvGoOrderLabel && !goOrderFragment.hasData()) {
//                return true;
//            }
//            if (v == tvMyOrderLabel && !myOrderFragment.hasData()) {
//                myOrderFragment.setFilterFocused();
//                return true;
//            }
            if (v == tvmulticastSwitchLabel ){
                if (otherSettingFragment.getEtPassword().getVisibility() == View.VISIBLE){
                    otherSettingFragment.getEtPassword().requestFocus();
                    return true;
                }
                return true;
            }
        }

        //焦点在订购中心左侧选择TAB上时按下菜单键打开过滤Dialog
        if ( keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN ){
            if( currentFragment instanceof NewOrderCenterMyOrderFragment){
                ((NewOrderCenterMyOrderFragment)currentFragment).onMenuKeyDown();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe
    public void onEvent(OrderCenterTopFocusEvent event){
        if(null!=lastMenuView){
            lastMenuView.setFocusable(true);
            lastMenuView.requestFocus();
        }
    }
}