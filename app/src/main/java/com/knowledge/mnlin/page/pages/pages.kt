package com.knowledge.mnlin.page.pages

import android.support.design.widget.TabLayout
import com.knowledge.mnlin.page.R
import com.knowledge.mnlin.page.adapters.PageAdapter
import com.knowledge.mnlin.page.agents.PageTransAnimAgent
import com.knowledge.mnlin.page.animations.PageTABottomRaise
import com.knowledge.mnlin.page.animations.PageTAScale
import com.knowledge.mnlin.page.annotations.InjectPageLauncherType
import com.knowledge.mnlin.page.annotations.InjectPageLayoutRes
import com.knowledge.mnlin.page.core.PageImpl
import com.knowledge.mnlin.page.interfaces.PageLauncherType
import com.knowledge.mnlin.page.interfaces.PageTransAnimation
import com.knowledge.mnlin.page_annotation.annotations.InjectPageTransAnim
import com.knowledge.mnlin.page_annotation.annotations.PageEnterPoint
import com.knowledge.mnlin.sdialog.utils.dOnClick
import kotlinx.android.synthetic.main.activity_multi_fragment.view.*
import kotlinx.android.synthetic.main.fragment_second.view.*

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author mnlin0905@gmail.com
 **************************************/

@PageEnterPoint
@InjectPageLayoutRes(layoutResId = R.layout.fragment_first)
@InjectPageLauncherType(pageLauncherType = PageLauncherType.LAUNCHER_SINGLE_TASK)
class FirstPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(SecondPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_second)
@InjectPageTransAnim(animations = ["123456", "789546"])
class SecondPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(ThirdPage())
        }
    }

    override fun generateTransAnimation(): PageTransAnimation? {
        return PageTransAnimAgent(
            PageTABottomRaise.getSingleInstance(),
            PageTAScale.getSingleInstance()
        )
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_third)
class ThirdPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(FourthPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_fourth)
class FourthPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(FifthPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_fifth)
class FifthPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            // pageContext.addPage(FirstPage())
            pageContext.addPage(SixPage())
        }
    }

    /**
     * page visible and interactive
     */
    override fun onPageActive() {
        super.onPageActive()

        //pageContext.addPage(FirstPage())

        //pageContext.insertPage(4, FirstPage())

        //pageContext.addPage(SixPage())

        //pageParent.removePage(pageParent.)
    }
}

/**
 * function : adapter-page
 *
 * Created on 2019/10/26  14:50
 * @author mnlin0905@gmail.com
 */
@InjectPageLayoutRes(layoutResId = R.layout.activity_multi_fragment)
class SixPage : PageImpl() {
    var bottomMenuIds: Array<Int> = arrayOf(
        R.id.action_one,
        R.id.action_two,
        R.id.action_three,
        R.id.action_four
    )

    override fun onPageViewInject() {
        super.onPageViewInject()

        contentView.apply {
            vp_pager.adapter =
                PageAdapter(listOf(FirstPage(), SecondPage(), ThirdPage(), FourthPage()))
            tl_tab.setupWithViewPager(vp_pager)

            //bottom-navigation -> view-pager
            bv_navigation.setOnNavigationItemSelectedListener {
                if (vp_pager.currentItem != it.order) {
                    vp_pager.setCurrentItem(it.order, true)
                }
                true
            }

            //tab-layout -> bottom-navigation
            tl_tab.addOnTabSelectedListener(
                object : TabLayout.ViewPagerOnTabSelectedListener(null) {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        bv_navigation.selectedItemId = bottomMenuIds[tab.position]
                    }
                })
        }
    }

    override fun generateTransAnimation(): PageTransAnimation? {
        return PageTABottomRaise.getSingleInstance()
    }
}
