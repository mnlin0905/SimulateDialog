package com.knowledge.mnlin.test.sfragment

import android.annotation.LayoutRes
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.knowledge.mnlin.test.R
import com.knowledge.mnlin.sdialog.utils.getActivityFromView

/**************************************
 * function : 该Fragment 可直接 嵌入 Activity 中,布局随后懒加载完成
 *
 * Created on 2019/4/10  15:46
 * @author mnlin
 **************************************/

open class DefaultSimulateFragmentImpl<T : Activity> : FrameLayout, SimulateFragmentInterface,
    FragmentStatusChangedListener {
    /**
     * 保持布局不变(该contentView根部局包含了自身的FrameLayout)
     *
     * 布局在初始化前可能为null
     */
    private lateinit var contentView: View

    /**
     * layout 布局
     *
     * 可能为null,但需保证 contentView 不会为null
     */
    private var layoutResId: Int? = null

    /**
     * 上一个状态值
     */
    private var currentFragmentStatus = SimulateFragmentInterface.STATUS_UNKNOWN

    /**
     * 状态监测监听
     */
    private var listener: FragmentStatusChangedListener? = this

    /**
     * 是否是第一次显示某个tab
     */
    private var isFirstAppear = true

    constructor(context: Context) : this(context, attrs = null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //获取需要加载的布局
        val array = context.obtainStyledAttributes(attrs, R.styleable.DefaultSimulateFragmentImpl)
        val layoutId = array.getResourceId(
            R.styleable.DefaultSimulateFragmentImpl_DefaultSimulateFragmentImpl_layout,
            -1
        )

        //如果有放置 Fragment 的layout文件布局,则进行加载
        if (layoutId != -1) {
            layoutResId = layoutId
        } else {
            print("layoutResid为null")
        }

        array.recycle()
    }

    /**
     * 供其他非 xml 使用 来创建布局文件
     *
     * 此时需要直接创建好布局,来保证 view 加载完成
     *
     * 同时,需要更改根布局的继承属性
     *
     * @param Container 容器布局,即加载 该Fragment 的布局
     */
    constructor(
        context: Context,
        Container: ViewGroup,
        @LayoutRes layoutRes: Int? = null,
        childView: View? = null
    ) : super(context) {
        contentView = when {
            layoutRes != null -> LayoutInflater.from(context).inflate(layoutRes, Container, false)
            childView != null -> childView
            else -> TODO("layoutRes 和 childView 不能同时为 null")
        }

        //通过代码创建 Fragment 时,layoutResId 不进行赋值,确保其他方法,如 onFragmentCreate 不会出错
        //通过代码创建 Fragment 时,根部局不再使用 本  FrameLayout,而是 layout 自己定义的布局
    }

    final override fun generateView(): View {
        return contentView
    }

    final override fun getCurrentFragmentStatus(): Int {
        return currentFragmentStatus
    }

    /**
     * 获取宿主的 activity
     */
    final fun generateActivity(): T {
        return generateView().getActivityFromView() as T
    }

    /**
     * 当界面可见或者其他情况时,处理具体事件
     */
    final override fun onFragmentStatusChanged(status: Int): Boolean {
        currentFragmentStatus = status

        //分开处理不同的逻辑事件
        return when (currentFragmentStatus) {
            SimulateFragmentInterface.STATUS_UNKNOWN -> false
            SimulateFragmentInterface.STATUS_APPEAR ->
                if (isFirstAppear) {
                    isFirstAppear = false
                    listener?.onFragmentFirstAppear() ?: false
                } else {
                    listener?.onFragmentRepeatAppear() ?: false
                }
            SimulateFragmentInterface.STATUS_DISAPPEAR ->
                listener?.onFragmentDisappear() ?: false
            else -> TODO("unknown status / unreachable")
        }
    }

    final override fun onFinishInflate() {
        super.onFinishInflate()

        //当布局被加载时,才会 考虑 创建 调用 create 方法
        if (!isInEditMode) {
            contentView = layoutResId?.let {
                LayoutInflater.from(getActivityFromView()).inflate(it, this, true)
            } ?: contentView
        } else {
            layoutResId?.let {
                LayoutInflater.from(this.context).inflate(it, this, true)
            }
        }
    }
}