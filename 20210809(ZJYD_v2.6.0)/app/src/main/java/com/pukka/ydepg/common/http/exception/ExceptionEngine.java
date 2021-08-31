package com.pukka.ydepg.common.http.exception;

import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.response.ErrorResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.Strings;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ExceptionEngine.java
 * @author: yh
 * @date: 2016-11-07 16:49
 */

public class ExceptionEngine {

    private static final String TAG = ExceptionEngine.class.getSimpleName();
    //拼接在前面
    private static final int HEAD                  = 100;

    //对应HTTP的状态码
    private static final int UNAUTHORIZED          = 401;
    private static final int FORBIDDEN             = 403;
    private static final int NOT_FOUND             = 404;
    private static final int REQUEST_TIMEOUT       = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY           = 502;
    private static final int SERVICE_UNAVAILABLE   = 503;
    private static final int GATEWAY_TIMEOUT       = 504;

    private static final int SERVER_EXCEPTION      = 700;
    private static final int JSON_PARES_EXCEPTION  = 701;
    private static final int CONNECT_EXCEPTION     = 702;
    private static final int SOCKET_TIMEOUT_EXCEPTION= 703;
    private static final int UNKNOWN_ERROR         = 801;
    private static final int UNKNOWN_HTTP          = 800;

    private String message;

    private int errorCode;

    public ExceptionEngine(int errorCode) {
        this.errorCode = errorCode;
    }

    public static ExceptionEngine handleException(Throwable e){

        SuperLog.error(TAG,"handleException()");

        ExceptionEngine ex;
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;

            ex = new ExceptionEngine(ERROR.HTTP_ERROR);

//            Response response = httpException.response();

//            if(response != null && response.errorBody() != null){
//                try{
//                    String errorBody = response.errorBody().string();
//                    SuperLog.error(TAG,"handleException() errorBody = "+errorBody);
//
//                    ErrorResponse errorResponse = JsonParse.json2Object(errorBody,ErrorResponse.class);
//
//                    if(errorResponse != null){
//                        if(!TextUtils.isEmpty(errorResponse.getErrorDesc())){
//                            ex.message = errorResponse.getErrorDesc();
//                        }else if(!TextUtils.isEmpty(errorResponse.getErrorMsg())){
//                            ex.message = errorResponse.getErrorMsg();
//                        }else{
//                            ex.message = Strings.getInstance().getString(R.string.http_unknown_error);
//                        }
//                    }else{
//                        ex.message = Strings.getInstance().getString(R.string.http_unknown_error);
//                    }
//
//
//                    return ex;
//                }catch (Exception exception){
//                    SuperLog.error(TAG,"handleException() exception:"+exception.toString());
//                }
//            }

            switch(httpException.code()){
                case UNAUTHORIZED:{
                    ex = new ExceptionEngine(HEAD*1000 +UNAUTHORIZED);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_401);
                    break;
                }
                case FORBIDDEN:{
                    ex = new ExceptionEngine(HEAD*1000 +FORBIDDEN);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_403);
                    break;
                }
                case NOT_FOUND:{
                    ex = new ExceptionEngine(HEAD*1000 +NOT_FOUND);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_404);
                    break;
                }
                case REQUEST_TIMEOUT:{
                    ex = new ExceptionEngine(HEAD*1000 +REQUEST_TIMEOUT);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_408);
                    break;
                }
                case GATEWAY_TIMEOUT:{
                    ex = new ExceptionEngine(HEAD*1000 +GATEWAY_TIMEOUT);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_504);
                    break;
                }
                case INTERNAL_SERVER_ERROR:{
                    ex = new ExceptionEngine(HEAD*1000 +INTERNAL_SERVER_ERROR);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_500);
                    break;
                }
                case BAD_GATEWAY:{
                    ex = new ExceptionEngine(HEAD*1000 +BAD_GATEWAY);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_502);
                    break;
                }
                case SERVICE_UNAVAILABLE:{
                    ex = new ExceptionEngine(HEAD*1000 +SERVICE_UNAVAILABLE);
                    ex.message = Strings.getInstance().getString(R.string.http_errorcode_503);
                    break;
                }
                default:
                    ex = new ExceptionEngine(HEAD*1000 +UNKNOWN_HTTP);
                    ex.message = Strings.getInstance().getString(R.string.http_error);  //均视为网络错误
                    break;
            }

            return ex;
        } else if (e instanceof ServerException){    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ExceptionEngine(HEAD*1000 +SERVER_EXCEPTION);
            ex.message = Strings.getInstance().getString(R.string.socket_errorcode_700);

            SuperLog.error(TAG,"handleException() ServerException message = "+resultException.message);
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new ExceptionEngine(HEAD*1000 +JSON_PARES_EXCEPTION);
            ex.message = Strings.getInstance().getString(R.string.socket_errorcode_701); //均视为解析错误
            return ex;
        }else if(e instanceof ConnectException){
            ex = new ExceptionEngine(HEAD*1000 +CONNECT_EXCEPTION);
            ex.message = Strings.getInstance().getString(R.string.socket_errorcode_702);  //均视为网络错误
            return ex;
        }else if (e instanceof SocketTimeoutException){
            ex = new ExceptionEngine(HEAD*1000 +SOCKET_TIMEOUT_EXCEPTION);
            ex.message = Strings.getInstance().getString(R.string.socket_errorcode_703);
            return ex;
        }else {
            ex = new ExceptionEngine(HEAD*1000 +UNKNOWN_ERROR);
            ex.message = Strings.getInstance().getString(R.string.http_unknown_error);  //未知错误
            return ex;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 约定异常
     */

    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;
        /**
         * Socket超时
         */
        public static final int SOCKET_TIMEOUT_ERROR = 1004;
    }

    public class ServerException extends RuntimeException {
        private int    code;
        private String message;
    }
}