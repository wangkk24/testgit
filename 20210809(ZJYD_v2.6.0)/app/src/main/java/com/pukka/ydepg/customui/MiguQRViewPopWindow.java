package com.pukka.ydepg.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;

public class MiguQRViewPopWindow extends PopupWindow {
    private Context mContext;
    private RelativeLayout mRlQrView;
    private ImageView mQrView;
    private ImageView mMiguLogo;
    private Button mCloseBtn;
    private String mVodCode = "";
    private String mSrcchOrPsrcch = "";
    public static final String mSearchResultType="SEARCH";
    public static final String mFavriteResultType="FAVRITE";
    public static final String mHistoryResultType="HISTORY";
    /**
     * 二维码链接
     */
//    private static final String DEFAULT_LINK_URL =
//            "http://aikanvod.miguvideo.com/video/p/share.jsp?user=guest&vodid=52977374&srcch=20000001&from=singlemessage&isappinstalled=0";
    private static final String DEFAULT_LINK_URL =
            "http://aikanvod.miguvideo.com/video/p/share.jsp?user=guest&srcch=08_01_00__&psrcch=08_01_00__&vodcode=636074757";


    public MiguQRViewPopWindow(Context context, String vodCode, String srcchOrPsrcchType) {
        mContext = context;
        mVodCode = vodCode;
        if(srcchOrPsrcchType.equals(mSearchResultType))
        {
            mSrcchOrPsrcch="08_01_00__";
        }else if(srcchOrPsrcchType.equals(mFavriteResultType))
        {
            mSrcchOrPsrcch="08_01_01_00_";
        }else if(srcchOrPsrcchType.equals(mHistoryResultType)){
            mSrcchOrPsrcch="08_01_01_01_";
        }
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_migupopwindow, null);
        mRlQrView = (RelativeLayout) view.findViewById(R.id.rl_qrcode);
        mQrView = (ImageView) view.findViewById(R.id.iv_qrcode);
        mCloseBtn = (Button) view.findViewById(R.id.iv_close);
        mMiguLogo = (ImageView) view.findViewById(R.id.iv_migulogo);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mQrView.setImageBitmap(createQrcode());
        mCloseBtn.requestFocus();
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCloseBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });

    }

    public void showPopupWindow(View parentView) {

        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B3000000")));
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);

    }

    private Bitmap createQrcode() {
        String url = "";
        if (!TextUtils.isEmpty(mVodCode)) {
            url = DEFAULT_LINK_URL.replaceFirst("636074757", mVodCode);
            if (!TextUtils.isEmpty(mSrcchOrPsrcch)) {
                url = url.replaceAll("08_01_00__", mSrcchOrPsrcch);
            }
            SuperLog.error("MiguQRViewOpoWindow","url ="+url);
        } else {
            SuperLog.error("MiguQRViewOpoWindow","mVodCode is null");
            url = DEFAULT_LINK_URL;
        }
        Bitmap logoMap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.migu_logo);
        return QRCodeGenerator.createQRImage(url, 230, 230, logoMap);
    }
}
