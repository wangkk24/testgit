package com.pukka.ydepg.launcher.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.ChangeUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyUserAttrRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserAttrsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ModifyUserAttrResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateUserRegInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.mvp.contact.MyContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.moudule.featured.bean.VODMining;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 我的页面业务类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.MyPresenter.java
 * @date: 2017-12-16 17:04
 * @version: V1.0 描述当前版本功能
 */
public class MyPresenter extends BasePresenter<MyContact.IMyView> implements MyContact.IMyPresenter {
    private static final String TAG = MyPresenter.class.getSimpleName();

    public MyPresenter() {}

    /**
     * 获取播放历史信息
     *
     * @param request
     */
    @SuppressLint("CheckResult")
    @Override
    public void queryBookMark(QueryBookmarkRequest request, Context context) {
        String interfaceName = HttpConstant.QUERYBOOKMARK;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().queryBookmark(url, request).compose(onCompose(mView.bindToLife())).subscribe(queryBookmarkResponse -> {
            String retCode = queryBookmarkResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                List<VOD> vods = getBookMarkVODList(queryBookmarkResponse.getBookmarks());
                List<VodBean> vodBeans = convert2SearchVODBean(vods);
                mView.loadBookmarkData(vodBeans, vods);
            } else {
                handleErrorIncludeTimeOut(retCode, interfaceName, context);
            }
        }, throwable -> {
        });
    }

    /**
     * 获取收藏信息
     *
     * @param request
     */
    @SuppressLint("CheckResult")
    @Override
    public void queryFavorite(QueryFavoriteRequest request, Context context) {
        String interfaceName = HttpConstant.QUERYFAVORITE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().queryFavorite(url, request).compose(onCompose(mView.bindToLife())).subscribe(queryFavoriteResponse -> {
            String retCode = queryFavoriteResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                List<VOD> vods = getFavVODList(queryFavoriteResponse.getFavorites());
                List<VodBean> vodBeans = convert2SearchVODBean(vods);
                mView.loadFavoriteData(vodBeans, vods);
                SuperLog.debug(TAG, "success");
            } else {
                if (mView != null) {
                    mView.loadItemDataFail();
                }
                handleErrorIncludeTimeOut(retCode, interfaceName, context);
            }
        }, throwable -> {
            if (mView != null) {
                mView.loadItemDataFail();
            }
            SuperLog.debug(TAG, throwable.getLocalizedMessage());
        });
    }

    public void querySubscribe(QueryUniInfoRequest request) {
        String interfaceName = HttpConstant.QUERY_SUBSCRIBE_INFO;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        if (null == mView) {
            return;
        }
        HttpApi.getInstance().getService().querySubscriberInfo(url, request).compose(onCompose(mView.bindToLife())).subscribe(querySubscriberResponse -> {
            String retCode = querySubscriberResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                List<NamedParameter> customFields = querySubscriberResponse.getSubscriber().getCustomFields();
                String userId=null;
                String orderingSwitch="0";
                if (!CollectionUtil.isEmpty(customFields)) {
                    for (NamedParameter namedParameter : customFields) {
                        if ("BILL_ID".equals(namedParameter.getKey()) && !CollectionUtil.isEmpty(namedParameter.getValues())) {
                            userId = namedParameter.getValues().get(0);
                            SessionService.getInstance().getSession().setAccountName(userId);
                        }
                        if(Constant.ORDERING_SWITCH.equals(namedParameter.getKey())&&!CollectionUtil.isEmpty(namedParameter.getValues())){
                            orderingSwitch=namedParameter.getValues().get(0);
                        }
                    }

                }
                if (mView != null) {
                    mView.querySubscriberSucess(userId,orderingSwitch,querySubscriberResponse.getSubscriber());
                }
            }else{
                if (mView != null) {
                    mView.loadItemDataFail();
                }
            }
        }, throwable -> {
            if (mView != null) {
                mView.loadItemDataFail();
            }
        });
    }

    @SuppressLint("CheckResult")
    public void queryUserAttrs() {
        if (null == mView) {
            return;
        }
        QueryUserAttrsRequest request = new QueryUserAttrsRequest();
        request.setUserId(AuthenticateManager.getInstance().getCurrentUserInfo().getUserId());
        List<String> attrNames = new ArrayList<>();
        request.setAttrNames(new ArrayList<String>(Arrays.asList(com.pukka.ydepg.launcher.Constant.TVMULTICASTSWITCH)));
        HttpApi.getInstance().getService().queryUserAttrs(HttpUtil.getVssUrl(HttpConstant.QUERY_USER_ATTRS), request).compose(onCompose(mView.bindToLife())).subscribe(queryUserAttrsResponse -> {
            String orderingSwitch="0"; //0关 1开
            if (queryUserAttrsResponse.getReturnCode() == Integer.parseInt(Result.RETCODE_OK_TWO)) {
                List<QueryUserAttrsResponse.UserAttr> attrs = queryUserAttrsResponse.getAttrs();
                if(!CollectionUtil.isEmpty(attrs)){
                    for (QueryUserAttrsResponse.UserAttr userAttr : attrs){
                        if (com.pukka.ydepg.launcher.Constant.TVMULTICASTSWITCH.equalsIgnoreCase(userAttr.getAttrName())){
                            orderingSwitch = userAttr.getAttrValue();
                        }
                    }
                }
                mView.queryUserAttrsSuccess(orderingSwitch);
            }else{
                mView.loadItemDataFail();
            }
        }, throwable -> {
            mView.loadItemDataFail();
        });
    }

    //VRS当前童锁状态查询功能
    @SuppressLint("CheckResult")
    public void queryUserOrderingSwitch() {
        QueryUserOrderingSwitchRequest request = new QueryUserOrderingSwitchRequest();
        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserId(userInfo.getUserId());
        String interfaceName = HttpConstant.QUERY_USER_ORDER_SIWTCH;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        if (null == mView) {
            return;
        }
        HttpApi.getInstance().getService().queryUserOrderingSwitch(url, request).compose(onCompose(mView.bindToLife())).subscribe(querySubscriberResponse -> {
            String retCode = querySubscriberResponse.getResult().getReturnCode();
            SuperLog.debug(TAG,"queryUserOrderingSwitch retCode = "+ retCode);
            if (TextUtils.equals(retCode, Result.RETCODE_OK_TWO)) {
                //0关闭 默认值；1开启
                String orderingSwitch="0";
                if (!TextUtils.isEmpty(querySubscriberResponse.getOrderingSwitch())){
                    orderingSwitch = querySubscriberResponse.getOrderingSwitch();
                }
                if (mView != null) {
                    mView.queryUserOrderingSiwtchSuccess(orderingSwitch);
                }
            }else{
                if (mView != null) {
                    mView.queryUserOrderingSiwtchSuccess("0");
                }
            }
        }, throwable -> {
            if (mView != null) {
                mView.queryUserOrderingSiwtchSuccess("0");
            }
        });
    }

    //VRS当前童锁状态更新功能
    @SuppressLint("CheckResult")
    public void changeUserOrderingSwitch(int orderingSwitch,Context context) {
        ChangeUserOrderingSwitchRequest request = new ChangeUserOrderingSwitchRequest();
        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserId(userInfo.getUserId());
        request.setOrderingSwitch(orderingSwitch);
        request.setChangeChannel("EPG");
        String interfaceName = HttpConstant.CHANGE_USER_ORDER_SWITCH;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        if (null == mView) {
            return;
        }
        HttpApi.getInstance().getService().changeUserOrderingSwitch(url, request).compose(onCompose(mView.bindToLife())).subscribe(response -> {
            String retCode = response.getResult().getReturnCode();
            SuperLog.debug(TAG,"queryUserOrderingSwitch retCode = "+ retCode);
            if (TextUtils.equals(retCode, Result.RETCODE_OK_TWO)) {
                //变更成功
                if (mView != null) {
                    mView.changeUserOrderingSiwtchSuccess();
                }
            }else{
                EpgToast.showToast(context, "订购设置失败");
            }
        }, throwable -> {
            EpgToast.showToast(context, "订购设置失败");
        });
    }

    /**
     * 发送短信验证码
     *
     * @param request
     */
    public void sendSMS(SendSmsRequest request, Context context) {
        HttpApi.getInstance().getService().sendSMS(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.SENDSMS, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<BaseResponse>(HttpConstant.SENDSMS,context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (null != response && null != response.getResult()) {
                            String returnCode = response.getResult().getRetCode();
                            String returnMsg = response.getResult().getRetMsg();
                            if (!TextUtils.isEmpty(returnCode) && !returnCode.equals(Result.RETCODE_OK)) {
                                SuperLog.error(TAG, "returnCode:" + returnCode + ",returnMsg:" + returnMsg);
                                if (returnCode.equals("157021051")) {
                                    //获取短信验证码次数达到上限。
                                    EpgToast.showToast(OTTApplication.getContext(),
                                            OTTApplication.getContext().getString(
                                                    R.string.sms_code_upper_limit));
                                } else {
                                    //发送短信失败。
                                    EpgToast.showToast(OTTApplication.getContext(),
                                            OTTApplication.getContext().getString(
                                                    R.string.sms_code_senderror));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, "sendSMS error");
                    }
                });
    }


    //VRS发送验证码接口
    public void sendVerifiedCode(String mobileNum, Context context){

        SendVerifiedCodeRequest request = new SendVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setMobileNum(mobileNum);

        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            request.setUserId(mUserInfo.getUserId());
        }
        request.setUserType(2);
        request.setSceneType(2);

        String interfaceName = HttpConstant.SEND_VERIFIED_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().sendVerifiedCode(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.SEND_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {

                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //发送短信成功
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_sendsuccess));
                    }else {
                        //发送短信失败。
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_senderror));
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "sendSMS error");
            }
        });
    }

    //VRS验证验证码接口
    public void checkVerifiedCode(String verifiedCode, String number, Context context){

        CheckVerifiedCodeRequest request = new CheckVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setVerifiedCode(verifiedCode);

        request.setUserId(number);
        request.setUserType("1");

        request.setSceneType("2");

        String interfaceName = HttpConstant.CHECK_VERIFIED_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().checkVerifiedCode(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.CHECK_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {
                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //验证码验证成功
                        if (null != mView){
                            mView.checkVerifiedCodeSuccess();
                        }
                    }else{
                        //验证码验证失败
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_checkerror));
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.info2SD(TAG,e.getMessage());
            }
        });
    }


    public void updateUserRegInfo(UpdateUserRegInfoRequest request, Context context){
        HttpApi.getInstance().getService().updateUserRegInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.UPDATE_USER_REGINFO, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<UpdateUserRegInfoResponse>(HttpConstant.UPDATE_USER_REGINFO, context) {
                    @Override
                    public void onSuccess(@NonNull UpdateUserRegInfoResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String resultMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                if(null!=mView){
                                    mView.updateUserRegInfoSucess();
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
                                mView.updateUserRegInfoFail();
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        mView.updateUserRegInfoFail();
                    }
                });
    }

    /**
     * 查询统一支付开通情况
     * @param request request
     */
  public void queryUniPayInfo(QueryUniInfoRequest request, Context context) {
        HttpApi.getInstance().getService().queryUniPayInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERY_UNIPAY_INFO, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<QueryUniPayInfoResponse>(HttpConstant.QUERY_UNIPAY_INFO,context) {
                    @Override
                    public void onSuccess(@NonNull QueryUniPayInfoResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String resultMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                if(null!=mView){
                                    mView.queryUniPayInfoSucc(response);
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
                                if (mView != null) {
                                    mView.loadItemDataFail();
                                }
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        if (mView != null) {
                            mView.loadItemDataFail();
                        }
                    }
                });
    }

    /**
     * 更新订户信息
     * @param request request
     */
    public void updateSubscriber(UpdateSubscriberRequest request, Context context) {
        HttpApi.getInstance().getService().updateSubscriber(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.UPDATE_SUBSCRIBER, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<UpdateSubscriberResponse>(HttpConstant.UPDATE_SUBSCRIBER, context) {
                    @Override
                    public void onSuccess(@NonNull UpdateSubscriberResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String resultMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                if(null!=mView){
                                    mView.updateSubscriberSucess();
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
                                mView.updateSubscriberFail();
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        mView.updateSubscriberFail();
                    }
                });
    }

    /**
     * 自定义用户属性变更接口
     */
    public void modifyUserAttr(ModifyUserAttrRequest request, Context context) {
        HttpApi.getInstance().getService().modifyUserAttr(HttpUtil.getVssUrl(HttpConstant.MODIFY_USER_ATTR), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<ModifyUserAttrResponse>(HttpConstant.MODIFY_USER_ATTR, context) {
                    @Override
                    public void onSuccess(@NonNull ModifyUserAttrResponse response) {
                        if(null!=response && null!=response.getReturnCode()){
                            Integer returnCode = response.getReturnCode();
                            String returnMessage = response.getReturnMessage();
                            if(returnCode == Integer.parseInt(Result.RETCODE_OK_TWO)){
                                if(null!=mView){
                                    mView.updateSubscriberSucess();
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+returnMessage);
                                mView.updateSubscriberFail();
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        mView.updateSubscriberFail();
                    }
                });
    }

    /**
     * 从BookmarkItem取出VOD
     *
     * @param bookmarks
     * @return
     */
    public List<VOD> getBookMarkVODList(List<BookmarkItem> bookmarks) {
        if (CollectionUtil.isEmpty(bookmarks)) {
            return new ArrayList<>();
        }
        List<BookmarkItem> items=new ArrayList<>();
        //儿童模式只展示儿童模式书签
        if(SharedPreferenceUtil.getInstance().getIsChildrenEpg())
        {
            for (int i = 0; i < bookmarks.size(); i++)
            {
                if (IsTV.isChildMode(bookmarks.get(i).getVOD()))
                {
                    items.add(bookmarks.get(i));
                }
            }
        }else{
            items.addAll(bookmarks);
        }
        List<VOD> bookmarkVODList = new ArrayList<>();
        for (BookmarkItem bookmarkItem : items) {
            bookmarkVODList.add(bookmarkItem.getVOD());
        }
        return bookmarkVODList;
    }


    /**
     * 从Content取出VOD
     * @param favorites
     * @return
     */
    public List<VOD> getFavVODList(List<Content> favorites) {
        if (CollectionUtil.isEmpty(favorites)) {
            return new ArrayList<>();
        }
        List<VOD> favOfVODList = new ArrayList<>();
        for (Content favItem : favorites) {
            favOfVODList.add(favItem.getVOD());
        }
        return favOfVODList;
    }

    /**
     * 把VOD转换成vodbean
     *
     * @param vodList
     * @return
     */
    public List<VodBean> convert2SearchVODBean(List<VOD> vodList) {
        List<VodBean> vodBeanList = new ArrayList<>();
        if (!CollectionUtil.isEmpty(vodList)) {
            for (VOD vod : vodList) {
                VodBean vodBean = new VodBean();
                vodBean.setId(vod.getID());
                vodBean.setName(vod.getName());
                vodBean.setRate(Utils.formatRate(vod.getAverageScore()));
                vodBean.setHD(VODMediaFile.Definition.HD.equals(VODMining.getDefinition(vod)));
                vodBean.setDefinition(VODMining.getDefinition(vod));
                vodBean.setCpId(vod.getCpId());
                vodBean.setVODType(vod.getVODType());
                if (VodUtil.isMiguVod(vod)) {
                    vodBean.setPoster(VODMining.getMiguPoster(vod));
                } else {
                    vodBean.setPoster(VODMining.getPoster(vod));
                }
                vodBean.setHCSSlaveAddressList(VODMining.getHCSSlaveAddressList(vod));
                vodBean.setSubjectIds(vod.getSubjectIDs());
                vodBeanList.add(vodBean);
            }
        }
        return vodBeanList;
    }
}