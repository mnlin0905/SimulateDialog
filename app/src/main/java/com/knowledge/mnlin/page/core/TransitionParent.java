package com.knowledge.mnlin.page.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageCallback;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 2019/10/22  15:31
 * function : route the animator
 *
 * @author mnlin0905@gmail.com
 */
abstract class TransitionParent extends FrameLayout {
    /**
     * store the view who in remove animation
     */
    private List<Page> removedCaches = new CopyOnWriteArrayList<>();

    /**
     * At the end of the animation, make sure that the view is removed and the traces are removed
     */
    private PageCallback<Page> mustBeCalledWhenRemoved = tag -> {
        if (removedCaches.remove(tag)) {
            removeView(tag.providerContentView());
        }
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
    public void addView(Page page) {
        this.addView(page, -1);
    }

    /**
     * add for {@link PageManager
     */
    public void addView(Page page, int index) {
        // if page will be removed ,but already in the removedCaches'' list(Actually not removed),we only should remove on 'removedCaches'
        if (removedCaches.indexOf(page) != -1) {
            removedCaches.remove(page);
            page.performCancelPageAnimation();
            page.performReturnToAttachStatus();
            return;
        }

        View view = page.providerContentView();

        super.addView(view, index, page.providerIntegrateParams());

        // The first view cannot be animated
        if (getChildCount() > 1) {
            // Distinguish animation executed in different time periods
            if (index == getChildCount() || index == -1) {
                // is first add to record
                page.performPageRecordRightPush();
            } else {
                page.performPageRecordLeftInsert();
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
            removedCaches.add(page);
            if (findAllPages().getLast() == page) {
                page.performPageRecordRightPop(mustBeCalledWhenRemoved);
            } else {
                page.performPageRecordLeftRemove(mustBeCalledWhenRemoved);
            }
            return;
        }

        removeView(view);
    }

    /**
     * override this method for exclude view who in removed-animation
     */
    public int indexOfChild(Page page) {
        return removedCaches.indexOf(page) == -1 ? indexOfChild(page.providerContentView()) : -1;
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    /**
     * get all pages in {@link PageStackRecord}
     *
     * @return all pages
     */
    protected abstract LinkedList<Page> findAllPages();
}
