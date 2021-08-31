package com.pukka.ydepg.launcher.util;

import android.content.Context;

import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.dialog.ChildSwitchPwdDialog;
import com.pukka.ydepg.customui.dialog.ParentSetCenterDialog;
import com.pukka.ydepg.launcher.mvp.contact.SwitchDialogContact;
import com.pukka.ydepg.launcher.mvp.presenter.SwitchDialogPresenter;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.moudule.children.report.ChildrenConstant;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;
import java.util.Map;

public class SwitchDialogUtil implements SwitchDialogContact.SwitchView {

    private static final String TAG = SwitchDialogUtil.class.getSimpleName();

    private Context context;

    private SwitchDialogPresenter presenter;

    /**
     * 1:家长中心
     * 2:退出儿童模式
     */
    private String typeForToActivity = "";

    //算术题成功以后所要跳往的界面————1:普通版EPG；2:简版EPG
    private int onclickType = 0;

    /**
     * 是否是从点击资源换切换界面弹的Dialog，点击切换ＥＰＧ触发的查询家长密码
     * ==0:不是，==1：是
     * */
    private int isFromDialog = 0;

    //from ParentSetCenterDialog,不需新建此ParentSetCenterDialog
    private ParentSetCenterDialog dialogSingleTime = null;

    //计算题回答成功，跳转界面所需的参数
    private String dataType, actionUrl, actionType, contentId;
    private VOD vod;
    private Map<String, String> extraData;

    public SwitchDialogUtil(Context context) {
        this.context = context;
    }

    //查询家长密码
    public void queryUserAttrs(){
        presenter = new SwitchDialogPresenter();
        presenter.attachView(this);
        presenter.queryUserAttrs();
    }

    public void setOnclickType(int onclickType) {
        this.onclickType = onclickType;
    }
    public void setisFromDialog(int isFromDialog) {
        this.isFromDialog = isFromDialog;
    }

    public void setParentSetCenterDialog(ParentSetCenterDialog dialogSingleTime) {
        this.dialogSingleTime = dialogSingleTime;
    }

    //计算题解锁界面成功后所要跳往的界面
    public void setTypeForToActivity(String typeForToActivity) {
        this.typeForToActivity = typeForToActivity;
    }

    public void setIntentData(String dataType, String actionUrl, String actionType, String contentId, VOD vod, Map<String, String> extraData) {
        this.dataType = dataType;
        this.actionUrl = actionUrl;
        this.actionType = actionType;
        this.contentId = contentId;
        this.vod = vod;
        this.extraData = extraData;
    }

    @Override
    public void checkVerifiedCodeSuccess() {
        SuperLog.debug(TAG,"checkVerifiedCodeSuccess");

    }

    @Override
    public void checkVerifiedCodeFail() {

    }

    @Override
    public void queryUniPayInfoSuccess(QueryUniPayInfoResponse response) {
        SuperLog.debug(TAG,"queryUniPayInfoSuccess");
    }

    @Override
    public void queryUniPayInfoFail() {
        SuperLog.debug(TAG,"queryUniPayInfoFail");
    }

    @Override
    public void modifyUserAttrSuccess() {
        SuperLog.debug(TAG,"modifyUserAttrSuccess");
    }

    @Override
    public void modifyUserAttrFail() {
        SuperLog.debug(TAG,"modifyUserAttrFail");
    }

    @Override
    public void queryUserAttrsSuccess(String pwdValue) {
        if (isFromDialog == 1 && null != dialogSingleTime){
            dialogSingleTime.dismissDialogPar();
        }
        ChildSwitchPwdDialog dialog = new ChildSwitchPwdDialog(context,2,pwdValue);
        dialog.setTypeForToActivity(typeForToActivity);
        dialog.setOnclickType(onclickType);
        dialog.setIntentData(ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, actionType,
                null == vod ? "":vod.getID(), vod, extraData);
        dialog.show();
        SuperLog.info2SDDebug(TAG,"queryUserAttrsSuccess,打开输入家长密码界面");
    }

    @Override
    public void queryUserAttrsFail() {

        //TODO 测试----Start
        /*ChildSwitchPwdDialog dialog = new ChildSwitchPwdDialog(context,((MainActivity)context).getKeyCode(),"0000");
        dialog.setTypeForToActivity(typeForToActivity);
        dialog.setOnclickType(onclickType);
        dialog.setIntentData(ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, actionType,
                null == vod ? "":vod.getID(), vod, extraData);
        dialog.show();*/
        //TODO 测试----End

        if (isFromDialog == 1 && null != dialogSingleTime){
            dialogSingleTime.unLock();
        }else{
            ParentSetCenterDialog dialogSingleTime = new ParentSetCenterDialog(context, ChildrenConstant.VIEWTYPE.UNLOCK);//typeForToActivity
            dialogSingleTime.setTypeForToActivity(typeForToActivity);
            dialogSingleTime.setOnclickType(onclickType);
            dialogSingleTime.setIntentData(context, ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, actionType,
                    null == vod ? "":vod.getID(), vod, extraData);
            dialogSingleTime.show();
        }
        SuperLog.info2SDDebug(TAG,"queryUserAttrsFail,家长密码为空，打开数字计算验证界面");
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

    public static UniPayInfo resolveMainUniPayInfo(QueryUniPayInfoResponse response) {
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
}
