/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.customui.focusView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.BrowseFrameLayoutExt;
import com.pukka.ydepg.common.extview.FrameLayoutExt;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelPlaybillContext;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaybillLite;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.event.OpenVodEvent;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.event.TVGuideEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.ChannelAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.ColumnAdapter;
import com.pukka.ydepg.moudule.livetv.presenter.TVGuidePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.contract.TVGuideContract;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveTVCacheUtil;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.CurrentChannelPlaybillInfo;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 频道页管理容器View
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuideManagerView
 * @Package com.pukka.ydepg.customui.focusView
 * @date 2017/12/26 09:28
 */
public class TVGuideManagerView extends FrameLayoutExt
        implements BrowseFrameLayout.OnChildFocusListener, TVGuideContract.View,
        BaseAdapter.OnItemListener, TVGuideRecycleView.InteceptorListener {

    private static final String TAG = "TVGuideManagerView";

    private static final long DELAY_INVIDATE = 40;

    private TVGuidePresenter mPresenter;

    private String mCurrentChildSubjectID;

    //配置一个栏目id，播放界面只展示频道列表
    private String mSubjectId;

    //1代表查询子栏目，非1代表不查
    private String mIsQuerySubject;

    /**
     * 1代表展示频道号，非1代表不展示
     *  频道号非channelNo,而是频道集合的position，从1开始，同时切台的频道号按照此postion，并非之前的channelNo,(勿影响之前原有逻辑)。
     * */
    private String mIsShowChannelNo;

    @BindView(R.id.browse_container)
    BrowseFrameLayoutExt mBrowseFrameLayout;

    private RxAppCompatActivity mRxAppCompatActivity;

    /*
    * 记录栏目最后选择的position
    * */
    private int mColumnPosition = 0;

    private int selectPos = 0;

    /*
    * 栏目只有一条，则不显示栏目，只显示频道
    * */
    private boolean isShowColumn = true;

    /*
    * 栏目--频道
    * key:subjectid
    * value:频道List
    * */
    private HashMap<String, List<ChannelDetail>> mapSubjectID2Channels = new HashMap<>();

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.mRxAppCompatActivity = rxAppCompatActivity;
    }

    public interface ViewType {
        /**
         * 栏目列表
         */
        String VIEW_COLUMN = "COLUMN";
        /**
         * 当前频道当前节目单列表
         */
        String VIEW_CHANNEL = "CHANNEL";
        /**
         * 回看列表
         */
        String VIEW_TVOD = "TVOD";
        /**
         * 剧集列表
         */
        String VIEW_VOD_SERIES = "VOD_SERIES";
        /**
         * 日期列表
         */
        String VIEW_DATE = "DATE";
        /**
         * 节目单列表
         */
        String VIEW_PROGRAM = "PROGRAM";
    }

    /**
     * 栏目
     */
    @BindView(R.id.rv_column)
    TVGuideRecycleView mColumnListView;
    /**
     * 当前频道当前节目单和TVOD回看VOD共用的RecycleView
     */
    @BindView(R.id.rv_channel)
    TVGuideRecycleView mBTVAndTVODListView;
    /**
     * 日期
     */
    @BindView(R.id.rv_datelist)
    TVGuideRecycleView mDateListView;
    /**
     * 节目单
     */
    @BindView(R.id.rv_program)
    TVGuideRecycleView mProgramListView;
    /**
     * 左边指向箭头
     */
    @BindView(R.id.iv_left)
    ImageView mLeftImageView;
    /**
     * 分割线
     */
    @BindView(R.id.v_columnline)
    View mColumnLine;

    @BindView(R.id.v_channel_line)
    View mChannelLine;

    @BindView(R.id.v_date_line)
    View mDateLine;

    @BindView(R.id.rl_nodata)
    RelativeLayout mChannelNoDataLayout;

    /**
     * Item间距集合
     */
    private ArrayMap<TVGuideRecycleView, Integer> mSpaceMaps = null;
    /**
     * adapter集合
     */
    private ArrayMap<TVGuideRecycleView, BaseAdapter> mAdapterMaps = null;

    /**
     * 栏目Adapter
     */
    private ColumnAdapter mColumnAdapter;
    /**
     * 当前频道对应的当前节目单Adapter
     */
    private ChannelAdapter mChannelAdapter;

    /**
     * 当前回看VOD查询的起始位置
     */
    private int mVODListOffset = 0;


    /**
     * 当前频道当前节目单上下文起始位置
     */
    private volatile int mPlaybillOffset = 0;
    /**
     * 当前频道当前节目单上下文每次分页获取的数据大小
     */
    private static final int PLAYBILL_COUNT = 14;

    /**
     * 当前栏目选中的subjectId
     */
    private String mSelectSubjectId;

    /**
     * 是否清空回看记忆的焦点位置
     */
    private volatile boolean isClearRecordPos;

    /**
     * 当前频道当前节目单信息列表
     */
    private List<CurrentChannelPlaybillInfo> mCurrentPlaybillInfos;

    /*
     *  当前用户可以观看的所有栏目Id
     */
    private volatile List<Subject> listSubjectID;

    private boolean isSpecialSubject = false;

    public TVGuideManagerView(@NonNull Context context) {
        this(context, null);
    }

    public TVGuideManagerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVGuideManagerView(@NonNull Context context, @Nullable AttributeSet attrs,
                              @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 视图被依附到别的容器中时候回调
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter = new TVGuidePresenter();
        mPresenter.attachView(this);
        mPresenter.initialize(mSpaceMaps, mAdapterMaps);
        //初始化监听器
        initListener();

        // 获取缓存到本地的栏目-频道，key为栏目id，value为频道集合
        mapSubjectID2Channels = LiveDataHolder.get().getSaveHashMap();

        // 获取缓存到本地的所有栏目
        Log.e("gwptest", "TVGuideManagerView mSubjectId is " + mSubjectId);
        if(!TextUtils.isEmpty(mSubjectId)){
            isSpecialSubject = true;
            listSubjectID = LiveDataHolder.get().getTargetChildSubject(mSubjectId);
            if(listSubjectID == null || listSubjectID.size() == 0){
                listSubjectID = new ArrayList<>();
                listSubjectID.add(LiveDataHolder.get().getTargetSubject(mSubjectId));
            }
//            if(TextUtils.equals(mIsQuerySubject, "1")){
//            }
//            else{
//                listSubjectID = new ArrayList<>();
//                listSubjectID.add(LiveDataHolder.get().getTargetSubject(mSubjectId));
//            }

        }
        if(listSubjectID == null || listSubjectID.size() == 0) {
            listSubjectID = LiveDataHolder.get().getSaveSubjectList();
            isSpecialSubject = false;
        }
        if(mChannelAdapter != null) {
            mChannelAdapter.setSpecialSubject(isSpecialSubject);
            mChannelAdapter.setmIsShowChannelNo(mIsShowChannelNo);
        }

        // 设置栏目-频道到guid
        onChannelColumn();
    }

    /**
     * 视图从依附的容器中移除的时候回调
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mPresenter) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    /**
     * xml映射完成
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDateListView.setVisibility(View.GONE);
        mProgramListView.setVisibility(View.GONE);

        //间距集合
        mSpaceMaps = new ArrayMap<>();
        mSpaceMaps.put(mColumnListView, getResources().getDimensionPixelSize(R.dimen.
                epglist_rvcolumn_bottomspace));
        mSpaceMaps.put(mBTVAndTVODListView, getResources().getDimensionPixelSize(R.dimen.
                epglist_rvchannel_bottomspace));

        //adapter集合
        mAdapterMaps = new ArrayMap<>();
        mAdapterMaps.put(mColumnListView, (mColumnAdapter = new ColumnAdapter(getContext(), new ArrayList<>())));
        mAdapterMaps.put(mBTVAndTVODListView, (mChannelAdapter = new ChannelAdapter(getContext(), new ArrayList<>())));
        mChannelAdapter.setSpecialSubject(isSpecialSubject);
        mChannelAdapter.setmIsShowChannelNo(mIsShowChannelNo);
        mChannelAdapter.setManagerView(this);
    }

    /**
     * 初始化监听设置
     */
    public void initListener() {
        mBrowseFrameLayout.setOnChildFocusListener(this);
        //recycleView ==> dispatchKeyEvent回调
        mColumnListView.setInteceptor(this);
        mBTVAndTVODListView.setInteceptor(this);
        //item选中监听器
        mColumnAdapter.setOnItemListener(this);
        mChannelAdapter.setOnItemListener(this);
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return false;
    }

    /**
     * 拦截KeyEvent事件,内部消费掉DPAD_LEFT和DPAD_RIGHT
     */
    @Override
    public boolean onInterceptorFocus(int keyCode, String viewType) {
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue == RemoteKeyEvent.BTV || codeValue == RemoteKeyEvent.TVOD) {
            View itemView = mColumnListView.getRecordFocusView();
            //只有当前view不是焦点状态,才能清空
            if (null != itemView && !itemView.isFocused()) {
                //清除栏目中item保存的焦点位置view的背景和字体粗|正常
                ((BaseLinearFocusView) itemView).updateRecordViewStatus(true);
            }
            //场景:当前列表在节目单列表或者日期列表,回看子集列表的时候;
            //此时点击切换btv/tvod时,需要隐藏右边的日期,节目单或者电视剧子集列表,同时动画回来
            //然后选中BTV或者TVOD的Item
            if (mLeftImageView.getVisibility() == View.VISIBLE) {
                scrollToAnim(false);
            }
        } else {
            //启动点播keycode
            if (codeValue == RemoteKeyEvent.VOD) {
                EventBus.getDefault().post(new OpenVodEvent());
                return true;
            }
        }
        if (codeValue == RemoteKeyEvent.BTV) {
            //当前是直播
            ((LiveTVActivity) mRxAppCompatActivity).showTVGuide();
        } else if (codeValue == RemoteKeyEvent.TVOD) {
            //当前是回看
            //((LiveTVActivity) mRxAppCompatActivity).showTVGuide();
            ((LiveTVActivity) mRxAppCompatActivity).jumpToCatchUp();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {//左键
            switch (viewType) {
                case ViewType.VIEW_TVOD:
                case ViewType.VIEW_CHANNEL:
          /*
           * itemView类型是当前直播频道节目单列表,回看列表,左移动
           * 栏目列表上次记录的焦点view手动触发获取焦点选中.
           */
          /*
          * 大于一条栏目时，执行向左操作
          * */
                    if (isShowColumn) {
                        mColumnListView.recordViewRequestFocus();
                        scrollToAnim(false);
                        if (viewType.equals(ViewType.VIEW_CHANNEL)) {
                            mBTVAndTVODListView.getLayoutManager().scrollToPosition(LiveDataHolder
                                    .get().getChannelSelectPosition());
                        }
                    }

                    break;
                case ViewType.VIEW_DATE:
                case ViewType.VIEW_VOD_SERIES:
                    mChannelAdapter.updateRightIconStatus(false);
          /*
           * itemView类型是日期或者是电视剧子集列表,左移动
           * 回看列表上次记录的焦点View手动触发获取焦点
           */
                    mBTVAndTVODListView.recordViewRequestFocus();
                    break;
                case ViewType.VIEW_PROGRAM:
          /*
           * 当前是节目单列表,做移动,日期列表记录的焦点View获取焦点
           */
                    mDateListView.recordViewRequestFocus();
                    break;
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {//右键
            switch (viewType) {
                case ViewType.VIEW_COLUMN:
                    //从栏目列表右移动
                    correctRecycleViewFocus(isClearRecordPos, mColumnListView, mBTVAndTVODListView);
                    break;
				default:
					break;
            }
            return true;
        }
        return false;
    }

    /**
     * 纠正两个列表直接数据切换过程中出现的问题.
     * <p>
     * 备注:测试过程中复现bug,在某个栏目下翻页,返回切出去到别的栏目没有数据或者数据少,反复切
     * 会出现右侧的view调用requestFocus之后返回true,但是不走view自身的onFocusChanged回调
     * 也就是说栏目的view还是处在获取焦点状态,
     * 所以需要让右侧的recycleview先获取到焦点,然后在调用view.requestFocus()
     *
     * @param isClearRecord true不调用记忆的位置,false调用记忆的位置
     * @param orgRv         原位置的recycleview
     * @param destRv        目标位置的recycleview
     */
    private void correctRecycleViewFocus(boolean isClearRecord, TVGuideRecycleView orgRv,
                                         TVGuideRecycleView destRv) {
        RecyclerView.Adapter adapter = destRv.getAdapter();
        if (null == adapter) return;
        if (adapter.getItemCount() > 0) {
            //当前所在位置是栏目列表,准备向右移动
            if (isClearRecord) {//清空记忆位置
                destRv.itemViewRequestFocus(0);
            } else {
                if (adapter instanceof ChannelAdapter) {
                    if (mColumnPosition != LiveDataHolder.get().getCulomnSelectPosition()) {
                        destRv.itemViewRequestFocus(0);
                    } else {
                        //如果目标的adapter是频道列表,则选中当前正在播放的频道
                        View view = destRv.itemViewRequestFocus(LiveDataHolder.get().getChannelSelectPosition());
                        if (null == view) {
                            //可能没有获取到该位置的View,那么取第一位的view获取焦点
                            destRv.itemViewRequestFocus(0);
                        }
                    }

                } else {
                    destRv.recordViewRequestFocus();
                }
            }
            postDelayed(() -> {
                if (null != orgRv.getRecordFocusView() && orgRv.getRecordFocusView().isFocused()) {
                    destRv.requestFocus();
                    if (isClearRecord) {//清空记忆位置
                        destRv.itemViewRequestFocus(0);
                    } else {
                        destRv.recordViewRequestFocus();
                    }
                }
            }, DELAY_INVIDATE);
        }
    }

    /**
     * childLayout接收到焦点时候的回调
     *
     * @param child   viewgroup
     * @param focused 当前获取到焦点的view&viewgroup
     */
    @Override
    public void onRequestChildFocus(View child, View focused) {
        if (focused instanceof BaseLinearFocusView) {
            //进入adapter中的item时候,根据不同类型更新上一个条目的状态
            String viewType = String.valueOf(focused.getTag());
            switch (viewType) {
                case ViewType.VIEW_CHANNEL:
                    //当前是频道条目
                    mColumnListView.switchRecordViewBackground();
                    break;
                default:
                	break;
            }
        }
    }

    /**
     * item选中事件回调监听
     * 选中的时候可以做的事情;
     * 诸如:栏目条目选中的时候,需要加载对应的数据,回看列表,是电视剧的时候需要先查询详情获取子集数据,
     * 记录某个栏目选中的ID等等.........
     *
     * @see View#setOnFocusChangeListener(android.view.View.OnFocusChangeListener)
     */
    @Override
    public void onItemSelect(String viewType, int position) {
        SuperLog.debug(TAG, "[onItemSelect]:" + viewType);
        switch (viewType) {
            case ViewType.VIEW_COLUMN:
                if(mColumnPosition != position){
					mPlaybillOffset = 0;
				}
                mColumnPosition = position;
                isClearRecordPos = false;
                Subject subject = mColumnAdapter.getItemPosition(position);
                if (null == subject) return;
                mVODListOffset = 0;
                if (!TextUtils.isEmpty(mSelectSubjectId) && !mSelectSubjectId.equals(subject.getID())) {
                    mChannelAdapter.clearAll();
                }
                //记录当前回看列表的栏目ID
                mSelectSubjectId = subject.getID();
                if (subject != null && subject.getID() != null && mChannelAdapter.getItemCount() == 0) {
                    List<ChannelDetail> channelDetailList = mapSubjectID2Channels.get(subject.getID());
                    getCurrentChannelPlaybill(channelDetailList);
                }
                break;
			default:
				break;

        }
    }

    /**
     * Item点击事件回调监听,点击条目事件
     *
     * @param viewType viewType
     * @param position position
     */
    @Override
    public void onItemClickListener(String viewType, int position) {
        SuperLog.debug(TAG, "[onItemClickListener]>viewType:" + viewType + ",position:" + position);
        switch (viewType) {
            case ViewType.VIEW_CHANNEL:
                // 记录当前选中的栏目和频道位置
                LiveDataHolder.get().setChannelSelectPosition(position);
                LiveDataHolder.get().setCulomnSelectPosition(mColumnPosition);
                //点击应该是播放当前频道当前的节目单
                CurrentChannelPlaybillInfo playbillInfo = mChannelAdapter.getItemPosition(position);

                //点击发生切台，上边代码已经保存栏目和频道位置，鉴权时后不用存频道位置和栏目位置
				LiveDataHolder.get().setMiniEpg(true);

                //对当前频道进行鉴权
                EventBus.getDefault().post(new PlayUrlEvent(playbillInfo, listSubjectID.get(mColumnPosition).getID(), true));
                //测试要求点击OK键之后消失TVGUIDE页
                EventBus.getDefault().post(new TVGuideEvent(false));
                break;
        }
    }

    /**
     * 栏目列表数据加载成功回调
     */
    @Override
    public void onChannelColumn() {

        if (mapSubjectID2Channels == null || mapSubjectID2Channels.size() == 0 || listSubjectID == null || listSubjectID.size() == 0) {
            mColumnListView.setVisibility(View.GONE);
            mColumnLine.setVisibility(View.GONE);
            mBTVAndTVODListView.setVisibility(View.GONE);
            EpgToast.showLongToast(mRxAppCompatActivity, "无可播放频道");
            return;
        }

        /*
        * LiveDataHolder.get().getIndex() == -1
        * 只在重新登陆时调用一次
        * infos：获取存储的频道ID和MediaId数组是否为空,为空则表示：第一次安装进入或者切换了新用户登陆
        * defaultChannelNO：终端参数返回的默认频道Id
        * 存储的UserId和Session中的UserId是否一样
        * */
		String[] infos 			= LiveTVCacheUtil.getInstance().getRecordPlayChannelInfo();
        String defaultChannelNO = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.PLAY_DEFAULT_CHANNEL);
		List<ChannelDetail> channelDetailListCanPlay = LiveDataHolder.get().getChannelPlay();
        if (infos == null && !TextUtils.isEmpty(defaultChannelNO) && (TextUtils.isEmpty(LiveDataHolder.get().getUserIdIndexIndex())
                || !LiveDataHolder.get().getUserIdIndexIndex().equals(SessionService.getInstance().getSession().getUserId()))) {
            LiveDataHolder.get().setUserIdIndexIndex();
			LiveDataHolder.get().setMiniEpg(true);
            int channelIndex = LiveUtils.parseChannelIdIndex(defaultChannelNO, channelDetailListCanPlay);
            if (channelIndex != -1) {
                getPosition(channelDetailListCanPlay.get(channelIndex), channelIndex);
            } else {
                LiveDataHolder.get().setCulomnSelectPosition(0);
                LiveDataHolder.get().setChannelSelectPosition(0);
            }
        }

        if (listSubjectID.size() == 1) {
            // 栏目只有一条时，隐藏栏目，只展示频道
            mColumnListView.setVisibility(View.GONE);
            mColumnLine.setVisibility(View.GONE);
            isShowColumn = false;
            mColumnAdapter.clearAll();
            mColumnAdapter.bindData(listSubjectID);
            Subject subject = listSubjectID.get(0);
            if (subject != null && subject.getID() != null) {
                if (LiveDataHolder.get().getChannelSelectPosition() >= 14) {
                    mChannelAdapter.setRequestDataStatus(false);
                    requestPlaybillContextByTime(LiveDataHolder.get().getChannelSelectPosition());
                } else {
                    List<ChannelDetail> channelDetailList = mapSubjectID2Channels.get(subject.getID());
                    //LiveDataHolder.get().setChannelPlay(channelDetailList);
                    getCurrentChannelPlaybill(channelDetailList);
                }
            }
        } else if (listSubjectID.size() > 1) {//大于一条，显示栏目+频道
            mColumnListView.setVisibility(View.VISIBLE);
            mColumnLine.setVisibility(View.VISIBLE);

            mColumnAdapter.clearAll();
            mColumnAdapter.bindData(listSubjectID);
            //delay
            postDelayed(() -> {
                mColumnListView.itemViewRequestFocus(LiveDataHolder.get().getCulomnSelectPosition());
            }, DELAY_INVIDATE);
        } else {
        	SuperLog.error(TAG,"The size of listSubjectID is " + listSubjectID.size());
		}
    }

    /**
     * 当前频道当前节目单列表加载成功
     *
     * @param list 当前频道当前节目单列表
     */
    @Override
    public void onLoadChannelPlaybillSucc(List<ChannelPlaybillContext> list) {
        SuperLog.debug(TAG, "onLoadChannelPlaybillSucc====当前频道当前节目单列表加载成功==start");
        boolean isFirst = mChannelAdapter.getItemCount() == 0;
        if (null == list || list.size() == 0 && isFirst) {
            //没有数据
            mChannelNoDataLayout.setVisibility(View.VISIBLE);
            mChannelAdapter.clearAll();
        } else {
            mChannelNoDataLayout.setVisibility(View.GONE);
            //设置更新频道节目单列表区间[mPlaybillOffset,end]
            int end = mPlaybillOffset + PLAYBILL_COUNT;
            if(null!=mCurrentPlaybillInfos) {
                if (end > mCurrentPlaybillInfos.size()) {
                    end = mCurrentPlaybillInfos.size();
                }
                int index = 0;
                CurrentChannelPlaybillInfo playbillInfo;
                PlaybillLite playbillLite;
                long refreshTime = System.currentTimeMillis();

                //List<CurrentChannelPlaybillInfo> mJbCurrentChannelPlaybillInfos = new ArrayList<>();
                //更新当前频道当前节目单列表,刷新列表中节目单名称和ID;
                for (int i = mPlaybillOffset; i < end; i++) {
                    playbillInfo = mCurrentPlaybillInfos.get(i);
                    //SuperLog.debug(TAG,">>>>>>>>>>开始for循环:"+playbillInfo.getChannelName());
                    //刷新数据当前加载成功时的时间
                    playbillInfo.setRefreshDataTime(refreshTime);
                    if (null != mCurrentPlaybillInfos && index < list.size()) {
                        playbillLite = list.get(index).getCurrentPlaybillLite();
                        //刷新节目单名和节目单ID
                        if (null != playbillLite && playbillLite.getChannelID()
                            .equals(playbillInfo.getChannelId())) {
                            //SuperLog.debug(TAG,">>>>>>>>>>更新了:"+playbillLite.getName());
                            playbillInfo.setChannelProgramName(playbillLite.getName());
                            playbillInfo.setPlaybillId(playbillLite.getID());
                            //mJbCurrentChannelPlaybillInfos.add(playbillInfo);
                        }
                    }
                    index = index + 1;
                }
            }
            if (mChannelAdapter.getItemCount() == 0) {
                mChannelAdapter.bindData(mCurrentPlaybillInfos);

                if (mColumnPosition == LiveDataHolder.get().getCulomnSelectPosition()||listSubjectID.size()==1) {

                    selectPos = LiveDataHolder.get().getChannelSelectPosition();
                    List<ChannelDetail> channelPlay = LiveDataHolder.get().getChannelPlay();
                    if (mapSubjectID2Channels != null && mapSubjectID2Channels.size() == 1 &&
                            channelPlay != null && selectPos > channelPlay.size()) {
                        selectPos = 0;
                    }

                    mBTVAndTVODListView.getLayoutManager().scrollToPosition(selectPos);
                    postDelayed(() -> {
                        //第一次加载出来当前频道当前节目单列表
                        mBTVAndTVODListView.itemViewRequestFocus(selectPos);
                    }, DELAY_INVIDATE);
                }
            } else {
                //局部刷新
                mChannelAdapter.notifyItemRangeChanged(mPlaybillOffset, end);
                if (mColumnPosition == LiveDataHolder.get().getCulomnSelectPosition()||listSubjectID.size()==1) {
                    postDelayed(() -> {
                        View itemView = mBTVAndTVODListView.getRecordFocusView();
                        if (null != itemView && !itemView.isFocused()) {
                            itemView.requestFocus();
                        }
                    }, DELAY_INVIDATE);
                }
            }
            mChannelAdapter.setRequestDataStatus(true);
        }
        SuperLog.debug(TAG, "onLoadChannelPlaybillSucc====当前频道当前节目单列表加载成功==end");
    }

    /**
     * 当前频道当前节目单列表加载出错
     */
    @Override
    public void onLoadChannelPlaybillError() {
        mChannelAdapter.setRequestDataStatus(true);
        if (mChannelAdapter.getItemCount() == 0) {
            mChannelNoDataLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 滚动动画:
     * isRight==true:二级列表展开三级列表;
     * isRight==false:二级列表回到一级列表;
     *
     * @param isRight true当前操作往右,布局移动到左边;false当前操作往左,布局移动回来;
     */
    private void scrollToAnim(boolean isRight) {
        int translationX = 0;
        if (isRight) {
            //一级栏目列表宽度+margin值+line的宽度+(columnLine的marginValue-LeftIcon.marginValue),动画要移动的距离
            translationX = DensityUtil.dip2px(getContext(), 190);
        }
        if (!isRight) {
            mLeftImageView.setVisibility(View.INVISIBLE);
            mColumnLine.setVisibility(View.VISIBLE);
        } else {
            mLeftImageView.setVisibility(View.VISIBLE);
            mColumnLine.setVisibility(View.INVISIBLE);
        }
        //fix:微调动画卡顿场景
        //设置当前控件缓存:只保存动画绘图缓存
        mBrowseFrameLayout.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(mBrowseFrameLayout, "scrollX", translationX);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(mBrowseFrameLayout, "scrollY", 0);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(400L);
        animators.playTogether(xTranslate, yTranslate);
        animators.start();
    }


    /**
     * 请求当前频道当前节目单
     *
     * @param postion itemView位置
     */
    public void requestPlaybillContextByTime(int postion) {
        int index;
        if ((postion + 1) % PLAYBILL_COUNT == 0) {
            index = (postion + 1) / PLAYBILL_COUNT;
        } else {
            //63/14 index=5
            index = (postion + 1) / PLAYBILL_COUNT + 1;
        }
        //56 14
        mPlaybillOffset = (index - 1) * PLAYBILL_COUNT;
        Subject mSubject = mColumnAdapter.getItemPosition(mColumnPosition);
        if (mSubject != null && mSubject.getID() != null) {
            List<ChannelDetail> channelDetailList = mapSubjectID2Channels.get(mSubject.getID());
            getCurrentChannelPlaybill(channelDetailList);
        }
    }

    /**
     * 绑定生命周期
     */
    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null == mRxAppCompatActivity ? null : mRxAppCompatActivity.bindToLifecycle();
    }

    // 请求频道下的节目单
    private void getCurrentChannelPlaybill(List<ChannelDetail> channelDetailList) {
        if (channelDetailList != null && channelDetailList.size() > 0) {
            if (mChannelAdapter.getItemCount() == 0) {
                mCurrentPlaybillInfos = LiveDataHolder.get().getCurrentPlaybillInfoList(channelDetailList);
            }
        }

        //请求对应区间的当前频道当前节目单列表
        if (null != mPresenter && channelDetailList != null) {
            mPresenter.queryCurrentChannelPlaybill(mPlaybillOffset, PLAYBILL_COUNT, channelDetailList, getContext());
        } else {
            //没有数据
            mChannelNoDataLayout.setVisibility(View.VISIBLE);
            // 判断RecyclerView绘制完成，停止滚动后再notifyDataSetChanged刷新数据
            if (mBTVAndTVODListView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && !mBTVAndTVODListView.isComputingLayout()){
                mChannelAdapter.clearAll();
            }
        }
    }

    // 根据频道Id和可播放列表 存储弹框落焦位置
    private void getPosition(ChannelDetail channelDetail, int index) {
        // 只有一个栏目时，只显示频道，直接存储频道Index
        if (listSubjectID != null && (listSubjectID.size() == 1 || listSubjectID.size() == 0)) {
            LiveDataHolder.get().setChannelSelectPosition(index);
			LiveDataHolder.get().setCulomnSelectPosition(0);
        } else if (listSubjectID != null && listSubjectID.size() > 1){//栏目大于1个，显示栏目和频道
            if(TextUtils.isEmpty(mCurrentChildSubjectID)) {
                List<String> listChannelSubjectID = channelDetail.getSubjectIDs();
                // listSubjectID：得到栏目Id数组，用于判断根据频道id确定栏目位置
                for (Subject subject : listSubjectID) {
                    if(listChannelSubjectID.contains(subject.getID())){
                        LiveDataHolder.get().setCulomnSelectPosition(listSubjectID.indexOf(subject));
                        mCurrentChildSubjectID = subject.getID();
                        break;
                    }
                }
            }
            else {
                // listSubjectID：得到栏目Id数组，用于判断根据频道id确定栏目位置
                for (Subject subject : listSubjectID) {
                    if (TextUtils.equals(mCurrentChildSubjectID, subject.getID())) {
                        LiveDataHolder.get().setCulomnSelectPosition(listSubjectID.indexOf(subject));
                        break;
                    }
                }
            }

            // 获取相应栏目下的频道集合，根据频道id获取相应栏目下频道集合中的位置
            List<ChannelDetail> listSelectChannelDetail = mapSubjectID2Channels.get(mCurrentChildSubjectID);
            if (listSelectChannelDetail != null && listSelectChannelDetail.size() > 0) {
                for (ChannelDetail cd :  listSelectChannelDetail) {
                    if (channelDetail.getID().equals(cd.getID())) {
                        LiveDataHolder.get().setChannelSelectPosition(listSelectChannelDetail.indexOf(cd));
                        break;
                    }
                }
            }
        }
    }

    public void setSubjectParam(String subjectID, String mIsQuerySubject, String mIsShowChannelNo, String mCurrentChildSubjectID){
        mSubjectId = subjectID;
        this.mIsQuerySubject = mIsQuerySubject;
        this.mIsShowChannelNo = mIsShowChannelNo;
        this.mCurrentChildSubjectID = mCurrentChildSubjectID;
    }
}