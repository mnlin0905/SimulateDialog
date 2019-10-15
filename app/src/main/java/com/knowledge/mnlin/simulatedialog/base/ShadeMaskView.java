package com.knowledge.mnlin.simulatedialog.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.knowledge.mnlin.simulatedialog.interfaces.MaskOperateListener;
import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;

/**
 * Created on 2019/10/15  10:03
 * function : simulate the mask
 *
 * @author mnlin0905@gmail.com
 */
public class ShadeMaskView extends FrameLayout implements View.OnClickListener, Page {
    /**
     * dimen bg-drawable
     */
    private Drawable maskDrawable;

    /**
     * action dispatch
     */
    @Nullable
    private MaskOperateListener listener;

    {
        maskDrawable = new ColorDrawable(Color.parseColor("#00000000"));
    }

    public ShadeMaskView(@NonNull Context context) {
        this(context, null);
    }

    public ShadeMaskView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadeMaskView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public ShadeMaskView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // init the mask
        setBackground(maskDrawable);
        setOnClickListener(this);
        setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * @param listener dest listener
     */
    public void setMaskOperateListener(@Nullable MaskOperateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.dispatchMaskOnClick(view);
        }
    }

    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    @Override
    public PageParent getPageParent() {
        return PageParent.instance;
    }

    /**
     * @return page's style
     */
    @Override
    public int getPageAppearanceType() {
        return PageAppearance.PAGE_APPEARANCE_FULLSCREEN;
    }

    /**
     * called when "back" be pressed
     *
     * @return if true,manager will not close the page (regard as a dialog)
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * @return page's launcher type
     */
    @Override
    public int getLauncherType() {
        return PageLauncherType.LAUNCHER_DEFAULT_TYPE;
    }

    /**
     * Called back in constructor
     */
    @Override
    public void onPageCreate() {

    }

    /**
     * page's content-view has set
     */
    @Override
    public void onPageViewInject() {

    }

    /**
     * page is visible
     */
    @Override
    public void onPageAttach() {

    }

    /**
     * page be blocked
     */
    @Override
    public void onPageDetach() {

    }

    /**
     * page is removed from page-stack-record
     */
    @Override
    public void onPageDestroy() {

    }

    /**
     * When pages are reused
     *
     * @see Activity#onNewIntent(Intent)
     */
    @Override
    public void onPageReused() {

    }

    /**
     * @return real-view in page ; if it is null , the system reads the annotated values
     */
    @NonNull
    @Override
    public View providerContentView() {
        return this;
    }

    /**
     * @return the layout-params put in parent-view , if it is null , the system will take the default value (read from xml)
     */
    @NonNull
    @Override
    public ViewGroup.LayoutParams providerIntegrateParams() {
        return this.getLayoutParams();
    }
}