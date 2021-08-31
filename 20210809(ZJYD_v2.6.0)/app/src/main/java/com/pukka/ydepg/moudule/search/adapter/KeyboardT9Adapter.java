package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;
import com.pukka.ydepg.moudule.search.view.CircleT9View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 鍵盤适配器
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.adapter.KeyboardCharAdapter.java
 * @author:xj
 * @date: 2017-12-15 15:20
 */
public class KeyboardT9Adapter extends RecyclerView.Adapter<KeyboardT9Adapter.KeyboardViewHolder> {

    Context mContext;

    private final int POSITION_NUMBER_5 = 4;

    private View firstFocusView;

    private List<String> mKeyboardList;
    private OnItemClickListener mOnItemClickListener = new InnerOnItemClickListen();
    private int mCurrentLine = -1;

    public KeyboardT9Adapter(Context mContext) {
        this.mContext = mContext;
        this.mKeyboardList = new ArrayList<>();
        for (int index = 1; index < 10; index++) {
            mKeyboardList.add(index + "");
        }
    }

    @Override
    public KeyboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_keyboard_t9_item, parent, false);
        return new KeyboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KeyboardViewHolder holder, int position) {
        holder.t9NumberTextView.setText(mKeyboardList.get(position));
        holder.t9CharTextView.setText(getT9String(position+1));
        if(position == 0){
            //让数字一的落焦白框变大一些,与其他数字基本一致
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.textLayout.getLayoutParams();
            params.width  = DensityUtil.dip2px(mContext, 45);
            //params.height = DensityUtil.dip2px(mContext, 100);
            // 根据布局参数的设置，重新设置view（这里用了text view，当然其他的view也是通用的）的大小
            holder.textLayout.setLayoutParams(params);
        }
        holder.textLayout.setOnKeyListener(new CircleT9ViewOnKeyListener(holder.circleT9View,position));
        holder.textLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mCurrentLine = position / 3;
                }
            }
        });

        //数字5(pos=4)为默认落焦位置
        if (position == POSITION_NUMBER_5) {
            holder.itemView.requestFocus();
            firstFocusView = holder.itemView;
        }
    }

    private String getT9String(int num) {
        String str;
        switch (num) {
            case 2:
                str = "ABC";
                break;
            case 3:
                str = "DEF";
                break;
            case 4:
                str = "GHI";
                break;
            case 5:
                str = "JKL";
                break;
            case 6:
                str = "MNO";
                break;
            case 7:
                str = "PQRS";
                break;
            case 8:
                str = "TUV";
                break;
            case 9:
                str = "WXYZ";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return null == mKeyboardList ? 0 : mKeyboardList.size();
    }

    public int getCurrentLine(){
        return mCurrentLine;
    }

    public void requestFirstFocus(){
        if(firstFocusView != null){
            firstFocusView.requestFocus();
        }
    }

    private class CircleT9ViewOnKeyListener implements View.OnKeyListener {
        private CircleT9View circleT9View;
        private int position;

        public CircleT9ViewOnKeyListener(CircleT9View circleT9View,int position) {
            this.circleT9View = circleT9View;
            this.position     = position;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // ========= T9键盘不可见时 =========
            if(circleT9View.getVisibility() != View.VISIBLE){
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                    //按下确定键 0字符直接输入,其他字符展开T9键盘
                    if(position==0){
                        //[数字1]没有T9键盘,直接输入1
                        mOnItemClickListener.onNumberItemClick(mKeyboardList.get(position));
                    } else {
                        //[其他数字]T9键盘不可见时点击则展开T9键盘,可见时点击处理交由OnKeyListener
                        circleT9View.init(position+1);
                        circleT9View.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else {
                    //按下其他键交由上层继续处理
                    return false;
                }
            }

            // ========= T9键盘可见时 =========
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                //按键按下时只展示选中效果
                switch (keyCode){
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        //T9键盘下方向可能不存在字符,存在才有动作,否则无反应
                        circleT9View.setSelectPosition(CircleT9View.SelectPosition.DOWN);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        circleT9View.setSelectPosition(CircleT9View.SelectPosition.UP);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        circleT9View.setSelectPosition(CircleT9View.SelectPosition.LEFT);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        circleT9View.setSelectPosition(CircleT9View.SelectPosition.RIGHT);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        circleT9View.setSelectPosition(CircleT9View.SelectPosition.CENTER);
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        //T9键盘展示时按下返回见可以关闭T9键盘
                        circleT9View.setVisibility(View.GONE);
                        return true;
                    default:
                        //按下其他键不做任何处理,交由上层继续处理
                        return false;
                }
            }

            if (event.getAction() == KeyEvent.ACTION_UP) {
                //按键抬起时输入选择字符
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        //T9键盘下方向可能不存在字符,存在才有动作,否则无反应,这个if判断只对KeyEvent.KEYCODE_DPAD_DOWN时有用,其他方向必定不为空
                        if (!TextUtils.isEmpty(circleT9View.getSelectText(CircleT9View.SelectPosition.DOWN))) {
                            mOnItemClickListener.onNumberItemClick(circleT9View.getSelectText(-1));
                            circleT9View.setVisibility(View.GONE);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        mOnItemClickListener.onNumberItemClick(circleT9View.getSelectText(-1));
                        circleT9View.setVisibility(View.GONE);
                        return true;
                    default:
                        return false;
                }
            }

            return false;
        }
    }

    public interface OnItemClickListener{
        void onNumberItemClick(String key);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    private class InnerOnItemClickListen implements OnItemClickListener{
        @Override
        public void onNumberItemClick(String key) {
            SuperLog.info2SD(KeyboardT9Adapter.class.getSimpleName(),"OnItemClickListener is not set !");
        }
    }

    class KeyboardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_t9_layout)
        ViewGroup textLayout;

        @BindView(R.id.item_t9_number)
        TextView t9NumberTextView;

        @BindView(R.id.item_t9_char)
        TextView t9CharTextView;

        @BindView(R.id.t9_circle)
        CircleT9View circleT9View;

        public KeyboardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}