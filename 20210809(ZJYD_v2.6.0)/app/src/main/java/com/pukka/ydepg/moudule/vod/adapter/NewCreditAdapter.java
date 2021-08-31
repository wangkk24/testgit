package com.pukka.ydepg.moudule.vod.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Cast;
import com.pukka.ydepg.common.http.v6bean.v6node.CastRole;
import com.pukka.ydepg.moudule.vod.activity.OldActorActivity;

import java.util.ArrayList;
import java.util.List;

public class NewCreditAdapter extends RecyclerView.Adapter<NewCreditAdapter.ViewHolder> {

    private List<Cast> mCasts;

    private onClickListenr listenr;

    public NewCreditAdapter(List<CastRole> mediaFiles, List<String> subjectIds) {
        mCasts = new ArrayList<>();
        for (CastRole castRole : mediaFiles) {
            List<Cast> cast = castRole.getCasts();
            if (cast != null && cast.size() != 0) {
                mCasts.addAll(cast);
            }
        }
    }

    private View.OnKeyListener mOnKeyListener;


    public void setOnkeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_credit_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cast cast = mCasts.get(position);
        String castName = cast.getCastName();
        holder.itemView.setTag(cast);
        holder.itemView.setTag(R.id.voddetail_cast_position,position);
        if (!TextUtils.isEmpty(castName)) {
            holder.credtText.setText(castName);
        }
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView credtText;

        public ViewHolder(View itemView) {
            super(itemView);

            if (mOnKeyListener != null) {
                itemView.setOnKeyListener(mOnKeyListener);
            }

            credtText = (TextView) itemView.findViewById(R.id.credit_text);
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TextView text = (TextView) v.findViewById(R.id.credit_text);
                    if (hasFocus) {
                        text.setSelected(true);
                    }else{
                        text.setSelected(false);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listenr){
                        listenr.onClick();
                    }
                    Cast cast = (Cast) v.getTag();
                    //新演员界面ActorActivity
                    Intent intent = new Intent(v.getContext(), OldActorActivity.class);
                    intent.putExtra("search_name", cast.getCastName());
                    intent.putExtra("search_key", cast.getCastName());
                    intent.putExtra("cast_id", cast.getCastID());
                    intent.putExtra("content_type", "VOD");
                    intent.putExtra("search_score", "ACTOR");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public interface onClickListenr{
        void onClick();
    }

    public void setListenr(onClickListenr listenr) {
        this.listenr = listenr;
    }
}
