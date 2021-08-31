package com.pukka.ydepg.common.utils.base64Utils;

import com.pukka.ydepg.service.NtpTimeService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class AepAuthUtil
{  
    //AEP获取密钥，Sha256Base64加密方式
    public static String getWsse(String userName, String appsecret)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString=sdf.format(new Date(NtpTimeService.queryNtpTime()));
        String nonce = getNonce();
        String passwordDigest =
            "UsernameToken Username=\"" + userName + "\",PasswordDigest=\""
                + encryptSha256Base64(nonce + dateString + appsecret) + "\", Nonce=\"" + nonce + "\", Created=\""
                + dateString + "\"";
        return passwordDigest;
    }
    
    public static String encryptSha256Base64(String passwd)
    {
        
        byte[] tmpEncrypted;
        byte[] encrypted;
        try {
            MessageDigest sha=MessageDigest.getInstance("SHA-256");
            tmpEncrypted = sha.digest(passwd.getBytes("UTF-8"));
            encrypted = Base64.encodeBase64(tmpEncrypted);
        } catch (NoSuchAlgorithmException e1) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }

        try {
            return null==encrypted?null:new String(encrypted, "8859_1");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        
    }
    
    public static String getNonce()
    {
        char[] charArray =
            {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuffer nonce = new StringBuffer();
        for (int i = 0; i < 36; i++) {
            nonce.append(charArray[(int)(Math.random() * charArray.length)]);
        }
        return nonce.toString();
    }
}