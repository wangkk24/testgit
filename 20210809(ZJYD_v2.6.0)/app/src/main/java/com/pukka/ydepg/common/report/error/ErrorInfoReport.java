package com.pukka.ydepg.common.report.error;

import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**  
  * @ProjectName:  ZJYD
  * @Description:  当产生平台接口调用错误时,EPG需要按指定格式发送广播,以便软探针采集上报,错误码映射文件errorCodeMap.json   
  * @Author:       liuxia   
  * @CreateDate:   2018/12/06   
  * @UpdateUser:      
  * @UpdateDate:     
  * @UpdateRemark: 说明本次修改内容] 
  * @Version:      v1.0   
  */
public class ErrorInfoReport {

    private static final String TAG = ErrorInfoReport.class.getSimpleName();

    private static final String REPORT_ERROR_ACTION = "com.cmcc.mid.softdetector.errorcode";

    private static final String PACKAGE_NAME = OTTApplication.getContext().getPackageName();

    private static final String ERROR_MAP_FILE = "errorCodeMap.json";//error config file

    private static final String DEFAULT_ERROR_CODE = "default";

    private static final String VERSION_NAME = CommonUtil.getVersionName();

    private static ErrorInfoReport gInstance = new ErrorInfoReport();

    private static HashMap<String, HashMap<String,String>> mapInterface2ErrorMap = new HashMap<>();

    public static synchronized ErrorInfoReport getInstance() {
        if (gInstance == null) {
            gInstance = new ErrorInfoReport();
        }
        return gInstance;
    }

    private ErrorInfoReport() {
        SuperLog.debug(TAG, "Start to extract reporting error info");
    }

    //文件读写及超大JSON对象反序列化,耗时操作,需放置在子线程执行
    public void loadReportErrorInfo() {
        synchronized (this) {    //此时防止读取，加载结束后再允许读取
            SuperLog.info2SD(TAG, "Start to extract from errorCodeMap.json");
            String errorCodeString = getFileFromAssets(ERROR_MAP_FILE);
            if (errorCodeString == null) {
                return;
            }

            ErrorMap error = JsonParse.json2Object(errorCodeString, ErrorMap.class);
            if (null != error) {
                buildMap(error.getListErrorCodeMap());
            }
            SuperLog.info2SD(TAG, "Finish to load errorCodeMap.json");
        }
    }

    /**
     * 从assets文件中读取错误码文件
     *
     * @param fileName 错误码文件名
     * @return String 错误码文件
     */
    private String getFileFromAssets(String fileName)
    {
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        try {
            inputReader  = new InputStreamReader(OTTApplication.getContext().getResources().getAssets().open(fileName));
            bufferReader = new BufferedReader(inputReader);
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = bufferReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
        catch (Exception e) {
            SuperLog.error(TAG,e);
        }
        finally {
            FileUtil.closeReader(inputReader);
            FileUtil.closeReader(bufferReader);
        }
        return null;
    }

    /**
     * 将错误码list构建成Map,便于数据查询
     *
     * @param listErrorInfo 错误码映射信息列表
     */
    private void buildMap(ArrayList<ErrorInfo> listErrorInfo) {
        if (null == listErrorInfo || listErrorInfo.isEmpty()) {
            SuperLog.error(TAG, "errorNodeList is null or empty");
            return;
        }

        for (ErrorInfo error:listErrorInfo ) {
            String interfaceName = error.getInterfaceName();
            HashMap<String,String> errorMap = mapInterface2ErrorMap.get(interfaceName);
            if( errorMap == null ){
                errorMap = new HashMap<>();
            }
            errorMap.put(error.getInternalErrorCode(),error.getExternalErrorCode());
            mapInterface2ErrorMap.put(interfaceName,errorMap);
        }
    }

    public void reportErrorCode(String interfaceName, String internalErrorCode){
        new Thread(() -> {
            synchronized (this){    //此时防止读取，加载结束后再允许读取
                try{
                    HashMap<String,String> errorMap = mapInterface2ErrorMap.get(interfaceName);
                    if ( errorMap != null ){
                        String externalErrorCode = errorMap.get(internalErrorCode);
                        if( TextUtils.isEmpty(externalErrorCode) ){
                            externalErrorCode = errorMap.get(DEFAULT_ERROR_CODE);
                        }
                        StringBuilder sb = new StringBuilder("Interface[")
                                .append(interfaceName)
                                .append("] internal error code[")
                                .append(internalErrorCode)
                                .append("] -> external error code[")
                                .append(externalErrorCode)
                                .append("]");
                        SuperLog.info2SD(TAG,sb.toString());
                        if( !TextUtils.isEmpty(externalErrorCode) ){
                            Intent intent = new Intent();
                            intent.setAction(REPORT_ERROR_ACTION);
                            intent.putExtra("packageName",   PACKAGE_NAME);
                            intent.putExtra("packageVersion",VERSION_NAME);
                            intent.putExtra("errorCode", externalErrorCode);
                            OTTApplication.getContext().sendBroadcast(intent);
                            SuperLog.info2SD(TAG,"External error code has been send to " + REPORT_ERROR_ACTION);
                        } else {
                            SuperLog.error(TAG,"");
                        }
                    } else {
                        SuperLog.error(TAG,"There is no report error info for interface " + interfaceName);
                    }
                }catch (Exception e){
                    SuperLog.error(TAG,e);
                }
            }
        }).start();
    }
}