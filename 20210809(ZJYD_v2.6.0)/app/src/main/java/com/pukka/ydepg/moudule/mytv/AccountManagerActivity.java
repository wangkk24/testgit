package com.pukka.ydepg.moudule.mytv;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.errorcode.ErrorMessage;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.presenter.AccountManagerPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.AccountManagerDataView;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账号绑定
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.AccountManagerActivity.java
 * @author:xj
 * @date: 2017-12-25 16:33
 */

public class AccountManagerActivity extends BaseActivity implements AccountManagerDataView{


    private static final int TASK_MESSAGE_REFRESH_STATUS = 1;
    /**
     * 二维码图片
     */
    @BindView(R.id.iv_account_bind_qrcode)
    ImageView mQrcodeIv;

    /**
     * 绑定账号提示
     */
    @BindView(R.id.tv_account_info)
    TextView mTvAccountInfo;

    @BindView(R.id.account_name_one)
    TextView mAccountNameOne;

    /**
     * 重新绑定
     */
    @BindView(R.id.btn_account_bind)
    Button mBtnAccountBind;

    @BindView(R.id.bind_pic)
    ImageView mBindImageView;

    @BindView(R.id.account_info)
    LinearLayout mAccountInfoLayout;

    @BindView(R.id.btn_operation_tips)
    Button mBtnOperationTips;

    @BindView(R.id.account_content_framelayout)
    FrameLayout mAccountFramelayout;

    @BindView(R.id.bind_description)
    TextView bindDescription;

    @BindView(R.id.tv_migu_desc)
    TextView mMiguDesc;

    AccountManagerPresenter mPresenter;

    //二维码信息string
    private String qrCode = "";

    /**
     * 超时弹窗
     */
    private PopupWindow mTimeoutDialog;
    /**
     * 账号列表
     */
    private PopupWindow mAccountDialog;

    private PopupWindow mBrowseDialog;

    /**
     * 重新绑定
     */
    private LinearLayout mAgainBind;

    /**
     * 小的二维码
     */
    private ImageView mSmallImage;

    private Timer mTimer;
    /**
     * 已绑定列表
     */
    private List<String> mBindedSubscribers = new ArrayList<>();

    /**
     * 弹窗账号列表
     */
    private RecyclerView mBindAccountList;
    /**
     * 适配器
     */
    private RecyclerView.Adapter adapter;
    /**
     * 列表选择项
     */
    private int checkIndex = -1;

    /**
     * 是否可以点击
     */
    private boolean isClickable=true;

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TASK_MESSAGE_REFRESH_STATUS) {
                qrCodeAuthenticateStatus();
            }
        }
    };


    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            //弹出失效窗口
            if (mTimeoutDialog!=null&&!mTimeoutDialog.isShowing() && null != mAccountFramelayout) {
                mTimeoutDialog.showAtLocation(mAccountFramelayout, Gravity.BOTTOM, 0, 0);
            }
            stopTimer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setmUnBinder(ButterKnife.bind(this));
        mPresenter = new AccountManagerPresenter(this);
        mPresenter.setDataView(this);
        initView();
        qrCodeAuthenticate();
        queryBindedSubscriber();
    }

    private void initView() {
        String miguInfo = getResources().getString(R.string.account_bind_comp);
        SpannableStringBuilder style = new SpannableStringBuilder(miguInfo);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#fccb35")), 2, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMiguDesc.setText(style);
        mBtnAccountBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountBindListDialog();
            }
        });
        mBtnOperationTips.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
               showBrowseDialog();
            }
        });
        mBtnAccountBind.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        mBindImageView.setImageDrawable(hasAccountBinded()?getResources().getDrawable(R.drawable.account_bind_5):getResources().getDrawable(R.drawable.account_bind_1));
        bindDescription.setVisibility(!hasAccountBinded() ? View.VISIBLE : View.GONE);
        mBtnOperationTips.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        mAccountInfoLayout.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        mTvAccountInfo.setVisibility(View.GONE);
