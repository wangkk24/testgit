package com.pukka.ydepg.moudule.mytv;

import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.scene.UBDSMSVerify;
import com.pukka.ydepg.common.utils.ValidateUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.mvp.contact.MyContact;
import com.pukka.ydepg.launcher.mvp.presenter.MyPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment;
import com.pukka.ydepg.moudule.featured.bean.VodBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liudo on 2018/4/20.
 */

public class SecondConfirmationFragment extends BaseMvpFragment<MyPresenter> implements MyContact.IMyView, View.OnKeyListener, View.OnClickListener {


    private final String TAG = this.getClass().getName();
    /**
     * 开关
     */
    private TextView switch_locktv;
    /**
     * 账号
     */
    private TextView accounttv;
    /**
     * 验证码输入框
     */
    private EditText et_input_verification;
    /**
     * 发送验证码
     */
    private TextView send_btn;
    /**
     * 确认变更
     */
    private Button comfirm_btn;
    /**
     * 手机验证布局
     */
    private LinearLayout phone_lly;
    /**
     * 短信验证开关布局
     */

    private RelativeLayout children_lay;
    /**
     * 开通统一支付提示
     */
    private TextView uni_hint;


    /**
     * 主界面
     */
    private View mainView;

    private String orderingSwitch;


    private boolean isLoadSuccess = false;

    private Subscriber mSubscriber;

    private Profile mProfile;

    private String billId;

    //重复点击最后时间
    private long mLastOnClickTime = 0;
    //重复点击时间
    private static final long VALID_TIME = 500;


