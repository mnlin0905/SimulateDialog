package com.knowledge.mnlin.simulatedialog.interfaces;

import android.content.Intent;

/**
 * Created on 2019/10/12  21:53
 * function : page -  interface
 *
 * @author mnlin0905@gmail.com
 */
public interface PageLifeCycle {
    /**
     * Called back in constructor
     */
    void onPageCreate();

    /**
     * page's content-view has set
     */
    void onPageViewInject();

    /**
     * page is visible
     */
    void onPageAttach();

    /**
     * page be blocked
     */
    void onPageDetach();

    /**
     * page is removed from page-stack-record
     */
    void onPageDestroy();

    /**
     * When pages are reused
     *
     * @see android.app.Activity#onNewIntent(Intent)
     */
    void onPageReused();
}
