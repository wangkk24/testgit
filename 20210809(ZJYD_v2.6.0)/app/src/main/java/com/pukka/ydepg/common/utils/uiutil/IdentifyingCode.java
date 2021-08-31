package com.pukka.ydepg.common.utils.uiutil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.common.http.bean.node.ColorBean;
import com.pukka.ydepg.common.utils.JsonParse;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class IdentifyingCode
{
    //随机数数组，验证码上的数字和字母
    private static final char[] CHARS={
        '0','1','2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static String colorStr=
        "[{\"red\":0, \"green\":135, \"blue\":255}, {\"red\":51, \"green\":153,\"blue\": 51}, {\"red\":255, \"green\": 102, \"blue\":102}, {\"red\":255, \"green\":153, \"blue\":0}, {\"red\":153,  \"green\":102, \"blue\":0}, {\"red\":153,  \"green\":102, \"blue\":153}, {\"red\":51,  \"green\":153, \"blue\":153}, {\"red\":102,  \"green\":102,\"blue\": 255}, {\"red\":0, \"green\":102, \"blue\":204}, {\"red\":204, \"green\":51,  \"blue\":51}, {\"red\":0,  \"green\":153, \"blue\": 204}, {\"red\":0, \"green\":51, \"blue\":102}]";

    private static List<ColorBean> colorBeans;

    static {
        colorBeans = JsonParse.json2Object(colorStr, new TypeToken<List<ColorBean>>() {}.getType());
    }

    //这是一个单例模式
    private static IdentifyingCode IdentifyingCode;

    public static IdentifyingCode getInstance(){
        if(IdentifyingCode==null){
            IdentifyingCode=new IdentifyingCode();
        }
        return IdentifyingCode;
    }
    //验证码个数
    private static final int CODE_LENGTH = 3;
    //字体大小
    private static final int FONT_SIZE = 50;
    //线条数
    private static final int LINE_NUMBER = 10;
    //padding，其中base的意思是初始值，而range是变化范围。数值根据自己想要的大小来设置
    private static final int BASE_PADDING_LEFT=30,BASE_PADDING_TOP=40;

    //画布的长宽
    private int width;

    //验证码默认宽高
    private int height;

    //字体的随机位置
    private int base_padding_left=BASE_PADDING_LEFT, base_padding_top=BASE_PADDING_TOP;
    //验证码个数，线条数，字体大小
    private int codeLength=CODE_LENGTH,line_number=LINE_NUMBER,font_size=FONT_SIZE;

    private String code;
    private int padding_left,padding_top;
    private Random random=new SecureRandom();

    //验证码图片(生成一个用位图)
    public Bitmap createBitmap(int width,int height){
        padding_left=5;
        padding_top=base_padding_top;
        this.width=width;
        this.height=height;
        //创建指定格式，大小的位图//Config.ARGB_8888是一种色彩的存储方法
        Bitmap bp=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(bp);

        code =createCode();
        //将画布填充为白色
        c.drawColor(Color.WHITE);
        //新建一个画笔
        Paint paint =new Paint();
        //设置画笔抗锯齿
        paint.setAntiAlias(true);
        paint.setTextSize(font_size);
        //在画布上画上验证码
        //        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        for(int i=0;i<code.length();i++){
            randomTextStyle(paint);
            //这里的padding_left,padding_top是文字的基线
            c.drawText(code.charAt(i)+"",padding_left,padding_top,paint);
            randomPadding();
        }
        //画干扰线
        for(int i = 0;i<line_number;i++){
            drawLine(c,paint);
        }
        //保存一下画布
        c.save();
        c.restore();
        return bp;
    }

    //生成验证码
    private String createCode(){
        StringBuilder sb=new StringBuilder();
        //利用random生成随机下标
        for(int i=0;i<codeLength;i++){
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    //画线
    private void drawLine(Canvas canvas,Paint paint){
        int color=randomColor();
        int startX=random.nextInt(width);
        int startY=random.nextInt(height);
        int stopX=random.nextInt(width);
        int stopY=random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX,startY,stopX,stopY,paint);
    }
    //随机文字样式，颜色，文字粗细与倾斜度
    private void randomTextStyle(Paint paint){
        int color=randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());//true为粗体，false为非粗体
    }
    //生成随机颜色，利用RGB
    private int randomColor(){
        return randomColor(1);
    }
    private int randomColor(int rate){
           ColorBean bean=colorBeans.get(random.nextInt(colorBeans.size()-1));
        return Color.rgb(bean.getRed(),bean.getGreen(),bean.getBlue());
    }
    //验证码位置随机
    private void randomPadding(){
        padding_left=base_padding_left+padding_left;
    }

    public String getCode(){
        return code;
    }

}
