package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.AdUtil;
import com.pukka.ydepg.common.ad.IADListener;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.fcc.MulticastTool;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.node.Marketing;
import com.pukka.ydepg.common.http.v6bean.v6Controller.SubmitDeviceInfoController;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthenticateBasic;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthenticateDevice;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6request.AuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBindedSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectVODBySubjectIDRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SetQrCodeSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.AuthenticateResponse;
import com.pukka.ydepg.common.profile.ProfileManager;
import com.pukka.ydepg.common.report.ubd.scene.UBDVersion;
import com.pukka.ydepg.common.start.StartResourceManager;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.request.LoginRequest;
import com.pukka.ydepg.launcher.bean.request.QueryCustomizeConfigRequest;
import com.pukka.ydepg.launcher.bean.request.ZJLoginRequest;
import com.pukka.ydepg.launcher.bean.response.LoginResponse;
import com.pukka.ydepg.launcher.bean.response.QueryCustomizeConfigResponse;
import com.pukka.ydepg.launcher.bean.response.ZJLoginResponse;
import com.pukka.ydepg.launcher.http.LoginNetApi;
import com.pukka.ydepg.launcher.mvp.contact.AuthenticateContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.ThreadManager;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.home.mvp.contact.StartHeartBitEvent;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.xmpp.XmppService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * ??????Presenter
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.AuthenticatePresenter.java
 * @date: 2018-01-09 13:57
 * @version: V1.0 ????????????????????????
 */
public class AuthenticatePresenter<T extends AuthenticateContact.IAuthenticateView> extends BasePresenter<T> implements AuthenticateContact.IAuthenticatePresenter {

    private static final String TAG = AuthenticatePresenter.class.getSimpleName();

    private IADListener adListener = new IADListener() {
        @Override
        public void onSuccess(List<AdvertContent> listAdvertContent) {
            mView.loadAdvertContentSuccess(listAdvertContent);
        }

        @Override
        public void onFail() {
            SuperLog.info2SD(TAG,"Get SSP start advert failed. Go on to next step.");
            mView.loadAdvertFail();
        }
    };

