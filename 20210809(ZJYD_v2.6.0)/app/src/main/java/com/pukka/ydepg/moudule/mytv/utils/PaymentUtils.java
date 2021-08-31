package com.pukka.ydepg.moudule.mytv.utils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.launcher.session.SessionService;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PaymentUtils {

    /**
     * 产品不支持自动续订的标识
     */
    private static String PRODUCT_IS_NOT_AUTO_EXTEND = "0";

    /**
     * 判断产品是否支持第三方支付，规则如下：
     * 当产品不支持自动续订的时候支持第三方支付，
     * 当产品支持自动续订的时候不支持第三方支付
     *
     * @param product 产品信息
     * @return boolean
     */
    public static boolean isProductSupportThirdPartPayment(Product product) {
        if(product == null) {
            return false;
        }
        return PRODUCT_IS_NOT_AUTO_EXTEND.equals(product.getIsAutoExtend()) || "1".equals(product.getProductType());
    }

    public static boolean isUserSupportThirdPartPayment(String userState, String realNameFlag) {
        return "1".equals(userState) && "11".equals(realNameFlag);
    }

    /**
     * 生成messageID
     *  appKey(6位)+deviceID(8位) + YYMMDDHHMMSS + 4位序列号
     *  本地时间.
     *  appKey: 获取对应的后6位值.
     *  deviceID: 预留给Partner标识服务器节点或者
     *  终端编号, 建议数字和字母组合使用.
     *  YYMMDDHHMMSS: 本地时间戳
     *  4位序列号: 0000 – 9999
     * @return messageID
     */
    public static String generateMessageID() {
        String appKey = "00000000000000000001000000000011";
        String deviceID = SessionService.getInstance().getSession().getDeviceId();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String serialNumber = getRandomNumberString(4);
        StringBuffer messageID = new StringBuffer();
        if(appKey.length() < 8) {
            while (true) {
                StringBuffer appKeyStringBuffer = new StringBuffer(appKey);
                if(appKey.length() <8) {
                    appKeyStringBuffer.append("0");
                } else {
                    break;
                }
            }
            messageID.append(appKey);
        } else {
            messageID.append(appKey.substring(appKey.length() - 8));
        }

        if(deviceID.length() < 6) {
            while (true) {
                StringBuffer deviceIDStringBuffer = new StringBuffer(appKey);
                if(appKey.length() < 6) {
                    deviceIDStringBuffer.append("0");
                } else {
                    break;
                }
            }
        } else {
            messageID.append(deviceID.substring(0, 6));
        }

        messageID.append(timestamp).append(serialNumber);

        return messageID + "";
    }

    private static Random random = new SecureRandom();

    public static String getRandomNumberString(int length) {
        StringBuffer randomNumberString = new StringBuffer();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                randomNumberString.append(random.nextInt(10));
            }
        }
        return randomNumberString.toString();
    }

    //4K产品包会在product中的CustomField字段中携带key=4k value=1(4k产品包)/0(非4k产品包)
    private static String PRODUCT_RESOURCE_4 = "1";
    public static List<Product> getAvailableProduct(List<Product> listProduct){
        Iterator<Product> it = listProduct.iterator();
        while(it.hasNext()){
            List<NamedParameter> listNp = it.next().getCustomFields();
            List<String> listIs4kProduct = CommonUtil.getCustomNamedParameterByKey(listNp,"4k");
            if(listIs4kProduct !=null){
                //产品包是4k产品包 且 机顶盒不支持4k 则 过滤此产品包
                if(PRODUCT_RESOURCE_4.equals(listIs4kProduct.get(0))&& !VodUtil.isDeviceSupport4K()){
                    it.remove();
                }
            }
        }
        return listProduct;
    }
}