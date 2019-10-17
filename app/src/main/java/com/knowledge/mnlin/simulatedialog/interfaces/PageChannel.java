package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;

import com.knowledge.mnlin.simulatedialog.base.PageParent;

/**
 * Created on 2019/10/17  11:00
 * function : page-self's method
 *
 * @author mnlin0905@gmail.com
 */
public interface PageI {
    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    PageParent getPageParent();

    void    
}
