package com.pukka.ydepg.common.errorcode;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.uiutil.Strings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 返回码工具类
 * 根据返回码实现不同处理
 *
 * @author wangzhengrong 00255795
 */
public class ErrorCode
{
    private static final String TAG = ErrorCode.class.getSimpleName();

    /**
     * 错误码code不明确定义的默认错误码
     */
    private static final String DEFAULT = "10000";

    /**
     * error config file
     */
    private static final String ERROR_CFG_FILE = "error_code.json";

    private static ErrorCode gInstance = null;

    private static HashMap<String, HashMap<String, ErrorNode>> errorCodeMap = new HashMap<>();

    public static synchronized ErrorCode getInstance()
    {
        if (gInstance == null)
        {
            gInstance = new ErrorCode();
        }
        return gInstance;
    }

    private ErrorCode()
    {
        SuperLog.debug(TAG, "start to extract error code");

        String errorCodeString = getFileFromAssets(ERROR_CFG_FILE);
        if (errorCodeString == null)
        {
            return;
        }
        ErrorFile error = JsonParse.json2Object(errorCodeString,ErrorFile.class);
        if(null != error)
        {
            buildMap(error.getErrorNodeList());
        }
        SuperLog.debug(TAG, "finish to extract error code");
    }

    /**
     * 查询错误码提示语
     *
     * @param interfaceName 接口名称，区分大小写
     * @param code          平台返回错误码
     * @return ErrorMessage 错误消息，含错误码和提示语
     */
    public static synchronized ErrorMessage findError(String interfaceName, String code)
    {
        getInstance();
        ErrorMessage errMsg;
        HashMap<String, ErrorNode> interfaceMap = errorCodeMap.get(interfaceName);
        if (null == interfaceMap)//当前接口没有在《error_code.json》中定义
        {
            SuperLog.debug(TAG, "Interface[" + interfaceName + "] did not define in " + ERROR_CFG_FILE);
            errMsg = new ErrorMessage(code, Strings.getInstance().getString(R.string.error_code_nomal));
        } else {
            ErrorNode errorNode = interfaceMap.get(code);
            if (null == errorNode)
            {
                //接口的错误码没有在文档中定义,以默认错误信息展示
                SuperLog.debug(TAG, "Interface[" + interfaceName + "]'s " + code + " did not define in "+ERROR_CFG_FILE);
                errorNode = interfaceMap.get(DEFAULT);
            }

            if (null == errorNode){
                //接口的错误码没有在文档中定义,并不提供默认错误码,以约定默认错误信息展示
                SuperLog.debug(TAG, "Interface[" + interfaceName + "] does not have a default error info");
                errMsg = new ErrorMessage(code, Strings.getInstance().getString(R.string.error_code_nomal));
            } else {
                //接口的错误码有在文档中定义,使用预定义错误信息
                errMsg = new ErrorMessage(code, errorNode.getFirstLanguage());
            }
        }

        SuperLog.error(TAG, "Interface[" + interfaceName + "] errorCode=" + code + " errorMsg=" + errMsg.getMessage() );
        return errMsg;
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
        try
        {
            inputReader = new InputStreamReader(OTTApplication.getContext()
                    .getResources().getAssets().open(fileName));
            bufferReader = new BufferedReader(inputReader);
            String line = "";
            StringBuffer result = new StringBuffer();
            while ((line = bufferReader.readLine()) != null)
            {
                result.append(line);
            }
            return result.toString();
        }
        catch (Exception e)
        {
            SuperLog.warn(TAG, "read error_code file exception.");
            SuperLog.error(TAG,e);
        }
        finally
        {
            FileUtil.closeReader(inputReader);
            FileUtil.closeReader(bufferReader);
        }
        return null;
    }

    /**
     * 将错误码list构建成Map,便于数据查询
     *
     * @param errorNodeList 错误码列表
     */
    private void buildMap(ArrayList<ErrorNode> errorNodeList)
    {
        if (null == errorNodeList || errorNodeList.isEmpty())
        {
            SuperLog.warn(TAG, "errorNodeList is null or empty");
            return;
        }
        for (int i = 0; i < errorNodeList.size(); i++)
        {
            ErrorNode errorNode = errorNodeList.get(i);
            String interfaceName = errorNode.getInterfaceName();
            String code = errorNode.getCode();
            HashMap<String, ErrorNode> interfaceMap = new HashMap<String, ErrorNode>();
            if (errorCodeMap.containsKey(interfaceName))
            {
                interfaceMap = errorCodeMap.get(interfaceName);
            }
            interfaceMap.put(code, errorNode);
            errorCodeMap.put(interfaceName, interfaceMap);
        }
    }

