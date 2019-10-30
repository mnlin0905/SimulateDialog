package com.knowledge.mnlin.page.interfaces;

import android.support.annotation.IntRange;

/**
 * Created on 2019/10/15  11:00
 * function : page's style
 *
 * @author mnlin0905@gmail.com
 */
public interface PageAppearance {
    /**
     * fullscreen page,can be added directly
     */
    int PAGE_APPEARANCE_FULLSCREEN = 0x00000000;

    /**
     * Page with transparent background or window type
     */
    int PAGE_APPEARANCE_PART = 0x00000001;

    /**
     * @return page's style
     */
    @IntRange(from = PAGE_APPEARANCE_FULLSCREEN, to = PAGE_APPEARANCE_PART)
    int getPageAppearanceType();
}
