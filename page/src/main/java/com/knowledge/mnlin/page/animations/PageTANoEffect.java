package com.knowledge.mnlin.page.animations;

import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageCallback;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;
import com.knowledge.mnlin.page_annotation.annotations.PageTransAnimTag;

/**
 * Created on 2019/10/23  12:12
 * function : no animator
 * <p>
 * `mustCalledWhenEndOrCancel` will be invoked immediately
 *
 * @author mnlin0905@gmail.com
 */
@PageTransAnimTag
public class PageTANoEffect implements PageTransAnimation {
    /**
     * Singleton mode
     */
    private static PageTANoEffect singleInstance;

    private PageTANoEffect() {
    }

    public static PageTANoEffect getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new PageTANoEffect();
        }
        return singleInstance;
    }

    @Override
    public void onPageRecordRightPush(Page page) {

    }

    @Override
    public void onPageRecordLeftInsert(Page page) {

    }

    @Override
    public void onPageRecordRightPop(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        mustCalledWhenEndOrCancel.run(page);
    }

    @Override
    public void onPageRecordLeftRemove(Page page, PageCallback<Page> mustCalledWhenEndOrCancel) {
        mustCalledWhenEndOrCancel.run(page);
    }

    @Override
    public void returnToAttachStatus(Page page) {

    }

    @Override
    public void returnToDetachStatus(Page page) {

    }

    @Override
    public void cancelPageAnimation(Page page) {
    }
}
