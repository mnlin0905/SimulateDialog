package com.knowledge.mnlin.page.annotations

import android.annotation.MenuRes
import android.annotation.StringRes
import java.lang.annotation.Inherited

/**************************************
 * function : boost frame function
 *
 * Created on 2019/10/23  22:01
 * @author mnlin0905@gmail.com
 **************************************/

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
