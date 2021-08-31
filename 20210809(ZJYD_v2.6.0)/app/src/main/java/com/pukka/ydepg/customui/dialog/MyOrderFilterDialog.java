package com.pukka.ydepg.customui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.BrowseFrameLayoutExt;

import java.util.Calendar;

/**
 * 在此写用途
 *
 * @FileName: com.pukka.ydepg.customui.dialog.MyOrderFilterDialog.java
 * @author: luwm
 * @date: 2018-08-20 17:40
 * @Version V1.0 <描述当前版本功能>
 */
public class MyOrderFilterDialog extends Dialog {
    TextView year1, year2, year3;
    private static final String TAG = "MyOrderFilterDialog";
    private Context mContext;
    private TextView tvSelectYear = null;
    private TextView tvSelectMonth = null;
    private LinearLayout yearContainer, monthContainer;
    private BrowseFrameLayoutExt rootView;
    private FilterSelectListener mListener;

    public MyOrderFilterDialog(@NonNull Context context) {
        super(context, R.style.filter_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_order_filter_window_layout, null);
        setContentView(view);
        rootView = (BrowseFrameLayoutExt) view;
        initView();
        initListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        //按[返回键]/[菜单键]关闭过滤窗口
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU){
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        yearContainer  = (LinearLayout) findViewById(R.id.my_order_filter_yearContainer);
        monthContainer = (LinearLayout) findViewById(R.id.my_order_filter_monthContainer);
        tvSelectMonth  = (TextView) monthContainer.getChildAt(monthContainer.getChildCount() - 1);
        tvSelectYear   = (TextView) yearContainer.getChildAt(yearContainer.getChildCount() - 1);

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        year1 = (TextView) findViewById(R.id.tv_my_order_filter_year1);
        year2 = (TextView) findViewById(R.id.tv_my_order_filter_year2);
        year3 = (TextView) findViewById(R.id.tv_my_order_filter_year3);

        year1.setText((thisYear - 2) + "");
        year2.setText((thisYear - 1) + "");
        year3.setText(thisYear + "");
    }

    private void initListener() {
        for (int i = 0; i < yearContainer.getChildCount(); i++) {
            //年份焦点变化监听
            yearContainer.getChildAt(i).setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.setSelected(false);            //从月份回到年份,年份取消选中效果
                    tvSelectYear = (TextView) v;     //设置选中年份
                    if( tvSelectMonth != null ){
                        tvSelectMonth.setSelected(false);//从月份回到年份,选中月份取消选中效果
                        tvSelectMonth = null;            //从月份回到年份,清空选中月份
                    }
                }
            });

            //年份按键监听
            final int index = i;
            yearContainer.getChildAt(i).setOnKeyListener((v, keyCode, event) -> {
                //对[全部]年份生效:不响应[右]和[下]操作,防止焦点移动
                if( index == yearContainer.getChildCount() - 1 ){
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
                        return true;
                    }
//                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
//                        return true;
//                    }
                }

                //对所有年份生效:按[确定键]/[方向下]选中,并将焦点移至下方月份处
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN
                        || keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN ) {
                    //从年份区域向下,[当前]年份获取选中效果,[01]月份获得落焦
                    v.setSelected(true);
                    tvSelectYear = (TextView) v;
                    monthContainer.getChildAt(1).requestFocus();
                    return true;
                }
                return false;
            });
        }

        for (int i = 0; i < monthContainer.getChildCount(); i++) {
            //月份按键监听
            monthContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.isSelected()){
                        //当前月份已经选中则关闭查询过滤窗口,同时根据用户选择条件查询订购数据
                        MyOrderFilterDialog.super.dismiss();
                        mListener.onSelected(tvSelectYear.getText().toString(), ((TextView)v).getText().toString());
                    } else {
                        //先取消选中之前选中的月份
                        if( tvSelectMonth !=null ){
                            tvSelectMonth.setSelected(false);
                        }
                        //当前月份没选中则选中
                        v.setSelected(true);
                        tvSelectMonth = (TextView) v;
                    }
                }
            });
        }

        //总体布局 焦点搜索 监听
        rootView.setOnFocusSearchListener((focused, direction) -> {
//            if (direction == View.FOCUS_DOWN && null != yearContainer.findFocus()) {
//                //从年份区域向下,01月获得落焦
//                return monthContainer.getChildAt(1);
//            }
            if (direction == View.FOCUS_UP && null != monthContainer.findFocus()) {
                if (null != tvSelectYear) {
                    return tvSelectYear;
                } else {
                    return yearContainer.getChildAt(1);
                }
            }
            return null;
        });
    }

    public interface FilterSelectListener {
        void onSelected(String year, String month);
    }

    public void setListener(FilterSelectListener selectListener) {
        this.mListener = selectListener;
    }

    @Override
    public void show() {
        super.show();
        //打开过滤对话框后第一个年份获得焦点
        yearContainer.getChildAt(0).requestFocus();
    }
}