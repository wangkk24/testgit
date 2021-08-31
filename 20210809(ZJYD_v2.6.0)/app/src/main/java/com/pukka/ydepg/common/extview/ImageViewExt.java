package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.Drawables;


public class ImageViewExt extends AppCompatImageView {
    /**
     * The constant TAG for log
     */
    private static final String TAG = "ImageViewExt";

    private int bgImgNinePatchId;

    private String bgImgNinePatchName;

    private int bgImgId;

    private String bgImgName;

    private int bgImgNormalId;

    private String bgImgNormalName;

    private int bgImgSelectedId;

    private String bgImgSelectedName;

    private int srcImgId;

    private String srcImgName;

    private int srcImgNormalId;

    private String srcImgNormalName;

    private int srcImgSelectedId;

    private String srcImgSelectedName;

    /**
     * The background color id
     */
    private int bgImgColorId;

    /**
     * The background color name
     */
    private String bgImgColorName;

    /**
     * The normal background color id
     */
    private int bgImgColorNormalId;

    /**
     * The normal background color id
     */
    private String bgImgColorNormalName;

    private int srcImgColorId;

    private String srcImgColorName;

    /**
     * The selected background color id
     */
    private int bgImgColorSelectedId;

    /**
     * The selected background color name
     */
    private String bgImgColorSelectedName;

    public ImageViewExt(Context context) {
        super(context);
    }

