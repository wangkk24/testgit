package com.pukka.ydepg.moudule.mytv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.Cast;
import com.pukka.ydepg.common.http.v6bean.v6node.CastRole;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.DateUtil;
import com.pukka.ydepg.common.utils.HeartBeatUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.event.PlayUrlEvent;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.adapter.FilterTimeAdapter;
import com.pukka.ydepg.moudule.mytv.adapter.OrderedListAdapter;
import com.pukka.ydepg.moudule.mytv.bean.OrderFilterDataBean;
import com.pukka.ydepg.moudule.mytv.presenter.OrderedPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderedView;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.view.DetailDataView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pukka.ydepg.moudule.mytv.MyOrderActivity.ORDER_REPSONSE;

/**
 * 我的订购
 * Created by hasee on 2017/8/24.
 */

public class OrderedListActivity extends BaseActivity implements OrderedListAdapter.OnItemSelectedListener, FilterTimeAdapter.OnItemSelectListener, OrderedView, OrderedListAdapter.onUnSubscribedListener, DetailDataView {

    private final String TAG = this.getClass().getName();

    private final int UNSUBSCRIBER=1102;

    private final int TRY_SEE_SUBSCRIBE=1103;

    /**
     * 列表
     */
    @BindView(R.id.order_list)
    VerticalGridView mOrderedList;
    /**
     * 请求一次最大数目
     */
    private static final int MAXLENGTH = 30;
    /**
     * 请求订购记录数据
     */
    List<Subscription> mSubscriptionList = new ArrayList<>();
    /**
     * 请求订购适配器
     */
    OrderedListAdapter mOrderedListAdapter;
    /**
     * 列表总数View
     */
    @BindView(R.id.ordered_title_total_count)
    TextView mTotolView;
    /**
     * 选中页签view
     */
    @BindView(R.id.ordered_title_select_number)
    TextView mSelectPositionView;
    /**
     * 总数
     */
    String mTotal;
    /**
     * 筛选弹窗
     */
    private PopupWindow mPopupWindow;


    @BindView(R.id.ordered_title_filter_layout)
    View mFilterView;
    /**
     * 是否加载更多
     */
    private boolean mIsLoadMore;

    private DetailPresenter mDetailPresenter;

    /**
     * 详情图片
     */
    @BindView(R.id.ordered_detail_icon)
    ImageView mSubscriptionImage;
    /**
     * 详情文字1
     */
    @BindView(R.id.ordered_info_type)
    TextView mOrderType;
    /**
     * 详情文字2
     */
    @BindView(R.id.ordered_info_level)
    TextView mOrderIntroduce;
    /**
     * 详情文字3
     */
    @BindView(R.id.ordered_info_director)
    TextView mOrderDirector;
    /**
     * 详情文字4
     */
    @BindView(R.id.ordered_info_performer)
    TextView mOrderPerformer;
    /**
     * 详情view
     */
    @BindView(R.id.ordered_detail_layout)
    View mOrderDetailLayout;
    /**
     * 无数据
     */
    @BindView(R.id.no_data_layout)
    View mNoDataLayout;
    /**
     * 筛选类型
     */
    @BindView(R.id.mytv_filter_result_type)
    TextView mFilterResultType;
    /**
     * 筛选时间
     */
    @BindView(R.id.mytv_filter_result_time)
    TextView mFilterResultTime;
    /**
     * 过期时间展示
     */
    @BindView(R.id.ordered_info_validity)
    TextView mTime;
    /**
     * 上次按下或者按上时间
     */
    private long mLastTime;

    private static final long FOURTY_EIGHT_HOURS = 172800000;//48小时
    private static final long SEVEN_DAY = 604800000;//7天
    private static final long TWO_HOURS = 7200000;//2小时
    private static final long ONE_DAY = 86400000;//7天
    private static final long ONE_HOURS = 3600000;//1小时
    /**
     * 请求控制器
     */
//    SelfAppInfoController mSelfAppInfoController;
    OrderedPresenter mOrderedPresenter;
    /**
     * 请求body
     */
    DSVQuerySubscription request;


    private VODDetail mVoddetail;

