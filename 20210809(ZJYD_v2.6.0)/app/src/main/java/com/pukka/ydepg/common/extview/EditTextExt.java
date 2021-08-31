package com.pukka.ydepg.common.extview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;


import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.Colors;
import com.pukka.ydepg.common.utils.uiutil.Drawables;

import java.io.IOException;


public class EditTextExt extends EditText
{
    /**
     * The constant TAG for log
     */
    private static final String TAG = "EditTextExt";

    /**
     * The context
     */
    private Context context;

    private int bgEditNinePatchId;

    private String bgEditNinePatchName;

    /**
     * The hint text id
     */
    private int textHintEditId;

    /**
     * The hint text name
     */
    private String textHintEditName;

    /**
     * The text color id
     */
    private int colorEditId;

    /**
     * The text color name
     */
    private String colorEditName;

    /**
     * The hint text color id
     */
    private int colorHintEditId;

    /**
     * The hint text color name
     */
    private String colorHintEditName;

    private int bgEditId;

    private String bgEditName;

    private int bgColorEditId;

    private String bgColorEditName;

    public EditTextExt(Context context)
    {
        super(context);
    }

    public EditTextExt(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        if (!this.isInEditMode() && OTTApplication.getContext().isNeedLoadResource())
        {
            this.context = context;
            TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextExt);
            bgEditNinePatchId = typeArray.getResourceId(R.styleable.EditTextExt_bgEditNinePatch,
                    -1);
            textHintEditId = typeArray.getResourceId(R.styleable.EditTextExt_textHintEdit, -1);
            colorEditId = typeArray.getResourceId(R.styleable.EditTextExt_colorEdit, -1);
            colorHintEditId = typeArray.getResourceId(R.styleable.EditTextExt_colorHintEdit, -1);
            bgEditId = typeArray.getResourceId(R.styleable.EditTextExt_bgEdit, -1);
            bgColorEditId = typeArray.getResourceId(R.styleable.EditTextExt_bgColorEdit, -1);
            typeArray.recycle();

            try
            {
                if (bgEditNinePatchId != -1)
                {
                    bgEditNinePatchName = getResources().getResourceEntryName(bgEditNinePatchId);
                }
                if (textHintEditId != -1)
                {
                    textHintEditName = getResources().getResourceEntryName(textHintEditId);
                }
                if (colorEditId != -1)
                {
                    colorEditName = getResources().getResourceEntryName(colorEditId);
                }
                if (colorHintEditId != -1)
                {
                    colorHintEditName = getResources().getResourceEntryName(colorHintEditId);
                }
                if (bgEditId != -1)
                {
                    bgEditName = getResources().getResourceEntryName(bgEditId);
                }
                if (bgColorEditId != -1)
                {
                    bgColorEditName = getResources().getResourceEntryName(bgColorEditId);
                }
            }
            catch (Resources.NotFoundException e)
            {
                SuperLog.error(TAG, e);
            }
        }
    }

    public EditTextExt(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow()
    {
        if (!this.isInEditMode())
        {
            super.onAttachedToWindow();
        }
    }

    /**
     * Set the background
     */
    private void setEditTextCustomDrawable()
    {
        // Set the background by image
        if (!TextUtils.isEmpty(bgEditNinePatchName))
        {
            super.setBackgroundDrawable(Drawables.getInstance().getNinePatchDrawable(getResources
                    (), bgEditNinePatchId));
        }
        else if (!TextUtils.isEmpty(bgEditName))
        {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawable(getResources(),
                    bgEditId));
        }
        // Set the background by color
        else if (!TextUtils.isEmpty(bgColorEditName))
        {
            super.setBackgroundDrawable(Drawables.getInstance().getDrawableColor(getResources(),
                    bgColorEditId));
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        if (!this.isInEditMode())
        {
            super.onDetachedFromWindow();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        if (!this.isInEditMode())
        {
            super.onFinishInflate();

            if (OTTApplication.getContext().isNeedLoadResource())
            {
                try
                {
                    setEditTextCustomString();
                    setEditTextCustomColor();
                    setEditTextCustomDrawable();
                }
                catch (IOException e)
                {
                    SuperLog.error(TAG, e);
                }
            }
        }
    }

    private void setEditTextCustomString() throws IOException
    {
        if (!TextUtils.isEmpty(textHintEditName))
        {
            String str = Strings.getInstance().getString(textHintEditName, "");
            if (!TextUtils.isEmpty(str))
            {
                setHint(str);
            }
        }
    }

    private void setEditTextCustomColor() throws IOException
    {
        if (!TextUtils.isEmpty(colorEditName))
        {
            setTextColor(Colors.getInstance().getColor(getResources(), colorEditId));
        }
        if (!TextUtils.isEmpty(colorHintEditName))
        {
            setHintTextColor(Colors.getInstance().getColor(getResources(), colorHintEditId));
        }
    }

    public void setTextExt(String text)
    {
        this.setText(text);
    }
}
