package com.knowledge.mnlin.simulatedialog.plugins

import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType
import java.lang.annotation.Inherited

/**************************************
 * function : auto-inject-annotation
 *
 * Created on 2018/6/30  18:37
 * @author mnlin
 **************************************/

/**
 * function : inject layout
 *
 * Created on 2018/6/30  17:29
 * @author mnlin
 */
@Inherited
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectPageLayoutRes(@LayoutRes val layoutResId: Int)

/**
 * function : inject page-appearance-type
 *
 * Created on 2019/10/15  17:48
 * @author 80270427
 */
@Inherited
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectPageAppearanceType(
    @IntRange(
        from = PageAppearance.PAGE_APPEARANCE_FULLSCREEN.toLong(),
        to = PageAppearance.PAGE_APPEARANCE_PART.toLong()
    ) val pageAppearanceType: Int
)

/**
 * function : inject page-launcher-type
 *
 * Created on 2019/10/15  17:48
 * @author 80270427
 */
@Inherited
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectPageLauncherType(
    @IntRange(
        from = PageLauncherType.LAUNCHER_DEFAULT_TYPE.toLong(),
        to = PageLauncherType.LAUNCHER_SINGLE_TASK.toLong()
    ) val pageLauncherType: Int
)


////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////  Less used ////////
////////////////////////////////////////////////////////////////////////////////////////

/**
 * function : inject title
 *
 * Created on 2018/8/6  15:22
 * @author mnlin
 */
@Inherited
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectPageTitle(@StringRes val titleRes: Int = 0, val title: String = "")

/**
 * function : inject menu
 *
 * Created on 2018/6/30  18:34
 * @author mnlin
 */
@Inherited
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectPageMenuRes(@MenuRes val menuResId: Int)

