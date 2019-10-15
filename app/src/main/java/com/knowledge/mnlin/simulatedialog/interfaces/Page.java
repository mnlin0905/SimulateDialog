package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;

import com.knowledge.mnlin.simulatedialog.base.PageParent;

/**
 * Created on 2019/10/14  11:17
 * function : top-level interface
 *
 * @author mnlin0905@gmail.com
 */
public interface Page extends PageLifeCycle, PageProvider, PageLauncherType, PageKeyWatch,PageAppearance,PageMethodReversal {
    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    PageParent getPageParent();
}
