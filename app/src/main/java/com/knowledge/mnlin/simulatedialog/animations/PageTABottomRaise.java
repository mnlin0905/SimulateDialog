package com.knowledge.mnlin.simulatedialog.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageCallback;
import com.knowledge.mnlin.simulatedialog.interfaces.PageTransAnimation;

/**
 * Created on 2019/10/22  16:28
 * function : Raise from bottom (almost for part-page)
 *
 * @author mnlin0905@gmail.com
 */
public class PageTABottomRaise implements PageTransAnimation {
    private static final int screenHeight = ScreenUtils.getScreenHeight();

    /**
     * Singleton mode
     */
    private static PageTABottomRaise singleInstance;

    /**
     * variable holder
     */
    private int pageHeight;

    private PageTABottomRaise() {
    }

    public static PageTABottomRaise getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new PageTABottomRaise();
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
        child.setTranslationY(pageHeight);
        child.animate().translationY(0).setDuration(DEFAULT_PAGE_TRANS_DURATION).setListener(null).start();
    }

    /**
     * in {@link PageStackRecord} , page insert to front
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
                .translationY(pageHeight)
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
}
