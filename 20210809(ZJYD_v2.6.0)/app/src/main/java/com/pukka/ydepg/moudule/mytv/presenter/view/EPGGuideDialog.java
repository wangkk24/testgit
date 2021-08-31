package com.pukka.ydepg.moudule.mytv.presenter.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.utils.GlideUtil;

import java.util.List;

/**
 * 节点指引页
 */

public class EPGGuideDialog extends Dialog implements Dialog.OnDismissListener {

    private ImageViewExt im_guide;
    private ImageViewExt im_guide_left;
    private ImageViewExt im_guide_right;
    private ImageViewExt tv_guide_btn;
    private ImageViewExt iv_hint;
    private Context context;
    private RequestOptions options;
    private List<String> epgGuideBgs;
    private int index = 0;
    //private Map<Integer,Drawable> picMap = new HashMap<>();
    private OnDismissListener onDismissListener;

    @SuppressLint("CutPasteId")
    public EPGGuideDialog(Context context,OnDismissListener onDismissListener) {
        super(context,R.style.message_dialog);
        this.context           = context;
        this.onDismissListener = onDismissListener;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_epg_guide,null,false);
        //getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        /*设置全屏无标题头*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        im_guide = view.findViewById(R.id.im_guide);
        im_guide_left = view.findViewById(R.id.im_guide_left);
        im_guide_right = view.findViewById(R.id.im_guide_right);
        iv_hint = view.findViewById(R.id.iv_hint);
        tv_guide_btn = view.findViewById(R.id.tv_guide_btn);
        tv_guide_btn.setOnClickListener(listener);
        im_guide_left.setVisibility(View.GONE);

        if (CommonUtil.isJMGODevice()){
            ViewGroup.LayoutParams layoutParams = tv_guide_btn.getLayoutParams();
            layoutParams.height = context.getResources().getDimensionPixelSize(R.dimen.margin_60);
            layoutParams.width = context.getResources().getDimensionPixelSize(R.dimen.margin_160);
            tv_guide_btn.setLayoutParams(layoutParams);
        }

        /*if (drawables.length == 1){
            im_guide_right.setVisibility(View.GONE);
        }*/

        /*tv_guide_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    tv_guide_btn.setImageResource(R.drawable.epg_guide_btn_focus);
                }else{
                    tv_guide_btn.setImageResource(R.drawable.epg_guide_btn_no_focus);
                }
            }
        });*/

        setContentView(view);//设置View
        setOnDismissListener(this);//设置销毁

    }

    private void initData() {
        options  = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
        loadGuidePic();
    }

    private void loadGuidePic(){
        GlideUtil.loadForResourceReadyEpgGuide(context,epgGuideBgs.get(index),im_guide,R.drawable.default_poster_bg,onGlideResourceReady);
        //GlideApp.with(context).load(epgGuideBgs.get(index)).apply(options).diskCacheStrategy(DiskCacheStrategy.ALL).into(im_guide);
    }

    public void setEpgGuideBgs(List<String> guideBgs){
        this.epgGuideBgs = guideBgs;
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_guide_btn){
                //点击我知道了 推出节点指引
                dismiss();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
            //if (im_guide_left.getVisibility() == View.VISIBLE){
            if (index > 0){
                index--;
                /*im_guide_right.setVisibility(View.VISIBLE);
                if (index == 0){
                    im_guide_left.setVisibility(View.GONE);
                }*/
                loadGuidePic();
            }
        }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            //if (im_guide_right.getVisibility() == View.VISIBLE){
            if (index < epgGuideBgs.size()-1){
                index++;
                /*im_guide_left.setVisibility(View.VISIBLE);
                if (index == epgGuideBgs.size()-1){
                    im_guide_right.setVisibility(View.GONE);
                }*/
                loadGuidePic();
            }
        }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            tv_guide_btn.requestFocus();
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 展示popwindow
     */
    public final void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        initData();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismissListener.onDismiss();
    }

    public interface OnDismissListener{
        void onDismiss();
    }

    private GlideUtil.OnGlideResourceReady onGlideResourceReady = new GlideUtil.OnGlideResourceReady(){

        @Override
        public void onReady() {
            tv_guide_btn.setVisibility(View.VISIBLE);
            iv_hint.setVisibility(View.VISIBLE);
            tv_guide_btn.requestFocus();
        }
    };

}
