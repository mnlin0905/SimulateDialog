package com.knowledge.mnlin.simulatedialog.interfaces;

import com.knowledge.mnlin.simulatedialog.base.PageParent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Created on 2019/10/14  19:54
 * function : all operates for {@link PageParent} , {@link PageManager} , {@link PageStackRecord}
 * <p>
 * For different classes, implement their own logic
 * for {@link PageParent} : Responsible for subclasses and calling implementations of other classes
 * for {@link PageManager} : Add page to the actual view tree
 * for {@link PageStackRecord} : Record and save page references
 *
 * @author mnlin0905@gmail.com
 */
public interface PageOperate {
    /**
     * remove record
     *
     * @param page {@link Page}
     * @return true if remove success
     */
    boolean removePage(@NotNull Page page);

    /**
     * add record
     *
     * @param page {@link Page}
     * @throws RuntimeException Do not override this method
     */
    default void addPage(@NotNull Page page) {
        insertPage(findAllPages().size(), page);
    }

    /**
     * insert record
     *
     * @param page  {@link Page}
     * @param index the index of page who required add
     */
    void insertPage(int index, @NotNull Page page);

    /**
     * get required page
     *
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    @Nullable
    Page findPage(int index);

    /**
     * get all pages
     *
     * @return all pages
     */
    @NotNull
    LinkedList<Page> findAllPages();
}
