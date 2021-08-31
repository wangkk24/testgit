package com.pukka.ydepg.customui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;

public class FullScreenDialog extends Dialog
{
    public FullScreenDialog(@NonNull Context context)
    {
        super(context, R.style.filter_dialog);
    }

    public FullScreenDialog(@NonNull Context context, int themeResId)
    {
        super(context, themeResId);
    }


    @Override
    public void show()
    {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }
}
