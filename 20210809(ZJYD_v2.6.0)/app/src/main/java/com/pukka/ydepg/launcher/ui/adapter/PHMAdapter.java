package com.pukka.ydepg.launcher.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.MarqueeTextView;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.profile.ProfileManager;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.ui.template.AutoScrollTemplate;
import com.pukka.ydepg.launcher.ui.template.DataLoaderAdapter;
import com.pukka.ydepg.launcher.ui.template.DataLoaderFactory;
import com.pukka.ydepg.launcher.ui.template.FreeLayoutTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTemplate;
import com.pukka.ydepg.launcher.ui.template.HorizontalScrollTimeLineTemplate;
import com.pukka.ydepg.launcher.ui.template.MixVideoTemplate;
import com.pukka.ydepg.launcher.ui.template.MyFunctionTemplate;
import com.pukka.ydepg.launcher.ui.template.MyListForProfileTemplate;
import com.pukka.ydepg.launcher.ui.template.MyListTemplate;
import com.pukka.ydepg.launcher.ui.template.PHMTemplate;
import com.pukka.ydepg.launcher.ui.template.TemplateType;
import com.pukka.ydepg.launcher.ui.template.VerticalScrollTestTemplate;
import com.pukka.ydepg.launcher.ui.template.VideoLiveTemplate;
import com.pukka.ydepg.launcher.util.TemplateFactory;

import java.util.ArrayList;
import java.util.List;

import static com.pukka.ydepg.launcher.ui.MainActivity.IS_SCROLL_NAVIGATION_MODE;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.PHMAdapter.java
 * @date: 2018-03-18 21:31
 * @version: V1.0 描述当前版本功能
 */
public class PHMAdapter extends RecyclerView.Adapter<PHMViewHolder> implements View.OnFocusChangeListener {
    private List<GroupElement> mGroupElements = new ArrayList<>();
    private PHMFragment mTabItemFragment;
    List<SubjectVodsList> mDatas; // 目前只有这一种数据类型。
    private static final String TAG = PHMAdapter.class.getSimpleName();
    private View.OnFocusChangeListener mOnFocusChangeListener = null;

    private int templateHistrotyIndex = -1;
    private int templateFavoriteIndex = -1;
    /*虚拟导航栏总布局*/
    private RelativeLayout mTopView;
    private RelativeLayout mRlLogoChildren;
    private ImageViewExt mIvLogoLeft,mIvLogoRight,mIvLogoChildLeft,mIvLogoChildRight,mIvLogoChildCenter;

    private MarqueeTextView mTvEpgScrollAds;//首页顶部滚动广告tv
    private LinearLayoutExt mLinearContent;//首页功能键content group

    private TextViewExt tvUserTitle;//首页功能键content group
    //首页手动刷新按钮
    private RelativeLayoutExt mRlMainRefresh;
    //private ImageViewExt mImMainRefresh;
    /*虚拟导航栏加载数据RecyclerView*/
    private RecyclerViewTV mRecyclerViewTV;

    /*特殊位置标记，当支持虚拟导航栏时需要+1*/
    private int specialPosition = 0;

    //首页左Logo、首页右Logo、二级桌面左Logo
    private String mHomeTvLogoImgUrl,mHomeTvLogoRightImgUrl,mChildHomeTvLogoLeft,mChildHomeTvLogoRight,mChildHomeTvLogoMiddle;

    private final RelativeLayout.LayoutParams layoutParams;
    //isMain 是否是首页，false=二级桌面
    private boolean isMain = true;
    private boolean isChild = false;

    //滚动，将要显示的position
    private int mWillShowPosition = 0;

    public void setOnFocusChangeListener(View.OnFocusChangeListener mOnFocusChangeListener) {
        this.mOnFocusChangeListener = mOnFocusChangeListener;
    }

    public void addDatas(List<SubjectVodsList> datas, List<GroupElement> groupElements) {
        this.mDatas.addAll(datas);
    }

    public void setDatas(List<SubjectVodsList> datas, List<GroupElement> groupElements) {
        this.mDatas = datas;
        mGroupElements = new ArrayList<>();
        if (IS_SCROLL_NAVIGATION_MODE) {
            mGroupElements.add(0, groupElements.get(0));
        }
        mGroupElements.addAll(groupElements);
        notifyDataSetChanged();
    }

