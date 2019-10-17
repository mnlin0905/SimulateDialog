package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;

import com.knowledge.mnlin.simulatedialog.base.PageParent;

/**
 * Created on 2019/10/17  11:00
 * function : page-self's method,Responsible for communicating with other strongly related objects
 *
 * @author mnlin0905@gmail.com
 */
public interface PageChannel<T extends PageChannel> {
    /**
     * 7y
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    PageParent getPageParent();

    /**
     * @return index in {@link com.knowledge.mnlin.simulatedialog.base.PageStackRecord} ; -1 if not attach to {@link com.knowledge.mnlin.simulatedialog.base.PageManager}
     */
    int getIndexInStackRecord();
}
