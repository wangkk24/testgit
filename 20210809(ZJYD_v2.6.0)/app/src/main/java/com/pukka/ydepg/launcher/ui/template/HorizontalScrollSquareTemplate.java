package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向滑动
 * 264*264 1:1
 *
 * @FileName: com.pukka.ydepg.launcher.ui.template.MyFunctionTemplate.java
 * @author: luwm
 * @data: 2018-06-22 15:03
 * @Version V1.0 <描述当前版本功能>
 */
public class HorizontalScrollSquareTemplate extends PHMTemplate {
    private static final String TAG = "HorizontalScrollTemplate";
    private RecyclerViewTV recyclerViewTV;
    private List<VOD> vods = new ArrayList<>();
    private int realIndex = 0;//资源位对应的真实VOD索引

    private Group mGroup;

    public HorizontalScrollSquareTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
    }

    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        recyclerViewTV = (RecyclerViewTV) findViewById(R.id.rv_horizontal_scro);
        recyclerViewTV.setLayoutManager(new RvLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false,recyclerViewTV));
        recyclerViewTV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getLayoutManager().getPosition(view);
                if (position > 0) {
                    outRect.left = 16;
                }
            }
        });

        onFragmentPause();
    }

    public void setDatas(List<Element> elementList, List<VOD> vodList, Group group) {
        this.vods = vodList;
        this.mGroup = group;
        /*elementList.add(new Element());
        elementList.add(new Element());
        elementList.add(new Element());*/
        realIndex = 0;
        //item_horizontal_scroll:264*207 1.275
        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_horizontal_scroll_square, elementList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                relativeLayout.setGroup(mGroup);
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
                    }else if (null != vods && vods.size() > position){
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
