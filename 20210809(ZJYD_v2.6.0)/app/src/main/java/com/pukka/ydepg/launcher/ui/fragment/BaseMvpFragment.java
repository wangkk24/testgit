package com.pukka.ydepg.launcher.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.moudule.base.BaseFragment;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * mvp架构的baseFragment
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.fragment.BaseMvpFragment.java
 * @date: 2017-12-17 8:57
 * @version: V1.0 由于Viewpager的机制，BufferKnife用unBind会导致异常，而不用则会内存泄漏。所以这里不集成BufferKnife
 * 无法使用Databinding的项目可以使用 一些自动生成findbyid的插件，比如FindViewByMe
 */
public abstract class BaseMvpFragment<T extends IBaseContact.IBasePresenter> extends BaseFragment implements IBaseContact.IBaseView {
    protected T presenter;
    protected Activity activity;
    private View rootView;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            activity = getActivity();
            rootView = inflater.inflate(attachLayoutRes(), null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    protected abstract void initView(View view);

    /**
     * fragmentpagerAdapter中 页面移出缓存后不会调用ondestroy，所以在这个生命周期释放rxjava资源
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化 Presenter对象
     */
    protected abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
        unbinder.unbind();
    }
}