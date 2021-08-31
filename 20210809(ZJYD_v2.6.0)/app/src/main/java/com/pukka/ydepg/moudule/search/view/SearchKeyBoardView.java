package com.pukka.ydepg.moudule.search.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;
import com.pukka.ydepg.moudule.search.adapter.KeyboardCharAdapter;
import com.pukka.ydepg.moudule.search.adapter.KeyboardNumberAdapter;
import com.pukka.ydepg.moudule.search.adapter.KeyboardT9Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * 搜索键盘控件
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.view.SearchKeyBoardView.java
 * @author:xj
 * @date: 2017-12-15 10:36
 */
public class SearchKeyBoardView extends LinearLayout implements View.OnClickListener {

    // 全键盘字母部分RecycleView(不包含Z)
    @BindView(R.id.keyboard_char)
    RecyclerView mRecycleViewChar;

    // T9键盘数字部分RecycleView(不包含0)
    @BindView(R.id.keyboard_t9)
    RecyclerView mRecycleViewT9;

    // 数字键盘数字部分RecycleView(不包含0)
    @BindView(R.id.keyboard_number)
    RecyclerView mRecycleViewNumber;

    // 清空按键
    @BindView(R.id.keyboard_clear_btn)
    View mClearBtn;

    // Z/0按键
    @BindView(R.id.keyboard_Z_0)
    TextView mZ0Btn;

    // 退格按键
    @BindView(R.id.keyboard_delete_btn)
    View mDeleteBtn;

    // 切换全键盘按键
    @BindView(R.id.switch_char_keyboard)
    View mSwitchCharKeyboardBtn;

    // 切换全键盘按键
    @BindView(R.id.switch_t9_keyboard)
    View mSwitchT9KeyboardBtn;

    // 切换数字键盘按键
    @BindView(R.id.switch_number_keyboard)
    View mSwitchNumberKeyboardBtn;

    // 全键盘适配器
    private KeyboardCharAdapter mKeyBoardCharAdapter;

    // 数字键盘适配器
    private KeyboardT9Adapter mKeyBoardT9Adapter;

    // 数字键盘适配器
    private KeyboardNumberAdapter mKeyBoardNumberAdapter;

    // 键盘按键事件监听
    private OnKeyBoardClickListener mOnKeyBoardClickListener;

    // 全键盘列表的内容List
    private List<String> mKeyboardCharList   = new ArrayList<>();

    // 数字键盘列表的内容List
    private List<String> mKeyboardNumberList = new ArrayList<>();

    public SearchKeyBoardView(Context context) {
        this(context, null);
    }

    public SearchKeyBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchKeyBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.search_keyboard_view_layout, this, true);
        ButterKnife.bind(this);
        refreshKeyboardList();

        //全键盘相关布局初始化
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5);
        mRecycleViewChar.setLayoutManager(manager);
        mKeyBoardCharAdapter = new KeyboardCharAdapter(getContext(), mKeyboardCharList);
        mKeyBoardCharAdapter.setOnItemClickListener(new KeyboardCharAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String key) {
                if (null != mOnKeyBoardClickListener) {
                    mOnKeyBoardClickListener.onKeyBoardClick(key);
                }
            }
        });
        mRecycleViewChar.setAdapter(mKeyBoardCharAdapter);

        //T9键盘相关布局初始化
        GridLayoutManager t9Manager = new GridLayoutManager(getContext(), 3);
        mRecycleViewT9.setLayoutManager(t9Manager);
        mKeyBoardT9Adapter = new KeyboardT9Adapter(getContext());
        mKeyBoardT9Adapter.setOnItemClickListener(new KeyboardT9Adapter.OnItemClickListener() {
            @Override
            public void onNumberItemClick(String key) {
                if (null != mOnKeyBoardClickListener) {
                    mOnKeyBoardClickListener.onKeyBoardClick(key);
                }
            }
        });
        mRecycleViewT9.setAdapter(mKeyBoardT9Adapter);

        //数字键盘相关布局初始化
        GridLayoutManager numberManager = new GridLayoutManager(getContext(), 3);
        mRecycleViewNumber.setLayoutManager(numberManager);
        mKeyBoardNumberAdapter = new KeyboardNumberAdapter(getContext(), mKeyboardNumberList);
        mKeyBoardNumberAdapter.setOnItemClickListener(new KeyboardNumberAdapter.OnItemClickListener() {
            @Override
            public void onNumberItemClick(String key) {
                if (null != mOnKeyBoardClickListener) {
                    mOnKeyBoardClickListener.onKeyBoardClick(key);
                }
            }
        });
        mRecycleViewNumber.setAdapter(mKeyBoardNumberAdapter);

        //公共布局[清空][0/Z][退格]初始化
        mClearBtn.setOnClickListener(this);
        mZ0Btn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);

        //切换键盘布局[全键盘][T9键盘][数字键盘]初始化
        mSwitchCharKeyboardBtn.setOnClickListener(this);
        mSwitchCharKeyboardBtn.setSelected(true); //初始默认选中全键盘
        mSwitchT9KeyboardBtn.setOnClickListener(this);
        mSwitchNumberKeyboardBtn.setOnClickListener(this);