    private boolean isFilter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNSUBSCRIBER:
                    if(null!=unSubscribeDialog&&unSubscribeDialog.isShowing()){
                        unSubscribeDialog.dismiss();
                    }
                    mOrderedPresenter.queryProductInfo(request, MAXLENGTH);
                    showUnSubscribeDialog(1, null, null);
                    break;
                case TRY_SEE_SUBSCRIBE:
                    mOrderedPresenter.queryProductInfo(request, MAXLENGTH);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered);
        setmUnBinder(ButterKnife.bind(this));
        initView();
        mOrderedPresenter = new OrderedPresenter(this);
        mDetailPresenter = new DetailPresenter(this);
        mDetailPresenter.setDetailDataView(this);
        mOrderedPresenter.setDataView(this);
        request = new DSVQuerySubscription();
//        mSelfAppInfoController = new SelfAppInfoController(this,this);
        isFilter = false;
        initIntentDate();
        initData();
    }

    private void initIntentDate() {
      String reponseStr=  getIntent().getStringExtra(ORDER_REPSONSE);
      if(!TextUtils.isEmpty(reponseStr)){
          QueryProductInfoResponse response= JsonParse.json2Object(reponseStr,QueryProductInfoResponse.class);
          if(null!=response&&null!=response.getProducts()&&response.getProducts().size()>0){
              mIsLoadMore=false;
              getDateAction(response);
          }

      }
    }

    /**
     * 数据加载 加载跟多从列表下一个开始 否则从0开始
     */
    private void initData() {
        mOrderedPresenter.cancelProductInfo();
        int offset;
        if (mIsLoadMore) {
            offset = mSubscriptionList.size();
        } else {
            offset = 0;
        }
        request.setOffset(offset + "");
        mOrderedPresenter.queryProductInfo(request, MAXLENGTH);
    }

    private void initView() {
        mOrderedList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mOrderedList.getChildCount() > 0) {
                        mOrderedList.getChildAt(0).requestFocus();
                    }
                }
            }
        });
        mOrderedListAdapter = new OrderedListAdapter(mOrderedList, mSubscriptionList, this);
        mOrderedListAdapter.setOnItemSelectedListener(this);
        mOrderedList.setAdapter(mOrderedListAdapter);
        mOrderedListAdapter.setUnSubscribedListener(this);
        mOrderedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                if ((lastChildBottom - recyclerBottom) < lastChildView.getHeight() && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    SuperLog.debug("OrderedListActivity", "scrolled to last item!");
                    if (mSubscriptionList.size() < Integer.parseInt(mTotal)) {
                        mIsLoadMore = true;
                        isFilter = false;
                        initData();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mOrderDetailLayout.setVisibility(View.GONE);
    }

    @Override
    public void queryProductInfoSuccess(QueryProductInfoResponse response) {
        getDateAction(response);

    }

    public void getDateAction(QueryProductInfoResponse response){
        mNoDataLayout.setVisibility(View.GONE);
        if (null != response) {
            mTotal = response.getTotal();
            if (!TextUtils.isEmpty(mTotal)) {
                if (Integer.parseInt(mTotal) > 0) {
                    mTotolView.setText(String.format(getResources().getString(R.string.ordered_list_total_count), mTotal));
                } else {
                    mTotolView.setText("");
                }
            }
            if (!mIsLoadMore) {
                mOrderedListAdapter.clearData();

            }
            if (isFilter && !mIsLoadMore) {
                mOrderedListAdapter.setSelectPostion(0);
            }
            List<Subscription> subscriptionList = response.getProducts();
            if (null != subscriptionList && !subscriptionList.isEmpty()) {
                mOrderedListAdapter.addData(subscriptionList);
                if (isFilter && !mIsLoadMore) {
                    mOrderedList.getLayoutManager().scrollToPosition(0);
                    View lastView = mOrderedList.getLayoutManager().findViewByPosition(0);
                    if (null == lastView) {
                        if (null != mOrderedList.findViewHolderForLayoutPosition(0)) {
                            lastView = mOrderedList.findViewHolderForLayoutPosition(0).itemView;
                        }
                    }
                    if (null != lastView) {
                        lastView.setFocusable(true);
                        lastView.requestFocus();
                    }
                }

            } else {
                //TODO no data
                if (!mIsLoadMore) {
                    mSelectPositionView.setText("");
                    mOrderedListAdapter.clearData();
                    mOrderDetailLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }

            }
        }



    }


    @Override
    public void queryProductInfoFail() {
        if (!mIsLoadMore) {
            mSelectPositionView.setText("");
            mOrderedListAdapter.clearData();
            mOrderDetailLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 列表选中回调
     *
     * @param subscription
     * @param position
     */
    @SuppressLint("CheckResult")
    @Override
    public void onItemSelected(Subscription subscription, int position) {
        SuperLog.debug("OrderedListActivity", "onItemSelected");
        if (mSubscriptionList.size() > 0) {
            mSelectPositionView.setText(String.valueOf(position + 1));
        }
        mTime.setVisibility(View.GONE);
        if (null != subscription) {
            String endTime = subscription.getEndTime();
            if (!TextUtils.isEmpty(endTime)) {
                long end = Long.parseLong(endTime);
                showTime(end);
            }
            mOrderDetailLayout.setVisibility(View.VISIBLE);
            Picture picture = subscription.getPicture();
            List<String> posterList = picture.getPosters();
            RequestOptions options  = new RequestOptions()
                    .placeholder(R.drawable.default_product);

            if (null != posterList && posterList.size() > 0) {
                Glide.with(this).load(posterList.get(0)).apply(options).into(mSubscriptionImage);
            } else {
                mSubscriptionImage.setImageResource(R.drawable.default_product);
            }
            if (TextUtils.equals(subscription.getProductType(), Subscription.ProductType.BY_TIMES)) {//单次
                PriceObjectDetail priceObjectDetail = subscription.getPriceObjectDetail();
                if (null != priceObjectDetail) {
                    String type = priceObjectDetail.getType();
                    String contentType = priceObjectDetail.getContentType();
                    if (TextUtils.equals(type, PriceObjectDetail.Type.CONTENT)) {
                        if (TextUtils.equals(contentType, PriceObjectDetail.ContentType.VOD)) {
                            VOD vod = priceObjectDetail.getVOD();
                            if (null != vod) {
                                String name = vod.getName();
                                String introduce = vod.getIntroduce();
                                Picture picture1 = vod.getPicture();
                                if (null != picture1) {
                                    List<String> posterList1 = picture1.getPosters();
                                    if (null != posterList1 && posterList1.size() > 0) {
                                        options.placeholder(R.drawable.default_product);
                                        Glide.with(this).load(posterList1.get(0)).apply(options).into(mSubscriptionImage);
                                    } else {
                                        mSubscriptionImage.setImageResource(R.drawable.default_product);
                                    }
                                }
                                String actors = "";
                                String directors = "";
                                List<CastRole> castRoleList = vod.getCastRoles();
                                StringBuffer actor = new StringBuffer();
                                StringBuffer director = new StringBuffer();
                                if (null != castRoleList) {
                                    for (CastRole castRole : castRoleList) {
                                        if (TextUtils.equals(castRole.getRoleType(), CastRole.RoleType.ACTOR)) {
                                            List<Cast> castList = castRole.getCasts();
                                            for (Cast cast : castList) {
                                                if (TextUtils.isEmpty(actor)) {
                                                    actor.append(cast.getCastName());
                                                } else {
                                                    actor.append(",").append(cast.getCastName());
                                                }
                                            }

                                        }
                                        if (TextUtils.equals(castRole.getRoleType(), CastRole.RoleType.DIRECTOR)) {
                                            List<Cast> castList = castRole.getCasts();
                                            for (Cast cast : castList) {
                                                if (TextUtils.isEmpty(director)) {
                                                    director.append(cast.getCastName());
                                                } else {
                                                    director.append(",").append(cast.getCastName());
                                                }
                                            }

                                        }
                                    }
                                    if (TextUtils.isEmpty(actor)) {
                                        actors = getResources().getString(R.string.mytv_order_unknown);
                                    } else {
                                        actors = actor.toString();
                                    }
                                    if (TextUtils.isEmpty(director)) {
                                        directors = getResources().getString(R.string.mytv_order_unknown);
                                    } else {
                                        directors = director.toString();
                                    }
                                }
                                setStyleText(mOrderType, getString(R.string.mytv_order_name) + name);
                                if (!TextUtils.isEmpty(introduce)) {
                                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                                    Matcher m = p.matcher(introduce);
                                    introduce = m.replaceAll("");
                                }
                                setStyleText(mOrderIntroduce, getString(R.string.mytv_order_introduce) + introduce);
                                if (!TextUtils.isEmpty(directors) && !getResources().getString(R.string.mytv_order_unknown).equals(directors)) {
                                    mOrderDirector.setVisibility(View.VISIBLE);
                                    setStyleText(mOrderDirector, getString(R.string.mytv_order_director) + directors);
                                } else {
                                    mOrderDirector.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(actors) && !getResources().getString(R.string.mytv_order_unknown).equals(actors)) {
                                    mOrderPerformer.setVisibility(View.VISIBLE);
                                    setStyleText(mOrderPerformer, getString(R.string.mytv_order_actor) + actors);
                                } else {
                                    mOrderPerformer.setVisibility(View.GONE);
                                }
                            }

                        }
                    }
                }

            } else {//周期
                mOrderType.setVisibility(View.VISIBLE);
                mOrderIntroduce.setVisibility(View.VISIBLE);
                mOrderDirector.setVisibility(View.GONE);
                mOrderPerformer.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(subscription.getProductName())) {
                    setStyleText(mOrderType, getResources().getString(R.string.mytv_order_name) + subscription.getProductName());
                } else {
                    mOrderType.setText("");
                }
                if (!TextUtils.isEmpty(subscription.getProductDesc())) {
                    setStyleText(mOrderIntroduce, getResources().getString(R.string.mytv_order_introduce) + subscription.getProductDesc());
                    //mIntroduce.setText("");
                }

            }
        }
    }

    /**
     * 文字颜色 类型：颜色变化
     *
     * @param tv
     * @param text
     */
    public void setStyleText(TextView tv, String text) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.c23_color)), 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU://KeyEvent.KEYCODE_DPAD_MENU:
                showFilterPopWindow();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(mSubscriptionList.size()==0) {
                    finish();
                }
                break;

        }
        //限定长按只能够0.5秒触发一次
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
            case KeyEvent.ACTION_UP:
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (System.currentTimeMillis() - mLastTime > 500) {
                        mLastTime = System.currentTimeMillis();
                    } else {
                        return true;
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 展示筛选列表
     */
    private void showFilterPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.mytv_dialog_filter_layout, null);
        VerticalGridView vgTimeList = (VerticalGridView) view.findViewById(R.id.my_tv_dialog_time);
        VerticalGridView vgTypeList = (VerticalGridView) view.findViewById(R.id.my_tv_dialog_type);
        List<OrderFilterDataBean> timeList = getTimeList();
        List<OrderFilterDataBean> typeList = getTypeList();
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        FilterTimeAdapter timeAdapter = new FilterTimeAdapter(timeList, vgTimeList, this);
        FilterTimeAdapter typeAdapter = new FilterTimeAdapter(typeList, vgTypeList, this);
        timeAdapter.setOnItemSelectListener(this);
        typeAdapter.setOnItemSelectListener(this);
        vgTimeList.setAdapter(timeAdapter);
        vgTypeList.setAdapter(typeAdapter);

        mPopupWindow.showAtLocation(mFilterView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获取筛选时间列表
     *
     * @return
     */
    private List<OrderFilterDataBean> getTimeList() {
        List<OrderFilterDataBean> timeList = new ArrayList<>();
        OrderFilterDataBean orderBean = new OrderFilterDataBean();
        orderBean.setmType(OrderFilterDataBean.FILTERTYPE.TIME);
        orderBean.setName(getResources().getString(R.string.mytv_can_use));
        timeList.add(orderBean);
        for (int i = 0; i < 6; i++) {
            OrderFilterDataBean orderMonth = new OrderFilterDataBean();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            orderMonth.setFromDateTime(DateUtil.getFirstDayOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
            orderMonth.setToDateTime(DateUtil.getLastDayOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1) + "");
            orderMonth.setmType(OrderFilterDataBean.FILTERTYPE.TIME);
            String month = "";
            int mon = calendar.get(Calendar.MONTH) + 1;
            SuperLog.debug("getTimeList", mon + "     " + month + "    " + i);
            if (mon >= 10) {
                month = mon + "";
            } else {
                month = "0" + mon;
            }
            orderMonth.setName(calendar.get(Calendar.YEAR) + "/" + month);
            timeList.add(orderMonth);
        }
        return timeList;
    }

    /**
     * 获取筛选类型列表
     *
     * @return
     */
    public List<OrderFilterDataBean> getTypeList() {
        List<OrderFilterDataBean> typeList = new ArrayList<>();
        OrderFilterDataBean orderBean = new OrderFilterDataBean();
        orderBean.setmType(OrderFilterDataBean.FILTERTYPE.TYPE);
        orderBean.setName(getResources().getString(R.string.mytv_all));
        typeList.add(orderBean);
        OrderFilterDataBean orderOne = new OrderFilterDataBean();
        orderOne.setmType(OrderFilterDataBean.FILTERTYPE.TYPE);
        orderOne.setProductType("1");
        orderOne.setName(getResources().getString(R.string.mytv_one));
        typeList.add(orderOne);
        OrderFilterDataBean orderMonth = new OrderFilterDataBean();
        orderMonth.setmType(OrderFilterDataBean.FILTERTYPE.TYPE);
        orderMonth.setName(getResources().getString(R.string.mytv_month));
        orderMonth.setProductType("0");
        typeList.add(orderMonth);
        return typeList;
    }

    /**
     * 类型选中请求数据
     *
     * @param bean
     */
    @Override
    public void onItemSelected(OrderFilterDataBean bean) {
        mIsLoadMore = false;
        isFilter = true;
        SuperLog.debug(OrderedListActivity.class.getName(), bean.getFromDateTime() + "   " + bean.getProductType() + "  " + bean.getToDateTime());
        if (bean.getmType() == OrderFilterDataBean.FILTERTYPE.TYPE) {
            mFilterResultType.setText(bean.getName());
            if (TextUtils.equals(bean.getProductType(), request.getProductType())) {
                return;
            } else {
                request.setProductType(bean.getProductType());
            }
        } else {
            mFilterResultTime.setText(bean.getName());
            if (TextUtils.equals(bean.getFromDateTime(), request.getFromDate()) && TextUtils.equals(bean.getToDateTime(), request.getToDate())) {
                return;
            } else {
                request.setFromDate(bean.getFromDateTime());
                request.setToDate(bean.getToDateTime());
            }
        }
        SuperLog.debug(OrderedListActivity.class.getName(), request.getFromDate() + "   " + request.getProductType() + "  " + request.getToDate());

        initData();
    }

    /**
     * 显示过期时间
     */
    private void showTime(long endTime) {
        mTime.setVisibility(View.GONE);
        Date date = new Date();
        long nowTime = date.getTime();
        long interval = endTime - nowTime;
        if (interval > 0) {//没有过期
            if (FOURTY_EIGHT_HOURS <= interval && interval <= SEVEN_DAY) {
                mTime.setVisibility(View.VISIBLE);
                double countDay = Math.ceil(interval / (double)ONE_DAY);
                mTime.setText(String.format(getResources().getString(R.string.order_day_time), countDay + ""));

            } else if (TWO_HOURS < interval && interval <= FOURTY_EIGHT_HOURS) {
                mTime.setVisibility(View.VISIBLE);
                int countHour = (int) Math.ceil(interval / (double)ONE_HOURS);
                mTime.setText(String.format(getResources().getString(R.string.order_time), countHour + ""));
            }
        } else {//已经过期
            mTime.setVisibility(View.GONE);
        }
    }


    @Override
    public void onUnSubscribed(String productId, String cancelType) {
        showUnSubscribeDialog(0, productId, cancelType);
    }

    @Override
    public void onPlayVod(VOD vod) {
        if (null != vod) {
            mDetailPresenter.getVODDetail(vod.getID());
        }

    }

    public void cacelSubscribed(String productId, String cancelType) {
        if (null != mOrderedPresenter) {
            SuperLog.debug(TAG, "cancelType=" + cancelType + "|productId=" + productId);
            CancelSubscribeRequest.CancelSubscibe cancelSubscribe = new CancelSubscribeRequest.CancelSubscibe(productId, Integer.parseInt(cancelType));
            CancelSubscribeRequest cancelSubscribeRequest = new CancelSubscribeRequest(cancelSubscribe);
            mOrderedPresenter.unsuscribe(cancelSubscribeRequest);
        }
    }

    @Override
    public void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse) {
        showUnSubscribeDialog(2, null, null);
        mHandler.sendEmptyMessageDelayed(UNSUBSCRIBER, 5000);
    }

    @Override
    public void unsubscribeFail() {
        EpgToast.showToast(OrderedListActivity.this, "退订失败");
    }


    PopupWindow unSubscribeDialog = null;

    private void showUnSubscribeDialog(final int showType, final String productId, final String
            cancelType) {
        SuperLog.debug(TAG, "showType=" + showType + "|productId=" + productId + "|cancelType=" +
                cancelType);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_unsubscirber, null);
        TextView ok = (TextView) view.findViewById(R.id.unSubscriber_dialog_sure);
        TextView cancel = (TextView) view.findViewById(R.id.unSubscriber_dialog_cancel);
        unSubscribeDialog = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        unSubscribeDialog.setOutsideTouchable(true);
        unSubscribeDialog.setBackgroundDrawable(new ColorDrawable());
        ok.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ok.setTextColor(b ? getResources().getColor(R.color.white_0) : getResources().getColor(R.color.c21_color));
            }
        });
        cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                cancel.setTextColor(b ? getResources().getColor(R.color.white_0) : getResources().getColor(R.color.c21_color));
            }
        });
        if (showType == 1) {
            TextView popTitleView = (TextView) view.findViewById(R.id.unSubscriber_dialog_title);
            popTitleView.setText("退订成功");
            TextView popInfoView = (TextView) view.findViewById(R.id.pop_detail);
            popInfoView.setText("下月生效，观看权益保留到月底。");
            ok.setText("知道了");
            ok.requestFocus();
            cancel.setVisibility(View.GONE);
        } else if (showType == 2) {
            TextView popInfoView = (TextView) view.findViewById(R.id.pop_detail);
            popInfoView.setText("退订处理中");
            ok.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        } else {
            ok.requestFocus();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unSubscribeDialog.dismiss();
                }
            });
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSubscribeDialog.dismiss();
                if (showType == 0) {
                    cacelSubscribed(productId, cancelType);
                }
            }
        });
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.orderlist_root_layout);
        unSubscribeDialog.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showNoContent() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showDetail(VODDetail vodDetail, String actionId, List<RecmContents> recmContentsList) {
        if (null != mDetailPresenter) {
            mVoddetail = vodDetail;
            mDetailPresenter.setVODDetail(vodDetail);
            playProduct(vodDetail);
        }

    }

    public void playProduct(VODDetail detail) {
        mDetailPresenter.setButtonOrderOrSee(true);
        if (VodUtil.isMiguVod(detail)) {
//            EpgToast.showToast(this, "请到咪咕爱看app进行观看!");
            RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.orderlist_root_layout);
            MiguQRViewPopWindow popWindow  = new MiguQRViewPopWindow(this,detail.getCode(),MiguQRViewPopWindow.mSearchResultType);
            popWindow.showPopupWindow(rootLayout);
            return;
        }
        String vodType = detail.getVODType();
        if (vodType.equals("0")) {
            List<VODMediaFile> vodMediaFiles = detail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                mDetailPresenter.playVOD(detail);
            } else {
                if (!HeartBeatUtil.getInstance().isSubscribedByPrice(detail,null==mVoddetail?"":mVoddetail.getPrice())) {
                    EpgToast.showToast(OrderedListActivity.this, "没有找到资源文件！");
                } else {
                    EpgToast.showToast(OrderedListActivity.this, "播放失败！");
                }
                return;
            }
        } else {
            List<Episode> episodes = detail.getEpisodes();
            Bookmark bookmark = detail.getBookmark();
            if (bookmark != null) {
                SuperLog.debug(TAG, "set bookmark--->" + "videoId:" + bookmark.getItemID() + ",sitcomNO:" + bookmark.getSitcomNO() + ",breakpoint:" + bookmark.getRangeTime());
            }
            if (episodes != null && episodes.size() != 0) {
                Episode playEpisode = null;
                if (bookmark != null && !TextUtils.isEmpty(bookmark.getSitcomNO())) {
                    for (Episode episode : episodes) {
                        if (bookmark.getSitcomNO().equals(episode.getSitcomNO())) {
                            playEpisode = episode;
                        }
                    }
                } else {
                    playEpisode = episodes.get(0);
                }
                if (null != playEpisode){
                    mDetailPresenter.playVOD(playEpisode);
                }
            } else {
                EpgToast.showToast(OrderedListActivity.this, "没有可播放的子集！");
                return;
            }
        }
    }

    @Override
    public void showCollection(boolean isCollection) { }

    @Override
    public void setNewScore(List<Float> newScore) { }

    @Override
    public void showContentNotExit() { }

    //订购完成后，认证播放
    @Subscribe
    public void onEvent(PlayUrlEvent event) {
        if (null != event && (event.isVODSubscribe() || event.isTrySeeSubscribe()) && null != mVoddetail) {
            playProduct(mVoddetail);
        }
    }
}