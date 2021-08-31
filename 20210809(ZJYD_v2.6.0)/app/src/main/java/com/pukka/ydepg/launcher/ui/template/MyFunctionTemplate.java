package com.pukka.ydepg.launcher.ui.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的页面 常用功能模块化
 * 支持配置动态数据
 *
 * @FileName: com.pukka.ydepg.launcher.ui.template.MyFunctionTemplate.java
 * @author: luwm
 * @data: 2018-06-22 15:03
 * @Version V1.0 <描述当前版本功能>
 */
public class MyFunctionTemplate extends PHMTemplate {
    private static final String TAG = "MyFunctionTemplate";
    private RecyclerViewTV recyclerViewTV;
    private List<VOD> vods = new ArrayList<>();
    private int realIndex = 0;//资源位对应的真实VOD索引
    private static final String QUERY_COUNT_LIMIT = "5"; //查询数量
    private static final String QUERY_OFFSET_LIMIT = "0"; //查询开始位置

    TextViewExt accountName;
    TextViewExt mUserNameTv;
    TextViewExt mProductEndTv;
    TextViewExt mDeviceVersionTv;
    TextViewExt mStbIdTv;
    TextViewExt mVersinName;
    private RelativeLayout userInfoContainer;
    private RelativeLayout userInfoAvatarLayout;
    private RelativeLayout userInfoTextLayout;

