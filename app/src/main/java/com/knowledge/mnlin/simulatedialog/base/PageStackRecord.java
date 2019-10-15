package com.knowledge.mnlin.simulatedialog.base;

import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageOperate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Created on 2019/10/14  11:07
 * function : page-stack
 * <p>
 * for page route
 *
 * @author mnlin0905@gmail.com
 */
public final class PageStackRecord implements PageOperate {
    /**
     * single - instance
     */
    private static final PageStackRecord singleInstance = new PageStackRecord();

    /**
     * record page
     */
    private LinkedList<Page> records = new LinkedList<>();

    private PageStackRecord() {
        // throw new RuntimeException("not support");
    }

    /**
     * provide instance for external call
     *
     * @return {@link PageStackRecord#singleInstance}
     */
    public static PageStackRecord getInstance() {
        return singleInstance;
    }

    /**
     * @param page {@link Page}
     */
    @Override
    public boolean removePage(@NotNull Page page) {
        return records.remove(page);
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
// TODO
        }
        records.add(index, page);
    }

    /**
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    @Nullable
    @Override
    public Page findPage(int index) {
        if (records.size() > index) {
            return records.get(index);
        }
        return null;
    }

    /**
     * get all pages
     *
     * @return all pages
     */
    @NotNull
    @Override
    public LinkedList<Page> findAllPages() {
        return records;
    }
}
