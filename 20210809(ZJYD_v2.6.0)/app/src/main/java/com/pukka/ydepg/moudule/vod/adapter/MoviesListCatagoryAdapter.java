package com.pukka.ydepg.moudule.vod.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.vod.presenter.MoviesListPresenter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 *点播栏目列表适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class MoviesListCatagoryAdapter extends RecyclerView.Adapter<MoviesListCatagoryAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG=MoviesListCatagoryAdapter.class.getName();
    private List<Subject> subjects;
    private MoviesListPresenter mMoviesListPresenter;
    private OnItemClickListener mMoviewListOnItemClickListener;
    private String mFocusID;//驻留栏目焦点
    private String currentFocusID;
    private VerticalGridView mRecyclerView;
    private Handler mHandler;

    private static  class CatagoryHandler extends Handler {

        private WeakReference<MoviesListCatagoryAdapter>  catagoryAdapterW;

        public CatagoryHandler(MoviesListCatagoryAdapter adapter){
            catagoryAdapterW=new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            MoviesListPresenter presenter;
            MoviesListCatagoryAdapter adapter;
            if (null != (adapter = catagoryAdapterW.get()) && null != (presenter = adapter.mMoviesListPresenter) && null != msg) {
                if (msg.what == 1) {
                    SuperLog.debug(TAG, "onFocusChange  mHandler send what 1->loadMoviesContent ");
                    if(null!=msg.obj&&msg.obj instanceof Subject) {
                        Subject subject = (Subject) msg.obj;
                        if (!TextUtils.isEmpty(adapter.currentFocusID)) {
                            if (subject.getID().equals(adapter.currentFocusID)) {
                                return;
                            }
                        }
                        presenter.loadFilterContent();
                        presenter.hideFilterCondition();
                        presenter.setOldContentFocusView(null);
                        presenter.resetContentStatus();

                        adapter.currentFocusID = subject.getID();

                        presenter.setSubject(subject);
                        presenter.loadMoviesContent(subject,"0","24");
                        presenter.resetLoadNum();
                        presenter.resetFilterField();
                    }
                } else if (msg.what == 2) {
                    RelativeLayout rel = (RelativeLayout) msg.obj;
                    rel.requestFocus();
                } else if (msg.what == 3) {
                    presenter.setSearchFocus();
                }
            }
        }
    }

    public String getCurrentContentId() {
        return currentFocusID;
    }

    public MoviesListCatagoryAdapter(List<Subject> list, MoviesListPresenter moviesListPresenter, String focusID, TextView categoryName, VerticalGridView recyclerView,Activity activity) {
        this.subjects = list;
        this.mMoviesListPresenter = moviesListPresenter;
        this.mFocusID = focusID;
        this.mRecyclerView = recyclerView;
        this.mHandler = new CatagoryHandler(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_list_catagory, parent, false);
        return new ViewHolder(view);
    }

    public void setmMoviewListOnItemClickListener(OnItemClickListener mMoviewListOnItemClickListener) {
        this.mMoviewListOnItemClickListener = mMoviewListOnItemClickListener;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(subjects.get(position));
        SuperLog.debug(TAG, "onBindViewHolder mFocusID:" + mFocusID + ",item Id:" + subjects.get(position).getID());
        String hasChildren = subjects.get(position).getHasChildren();
        if ("1".equals(hasChildren)) {//有可用子栏目、展示more脚标
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img.setVisibility(View.GONE);
        }
        String focusID = subjects.get(position).getID();
        if (TextUtils.isEmpty(mFocusID) && position == 0 && mRecyclerView.getSelectedPosition() == 0) {
            SuperLog.debug(TAG,"mFocusID 当前驻留焦点："+mRecyclerView.getSelectedPosition());
            Message msg = mHandler.obtainMessage();
            msg.what = 2;
            msg.obj = holder.itemView;
            mHandler.sendMessageDelayed(msg, 200);
        } else {
            if (focusID.equals(mFocusID)) {
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                msg.obj = holder.itemView;
                mHandler.sendMessage(msg);
            }
        }
        holder.name.setText(subjects.get(position).getName());
        if(subjects.size() <= 10){
            if(position == subjects.size() -1){
                mHandler.sendEmptyMessageDelayed(3,200);
            }
        }else{
            if(position == 10){
                mHandler.sendEmptyMessageDelayed(3,200);
            }
        }

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public void onClick(View v) {
        if (mMoviewListOnItemClickListener != null) {
            mMoviewListOnItemClickListener.onItemClick(v, (Subject) v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView img;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.catagory_name);
            img = (ImageView) itemView.findViewById(R.id.category_more);
            itemView.setOnClickListener(MoviesListCatagoryAdapter.this);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.findViewById(R.id.catagory_name).setSelected(true);
                        Subject subject = (Subject) v.getTag();
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = subject;
                        if (mHandler.hasMessages(1)) {
                            mHandler.removeMessages(1);
                        }
                        mHandler.sendMessage(msg);
                    }else{
                        v.findViewById(R.id.catagory_name).setSelected(false);
                    }
                }
            });
        }
    }

    public  interface OnItemClickListener {
        void onItemClick(View view, Subject subject);
    }
}