package com.knowledge.mnlin.page.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageCallback;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;
import com.knowledge.mnlin.page_annotation.annotations.PageTransAnimTag;

/**
 * Created on 2019/10/22  16:28
 * function : down from top (almost for part-page)
 *
 * @author mnlin0905@gmail.com
 */
@PageTransAnimTag
public class PageTATopDown implements PageTransAnimation {
    private static final int screenHeight = ScreenUtils.getScreenHeight();

    /**
     * Singleton mode
     */
    private static PageTATopDown singleInstance;

    /**
     * variable holder
     */
    private int pageHeight;

    private PageTATopDown() {
    }

    public static PageTATopDown getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new PageTATopDown();
        }
        return singleInstance;
    }

    /**
     * Before animating, make sure you know the height of the page
     */
    private void ensureHeightDimen(Page page) {
        if (pageHeight <= 0) {
            View view = page.providerContentView();
            pageHeight = view.getMeasuredHeight();
            if (pageHeight <= 0) {
                view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                pageHeight = view.getMeasuredHeight();
                if (pageHeight <= 0) {
                    pageHeight = screenHeight;
                }
            }
        }
    }

    /**
     * in {@link PageStackRecord} , page add to end
     *
     * @param page page
     */
    @Override
    public void onPageRecordRightPush(Page page) {
        ensureHeightDimen(page);
        View child = page.providerContentView();
        child.setTranslationY(-pageHeight);
        child.animate().translationY(0).setDuration(DEFAULT_PAGE_TRANS_DURATION).setListener(null).start();
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
        ensureHeightDimen(page);
        View child = page.providerContentView();
        child.setTranslationY(0);
        child.animate()
                .translationY(-pageHeight)
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
        page.providerContentView().setTranslationY(0);
    }

    /**
     * Return to the Detach state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToDetachStatus(Page page) {
        ensureHeightDimen(page);
        page.providerContentView().setTranslationY(pageHeight);
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
