package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter;

import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyUserAttrRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.mvp.contact.MyContact;
import com.pukka.ydepg.launcher.mvp.presenter.MyPresenter;
import com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.service.presenter.HeartBeatPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.pukka.ydepg.launcher.Constant.TVMULTICASTSWITCH;

public class NewOrderCenterMulticastSwitchFragment extends BaseMvpFragment<MyPresenter> implements MyContact.IMyView {

    private static final String TAG = "NewOrderCenterMulticast";

    //默认密码为10086
    private static final String DEFAULT = "10086";

    RelativeLayout switchStateLayout;

    /*
    组播开关状态
     */
    TextView switchStateLabel;

    /*
    密码输入框
     */
    EditText etPassword;

    /*
    组播开关按钮
     */
    Button multicastSwitchBtn;

    //组播开关状态
    boolean multicastSwitch = false;

    //用于防止快速重复点击开关
    boolean canClick = true;

    private HeartBeatPresenter mPresenter = new HeartBeatPresenter();

    /**************************************BaseMvpFragment方法**************************************/

    @Override
    protected void initView(View view) {
        switchStateLabel = view.findViewById(R.id.switch_state_label);
        etPassword = view.findViewById(R.id.et_password);
        multicastSwitchBtn = view.findViewById(R.id.comfirm_btn);
        switchStateLayout = view.findViewById(R.id.switch_state);

        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        initListener();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_multicast_switch;
    }

    @Override
    protected void initPresenter() {
        presenter = new MyPresenter();
    }

    /**************************************生命周期**************************************************/

    @Override
    public void onResume() {
        switchStateLabel.setVisibility(View.INVISIBLE);
        etPassword.setVisibility(View.INVISIBLE);
        multicastSwitchBtn.setVisibility(View.INVISIBLE);
        switchStateLayout.setVisibility(View.INVISIBLE);
        super.onResume();
        //密码输入框清空
        etPassword.setText("");
        if (null != presenter) {
            presenter.queryUserAttrs();
        }
    }
    /**************************************自定义方法**************************************************/

    private void initListener(){
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                    return true;
                }
                return false;
            }
        });
        multicastSwitchBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    return true;
                }
                return false;
            }
        });

        multicastSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!canClick){
                    return;
                }
                canClick = false;

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canClick = true;
                    }
                },2000);

                if (etPassword.getText().toString().length() == 0){
                    EpgToast.showToast(getActivity(), getString(R.string.multicate_switch_editpassword));
                    return;
                }

                if (DEFAULT.equals(etPassword.getText().toString())){
                    presenter.modifyUserAttr(getModifyUserAttrRequest(), getActivity());
                }else{
                    EpgToast.showToast(getActivity(), getString(R.string.password_error));
                }
            }
        });
    }

    private ModifyUserAttrRequest getModifyUserAttrRequest() {
        ModifyUserAttrRequest request = new ModifyUserAttrRequest();
        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserId(userInfo.getUserId());
        request.setActionType("0");
        QueryUserAttrsResponse.UserAttr userAttr = new QueryUserAttrsResponse.UserAttr();
        userAttr.setAttrName(TVMULTICASTSWITCH);
        userAttr.setAttrValue(multicastSwitch ? "0" : "1");
        request.setModifyAttr(userAttr);
        return request;
    }

    public Subscriber getResetSubscirber(Subscriber mSubscriber) {
        if (null != mSubscriber) {

            List<NamedParameter> customFields = mSubscriber.getCustomFields();
            if (null != customFields && !customFields.isEmpty()) {
                for (int i = customFields.size() - 1; i >= 0; i--) {
                    checkIsValidate(customFields.get(i));
                    if (TVMULTICASTSWITCH.equals(customFields.get(i).getKey())) {
                        customFields.remove(i);
                    }
                }
                List<String> stringList = new ArrayList<>();
                if (multicastSwitchBtn.getText().equals(getString(R.string.open))){
                    stringList.add("1");
                }else{
                    stringList.add("0");
                }

                NamedParameter namedParameter = new NamedParameter(TVMULTICASTSWITCH, stringList);
                customFields.add(namedParameter);
                mSubscriber.setCustomFields(customFields);

            } else {
                customFields = new ArrayList<NamedParameter>();
                List<String> stringList = new ArrayList<>();
                if (multicastSwitchBtn.getText().equals(getString(R.string.open))){
                    stringList.add("1");
                }else{
                    stringList.add("0");
                }
                NamedParameter namedParameter = new NamedParameter(TVMULTICASTSWITCH, stringList);
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

    private void refreshUI(boolean multicastSwitch){
        switchStateLayout.setVisibility(View.VISIBLE);
        switchStateLabel.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
        multicastSwitchBtn.setVisibility(View.VISIBLE);

        if (multicastSwitch){
            //开关打开
            switchStateLabel.setText(getString(R.string.open));
            multicastSwitchBtn.setText(getString(R.string.close));
        }else{
            //开关关闭
            switchStateLabel.setText(getString(R.string.close));
            multicastSwitchBtn.setText(getString(R.string.open));
        }

        //组播单播开关切换，刷新本地直播列表数据
        mPresenter.queryChannelSubjectList(getContext(), multicastSwitch,null);
    }


    /**************************************get方法**********************************************/

    public EditText getEtPassword() {
        return etPassword;
    }

    /**************************************IMyView回调**********************************************/

    @Override
    public void querySubscriberSucess(String userId, String orderingSwitch, Subscriber subscriber) { }

    @Override
    public void updateSubscriberSucess() {
        if (multicastSwitchBtn.getText().equals(getString(R.string.open))){
            multicastSwitch = true;
        }else{
            multicastSwitch = false;
        }
        LiveDataHolder.get().setIsChangeMulticastSwitch(true);
        SharedPreferenceUtil.getInstance().setMulticastSwitch(multicastSwitch);
        //刷新UI并将结果保存到本地
        refreshUI(multicastSwitch);
        etPassword.setText("");
        canClick = true;
    }

    @Override
    public void updateSubscriberFail() {
        if (multicastSwitchBtn.getText().equals(getString(R.string.open))){
            EpgToast.showToast(getContext(), getString(R.string.open_fail));
        }else{
            EpgToast.showToast(getContext(), getString(R.string.close_falil));
        }
        canClick = true;
    }



    @Override
    public void updateUserRegInfoSucess() {

    }

    @Override
    public void updateUserRegInfoFail() {

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
    public void loadBookmarkData(List<VodBean> bookMarkList, List<VOD> bookMarkVODList) {

    }

    @Override
    public void loadFavoriteData(List<VodBean> favoriteList, List<VOD> favoriteVODList) {

    }

    @Override
    public void loadSubscriptionData(String time) {

    }

    @Override
    public void queryUniPayInfoSucc(QueryUniPayInfoResponse response) {

    }

    @Override
    public void queryUserAttrsSuccess(String multiCastSwitch) {
        if ("1".equalsIgnoreCase(multiCastSwitch)) {
            multicastSwitch = true;
        } else {
            multicastSwitch = false;
        }

        //刷新UI并将结果保存到本地
        refreshUI(multicastSwitch);
    }

    @Override
    public void loadItemDataFail() {}
}
