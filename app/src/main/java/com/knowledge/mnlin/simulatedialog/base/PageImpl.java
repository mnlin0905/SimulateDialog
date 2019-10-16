package com.knowledge.mnlin.simulatedialog.base;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.knowledge.mnlin.simulatedialog.interfaces.FullPage;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType;
import com.knowledge.mnlin.simulatedialog.interfaces.PageLifeCycle;
import com.knowledge.mnlin.simulatedialog.interfaces.PartPage;
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
public abstract class PageImpl implements PartPage, FullPage {
    private final String TAG = getClass().getCanonicalName();

    /**
     * simulated masking effect
     */
    @Nullable
    ShadeMaskView maskForPart;

    /**
     * page's  current gradation
     */
    @IntRange(from = PageLifeCycle.PAGE_GRADATION_IDEL, to = PageLifeCycle.PAGE_GRADATION_DETACH_FROM_PARENT)
    private int pageCurrentGradation;

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
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_PAGE_CREATE;
        Logger.v("%s : %s", TAG, "onPageCreate");
    }

    /**
     * page's content-view has set
     */
    @CallSuper
    @Override
    public void onPageViewInject() {
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_VIEW_INJECT;
        Logger.v("%s : %s", TAG, "onPageViewInject");
    }

    /**
     * page is visible
     */
    @Override
    @CallSuper
    public void onPageAttachToParent() {
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_ATTACH_TO_PARENT;
        Logger.v("%s : %s", TAG, "onPageAttachToParent");
    }

    /**
     * page visible and interactive
     */
    @Override
    public void onPageActive() {
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_ACTIVE;
        Logger.v("%s : %s", TAG, "onPageActive");
    }

    /**
     * Before the interface is invisible, it has entered the visible state again.
     * <p>
     * called after {@link PageLifeCycle#onPageActive()}
     */
    @Override
    public void onPageReResume() {
        Logger.v("%s : %s", TAG, "onPageReResume");
    }

    /**
     * The page is not visible or partially visible and cannot interact with the user
     */
    @Override
    public void onPageDeactive() {
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_DEACTIVE;
        Logger.v("%s : %s", TAG, "onPageDeactive");
    }

    /**
     * page be blocked
     */
    @Override
    @CallSuper
    public void onPageDetachFromParent() {
        pageCurrentGradation = PageLifeCycle.PAGE_GRADATION_DETACH_FROM_PARENT;
        Logger.v("%s : %s", TAG, "onPageDetachFromParent");
    }

    /**
     * When pages are reused
     *
     * @see Activity#onNewIntent(Intent)
     */
    @Override
    @CallSuper
    public void onPageReused() {
        Logger.v("%s : %s", TAG, "onPageReused");
    }

    /**
     * page current phase
     */
    @Override
    public int getCurrentGradation() {
        return pageCurrentGradation;
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
                    View contentView = generateContentView(parent);

                    // if subclass don't override method
                    if (contentView == null) {
                        if (annotationLayoutId == null) {
                            throw new RuntimeException("please implement method 'generateContentView(PageParent)' or  adding 'InjectPageLayoutRes' annotations");
                        }

                        // get value from annotation
                        contentViewHolder = LayoutInflater.from(parent).inflate(annotationLayoutId, parent.getPageManager(), false);
                    } else {
                        // "generateContentView()" method takes precedence
                        contentViewHolder = contentView;
                    }

                    // Event penetration not allowed
                    //noinspection ConstantConditions
                    contentViewHolder.setClickable(true);

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
        int result = annotationAppearanceType == null ? PageAppearance.PAGE_APPEARANCE_FULLSCREEN : annotationAppearanceType;

        // setting to "full" is not allowed if it is "part" type , prevent click event penetration
        if (result == PageAppearance.PAGE_APPEARANCE_FULLSCREEN &&
                (providerIntegrateParams().height != ViewGroup.LayoutParams.MATCH_PARENT ||
                        providerIntegrateParams().width != ViewGroup.LayoutParams.MATCH_PARENT)) {
            result = PageAppearance.PAGE_APPEARANCE_PART;
        }

        return result;
    }

    /**
     * @return host's mast-view
     */
    @Nullable
    @Override
    public ShadeMaskView peekMaskForPart() {
        return maskForPart;
    }

    /**
     * inject host's mast-view
     *
     * @param mask mask-view
     */
    @Override
    public void injectMaskForPart(@NonNull ShadeMaskView mask) {
        maskForPart = mask;
    }

    /**
     * Provide quick access to "contentViewHolder" for kotlin
     *
     * @return the contentViewHolder if it is not null
     * @throws RuntimeException if contentViewHolder is null
     */
    @NonNull
    public View getContentView() {
        if (contentViewHolder == null) {
            throw new RuntimeException("content-view is null");
        }
        //noinspection ConstantConditions
        return contentViewHolder;
    }
}
