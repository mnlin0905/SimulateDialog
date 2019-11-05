package com.knowledge.mnlin.page.interfaces;

import android.annotation.NonNull;

import com.knowledge.mnlin.page.core.PageContext;
import com.knowledge.mnlin.page.core.PageParent;

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
    PageContext getPageContext();

    /**
     * @return index in {@link com.knowledge.mnlin.page.core.PageStackRecord} ; -1 if not attach to {@link com.knowledge.mnlin.page.core.PageManager}
     */
    int getIndexInStackRecord();
}
