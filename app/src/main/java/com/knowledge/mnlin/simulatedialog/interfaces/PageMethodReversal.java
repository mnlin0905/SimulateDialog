package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.simulatedialog.core.PageImpl;
import com.knowledge.mnlin.simulatedialog.core.PageParent;

/**
 * Created on 2019/10/15  11:28
 * function : Value Processing Logic from Subclasses
 *
 * @author mnlin0905@gmail.com
 */
public interface PageMethodReversal {
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
     * @param pageParent PageParent
     * @return real view of page
     */
    @Nullable
    default View generateContentView(@NonNull PageParent pageParent) {
        ensureCalledMethodIsLegal();
        return null;
    }

    /**
     * @param pageParent PageParent
     * @return real layout-params of page
     */
    @Nullable
    default ViewGroup.LayoutParams generateLayoutParams(@NonNull PageParent pageParent) {
        ensureCalledMethodIsLegal();
        return null;
    }

    /**
     * @param pageParent PageParent
     * @return real layout-params of page
     */
    @Nullable
    default PageTransAnimation generateTransAnimation(@NonNull PageParent pageParent) {
        ensureCalledMethodIsLegal();
        return null;
    }
}
