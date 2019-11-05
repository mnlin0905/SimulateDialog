package com.knowledge.mnlin.test.sfragment

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * function : 适配器,用于 view-pager 加载 SimulateFragmentInterface
 *
 * Created on 2019/4/11  17:33
 * @author mnlin
 */
class SimulateFragmentAdapter<T : SimulateFragmentInterface> : PagerAdapter {
    /**
     * 被模拟的 fragments
     */
    var datas: MutableList<T>

    /**
     * 标题
     */
    var titles: MutableList<String>? = null

    /**
     * 需要保持一个 SimulateFragmentInterface 列表,用于显示内容布局
     */
    constructor(datas: MutableList<T>) : super() {
        this.datas = datas
    }

    /**
     * 需要保持一个 SimulateFragmentInterface 列表,用于显示内容布局
     */
    constructor(datas: MutableList<T>, titles: MutableList<String>) : super() {
        this.datas = datas
        this.titles = titles
    }

    /**
     * 判断view 和 item-view 是否 是同一个
     */
    @Suppress("UNCHECKED_CAST")
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return datas.size
    }

    /**
     * 将创建好的布局,添加到 container 中
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return datas[position].generateView().also {
            container.addView(it)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(datas[position].generateView())
    }

    /**
     * 返回title,为和 tab-layout 等控件 做绑定
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.getOrNull(position)
    }
}