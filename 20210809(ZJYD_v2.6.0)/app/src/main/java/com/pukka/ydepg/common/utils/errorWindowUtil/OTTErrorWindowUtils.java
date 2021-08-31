package com.pukka.ydepg.common.utils.errorWindowUtil;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.session.SessionService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OTTErrorWindowUtils {

    protected final static String TAG = OTTErrorWindowUtils.class.getSimpleName();

    private static final String SHOW_ERROR_WINDOW = "1";

    public static final String ERROR_QRCODE_URL             = "error_QRCode_url";              //故障二维码URL
    public static final String IS_SHOW_ERROR_WINDOW         = "isShowErrorQRCodeNew";          //故障对话框开关 更新key 原参数isShowErrorQRCode自2.4版本后不再使用

    //判断故障二维码开关是否开启
    public static boolean needShowErrorDialog(){
        return SHOW_ERROR_WINDOW.equals(CommonUtil.getConfigValue(IS_SHOW_ERROR_WINDOW));
    }

    @SuppressWarnings("checkResult")
    public static void getErrorInfoFromPbs(String interfaceName,String errorCode,Context context){
        if( TextUtils.isEmpty(interfaceName) ||  TextUtils.isEmpty(errorCode) || !needShowErrorDialog()){
            return;
        }

        String url = getErrorUrl(interfaceName,errorCode);
        HttpApi.getInstance().getService().sendGetRequest(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            SuperLog.info2SD(TAG, "Get error info from PBS successfully.");
                            try{
                                PbsErrorData errorData = JsonParse.json2Object(response.string(), PbsErrorData.class);
                                if( errorData == null ){
                                    SuperLog.info2SD(TAG,"Error window will not show because of PBS returns null.");
                                    showErrorSimple(interfaceName,errorCode);
                                    return;
                                }
                                errorData.setInterfaceName(interfaceName);
                                errorData.setErrorCode(errorCode);

                                handlePbsErrorInfo(errorData,context);
                            } catch (Exception e){
                                SuperLog.error(TAG,e);
                                SuperLog.error(TAG,"Error window will not show because of parse PBS exception.");
                                showErrorSimple(interfaceName,errorCode);
                            }
                        },
                        throwable -> {
                            SuperLog.error(TAG,throwable);
                            SuperLog.error(TAG,"Error window will not show because of PBS return exception.");
                            showErrorSimple(interfaceName,errorCode);
                        });
    }

    private static String getErrorUrl(String interfaceName,String errorCode){
        String activity = OTTApplication.getContext().getCurrentActivity().getClass().getSimpleName();
        String url = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getErrorInfoByCode.jsp";
        url = url + "?userId="    + SessionService.getInstance().getSession().getUserId()
                  + "&billId="    + SessionService.getInstance().getSession().getBillId()
                  + "&errorCode=" + errorCode
                  + "&interface=" + interfaceName
                  + "&activity="  + activity
                  + "&vt=9"; //页面版式，传9表示以json格式返回
        return url;
    }

    private static void showErrorSimple(String interfaceName,String errorCode){
        String message = ErrorCode.findError(interfaceName, errorCode).getMessage();
        EpgToast.showToast(OTTApplication.getContext(), interfaceName + "error: " + message);
    }

    private static void handlePbsErrorInfo(PbsErrorData data,Context context){
        if("0".equals(data.getIsPop())){
            SuperLog.info2SD(TAG,"PBS returns no need to show error window(isPop = 0).");
            showErrorSimple(data.getInterfaceName(),data.getErrorCode());
            return;
        }

        showErrorDialog(data,context);
    }

    private static void showErrorDialog(PbsErrorData data, Context context) {

        if(context == null){
            context = OTTApplication.getContext().getCurrentActivity();
        }
        ErrorWindowDialog dialog = new ErrorWindowDialog(context, R.style.message_dialog);
        dialog.setErrInfo(data.getErrorCode(),data.getErrorDesc(),data.getOperate(), data.getSuggest());
        dialog.show();
    }
}