package com.knowledge.mnlin.simulatedialog.animations;

import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageCallback;
import com.knowledge.mnlin.simulatedialog.interfaces.PageTransAnimation;

/**
 * Created on 2019/10/23  12:12
 * function : no animator
 * <p>
 * `mustCalledWhenEndOrCancel` will be invoked immediately
 *
 * @author mnlin0905@gmail.com
 */
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
}