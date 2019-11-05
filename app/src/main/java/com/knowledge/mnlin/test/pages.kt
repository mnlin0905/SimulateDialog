package com.knowledge.mnlin.test

import com.google.android.material.tabs.TabLayout
import com.knowledge.mnlin.page.adapters.PageAdapter
import com.knowledge.mnlin.page.animations.PageTABottomRaise
import com.knowledge.mnlin.page.core.PageImpl
import com.knowledge.mnlin.page.factory.PageTransAnimKeyConst
import com.knowledge.mnlin.page.interfaces.PageAppearance
import com.knowledge.mnlin.page.interfaces.PageLauncherType
import com.knowledge.mnlin.page.interfaces.PageTransAnimation
import com.knowledge.mnlin.page_annotation.annotations.*
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
@InjectPageLauncherType(launcherType = PageLauncherType.LAUNCHER_SINGLE_TASK)
@InjectPageAppearanceType(appearanceType = PageAppearance.PAGE_APPEARANCE_FULLSCREEN)
@InjectPageTransAnim(animations = [PageTransAnimKeyConst.PageTAScale, PageTransAnimKeyConst.PageTABottomRaise])
class FirstPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(SecondPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_second)
@InjectPageTransAnim(animations = [ PageTransAnimKeyConst.PageTATopDown])
class SecondPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageContext.addPage(ThirdPage())
        }
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
                PageAdapter(listOf(
                    FirstPage(),
                    SecondPage(),
                    ThirdPage(),
                    FourthPage()
                ))
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
