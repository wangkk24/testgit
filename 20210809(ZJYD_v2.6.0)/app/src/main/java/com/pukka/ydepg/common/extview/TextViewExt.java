package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.Colors;
import com.pukka.ydepg.common.utils.uiutil.Drawables;
import com.pukka.ydepg.common.utils.uiutil.Strings;

import java.io.IOException;


public class TextViewExt extends AppCompatTextView {
    /**
     * The constant TAG for log
     */
    private static final String TAG = "TextViewExt";

    /**
     * The context
     */
    private Context context;

    private int bgTxtNinePatchId;

    private String bgTxtNinePatchName;

    /**
     * The text id
     */
    private int textTxtId;

    /**
     * The text name
     */
    private String textTxtName;

    /**
     * The color text id
     */
    private int colorTxtId;

    /**
     * The color text name
     */
    private String colorTxtName;

    private int bgTxtId;

    private String bgTxtName;

    private int bgTxtColorId;

    private String bgTxtColorName;

    /**
     * The normal text color id
     */
    private int colorTxtNormalId;

    /**
     * The normal text color name
     */
    private String colorTxtNormalName;

    /**
     * The selected text color id
     */
    private int colorTxtSelectedId;

    /**
     * The selected text color name
     */
    private String colorTxtSelectedName;

    public TextViewExt(Context context) {
        super(context);
    }

    public TextViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode() || OTTApplication.getContext().isNeedLoadResource()) {
            this.context = context;
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewExt);
            bgTxtNinePatchId = typeArray.getResourceId(R.styleable.TextViewExt_bgTxtNinePatch, -1);
            textTxtId = typeArray.getResourceId(R.styleable.TextViewExt_textTxt, -1);
            colorTxtId = typeArray.getResourceId(R.styleable.TextViewExt_colorTxt, -1);
            bgTxtId = typeArray.getResourceId(R.styleable.TextViewExt_bgTxt, -1);
            bgTxtColorId = typeArray.getResourceId(R.styleable.TextViewExt_bgTxtColor, -1);
            colorTxtNormalId = typeArray.getResourceId(R.styleable.TextViewExt_colorTxtNormal, -1);
            colorTxtSelectedId = typeArray.getResourceId(R.styleable
                    .TextViewExt_colorTxtSelected, -1);
            typeArray.recycle();

            try {
                if (bgTxtNinePatchId != -1) {
                    bgTxtNinePatchName = getResources().getResourceEntryName(bgTxtNinePatchId);
                }
                if (textTxtId != -1) {
                    textTxtName = getResources().getResourceEntryName(textTxtId);
                }
                if (colorTxtId != -1) {
                    colorTxtName = getResources().getResourceEntryName(colorTxtId);
                }
                if (colorTxtNormalId != -1 && colorTxtSelectedId != -1) {
                    colorTxtNormalName = getResources().getResourceEntryName(colorTxtNormalId);
                    colorTxtSelectedName = getResources().getResourceEntryName(colorTxtSelectedId);
                }
                if (bgTxtId != -1) {
                    bgTxtName = getResources().getResourceEntryName(bgTxtId);
                }
                if (bgTxtColorId != -1) {
                    bgTxtColorName = getResources().getResourceEntryName(bgTxtColorId);
                }
            } catch (Resources.NotFoundException e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    public TextViewExt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode() && OTTApplication.getContext().isNeedLoadResource()) {
            try {
                setDefValue();
                setTextViewCustomString();
                setTextViewCustomColor();
                setTextViewCustomDrawable();
            } catch (IOException e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    private void setDefValue() {
        if (textTxtId != -1)
            setText(textTxtId);
        if (colorTxtId != -1)
            setTextColor(getResources().getColor(colorTxtId));
        if (bgTxtNinePatchId != -1) {
            super.setBackgroundResource(bgTxtNinePatchId);
        } else if (bgTxtId != -1) {
            super.setBackgroundResource(bgTxtId);
        } else if (bgTxtColorId != -1) {
            super.setBackgroundResource(bgTxtColorId);
        }
    }

    private void setTextViewCustomString() throws IOException {
        if (!TextUtils.isEmpty(textTxtName)) {
            String str = Strings.getInstance().getString(textTxtName, getResources().getString(textTxtId));
            if (!TextUtils.isEmpty(str)) {
                setText(str);
            }
        }
    }

    /**
     * Set the background
     */
    private void setTextViewCustomDrawable() {
        // Set the background by image
        if (!TextUtils.isEmpty(bgTxtNinePatchName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getNinePatchDrawable(getResources
                    (), bgTxtNinePatchId));
        } else if (!TextUtils.isEmpty(bgTxtName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawable(getResources(),
                    bgTxtId));
        } else if (!TextUtils.isEmpty(bgTxtColorName)) {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawableColor(getResources(),
                    bgTxtColorId));
        }
    }

    private void setTextViewCustomColor() throws IOException {
        if (!TextUtils.isEmpty(colorTxtName)) {
            setTextColor(Colors.getInstance().getColor(getResources(), colorTxtId));
        } else if (!TextUtils.isEmpty(colorTxtNormalName) && !TextUtils.isEmpty(colorTxtSelectedName)) {
            int ncolor = Colors.getInstance().getColor(getResources(), colorTxtNormalId);
            int scolor = Colors.getInstance().getColor(getResources(), colorTxtSelectedId);

            int[][] states = new int[2][];
            int[] colors = new int[]{scolor, scolor, scolor, ncolor};
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};
            ColorStateList colorStateList = new ColorStateList(states, colors);
            this.setTextColor(colorStateList);
        }
    }

    /**
     * Set the text color for white label
     *
     * @param colorId
     */
    public void setTextColorExt(int colorId) {
        setTextColor(Colors.getInstance().getColor(getResources(), colorId));
    }

    /**
     * Set the text for white label
     *
     * @param stringId
     */
    public void setTextExt(int stringId) {
        setText(Strings.getInstance().getString(stringId));
    }

    /**
     * Set the text for white label
     *
     * @param str
     */
    public void setTextExt(String str) {
        setText(str);
    }

    public void setTextExt(SpannableString str) {
        setText(str);
    }

    /**
     * Set the background by color
     *
     * @param colorId
     */
    public void setBackgroundColorExt(int colorId) {
        setBackgroundColor(Colors.getInstance().getColor(getResources(), colorId));
    }

    public void setBackgroundExt(int resId) {
        bgTxtId = resId;
        bgTxtName = getResources().getResourceEntryName(resId);
        if (OTTApplication.getContext().isNeedLoadResource()) {
            setTextViewCustomDrawable();
        } else {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawable(getResources(), resId));
        }
    }
}
