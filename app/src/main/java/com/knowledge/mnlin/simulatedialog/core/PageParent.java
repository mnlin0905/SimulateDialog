package com.knowledge.mnlin.simulatedialog.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.knowledge.mnlin.simulatedialog.R;
import com.knowledge.mnlin.simulatedialog.configs.PageConfigs;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;
import com.knowledge.mnlin.simulatedialog.interfaces.PartPage;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * Created on 2019/10/12  20:15
 * function : page - parent ; a plugin collections
 * <p>
 * provider include-dialog-view-group and control page logic
 * <p>
 * Only one sInstance object is allowed to create
 *
 * @author mnlin0905@gmail.com
 */
@SuppressLint("Registered")
public class PageParent extends AppCompatActivity {
    private static final String TAG = "PageParent";

    /**
     * external reference
     * <p>
     * must initialized before {@link PageImpl} generation instance
     */
    static PageParent sInstance;

    /**
     * page-manager ; page-parent's root layout,manger the page's add/remove ...
     * <p>
     * manager all page's "add/remove/hide" and so on
     * <p>
     * it's a view-group , can't be null
     * <p>
     * initialized after setContentView
     */
    private PageManager pageManager;

    /**
     * asynchronous task processing
     */
    private Handler mainHandler;

    {
        // inject sInstance
        //noinspection ConstantConditions
        if (sInstance != null && pageManager != null && findAllPages().size() != 0) {
            throw new RuntimeException("cannot be create repeatedly");
        }

        sInstance = this;
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // parent-view
        setContentView(R.layout.page_parent_root_layout);

        // init value
        pageManager = findViewById(R.id.vg_page_route);
        mainHandler = new Handler(Looper.getMainLooper());

        // enter - point
        addPage(PageProcessor$EnterPoint.getPageEnterPoint());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "System level 'onNewIntent' event need to be handled by the page itself");
        LinkedList<Page> allPages = findAllPages();
        int size = allPages.size();
        Page page;
        for (int i = size - 1; i >= 0; i--) {
            page = allPages.get(i);
            if (page.dispatchOnNewIntent(intent)) {
                Log.v(TAG, "System level 'onNewIntent' event has been consumed by " + page.getClass().getCanonicalName()
                        + " ,stack-position is " + i
                        + " ,stack-size is " + size);
                return;
            }
        }
        Log.v(TAG, "System level 'onNewIntent' event Has been abandoned, "
                + " ,stack-size is " + size);
    }

    /**
     * @return main-handler
     */
    @NonNull
    protected Handler getMainHandler() {
        return mainHandler;
    }

    /**
     * @return {@link #singlePageManager}
     */
    @NotNull
    PageManager getPageManager() {
        return pageManager;
    }

    @Override
    @CallSuper
    public void onBackPressed() {
        LinkedList<Page> allRecords = findAllPages();
        int size = allRecords.size();

        if (size == 0) {
            // has none page
            super.onBackPressed();
        } else if (size == 1 && PageConfigs.canBackOnlyOnePage) {
            // has one page but direct return for beauty
            super.onBackPressed();
        } else if (!allRecords.getLast().onBackPressed()) {
            // intercept the action
            super.onBackPressed();
        }
    }

    /**
     * remove record
     * <p>
     * attend: Please call in the main thread.
     * <p>
     * Be careful : must ensure page has added to {@link PageStackRecord},otherwise program will crash
     *
     * @param page {@link Page}
     * @return true if remove success
     */
    private boolean removePage(@NotNull Page page) {
        boolean result = pageManager.removePage(page);

        // prevent blank interface
        if (findAllPages().size() == 0) {
            onBackPressed();
        }

        return result;
    }

    /**
     * insertPage record
     * <p>
     * attend: Please call in the main thread.
     *
     * @param newPage {@link Page}
     * @param index   the index of newPage who required add
     */
    private void addPage(@NotNull Page newPage) {
        LinkedList<Page> allPages = findAllPages();
        int size = allPages.size();
        switch (newPage.getLauncherType()) {
            case PageLauncherType.LAUNCHER_DEFAULT_TYPE: // default
                pageManager.insertPage(size, newPage);
                break;
            case PageLauncherType.LAUNCHER_SINGLE_TOP: // single top
                Page last = allPages.peekLast();
                if (last != null && newPage.getClass() == last.getClass()) {
                    last.onPageReused();
                } else {
                    pageManager.insertPage(size, newPage);
                }
                break;
            case PageLauncherType.LAUNCHER_SINGLE_TASK: // single task
                int position = -1;
                for (int i = 0; i < allPages.size(); i++) {
                    if (allPages.get(i).getClass() == newPage.getClass()) {
                        position = i;
                        break;
                    }
                }
                if (position != -1) {
                    while (allPages.size() - 1 > position) {
                        // TODO To be optimized, try to avoid repeated "add/move" and other work
                        Page removed = allPages.get(allPages.size() - 1);
                        removePage(removed);
                    }
                    allPages.getLast().onPageReused();
                } else {
                    pageManager.insertPage(size, newPage);
                }
                break;
            default:
                throw new RuntimeException("not support");
        }
    }

    /**
     * @see PageParent#insertPageForUser(int, Page)
     */
    final void insertPageForUser(@NotNull Page page) {
        insertPageForUser(findAllPages().size(), page);
    }

    /**
     * insertPage page (for user invoke)
     * <p>
     * Please use this method carefully, which may cause confusion of interface layout.
     *
     * @param index index to insertPage
     * @param page  dest page
     * @throws RuntimeException If page already exists in the record
     */
    final void insertPageForUser(final int index, @NotNull Page page) {
        // prevent impact on ongoing page operations
        mainHandler.post(() -> {
            int indexInner = index;

            // judge index's legality
            if (indexInner == 0) {
                findAllPages().add(0, page);
            } else if (findAllPages().indexOf(page) != -1) {
                Log.w(TAG, "cannot add page who has add to record");
            } else if (indexInner >= findAllPages().size()) {
                addPage(page);
            } else {
                Page destPage = findAllPages().get(indexInner);

                // It is not allowed to insertPage page directly before "part"
                if (destPage.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART) {
                    destPage = findAllPages().get(--indexInner);
                }

                // add record or replace visible page
                if (getPageManager().indexOfChild(destPage) == -1) {
                    findAllPages().add(indexInner, page);
                } else {
                    getPageManager().insertPage(indexInner, page);
                }
            }
        });
    }

    /**
     * remove page (for user invoke)
     * <p>
     * Please use this method carefully, which may cause confusion of interface layout.
     *
     * @param page the page which will be removed
     */
    final boolean removePageForUser(@NotNull Page page) {
        return removePageForUser(findAllPages().indexOf(page));
    }

    /**
     * remove page (for user invoke)
     * <p>
     * Please use this method carefully, which may cause confusion of interface layout.
     *
     * @param index index to remove
     */
    final boolean removePageForUser(final int finalIndex) {
        int index = finalIndex;

        LinkedList<Page> allPages = findAllPages();
        int size = allPages.size();
        if (index < 0 || index >= size) {
            Log.v(TAG, "remove failed,index is invalid");
            return false;
        } else if (index == 0 && size > 1 && allPages.get(1) instanceof ShadeMaskView) {
            Log.v(TAG, "remove failed,cannot remove first page if second page is part");
            return false;
        } else {
            Page destPage = allPages.get(index);

            // if removed-page is mask-view, need to remove the part-page first
            if (destPage instanceof ShadeMaskView) {
                destPage = allPages.get(++index);
            }

            if (getPageManager().indexOfChild(destPage) != -1) {
                // page is visible ,can it be removed?
                if (PageConfigs.canOperateBackVisiblePage || index == size - 1) {
                    return removePage(destPage);
                } else {
                    Log.v(TAG, "remove failed,cannot remove visible page (if want to remove visible page success,please set 'PageConfigs.canOperateVisiblePage' = true)");
                    return false;
                }
            } else {
                // only remove page in the 'PageStackRecord'
                boolean result = allPages.remove(destPage);

                // if necessary, should remove mask-view
                if (result && destPage instanceof PartPage) {
                    result = allPages.remove(((PartPage) destPage).peekMaskForPart());
                }

                return result;
            }
        }
    }

    /**
     * get required page
     * <p>
     * attend: Please call in the main thread.
     *
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    final Page findPage(int index) {
        return pageManager.findPage(index);
    }

    /**
     * page index in {@link PageStackRecord}
     * <p>
     * attend: Please call in the main thread.
     *
     * @param page page
     * @return index corresponding to record
     */
    final int indexOfPage(Page page) {
        return findAllPages().indexOf(page);
    }

    /**
     * get all pages
     * <p>
     * attend: Please call in the main thread.
     *
     * @return all pages
     */
    @NotNull
    final LinkedList<Page> findAllPages() {
        return pageManager.findAllPages();
    }
}
