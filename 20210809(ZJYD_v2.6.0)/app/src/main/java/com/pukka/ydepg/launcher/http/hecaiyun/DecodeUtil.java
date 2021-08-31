package com.pukka.ydepg.launcher.http.hecaiyun;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//此代码由和彩云程文强提供
public class DecodeUtil {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private static final String secret = "zjtv#zITV#;!ty:Sv;wQKmir:.j?WGc`xRn";

    /**
     * 保存登录接口返回数据，解密使用的key
     *
     * @param userPass
     */
    public static String getLoginClientKet(String userPass) {
        String loginKey = secret;
        try {
            String priKey = DecodeUtil.toMD5(DecodeUtil.toMD5(userPass) + loginKey).substring(0, 16).toUpperCase();
            return priKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String toMD5(String strSrc) throws NoSuchAlgorithmException {
        if (strSrc == null) {
            return null;
        }

        byte[] bt = strSrc.getBytes();

        // findbug md5实例不会为空
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(bt);

        return bytes2Hex(md5.digest());
    }

    /**
     * [将byte数组转换为16进制字符串]
     *
     * @param bts byte数组，即待转换的数组
     * @return 转换后的16进制字符串
     */
    private static String bytes2Hex(byte[] bts) {
        StringBuilder sb = new StringBuilder(bts.length * 2);
        for (int i = 0; i < bts.length; i++) {
            sb.append(HEX[bts[i] >> 4 & 0xf]);
            sb.append(HEX[bts[i] & 0xf]);
        }
        return sb.toString();
    }

    public static String decryptAES(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = DecodeUtil.hex2byte(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    /**
     * [将16进制字符串转换为byte数组]
     *
     * @param strhex 16进制字符串
     * @return 转换后的字节数组
     */
    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 != 0) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }
}