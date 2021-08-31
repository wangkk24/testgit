package com.pukka.ydepg.moudule.mytv.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.mytv.bean.BodyContentBean;
import com.pukka.ydepg.moudule.mytv.presenter.view.MessageDialogAdapterView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 2018/5/14.
 * 消息记录Adapter
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHold> {

    private RxAppCompatActivity mRxAppCompatActivity;
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    private MessageOnclickListener mMessageOnclickListener;

    private List<String> mNotifitionList;

    public static final String TAG = "MessageAdapter";

    /*
    * 記錄获取焦点的position
    * */
    private int mPosition = 0;

    public MessageAdapter(RxAppCompatActivity rxAppCompatActivity, MessageOnclickListener messageOnclickListener) {
        this.mRxAppCompatActivity = rxAppCompatActivity;
        this.mMessageOnclickListener = messageOnclickListener;
    }

    /*
    * 设置Data
    * */
    public void setData(List<String> notifitionList) {
        this.mNotifitionList = notifitionList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mRxAppCompatActivity).inflate(R.layout.adapter_message, null, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        if (mNotifitionList != null && mNotifitionList.size() > 0 && position < mNotifitionList.size()) {
            String mNotifition = mNotifitionList.get(position);
            try {
                JSONObject object = new JSONObject(mNotifition);
                if (!TextUtils.isEmpty(object.getString("mode"))) {
                    if (object.getString("mode").equals("5")) {
                        //命令通知消息
                        BodyContentBean bodyContentBean = JsonParse.json2Object(mNotifition,BodyContentBean.class);
                        if (bodyContentBean != null && bodyContentBean.getContent() != null){
                            setText(holder,bodyContentBean.getContent().getMessageTitle(),bodyContentBean.getReceivingTime());
                        }
                    } else if (object.getString("mode").equals("0")) {
                        //滚动消息模式
                        setText(holder,object.getString("content"),object.getString("receivingTime"));
                    } else if (object.getString("mode").equals("6")) {
                        //简单文本消息
                        //setText(holder,object.getString("content"),object.getString("validTime"));
                        setText(holder,object.getString("content"),object.getString("receivingTime"));
                    }
                }

            } catch (JSONException e) {
                SuperLog.error(TAG,e);
            }

        }
        if (holder.itemView.hasFocus()) {
            mPosition = position;
        }

            /*
            * 浏览推送信息
            * */
        holder.mRootViewLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNotifitionList != null && mNotifitionList.size() > 0){
                    mMessageOnclickListener.onItemClick(mNotifitionList.get(position),position);
                }
            }
        });

    }

    private void setText(ViewHold holder,String title,String time){
        if (!TextUtils.isEmpty(title)){
            holder.mNameTv.setText(title);
        }else{
            holder.mNameTv.setText("");
        }

        if (!TextUtils.isEmpty(time) && time.length() > 0){
            holder.mTimeTv.setText(time);
        }else{
            holder.mTimeTv.setText(mFormat.format(new Date()));
        }
    }

    @Override
    public int getItemCount() {
        if (mNotifitionList != null && mNotifitionList.size() > 0){
            return mNotifitionList.size();
        }
        return 0;
    }

    class ViewHold extends RecyclerView.ViewHolder {

        TextView mNameTv;
        TextView mTimeTv;
        MessageDialogAdapterView mRootViewLinear;

        public ViewHold(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.name_tv);
            mTimeTv = (TextView) itemView.findViewById(R.id.time_tv);
            mRootViewLinear = (MessageDialogAdapterView) itemView.findViewById(R.id.root_view_linear);

            mRootViewLinear.setFocusListener(hasFocus -> {
                mRootViewLinear.setSelected(hasFocus);
            });
        }
    }

    public interface MessageOnclickListener {
        void onItemClick(String body,int position);
    }

}
