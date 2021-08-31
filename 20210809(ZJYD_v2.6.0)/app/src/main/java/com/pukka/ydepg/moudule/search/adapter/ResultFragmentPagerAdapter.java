package com.pukka.ydepg.moudule.search.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pukka.ydepg.moudule.search.SearchResultFragment;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.adapter.ResultFragmentPagerAdapter.java
 * @author:xj
 * @date: 2017-12-19 16:05
 */
public class ResultFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<SearchSubjectBean>          mSubjectBean;
    private Map<String,SearchResultFragment> fragmentMap = new HashMap<>();

    public ResultFragmentPagerAdapter(FragmentManager fm, List<SearchSubjectBean> subjectBeans) {
        super(fm);
        mSubjectBean = subjectBeans;
    }

    @Override
    public Fragment getItem(int position) {
        String typeKey = mSubjectBean.get(position).getId();
        SearchResultFragment searchResultFragment = fragmentMap.get(typeKey);
        if (null == searchResultFragment){
            searchResultFragment = SearchResultFragment.newInstance(mSubjectBean.get(position));
            fragmentMap.put(typeKey,searchResultFragment);
        }
        return searchResultFragment;
    }

    @Override
    public int getCount() {
        return mSubjectBean.size();
    }

    public SearchResultFragment getCurrentSearchResultFragment(int position) {
        return fragmentMap.get(mSubjectBean.get(position).getId());
    }
}