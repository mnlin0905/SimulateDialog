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
     * At this time, page has been added to the view interface
     * <p>
     * May be called more than once
     *
     * @hide
     */
    void onPageAttachToParent();

    /**
     * page visible and interactive
     * <p>
     * At this time , page is in the foreground interface
     */
    void onPageActive();

    /**
     * Before the interface is invisible, it has entered the visible state again.
     * <p>
     * called after {@link PageLifeCycle#onPageActive()}
     */
    void onPageReResume();

    /**
     * At this time, page is about to be removed from the view interface
     * <p>
     * or it's a background-page (only before part-page that's what happens)
     */
    void onPageDeactive();

    /**
     * page removed from parent
     * <p>
     * At this time, page has been removed from the view interface
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
