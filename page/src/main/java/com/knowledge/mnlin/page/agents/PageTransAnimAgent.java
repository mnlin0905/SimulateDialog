package com.knowledge.mnlin.page.agents;

import android.util.Log;

import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageCallback;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created on 2019/10/26  15:09
 * function : the agent of page-animator
 * <p>
 * you can combine multiple animations into one to use
 *
 * @author mnlin0905@gmail.com
 */
public class PageTransAnimAgent implements PageTransAnimation {
    private static final String TAG = "PageTransAnimAgent";

    /**
     * the animators
     */
    private List<PageTransAnimation> animators = new ArrayList<>();

    public PageTransAnimAgent(@NotNull PageTransAnimation... animators) {
        this.animators.addAll(Arrays.asList(animators));
        Collections.sort(this.animators, (o1, o2) -> o1.getLevel() - o2.getLevel());
        Log.v(TAG, "animator agent inject");
    }

    /**
     * in {@link PageStackRecord} , page add to end
     *
     * @param page page
     */
    @Override
    public void onPageRecordRightPush(Page page) {
        for (PageTransAnimation animator : animators) {
            animator.onPageRecordRightPush(page);
        }
    }

    /**
     * in {@link PageStackRecord} , page insertPage to front
     *
     * @param page page
     */
    @Override
    public void onPageRecordLeftInsert(Page page) {
        for (PageTransAnimation animator : animators) {
            animator.onPageRecordLeftInsert(page);
        }
    }

    /**
     * in {@link PageStackRecord} , page pop from back
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    @Override
    public void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        for (PageTransAnimation animator : animators) {
            animator.onPageRecordRightPop(page, mustCalledWhenEndOrCancel);
        }
    }

    /**
     * in {@link PageStackRecord} , page remove from front
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    @Override
    public void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        for (PageTransAnimation animator : animators) {
            animator.onPageRecordLeftRemove(page, mustCalledWhenEndOrCancel);
        }
    }

    /**
     * Return to Attach state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToAttachStatus(Page page) {
        for (PageTransAnimation animator : animators) {
            animator.returnToAttachStatus(page);
        }
    }

    /**
     * Return to the Detach state to ensure that the page can recover.
     *
     * @param page page
     */
    @Override
    public void returnToDetachStatus(Page page) {
        for (PageTransAnimation animator : animators) {
            animator.returnToDetachStatus(page);
        }
    }

    /**
     * cancel the animation if necessary
     *
     * @param page page
     */
    @Override
    public void cancelPageAnimation(Page page) {
        for (PageTransAnimation animator : animators) {
            animator.cancelPageAnimation(page);
        }
    }
}
