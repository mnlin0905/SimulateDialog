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
     * page's status
     */
    int PAGE_GRADATION_IDEL = 0x00000000;
    int PAGE_GRADATION_PAGE_CREATE = 0x00000001;
    int PAGE_GRADATION_VIEW_INJECT = 0x00000010;
    int PAGE_GRADATION_ATTACH_TO_PARENT = 0x00000100;
    int PAGE_GRADATION_ACTIVE = 0x00001000;
    int PAGE_GRADATION_DEACTIVE = 0x00010000;
    int PAGE_GRADATION_DETACH_FROM_PARENT = 0x00100000;

    /**
     * Called back in constructor
     */
    void onPageCreate();

    /**
     * page's content-view has set
     */
    void onPageViewInject();

    /**
     * page added to parent
     * <p>
     * May be called more than once
     *
     * @hide
     */
    void onPageAttachToParent();

    /**
     * page visible and interactive
     */
    void onPageActive();

    /**
     * Before the interface is invisible, it has entered the visible state again.
     * <p>
     * called after {@link PageLifeCycle#onPageActive()}
     */
    void onPageReResume();

    /**
     * The page is not visible or partially visible and cannot interact with the user
     */
    void onPageDeactive();

    /**
     * page removed from parent
     * <p>
     * May be called more than once
     *
     * @hide
     */
    void onPageDetachFromParent();

    /**
     * When pages are reused
     * <p>
     * It's not a lifecycle, it's just a reuse caused by the launcher pattern.
     *
     * @see android.app.Activity#onNewIntent(Intent)
     */
    void onPageReused();

    /**
     * page current phase
     */
    int getCurrentGradation();
}
