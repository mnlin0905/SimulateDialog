package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.IntRange;

/**
 * Created on 2019/10/14  14:39
 * function : the type when put in page
 *
 * @author mnlin0905@gmail.com
 */
public interface PageLauncherType {
    /**
     * allow multiple pages to be identical
     */
    int LAUNCHER_DEFAULT_TYPE = 0x00000000;

    /**
     * @see {@link android.content.Intent#FLAG_ACTIVITY_SINGLE_TOP}
     */
    int LAUNCHER_SINGLE_TOP = 0x00000001;

    /**
     * @see {@link android.content.Intent#FLAG_ACTIVITY_CLEAR_TOP}
     */
    int LAUNCHER_SINGLE_TASK = 0x00000002;

    /**
     * @return page's launcher type
     */
    @IntRange(from = LAUNCHER_DEFAULT_TYPE, to = LAUNCHER_SINGLE_TASK)
    int getLauncherType();
}
