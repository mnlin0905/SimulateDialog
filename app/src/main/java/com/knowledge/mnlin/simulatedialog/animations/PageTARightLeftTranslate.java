package com.knowledge.mnlin.simulatedialog.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageCallback;
import com.knowledge.mnlin.simulatedialog.interfaces.PageTransAnimation;

/**
 * Created on 2019/10/22  16:28
 * function : right-side enter and left-side exit
 *
 * @author mnlin0905@gmail.com
 */
public class PageTARightLeftTranslate implements PageTransAnimation {
    /**
     * variable holder
     */
    private static final int screenWidth = ScreenUtils.getScreenWidth();

    private static final int screenHeight = ScreenUtils.getScreenHeight();

    /**
     * Singleton mode
     */
    private static PageTARightLeftTranslate singleInstance;

    private PageTARightLeftTranslate() {
    }

    public static PageTARightLeftTranslate getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new PageTARightLeftTranslate();
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
        child.setTranslationX(screenWidth);
        child.animate().translationX(0).setDuration(DEFAULT_PAGE_TRANS_DURATION).setListener(null).start();
    }

    /**
     * in {@link PageStackRecord} , page insertPage to front
     *
     * @param page page
     */
    @Override
    public void onPageRecordLeftInsert(Page page) {
        View child = page.providerContentView();
        child.setTranslationX(-screenWidth);
        child.animate().translationX(0).setDuration(DEFAULT_PAGE_TRANS_DURATION).setListener(null).start();
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
        child.setTranslationX(0);
        child.animate()
                .translationX(screenWidth)
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
        View child = page.providerContentView();
        child.setTranslationX(0);
        child.animate()
                .translationX(-screenWidth)
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
     * Return to the normal display state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToAttachStatus(Page page) {
        page.providerContentView().setTranslationX(0);
    }

    /**
     * Return to the Detach state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToDetachStatus(Page page) {
        page.providerContentView().setTranslationX(screenWidth);
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
