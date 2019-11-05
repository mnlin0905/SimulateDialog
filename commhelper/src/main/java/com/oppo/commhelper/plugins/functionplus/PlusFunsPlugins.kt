package com.oppo.commhelper.plugins.functionplus

import android.annotation.ColorInt
import android.app.Activity
import android.content.ContextWrapper
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.widget.Toast

/**************************************
 * function : 向已有的类添加方法,避免子类的生成
 *
 * Created on 2018/7/2  11:38
 * @author mnlin
 **************************************/

/**
 * 添加onClick防抖动监听
 */
fun <T : View> T.dOnClick(action: T.() -> Unit) {
    this.setOnClickListener(object : DOnClickListener() {
        override fun doClick(v: View?) {
            action(this@dOnClick)
        }
    })
}

/**
 * 添加onClick防抖动监听
 */
fun <T : List<F>, F : View> T.dOnClick(action: F.() -> Unit) {
    this.map {
        it.dOnClick(action = action)
    }
}

/**
 * 添加onClick防抖动监听,[action]为需要执行的代码
 *
 * 如果[filter]返回true,则执行注册的事件,否则,不执行
 */
fun <T : View> T.dOnClick(filter: () -> Boolean, action: T.() -> Unit) {
    this.setOnClickListener(object : DOnClickListener() {
        override fun doClick(v: View) {
            if (filter()) action(this@dOnClick)
        }
    })
}

/**
 * 显示toast,可以返回自身
 */
@Suppress("NOTHING_TO_INLINE")
inline infix fun <T : Activity> T.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 专门重写Boolean方法,true时执行
 */
inline fun Boolean.onTrue(block: () -> Unit): Boolean {
    if (this) {
        block()
    }
    return this
}

/**
 * 专门重写Boolean方法,false时执行
 */
inline fun Boolean.onFalse(block: () -> Unit): Boolean {
    if (!this) {
        block()
    }
    return this
}

/**
 * 空操作:
 *
 * 可不提示,
 * 可提示文字,
 * 可TODO显示将来需要采用的操作,
 * 可编写好不会执行代码
 *
 * 测试阶段toast
 * 正事环境无操作
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> T.empty(comment: String? = null, TODO: String? = null, unreachable: T.() -> Unit = {}): T {
    return this
}

/**
 * 添加注释的方式
 */
inline infix fun <T : Any?> T.means(comment: String): T = empty(comment = comment)

/**
 * 将collection全部元素放某个默认值
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <F, T : MutableList<F>> T.initValue(value: F) {
    forEachIndexed { index, _ ->
        this[index] = value
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <F> Array<F>.initValue(value: F) {
    for (index in 0 until this.size) {
        this[index] = value
    }
}

/**
 * 只有filter通过,才能进行后面操作
 */
inline fun <T> T.filterRun(filter: T.() -> Boolean, block: T.() -> Any?): Any? {
    return if (filter()) {
        block()
    } else null
}

/**
 * 只有filter通过,才能进行后面操作
 */
inline fun <T> T.filterLet(filter: (it: T) -> Boolean, block: (it: T) -> Any?): Any? {
    return if (filter(this)) {
        block(this)
    } else null
}

/**
 * 只有filter通过,才能进行后面操作
 */
inline fun <T> T.filterAlso(filter: (it: T) -> Boolean, block: (it: T) -> Unit): T {
    if (filter(this)) {
        block(this)
    }

    return this
}

/**
 * 从view中获取Activity对象
 */
inline fun <T : View> T.getActivityFromView(): Activity {
    var activity = this.context
    while (activity is ContextWrapper) {
        if (activity is Activity) {
            return activity
        }
        activity = activity.baseContext
    }
    TODO("只能使用Activity来创建View")
}

/**
 * 创建自定义布局
 */
inline fun <T> T.customShapeDrawable(radius: Float, @ColorInt color: Int): ShapeDrawable {
    return object : ShapeDrawable(RoundRectShape(floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius), null, null)) {
        init {
            paint.color = color
        }
    }
}

inline fun <T : View> T.postV(crossinline callback: T.() -> Unit) {
    this.post {
        callback()
    }
}

/**
 * 专门重写Boolean方法,folse与ture时执行不同的lambda,返回相同的值
 */
inline fun <T> Boolean.kindAnyReturn(onFalse: () -> T, onTrue: () -> T): T {
    return if (this) onTrue() else onFalse()
}