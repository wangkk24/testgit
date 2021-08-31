package com.pukka.ydepg.common.report.problem;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.session.SessionService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VSPError {

    private static String TAG = VSPError.class.getSimpleName();

    private static String PBS_ERROR_SERVER_URL = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getErrorInfoByCode.jsp";

    //向PBS获取错误的处理方式,信息等
    @SuppressWarnings("CheckResult")
    public void getErrorSolution(String interfaceName,String errorCode){
        String url = PBS_ERROR_SERVER_URL
                + "?userId="    + SessionService.getInstance().getSession().getUserId()
                + "&billId="    + SessionService.getInstance().getSession().getBillId()
                + "&errorCode=" + errorCode
                + "&interface=" + interfaceName
                + "&activity="  + OTTApplication.getContext().getCurrentActivity()
                + "&vt=9";        //页面版式，传9表示以json格式返回
        HttpApi.getInstance().getService().sendGetRequest(url)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    //主线程执行
                    try{
                        String strResponse = response.string();
                        PbsErrorInfoResponse pbsResponse = JsonParse.json2Object(strResponse,PbsErrorInfoResponse.class);
                        SuperLog.info2SD(TAG,"stb_getErrorInfoByCode successfully, response is : \n\t" + pbsResponse);
                        pbsResponse.setInterfaceName(interfaceName);
                        pbsResponse.setErrorCode(errorCode);
                        handlePbsErrorResponse(pbsResponse);
                    } catch ( Exception e){
                        SuperLog.error(TAG,e);
                        SuperLog.error(TAG,"Get Pbs error info failed.");
                    }
                },
                throwable -> {
                    SuperLog.error(TAG,throwable);
                    SuperLog.error(TAG,"Get Pbs error info exception.");
                });
    }

    private void handlePbsErrorResponse(PbsErrorInfoResponse pbsErrorInfoResponse){
        if( pbsErrorInfoResponse == null ){
            return;
        }

        if( "1".equals(pbsErrorInfoResponse.getIsPop())){
            showErrorDialog();
        } else {
            //
            EpgToast.showLongToast(OTTApplication.getContext(),"发生错误|" + pbsErrorInfoResponse.getInterfaceName() + "|" + pbsErrorInfoResponse.getErrorCode());
        }
    }

    private void showErrorDialog(){

    }
}