//        mTvAccountInfo.setText(hasAccountBinded() ? mBindedSubscribers.size() + getString(R.string.account_bind_tips) : getString(R.string.account_not_bind));
//        mTvAccountInfo.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
    }

    /**
     * 账号绑定列表
     */
    private void showAccountBindListDialog() {
        if (null == mAccountDialog) {
            View v = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_account_list, null);
            TextView dialogBindAccountTv = (TextView) v.findViewById(R.id.tv_dialog_account_bind);
            Button unbindBtn = (Button) v.findViewById(R.id.btn_dialog_account_unbind);
            mBindAccountList = (RecyclerView) v.findViewById(R.id.rv_dialog_account_bind);
            mAccountDialog = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            //noinspection deprecation
            mAccountDialog.setBackgroundDrawable(new BitmapDrawable());
            setupRecyclerView();
            dialogBindAccountTv.setText(hasAccountBinded() ? mBindedSubscribers.size() +
                    getString(R.string.account_bind_tips) : getString(R.string.account_not_bind));
            unbindBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkIndex != -1) {
                        if(isClickable){
                            isClickable=false;
                            handleUnbindSubscriber();
                        }
                    } else {
                        EpgToast.showLongToast(AccountManagerActivity.this, getString(R.string.account_choose_tips));
                    }
                }
            });
        }
        mAccountDialog.showAtLocation(mAccountFramelayout, Gravity.BOTTOM, 0, 0);
        if(checkIndex==0){
            //解绑的时候,checkIndex=0,重新绑定时候,弹窗显示出来,第一个位置checkbox按钮勾选
            //此时要让第一个位置的itemView获取焦点,不然没有选中状态
            mAccountFramelayout.postDelayed(new Runnable() {
                @Override public void run() {
                    RecyclerView.ViewHolder holder=mBindAccountList.findViewHolderForLayoutPosition(0);
                    if(null!=holder && null!=holder.itemView){
                        holder.itemView.requestFocus();
                    }
                }
            },100);
        }
    }

    private void showBrowseDialog(){
        if (null == mBrowseDialog){
            View v = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_operation_step, null);
            mAgainBind = (LinearLayout) v.findViewById(R.id.layout_again_bind);
            TextView accountNameTwo= (TextView) v.findViewById(R.id.account_name_two);
            accountNameTwo.setText(hasAccountBinded()?getResources().getString(R.string.account)+StringUtils.getEncryptionNumber(mBindedSubscribers.get(0)):
                    getResources().getString(R.string.account_name));
            mSmallImage = (ImageView) v.findViewById(R.id.small_qrcode);
            if (!TextUtils.isEmpty(qrCode)){
                Bitmap bitmap = QRCodeGenerator.genQrCode(qrCode, getResources().getDimensionPixelOffset(R.dimen.margin_24),
                        getResources().getDimensionPixelOffset(R.dimen.margin_24));
                if (null != bitmap) {
                    mSmallImage.setImageBitmap(bitmap);
                }
            }
            mBrowseDialog = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            //noinspection deprecation

            mAgainBind.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    checkIndex=0;
                    handleUnbindSubscriber();
                    mBrowseDialog.dismiss();
                }
            });

        }
        mBrowseDialog.setBackgroundDrawable(new BitmapDrawable());
        mBrowseDialog.setFocusable(true);
        mBrowseDialog.showAtLocation(mAccountFramelayout, Gravity.BOTTOM, 0, 0);
        mAgainBind.requestFocus();
    }


    /**
     * 初始化账号列表
     */
    private void setupRecyclerView() {
        mBindAccountList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new AccountViewHolder(LayoutInflater.from(AccountManagerActivity.this).inflate(
                        R.layout.account_bind_item, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                AccountViewHolder accountViewHolder = (AccountViewHolder) holder;
                accountViewHolder.setAccount(StringUtils.getEncryptionNumber(mBindedSubscribers.get(position)));
                accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == checkIndex){
                            checkIndex = -1;
                        }else {
                            checkIndex = position;
                        }
                        notifyItemRangeChanged(0,mBindedSubscribers.size());
                    }
                });
                if (checkIndex == position){
                    accountViewHolder.accountCb.setSelected(true);
                }else {
                    accountViewHolder.accountCb.setSelected(false);
                }
            }

            @Override
            public int getItemCount() {
                return mBindedSubscribers.size();
            }
        };
        mBindAccountList.setAdapter(adapter);
    }

    /**
     * 请求刷新二维码
     */
    private void qrCodeAuthenticate() {
        mPresenter.qrCodeAuthenticate();
    }

    /**
     * 检查二维码状态
     */
    private void qrCodeAuthenticateStatus() {
        mPresenter.qrCodeAuthenticateStatus();
    }

    /**
     * 查询绑定列表
     */
    private void queryBindedSubscriber() {
        mPresenter.queryBindedSubscriber();
    }

    /**
     * 解绑账号
     */
    private void handleUnbindSubscriber() {
        mPresenter.handleUnbindSubscriber(mBindedSubscribers.get(checkIndex));
    }

    /**
     * 清除账号状态
     */
    private void quitQrCodeAuthenticate() {
        mPresenter.quitQrCodeAuthenticate();
    }
    /**
     * 设置当前二维码认证用户
     *
     */
    private void setQrCodeAuthenticatedSubscriber(String subscriberId){

        mPresenter.setQrCodeAuthenticatedSubscriber(subscriberId);

    }


    @Override
    public void qrCodeCallBackSuccess(QrCodeAuthenticateResponse authenticateResponse) {
        if (null != authenticateResponse) {
            qrCode = authenticateResponse.getQrCode();
            Bitmap bitmap = QRCodeGenerator.genQrCode(qrCode, 304,
                    304);
            if (null != bitmap) {
                mQrcodeIv.setImageBitmap(bitmap);
            }
            //二维码失效时间
            String validTime = authenticateResponse.getValidTime();
            mQrcodeIv.removeCallbacks(mRunable);
            //失效时间到达时弹框
            long valid=Long.valueOf(validTime) - System.currentTimeMillis();
            SuperLog.debug("AccountManagerActivity ","validTime:"+valid);
            mQrcodeIv.postDelayed(mRunable, valid);
            //检查二维码状态 是否已经绑定了
            startTimeTask(validTime);
        }

    }

    @Override
    public void qrCodeCallBackFailed() {
//        //TAG 测试数据
//        Bitmap bitmap = QRCodeGenerator.genQrCode("www.baidu.com", getResources().getDimensionPixelOffset(R.dimen.account_content_qrcode_width),
//                getResources().getDimensionPixelOffset(R.dimen.account_content_qrcode_width));
//        if (null != bitmap) {
//            mQrcodeIv.setImageBitmap(bitmap);
//        }
        mQrcodeIv.postDelayed(mRunable,2000);
        EpgToast.showLongToast(this,getString(R.string.netWork_request_failed));
    }

    /**
     * 失效弹窗
     *
     * @return
     */
    private PopupWindow getTimeoutDialog() {
        if (mTimeoutDialog == null) {
            @SuppressLint("InflateParams")
            View v = LayoutInflater.from(this).inflate(R.layout.dialog_qrcode_time_out, null);
            Button refreshBtn = (Button) v.findViewById(R.id.btn_dialog_qrcode_timeout);
            refreshBtn.requestFocus();
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qrCodeAuthenticate();
                    mTimeoutDialog.dismiss();
                }
            });
            mTimeoutDialog = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            //noinspection deprecation
            mTimeoutDialog.setBackgroundDrawable(new BitmapDrawable());
        }
        return mTimeoutDialog;
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 开启定时器
     *
     * @param validTime
     */
    private void startTimeTask(String validTime) {
        stopTimer();
        mTimer = new Timer();
        mTimer.schedule(new CheckStatusTimeTask(mHandle, validTime), 5000, 5000);
    }

    @Override
    public void queryQrCodeStatusSuccess(QueryQrCodeStatusResponse qrCodeStatusResponse) {
        if (null != qrCodeStatusResponse) {
            if (TextUtils.equals(qrCodeStatusResponse.getStatus().toUpperCase(), QueryQrCodeStatusResponse.Status.SUCCESSED)) {
                //说明二维码绑定是成功的
                handleBindSubscriberId(qrCodeStatusResponse.getScannedSubscriberId());
                //设置二维码订户
                setQrCodeAuthenticatedSubscriber(qrCodeStatusResponse.getScannedSubscriberId());
            }
            if (TextUtils.equals(qrCodeStatusResponse.getStatus().toUpperCase(), QueryQrCodeStatusResponse.Status.FAILED)) {
                //说明二维码已经失效了 弹出刷新提示
                if(!isDestroyed()){
                    //弹出失效窗口
                    if (null!= getTimeoutDialog() && !getTimeoutDialog().isShowing()) {
                        getTimeoutDialog().showAtLocation(mAccountFramelayout, Gravity.BOTTOM, 0, 0);
                    }
                }
                stopTimer();
            }
        }
    }

    @Override
    public void queryQrCodeStatusFailed(ErrorMessage message) {
        if (null != message) {
            if (CommonUtil.isSessionError(message.getCode())) {//会话超时
                stopTimer();
            }
        }

    }

    @Override
    public void qeryBindedSubscriberSuccess(QueryBindedSubscriberResponse queryBindedSubscriberResponse) {
        List<String> bindedSubscribers = queryBindedSubscriberResponse.getBindedSubscribers();
        //保存 更新列表 刷新绑定文字
        if (null != bindedSubscribers) {
            SharedPreferenceUtil.getInstance().saveBindedSubscribers(bindedSubscribers);
            if (bindedSubscribers.size() > 0){
                //最后一个设置
                setQrCodeAuthenticatedSubscriber(bindedSubscribers.get(bindedSubscribers.size()-1));
            }
        }
        getBindedSubscribers();
        updateAccountInfo();

    }

    /**
     * 更新已经绑定信息
     */
    private void updateAccountInfo() {
        mBindImageView.setImageDrawable(hasAccountBinded()?getResources().getDrawable(R.drawable.account_bind_5):getResources().getDrawable(R.drawable.account_bind_1));
        mBtnAccountBind.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        bindDescription.setVisibility(!hasAccountBinded() ? View.VISIBLE : View.GONE);
        mBtnOperationTips.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        mAccountInfoLayout.setVisibility(hasAccountBinded() ? View.VISIBLE : View.GONE);
        mAccountNameOne.setText(hasAccountBinded()?getResources().getString(R.string.account)+StringUtils.getEncryptionNumber(mBindedSubscribers.get(0)):
        getResources().getString(R.string.account_name));
        if (hasAccountBinded()) {
            mBtnOperationTips.requestFocus();
        }
    }

    private boolean hasAccountBinded() {
        return !mBindedSubscribers.isEmpty();
    }

    @Override
    public void qeryBindedSubscriberFailed(ErrorMessage message) {
        if (null != message) {
            if (CommonUtil.isSessionError(message.getCode())) {//会话超时
                stopTimer();
            }
        }
    }

    /**
     * 得到绑定的数据
     */
    private void getBindedSubscribers() {
        mBindedSubscribers.clear();
        String info = SharedPreferenceUtil.getInstance().getBindedSubscribers();
        if (!TextUtils.isEmpty(info)) {
            Collections.addAll(mBindedSubscribers, info.split(";"));
        }
    }

    @Override
    public void unBindSubsrciberSuccess() {
        isClickable=true;
        //解绑成功 1刷新二维码 2重新查询绑定列表
        mBindedSubscribers.clear();
        if(null!=adapter){
            adapter.notifyDataSetChanged();
        }
        updateAccountInfo();
        EpgToast.showLongToast(this, getString(R.string.account_unbind_success));
        if(null!=mAccountDialog){
            mAccountDialog.dismiss();
        }
        //重新拉取二维码
        qrCodeAuthenticate();
        //重新监控绑定状态
        queryBindedSubscriber();
        //退出二维码认证状态
        quitQrCodeAuthenticate();
    }

    @Override
    public void unBindSubsrciberFailed() {
        isClickable=true;
        EpgToast.showLongToast(this, getString(R.string.account_unbind_failed));
    }

    @Override
    public void quitQrCodeSuccess() {

    }

    @Override
    public void quitQrCodeFailed() {

    }

    /**
     * 有效期内检查二维码状态 是否已经被绑定了
     */
    private static class CheckStatusTimeTask extends TimerTask {

        private Handler handler;
        private String validTime;

        CheckStatusTimeTask(Handler handler, String validTime) {
            this.handler = handler;
            this.validTime = validTime;
        }

        @Override
        public void run() {
            if (Long.valueOf(validTime) > System.currentTimeMillis()) {
                handler.sendMessage(Message.obtain(handler, TASK_MESSAGE_REFRESH_STATUS));
            }
        }
    }

    private void handleBindSubscriberId(String subscriberId) {
        boolean contained = false;
        for (String id : mBindedSubscribers) {
            if (id.equals(subscriberId)) {
                contained = true;
                break;
            }
        }
        if (!contained) {
            mBindedSubscribers.add(subscriberId);
            updateAccountInfo();
        }
        EpgToast.showLongToast(this, getString(R.string.account_bind_success));
        stopTimer();
    }

    private class AccountViewHolder extends RecyclerView.ViewHolder {

        private TextView accountTv;
        private ImageView accountCb;

        AccountViewHolder(View itemView) {
            super(itemView);
            accountTv = (TextView) itemView.findViewById(R.id.tv_account_bind_account);
            accountCb = (ImageView) itemView.findViewById(R.id.cb_account_bind_check);
        }

        void setAccount(String account) {
            accountTv.setText(account);
        }

    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
        mHandle.removeCallbacks(mRunable);
    }
}
