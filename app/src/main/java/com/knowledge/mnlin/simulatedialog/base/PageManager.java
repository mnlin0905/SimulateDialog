package com.knowledge.mnlin.simulatedialog.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.interfaces.MaskOperateListener;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageOperate;

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
     * simulated masking effect
     */
    @NonNull
    private final ShadeMaskView shadeMaskView;

    /**
     * page's route-record
     *
     * @see PageStackRecord
     */
    private PageStackRecord pageStackRecord = PageStackRecord.getInstance();

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

        // init mask
        shadeMaskView = new ShadeMaskView(context);
        shadeMaskView.setMaskOperateListener(this);
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
     * @return true if remove success
     */
    @Override
    public boolean removePage(@NotNull Page page) {
        // remove page
        removeView(page.providerContentView());

        // on-page-detach
        page.onPageDetach();

        // remove success or fail,remove record
        boolean result = pageStackRecord.removePage(page);

        // post "on-page-destroy"
        post(page::onPageDestroy);

        // remove mask
        if (findAllPages().size() > 0 && findAllPages().getLast().getClass() == ShadeMaskView.class) {
            removePage(findAllPages().getLast());
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
        // if page is not fullscreen , add mask-view
        if (page.getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART) {
            addPage(new ShadeMaskView(getContext()));
        }

        // add page
        addView(page.providerContentView(), index, page.providerIntegrateParams());

        // add record
        pageStackRecord.addPage(page);

        // on-page-attach
        page.onPageAttach();
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
     * @param view target
     */
    @Override
    public void dispatchMaskOnClick(@NonNull View view) {

    }
}
