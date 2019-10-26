package com.knowledge.mnlin.simulatedialog

import android.support.design.widget.TabLayout
import com.example.page_annotation.PageEnterPoint
import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.simulatedialog.adapters.PageAdapter
import com.knowledge.mnlin.simulatedialog.agents.PageTransAnimAgent
import com.knowledge.mnlin.simulatedialog.animations.PageTABottomRaise
import com.knowledge.mnlin.simulatedialog.animations.PageTAScale
import com.knowledge.mnlin.simulatedialog.annotations.InjectPageLauncherType
import com.knowledge.mnlin.simulatedialog.annotations.InjectPageLayoutRes
import com.knowledge.mnlin.simulatedialog.core.PageImpl
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType
import com.knowledge.mnlin.simulatedialog.interfaces.PageTransAnimation
import kotlinx.android.synthetic.main.activity_multi_fragment.view.*
import kotlinx.android.synthetic.main.fragment_second.view.*

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author 80270427
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
 * @author 80270427
 */
@InjectPageLayoutRes(layoutResId = R.layout.activity_multi_fragment)
class SixPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()

        contentView.apply {
            var adapter = PageAdapter(listOf(FirstPage(), SecondPage(), ThirdPage(), FourthPage()))
            vp_pager.adapter = adapter
            tl_tab.setupWithViewPager(vp_pager)

            //bottom-navigation -> view-pager
            bv_navigation.setOnNavigationItemSelectedListener {
                if (vp_pager.currentItem != it.order) {
                    vp_pager.setCurrentItem(it.order, true)
                }
                true
            }

            //tab-layout -> bottom-navigation
            tl_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    //
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    //
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    p0?.let {
                        bv_navigation.selectedItemId = arrayOf(
                            R.id.action_one,
                            R.id.action_two,
                            R.id.action_three,
                            R.id.action_four
                        )[it.position]
                    }
                }
            })
        }
    }

    override fun generateTransAnimation(): PageTransAnimation? {
        return PageTABottomRaise.getSingleInstance()
    }
}
