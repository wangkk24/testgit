package com.pukka.ydepg.common.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.listener.HttpHeaders;
import com.pukka.ydepg.common.upgrade.UpgradeManager;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.LogSecurityUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.base64Utils.AepAuthUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.http.hecaiyun.HecaiyunServiceImpl;
import com.pukka.ydepg.launcher.session.SessionService;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.pukka.ydepg.launcher.http.hecaiyun.HecaiyunHttpService.HCY_QUERY_API;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: LogInterceptor.java
 * @author: yh
 * @date: 2016-11-04 15:14
 */
public class LogInterceptor implements Interceptor {

    private static final String TAG = LogInterceptor.class.getSimpleName();

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static final int ERROR_201 = 0x00C9;

    private static final int ERROR_301 = 0x012D;     //重定向

    private static final int ERROR_302 = 0x012E;

    private static boolean isPrintUserInfo = true;

    private static final String HTTP_INFO  = "[HttpInfo]";

    private static final String EPG_APK_USER_AGENT = "Huawei-EPG-APK";

    public enum Level {
        /**
         * No logs.
         */
        NONE,


        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,



        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,



        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);

        void xmlLog(String message);

        /**
         * A {@link LogInterceptor.Logger} defaults output appropriate for the current platform.
         */
        LogInterceptor.Logger DEFAULT = new LogInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (!isPrintUserInfo) return;
                SuperLog.debug(TAG, message);
            }

            @Override
            public void xmlLog(String message) {
                if (!isPrintUserInfo) return;
                SuperLog.xmlPrint(TAG, message);
            }
        };
    }

    public LogInterceptor() {
        this(LogInterceptor.Logger.DEFAULT);
        if(BuildConfig.DEBUG_LOG){
            level=Level.BODY;
            isPrintUserInfo=true;
        }else{
            level=Level.NONE;
            isPrintUserInfo=false;
        }
    }

    private LogInterceptor(LogInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final LogInterceptor.Logger logger;

    private LogInterceptor.Level level=Level.BODY;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String requestUrl = "";

        HttpUrl url = request.url();


        if (url != null) {
            requestUrl = url.toString();
        }

        //获取接口名称
        String interfaceName = requestUrl;
        int index = requestUrl.indexOf("V3");
        if (index > 0 && index < (requestUrl.length() - 3)) {
            interfaceName = requestUrl.substring(index + 3);
        }

        /**
         * 设置 Cookie
         */
        if (!TextUtils.isEmpty(requestUrl) &&!requestUrl.contains("/VSP/V3/ZJLogin")) {
            if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getSeesionId())) {
                String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();

                if (!jsessionid.contains("JSESSIONID=")) {
                    jsessionid = "JSESSIONID=" + jsessionid;
                }

                String csrfsession = SessionService.getInstance().getSession().getCookie();
                if (!TextUtils.isEmpty(csrfsession)) {
                    jsessionid = jsessionid + "; " + csrfsession;
                }

                String xCSRFToken = SessionService.getInstance().getSession().getCSRFToken();
                if (!TextUtils.isEmpty(xCSRFToken)) {
                    jsessionid = jsessionid + "; " + xCSRFToken;
                }
                String userToken= SessionService.getInstance().getSession().getUserToken();
                //Modified by liuxia at 20200731 关闭session打印 无用日志且有安全问题
                //logger.log("[SetCookie] Jsessionid|" + jsessionid);

                Request.Builder newBuilder = request.newBuilder();
                newBuilder = newBuilder.header("User-Agent", EPG_APK_USER_AGENT)
                    .header("Cookie",jsessionid)//Cookie:JSESSIONID=01OG9KT64YZ6KW4Z3VQ7L27Z5ZJ7DO0R
                    .header("Set-Cookie",jsessionid + "; Path=/VSP/");
                if(requestUrl.contains("/VSP/VSS")){
                    request = newBuilder
                        .header("authorization",userToken)
                        .header("Authorization","WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"")
                        .header("X-WSSE",AepAuthUtil.getWsse(HttpConstant.AppKey,HttpConstant.AppSecret))
                        .build();
                } else if(requestUrl.contains("/adex/v1.0/")) {
                    request = newBuilder
                        .header("Accept-Language","no-cache")
                        .header("Postman-Token",SharedPreferenceUtil.getInstance().getSeesionId())
                        .header("User-Agent", HttpUtil.getSspUserAgent())//按照要求规范广告类请求的User-Agent
                        .build();
                } else if(UpgradeManager.isUpgradeConfigUrl(requestUrl)){
                    request = newBuilder
                            .removeHeader("User-Agent")      //自定义日志拦截器添加
                            .removeHeader("Cookie")          //自定义日志拦截器添加
                            .removeHeader("Set-Cookie")      //自定义日志拦截器添加
                            .removeHeader("Accept-Encoding") //系统默认BridgeInterceptor添加
                            .removeHeader("Connection")      //系统默认BridgeInterceptor添加
                            .header("Content-Type", UpgradeManager.CONTENT_TYPE)  //华为升级服务器要求必传字段
                            .build();
                } else if(requestUrl.contains("aikanvod.miguvideo.com")) {
                    request = newBuilder
                            .header("authorization", userToken)
                            .header("EpgSession", jsessionid) //EpgSession:JSESSIONID=01OG9KT64YZ6KW4Z3VQ7L27Z5ZJ7DO0R
                            .header("Location", ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL())//Location:http://117.148.130.113:7047
                            .build();
                }
                else if(requestUrl.contains(HCY_QUERY_API)){
                    request = newBuilder
                            .removeHeader("User-Agent")      //自定义日志拦截器添加
                            .removeHeader("Cookie")          //自定义日志拦截器添加
                            .removeHeader("Set-Cookie")      //自定义日志拦截器添加
                            .header("Authorization", HecaiyunServiceImpl.getRequestHeaderAuthorization())
                            .build();
                }
                else {
                    request = newBuilder
                        .header("authorization",userToken)
                        .build();
                }
            }
        }

        //Release版本走此段逻辑
        if (level == LogInterceptor.Level.NONE) {
            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            int httpCode=0;
            if(null!=response){
                httpCode = response.code();
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            StringBuffer sb = new StringBuffer(LogInterceptor.HTTP_INFO)
                    .append("<")
                    .append(interfaceName)
                    .append(">[")
                    .append(tookMs)
                    .append("ms][")
                    .append(httpCode)
                    .append("]");
            SuperLog.info2SD(TAG, LogSecurityUtil.getSecurityLog(sb.toString(),LogSecurityUtil.REGEX_HTTPURL,LogSecurityUtil.REPLACEMENT_HTTPURL));

            //如果HTTP响应为200但平台返回接口调用错误，打印具体错误信息
            if (null != response){
                logResponseErrorInfo(response,interfaceName);
            }
            return response;
        }


        //========================================================================================//


        // Debug版本走以下逻辑(Release版本不走)
        // ************ START [Debug版本]打印 request header ************
        boolean logBody = level == LogInterceptor.Level.BODY;                  //true
        boolean logHeaders = logBody || level == LogInterceptor.Level.HEADERS; //true

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "Request Body:--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        SuperLog.info2SD(TAG,requestStartMessage);

        if (/*logHeaders*/false) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.log("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (isPlaintext(buffer)) {
                    logger.log(buffer.readString(charset));
                    logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logger.log("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);

        if(null==response){
            SuperLog.error(TAG, interfaceName + " response is null.");
            return null;
        }

        // ************ END [Debug版本]打印 request header ************


        //Comment by liuxia at 20200731
//        // ************ START [Debug版本]打印 response header ************
//        Headers headers = response.headers();
//        if (headers != null) {
//            for (int i = 0, count = headers.size(); i < count; i++) {
//                String name = headers.name(i);
//                // Skip headers from the request body as they are explicitly logged above.
//                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
//                    logger.log("Response hearders --> " + name + ": " + headers.value(i));
//                }
//            }
//        }
//        // ************ END [Debug版本]打印 response header ************

        // 打印接口调用信息(接口名/接口调用时间/http响应码)
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
//        StringBuffer sb = new StringBuffer(LogInterceptor.HTTP_INFO)
//                .append("<")
//                .append(interfaceName)
//                .append(">[")
//                .append(tookMs)
//                .append("ms][")
//                .append(response.code())
//                .append("]");
//        SuperLog.info2SD(TAG, sb.toString());
        // 接口调用失败写文件
        logResponseErrorInfo(response,interfaceName);



        // ************ START [Debug版本]打印 response body ************
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        SuperLog.info2SD(TAG,"Response Body:<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (cost " + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP (No response body printed)");
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)(No response body printed)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                //防止没有数据时，json解析失败，抛出异常java.io.EOFException
                if (response.code() == ERROR_201 && buffer.size() == 0) {
                    buffer.writeUtf8("{ \"message\":\"code = 201\"}");
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logger.log("Couldn't decode the response body; charset is likely malformed.");
                        logger.log("<-- END HTTP (No response body printed)");

                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)(No response body printed)");
                    return response;
                }

                if (contentLength != 0) {
                    String body = buffer.clone().readString(charset);

                    //logger.log("Before Format:" + body);

                    if (body.contains("<?xml") || body.contains("<html>") && body.length() < 4000) {
                        logger.log("<---------------------------------- XML Format ------------------------------>");
                        logger.xmlLog(body);
                    } else {
                        logger.log("<--------------------------------- JSON Format ------------------------------>");
                        String sUrl = response.request().url().toString();
                        //index@代表桌面json文件 例如 http://117.148.130.129:7350/PHS/desktop/11292/index@1599528566860.json
                        if(sUrl.contains("QueryCustomizeConfig") || sUrl.contains("index@") || sUrl.contains("http://aikanvod.miguvideo.com/video/p/useraction.jsp") ){
                            logger.log(sUrl + " does not need to log response body");
                        } else {
                            logger.log(sUrl + " response body is as followed:>>>\n"+toPrettyFormat(body));
                        }
                    }
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }
        // ************ END [Debug版本]打印 response body ************

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private void logResponseErrorInfo(Response response,String interfaceName){
        try{
            //部分接口不打印日志(因为其返回结果无成功码,无法判断是否为失败)
            if(isCloseInterfaceLog(interfaceName)){
                SuperLog.info2SD(TAG,interfaceName + "'s error log has been disabled.");
                return;
            }

            BufferedSource source = response.body().source();
            source.request(500L); // Buffer the entire body.
            //source.request(Long.MAX_VALUE);//临时使用
            String responseInfo = source.buffer().clone().readString(UTF8);

            //有返回结果码的接口返回为失败时打印返回具体内容
            if(!respondSuccess(responseInfo)){
                StringBuffer sb = new StringBuffer(LogInterceptor.HTTP_INFO)
                        .append("<")
                        .append(interfaceName)
                        .append("> response detail : \n\t")
                        .append("======================================================\n\t")
                        .append(responseInfo)
                        .append("\n\t======================================================");
                SuperLog.info2SD(TAG, LogSecurityUtil.getSecurityLog(sb.toString(),LogSecurityUtil.REGEX_HTTPURL,LogSecurityUtil.REPLACEMENT_HTTPURL));
            }
        }catch(Exception e){
            SuperLog.error(TAG,e);
        }
    }

    private final String  regexInterface   = "index\\.json|PHS/desktop|upgrade\\.jsp|\\.apk|\\.jpg|stb_getErrorInfoByCode\\.jsp|useraction\\.jsp";
    private final Pattern patternInterface = Pattern.compile(regexInterface);
    //部分接口不打印日志
    private boolean isCloseInterfaceLog(String interfaceName){
        return patternInterface.matcher(interfaceName).find();
    }

    //匹配接口返回成功正则规则： 匹配[code":"0"]或者[00000000]
    private final String  regexSuccess     = "code\":\"0\"|0{8}|resultCode\":\"0\"";
    private final Pattern patternSuccess   = Pattern.compile(regexSuccess);
    //接口返回结果是否为成功
    private boolean respondSuccess(String response){
        return patternSuccess.matcher(response).find();
    }

    //格式化Json字符串
    private String toPrettyFormat(String json) {
        try{
            if(json == null || json.isEmpty()){
                return null;
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (Exception e){
            Log.e(TAG,"print json log error",e);
            Log.e(TAG,"response string is : " + json);
            return "Json data format failed,origin string is as followed:>>>\n" + json;
        }
    }
}