package com.pukka.ydepg.xmpp.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class Base64
{
    /**
     * the range of ASSIC in BASE64 code
     */
    private static final char[] S_BASE64CHAR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '+', '/'};

    private static final char S_BASE64PAD = '=';

    /**
     *
     */
    private static final byte[] S_DECODETABLE = new byte[128];

    static
    {
        for (int i = 0; i < S_DECODETABLE.length; i++)
        {
            S_DECODETABLE[i] = Byte.MAX_VALUE; // 127 0x7F
        }

        for (int i = 0; i < S_BASE64CHAR.length; i++)
        {
            // 0 to 63
            S_DECODETABLE[S_BASE64CHAR[i]] = (byte) i;
        }

    }

    /**
     * base64 encode
     *
     * @param bytes byte[]
     * @return encode result
     */
    public static String encode(byte bytes[])
    {
        int code = 0;
        StringBuffer sb = new StringBuffer((bytes.length - 1) / 3 << 6);
        for (int i = 0; i < bytes.length; i++)
        {
            code |= bytes[i] << 16 - (i % 3) * 8 & 255 << 16 - (i % 3) * 8;
            if (i % 3 == 2 || i == bytes.length - 1)
            {
                sb.append(S_BASE64CHAR[(code & 16515072) >>> 18]);
                sb.append(S_BASE64CHAR[(code & 258048) >>> 12]);
                sb.append(S_BASE64CHAR[(code & 4032) >>> 6]);
                sb.append(S_BASE64CHAR[code & 63]);
                code = 0;
            }
        }

        if (bytes.length % 3 > 0)
        {
            sb.setCharAt(sb.length() - 1, '=');
        }
        if (bytes.length % 3 == 1)
        {
            sb.setCharAt(sb.length() - 2, '=');
        }

        return sb.toString();
    }

    /**
     * base64 encode
     *
     * @param data plain text
     * @return encode result
     */
    public static String encode(String data)
    {
        return encode(decodeddd(data));
    }

    /**
     * decode
     *
     * @param string
     * @return
     */
    public static byte[] decodeddd(String string)
    {
        try
        {
            return string.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return string.getBytes(Charset.forName("UTF-8"));
        }
    }

    /**
     * base64 decode
     *
     * @param data base64 encoded result
     * @return decode
     */
    public static byte[] decode(String data)
    {
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[data.length() / 4 * 3 + 3];
        int obufcount = 0;
        for (int i = 0; i < data.length(); i++)
        {
            char ch = data.charAt(i);
            if (ch == S_BASE64PAD || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte
                    .MAX_VALUE)
            {
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length)
                {
                    ibufcount = 0;
                    obufcount += decode(ibuf, obuf, obufcount);
                }
            }
        }

        if (obufcount == obuf.length)
        {
            return obuf;
        }

        byte[] ret = new byte[obufcount];
        System.arraycopy(obuf, 0, ret, 0, obufcount);
        return ret;
    }

    /**
     * @param ibuf
     * @param obuf
     * @param wp
     * @return
     */
    private static int decode(char[] ibuf, byte[] obuf, int wp)
    {
        int outlen = 3;
        if (ibuf[3] == S_BASE64PAD)
        {
            outlen = 2;
        }

        if (ibuf[2] == S_BASE64PAD)
        {
            outlen = 1;
        }

        int b0 = S_DECODETABLE[ibuf[0]];
        int b1 = S_DECODETABLE[ibuf[1]];
        int b2 = S_DECODETABLE[ibuf[2]];
        int b3 = S_DECODETABLE[ibuf[3]];
        if (wp >= 0 && wp < obuf.length)
        {
            switch (outlen)
            {

                case 1:
                    obuf[wp] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
                    return 1;
                case 2:
                    obuf[wp++] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
                    obuf[wp] = (byte) (b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
                    return 2;
                case 3:
                    obuf[wp++] = (byte) (b0 << 2 & 0xfc | b1 >> 4 & 0x3);
                    obuf[wp++] = (byte) (b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
                    obuf[wp] = (byte) (b2 << 6 & 0xc0 | b3 & 0x3f);
                    return 3;
                default:
                    throw new RuntimeException("base64 Couldn't decode.");
            }
        }
        return outlen;
    }
}
