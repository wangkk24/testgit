package com.pukka.ydepg.launcher.ui.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.HorizontalGridView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 时间轴模板
 */
@SuppressLint("ViewConstructor")
public class HorizontalScrollTimeLineTemplate extends PHMTemplate {
    private static final String TAG = "HorizontalScrollTemplate";
    public static final String LEFT = "left";
    public static final String MIDDLE = "middle";
    public static final String PUBLISH_TIME = "publish_time";
    private HorizontalGridView recyclerViewTV;
    private List<VOD> vods = new ArrayList<>();
    private int realIndex = 0;//资源位对应的真实VOD索引

    private Group mGroup;

    public HorizontalScrollTimeLineTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        recyclerViewTV = findViewById(R.id.rv_horizontal_scro);
        //recyclerViewTV.setLayoutManager(new RvLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false,recyclerViewTV));
        //recyclerViewTV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTV.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ALIGNED);
        /*recyclerViewTV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getLayoutManager().getPosition(view);
                if (position > 0) {
                    //outRect.left = 16;
                }
            }
        });*/

        onFragmentPause();
    }

    private ExtraData mExtraData;
    public void setExtraData(ExtraData extraData){
        mExtraData = extraData;
    }

    public void setDatas(List<Element> elementList, List<VOD> vodList, Group group) {
        this.mGroup = group;
        this.vods = vodList;
        realIndex = 0;

        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_horizontal_scroll_timeline, elementList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                Map<String, String> extraData = element.getExtraData();
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = holder.getView(R.id.rr_item_timeline);
                if (null != extraData && !TextUtils.isEmpty(extraData.get(PUBLISH_TIME))){
                    String timeString = extraData.get(PUBLISH_TIME);
                    //List<String> timeList = JsonParse.jsonToStringList(mExtraData.getTimeline_time());
                    //List<String> timeList = JsonParse.jsonToStringList(string);
                    //显示中间
                    if (null != mExtraData && !TextUtils.isEmpty(mExtraData.getTimeline_position()) && mExtraData.getTimeline_position().equalsIgnoreCase(MIDDLE)){
                        TextView tvTimeMiddle = holder.getView(R.id.tv_timeline_time_middle);
                        RelativeLayout rlTimeLineMiddle = holder.getView(R.id.rl_timeline_middle);
                        tvTimeMiddle.setText(timeString);
                        tvTimeMiddle.setVisibility(VISIBLE);
                        rlTimeLineMiddle.setVisibility(VISIBLE);
                    }else{
                        //不配置默认和配置left，靠左显示
                        TextView tvTimeLeft = holder.getView(R.id.tv_timeline_time_left);
                        RelativeLayout rlTimeLineLeft = holder.getView(R.id.rl_timeline_left);
                        tvTimeLeft.setText(timeString);
                        tvTimeLeft.setVisibility(VISIBLE);
                        rlTimeLineLeft.setVisibility(VISIBLE);
                    }
                }else{
                    TextView tvTimeLeft = holder.getView(R.id.tv_timeline_time_left);
                    RelativeLayout rlTimeLineLeft = holder.getView(R.id.rl_timeline_left);
                    tvTimeLeft.setVisibility(INVISIBLE);
                    rlTimeLineLeft.setVisibility(INVISIBLE);
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
                if (position < elementList.size() - 1) {
                    params.setMargins(0, 0, 16, 0);
                    relativeLayout.setLayoutParams(params);
                } else {
                    params.setMargins(0, 0, 0, 0);
                    relativeLayout.setLayoutParams(params);
                }

                relativeLayout.setGroup(mGroup);
                relativeLayout.setElement(element);
                relativeLayout.setRadius(0.0f);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                    relativeLayout.setDefaultData(true);
                    relativeLayout.setElementData(element);
                } else {
                    relativeLayout.setDefaultData(false);
                    if (null != vods && vods.size() > realIndex) {
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(realIndex));
                        realIndex++;
                    } else if (null != vods && vods.size() > position) {
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(position));
                    }
                }
            }
        };
        recyclerViewTV.setAdapter(adapter);
    }

    @Override
    public View getFirstView() {
        return recyclerViewTV;
    }

    public void scrollToTop() {
        recyclerViewTV.getLayoutManager().scrollToPosition(0);
    }
}
