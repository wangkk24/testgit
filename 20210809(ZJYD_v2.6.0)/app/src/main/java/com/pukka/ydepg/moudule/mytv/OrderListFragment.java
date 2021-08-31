package com.pukka.ydepg.moudule.mytv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.moudule.mytv.adapter.OrderAdapter;
import com.pukka.ydepg.moudule.mytv.presenter.OrderedPresenter;
import com.pukka.ydepg.moudule.mytv.presenter.view.OrderedView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudo on 2018/4/19.
 */

public class OrderListFragment extends Fragment implements OrderedView {
    /**
     * 查询书签控制器
     */
    OrderedPresenter mPresenter;
    RecyclerView mRecyclerView;
    List<Subscription> mSubscriptionList = new ArrayList<>();
    private static final int pagerSize = 30;
    private OrderAdapter OrderAdapter;
    View mNoData;
    /**
     * 请求body
     */
    DSVQuerySubscription request;

    private QueryProductInfoResponse mQueryProductInfoResponse;

    public QueryProductInfoResponse getmQueryProductInfoResponse() {
        return mQueryProductInfoResponse;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new OrderedPresenter((RxAppCompatActivity) getActivity());
        request = new DSVQuerySubscription();
        request.setOffset("0");
        mPresenter.setDataView(this);
        initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    private void initData() {
        mPresenter.queryProductInfo(request,pagerSize);
    }

    private void initView(View view) {
        mNoData = view.findViewById(R.id.no_data);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mytv_history_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(OrderAdapter = new OrderAdapter(mSubscriptionList,getContext(),mPresenter));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscriptionList.clear();
        mQueryProductInfoResponse=null;
        OrderAdapter.notifyDataSetChanged();
        mNoData.setVisibility(View.GONE);
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);
    }


    @Override
    public void queryProductInfoSuccess(QueryProductInfoResponse response) {
        if(null!=response) {
            mQueryProductInfoResponse=response;
            mSubscriptionList.clear();
            mNoData.setVisibility(View.GONE);
            if (null != response.getProducts() && ! response.getProducts().isEmpty()) {
                mSubscriptionList.addAll(response.getProducts());
            } else {
                mNoData.setVisibility(View.VISIBLE);
            }
            OrderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void queryProductInfoFail() {
        mSubscriptionList.clear();
        OrderAdapter.notifyDataSetChanged();
        mNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void unsubscribeSuccess(SubscribeDeleteResponse subscribeDeleteResponse) {

    }

    @Override
    public void unsubscribeFail() {

    }
}