    public MyFunctionTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
    }

    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        accountName = (TextViewExt) findViewById(R.id.tv_my_function_busness_account);
        mUserNameTv = (TextViewExt) findViewById(R.id.tv_userName);
        mProductEndTv = (TextViewExt) findViewById(R.id.tv_productEnd);
        mDeviceVersionTv = (TextViewExt) findViewById(R.id.tv_deviceVersion);
        mStbIdTv = (TextViewExt) findViewById(R.id.tv_stbId);
        mVersinName = (TextViewExt) findViewById(R.id.tv_versionName);
        userInfoContainer = (RelativeLayout) findViewById(R.id.rl_userinfo_container);
        userInfoAvatarLayout = (RelativeLayout) findViewById(R.id.rl_userinfo_avatar_layout);
        userInfoTextLayout = (RelativeLayout) findViewById(R.id.rl_userinfo_text_layout);
        initClickListener();
        showAccount();

        recyclerViewTV = (RecyclerViewTV) findViewById(R.id.rv_my_fragment_function);
        recyclerViewTV.setLayoutManager(new RvLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false,recyclerViewTV));
        recyclerViewTV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getLayoutManager().getPosition(view);
                if (position > 0) {
                    outRect.left = (int) getResources().getDimension(R.dimen.function_item_margin_16);
                }
            }
        });
        if (!((MyPHMFragment) fragment).isShowUserDetail()) {
            userInfoAvatarLayout.setVisibility(VISIBLE);
            userInfoTextLayout.setVisibility(GONE);
        } else {
            userInfoAvatarLayout.setVisibility(GONE);
            userInfoTextLayout.setVisibility(VISIBLE);
        }
    }

    private void initClickListener() {
        userInfoContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.VISIBLE == userInfoAvatarLayout.getVisibility()) {
                    ((MyPHMFragment) fragment).setShowUserDetail(true);
                    userInfoTextLayout.setVisibility(VISIBLE);
                    userInfoAvatarLayout.setVisibility(GONE);
                } else {
                    ((MyPHMFragment) fragment).setShowUserDetail(false);
                    userInfoAvatarLayout.setVisibility(VISIBLE);
                    userInfoTextLayout.setVisibility(GONE);
                }
            }
        });
    }

    /**
     * 展示业务账号
     */
    public void showAccount() {
        String billID = SessionService.getInstance().getSession().getAccountName();
        if (!TextUtils.isEmpty(billID)) {
            if (billID.contains("null")) {
                billID = "";
            }
        } else {
            billID = "";
        }
        accountName.setText(appendLabel(context.getString(R.string.my_account_name), billID));
    }

    @Override
    public View getFirstView() {
        return userInfoContainer;
    }

    public void setDatas(Group group,List<Element> elementList, SubjectVodsList vodList) {
        initTextViewData();
        querySubscription();
        if (null != vodList) {
            this.vods = vodList.getVodList();
        }
        CommonAdapter adapter = new CommonAdapter<Element>(context, R.layout.item_my_function, elementList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                relativeLayout.setRadius(0.0f);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                relativeLayout.setGroup(group);
                if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                    relativeLayout.setDefaultData(true);
                    relativeLayout.setElementData(element);
                } else {
                    relativeLayout.setDefaultData(false);
                    if (null != vods && vods.size() > realIndex) {
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(realIndex));
                        realIndex++;
                    }
                }
            }
        };
        recyclerViewTV.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    public void querySubscription() {
        if (SessionService.getInstance().getSession().getProfile() == null) {
            return;
        }
        DSVQuerySubscription request = new DSVQuerySubscription();
        request.setProfileID(SessionService.getInstance().getSession().getProfile().getID());
        request.setCount(QUERY_COUNT_LIMIT);
        request.setOffset(QUERY_OFFSET_LIMIT);
        request.setProductType("0");
        request.setIsMain("1");
        String interfaceName = HttpConstant.QUERY_SUBSCRIPTION;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + interfaceName;
        HttpApi.getInstance().getService().querySubscription(url, request).compose(fragment.getPresenter().onCompose(fragment.bindToLife())).subscribe(queryProductInfoResponse -> {
            String retCode = queryProductInfoResponse.getResult().getRetCode();
            if (TextUtils.equals(retCode, Result.RETCODE_OK)) {
                List<Subscription> orderList = queryProductInfoResponse.getProducts();
                List<Subscription> overOneYearOrderList = new ArrayList<Subscription>();
                Long nowTime = new Date().getTime();
                for (Subscription sub : orderList) {
                    String charge = sub.getChargeMode();
                    Long endTime = Long.parseLong(sub.getEndTime());
                    Long startTime = Long.parseLong(sub.getStartTime());
                    //展示包年charge = 21套餐
                    if ("21".equalsIgnoreCase(charge)) {
                        overOneYearOrderList.add(sub);
                    }
                    if (overOneYearOrderList.size() > 0) {
                        for (Subscription s : overOneYearOrderList) {
                            if (endTime > nowTime && startTime < nowTime) {
                                long time = Long.parseLong(overOneYearOrderList.get(0).getEndTime());
                                String date = DateCalendarUtils.formatDate(time, "yyyy-MM-dd");
                                loadSubscriptionData(date);
                                break;
                            }
                        }
                    }
                }
            } else {
                fragment.getPresenter().handleErrorIncludeTimeOut(retCode, interfaceName, context);

            }
        }, throwable -> {
            SuperLog.debug(TAG, throwable.getLocalizedMessage());
        });
    }

    public void loadSubscriptionData(String time) {
        if (!TextUtils.isEmpty(time) && !BuildConfig.DEBUG_LOG) {
            mProductEndTv.setText(String.format("%s%s", context.getString(R.string.my_product_end_time), time));
            mProductEndTv.setVisibility(View.VISIBLE);
            mVersinName.setVisibility(View.GONE);
        } else {
            mProductEndTv.setVisibility(View.GONE);
        }
    }

    private void initTextViewData() {
        UserInfo userInfo = AuthenticateManager.getInstance().getUserInfo();
        if (userInfo != null) {
            mUserNameTv.setText(userInfo.getUserId());
        }
        mProductEndTv.setText("");
        mProductEndTv.setVisibility(GONE);
        mDeviceVersionTv.setText(appendLabel(context.getString(R.string.my_device_no), CommonUtil.getDeviceType()));
        mStbIdTv.setText(appendLabel(context.getString(R.string.my_stb_id), CommonUtil.getSTBID()));
        mVersinName.setText(appendLabel(context.getString(R.string.my_version_name), CommonUtil.getVersionName()));
    }

    private String appendLabel(String tag, String value) {
        return String.format("%s%s", tag, value);
    }

    public void scrollToTop() {
        recyclerViewTV.getLayoutManager().scrollToPosition(0);
    }
}