    private int count_time = 60;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            count_time--;
            if (count_time != 0) {
                send_btn.setText(String.format("重新发送(%1$s)",
                        String.valueOf(count_time)));
                sendEmptyMessageDelayed(1, 1000);
            } else {
                send_btn.setText("重新发送");
                send_btn.setFocusable(true);//焦点可以获取
                removeMessages(1);
            }


        }
    };


    @Override
    protected void initView(View view) {
        switch_locktv = (TextView) view.findViewById(R.id.switch_lock);
        accounttv = (TextView) view.findViewById(R.id.account);
        et_input_verification = (EditText) view.findViewById(R.id.et_input_verification);
        setEditTextFilter(et_input_verification, 6);
        send_btn = (TextView) view.findViewById(R.id.send_btn);
        comfirm_btn = (Button) view.findViewById(R.id.comfirm_btn);
        phone_lly = (LinearLayout) view.findViewById(R.id.phone_lly);
        children_lay = (RelativeLayout) view.findViewById(R.id.child_lock_ly);
        uni_hint = (TextView) view.findViewById(R.id.uni_hint);
        children_lay.setVisibility(View.GONE);
        mainView = view.findViewById(R.id.root);
        mainView.setVisibility(View.GONE);
        initListener();

    }


    public void setEditTextFilter(final EditText editText, final int length) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                if ("+".equals(source.toString())) {
                    return "0";
                }
                if (!ValidateUtils.isNumbers(source.toString())) {
                    //0-9之间的数字才可以输入
                    if (!TextUtils.isEmpty(source.toString())) {
                        EpgToast.showToast(editText.getContext(),
                                editText.getContext().getString(R.string.input_phonenumber_range));
                    }
                    return "";
                } else if (source.length() + dest.length() > length) {
                    //输入的字符达到上限
                    EpgToast.showToast(editText.getContext(),
                            editText.getContext().getString(R.string.input_charset_maxlimit));
                    return "";
                }
                return source;
            }
        };
        InputFilter lengthFilter = new InputFilter.LengthFilter(length);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }

    private void initListener() {
        comfirm_btn.setOnKeyListener(this);
        send_btn.setOnKeyListener(this);
        send_btn.setOnClickListener(this);
        comfirm_btn.setOnClickListener(this);
        switch_locktv.setOnClickListener(this);
        et_input_verification.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_UP:

                            switch_locktv.setFocusable(true);
                            switch_locktv.requestFocus();
                            return true;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            if (send_btn.isFocusable()) {
                                send_btn.requestFocus();
                            } else {
                                et_input_verification.requestFocus();
                            }
                            return true;
                    }
                }
                return false;
            }
        });

        switch_locktv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (phone_lly.getVisibility() == View.VISIBLE) {
                                if (send_btn.isFocusable()) {
                                    send_btn.requestFocus();
                                } else {
                                    et_input_verification.requestFocus();
                                }
                                return true;
                            } else {
                                switch_locktv.requestFocus();
                                return true;
                            }
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            if (currentStateClose()) {
                                    return false;
                            } else {
                                phone_lly.setVisibility(View.VISIBLE);
                                if (getString(R.string.send_verification).equals(send_btn.getText())) {
                                    send_btn.setFocusable(true);
                                    send_btn.requestFocus();
                                } else {
                                    et_input_verification.setFocusable(true);
                                    et_input_verification.requestFocus();
                                }
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            switchLock();
                            return true;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            return true;
                    }
                }
                return false;
            }
        });

    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_second_confirmation;
    }

    @Override
    protected void initPresenter() {
        presenter = new MyPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != send_btn) {
            mHandler.removeMessages(1);
            send_btn.setText("发送验证码");
            send_btn.setFocusable(true);
            et_input_verification.setText("");
        }
        if (null != presenter) {
            presenter.querySubscribe(new QueryUniInfoRequest());
        }
        List<Profile> profileList = SessionService.getInstance().getSession().getProfileList();
        if (profileList != null && profileList.size() != 0) {
            mProfile = profileList.get(0);
        }

    }

    public void reload() {
        if (null != mHandler && null != phone_lly) {
            phone_lly.setVisibility(View.GONE);
            mHandler.removeMessages(1);
        }

    }


    @Override
    public void loadBookmarkData(List<VodBean> bookMarkList, List<VOD> bookMarkVODList) {

    }

    @Override
    public void loadFavoriteData(List<VodBean> favoriteList, List<VOD> favoriteVODList) {

    }

    @Override
    public void loadSubscriptionData(String time) {

    }

    @Override
    public void loadItemDataFail() {
        isLoadSuccess = false;
    }

    @Override
    public void querySubscriberSucess(String userId, String orderingSwitch, Subscriber subscriber) {
        this.orderingSwitch = orderingSwitch;
        this.mSubscriber = subscriber;
//        if (null != mSubscriber.getSubscriberContract() && !TextUtils.isEmpty(mSubscriber.getSubscriberContract().getMobilePhone())) {
//            billId = mSubscriber.getSubscriberContract().getMobilePhone();
//        }
        presenter.queryUniPayInfo(new QueryUniInfoRequest(), getActivity());

    }

    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {
        UniPayInfo mainUniPayInfo = resolveMainUniPayInfo(response);
        isLoadSuccess = true;
        if (null != mainUniPayInfo) {
            if (TextUtils.isEmpty(mainUniPayInfo.getBillID())) {
                //统一账号不存在
                children_lay.setVisibility(View.GONE);
                uni_hint.setVisibility(View.VISIBLE);
            } else {
                //显示开关
                billId = mainUniPayInfo.getBillID();
                switch_locktv.setText("0".equals(orderingSwitch) ? getString(R.string.confirmation_close) : getString(R.string.confirmation_open));
                switch_locktv.setFocusable(true);
//                switch_locktv.requestFocus();
                phone_lly.setVisibility(View.GONE);
                accounttv.setText("【" + billId + "】");
                children_lay.setVisibility(View.VISIBLE);
                uni_hint.setVisibility(View.GONE);
            }
        } else {
            //未开通统一账号
            children_lay.setVisibility(View.GONE);
            uni_hint.setVisibility(View.VISIBLE);

        }
        mainView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateSubscriberSucess() {
        if (currentStateClose()) {
            UBDSMSVerify.record(UBDConstant.OptType.OPEN_SMS,billId);
            switch_locktv.setText(getString(R.string.confirmation_open));
            children_lay.setVisibility(View.VISIBLE);
            uni_hint.setVisibility(View.GONE);
            phone_lly.setVisibility(View.GONE);
        } else {
            UBDSMSVerify.record(UBDConstant.OptType.CLOSE_SMS,billId);
            switch_locktv.setText(getString(R.string.confirmation_close));
            switch_locktv.setFocusable(true);
            switch_locktv.requestFocus();
            children_lay.setVisibility(View.VISIBLE);
            uni_hint.setVisibility(View.GONE);
            phone_lly.setVisibility(View.GONE);
            mHandler.removeMessages(1);
            send_btn.setText("发送验证码");
            et_input_verification.setText("");
        }

    }

    @Override
    public void updateSubscriberFail() {
        EpgToast.showToast(getContext(), "订购设置失败");
    }

    @Override
    public void updateUserRegInfoSucess() {
        UpdateSubscriberRequest request = new UpdateSubscriberRequest();
        request.setmSubscriber(getResetSubscirber(mSubscriber, "0"));
        presenter.updateSubscriber(request, getActivity());
    }

    @Override
    public void updateUserRegInfoFail() {
        EpgToast.showToast(getActivity(), "验证码错误");

    }

    @Override
    public void queryUserOrderingSiwtchSuccess(String orderingSwitch) {

    }

    @Override
    public void changeUserOrderingSiwtchSuccess() {

    }

    @Override
    public void checkVerifiedCodeSuccess() {

    }

    @Override
    public void queryUserAttrsSuccess(String multiCastSwitch) {

    }

    public UniPayInfo resolveMainUniPayInfo(QueryUniPayInfoResponse response) {
        UniPayInfo mainUniPayInfo = null;
        if (!"0".equals(response.getPayType())) {
            List<UniPayInfo> uniPayInfoList = response.getUniPayList();
            if (uniPayInfoList != null && !uniPayInfoList.isEmpty()) {
                for (UniPayInfo uniPayInfo : uniPayInfoList) {
                    if ("1".equals(uniPayInfo.getRoleID())) {
                        if ("1".equals(uniPayInfo.getPayState()) || "2".equals(uniPayInfo.getPayState())) {
                            mainUniPayInfo = uniPayInfo;
                            break;
                        }
                    }
                }
            }
        }
        return mainUniPayInfo;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    //焦点回到订购设置落焦
//                    EventBus.getDefault().post(new EventKeyLeftSwitchFocus());
                    return false;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (v.getId() == R.id.send_btn) {
                        switch_locktv.setFocusable(true);
                        switch_locktv.requestFocus();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mLastOnClickTime != 0 && System.currentTimeMillis() - mLastOnClickTime < VALID_TIME) {
            return;
        }
        mLastOnClickTime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.switch_lock:
                switchLock();
                break;
            case R.id.send_btn:
                countDown(60);
                break;
            case R.id.comfirm_btn:
                String verification = et_input_verification.getText().toString();
                if (TextUtils.isEmpty(verification) || verification.length() != 6) {
                    EpgToast.showToast(getActivity(), "请输入正确的验证码");
                    return;
                }
                if (null != mProfile) {
                    UpdateUserRegInfoRequest request = new UpdateUserRegInfoRequest();
                    request.setLoginName(mProfile.getLoginName());
                    request.setMsgType(UpdateUserRegInfoRequest.MsgType.RESET_PHONE);
                    request.setVerifiyCode(verification);
                    request.setNewValue(billId);
                    presenter.updateUserRegInfo(request, getActivity());
                }
                break;
        }
    }

    public void switchLock() {
        if (currentStateClose()) {
            UpdateSubscriberRequest request = new UpdateSubscriberRequest();
            request.setmSubscriber(getResetSubscirber(mSubscriber, "1"));
            presenter.updateSubscriber(request, getActivity());
        } else {
            phone_lly.setVisibility(View.VISIBLE);
            if (getString(R.string.send_verification).equals(send_btn.getText())) {
                send_btn.setFocusable(true);
                send_btn.requestFocus();
            } else {
                et_input_verification.setFocusable(true);
                et_input_verification.requestFocus();
            }
        }

    }


    public Subscriber getResetSubscirber(Subscriber mSubscriber, String value) {
        if (null != mSubscriber) {

            List<NamedParameter> customFields = mSubscriber.getCustomFields();
            if (null != customFields && !customFields.isEmpty()) {
                for (int i = customFields.size() - 1; i >= 0; i--) {
                    checkIsValidate(customFields.get(i));
                    if (Constant.ORDERING_SWITCH.equals(customFields.get(i).getKey())) {
                        customFields.remove(i);
                    }


                }
                List<String> stringList = new ArrayList<>();
                stringList.add(value);
                NamedParameter namedParameter = new NamedParameter(Constant.ORDERING_SWITCH, stringList);
                customFields.add(namedParameter);
                mSubscriber.setCustomFields(customFields);

            } else {
                customFields = new ArrayList<NamedParameter>();
                List<String> stringList = new ArrayList<>();
                stringList.add(value);
                NamedParameter namedParameter = new NamedParameter(Constant.ORDERING_SWITCH, stringList);
                customFields.add(namedParameter);
                mSubscriber.setCustomFields(customFields);
            }
        }
        return mSubscriber;
    }


    public void checkIsValidate(NamedParameter mNamedParameter) {
        if (null == mNamedParameter) {
            return;
        }
        List<String> stringList = mNamedParameter.getValues();
        if (null == stringList || stringList.isEmpty()) {
            return;
        }
        for (int i = 0; i < stringList.size(); i++) {
            if (null == stringList.get(i)) {
                stringList.set(i, "");
            }
        }

    }

    public boolean currentStateClose() {
        return getString(R.string.confirmation_close).equals(switch_locktv.getText());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }

    public void countDown(int time) {
        count_time = time;
        et_input_verification.setFocusable(true);
        et_input_verification.requestFocus();
        send_btn.setFocusable(false);
        send_btn.setText(String.format("重新发送(%1$s)", String.valueOf(time)));
        if (null != mProfile) {
            SendSmsRequest request = new SendSmsRequest();
            request.setLoginName(mProfile.getLoginName());
            request.setDestMobilePhone(billId);
            request.setMsgType(SendSmsRequest.MsgType.SMS_RESET_PHONE_CODE);
            presenter.sendSMS(request, getActivity());
            UBDSMSVerify.record(UBDConstant.OptType.SMS_VERIFIY,billId);
        }
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }


}
