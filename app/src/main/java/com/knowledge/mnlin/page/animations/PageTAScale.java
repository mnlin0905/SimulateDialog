package com.knowledge.mnlin.page.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageCallback;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;

/**
 * Created on 2019/10/22  16:28
 * function : right-side enter and left-side exit
 *
 * @author mnlin0905@gmail.com
 */
public class PageTAScale implements PageTransAnimation {
    /**
     * Singleton mode
     */
    private static PageTAScale singleInstance;

    private PageTAScale() {
    }

    public static PageTAScale getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new PageTAScale();
        }
        return singleInstance;
    }

    /**
     * in {@link PageStackRecord} , page add to end
     *
     * @param page page
     */
    @Override
    public void onPageRecordRightPush(Page page) {
        View child = page.providerContentView();
        child.setScaleX(0);
        child.setScaleY(0);
        child.animate().scaleX(1).scaleY(1).setDuration(DEFAULT_PAGE_TRANS_DURATION).setListener(null).start();
    }

    /**
     * in {@link PageStackRecord} , page insertPage to front
     *
     * @param page page
     */
    @Override
    public void onPageRecordLeftInsert(Page page) {
        onPageRecordRightPush(page);
    }

    /**
     * in {@link PageStackRecord} , page pop from back
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    @Override
    public void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        View child = page.providerContentView();
        child.setScaleX(1);
        child.setScaleY(1);
        child.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(DEFAULT_PAGE_TRANS_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mustCalledWhenEndOrCancel.run(page);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        onAnimationEnd(animation);
                    }
                })
                .start();
    }

    /**
     * in {@link PageStackRecord} , page remove from front
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    @Override
    public void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        onPageRecordRightPop(page, mustCalledWhenEndOrCancel);
    }

    /**
     * Return to the normal display state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToAttachStatus(Page page) {
        View view = page.providerContentView();
        view.setScaleX(1);
        view.setScaleY(1);
    }

    /**
     * Return to the Detach state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToDetachStatus(Page page) {
        View view = page.providerContentView();
        view.setScaleX(0);
        view.setScaleY(0);
    }

    /**
     * cancel the animation if necessary
     *
     * @param page page
     */
    @Override
    public void cancelPageAnimation(Page page) {
        page.providerContentView().animate().cancel();
    }
}
