package com.knowledge.mnlin.simulatedialog.base;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.interfaces.MaskOperateListener;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
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
final class PageManager extends FrameLayout implements PageOperate, MaskOperateListener {
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
    public final boolean removePage(@NotNull Page page) {
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
     * <p>
     * attend:
     * 1 page has insert into {@link PageStackRecord} or index larger than {@link PageStackRecord}'s size; otherwise ,please call {@link PageManager#insertPageRecord(int, Page)}
     * 2 This method should not be called directly
     *
     * @param page  {@link Page}
     * @param index the index of page who required add
     * @throws RuntimeException if first page is "part" type
     */
    @Override
    public final void insertPage(int index, @NotNull Page page) {
        // if page is not fullscreen , add mask-view
        if (isRealOperatePage && page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART && page instanceof PartPage) {
            if (findAllPages().size() == 0) {
                throw new RuntimeException("first page can't be 'part' type");
            }

            // insert mask
            insertMaskPage((PartPage) page, index++);
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
                Page destPage = findAllPages().get(position - 1);

                // You do not need to remove views that are not added to the user-interface
                if (indexOfChild(destPage.providerContentView()) != -1) {
                    isRealOperatePage = false;
                    removePage(destPage);
                    isRealOperatePage = true;
                }
            }
        }

        // on-page-activate
        page.onPageActive();

        // mask-view needs to be loaded before "part" in "isRealOperatePage" process
        if (!isRealOperatePage && page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART && page instanceof PartPage) {
            insertMaskPage((PartPage) page, --index);
        }
    }

    /**
     * insert page
     * <p>
     * Please use this method carefully, which may cause confusion of interface layout.
     *
     * @param index index to insert
     * @param page  dest page
     * @throws RuntimeException If page already exists in the record
     */
    public void insertPageRecord(final int index, @NotNull Page page) {
        // prevent impact on ongoing page operations
        post(() -> {
            int indexInner = index;

            // judge index's legality
            if (indexInner == 0) {
                pageStackRecord.findAllPages().add(0, page);
            } else if (findAllPages().indexOf(page) != -1) {
                throw new RuntimeException("cannot add page who has add to record");
            } else if (indexInner >= findAllPages().size()) {
                insertPage(indexInner, page);
            } else {
                Page destPage = findAllPages().get(indexInner);

                // It is not allowed to insert page directly before "part"
                if (destPage.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART) {
                    destPage = findAllPages().get(--indexInner);
                }

                // add record or replace visible page
                if (indexOfChild(destPage.providerContentView()) == -1) {
                    findAllPages().add(indexInner, page);
                } else {
                    insertPage(indexInner, page);
                }
            }
        });
    }

    /**
     * insert mask-page
     *
     * @param host  host
     * @param index index
     */
    private void insertMaskPage(PartPage host, int index) {
        ShadeMaskView mask = host.peekMaskForPart();
        if (mask == null) {
            mask = new ShadeMaskView(getContext()).setMaskOperateListener(this).setHostPage(host);
            insertPage(index, mask);
            host.injectMaskForPart(mask);
        } else {
            insertPage(index, mask);

            // another page needs to be loaded before "mask-view"
            int previousIndex = findAllPages().indexOf(host.peekMaskForPart()) - 1;
            if (previousIndex >= 0) {
                insertPage(previousIndex, findAllPages().get(previousIndex));
            }
        }
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
