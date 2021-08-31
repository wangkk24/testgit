package com.pukka.ydepg.common.utils.errorWindowUtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.util.Hashtable;

public class ZXingUtils {
    /**
     * 生成二维码 要转换的地址或字符串,可以是中文
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
//    public static Bitmap createQRImage(String url, final int width, final int height) {
//        try {
//            // 判断URL合法性
//            if (url == null || "".equals(url) || url.length() < 1) {
//                return null;
//            }
//            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.MARGIN, 1);
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
//                    BarcodeFormat.QR_CODE, width, height, hints);
//            bitMatrix = deleteWhite(bitMatrix);
//            int[] pixels = new int[width * height];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * width + x] = 0xff000000;
//                    } else {
//                        pixels[y * width + x] = 0xffffffff;
//                    }
//                }
//            }
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(width, height,
//                    Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            return bitmap;
//        } catch (WriterException e) {
//            SuperLog.error("ZXingUtils", e);
//        }
//        return null;
//    }
    public static Bitmap createQRImage(String url, int width, int height) {
        try {
            //判断URL合法性
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix matrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
            matrix = deleteWhite(matrix);//删除白边
            width = matrix.getWidth();
            height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
//去除二维码的白边

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}
