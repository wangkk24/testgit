package com.pukka.ydepg.moudule.mytv;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.presenter.EventKeyLeftSwitchFocus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by liudo on 2018/4/19.
 */

public class MyOrderActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener {


    /**
     * 订购列表
     */
    TextView orderListtv;
    /**
     * 订购短信验证开关开关
     */
    TextView orderSettv;
    Fragment mFragment;
    View mMenu;
    View mLine;
    OrderListFragment mOrderListFragment;
    SecondConfirmationFragment mSecondConfirmationFragment;

    public static final String ORDER_REPSONSE="order_repsonse";

    public static final String SELECT_CARD_IDENTIFICATION="select_card_Identification";

    public static final String SELECT_CHILDREN_LOCK="select_children_lock";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    /**
     * 1 历史选中 其余收藏选中
     */
    private void initData() {
        String str=getIntent().getStringExtra(SELECT_CARD_IDENTIFICATION);
        if(SELECT_CHILDREN_LOCK.equals(str)){
            orderSettv.requestFocus();
        }else{
            orderListtv.requestFocus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mOrderListFragment = new OrderListFragment();
        mSecondConfirmationFragment = new SecondConfirmationFragment();
        mLine = findViewById(R.id.line);
        orderListtv = (TextView) findViewById(R.id.activity_mytv_movie_history);
        orderSettv = (TextView) findViewById(R.id.activity_mytv_movie_collection);
        mMenu = findViewById(R.id.activity_my_movie_menu);
        orderListtv.setOnFocusChangeListener(this);
        orderListtv.setOnClickListener(this);
        orderSettv.setOnClickListener(this);
        orderSettv.setOnFocusChangeListener(this);
        orderSettv.setOnKeyListener(this);
        orderListtv.setOnKeyListener(this);
        orderListtv.setOnClickListener(this);
        orderSettv.setNextFocusDownId(R.id.activity_mytv_movie_collection);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Fragment to = null;
        switch (v.getId()) {
            case R.id.activity_mytv_movie_history:
                to = mOrderListFragment;
                orderSettv.setSelected(false);
                break;
            case R.id.activity_mytv_movie_collection:
                to = mSecondConfirmationFragment;
                orderSettv.setSelected(true);
                break;
        }
        if (null != to){
            switchContent(to);
        }
    }

    /**
     *      * Modify the contents of the display will not reload
     */
    protected void switchContent(Fragment to) {
        if (mFragment != to&&!this.isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_my_movie_content, to).commitAllowingStateLoss();
            mFragment = to;
            mSecondConfirmationFragment.reload();
        }
    }

    @Override
    public void onClick(View v) {
        jumpActivity(v);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                    SuperLog.error("jamie", "－－－－－向右－－－－－");
                    jumpActivity(v);
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (v == orderListtv) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        return false;
    }

    /**
     * 跳转历史记录页面 或者收藏页面
     *
     * @param view
     */
    private void jumpActivity(View view) {
        switch (view.getId()) {
            case R.id.activity_mytv_movie_history:
                Intent orderListit = new Intent(MyOrderActivity.this, OrderedListActivity.class);
                if(null!=mOrderListFragment&&null!=mOrderListFragment.getmQueryProductInfoResponse()){
                    orderListit.putExtra(ORDER_REPSONSE,JsonParse.object2String(mOrderListFragment.getmQueryProductInfoResponse()));
                }
                startActivity(orderListit);
                //设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                orderSettv.setSelected(false);
                break;
            case R.id.activity_mytv_movie_collection:
                    if(null!=mSecondConfirmationFragment&&null!=mSecondConfirmationFragment.findViewById(R.id.switch_lock)&&null!=mSecondConfirmationFragment.findViewById(R.id.child_lock_ly)){
                        if(mSecondConfirmationFragment.findViewById(R.id.child_lock_ly).getVisibility()==View.VISIBLE) {
                            View switchLock = mSecondConfirmationFragment.findViewById(R.id.switch_lock);
                            switchLock.setFocusable(true);
                            switchLock.requestFocus();
                        }else{
                            orderSettv.setFocusable(true);
                            orderSettv.requestFocus();
                        }
                    }else{
                        orderSettv.setFocusable(true);
                        orderSettv.requestFocus();
                    }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void eventKeyEvent(EventKeyLeftSwitchFocus eventKeyLeftSwitchFocus){
        if(null!=orderSettv){
            orderSettv.setFocusable(true);
            orderSettv.requestFocus();
        }
    }
}
