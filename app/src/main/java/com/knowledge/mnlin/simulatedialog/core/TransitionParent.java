package com.knowledge.mnlin.simulatedialog.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageCallback;
import com.knowledge.mnlin.simulatedialog.interfaces.PageOperate;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2019/10/22  15:31
 * function : route the animator
 *
 * @author mnlin0905@gmail.com
 */
abstract class TransitionParent extends FrameLayout implements PageOperate {
    /**
     * store the view who in remove animation
     */
    private List<Page> removedCaches = new LinkedList<>();

    /**
     * At the end of the animation, make sure that the view is removed and the traces are removed
     */
    private PageCallback<Page> mustBeCalledWhenRemoved = tag -> {
        removedCaches.remove(tag);
        removeView(tag.providerContentView());
    };

    public TransitionParent(@NonNull Context context) {
        super(context);
    }

    public TransitionParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransitionParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TransitionParent(@NotNull Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * add for {@link PageManager}
     */
    public void addView(Page page, ViewGroup.LayoutParams params) {
        this.addView(page, -1, params);
    }

    /**
     * add for {@link PageManager
     */
    public void addView(Page page, int index, ViewGroup.LayoutParams params) {
        View view = page.providerContentView();

        super.addView(view, index, params);

        // The first view cannot be animated
        if (getChildCount() > 1) {
            // Distinguish animation executed in different time periods
            if (index == getChildCount() || index == -1) {
                // is first add to record
                page.onPageRecordRightPush(page);
            } else {
                page.onPageRecordLeftInsert(page);
            }
        }
    }

    /**
     * remove view in page with animation
     */
    void removeView(Page page) {
        View view = page.providerContentView();

        // The first view cannot be animated
        if (getChildCount() > 1) {
            if (findAllPages().getLast() == page) {
                page.onPageRecordRightPop(page, mustBeCalledWhenRemoved);
            } else {
                page.onPageRecordLeftRemove(page, mustBeCalledWhenRemoved);
            }
        }

        removeView(view);
    }

    /**
     * override this method for exclude view who in removed-animation
     */
    public int indexOfChild(Page page) {
        return removedCaches.indexOf(page) == -1 ? indexOfChild(page.providerContentView()) : -1;
    }
}
