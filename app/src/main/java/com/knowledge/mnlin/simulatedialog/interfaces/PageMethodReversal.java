package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.simulatedialog.core.PageImpl;

/**
 * Created on 2019/10/15  11:28
 * function : Value Processing Logic from Subclasses
 *
 *
 *
 * @author mnlin0905@gmail.com
 */
interface PageMethodReversal {
    /**
     * called method is legal or illegal
     */
    static void ensureCalledMethodIsLegal() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        if (!stackTraceElement.getClassName().equals(PageImpl.class.getCanonicalName())) {
            throw new RuntimeException("Non 'PageImpl' class is not allowed to call this method");
        }
    }

    /**
     * @return real view of page
     */
    @Nullable
    default View generateContentView() {
        ensureCalledMethodIsLegal();
        return null;
    }

    /**
     * @return real layout-params of page
     */
    @Nullable
    default ViewGroup.LayoutParams generateLayoutParams() {
        ensureCalledMethodIsLegal();
        return null;
    }

    /**
     * @return real layout-params of page
     */
    @Nullable
    default PageTransAnimation generateTransAnimation() {
        ensureCalledMethodIsLegal();
        return null;
    }
}
