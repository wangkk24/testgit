package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchSubjectAdapter extends RecyclerView.Adapter<SearchSubjectAdapter.SearchSubjectViewHolder>{

    List<SearchSubjectBean> listSubject;

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    private int mCurrentPosition = -1;

    //上一个高亮的栏目标题view
    private View currentSelectSubject;

    //记录item的选中状态
    private List<Boolean> listSelectState;

    //记录item的落焦状态
    private List<Boolean> listFocusState;

    public SearchSubjectAdapter(Context context,List<SearchSubjectBean> data){
        mContext              = context;
        listSubject = data;
        listSelectState = new ArrayList<>();
        for(int i = 0; i< listSubject.size(); i++){
            listSelectState.add(false);
        }

        listFocusState = new ArrayList<>();
        for(int i = 0; i< listSubject.size(); i++){
            listFocusState.add(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(SearchSubjectViewHolder holder, int position) {
        holder.tv.setText(listSubject.get(position).getSubjectName());
        holder.tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //落焦栏目标题高亮,同时上一个栏目标题取消高亮
                    setSelectSubject(position,v);

                    mCurrentPosition = position;
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onSubjectItemClick("", position, listSubject.get(position).getSubjectType() == 1 ? 4 : 5);
                    }
                }
            }
        });

        if(listSelectState.get(position)){
            holder.tv.setSelected(true);
        } else {
            holder.tv.setSelected(false);
        }

        if(listFocusState.get(position)){
            holder.tv.requestFocus();
            listFocusState.set(position,false);//焦点设置完成后清空标志位
        }
    }

    @Override
    public int getItemCount() {
        return listSubject == null ? 0 : listSubject.size();
    }

    @Override
    public SearchSubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchSubjectViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_search_subject_item, parent, false));
    }

    public class SearchSubjectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.search_subject_name_text)
        public TextView tv;
        public SearchSubjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener{
        void onSubjectItemClick(String key, int position, int searchResultGridNumber);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public int getmCurrentPosition() {
        return mCurrentPosition;
    }

    public void setSelectSubject(int pos,View v){
        setSelectSubject(pos);
        //如果需要高亮的栏目和正在高亮的栏目相同则不需要处理,这样设置可以避免没有切换栏目而只是标题失去焦点时取消标题高亮
        if(v != currentSelectSubject){
            v.setSelected(true);//高亮新的需要高亮的栏目
            if( currentSelectSubject !=null ){
                //正在高亮的栏目不为空则取消高亮(栏目首次高亮时正在高亮栏目为空)
                currentSelectSubject.setSelected(false);
            }
            //更新正在高亮的栏目
            currentSelectSubject = v;
        }
    }

    public void setSelectSubject(int pos){
        //将之前的选中状态清除
        for(int i = 0; i< listSelectState.size(); i++){
            if(listSelectState.get(i)){
                listSelectState.set(i,false);
            }
        }
        //设置新的需要获得选中状态的View的position
        listSelectState.set(pos,true);
    }

    public void setFocusSubject(int pos){
        //将之前的获取落焦状态的view位置清除
        for(int i = 0; i< listFocusState.size(); i++){
            if(listFocusState.get(i)){
                listFocusState.set(i,false);
            }
        }
        //设置需要获得落焦状态的View的position
        listFocusState.set(pos,true);
    }
}
