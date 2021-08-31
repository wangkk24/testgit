package com.pukka.ydepg.customui.tv.autoscroll;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.ui.template.TypeThreeLoader;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoScrollViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private static final String TAG = "AutoScrollViewPagerAdapter";
    private RelativeLayoutExt rootView;
    private Context mContext;
    private AutoViewPager mView;
    private int count;//轮播图个数
    private List<Element> elements = new ArrayList<>();
    private Group mGroup;
    private List<VOD> vods = new ArrayList<>();
    private int layoutId;
    private OnAutoViewPagerItemClickListener listener;
    private Map<Integer, Boolean> fourResult = new HashMap<>();
    private Map<Integer, Integer> realIndexMap = new HashMap<>();
    private Map<Integer, ReflectRelativeLayout> mViewMap = new HashMap<>();
    private int selectIndex = 0;


    public AutoScrollViewPagerAdapter(Context context, RelativeLayoutExt rootView, OnAutoViewPagerItemClickListener autoScrollTemplate, List<Element> elements, Group group, List<VOD> dataList, int count, int layoutId) {
        this.mContext = context;
        this.rootView = rootView;
        this.listener = autoScrollTemplate;
        this.elements = elements;
        this.vods = dataList;
        this.count = count;
        this.layoutId = layoutId;
        this.mGroup = group;
        initDatas();
    }

    private void initDatas() {
        int realIndex = 0;
        for (int i = 0; i < elements.size(); i++) {
            if (i >= count) {
                break;
            }
            if (TextUtils.equals(elements.get(i).getForceDefaultData(), "true")) {
                realIndexMap.put(i, i);
                fourResult.put(i, true);
            } else {
                // 当vodlist无数据填充空数据，避免模板留空白
                if (CollectionUtil.isEmpty(vods)) {
                    fourResult.put(i, false);
                } else {
                    if (realIndex >= vods.size()) {
                        break;
                    }
                    realIndexMap.put(i, realIndex);
                    realIndex++;
                    fourResult.put(i, false);
                }
            }
        }
    }

    public void init(AutoViewPager viewPager, AutoScrollViewPagerAdapter adapter) {
        mView = viewPager;
        mView.setAdapter(this);
        mView.addOnPageChangeListener(this);
        mView.setCurrentItem(0);

        mView.start();
        mView.updatePointView(getRealCount());
    }

    public void setListener(OnAutoViewPagerItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return elements == null ? 1 : Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return elements == null ? 0 : Math.min(elements.size(), count);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public ReflectRelativeLayout getView(){
        return view;
    }

    private ReflectRelativeLayout view;
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        int realIndex = 0;
        if (getRealCount() != 0) {
            realIndex = position % getRealCount();
        }


        if (mViewMap.size() > 0 && null != mViewMap.get(realIndex)){
            view = mViewMap.get(realIndex);
            rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    view.setTextViewEffect(hasFocus);
                }
            });
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        // 点击时停止轮播，使其从其他页面返回时还停在原来位置
                        Element element;
                        VOD vod;
                        boolean isDefaultForceData = false;
                        if (!CollectionUtil.isEmpty(elements) && elements.size() > selectIndex) {
                            element = elements.get(selectIndex);
                        } else {
                            element = null;
                        }
                        if (!CollectionUtil.isEmpty(vods) && vods.size() > realIndexMap.get(selectIndex)) {
                            vod = vods.get(realIndexMap.get(selectIndex));
                        } else {
                            vod = null;
                        }
                        if (!fourResult.isEmpty()) {
                            isDefaultForceData = fourResult.get(selectIndex);
                        } else {
                            isDefaultForceData = false;
                        }
                        if (null != element){
                            listener.onItemClick(element, vod, isDefaultForceData);
                        }
                    }
                }
            });
            if (null == view.getParent()){
                //解决偶现问题：java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
                //container.removeView(view);
                container.addView(view);
            }else{
                SuperLog.debug(TAG,"null != view.getParent() and container.removeView(view)");
            }
            return view;
        }


        view = (ReflectRelativeLayout) LayoutInflater.from(mContext).inflate(layoutId, container, false);
        view.setGroup(mGroup);
        rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.setTextViewEffect(hasFocus);
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    // 点击时停止轮播，使其从其他页面返回时还停在原来位置
                    Element element;
                    VOD vod;
                    boolean isDefaultForceData = false;
                    if (!CollectionUtil.isEmpty(elements) && elements.size() > selectIndex) {
                        element = elements.get(selectIndex);
                    } else {
                        element = null;
                    }
                    if (!CollectionUtil.isEmpty(vods) && vods.size() > realIndexMap.get(selectIndex)) {
                        vod = vods.get(realIndexMap.get(selectIndex));
                    } else {
                        vod = null;
                    }
                    if (!fourResult.isEmpty()) {
                        isDefaultForceData = fourResult.get(selectIndex);
                    } else {
                        isDefaultForceData = false;
                    }
                    if (null != element){
                        listener.onItemClick(element, vod, isDefaultForceData);
                    }
                }
            }
        });
        if (fourResult.containsKey(realIndex) && null != view && null != elements && null != realIndexMap) {
            if (fourResult.get(realIndex)) {
                view.setDefaultData(true);
                view.setElementData(elements.get(realIndex));
//                view.setElementData(elements.get(realIndex).getElementDataList().get(0), elements.get(realIndex).getExtraData());
            } else {
                view.setDefaultData(false);
                if (vods!=null&&realIndexMap.containsKey(realIndex) && vods.size() > realIndexMap.get(realIndex)) {
                    view.parseVOD(new TypeThreeLoader(), vods.get(realIndexMap.get(realIndex)));
                } else {
                    view.setEmptyContent();
                }
            }
        }
        container.addView(view);
        mViewMap.put(realIndex,view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (getRealCount() == 0) {
            selectIndex = 0;
        } else {
            selectIndex = position % getRealCount();
        }
        if (getRealCount() == 0){
            mView.onPageSelected(0);
        }else{
            mView.onPageSelected(position % getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnAutoViewPagerItemClickListener {
        void onItemClick(Element element, VOD vod, boolean forceDefaultData);
    }


}
