package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.simulatedialog.base.PageParent;

/**
 * Created on 2019/10/15  11:28
 * function : Value Processing Logic from Subclasses
 *
 * @author mnlin0905@gmail.com
 */
public interface PageMethodReversal {
    /**
     * @param pageParent PageParent
     * @return real view of page
     */
    @Nullable
    default View getContentView(@NonNull PageParent pageParent) {
        return null;
    }

    /**
     * @param pageParent PageParent
     * @return real layout-params of page
     */
    @Nullable
    default ViewGroup.LayoutParams getLayoutParams(@NonNull PageParent pageParent) {
        return null;
    }
}