    /**
     * token????????????????????????
     * 1.????????????(??????token->ZJJLogin->queryCustomizeConfig->queryBindedSubscriber->getStartPicture)
     * 2.????????????
     * 3.queryTvGuideColumn
     */
    @Override
    public void reLogin(Context context) {
        SuperLog.info2SD(TAG, "reLogin");
        doBaseAuthenticate(context, true).flatMap(zjLoginResponse -> {
            EventBus.getDefault().post(new StartHeartBitEvent());
            if (!TextUtils.isEmpty(OTTApplication.getContext().getLastNavID())
                    && null != OTTApplication.getContext().getGroupElements()) {
                SuperLog.info2SD(TAG, "QueryEpgHomeVod failed last time, navID=" + OTTApplication.getContext().getLastNavID());
                new TabItemPresenter(OTTApplication.getContext().getView()).queryHomeEpg(
                        OTTApplication.getContext().getLastNavID(),
                        OTTApplication.getContext().getGroupElements(), context);
            }

            return afterAuthenticate(context);
        }).subscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool()))
                .unsubscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool())).subscribe(subjectVODLists -> {
            List<Subject> subjectList = new ArrayList<>();
            for (SubjectVODList vodList : subjectVODLists) {
                subjectList.add(vodList.getSubject());
            }
            //????????????????????????,TVGUIDE????????????????????????????????????
            LiveTVCacheUtil.getInstance().cacheColumnList(subjectList);
        }, throwable -> {
            SuperLog.error(TAG, throwable);
        });
    }


    /**
     * ???????????????????????????,?????????reLogin???????????????token???????????????????????????????????????,isReport?????????????????????????????????????????????
     * 1.??????token
     * 2.zjlogin
     * 3.queryCustomizeConfig
     * 4.????????????
     * 5.queryTvGuideColumn
     */
    public void reLoginAfterSwitchProfile(Context context) {
        doSwitchAuthenticate(context).flatMap(zjLoginResponse -> {
            SubmitDeviceInfoController.submitDeviceInfo(context);

            return afterAuthenticate(context);
        }).subscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool()))
                .unsubscribeOn(Schedulers.from(ThreadManager.getInstance().getSingleThreadPool())).subscribe(subjectVODLists -> {
            List<Subject> subjectList = new ArrayList<>();
            for (SubjectVODList vodList : subjectVODLists) {
                subjectList.add(vodList.getSubject());
            }
            //????????????????????????,TVGUIDE????????????????????????????????????
            LiveTVCacheUtil.getInstance().cacheColumnList(subjectList);
        }, throwable -> {
            SuperLog.debug(TAG, throwable.getMessage());
        });
    }

    /**
     * ??????????????????????????????????????????ZJLogin??????????????????
     * ??????????????????????????????????????????????????????????????????LauncherPresenter??????
     * ??????
     * 1.??????token
     * 2.ZJLogin
     * 3.????????????????????????
     * 4.queryCustomizeConfig
     * 5.?????????????????????service
     * 6.queryTvGuideColumn
     */
    @Override
    public void firstLogin(Context context) {
    }

    /**
     * ??????????????????????????????
     * querySubjectVODBySubjectID
     *
     * @return
     */
    public Observable<List<SubjectVODList>> afterAuthenticate(Context context) {
        return queryTvGuideColumn(context);
    }

    /**
     * ??????????????????
     * 1.??????token
     * 2.ZJLogin
     * 3.querySubscribe
     * 4.queryCustomizeConfig
     * 5.queryBindedSubscriber
     *
     * @return
     */
    public Observable<QueryCustomizeConfigResponse> doSwitchAuthenticate(Context context) {
        return Observable.create(subscribe -> {
            //1.??????token
            subscribe.onNext(AuthenticateManager.getInstance().getTokenTotal());
        }).filter(token -> {
            if (TextUtils.isEmpty((String) token)) {
                // ??????APK?????????Token ?????????????????????
                // getTokenTotal()??????null????????????reportTokenStatus???????????????????????????APK??????
                SuperLog.info2SD(TAG, "[Switch]Get token failed, wait for retry.");
                return false;
            } else {
                //??????APK?????????Token?????????????????????????????????
                AuthenticateManager.getInstance().saveTokenAndUserInfo((String) token);
                SuperLog.info2SD(TAG, "[Switch-1]Get token successfully and begin to send zjLogin request.");
                return true;
            }
        }).flatMap(token -> {
            //2.??????zjLogin
            return zjLogin();
        }).filter(zjLoginResponse -> {
            if (TextUtils.equals(zjLoginResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                if (!checkResponse(zjLoginResponse)) { //??????response????????????
                    return false;
                }
                saveZjLoginData(zjLoginResponse);  //??????zjLogin???????????????
                return true;
            } else {
                handleErrorIncludeTimeOut(zjLoginResponse.getResult().getRetCode(), "ZJLogin", context);
                return false;
            }
        }).flatMap(zjLoginResponse -> {
            //?????????????????? ??????????????????????????????????????????launcher????????????
            SuperLog.info2SD(TAG, "[Switch-2(??????)]Send QuerySubscriber finished.");
            return HttpApi.getInstance().getService().querySubscriberInfo(HttpUtil.getVspUrl(HttpConstant.QUERY_SUBSCRIBE_INFO),new QueryUniInfoRequest());
        }).filter(querySubscriberResponse -> {
            if (TextUtils.equals(querySubscriberResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                List<NamedParameter> customFields = querySubscriberResponse.getSubscriber().getCustomFields();
                String billID = CommonUtil.getCustomField(customFields, "BILL_ID");
                SessionService.getInstance().getSession().setAccountName(TextUtils.isEmpty(billID) ? "" : billID);

                MulticastTool.getInstance().startTestFcc(()->{
                    SuperLog.info2SD(TAG, "[Switch-3(??????)]Test Fcc finished.");
                });
                //????????????????????????????????????
                UBDVersion.record();
                LauncherService.getInstance().checkAndUpdateAfterSwitch(context);
                TopicService.getInstance().checkAndUpdateAfterSwitch(context);
                AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.START, adListener, null);
                //Added by liuxia for switch user
                SuperLog.info2SD(TAG, " <<<<<<<<<< Switch Login User[" + SessionService.getInstance().getSession().getUserId() + "] successfully. Begin to re-initialize XMPP connection >>>>>>>>>> ");
                XmppService.getInstance().reConnectXmpp();
                return true;
            }else{
                return false;
            }
        }).flatMap(zjLoginResponse -> {
            //3.??????CustomizeConfig
            return queryCustomizeConfig().filter(queryCustomizeConfigResponse -> {
                //????????????????????????????????????????????????
                OTTApplication.getContext().setNeedShowNetworkExceptionDialog(true);
                if (TextUtils.equals(queryCustomizeConfigResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                    OTTApplication.getContext().setLoginSuccess(true);
                    SessionService.getInstance().getSession().setQueryCustomizeConfigResponse(queryCustomizeConfigResponse);
                    SessionService.getInstance().commitSession();
                    queryBindedSubscriber();
                    AdUtil.setAdvertUrl();
                    AdUtil.querySspMember();
                    Map<String, Marketing> marketingMap = SessionService.getInstance().getSession().getTerminalConfigurationMarketing();
                    if (null != marketingMap) {
                        List<String> productIDs = new ArrayList<>();
                        for (Marketing m : marketingMap.values()) {
                            productIDs.add(m.getId());
                        }
                        queryProduct(productIDs, -1, 0);
                    }
                    queryUniPayInfo(new QueryUniInfoRequest());
                    new StartResourceManager().getStartResource();
                    ProfileManager.isChangeUser = true;       //?????????????????????????????????Profile
                    ProfileManager.isProfileFinished = false; //???????????????????????????????????????
                    return true;
                } else {
                    handleErrorIncludeTimeOut(queryCustomizeConfigResponse.getResult().getRetCode(), HttpConstant.QUERYCUSTOMIZECONFIG, context);
                    return false;
                }
            });
        });
    }

    /**
     * ????????????
     * 1.??????token
     * 2.ZJLogin
     * 3.querySubscribe
     * 4.queryCustomizeConfig
     * 5.queryBindedSubscriber
     * 6.getStartPicture
     * 7.queryUniPayInfo
     * ??????????????????????????????????????????????????????????????????   doSwitchAuthenticate  ????????????
     *
     * @return
     */
    public Observable<QueryCustomizeConfigResponse> doBaseAuthenticate(Context context, boolean isRelogin) {
        return Observable.create(subscribe -> {
            //1.??????token
            subscribe.onNext(CommonUtil.isSelfAuthenticate()? HttpConstant.AUTHENTICATE : AuthenticateManager.getInstance().getTokenTotal());
        }).filter(token -> {
            if(CommonUtil.isSelfAuthenticate()){
                //??????????????? 20200309?????????
                SuperLog.info2SD(TAG,"Self login flow , continue 1.");
                //TODO EDS???????????????????????????
                AuthenticateManager.getInstance().saveVspUrl("117.148.130.129","7220");
                return true;
            } else {
                //??????APK????????????
                if (TextUtils.isEmpty((String) token)) {
                    // ??????APK????????? ?????????????????????
                    // getTokenTotal()??????null????????????reportTokenStatus???????????????????????????APK??????
                    SuperLog.info2SD(TAG, "Get token failed, wait for retry.");
                    return false;
                } else {
                    //??????APK?????????
                    if (token == AuthenticateManager.getInstance().getLocalToken()) {
                        //??????APK?????????Token?????????Token??????,?????????????????????
                        SuperLog.info2SD(TAG, "[LOGIN-1]The token does not change, no need to zjLogin again.");
                        return false;
                    } else {
                        SuperLog.info2SD(TAG, "[LOGIN-1]Get token successfully and begin to send zjLogin request.");
                        //????????????Token?????????????????????URL?????????????????????
                        AuthenticateManager.getInstance().saveTokenAndUserInfo((String) token);
                        return true;
                    }
                }
            }
        }).flatMap(token -> {
            if(CommonUtil.isSelfAuthenticate()){
                //20200309????????????????????????
                SuperLog.info2SD(TAG,"Self login flow , continue 2. loginResponse="+token);
                return authLogin();
            } else {
                //???????????? 2.??????zjLogin
                return zjLogin();
            }
        }).filter(response -> { //response?????????zjLoginResponse??????authenticateResponse
            if( CommonUtil.isSelfAuthenticate() ){
                //20200309????????????????????????
                AuthenticateResponse auResponse = (AuthenticateResponse) response;
                if( Result.RETCODE_OK.equals(auResponse.getResult().getRetCode()) ){
                    saveAuthenticateData((AuthenticateResponse) response);
                    return true;
                } else {
                    handleErrorIncludeTimeOut(auResponse.getResult().getRetCode(), HttpConstant.AUTHENTICATE, context);
                    return false;
                }
            } else {
                ZJLoginResponse zjResponse = (ZJLoginResponse) response;
                if (Result.RETCODE_OK.equals(zjResponse.getResult().getRetCode())) {
                    //??????response????????????
                    if (!checkResponse(zjResponse)) {
                        return false;
                    }
                    saveZjLoginData(zjResponse);  //??????zjLogin???????????????
                    return true;
                } else {
                    handleErrorIncludeTimeOut(zjResponse.getResult().getRetCode(), "ZJLogin", context);
                    return false;
                }
            }
        }).flatMap(zjLoginResponse -> {
            //?????????????????????????????????launcher?????????
            return HttpApi.getInstance().getService().querySubscriberInfo(HttpUtil.getVspUrl(HttpConstant.QUERY_SUBSCRIBE_INFO),new QueryUniInfoRequest());
        }).filter(querySubscriberResponse -> {
            if (TextUtils.equals(querySubscriberResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                List<NamedParameter> customFields = querySubscriberResponse.getSubscriber().getCustomFields();
                String billID = CommonUtil.getCustomField(customFields,"BILL_ID");
                SessionService.getInstance().getSession().setAccountName(TextUtils.isEmpty(billID)?"":billID);

                if (!isRelogin) {
                    //???????????????????????????FCC,????????????????????????????????????,???????????????????????????
                    MulticastTool.getInstance().startTestFcc(()->{
                        SuperLog.info2SD(TAG,"Test FCC finished. Begin to query SSP startup advert.");
                        //?????????????????????????????????????????????,????????????????????????,??????????????????
                        AdManager.getInstance().queryAdvertContent(AdConstant.AdClassify.START, adListener, null);
                    });
                }
                return true;
            } else {
                handleErrorIncludeTimeOut(querySubscriberResponse.getResult().getRetCode(), HttpConstant.QUERY_SUBSCRIBE_INFO, context);
                return false;
            }
        }).flatMap(zjLoginResponse -> {
            //3.??????CustomizeConfig
            return queryCustomizeConfig().filter(queryCustomizeConfigResponse -> {
                //????????????????????????????????????????????????
                SuperLog.info2SD(TAG, "[LOGIN-4]Get queryCustomizeConfigResponse finished.");
                OTTApplication.getContext().setNeedShowNetworkExceptionDialog(true);
                if (TextUtils.equals(queryCustomizeConfigResponse.getResult().getRetCode(), Result.RETCODE_OK)) {
                    OTTApplication.getContext().setLoginSuccess(true);
                    SessionService.getInstance().getSession().setQueryCustomizeConfigResponse(queryCustomizeConfigResponse);
                    SessionService.getInstance().commitSession();

                    //???????????????????????????????????????????????????,??????XMPP???????????????????????????????????????????????????
                    SuperLog.info2SD(TAG, "Begin to update XMPP token after ZJLogin.");
                    XmppService.getInstance().updateToken(SessionService.getInstance().getSession().getUserToken());

                    queryBindedSubscriber();
                    new StartResourceManager().getStartResource();

                    AdUtil.setAdvertUrl();
                    AdUtil.querySspMember();
                    Map<String, Marketing> marketingMap = SessionService.getInstance().getSession().getTerminalConfigurationMarketing();
                    if (null != marketingMap) {
                        List<String> productIDs = new ArrayList<>();
                        for (Marketing m : marketingMap.values()) {
                            productIDs.add(m.getId());
                        }
                        queryProduct(productIDs, -1, 0);
                    }
                    queryUniPayInfo(new QueryUniInfoRequest());
                    return true;
                } else {
                    handleErrorIncludeTimeOut(queryCustomizeConfigResponse.getResult().getRetCode(), HttpConstant.QUERYCUSTOMIZECONFIG, context);
                    return false;
                }
            });
        });
    }

    private void queryBindedSubscriber() {
        QueryBindedSubscriberRequest request = new QueryBindedSubscriberRequest();
        HttpApi.getInstance().getService().qeryBindedSubscriber(HttpUtil.getVspUrl(HttpConstant.QUERYBINDEDSUBSCRIBER), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(queryBindedSubscriberResponse -> {
                    if (null != queryBindedSubscriberResponse && queryBindedSubscriberResponse.isSuccess()) {
                        List<String> bindedSubscribers = queryBindedSubscriberResponse.getBindedSubscribers();
                        //?????? ???????????? ??????????????????
                        if (null != bindedSubscribers) {
                            if (bindedSubscribers.size() > 0) {
                                //??????????????????
                                setQrCodeAuthenticatedSubscriber(bindedSubscribers.get(bindedSubscribers.size() - 1));
                            }
                        }
                    }
                }, throwable -> SuperLog.error(TAG, throwable));
        SuperLog.info2SD(TAG, "[LOGIN-5(??????)]Send QueryBindedSubscriber finished.");
    }

    private void setQrCodeAuthenticatedSubscriber(String subscriberId) {
        SetQrCodeSubscriberRequest request = new SetQrCodeSubscriberRequest();
        request.setQrCodeAuthenticatedSubscriberId(subscriberId);
        HttpApi.getInstance().getService().setQrCodeAuthenticatedSubscriber(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.SETQRCODEAUTHENTICATEDSUBSCRIBER, request).compose(onCompose(mView.bindToLife()))
                .subscribe(setQrCodeSubscriberResponse -> {
                });
    }

    /**
     * ??????profileid?????????zj????????????????????????
     *
     * @param response
     * @return
     */
    private boolean checkResponse(ZJLoginResponse response) {
        boolean isValid = false;
        List<Profile> profileList = response.getProfileList();
        String profileId = response.getProfileID();
        if (!TextUtils.isEmpty(profileId) && null != profileList) {
            for (Profile profile : profileList) {
                if (profileId.equals(profile.getID())) {
                    isValid = true;
                    break;
                }
            }
        }
        SuperLog.info2SD(TAG, "ZJLogin response validity is : " + isValid + "(false means critical error!)");
        return isValid;
    }

    /**
     * ??????zj?????????????????????
     *
     * @param response
     */
    private void saveZjLoginData(ZJLoginResponse response) {
        SharedPreferenceUtil.getInstance().saveSessionId(response.getjSessionID());
        //UserId
        String userId = AuthenticateManager.getInstance().getUserInfo().getUserId();
        SessionService.getInstance().getSession().setUserId(userId);

        // ???????????????????????????
        SessionService.getInstance().getSession().setUserAreaCode(response.getAreaCode());
        // userGroup??????UBD????????????(??????)
        SessionService.getInstance().getSession().setUserGroup(response.getUserGroup());
        // Added by l00190891 for UBD
        SessionService.getInstance().getSession().setDeviceId(response.getDeviceID());
        if (!TextUtils.isEmpty(response.getUserToken())) {
            SessionService.getInstance().getSession().setUserToken(response.getUserToken());
        }

        SessionService.getInstance().getSession().setProfileSN(response.getProfileSN());

        saveProfile(response, userId);
        SessionService.getInstance().commitSession();
    }

    /**
     * ??????zj?????????????????????
     *
     * @param response
     */
    private void saveAuthenticateData(AuthenticateResponse response) {
        SharedPreferenceUtil.getInstance().saveSessionId(response.getjSessionID());
        // ???????????????????????????
        SessionService.getInstance().getSession().setUserAreaCode(response.getAreaCode());
        // userGroup??????UBD????????????(??????)
        SessionService.getInstance().getSession().setUserGroup(response.getUserGroup());
        // Added by l00190891 for UBD
        SessionService.getInstance().getSession().setDeviceId(response.getDeviceID());
        if (!TextUtils.isEmpty(response.getUserToken())) {
            SessionService.getInstance().getSession().setUserToken(response.getUserToken());
        }

        SessionService.getInstance().getSession().setProfileSN(response.getProfileSN());

        saveProfile(response);
        SessionService.getInstance().commitSession();
    }

    public void saveProfile(ZJLoginResponse response, String userId) {
        SuperLog.info(TAG, "saveProfile()");
        List<Profile> profileList = response.getProfileList();
        SessionService.getInstance().getSession().setProfileList(profileList);
        SessionService.getInstance().getSession().setProfile(null);
        String profileId = response.getProfileID();

        if (null != profileList && profileList.size() > 0) {
            SuperLog.debug(TAG, "ProfileId:" + response.getProfileID() + ";name:" + profileList.get(0).getName());
        } else {
            SuperLog.debug(TAG, "ProfileId:" + response.getProfileID() + ";profileList.size====0");
        }
        // for xmpp, guest user must has a profileId, or login xmpp server will fail.
        if (TextUtils.isEmpty(userId)) {
            SuperLog.debug(TAG, "userId is empty or null, use return profileId.");
            SessionService.getInstance().getSession().setUserId(profileId);
        }

        if (!TextUtils.isEmpty(profileId) && null != profileList) {
            boolean match = false;
            for (Profile profile : profileList) {
                if (profileId.equals(profile.getID())) {
                    SessionService.getInstance().getSession().setProfile(profile);
                    match = true;
                    break;
                }
            }
            if (!match) {
                SuperLog.error(TAG, "profileId is not match");
            }
        } else {
            SuperLog.error(TAG, "profile is empty");
        }
    }

    public void saveProfile(AuthenticateResponse response) {
        SuperLog.info(TAG, "saveProfile()");
        List<Profile> profileList = response.getProfiles();
        SessionService.getInstance().getSession().setProfileList(profileList);
        SessionService.getInstance().getSession().setProfile(null);
        String profileId = response.getProfileID();

        if (null != profileList && profileList.size() > 0) {
            SuperLog.debug(TAG, "ProfileId:" + response.getProfileID() + ";name:" + profileList.get(0).getName());
        } else {
            SuperLog.debug(TAG, "ProfileId:" + response.getProfileID() + ";profileList.size====0");
        }
        // for xmpp, guest user must has a profileId, or login xmpp server will fail.
        String userId = response.getSubscriberID();
        if (TextUtils.isEmpty(userId)) {
            SuperLog.debug(TAG, "userId is empty or null, use return profileId.");
            userId = profileId;
        }
        SessionService.getInstance().getSession().setUserId(userId);
        AuthenticateManager.getInstance().getUserInfo().setUserId(userId);

        if (!TextUtils.isEmpty(profileId) && null != profileList) {
            boolean match = false;
            for (Profile profile : profileList) {
                if (profileId.equals(profile.getID())) {
                    SessionService.getInstance().getSession().setProfile(profile);
                    match = true;
                    break;
                }
            }
            if (!match) {
                SuperLog.error(TAG, "profileId is not match");
            }
        } else {
            SuperLog.error(TAG, "profile is empty");
        }
    }


    public Observable<LoginResponse> login() {
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + Constant.URL.ZJ_LOGIN;
        return LoginNetApi.getInstance().getService().login(url, new LoginRequest());
    }

    //??????ZJlogin????????????
    public Observable<ZJLoginResponse> zjLogin() {
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + Constant.URL.ZJ_LOGIN;
        return LoginNetApi.getInstance().getService().zjLogin(url, new ZJLoginRequest(AuthenticateManager.getInstance().getLocalToken()));
    }

    //??????Authenticate????????????
    public Observable<AuthenticateResponse> authLogin() {
        AuthenticateRequest request = new AuthenticateRequest();
        AuthenticateBasic basic = new AuthenticateBasic();

        basic.setAuthType("1");
        basic.setUserID("hwtest392");
        basic.setClientPasswd("123123");
        basic.setUserType("0");
        request.setAuthenticateBasic(basic);

        AuthenticateDevice device = new AuthenticateDevice();
        device.setAuthTerminalID("17FB9AC0-A016-4A3A-B3E7-8E520AEA34AD");
        device.setPhysicalDeviceID("17FB9AC0-A016-4A3A-B3E7-8E520AEA34AD");
        device.setDeviceModel("STB");//"?????????stb???????????????"
        request.setAuthenticateDevice(device);

        String url = HttpUtil.getVspUrl(HttpConstant.AUTHENTICATE);
        //url = "http://117.148.130.129:7220/VSP/V3/Authenticate";
        return HttpApi.getInstance().getService().authenticate(url,request);
    }

    /**
     * ??????queryCustomizeConfig??????
     *
     * @return
     */
    public Observable<QueryCustomizeConfigResponse> queryCustomizeConfig() {
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + Constant.URL.CUSTOMSIZE_CONFIG;
        QueryCustomizeConfigRequest request = new QueryCustomizeConfigRequest
                (Constant.QueryType.TERMINAL_CONFIG,
                        Constant.QueryType.EPG_CONFIG,
                        Constant.QueryType.CONTENT_LEVEL,
                        Constant.QueryType.USABLE_LANGUAGE,
                        Constant.QueryType.CURRENCY_CODE);
        return LoginNetApi.getInstance().getService().queryCustomizeConfig(url, request);
    }

    /**
     * ??????TVGUIDE????????????
     * <p>
     * querySubjectVODBySubjectID
     */
    public Observable<List<SubjectVODList>> queryTvGuideColumn(Context context) {
        QuerySubjectVODBySubjectIDRequest request = new QuerySubjectVODBySubjectIDRequest();
        //???????????????????????????"tvwatch_subject_id"??????key??????value?????????subjectId
        request.setSubjectID(SessionService.getInstance().getSession()
                .getTerminalConfigurationValue(Configuration.Key.TV_WATCH_SUBJECT_ID));
        request.setVODCount("0");
        request.setOffset("0");
        request.setSubjectCount("50");
        return HttpApi.getInstance().getService().querySubjectVODBySubjectID(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.QUERYSUBJECTVODBYSUBJECTID, request).filter(response -> {
            String returnCode = "";
            if (null != response) {
                Result result = response.getResult();
                if (null != result && (returnCode = result.getRetCode()).equals(Result.RETCODE_OK)) {
                    return true;
                }
            }
            handleErrorIncludeTimeOut(returnCode, HttpConstant.QUERYSUBJECTVODBYSUBJECTID, context);
            return false;
        }).map(querySubjectVODBySubjectIDResponse -> {
            return querySubjectVODBySubjectIDResponse.getSubjectVODLists();
        }).filter(subjectVODLists -> {
            return null != subjectVODLists && subjectVODLists.size() > 0;
        });
    }

    private void queryProduct(List<String> ids, int count, int offset) {
        QueryProductRequest request = new QueryProductRequest();
        request.setQueryType(QueryProductRequest.QueryType.BY_IDLIST);
        request.setProductIds(ids);
        request.setCount(count);
        request.setOffset(offset);
        HttpApi.getInstance().getService().queryProduct(HttpUtil.getVspUrl(HttpConstant.QUERY_PRODUCT), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(queryProductResponse -> {
                    if (null != queryProductResponse && null != queryProductResponse.getResult() && TextUtils.equals(Result.RETCODE_OK, queryProductResponse.getResult().getRetCode())) {
                        List<Product> products = queryProductResponse.getProductList();
                        if (!CollectionUtil.isEmpty(products)) {
                            Map<String, Product> marketProducts = new HashMap<>();
                            Map<String, Marketing> marketingMap = SessionService.getInstance().getSession().getTerminalConfigurationMarketing();
                            if (null != marketingMap) {
                                for (String productId : marketingMap.keySet()) {
                                    Marketing marketing = marketingMap.get(productId);
                                    String marketId = marketing.getId();
                                    for (int i = 0; i < products.size(); i++) {
                                        if (marketId.equals(products.get(i).getID())) {
                                            products.get(i).setMarketing(marketing);
                                            marketProducts.put(productId, products.get(i));
                                            break;
                                        }
                                    }
                                }
                            }
                            if (marketProducts.size() > 0) {
                                SessionService.getInstance().getSession().setMarketProducts(marketProducts);
                            }

                        }
                    }
                });
        SuperLog.info2SD(TAG, "[LOGIN-7(??????)]Send QueryProduct finished.");
    }

    private void queryUniPayInfo(QueryUniInfoRequest request) {
        HttpApi.getInstance().getService().queryUniPayInfo(HttpUtil.getVspUrl(HttpConstant.QUERY_UNIPAY_INFO), request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(queryUniPayInfoResponse -> {
                    if (null != queryUniPayInfoResponse && null != queryUniPayInfoResponse.getResult()) {
                        String returnCode = queryUniPayInfoResponse.getResult().getRetCode();
                        if (Result.RETCODE_OK.equals(returnCode)) {
                            if (!"0".equals(queryUniPayInfoResponse.getPayType())) {
                                List<UniPayInfo> uniPayInfoList = queryUniPayInfoResponse.getUniPayList();
                                if (uniPayInfoList != null && !uniPayInfoList.isEmpty()) {
                                    for (UniPayInfo uniPayInfo : uniPayInfoList) {
                                        if ("2".equals(uniPayInfo.getRoleID())) {
                                            //??????????????????????????????????????????VSP??????QueryUniPayInfo????????????rollid=2???billID?????????billID=hzjga26830016
                                            SessionService.getInstance().getSession().setBillId(uniPayInfo.getBillID());
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
        SuperLog.info2SD(TAG, "[LOGIN-8(??????)]Send QueryUniPayInfo finished.");
    }

}