    public interface MSA
    {
        String CLIENT_0 = "0";
        String CLIENT_16000 = "16000";
        String CLIENT_19000 = "19000";
        String CLIENT_23000 = "23000";
        String CLIENT_26751 = "26751";
        String CLIENT_26752 = "26752";
        String CLIENT_38000 = "38000";
        String CLIENT_38001 = "38001";
        String CLIENT_38002 = "38002";
        String CLIENT_38003 = "38003";
        String CLIENT_38004 = "38004";
        String CLIENT_38005 = "38005";
        String CLIENT_38006 = "38006";
        String CLIENT_38007 = "38007";
        String CLIENT_38008 = "38008";
        String CLIENT_38009 = "38009";
        String CLIENT_38010 = "38010";
        String CLIENT_38011 = "38011";
        String CLIENT_38012 = "38012";
        String CLIENT_38013 = "38013";
        String CLIENT_38014 = "38014";
        String CLIENT_38015 = "38015";
        String CLIENT_38016 = "38016";
        String CLIENT_38017 = "38017";
        String CLIENT_38018 = "38018";
        String CLIENT_38019 = "38019";
        String CLIENT_38020 = "38020";
        String CLIENT_38021 = "38021";
        String CLIENT_38022 = "38022";
        String CLIENT_38023 = "38023";
        String CLIENT_38024 = "38024";
        String CLIENT_38025 = "38025";
        String CLIENT_38026 = "38026";
        String CLIENT_38027 = "38027";
        String CLIENT_38028 = "38028";
        String CLIENT_38029 = "38029";
        String CLIENT_38030 = "38030";
        String CLIENT_38031 = "38031";
        String CLIENT_38032 = "38032";
        String CLIENT_38033 = "38033";
        String CLIENT_38034 = "38034";
        String CLIENT_38035 = "38035";
        String CLIENT_38036 = "38036";
        String CLIENT_38037 = "38037";
        String CLIENT_38038 = "38038";
        String CLIENT_38039 = "38039";
        String CLIENT_38040 = "38040";
        String CLIENT_38041 = "38041";
        String CLIENT_38042 = "38042";
        String CLIENT_38043 = "38043";
        String CLIENT_38044 = "38044";
        String CLIENT_38045 = "38045";
        String CLIENT_38046 = "38046";
        String CLIENT_38047 = "38047";
        String CLIENT_38048 = "38048";
        String CLIENT_38049 = "38049";
        String CLIENT_38050 = "38050";
        String CLIENT_38051 = "38051";
        String CLIENT_38052 = "38052";
        String CLIENT_38053 = "38053";
        String CLIENT_38054 = "38054";
        String CLIENT_38055 = "38055";
        String CLIENT_38056 = "38056";
        String CLIENT_38057 = "38057";
        String CLIENT_38058 = "38058";
        String CLIENT_38059 = "38059";
        String CLIENT_38060 = "38060";
        String CLIENT_38061 = "38061";
        String CLIENT_38062 = "38062";
        String CLIENT_38063 = "38063";
        String CLIENT_38064 = "38064";
        String CLIENT_38065 = "38065";
        String CLIENT_38066 = "38066";
        String CLIENT_38067 = "38067";
        String CLIENT_38068 = "38068";
        String CLIENT_38069 = "38069";
        String CLIENT_38070 = "38070";
        String CLIENT_38071 = "38071";
        String CLIENT_38072 = "38072";
        String CLIENT_38073 = "38073";
        String CLIENT_38074 = "38074";
        String CLIENT_38075 = "38075";
        String CLIENT_38076 = "38076";
        String CLIENT_38077 = "38077";
        String CLIENT_38078 = "38078";
        String CLIENT_38079 = "38079";

        String DEFAULT = "10099";
    }
}