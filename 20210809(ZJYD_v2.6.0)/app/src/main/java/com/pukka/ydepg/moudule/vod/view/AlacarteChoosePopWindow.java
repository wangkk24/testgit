package com.pukka.ydepg.moudule.vod.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySTBOrderInfoResponse;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.OwnChoose.OwnChooseEvent;
import com.pukka.ydepg.moudule.vod.utils.OrderConfigUtils;
import com.pukka.ydepg.moudule.vod.utils.VodDetailPBSConfigUtils;

import org.greenrobot.eventbus.EventBus;

public class AlacarteChoosePopWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "AlacarteChoosePopWindow";

    Context mContext;

    //单行提示信息
    TextView describeTextSingle;
    //提示信息
    TextView describeText1;
    //提示信息
    TextView describeText2;

    //取消按钮
    ChangeTextViewSpace cancelBtn;

    //确认按钮
    ChangeTextViewSpace confirmBtn;


    //是否是升级的样式
    boolean isUpgrade = false;

    //是否是自选集的样式
    boolean isOwnChoose = false;

    //错误码
    String code = "";

    public final static String ORDER_STR_DEFAULT = "本片为付费内容，观看完整版请购买";

    //剩余自选集包集数
    private String total;

    private VodDetailPBSConfigUtils utils;

    public AlacarteChoosePopWindow(Context mContext,boolean isOwnChoose) {
        Log.i(TAG, "upgradeChoose: code "+code);
        this.mContext = mContext;
        this.isOwnChoose = isOwnChoose;
        this.code = OrderConfigUtils.getInstance().getCode();
        this.utils = OrderConfigUtils.getInstance().getVodDetailPBSConfigUtils();
        if (OwnChooseEvent.CODE_BRONZE_ENOUGH.equals(code) || OwnChooseEvent.CODE_SILVER_ENOUGH.equals(code)){
            isUpgrade = false;
        }else{
            isUpgrade = true;
        }
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.window_pip_alacarte, null);

        describeTextSingle = view.findViewById(R.id.alacarte_text_single);
        describeText1 = view.findViewById(R.id.alacarte_text1);
        describeText2 = view.findViewById(R.id.alacarte_text2);
        cancelBtn    = view.findViewById(R.id.alacarte_cancel_btn);
        confirmBtn   = view.findViewById(R.id.alacarte_confirm_btn);
        cancelBtn.setSpacing(15);
        cancelBtn.setText("取消");
        confirmBtn.setSpacing(15);

        confirmBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    confirmBtn.setTextColor(mContext.getResources().getColor(R.color.white_0));
                }else{
                    confirmBtn.setTextColor(mContext.getResources().getColor(R.color.alacarte_btn_color_unfocused));
                }
            }
        });

        cancelBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    cancelBtn.setTextColor(mContext.getResources().getColor(R.color.white_0));
                }else{
                    cancelBtn.setTextColor(mContext.getResources().getColor(R.color.alacarte_btn_color_unfocused));
                }
            }
        });


        Log.i(TAG, "initView: OrderConfigUtils "+ OrderConfigUtils.getInstance().isAlacarteSuccess() + " "+
                isOwnChoose + " "+ (null != OrderConfigUtils.getInstance().getPresenter()));
        if (!OrderConfigUtils.getInstance().isAlacarteSuccess()){
            if (isOwnChoose && null != OrderConfigUtils.getInstance().getPresenter()){
                this.total = OrderConfigUtils.getInstance().getPresenter().getTotal();
                describeTextSingle.setVisibility(View.GONE);
                describeText1.setVisibility(View.VISIBLE);
                describeText2.setVisibility(View.VISIBLE);
                //请求失败的情况下兼容以前逻辑
                StringBuffer sb1=new StringBuffer();
                StringBuffer sb2=new StringBuffer();
                if (isUpgrade){
                    Log.i(TAG, "upgradeChoose: isUpgrade ");
                    sb1.append("当前");
                    if (OwnChooseEvent.CODE_BRONZE_NOT_ENOUGH.equals(code)){
                        sb1.append("青铜");
                    }else{
                        sb1.append("白银");
                    }
                    sb1.append("会员权益剩余");
                    sb1.append("<strong><font color=\"#a54a00\">");
                    sb1.append("0");
                    sb1.append("</font></strong>");
                    sb1.append("片，");
                    sb2.append("观看完整版，请升级会员。");
                }else{
                    sb1.append("当前");
                    if (OwnChooseEvent.CODE_BRONZE_ENOUGH.equals(code)){
                        sb1.append("青铜");
                    }else{
                        sb1.append("白银");
                    }
                    sb1.append("会员权益剩余");
                    sb1.append("<strong><font color=\"#a54a00\">");
                    sb1.append(total);
                    sb1.append("</font></strong>");
                    sb1.append("片，");
                    sb2.append("观看完整版，将扣除");
                    sb2.append("<strong><font color=\"#a54a00\">");
                    sb2.append("1");
                    sb2.append("</font></strong>");
                    sb2.append("片。");
                }

                if (isUpgrade){
                    confirmBtn.setText("升级");
                }else{
                    confirmBtn.setText("确认");
                }


                describeText1.setText(Html.fromHtml(sb1.toString()));
                describeText2.setText(Html.fromHtml(sb2.toString()));
            }else{
                //非自选包场景订购，请求失败展示默认文本
                isUpgrade = true;
                isOwnChoose = false;
                describeTextSingle.setVisibility(View.VISIBLE);
                describeText1.setVisibility(View.GONE);
                describeText2.setVisibility(View.GONE);

                describeTextSingle.setText(ORDER_STR_DEFAULT);
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setText("购买");
            }

        }else{
            describeText1.setVisibility(View.GONE);
            describeText2.setVisibility(View.GONE);
            describeTextSingle.setVisibility(View.VISIBLE);

            //通过VodDetailPBSConfigUtils请求到了需要展示的样式
            QuerySTBOrderInfoResponse response = utils.getmPlayVodquerySTBOrderInfoResponse();
            //0:会员升级；1：会员订购；2：青铜/白银会员权益充足；3.非会员未订购
            //2走add回调，其他走upgrade回调
            String scenceType = response.getSceneType();
            if (scenceType.equals("2")){
                isUpgrade = false;
            }else{
                isUpgrade = true;
            }

            confirmBtn.setText(response.getBotton());
            String tip = response.getTips();
//            tip = "当前青铜会员权益剩余18片，观看完整版，将扣除1片";
            //约定##作为换行符
            tip = tip.replace("##","\n");
            SpannableString textSpanned1 = new SpannableString(tip);

            String[] ss=tip.split("\\D+");//以非数字分割ss数组中就是你要的

            if (ss.length > 0){
                for (int i = 0; i <ss.length ; i++) {
                    if (i == 0){
                        String s = ss[i];
                        Log.i(TAG, "initView: ss[i]: "+ s + " index: "+tip.indexOf(s));
                        if (tip.contains(s)){
                            textSpanned1.setSpan(new ForegroundColorSpan(
                                            mContext.getResources().getColor(R.color.alacarte_number_color)),
                                    tip.indexOf(s), tip.indexOf(s)+s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }else{
                        String s = ss[i];
                        String lasts = ss[i - 1];
                        Log.i(TAG, "initView: ss[i]: "+ s + " fromIndex:  "+(tip.indexOf(lasts)+lasts.length()) +
                                " index: "+(tip.indexOf(s,tip.indexOf(lasts)+lasts.length())));
                        if (tip.indexOf(s,tip.indexOf(lasts)+lasts.length()) > 0){
                            textSpanned1.setSpan(new ForegroundColorSpan(
                                            mContext.getResources().getColor(R.color.alacarte_number_color)),
                                    tip.indexOf(s,tip.indexOf(lasts)+lasts.length()), tip.indexOf(s,tip.indexOf(lasts)+lasts.length())+s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }




//                    textSpanned1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.alacarte_number_color)),
//                            tip.lastIndexOf(s), tip.lastIndexOf(s)+s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
//            if (tip.contains("1")){
//                Log.i(TAG, "initView: contains 1 "+ tip.indexOf("1"));
//                textSpanned1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.alacarte_number_color)),
//                        tip.indexOf("1片"), tip.indexOf("1片")+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//
//            if (tip.contains("0")){
//                Log.i(TAG, "initView: contains 0 " + tip.indexOf("0"));
//                textSpanned1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.alacarte_number_color)),
//                        tip.indexOf("0片"), tip.indexOf("0片")+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }

            describeTextSingle.setText(textSpanned1);
        }


        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        confirmBtn.requestFocus();

        cancelBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "onKey: "+keyCode);
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN ){
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP ){
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                    if (confirmBtn.getVisibility() == View.VISIBLE){
                        confirmBtn.requestFocus();
                    }
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    return true;
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK ){
                    Log.i(TAG, "KeyEvent.KEYCODE_BACK");
                    if (null != callback){
                        callback.backChoose();
                    }
                    return true;
                }

                return false;
            }
        });

        confirmBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "onKey: "+keyCode);

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN ){
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP ){
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    cancelBtn.requestFocus();
                    return true;
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK ){
                    Log.i(TAG, "KeyEvent.KEYCODE_BACK");
                    if (null != callback){
                        callback.backChoose();
                    }
                    return true;
                }

                return false;
            }
        });

        setContentView(view);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "onKey: "+keyCode);
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK ){
                    Log.i(TAG, "KeyEvent.KEYCODE_BACK");
                    if (null != callback){
                        callback.backChoose();
                    }
                    return true;
                }
                return false;
            }
        });
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        initData();
    }

    private void initData(){
        //请求自选集包消费详情

    }


    public void showView(View parentView){
//        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        this.setBackgroundDrawable(null);
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.RIGHT, 0, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alacarte_cancel_btn:{
                if (null != callback){
                    callback.cancelChoose();
                }

                break;
            }

            case R.id.alacarte_confirm_btn:{
                if (null != callback){
                    if (isUpgrade){
                        callback.upgradeChoose();
                    }else{
                        callback.addOwnChoose();
                    }
                }

//                EventBus.getDefault().post(new PlayUrlEvent(false,false,true,false,""));

                dismiss();

                break;
            }
            default:
        }


    }
    @Override
    public void dismiss() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        if (stackTrace.length >= 2 && "dispatchKeyEvent".equals(stackTrace[1].getMethodName())) {
            //按了返回键
            if(callback != null){
                callback.backChoose();
            }else {
                dismiss2();
            }

        } else {
            //点击外部或者点击关闭调用了dismiss,直接让弹窗消失
            dismiss2();
        }

    }

    //让弹窗消失
    public void dismiss2() {
        super.dismiss();
    }



    public interface AddOwnChooseCallback {

        void addOwnChoose();

        void cancelChoose();

        void upgradeChoose();

        void backChoose();
    }

    private AddOwnChooseCallback callback;

    public void setCallback(AddOwnChooseCallback callback) {
        this.callback = callback;
    }

    public boolean isOwnChoose() {
        return isOwnChoose;
    }

    public void setOwnChoose(boolean ownChoose) {
        isOwnChoose = ownChoose;
    }
}
