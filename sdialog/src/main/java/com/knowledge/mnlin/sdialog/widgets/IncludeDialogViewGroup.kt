package com.knowledge.mnlin.sdialog.widgets

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.knowledge.mnlin.sdialog.R
import com.knowledge.mnlin.sdialog.interfaces.ProvideIncludeDialogVGAnimator
import com.knowledge.mnlin.sdialog.interfaces.SimulateDialogInterface
import com.knowledge.mnlin.sdialog.utils.filterAlso
import com.knowledge.mnlin.sdialog.utils.getActivityFromView
import com.knowledge.mnlin.sdialog.utils.onTrue

/**
 * Created on 2019/3/23  18:33
 * function : 可包含dialog的窗口(dialog为模拟的view)
 *
 *
 * 默认第一个Child为主布局,其他child为dialog
 *
 *
 * 可进行的操作包括:
 * 1.弹出第n个窗口,同时屏蔽除窗口外所有控件的点击事件
 * 2.点击返回键,如果有打开的窗口,则先关闭窗口
 * 3.可设置弹出框后的背景色
 *
 * 4.可设置弹出框显示和隐藏的动画效果
 * 5.允许同时弹出多个dialog
 * 6.弹窗期间允许数据进行刷新(跟一般的view类似)
 *
 * @author mnlin
 */

class IncludeDialogViewGroup : FrameLayout, View.OnClickListener {
    /**
     * 记录当前装载的dialog
     */
    private var allDialogs: MutableList<SimulateDialogInterface<*, *>> = mutableListOf()

    /**
     * 装载 dialog 对应的 animator对象,可执行动画
     */
    private var dialogAnimators: HashMap<SimulateDialogInterface<*, *>, ProvideIncludeDialogVGAnimator> = hashMapOf()

    /**
     * 遮罩布局
     */
    private var shadeView = FrameLayout(context)

    /**
     * dimen颜色(遮罩颜色值)
     */
    var maskColor: Int = defaultMaskColor