//        mKeyboardDelete.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (null != mOnKeyBoardClickListener) {
//                    mOnKeyBoardClickListener.onDeleteAll();
//                }
//                return false;
//            }
//        });
//        mKeyboardNumberDelete.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (null != mOnKeyBoardClickListener) {
//                    mOnKeyBoardClickListener.onDeleteAll();
//                }
//                return false;
//            }
//        });
    }

    /**
     * 获取键盘数据
     */
    private void refreshKeyboardList() {
        mKeyboardCharList.clear();
        for (int i = 65; i < 90; i++) {
             char c = (char)i;
             mKeyboardCharList.add(String.valueOf(c));
        }



        mKeyboardNumberList.clear();
        for (int index = 1; index < 10; index++) {
            mKeyboardNumberList.add(index + "");
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keyboard_delete_btn:
                if (null != mOnKeyBoardClickListener) {
                    mOnKeyBoardClickListener.onDelete();
                }
                break;
            case R.id.keyboard_clear_btn:
                if (null != mOnKeyBoardClickListener) {
                    mOnKeyBoardClickListener.onDeleteAll();
                }
                break;
            case R.id.keyboard_Z_0:
                if (null != mOnKeyBoardClickListener) {
                    String s = ((TextView)v).getText().toString();
                    mOnKeyBoardClickListener.onKeyBoardClick(s);
                }
                break;
            case R.id.switch_number_keyboard:
                switchKeyBoard(2);
                break;
            case R.id.switch_char_keyboard:
                switchKeyBoard(0);
                break;
            case R.id.switch_t9_keyboard:
                switchKeyBoard(1);
                break;
            default:
                break;
        }
    }

    public boolean isCharKeyboard(){
        if(mRecycleViewChar.getVisibility() == VISIBLE ){
            return true;
        } else {
            return false;
        }
    }

    //Type键盘类型 0:全键盘 1:T9键盘 2:数字键盘
    public void switchKeyBoard(int type){
        if(type == 0){
            //0:全键盘
            if(mSwitchCharKeyboardBtn.isSelected()){
                return;
            }

            requestFirstFocusFromNumberBoard();//M字符获取焦点
            mRecycleViewChar.setVisibility(VISIBLE);
            mRecycleViewT9.setVisibility(GONE);
            mRecycleViewNumber.setVisibility(GONE);
            mKeyBoardCharAdapter.notifyDataSetChanged();

            mZ0Btn.setText("Z");
            mZ0Btn.setTextSize(COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.keyboard_item_text_size));//单位 30SP
            mSwitchCharKeyboardBtn.setSelected(true);
            mSwitchT9KeyboardBtn.setSelected(false);
            mSwitchNumberKeyboardBtn.setSelected(false);
        } else if(type == 1){
            //1:T9键盘
            if(mSwitchT9KeyboardBtn.isSelected()){
                return;
            }

            mRecycleViewChar.setVisibility(GONE);
            mRecycleViewT9.setVisibility(VISIBLE);
            mRecycleViewNumber.setVisibility(GONE);
            mKeyBoardT9Adapter.notifyDataSetChanged();

            mZ0Btn.setText("0");
            mZ0Btn.setTextSize(COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.content_text_size));//单位24SP
            mSwitchCharKeyboardBtn.setSelected(false);
            mSwitchT9KeyboardBtn.setSelected(true);
            mSwitchNumberKeyboardBtn.setSelected(false);
        } else {
            //2:数字键盘
            if(mSwitchNumberKeyboardBtn.isSelected()){
                return;
            }

            mRecycleViewChar.setVisibility(GONE);
            mRecycleViewT9.setVisibility(GONE);
            mRecycleViewNumber.setVisibility(VISIBLE);
            mKeyBoardNumberAdapter.notifyDataSetChanged();

            mZ0Btn.setText("0");
            mZ0Btn.setTextSize(COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.keyboard_item_text_size));//单位30SP
            mSwitchCharKeyboardBtn.setSelected(false);
            mSwitchT9KeyboardBtn.setSelected(false);
            mSwitchNumberKeyboardBtn.setSelected(true);
        }
    }

    public interface OnKeyBoardClickListener {
        void onKeyBoardClick(String key);
        void onDelete();
        void onDeleteAll();
    }

    public void setOnKeyBoardClickListener(OnKeyBoardClickListener onKeyBoardClickListener) {
        mOnKeyBoardClickListener = onKeyBoardClickListener;
    }

    //用于处理：焦点在键盘最顶端时再按上键焦点原地不动
    public int getCurrentLine() {
        if (mRecycleViewChar.getVisibility() == View.VISIBLE){
            return mKeyBoardCharAdapter.getCurrentLine();
        } else if (mRecycleViewNumber.getVisibility() == View.VISIBLE){
            return mKeyBoardNumberAdapter.getCurrentLine();
        } else {
            return mKeyBoardT9Adapter.getCurrentLine();
        }
    }

    public View requestDeleteFocus() {
        return mDeleteBtn;
    }

    //用于儿童版搜索引导页关闭后键盘获取默认落焦
    public void requestFirstFocus() {
        if (mRecycleViewChar.getVisibility() == VISIBLE) {
            mKeyBoardCharAdapter.requestFirstFocus(false);
        }

        if (mRecycleViewT9.getVisibility() == VISIBLE){
            mKeyBoardT9Adapter.requestFirstFocus();
        }

        if (mRecycleViewNumber.getVisibility() == VISIBLE){
            mKeyBoardNumberAdapter.requestFirstFocus();
        }
    }

    //用于儿童版搜索从数字键盘切换到字符键盘获取默认落焦
    private void requestFirstFocusFromNumberBoard() {
        if (mKeyBoardCharAdapter != null) {
            mKeyBoardCharAdapter.requestFirstFocus(true);
        }
    }
}