package com.knowledge.mnlin.simulatedialog.base;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.interfaces.MaskOperateListener;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageOperate;
import com.knowledge.mnlin.simulatedialog.interfaces.PartPage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * Created on 2019/10/12  21:19
 * function : page manager
 * <p>
 * The main tasks are as follows:
 * 1. add / remove page
 * 2. inject animator
 * 3.
 *
 * @author mnlin0905@gmail.com
 */
public class PageManager extends FrameLayout implements PageOperate, MaskOperateListener {
    /**
     * is really remove or add page?
     * <p>
     * A lot of times we need to keep records.
     */
    private boolean isRealOperatePage = true;

    /**
     * page's route-record
     *
     * @see PageStackRecord
     */
    private PageStackRecord pageStackRecord = new PageStackRecord();

    public PageManager(@NotNull Context context) {
        this(context, null);
    }

    public PageManager(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageManager(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PageManager(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * @return external call
     */
    public PageStackRecord getPageStackRecord() {
        return pageStackRecord;
    }

    /**
     * remove record
     *
     * @param page {@link Page}
     * @return true if remove success , false if record remain
     */
    @Override
    public final synchronized boolean removePage(@NotNull Page page) {
        // on-page-deactivate
        page.onPageDeactive();

        // determine whether the previous page needs to be add
        if (isRealOperatePage && page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_FULLSCREEN && !(page instanceof ShadeMaskView)) {
            int position = findAllPages().indexOf(page);
            if (position > 0) {
                isRealOperatePage = false;
                insertPage(position - 1, findAllPages().get(position - 1));
                isRealOperatePage = true;
            }
        }

        // remove page
        removeView(page.providerContentView());

        // remove success or fail,remove record
        boolean result = isRealOperatePage && pageStackRecord.removePage(page);

        // on-page-detach
        page.onPageDetachFromParent();

        // remove mask
        if (page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART && page instanceof PartPage) {
            ShadeMaskView mask = ((PartPage) page).peekMaskForPart();
            if (mask != null) {
                removePage(mask);
            }
        }

        // if isRealOperatePage is false,need remove a page before(It should exist under normal circumstances.);
        if (!isRealOperatePage && page instanceof ShadeMaskView) {
            int beforePageIndex = findAllPages().indexOf(page) - 1;
            if (beforePageIndex >= 0) {
                removePage(findAllPages().get(beforePageIndex));
            }
        }

        return result;
    }

    /**
     * insert record
     *
     * @param page  {@link Page}
     * @param index the index of page who required add
     * @throws RuntimeException if first page is "part" type
     */
    @Override
    public final synchronized void insertPage(int index, @NotNull Page page) {
        // if page is not fullscreen , add mask-view
        if (page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART && page instanceof PartPage) {
            if (findAllPages().size() == 0) {
                throw new RuntimeException("first page can't be 'part' type");
            }

            // insert mask
            if (isRealOperatePage) {
                insertMaskPage((PartPage) page, index++);
            }
        }

        // add page ; Page insertion of visual interface is not supported at present
        if (index >= findAllPages().size()) {
            addView(page.providerContentView(), page.providerIntegrateParams());
        } else {
            addView(page.providerContentView(), 0, page.providerIntegrateParams());
        }

        // add record
        if (isRealOperatePage) {
            pageStackRecord.insertPage(index, page);
        } else {
            // on-page-resume
            page.onPageReResume();
        }

        // on-page-attach
        page.onPageAttachToParent();

        // determine whether the previous page needs to be removed
        if (isRealOperatePage && page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_FULLSCREEN && !(page instanceof ShadeMaskView)) {
            int position = findAllPages().indexOf(page);
            if (position > 0) {
                isRealOperatePage = false;
                removePage(findAllPages().get(position - 1));
                isRealOperatePage = true;
            }
        }

        // on-page-activate
        page.onPageActive();

        // TODO
        if (!isRealOperatePage && page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART && page instanceof PartPage) {
            insertMaskPage((PartPage) page, --index);

            // TODO mask 前面还需要再插入一个 page
            int previousIndex = findAllPages().indexOf(((PartPage) page).peekMaskForPart()) - 1;
            if (previousIndex >= 0) {
                insertPage(previousIndex, findAllPages().get(previousIndex));
            }
        }
    }

    /**
     * TODO : 如果后面支持 insert 中间插入,该方法就不必提取了
     * <p>
     * insert mask-page
     *
     * @param host  host
     * @param index index
     */
    private void insertMaskPage(PartPage host, int index) {
        ShadeMaskView mask = host.peekMaskForPart() == null ? new ShadeMaskView(getContext()).setMaskOperateListener(this).setHostPage(host) : host.peekMaskForPart();
        insertPage(index, mask);
        host.injectMaskForPart(mask);
    }

    /**
     * get required page
     *
     * @param index page's position in {@link records}
     * @return index corresponding to record
     */
    @Nullable
    @Override
    public Page findPage(int index) {
        return pageStackRecord.findPage(index);
    }

    /**
     * get all pages
     *
     * @return all pages
     */
    @NotNull
    @Override
    public LinkedList<Page> findAllPages() {
        return pageStackRecord.findAllPages();
    }

    /**
     * dispatch the click action
     *
     * @param mask target
     */
    @Override
    public void dispatchMaskOnClick(@NonNull ShadeMaskView mask) {
        if (mask.getHostPage().closedOnClickOutside()) {
            // remove host - view
            removePage(mask.getHostPage());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