    public ImageViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode() || OTTApplication.getContext().isNeedLoadResource()) {
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewExt);
            bgImgNinePatchId = typeArray.getResourceId(R.styleable.ImageViewExt_bgImgNinePatch, -1);
            bgImgId = typeArray.getResourceId(R.styleable.ImageViewExt_bgImg, -1);
            bgImgNormalId = typeArray.getResourceId(R.styleable.ImageViewExt_bgImgNormal, -1);
            bgImgSelectedId = typeArray.getResourceId(R.styleable.ImageViewExt_bgImgSelected, -1);
            srcImgId = typeArray.getResourceId(R.styleable.ImageViewExt_srcImg, -1);
            srcImgNormalId = typeArray.getResourceId(R.styleable.ImageViewExt_srcImgNormal, -1);
            srcImgSelectedId = typeArray.getResourceId(R.styleable.ImageViewExt_srcImgSelected, -1);
            bgImgColorId = typeArray.getResourceId(R.styleable.ImageViewExt_bgImgColor, -1);
            bgImgColorNormalId = typeArray.getResourceId(R.styleable
                    .ImageViewExt_bgImgColorNormal, -1);
            bgImgColorSelectedId = typeArray.getResourceId(R.styleable
                    .ImageViewExt_bgImgColorSelected, -1);
            srcImgColorId = typeArray.getResourceId(R.styleable.ImageViewExt_srcImgColor, -1);
            typeArray.recycle();

            try {
                if (bgImgNinePatchId != -1) {
                    bgImgNinePatchName = getResources().getResourceEntryName(bgImgNinePatchId);
                }
                if (bgImgId != -1) {
                    bgImgName = getResources().getResourceEntryName(bgImgId);
                }
                if (srcImgColorId != -1) {
                    srcImgColorName = getResources().getResourceEntryName(srcImgColorId);
                }
                if (bgImgColorId != -1) {
                    bgImgColorName = getResources().getResourceEntryName(bgImgColorId);
                }
                if (bgImgNormalId != -1 && bgImgSelectedId != -1) {
                    bgImgNormalName = getResources().getResourceEntryName(bgImgNormalId);
                    bgImgSelectedName = getResources().getResourceEntryName(bgImgSelectedId);
                }
                if (bgImgColorNormalId != -1 && bgImgColorSelectedId != -1) {
                    bgImgColorNormalName = getResources().getResourceEntryName(bgImgColorNormalId);
                    bgImgColorSelectedName = getResources().getResourceEntryName
                            (bgImgColorSelectedId);
                }
                if (srcImgId != -1) {
                    srcImgName = getResources().getResourceEntryName(srcImgId);
                }
                if (srcImgNormalId != -1 && srcImgSelectedId != -1) {
                    srcImgNormalName = getResources().getResourceEntryName(srcImgNormalId);
                    srcImgSelectedName = getResources().getResourceEntryName(srcImgSelectedId);
                }
            } catch (Resources.NotFoundException e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    public ImageViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try{
            super.onDraw(canvas);
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode() && OTTApplication.getContext().isNeedLoadResource()) {
            setDefValue();
            setImageViewCustomDrawable();
        }
    }

    private void setDefValue() {
        if (bgImgNinePatchId != -1) {
            super.setBackgroundResource(bgImgNinePatchId);
        } else if (bgImgId != -1) {
            super.setBackgroundResource(bgImgId);
        } else if (srcImgColorId != -1) {
            super.setImageResource(srcImgColorId);
        } else if (srcImgId != -1) {
            super.setImageResource(srcImgId);
        } else if (bgImgColorId!=-1) {
            super.setBackgroundResource(bgImgColorId);
        } else if (!TextUtils.isEmpty(bgImgColorNormalName) && !TextUtils.isEmpty
                (bgImgColorSelectedName)) {
            Drawable normalDrawable = Drawables.getInstance().getDrawableColor(getResources(),
                    bgImgColorNormalId);
            Drawable selectedDrawable = Drawables.getInstance().getDrawableColor(getResources(),
                    bgImgColorSelectedId);
            setStateDrawble(normalDrawable, selectedDrawable, true);
        }
        // Set the selector
        else if (!TextUtils.isEmpty(bgImgNormalName) && !TextUtils.isEmpty(bgImgSelectedName)) {
            setSelectorDrawable(bgImgNormalId, bgImgSelectedId, true);
        } else if (!TextUtils.isEmpty(srcImgNormalName) && !TextUtils.isEmpty(srcImgSelectedName)) {
            setSelectorDrawable(srcImgNormalId, srcImgSelectedId, false);
        }
    }

    /**
     * Set the background
     */
    private void setImageViewCustomDrawable() {
        // Set the background by image
        if (!TextUtils.isEmpty(bgImgNinePatchName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getNinePatchDrawable(getResources
                    (), bgImgNinePatchId));
        } else if (!TextUtils.isEmpty(bgImgName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawable(getResources(),
                    bgImgId));
        } else if (!TextUtils.isEmpty(srcImgColorName)) {
            super.setImageDrawable(Drawables.getInstance().getDrawableColor(getResources(),
                    srcImgColorId));
        } else if (!TextUtils.isEmpty(srcImgName)) {
            Bitmap dBitmap = Drawables.getInstance().decodeBitmap(srcImgName);
            if (null != dBitmap && !dBitmap.isRecycled()) {
                super.setImageBitmap(dBitmap);
            }
        } else if (!TextUtils.isEmpty(bgImgColorName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawableColor(getResources(),
                    bgImgColorId));
        } else if (!TextUtils.isEmpty(bgImgColorNormalName) && !TextUtils.isEmpty
                (bgImgColorSelectedName)) {
            Drawable normalDrawable = Drawables.getInstance().getDrawableColor(getResources(),
                    bgImgColorNormalId);
            Drawable selectedDrawable = Drawables.getInstance().getDrawableColor(getResources(),
                    bgImgColorSelectedId);
            setStateDrawble(normalDrawable, selectedDrawable, true);
        }
        // Set the selector
        else if (!TextUtils.isEmpty(bgImgNormalName) && !TextUtils.isEmpty(bgImgSelectedName)) {
            setSelectorDrawable(bgImgNormalId, bgImgSelectedId, true);
        } else if (!TextUtils.isEmpty(srcImgNormalName) && !TextUtils.isEmpty(srcImgSelectedName)) {
            setSelectorDrawable(srcImgNormalId, srcImgSelectedId, false);
        }
    }

    private void setSelectorDrawable(int normalResId, int selectedResId, boolean isBackground) {
        Drawable normalDrawable = Drawables.getInstance().getDrawable(getResources(), normalResId);
        Drawable selectedDrawable = Drawables.getInstance().getDrawable(getResources(),
                selectedResId);
        setStateDrawble(normalDrawable, selectedDrawable, isBackground);
    }

    private void setStateDrawble(Drawable normal, Drawable selected, boolean isBackgroud) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_selected}, selected);
        sd.addState(new int[]{}, normal);
        if (isBackgroud) {
            // The version of SDK lower than android4.0 has no setBackground() method, so use
            // setBackgroundDrawable()
            super.setBackgroundDrawable(sd);
        } else {
            super.setImageDrawable(sd);
        }
    }

    /**
     * Set the image resource by image
     *
     * @param drawableId
     */
    public void setImageResourceExt(int drawableId) {
        setImageDrawable(Drawables.getInstance().getDrawable(getResources(), drawableId));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}