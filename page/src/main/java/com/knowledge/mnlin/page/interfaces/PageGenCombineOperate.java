package com.knowledge.mnlin.page.interfaces;

import android.annotation.Nullable;

/**
 * Created on 2019/10/30  10:11
 * function : Provide unified management class interface call for automatically generated code
 * <p>
 * the combine of generate files for someone page (gen files)
 *
 * @author mnlin0905@gmail.com
 */
public interface PageGenCombineOperate {
    /**
     * auto inject (user can provider more than one animation)
     */
    @Nullable
    default PageTransAnimation getPageTransAnimation() {
        return null;
    }

    /**
     * auto inject {@link PageAppearance}
     */
    default int getPageAppearanceType() {
        return PageAppearance.PAGE_APPEARANCE_FULLSCREEN;
    }

    /**
     * auto inject {@link PageLauncherType}
     */
    default int getPageLauncherType() {
        return PageLauncherType.LAUNCHER_DEFAULT_TYPE;
    }

    /**
     * auto inject {@link PageLauncherType}
     *
     * @return  layoutResId ;  -1 (Indicates no assignment)
     */
    default int getPageLayoutRes() {
        return -1;
    }
}
