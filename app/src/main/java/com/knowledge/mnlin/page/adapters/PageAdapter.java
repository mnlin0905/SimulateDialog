package com.knowledge.mnlin.page.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.page.interfaces.Page;

import java.util.List;

/**
 * Created on 2019/10/26  14:31
 * function : adapter for pager-layout
 *
 * @author mnlin0905@gmail.com
 */
public class PageAdapter<T extends Page> extends PagerAdapter {
    /**
     * data resource
     */
    private List<T> pages;

    public PageAdapter(List<T> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    /**
     * @return the datas of adapter (provider for invoke)
     */
    public List<T> getDatas(){
        return pages;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = pages.get(position).providerContentView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
