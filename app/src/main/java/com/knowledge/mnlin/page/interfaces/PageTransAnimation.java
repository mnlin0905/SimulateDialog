package com.knowledge.mnlin.page.interfaces;

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
    long DEFAULT_PAGE_TRANS_DURATION = 2000L;

    /**
     * in {@link com.knowledge.mnlin.page.core.PageStackRecord} , page add to end
     *
     * @param page page
     */
    void onPageRecordRightPush(Page page);

    /**
     * in {@link com.knowledge.mnlin.page.core.PageStackRecord} , page insertPage to front
     *
     * @param page page
     */
    void onPageRecordLeftInsert(Page page);

    /**
     * in {@link com.knowledge.mnlin.page.core.PageStackRecord} , page pop from back
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel);

    /**
     * in {@link com.knowledge.mnlin.page.core.PageStackRecord} , page remove from front
     *
     * @param page                      page
     * @param mustCalledWhenEndOrCancel must call this method to prevent child's visible
     */
    void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel);

    /**
     * Return to Attach state to ensure that the page can recover.
     *
     * @param page page
     */
    void returnToAttachStatus(Page page);

    /**
     * Return to the Detach state to ensure that the page can recover.
     *
     * @param page page
     */
    void returnToDetachStatus(Page page);

    /**
     * cancel the animation if necessary
     *
     * @param page page
     */
    void cancelPageAnimation(Page page);

    /**
     * the animation priority , A higher value indicates a higher priority
     *
     * @return priority,a higher value indicates a higher priority
     */
    default int getLevel() {
        return 0;
    }
}