    /**
     * 当点击 dialog "外部" 时候,是否关闭弹框
     */
    var closeOnClickOut: Boolean = defaultCloseOnClickOut

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    /**
     * 布局加载完成后,再进行事件或其他逻辑(此时务必保证Activity的布局已经进行了加载)
     */
    @CallSuper
    override fun onFinishInflate() {
        super.onFinishInflate()

        // 当Preview 时,不影响真正显示效果
        if(!isInEditMode){
            //当布局结束后,标记到context,当前界面有可能弹出dialog
            getActivityFromView().findViewById<View>(android.R.id.content).setTag(R.id.id_include_dialog_view_group, this)

            //同时添加一个中间的布局View,用作遮挡板等功效
            shadeView.setBackgroundColor(Color.TRANSPARENT)
            addView(shadeView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    /**
     * 如果处在处于显示状态的view
     */
    fun existDialogOpened(): Boolean {
        //避免多个线程调用导致出现异常
        synchronized(this) {
            return allDialogs.any { it.generateView().parent != null }
        }
    }

    /**
     * 显示某个/些dialog,默认不显示所有
     *
     * 通过instance或者class,或者可以显示所有
     *
     * [instance] dialog 对象引用
     * [clazz] 显示class 为 clazz 的dialog
     * [showAll] true 表示显示所有被注册的dialog
     * [animator] 显示/隐藏 dialog 时的动画逻辑,空表示不执行动画
     */
    fun showDialogs(
        instance: SimulateDialogInterface<*, *>? = null,
        clazz: Class<SimulateDialogInterface<*, *>>? = null,
        showAll: Boolean = false,
        animator: ProvideIncludeDialogVGAnimator? = null
    ) {
        allDialogs.asSequence()
            .filter {
                showAll
                        || it === instance
                        || clazz?.isAssignableFrom(it::class.java) ?: false
            }.map {
                //第一,添加布局,使用动画效果
                if (animator != null) {
                    //保存动画对象
                    dialogAnimators[it] = animator

                    //调用方法开启动画
                    animator.startAnimator(this@IncludeDialogViewGroup, it) {
                        addView(it.generateView(), it.generateLayoutParams())
                    }
                } else {
                    //移除之前的动画对象
                    dialogAnimators.remove(it)

                    //无动画添加 view 进行显示
                    addView(it.generateView(), it.generateLayoutParams())
                }
            }
            .toList()
            .filterAlso({ it.isNotEmpty() }) {
                //第二,添加dimenColor
                shadeView.setBackgroundColor(maskColor)

                //第三,添加点击事件
                shadeView.setOnClickListener(this)
            }
    }


    /**
     * 关闭已经打开的布局(必须制定布局,或者关闭所有,默认关闭所有)
     *
     * 1.关闭被模拟的布局
     * 2.修改自身的状态信息,包括被屏蔽的点击事件,以及背景颜色等等
     *
     * [instance] dialog 对象引用
     * [clazz] 显示class 为 clazz 的dialog
     * [closeAll] true 表示显示所有被注册的dialog
     * [useAnimator] 是否使用动画效果来关闭,当为true且之前开启动画时,即[showDialogs]方法的 [showDialogs#animator)]
     *
     * @return  返回被关闭的dialog数量
     */
    fun closeDialogsOpened(
        instance: SimulateDialogInterface<*, *>? = null,
        clazz: Class<SimulateDialogInterface<*, *>>? = null,
        closeAll: Boolean = true,
        useAnimator: Boolean = true
    ): Int {
        val removeMask = {
            //判断,如果当前所有dialog都已关闭,则修改颜色值,取消遮罩监听
            if (!existDialogOpened()) {
                //第二,移除背景dimenColor
                shadeView.setBackgroundColor(Color.TRANSPARENT)

                //第三,移除监听事件
                shadeView.setOnClickListener(null)
                shadeView.isClickable = false
            }
        }

        return allDialogs.filter {
            closeAll
                    || it === instance
                    || clazz?.isAssignableFrom(it::class.java) ?: false
        }.filter {
            (it.generateView().parent != null).onTrue {
                //第一,移除布局
                if (useAnimator && dialogAnimators.containsKey(it)) {
                    //是否使用动画
                    dialogAnimators[it]!!.stopAnimator(this@IncludeDialogViewGroup, it) {
                        removeView(it.generateView())
                        removeMask()
                    }
                } else {
                    removeView(it.generateView())
                    removeMask()
                }
            }
        }.size
    }

    /**
     * 注册 dialog
     *
     * [dialog] 用于操作的view
     */
    fun registerDialog(dialog: SimulateDialogInterface<*, *>) {
        if (allDialogs.indexOf(dialog) == -1) {
            allDialogs.add(dialog)

            //对dialog人为添加点击事件,防止点击dialog时,触发shadeView布局导致弹窗关闭
            dialog.generateView().setOnClickListener {
                //不处理任何时间,只是防止点击dialog时被关闭
            }
        }
    }

    /**
     * 注销 某个dialog ,注销后,将移除,不是关闭,默认注销所有dialog
     *
     * [instance] dialog 引用
     * [clazz] 通过类型移除
     * [logoutAll]  移除所有
     *
     * @return 移除了多少个dialog
     */
    fun logoutDialogs(
        instance: SimulateDialogInterface<*, *>? = null,
        clazz: Class<SimulateDialogInterface<*, *>>? = null,
        logoutAll: Boolean = true
    ): Int {
        return allDialogs.asSequence()
            .filter {
                logoutAll
                        || it === instance
                        || clazz?.isAssignableFrom(it::class.java) ?: false
            }.map {
                allDialogs.remove(it)
            }.toList().size
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        //当dialog可见时,该点击事件用来防止误点到布局底部的视图
        if (existDialogOpened() && closeOnClickOut) {
            closeDialogsOpened()
        }
    }

    /**
     * 禁止自身设置点击事件
     */
    @Deprecated(message = "not support", replaceWith = ReplaceWith("super.setOnClickListener"))
    override fun setOnClickListener(l: OnClickListener?) {
        TODO()
    }

    /**
     * 默认伴生类,放置静态的 配置选项
     */
    companion object {
        /**
         * 默认的遮罩颜色
         */
        var defaultMaskColor: Int = Color.parseColor("#40000000")

        /**
         * 点击 外部时,是否默认的关闭弹出框
         */
        var defaultCloseOnClickOut: Boolean = true
    }
}
