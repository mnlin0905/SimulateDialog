package com.knowledge.mnlin.page.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.knowledge.mnlin.page_annotation.annotations.PageTag;
import com.knowledge.mnlin.page.animations.PageTABottomRaise;
import com.knowledge.mnlin.page.animations.PageTANoEffect;
import com.knowledge.mnlin.page.animations.PageTARightLeftTranslate;
import com.knowledge.mnlin.page.animations.PageTAScale;
import com.knowledge.mnlin.page.animations.PageTATopDown;
import com.knowledge.mnlin.page.annotations.InjectPageAppearanceType;
import com.knowledge.mnlin.page.annotations.InjectPageLauncherType;
import com.knowledge.mnlin.page.annotations.InjectPageLayoutRes;
import com.knowledge.mnlin.page.configs.PageConfigs;
import com.knowledge.mnlin.page.gen_interfaces.PageGenCombineOperate;
import com.knowledge.mnlin.page.interfaces.FullPage;
import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page.interfaces.PageAppearance;
import com.knowledge.mnlin.page.interfaces.PageCallback;
import com.knowledge.mnlin.page.interfaces.PageLauncherType;
import com.knowledge.mnlin.page.interfaces.PageLifeCycle;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;
import com.knowledge.mnlin.page.interfaces.PartPage;
import com.knowledge.mnlin.page.plugins.PagePlugin;
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
@PageTag
public abstract class PageImpl implements PartPage, FullPage {
    private final String TAG = getClass().getSimpleName();

    /**
     * help-class for generate files
     */
    @NotNull
    private PageGenCombineOperate combineOperateForPage;

    /**
     * animation agent
     */
    @NotNull
    private PageTransAnimation transAnimationAgent;

    /**
     * simulated masking effect
     */
    @Nullable
    private ShadeMaskView maskForPart;

    /**
     * page's  current gradation
     */
    @IntRange(from = PageLifeCycle.PAGE_GRADATION_IDEL, to = PageLifeCycle.PAGE_GRADATION_DETACH_FROM_PARENT)
    private int pageCurrentGradation;

    /**
     * holder a sInstance of content-view
     */
    @Nullable
    private volatile View contentViewHolder;

    /**
     * holder a sInstance of layout-param
     */
    @Nullable
    private volatile ViewGroup.LayoutParams layoutParamHolder;

    /**
     * annotation values
     */
    @Nullable
    private Integer annotationLayoutId;

    @Nullable
    private Integer annotationAppearanceType;

    @Nullable
    private Integer annotationLauncherType;

    /**
     * parent agent,To restrict page's access
     */
    private PageContext pageContext;

    /**
     * if attach to page-manager,this value is not -1
     */
    private int indexInPageStack = -1;

