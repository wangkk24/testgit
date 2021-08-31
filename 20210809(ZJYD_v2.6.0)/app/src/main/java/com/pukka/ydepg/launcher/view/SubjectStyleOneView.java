package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.pukka.ydepg.R;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.SubjectStyleOneView.java
 * @date: 2018-03-11 17:35
 * @version: V1.0 描述当前版本功能
 */


public class SubjectStyleOneView extends RelativeLayout {
    public SubjectStyleOneView(Context context) {
        super(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.viewgroup_subject_style1, this, true);
    }

    public SubjectStyleOneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubjectStyleOneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
