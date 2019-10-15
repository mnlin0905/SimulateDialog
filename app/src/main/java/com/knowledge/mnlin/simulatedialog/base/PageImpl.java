package com.knowledge.mnlin.simulatedialog.base;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.simulatedialog.interfaces.Page;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageAppearanceType;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageLauncherType;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageLayoutRes;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageMenuRes;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Created on 2019/10/12  22:00
 * function : page's implement
 * <p>
 * simulate dialog/view/activity/fragment
 *
 * @author mnlin0905@gmail.com
 */
public abstract class PageImpl implements Page {
    /**
     * holder a instance of content-view
     */
    @Nullable
    private volatile View contentViewHolder;

    /**
     * holder a instance of layout-param
     */
    @Nullable
    private volatile ViewGroup.LayoutParams layoutParamHolder;

    /**
     * annotation values
     */
    @Nullable
    private Integer annotationLayoutId;
    @Nullable
    private Integer annotationMenuId;
    @Nullable
    private Integer annotationAppearanceType;
    @Nullable
    private Integer annotationLauncherType;

    /**
     * parent cannot be null
     */
    @NotNull
    private PageParent parent;

    public PageImpl() {
        this.parent = PageParent.instance;

        // judge param and init annotation value
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof InjectPageLayoutRes) {
                annotationLayoutId = ((InjectPageLayoutRes) annotation).layoutResId();
                continue;
            }
            if (annotation instanceof InjectPageMenuRes) {
                annotationMenuId = ((InjectPageMenuRes) annotation).menuResId();
                continue;
            }
            if (annotation instanceof InjectPageAppearanceType) {
                annotationAppearanceType = ((InjectPageAppearanceType) annotation).pageAppearanceType();
                continue;
            }
            if (annotation instanceof InjectPageLauncherType) {
                annotationLauncherType = ((InjectPageLauncherType) annotation).pageLauncherType();
                continue;
            }
            // ... other annotation
        }

        // on-create
        onPageCreate();
    }

    /**
     * Called back in constructor
     */
    @Override
    @CallSuper
    public void onPageCreate() {
        Logger.v("PageImpl : ", "onPageCreate");
    }

    /**
     * page's content-view has set
     */
    @CallSuper
    @Override
    public void onPageViewInject() {
        Logger.v("PageImpl : ", "onPageViewInject");
    }

    /**
     * page is visible
     */
    @Override
    @CallSuper
    public void onPageAttach() {
        Logger.v("PageImpl : ", "onPageAttach");
    }

    /**
     * page be blocked
     */
    @Override
    @CallSuper
    public void onPageDetach() {
        Logger.v("PageImpl : ", "onPageDetach");
    }

    /**
     * page is removed from page-stack-record
     */
    @Override
    @CallSuper
    public void onPageDestroy() {
        Logger.v("PageImpl : ", "onPageDestroy");
    }

    /**
     * When pages are reused
     *
     * @see Activity#onNewIntent(Intent)
     */
    @Override
    @CallSuper
    public void onPageReused() {
        Logger.v("PageImpl : ", "onPageReused");
    }

    @Override
    public int getLauncherType() {
        return annotationLauncherType == null ? PageLauncherType.LAUNCHER_DEFAULT_TYPE : annotationLauncherType;
    }

    @Override
    public boolean onBackPressed() {
        return getPageParent().removePage(this);
    }

    /**
     * @return real-view in page ; if it is null , the system reads the annotated values
     */
    @NonNull
    @Override
    public final View providerContentView() {
        if (contentViewHolder == null) {
            synchronized (this) {
                if (contentViewHolder == null) {
                    View contentView = getContentView(parent);

                    // if subclass don't override method
                    if (contentView == null) {
                        if (annotationLayoutId == null) {
                            throw new RuntimeException("please implement method 'getContentView(PageParent)' or  adding 'InjectPageLayoutRes' annotations");
                        }

                        // get value from annotation
                        contentViewHolder = LayoutInflater.from(parent).inflate(annotationLayoutId, parent.getPageManager(), false);
                    } else {
                        // "getContentView()" method takes precedence
                        contentViewHolder = contentView;
                    }

                    // create - view
                    onPageViewInject();
                }
            }
        }

        //noinspection ConstantConditions
        return contentViewHolder;
    }

    /**
     * @return the layout-params put in parent-view , if it is null , the system will take the default value (read from xml)
     */
    @NonNull
    @Override
    public final ViewGroup.LayoutParams providerIntegrateParams() {
        if (layoutParamHolder == null) {
            synchronized (this) {
                if (layoutParamHolder == null) {
                    layoutParamHolder = providerContentView().getLayoutParams();
                }
            }
        }

        //noinspection ConstantConditions
        return layoutParamHolder;
    }


    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageParent}
     */
    @NonNull
    @Override
    public PageParent getPageParent() {
        return parent;
    }

    /**
     * @return page's style
     */
    @Override
    public int getPageAppearanceType() {
        return annotationAppearanceType == null ? PageAppearance.PAGE_APPEARANCE_FULLSCREEN : annotationAppearanceType;
    }
}
