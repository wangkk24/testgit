package com.pukka.ydepg.launcher.ui.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.List;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.TabFragmentAdapter.java
 * @date: 2017-12-15 17:16
 * @version: V1.0 描述当前版本功能
 */
public class PHMFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<Navigate> mNavList;

    ViewGroup container; //外层容器,即使用此Adapter的ViewPager
    String pageId;       //二级页面(ChildLauncherActivity)用,主页不需要

    /**
     * @param fm         FragmentManager
     * @param container  外层容器,即使用此Adapter的ViewPager
     * @param navList    导航栏数据对象List
     * @param pageId     二级页面(ChildLauncherActivity)json文件的pageId,主页不需要
     */
    public PHMFragmentAdapter(FragmentManager fm, ViewGroup container, List<Navigate> navList,String pageId) {
        super(fm);
        this.mNavList  = navList;
        this.container = container;
        this.pageId    = pageId;
    }

    //外部不可使用,要获取ITEM用public Fragment get(int position)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position >= mNavList.size()){
            return null;
        } else {
            PHMFragment fragment;
            //我的页面单独处理
            if (TextUtils.equals(mNavList.get(position).getId(), LauncherService.getInstance().getNavIdMine())) {
                fragment = new MyPHMFragment();
            }else{
                fragment = new PHMFragment();
            }

            fragment.setNavId(mNavList.get(position).getId());
            fragment.setPageIndex(position);
            if (TextUtils.isEmpty(pageId)) {
                //MainActivity的Fragment
                fragment.setMainActivity(OTTApplication.getContext().getMainActivity());
            } else {
                //二级页面的Fragment,同时fragment.isMain=false
                fragment.setPageId(pageId);
            }
            return fragment;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return mNavList == null ? 0 :mNavList.size();
    }

    public PHMFragment get(int position) {
        if (position == -1){
            position = LauncherService.getInstance().getFirstIndexForNormal();
        }
        return (PHMFragment)instantiateItem(container,position);
    }

    public int size() {
        return getCount();
    }

//    private PHMFragment mCurrentFragment;
//
//    public PHMFragment getCurrentFragment() {
//        return mCurrentFragment;
//    }
//
//    @Override
//    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        mCurrentFragment = (PHMFragment) object;
//        super.setPrimaryItem(container, position, object);
//    }
}