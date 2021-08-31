package com.pukka.ydepg.common.utils.fileutil;

import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;

import java.util.Arrays;
import java.util.Map;

public final class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();

    private StringUtils() { }

    /**
     * 特殊字符列表
     */
    private static String[] symbolArrays = new String[]{"@", "#", "$", "%", "&", "*", "/", "^", "|", "~", ",", ".", ";", ":", "!", "?", "'", "\"", "<", ">", "(", ")",
            "[", "]", "{", "}", "+", "-", "_", "=", "\\", "‘", "“"};

    public static boolean containSymbol(String key) {
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Arrays.asList(symbolArrays).contains(c + "")) {
                return true;
            }
        }
        return false;
    }

    public static String getEncryptionNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            //return number.substring(0,3) + "****"+ number.substring(number.length()-4,number.length());
            return number;
        }
        return "";
    }

    public static String getValidTime(String chargeMode) {
        // 包周期产品的计量方式，取值包括：
        String period = " "; //注意这里是一个空格 不是空
        if(!TextUtils.isEmpty(chargeMode)){
            switch (chargeMode) {
                case "0":
                    period = "月";
                    break;
                case "10":
                    period = "多天";
                    break;
                case "13":
                    period = "多月";
                    break;
                case "18":
                    period = "天";
                    break;
                case "19":
                    period = "周";
                    break;
                case "20":
                    period = "多周";
                    break;
                case "21":
                    period = "年";
                    break;
                default:
                    period = "";
                    break;

            }
        }
        return period;
    }

    private static String getPriceUnitFromVss(Product product) {
        if(null == product){
            return "";
        }
        Resources resources= OTTApplication.getContext().getResources();
        String productType = product.getProductType();
        //产品类型: 0>包周期;1>按次
        String indateValue="";
        switch (productType){
            case "1":
                //周期产品
                String display_day = SessionService.getInstance().getSession().getTerminalConfigurationValue(com.pukka.ydepg.launcher.Constant.PPVORDER_SHOW_TIME);
                if(TextUtils.isEmpty(display_day)){
                    display_day="72";
                }
                String rentPeriod = product.getRentPeriod();//获取有效期
                if(!TextUtils.isEmpty(rentPeriod)){
                    int disp=  Integer.parseInt(display_day);
                    int rentp=Integer.parseInt(rentPeriod);
                    if(rentp>=disp){
                        int retain=rentp%24;
                        int rate=rentp/24;
                        indateValue= String.format(resources.getString(R.string.order_list_nday),String.valueOf(rate))+(retain==0?"":String.format(resources.getString(R.string.order_list_nhour),String.valueOf(retain)));
                    }else{
                        //xx小时
                        indateValue = String.format(resources.getString(R.string.order_list_nhour),rentPeriod);
                    }
                }

                break;
            case "0":
                String chargeMode = product.getChargeMode();//包周期类型
                if(!TextUtils.isEmpty(chargeMode)){
                    switch (chargeMode){
                        case "0"://包月
                            //一个月
                            indateValue = "月";
                            break;
                        case "10"://包多天
                            //xxx天
                            indateValue = String.format(resources.getString(R.string.order_list_nday),
                                    product.getPeriodLength());
                            break;
                        case "13"://包多月
                            //xxx月
                            indateValue = String.format(resources.getString(R.string.order_list_nmonth),
                                    product.getPeriodLength());
                            break;
                        case "18"://包天
                            //一天
                            indateValue = "天";
                            break;
                        case "19"://包周
                            //一周
                            indateValue = "周";
                            break;
                        case "20"://包多周
                            //xxx周
                            indateValue = String.format(resources.getString(R.string.order_list_nweek),
                                    product.getPeriodLength());
                            break;
                        case "21"://包年
                            indateValue = "";
                            break;
                        default://在产品类型下未获取到周期产品计量方式
                            //一个月
                            indateValue = "月";
                            break;
                    }
                }else{
                    //一个月
                    indateValue ="月";
                    //chargeMode不为空
                }
                break;
            default://未匹配到相应的产品类型
                indateValue = "";
                break;
        }
        return indateValue;
    }

    private static Map<String,String> mapProduct2PriceUnit = CommonUtil.getMapConfigValue("PRODUCT_PRICE_UNIT");;

    public static String getPriceUnitFromTerminal(String productId){
        if(null != mapProduct2PriceUnit){
            return mapProduct2PriceUnit.get(productId);
        } else {
            return null;
        }
    }

    public static String analyticValidity(Product product) {
        String configuredUnit = getPriceUnitFromTerminal(product.getID());
        if( !TextUtils.isEmpty(configuredUnit)){
            return configuredUnit;
        } else {
            return getPriceUnitFromVss(product);
        }
    }
    public static String analyticValiditySubscription(Subscription product) {
        if(null == product){
            return "";
        }
        Resources resources= OTTApplication.getContext().getResources();
        String productType = product.getProductType();
        //产品类型: 0>包周期;1>按次
        String indateValue="";
        String chargeMode=product.getChargeMode();
        if(!TextUtils.isEmpty(chargeMode)){
            switch (chargeMode){
                case "0"://包月
                    //一个月
                    indateValue = "月";
                    break;
                case "10"://包多天
                    //xxx天
                    indateValue = String.format(resources.getString(R.string.order_list_nday),
                            product.getPeriodLength());
                    break;
                case "13"://包多月
                    //xxx月
                    indateValue = String.format(resources.getString(R.string.order_list_nmonth),
                            product.getPeriodLength());
                    break;
                case "18"://包天
                    //一天
                    indateValue = "天";
                    break;
                case "19"://包周
                    //一周
                    indateValue = "周";
                    break;
                case "20"://包多周
                    //xxx周
                    indateValue = String.format(resources.getString(R.string.order_list_nweek),
                            product.getPeriodLength());
                    break;
                case "21"://包年
                    indateValue = "年";
                    break;
                default://在产品类型下未获取到周期产品计量方式
                    //一个月
                    indateValue = "月";
                    break;
            }
        }else{
            //一个月
            indateValue ="月";
            //chargeMode不为空
        }
        return indateValue;
    }

    public static boolean isSubscribeByDay(Product product){
        return null != product &&("10".equals(product.getChargeMode())||"18".equals(product.getChargeMode())||"1".equals(product.getProductType()));
    }


    public static String splicingPlayUrl(String playurl){
        if(!TextUtils.isEmpty(playurl)) {
            StringBuffer buffer = new StringBuffer(playurl);
            UserInfo mUserInfo= AuthenticateManager.getInstance().getUserInfo();
            if(null!=mUserInfo){
                if(playurl.contains("?")) {
                    buffer.append("&USERID=");
                } else{
                    buffer.append("?USERID=");
                }
                buffer.append(mUserInfo.getUserId());
            }
            String stbId = CommonUtil.getSTBID();
            if(!TextUtils.isEmpty(stbId)){
                buffer.append("&STBID=");
                buffer.append(stbId);
            }
            playurl=buffer.toString();
        }
        return playurl;
    }
}
