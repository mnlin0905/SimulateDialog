package com.knowledge.mnlin.page.core;

import android.content.Context;
import android.os.Handler;

import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageMethodPiling;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/10/24  14:15
 * function : facade of PageParent
 * <p>
 * In order to control the access properties of some methods and the addition of new methods,
 * it is necessary to restrict the access capability through agents.
 *
 * @author mnlin0905@gmail.com
 */
public final class PageContext implements PageMethodPiling {
    public static final String TAG = "PageContext";

    /**
     * entity part, ensure memory safety
     */
    private final WeakReference<PageParent> mParent;

    PageContext(@NotNull PageParent pageParent) {
        super();
        this.mParent = new WeakReference(pageParent);
    }

    /**
     * @return get context from page-parent
     */
    @Nullable
    public final Context getContext() {
        return this.getPageParent();
    }

    /**
     * @return main-handler
     */
    @Nullable
    public final Handler getMainHandler() {
        PageParent entity = this.getPageParent();
        return entity != null ? entity.getMainHandler() : null;
    }

    /**
     * @see PageParent#addPage(Page)
     */
    public final void addPage(@NotNull Page page) {
        PageParent entity = this.getPageParent();
        if (entity != null) {
            entity.insertPageForUser(page);
        }
    }

    /**
     * @see PageParent#insertPageForUser(int, Page)
     */
    public final void insertPage(int index, @NotNull Page page) {
        PageParent entity = this.getPageParent();
        if (entity != null) {
            entity.insertPageForUser(index, page);
        }
    }

    /**
     * @see PageParent#removePageForUser(Page)
     */
    public final boolean removePage(@NotNull Page page) {
        PageParent entity = this.getPageParent();
        return entity != null && entity.removePageForUser(page);
    }

    /**
     * @see PageParent#removePageForUser(int)
     */
    public final boolean removePage(int index) {
        PageParent entity = this.getPageParent();
        return entity != null && entity.removePageForUser(index);
    }

    /**
     * @see PageParent#findPage(int)
     */
    @Nullable
    public final Page findPage(int index) {
        PageParent entity = this.getPageParent();
        return entity != null ? entity.findPage(index) : null;
    }

    /**
     * @see PageParent#indexOfPage(Page)
     */
    public final int getIndexOfPage(@NotNull Page page) {
        PageParent entity = this.getPageParent();
        return entity != null ? entity.indexOfPage(page) : -1;
    }

    /**
     * @see PageParent.findAllPages
     */
    @NotNull
    public final List<Page> getAllPages() {
        PageParent entity = this.getPageParent();
        return entity != null ? entity.findAllPages() : new ArrayList<>();
    }

    /**
     * @see android.content.pm.PackageManager
     */
    @Nullable
    final PageManager getPageManager() {
        PageParent entity = this.getPageParent();
        return entity != null ? entity.getPageManager() : null;
    }

    /**
     * TODO Release other application resources and close some connections with irreversible errors when exceptions occur
     * <p>
     * throw new RuntimeException("PageParent has finished");
     *
     * @return page-parent instance
     */
    private PageParent getPageParent() {
        PageParent pageParent = mParent.get();
        if (pageParent == null) {
            Logger.v("PageParent has finished ,so instance should release itself;");
            PageParent.sInstance = null;
            this.dispatchPiling();
            return null;
        }

        return pageParent;
    }
}