    //设置是否是首页还是二级页面
    public void setIsMain(boolean isMain){
        this.isMain = isMain;
    }

    //设置是否是儿童版首页
    public void setIsChildren(boolean isChild){
        this.isChild = isChild;
    }

    public int getWillShowPosition(){
        return mWillShowPosition;
    }

    public PHMAdapter(List<GroupElement> groupElements, PHMFragment phmFragment, List<SubjectVodsList> datas) {
        if (IS_SCROLL_NAVIGATION_MODE) {
            this.mGroupElements.add(0, groupElements.get(0));
        }
        this.mGroupElements.addAll(groupElements);
        this.mTabItemFragment = phmFragment;
        this.mDatas = datas;
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 创建模板布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public PHMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            // 带有滚动轮播banner的模板单独处理
            case R.layout.pannel_poster_scroll_11:
            case R.layout.pannel_poster_scroll_112135:
            case R.layout.pannel_poster_m_0_11:
                AutoScrollTemplate scrollTemplate = new AutoScrollTemplate(parent.getContext(), viewType, mTabItemFragment, this);
                return new PHMViewHolder(scrollTemplate);
            case R.layout.pannel_poster_l1_r4:
                /*VideoTemplate videoTemplate = new VideoTemplate(parent.getContext(), viewType, mTabItemFragment, this);
                return new PHMViewHolder(videoTemplate);*/
            case R.layout.pannel_poster_live_l1_r4:
                VideoLiveTemplate videoLiveTemplate = new VideoLiveTemplate(parent.getContext(), R.layout.pannel_poster_live_l1_r4, mTabItemFragment, this);
                return new PHMViewHolder(videoLiveTemplate);
            case R.layout.pannel_poster_my_function:
                MyFunctionTemplate template = new MyFunctionTemplate(parent.getContext(), R.layout.pannel_poster_my_function, mTabItemFragment, this);
                return new PHMViewHolder(template);
            case R.layout.pannel_poster_horizontal_timeline_16:
                HorizontalScrollTimeLineTemplate timeLineTemplate = new HorizontalScrollTimeLineTemplate(parent.getContext(), R.layout.pannel_poster_horizontal_timeline_16, mTabItemFragment, this);
                return new PHMViewHolder(timeLineTemplate);
            case R.layout.pannel_poster_horizontal_z_16:
            case R.layout.pannel_poster_horizontal_16:
            case R.layout.pannel_poster_horizontal_nor_16:
            case R.layout.pannel_poster_horizontal_sm_16:
                HorizontalScrollTemplate horizontalTemplateZ = new HorizontalScrollTemplate(parent.getContext(), viewType, mTabItemFragment, this);
                return new PHMViewHolder(horizontalTemplateZ);
            case R.layout.pannel_poster_vertiacal_hot:
                VerticalScrollTestTemplate hotLeaderBoardTemplate = new VerticalScrollTestTemplate(parent.getContext(), R.layout.pannel_poster_vertiacal_hot, mTabItemFragment, this);
                return new PHMViewHolder(hotLeaderBoardTemplate);
            case R.layout.pannel_poster_my_list:
                return new PHMViewHolder(new MyListTemplate(parent.getContext(), R.layout.pannel_poster_my_list, mTabItemFragment, this));
            case R.layout.pannel_poster_my_list_profile:
                return new PHMViewHolder(new MyListForProfileTemplate(parent.getContext(), R.layout.pannel_poster_my_list_profile, mTabItemFragment, this));
            case R.layout.pannel_poster_12_video://一行两列
            case R.layout.pannel_poster_13_video://一行两列
            case R.layout.pannel_poster_024_142231_video://2.4新增
            case R.layout.pannel_poster_024_1422_video://2.4新增
            case R.layout.pannel_poster_024_13_video://2.4新增
                //return new PHMViewHolder(new MixVideoTemplate(parent.getContext(), viewType, mTabItemFragment, this));
                return new PHMViewHolder(new MixVideoTemplate(parent.getContext(), viewType, mTabItemFragment, this));
//            case R.layout.pannel_poster_13_video://一行三列
//                return new PHMViewHolder(new MixThreeVideoTemplate(parent.getContext(), R.layout.pannel_poster_13_video, mTabItemFragment, this));
            case -1:
                return new PHMViewHolder(new PHMTemplate(parent.getContext(), R.layout.pannel_cp_navigation_16, mTabItemFragment, this));
            case R.layout.nav_view_layout:
                PHMTemplate phmTemplateNav = new PHMTemplate(parent.getContext(), R.layout.nav_view_layout, mTabItemFragment, this);
                initScrollNavigate(phmTemplateNav,parent.getContext());
                return new PHMViewHolder(phmTemplateNav);
                //智能推荐模板
            case R.layout.fragment_content://自由编排桌面通用模板布局
                FreeLayoutTemplate freeLayoutTemplate = new FreeLayoutTemplate(parent.getContext(), R.layout.fragment_content, mTabItemFragment, this);
                return new PHMViewHolder(freeLayoutTemplate);
            default:
                PHMTemplate phmTemplate = new PHMTemplate(parent.getContext(), viewType, mTabItemFragment, this);
                return new PHMViewHolder(phmTemplate);
        }

    }

    @Override
    public void onBindViewHolder(PHMViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 配置终端不识别的模板时进行过滤
        if (-1 == getItemViewType(position)) {
            return;
        }
        PHMTemplate phmTemplate = holder.itemView;
        GroupElement groupElement = mGroupElements.get(position);

        //模板处于界面对的position
        mTabItemFragment.setLoadLayoutIndex(position);

        mWillShowPosition = position;
        if (!isMain) {//如果是二级界面，没有标题且没有logo 设置上间距43dp
            if (isChild){
                if (1 == position){
                    layoutParams.setMargins(0, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_20), 0, 0);
                }
            }else if ( 0 == position && phmTemplate.isTitleNull(groupElement)
                && TextUtils.isEmpty(mChildHomeTvLogoLeft) && TextUtils.isEmpty(mChildHomeTvLogoRight) && TextUtils.isEmpty(mChildHomeTvLogoMiddle) ) {
                mRlLogoChildren.setVisibility(View.GONE);
                layoutParams.setMargins(0, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_43), 0, 0);
            }else if (0 == position){
                mRlLogoChildren.setVisibility(View.VISIBLE);
                //如果是坚果投影，单独处理
                if (!CommonUtil.isJMGODevice()){
                    this.layoutParams.setMargins(0, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_down_10), 0, 0);
                    //this.layoutParams.setMargins(0, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_down_2), 0, 0);
                }/*else{
                    this.layoutParams.setMargins(0, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_down_10), 0, 0);
                }*/
            }else{
                layoutParams.setMargins(0, 0, 0, 0);
            }
            holder.itemView.setLayoutParams(layoutParams);
        }

        try {
            // 我的页面模板特殊处理
            //根据group的index属性判断分别对应常用功能、播放历史、我的收藏，其他模板正常处理
            if (mTabItemFragment instanceof MyPHMFragment) {
                MyPHMFragment fragment = (MyPHMFragment) mTabItemFragment;
                //常用功能模板
                if (specialPosition == position) {
                    //holder.setIsRecyclable(false);
                    MyFunctionTemplate functionTemplate = (MyFunctionTemplate) phmTemplate;
                    fragment.setMyFunctionTemplate(functionTemplate);
                    functionTemplate.setDatas(groupElement.getGroup(),groupElement.getElement(), CollectionUtil.isEmpty(mDatas) ? null : mDatas.get(0));
                    functionTemplate.scrollToTop();
                    return;
                }
                //模板正确，名称不正确，直接隐藏该模板
                if (getItemViewType(position) == R.layout.pannel_poster_my_list && !TextUtils.equals(groupElement.getGroup().getNameDialect().get(0).getValue(), mTabItemFragment.getString(R.string.launcher_my_play_history)) &&
                        !TextUtils.equals(groupElement.getGroup().getNameDialect().get(0).getValue(), mTabItemFragment.getString(R.string.launcher_my_collect))) {
                    holder.setVisibility(false);
                }

                //播放历史
                if (TextUtils.equals(groupElement.getGroup().getNameDialect().get(0).getValue(), mTabItemFragment.getString(R.string.launcher_my_play_history)) && getItemViewType(position) == R.layout.pannel_poster_my_list) {
                    templateHistrotyIndex = position;
                    //第一次加载留位置，防止闪现下方内容
                    if (!fragment.isFirstLoadData()) {
                        if (CollectionUtil.isEmpty(fragment.getmBookMarkVods())) {
                            holder.setVisibility(false);
                        } else {
                            holder.setVisibility(true);
                            MyListTemplate template = (MyListTemplate) phmTemplate;
                            fragment.setHistoryTemplate(template);
                            //加载模板内的静态数据
                            if (groupElement.getElement().size() > templateHistrotyIndex){
                                template.setElementData(groupElement.getElement().get(templateHistrotyIndex));
                                template.setExtraData(groupElement.getElement().get(0).getExtraData(),1);
                                template.setGroupAndNavIndex(groupElement.getGroup());
                            }
                            template.loadBookmarkData(fragment.getmBookMarkVodBeans(), fragment.getmBookMarkVods());
                        }
                    } else {
                        //隐藏第三个模板，防止网络相应慢时下方内容不可达
                        if ((specialPosition + 2) == position) {
                            holder.setVisibility(false);
                        }
                    }
                    return;
                }
                //我的收藏
                if (TextUtils.equals(groupElement.getGroup().getNameDialect().get(0).getValue(), mTabItemFragment.getString(R.string.launcher_my_collect)) && getItemViewType(position) == R.layout.pannel_poster_my_list) {
                    templateFavoriteIndex = position;
                    if (!fragment.isFirstLoadData()) {
                        if (CollectionUtil.isEmpty(fragment.getmFavoriteVods())) {
                            holder.setVisibility(false);
                        } else {
                            holder.setVisibility(true);
                            MyListTemplate template = (MyListTemplate) phmTemplate;
                            fragment.setFavoriteTemplate(template);
                            if (groupElement.getElement().size() > templateFavoriteIndex) {
                                template.setElementData(groupElement.getElement().get(templateFavoriteIndex));
                                template.setExtraData(groupElement.getElement().get(0).getExtraData(),2);
                                template.setGroupAndNavIndex(groupElement.getGroup());
                            }
                            template.loadFavoriteData(fragment.getmFavoriteVodBeans(), fragment.getmFavoriteVods());
                        }
                    } else {
                        if ((specialPosition + 2) == position) {
                            holder.setVisibility(false);
                        }
                    }
                    return;
                }

            }

            //view不复用,复用会导致显示复用的数据的画面
            /*if (!isRecyclable(getItemViewType(position))) {
                holder.setIsRecyclable(false);
            }*/
            holder.setIsRecyclable(false);

            //设置模板标题
            if (null != groupElement.getGroup().getNameDialect() && groupElement.getGroup().getNameDialect().size() > 0){
                if (phmTemplate instanceof FreeLayoutTemplate && !TextUtils.isEmpty(groupElement.getGroup().getNameDialect().get(0).getImage())){
                    phmTemplate.setTitle(groupElement.getGroup(),position);
                }else{
                    phmTemplate.setTitle(groupElement.getGroup().getNameDialect().get(0).getValue(),position,groupElement.getGroup().getExtraData());
                }
            }

            //该模板中vod数据在整个页面请求到的vod列表中位置。-1代表该模板中无vod动态数据（如全静态）
            int dataIndex = groupElement.getDataIndex();

            List<VOD> vodsList = null;
            if (null != mDatas && mDatas.size() > 0){
                for (SubjectVodsList subjectVods : mDatas){
                    if (subjectVods.getSubjectID().equalsIgnoreCase(mGroupElements.get(position).getGroup().getCategoryCode())){
                        vodsList = subjectVods.getVodList();
                        break;
                    }
                }
            }
            //加载模板内的静态数据
            phmTemplate.setElementData(groupElement, position);
            if (dataIndex != -1 && null != vodsList) {
                DataLoaderAdapter adapter = DataLoaderFactory.getInstance().getDataLoaderAdapter(mGroupElements.get(position).getGroup().getType());
                if (!CollectionUtil.isEmpty(vodsList)) {
                    // 轮播模板加载数据方法重写
                    if (phmTemplate instanceof AutoScrollTemplate) {
                        ((AutoScrollTemplate) phmTemplate).bindSubjectVOD(vodsList, adapter, null, position);
                    }/* else if (phmTemplate instanceof VideoTemplate) {
                        ((VideoTemplate) phmTemplate).bindSubjectVOD(vodsList, position);
                    }*/ else if (null != adapter){
                        phmTemplate.bindSubjectVOD(vodsList, adapter, position);
                    }
                } else {
                    if (phmTemplate instanceof AutoScrollTemplate) {
                        ((AutoScrollTemplate) phmTemplate).bindSubjectVOD(vodsList, adapter, null, position);
                    } else {
                        phmTemplate.bindSubjectVOD(new ArrayList<>(), adapter, position);
                    }
                }
            }else{
                //解决焦点无法下移问题
                if (phmTemplate instanceof AutoScrollTemplate) {
                    ((AutoScrollTemplate) phmTemplate).setNavIndex(position);
                }
            }

            //配置直播轮播时，dataIndex = -1,无法加载直播数据，所以单独拿到最后面
            if (phmTemplate instanceof VideoLiveTemplate) {
                ((VideoLiveTemplate) phmTemplate).bindSubjectVOD(groupElement.getElement(), position);
                ((VideoLiveTemplate) phmTemplate).setElementData(groupElement, position);
            }else if (phmTemplate instanceof MixVideoTemplate) {
                ((MixVideoTemplate) phmTemplate).parseData(2);
                ((MixVideoTemplate) phmTemplate).setIndex(position);
            }else if (phmTemplate instanceof MyListForProfileTemplate) {
                MyListForProfileTemplate myListForProfileTemplate = (MyListForProfileTemplate)phmTemplate;
                myListForProfileTemplate.setElementData(groupElement);
                myListForProfileTemplate.loadBookmarkData(vodsList);
            }else if (phmTemplate instanceof FreeLayoutTemplate){
                FreeLayoutTemplate freeLayoutTemplate = (FreeLayoutTemplate) phmTemplate;
                freeLayoutTemplate.setNavIndex(position);
                if (dataIndex == -1 || null == vodsList) {
                    freeLayoutTemplate.setDatas(groupElement,null,position,mTabItemFragment);
                } else {
                    freeLayoutTemplate.setDatas(groupElement,vodsList,position,mTabItemFragment);
                }
            }

            setHorizontalScrollTemplateData(phmTemplate, groupElement, dataIndex,vodsList,position);

            //position == 0 是假导航栏  无需加载背景
            if (position != 0 && !(phmTemplate instanceof FreeLayoutTemplate)){
                phmTemplate.setExpendGroupParams(groupElement.getGroup().getExtraData(), groupElement.getGroup().getBackgroud());
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e.getLocalizedMessage());
            SuperLog.error(TAG,e);
        } finally {
        }
    }

    public int getTemplateHistrotyIndex() {
        return templateHistrotyIndex;
    }

    public int getTemplateFavoriteIndex() {
        return templateFavoriteIndex;
    }

    @Override
    public int getItemCount() {
        return mGroupElements == null ? 0 : mGroupElements.size();
    }

    /**
     * 根据group中controlId得到不同的布局id
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        /*（固定布局）滚动导航栏*/
        if (IS_SCROLL_NAVIGATION_MODE) {
            specialPosition = 1;
            if (position == (specialPosition - 1)) {
                return R.layout.nav_view_layout;
            }
        }
        if (null != mGroupElements.get(position).getGroup().getControlInfo()) {
            String controlInfo = mGroupElements.get(position).getGroup().getControlInfo().getControlId();
            if (mTabItemFragment instanceof MyPHMFragment) {
                //我的页面第一个模板使用固定布局，无论配置成什么模板
                if (specialPosition == position) {
                    return R.layout.pannel_poster_my_function;
                }
                if ((TextUtils.equals(mGroupElements.get(position).getGroup().getIndex(), "2") || TextUtils.equals(mGroupElements.get(position).getGroup().getIndex(), "3")) && controlInfo.equals(TemplateType.BOOKMARK_OR_FAVORITE)) {
                    return R.layout.pannel_poster_my_list;
                }
            }

            if (TextUtils.isEmpty(controlInfo)){
                return R.layout.fragment_content;
            }else if (TemplateFactory.getsResourceIdMap().containsKey(controlInfo)) {
                return TemplateFactory.getsResourceIdMap().get(controlInfo);
            } else {
                return -1;
            }
        } else {
            return R.layout.fragment_content;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (null != mOnFocusChangeListener) {
            mOnFocusChangeListener.onFocusChange(view, b);
        }
    }

    public boolean canScroll() {
        if (mTabItemFragment instanceof MyPHMFragment) {
            MyPHMFragment fragment = (MyPHMFragment) mTabItemFragment;
            if ((CollectionUtil.isEmpty(fragment.getmFavoriteVods()) || CollectionUtil.isEmpty(fragment.getmBookMarkVods())) && 3 == mGroupElements.size()) {
                return false;
            }
        }
        return true;
    }

    private void setHorizontalScrollTemplateData(PHMTemplate phmTemplate, GroupElement groupElement, int dataIndex,List<VOD> vodsList,int position) {
        if (phmTemplate instanceof HorizontalScrollTemplate) {
            HorizontalScrollTemplate horizontalScrollTemplateTemplate = (HorizontalScrollTemplate) phmTemplate;
            mTabItemFragment.setHorizontalScrollTemplate(horizontalScrollTemplateTemplate);
            if (dataIndex == -1) {
                horizontalScrollTemplateTemplate.setDatas(groupElement.getElement(), null,groupElement.getGroup(),position);
            } else {
                horizontalScrollTemplateTemplate.setDatas(groupElement.getElement(), vodsList,groupElement.getGroup(),position);
            }
            horizontalScrollTemplateTemplate.scrollToTop();
        }else if (phmTemplate instanceof HorizontalScrollTimeLineTemplate) {
            HorizontalScrollTimeLineTemplate horizontalScrollTemplateTemplate = (HorizontalScrollTimeLineTemplate) phmTemplate;
            mTabItemFragment.setHorizontalScrollTimeLineTemplate(horizontalScrollTemplateTemplate);
            if (dataIndex == -1) {
                horizontalScrollTemplateTemplate.setExtraData(groupElement.getGroup().getExtraData());
                horizontalScrollTemplateTemplate.setDatas(groupElement.getElement(), null,groupElement.getGroup());
            } else {
                horizontalScrollTemplateTemplate.setExtraData(groupElement.getGroup().getExtraData());
                horizontalScrollTemplateTemplate.setDatas(groupElement.getElement(), vodsList,groupElement.getGroup());
            }
            horizontalScrollTemplateTemplate.scrollToTop();
        }else if (phmTemplate instanceof VerticalScrollTestTemplate) {
            VerticalScrollTestTemplate horizontalScrollTemplateTemplate = (VerticalScrollTestTemplate) phmTemplate;
            mTabItemFragment.setVerticalScrollTestTemplate(horizontalScrollTemplateTemplate);
            horizontalScrollTemplateTemplate.setDatas(groupElement.getElement(), (VerticalScrollTestTemplate) phmTemplate,groupElement.getGroup());
            horizontalScrollTemplateTemplate.scrollToTop();
        }
    }

    /**
     * 初始化虚拟滚动栏
     *
     * @param phmTemplate
     */
    private void initScrollNavigate(PHMTemplate phmTemplate,Context context) {
        if (IS_SCROLL_NAVIGATION_MODE && phmTemplate != null) {
            mTopView = (RelativeLayout) phmTemplate.findViewById(R.id.top_view);
            mIvLogoLeft = (ImageViewExt) phmTemplate.findViewById(R.id.iv_logo1);
            mIvLogoRight = (ImageViewExt) phmTemplate.findViewById(R.id.iv_logo2);
            mIvLogoChildLeft = (ImageViewExt) phmTemplate.findViewById(R.id.iv_logo_children);
            mIvLogoChildRight = (ImageViewExt) phmTemplate.findViewById(R.id.iv_logo_children_right);
            mIvLogoChildCenter = (ImageViewExt) phmTemplate.findViewById(R.id.iv_logo_children_center);

            mRlLogoChildren = (RelativeLayout) phmTemplate.findViewById(R.id.rl_logo_children);
            //EPG top 跑马灯及功能键
            mTvEpgScrollAds = phmTemplate.findViewById(R.id.tv_epg_scroll_ads);
            mLinearContent = phmTemplate.findViewById(R.id.linear_content_group);

            tvUserTitle = phmTemplate.findViewById(R.id.tv_user_title);
            setUserTitle();

            if (!mTabItemFragment.getActivity().getClass().getSimpleName().equals(MainActivity.TAG)){
                mTopView.setVisibility(View.GONE);
                setChildHomeLogo(context);
                return;
            }

            setHomeLogo(context);

            MainActivity mActivity = (MainActivity) mTabItemFragment.getActivity();

            RelativeLayout mRelaNavForAdapter = phmTemplate.findViewById(R.id.rela_nav_for_top);
            if (isChild){
                if (null != mRelaNavForAdapter){
                    mRelaNavForAdapter.setVisibility(View.GONE);
                }
                return;
            }
            mRecyclerViewTV = (RecyclerViewTV) phmTemplate.findViewById(R.id.scroll_rv_nav);
            List<Navigate> mNavigates = mActivity.getmNavigates();
            CommonAdapter mNavAdapter = new CommonAdapter<Navigate>(mTabItemFragment.getActivity(), R.layout.item_launcher_scroll_nav, mNavigates) {
                @Override
                protected void convert(ViewHolder holder, Navigate navigate, int position) {
                    holder.setIsRecyclable(false);
                    TextView tvTitle = holder.getView(R.id.tv_nav_title);
                    //ImageView imTitle = holder.getView(R.id.im_nav_title);
                    //有设置tab的背景图片，则不显示tab title,只显示背景图片
                    if (!TextUtils.isEmpty(navigate.getImage()) && !TextUtils.isEmpty(navigate.getFocusImage()) && !TextUtils.isEmpty(navigate.getSecondaryTitleImg())){
                        tvTitle.setVisibility(View.INVISIBLE);
                    }else {
                        tvTitle.setVisibility(View.VISIBLE);
                        if (null != navigate.getNameDialect() && navigate.getNameDialect().size() > 0){
                            holder.setText(R.id.tv_nav_title, navigate.getNameDialect().get(0).getValue());
                        }
                    }
                }
            };
            mRecyclerViewTV.setLayoutManager(new RvLinearLayoutManager(mTabItemFragment.getActivity(), LinearLayoutManager
                    .HORIZONTAL, false,mRecyclerViewTV));
            mRecyclerViewTV.setAdapter(mNavAdapter);
        }
    }

    /**
     * 设置顶部的虚拟导航栏可见性
     *
     * @param b 是否展示
     */
    public void setTopViewVisible(boolean b) {
        if (b && mTopView != null) {
            mTopView.setVisibility(View.VISIBLE);
            setUserTitle();
        } else if (mTopView != null && mTopView.getVisibility() != View.INVISIBLE) {
            mTopView.setVisibility(View.INVISIBLE);
        }
    }

    private void setUserTitle() {
        String tvProfileName = ProfileManager.getProfileInfo().getProfileName();
        if (null != tvUserTitle && !TextUtils.isEmpty(tvProfileName)) {
            tvUserTitle.setVisibility(View.VISIBLE);
            tvUserTitle.setText(tvProfileName);
        } else if (null != tvUserTitle) {
            tvUserTitle.setVisibility(View.GONE);
        }
    }

    public void setHomeLogoUrl(Context context,String mHomeTvLogoImgUrl,String mHomeTvLogoRightImgUrl,String mChildHomeTvLogoLeft,String mChildHomeTvLogoRightUrl,String mChildHomeTvLogoMiddleUrl){
        this.mHomeTvLogoImgUrl = mHomeTvLogoImgUrl;
        this.mHomeTvLogoRightImgUrl = mHomeTvLogoRightImgUrl;
        this.mChildHomeTvLogoLeft = mChildHomeTvLogoLeft;
        this.mChildHomeTvLogoRight = mChildHomeTvLogoRightUrl;
        this.mChildHomeTvLogoMiddle = mChildHomeTvLogoMiddleUrl;
        if (isMain){
            setHomeLogo(context);
        }else{
            setChildHomeLogo(context);
        }
    }

    private void setHomeLogo(Context context){
        if (null == mIvLogoLeft)return;
        mIvLogoLeft.setBackgroundResource(0);
        mIvLogoLeft.setImageResource(0);
        mIvLogoRight.setBackgroundResource(0);
        mIvLogoRight.setImageResource(0);
        if (!TextUtils.isEmpty(mHomeTvLogoImgUrl)){
            Glide.with(context)
                    .load(mHomeTvLogoImgUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mIvLogoLeft.setImageResource(R.drawable.logo1);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mIvLogoLeft.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(mIvLogoLeft);
        }else{
            mIvLogoLeft.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mHomeTvLogoRightImgUrl)){
            Glide.with(context)
                    .load(mHomeTvLogoRightImgUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //mIvLogoRight.setBackgroundResource(R.drawable.logo2);
                            mIvLogoRight.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mIvLogoRight.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(mIvLogoRight);
        }else{
            mIvLogoRight.setVisibility(View.GONE);
        }
    }

    //加载二级桌面Logo
    private void setChildHomeLogo(Context context){
        SuperLog.debug(TAG,"ChildHomeTvLogoLeftUrl="+mChildHomeTvLogoLeft + ";ChildHomeTvLogoRightUrl="+mChildHomeTvLogoRight + ";mChildHomeTvLogoCenterUrl="+mChildHomeTvLogoMiddle);
        if (null != mIvLogoChildLeft && !TextUtils.isEmpty(mChildHomeTvLogoLeft)){
            mIvLogoChildLeft.setVisibility(View.VISIBLE);
            RequestOptions options  = new RequestOptions();
            Glide.with(context).load(mChildHomeTvLogoLeft).apply(options).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mIvLogoChildLeft.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(mIvLogoChildLeft);
        }
        if (null != mIvLogoChildRight && !TextUtils.isEmpty(mChildHomeTvLogoRight)){//mChildHomeTvLogoRight
            mIvLogoChildRight.setVisibility(View.VISIBLE);
            RequestOptions options  = new RequestOptions();
            Glide.with(context).load(mChildHomeTvLogoRight).apply(options).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mIvLogoChildRight.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(mIvLogoChildRight);
        }
        if (null != mIvLogoChildCenter && !TextUtils.isEmpty(mChildHomeTvLogoMiddle)){
            mIvLogoChildCenter.setVisibility(View.VISIBLE);
            RequestOptions options  = new RequestOptions();
            Glide.with(context).load(mChildHomeTvLogoMiddle).apply(options).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    mIvLogoChildCenter.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(mIvLogoChildCenter);
        }
    }

    /**
     * 解决虚拟导航栏背景图片引用已回收的Bitmap引发的Crash
     * 切换到新的tab页面时，把旧的tab页面的假导航栏背景置为null,防止渲染时引用回收的bitmap
     * */
    public void setRecyclerViewTvBackgroundNull(){
        if (null != mRecyclerViewTV){
            mRecyclerViewTV.setBackground(null);
        }
    }

    private boolean isRecyclable(int layoutId){
        if (layoutId == R.layout.pannel_cp_navigation_15 || layoutId == R.layout.pannel_poster_11
                || layoutId == R.layout.pannel_subject_nav_14 || layoutId == R.layout.pannel_poster_12
                || layoutId == R.layout.pannel_poster_13|| layoutId == R.layout.pannel_poster_0_14
                || layoutId == R.layout.fragment_content || layoutId == R.layout.pannel_poster_horizontal_z_16
                || layoutId == R.layout.pannel_poster_horizontal_16 || layoutId == R.layout.pannel_poster_horizontal_nor_16
                || layoutId == R.layout.pannel_poster_horizontal_sm_16 || layoutId == R.layout.pannel_poster_0_222_13 || layoutId == R.layout.pannel_poster_recommend_0_14) {
            return false;
        }
        return true;
    }
}