    public PageImpl() {
        pageContext = new PageContext(PageParent.sInstance);

        // judge param and init annotation value
        combineOperateForPage = PagePlugin.findCombineOperateForPage(this);

        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof InjectPageLayoutRes) {
                annotationLayoutId = ((InjectPageLayoutRes) annotation).layoutResId();
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
     * animation strategy if {@link com.knowledge.mnlin.page.interfaces.PageMethodReversal#generateTransAnimation()} method not implemented
     */
    private void adoptAnimationStrategy() {
        if (getPageAppearanceType() == PageAppearance.PAGE_APPEARANCE_PART) {
            if (providerIntegrateParams() instanceof FrameLayout.LayoutParams) {
                int gravity = ((FrameLayout.LayoutParams) providerIntegrateParams()).gravity;
                if (gravity == Gravity.CENTER) {
                    // if gravity is center
                    transAnimationAgent = PageTAScale.getSingleInstance();
                } else if ((gravity & Gravity.TOP) == Gravity.TOP) {
                    transAnimationAgent = PageTATopDown.getSingleInstance();
                } else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                    transAnimationAgent = PageTABottomRaise.getSingleInstance();
                } else {
                    transAnimationAgent = PageTARightLeftTranslate.getSingleInstance();
                }
            } else {
                transAnimationAgent = PageTABottomRaise.getSingleInstance();
            }
        } else {
            transAnimationAgent = PageTARightLeftTranslate.getSingleInstance();
        }
    }

    /**
     * @return get the animator agent
     */
    @SuppressWarnings("ConstantConditions")
    private PageTransAnimation getTranslateAnimationAgent() {
        // if it's null ,system will set default agent
        // Set corresponding switch animation for different page types
        // Delay initialization operation

        // provide by page-implement class
        if (transAnimationAgent == null) {
            transAnimationAgent = generateTransAnimation();
        }

        // get from gen-files
        if (transAnimationAgent == null) {
            transAnimationAgent = combineOperateForPage.getPageTransAnimation();
        }

        // auto inject default value
        if (transAnimationAgent == null) {
            if (PageConfigs.autoInjectPageTransAnim) {
                adoptAnimationStrategy();
            } else {
                transAnimationAgent = PageTANoEffect.getSingleInstance();
            }
        }

        return transAnimationAgent;
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
     * At this time, page is about to be removed from the view interface
     * <p>
     * or it's a background-page (only before part-page that's what happens)
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
        getPageContext().removePage(this);
        return true;
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
                    contentViewHolder = generateContentView();

                    // if subclass don't override method
                    if (contentViewHolder == null) {
                        if (annotationLayoutId == null) {
                            throw new RuntimeException("please implement method 'generateContentView()' or  adding 'InjectPageLayoutRes' annotations");
                        }

                        // get value from annotation
                        contentViewHolder = LayoutInflater.from(pageContext.getContext()).inflate(annotationLayoutId, pageContext.getPageManager(), false);
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
                    layoutParamHolder = generateLayoutParams();
                    if (layoutParamHolder == null) {
                        layoutParamHolder = providerContentView().getLayoutParams();
                        if (layoutParamHolder == null) {
                            layoutParamHolder = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        }
                    }
                }
            }
        }

        //noinspection ConstantConditions
        return layoutParamHolder;
    }


    /**
     * page must relative context(if has be added to parent)
     *
     * @return {@link PageContext}
     */
    @NonNull
    @Override
    public PageContext getPageContext() {
        return pageContext;
    }

    /**
     * @return index in {@link PageStackRecord} ; -1 if not attach to {@link PageManager}
     */
    @Override
    public int getIndexInStackRecord() {
        return pageContext.getAllPages().indexOf(this);
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
    public Page peekMaskForPart() {
        return maskForPart;
    }

    /**
     * inject host's mast-view
     *
     * @param mask mask-view
     */
    @Override
    public void injectMaskForPart(@NonNull Page mask) {
        if (mask instanceof ShadeMaskView) {
            maskForPart = (ShadeMaskView) mask;
            if (maskForPart.getHostPage() == null) {
                maskForPart.setHostPage(this);
            }
        }
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

    /**
     * @return {@link PartPage#setMaskDrawable(Drawable)}
     */
    @Nullable
    @Override
    public Drawable getMaskDrawable() {
        if (maskForPart != null) {
            return maskForPart.getDrawable();
        }
        return null;
    }

    /**
     * @param drawable mask-view-bg
     */
    @Override
    public void setMaskDrawable(Drawable drawable) {
        if (maskForPart != null) {
            maskForPart.setImageDrawable(drawable);
        }
    }

    @Override
    public void performPageRecordRightPush() {
        getTranslateAnimationAgent().onPageRecordRightPush(this);
    }

    @Override
    public void performPageRecordLeftInsert() {
        getTranslateAnimationAgent().onPageRecordLeftInsert(this);
    }

    @Override
    public void performPageRecordRightPop(PageCallback<Page> mustCalledWhenEndOrCancel) {
        getTranslateAnimationAgent().onPageRecordRightPop(this, mustCalledWhenEndOrCancel);
    }

    @Override
    public void performPageRecordLeftRemove(PageCallback<Page> mustCalledWhenEndOrCancel) {
        getTranslateAnimationAgent().onPageRecordLeftRemove(this, mustCalledWhenEndOrCancel);
    }

    @Override
    public void performReturnToAttachStatus() {
        getTranslateAnimationAgent().returnToAttachStatus(this);
    }

    @Override
    public void performReturnToDetachStatus() {
        getTranslateAnimationAgent().returnToDetachStatus(this);
    }

    @Override
    public void performCancelPageAnimation() {
        getTranslateAnimationAgent().cancelPageAnimation(this);
    }

    @Override
    public boolean dispatchOnNewIntent(@Nullable Intent newIntent) {
        return false;
    }
}
