package com.knowledge.mnlin.simulatedialog.interfaces;

/**
 * Created on 2019/10/22  16:21
 * function : Transition animation
 *
 * @author mnlin0905@gmail.com
 */
public interface PageTransAnimation {
    /**
     * default animation duration
     */
    long DEFAULT_PAGE_TRANS_DURATION = 300L;

    /**
     * in {@link com.knowledge.mnlin.simulatedialog.core.PageStackRecord} , page add to end
     *
     * @param page page
     */
    void onPageRecordRightPush(Page page);

    /**
     * in {@link com.knowledge.mnlin.simulatedialog.core.PageStackRecord} , page insert to front
     *
     * @param page page
     */
    void onPageRecordLeftInsert(Page page);

    /**
     * in {@link com.knowledge.mnlin.simulatedialog.core.PageStackRecord} , page pop from back
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel);

    /**
     * in {@link com.knowledge.mnlin.simulatedialog.core.PageStackRecord} , page remove from front
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel);
}