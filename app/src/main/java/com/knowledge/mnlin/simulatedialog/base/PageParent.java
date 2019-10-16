package com.knowledge.mnlin.simulatedialog.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.knowledge.mnlin.simulatedialog.FirstPage;
import com.knowledge.mnlin.simulatedialog.R;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;
import com.knowledge.mnlin.simulatedialog.interfaces.PageOperate;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * Created on 2019/10/12  20:15
 * function : page - parent ; a plugin collections
 * <p>
 * provider include-dialog-view-group and control page logic
 * <p>
 * Only one instance object is allowed to create
 *
 * @author mnlin0905@gmail.com
 */
@SuppressLint("Registered")
public class PageParent extends AppCompatActivity implements PageOperate {
    /**
     * external reference
     */
    static PageParent instance;

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
        // inject instance
        //noinspection ConstantConditions
        if (instance != null && pageManager != null && findAllPages().size() != 0) {
            throw new RuntimeException("cannot be create repeatedly");
        }

        instance = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // parent-view
        setContentView(R.layout.page_parent_root_layout);

        // init value
        pageManager = findViewById(R.id.vg_page_route);
        mainHandler = new Handler(Looper.getMainLooper());

        // TODO enter of page-app
        addPage(new FirstPage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * @return main-handler
     */
    @NonNull
    public Handler getMainHandler() {
        return mainHandler;
    }

    /**
     * @return {@link #singlePageManager}
     */
    @NotNull
    public PageManager getPageManager() {
        return pageManager;
    }

    @Override
    public void onBackPressed() {
        LinkedList<Page> allRecords = findAllPages();

        if (allRecords.size() == 0) {
            // has none page
            super.onBackPressed();
        }  else if (!allRecords.getLast().onBackPressed()) {
            // intercept the action
            super.onBackPressed();
        }
    }

    /**
     * remove record
     *
     * @param page {@link Page}
     * @return true if remove success
     */
    @Override
    public boolean removePage(@NotNull Page page) {
        boolean result = pageManager.removePage(page);

        // prevent blank interface
        if (findAllPages().size() == 0) {
            onBackPressed();
        }

        return result;
    }

    /**
     * insert record
     *
     * @param page  {@link Page}
     * @param index the index of page who required add
     */
    @Override
    public void insertPage(int index, @NotNull Page page) {
        switch (page.getLauncherType()) {
            case PageLauncherType.LAUNCHER_DEFAULT_TYPE: // default
                pageManager.insertPage(index, page);
                break;
            case PageLauncherType.LAUNCHER_SINGLE_TOP: // single top
                Page last = findAllPages().peekLast();
                if (last != null && page.getClass() == last.getClass()) {
                    last.onPageReused();
                } else {
                    pageManager.insertPage(index, page);
                }
                break;
            case PageLauncherType.LAUNCHER_SINGLE_TASK: // single task
                LinkedList<Page> allPages = findAllPages();
                int position = -1;
                for (int i = 0; i < allPages.size(); i++) {
                    if (allPages.get(i).getClass() == page.getClass()) {
                        position = i;
                    }
                }
                if (position != -1) {
                    for (int i = allPages.size() - 1; i > position; i--) {
                        removePage(allPages.get(i));
                    }
                    allPages.getLast().onPageReused();
                } else {
                    pageManager.insertPage(index, page);
                }
                break;
            default:
                throw new RuntimeException("not support");
        }
    }

    /**
     * get required page
     *
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    @Override
    public Page findPage(int index) {
        return pageManager.findPage(index);
    }

    /**
     * get all pages
     *
     * @return all pages
     */
    @NotNull
    @Override
    public LinkedList<Page> findAllPages() {
        return pageManager.findAllPages();
    }
}
