package com.knowledge.mnlin.simulatedialog

import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.simulatedialog.core.PageImpl
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageLayoutRes
import kotlinx.android.synthetic.main.fragment_second.view.*

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author 80270427
 **************************************/

@InjectPageLayoutRes(layoutResId = R.layout.fragment_first)
class FirstPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageParent.addPage(SecondPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_second)
//@InjectPageLauncherType(pageLauncherType = PageLauncherType.LAUNCHER_SINGLE_TASK)
class SecondPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageParent.addPage(ThirdPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_third)
class ThirdPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageParent.addPage(FourthPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_fourth)
class FourthPage : PageImpl() {
    override fun onPageViewInject() {
        super.onPageViewInject()
        contentView.tv_.dOnClick {
            pageParent.addPage(FifthPage())
        }
    }
}

@InjectPageLayoutRes(layoutResId = R.layout.fragment_fifth)
class FifthPage : PageImpl() {
    /**
     * page visible and interactive
     */
    override fun onPageActive() {
        super.onPageActive()

        pageParent.insertPageRecord(4, FirstPage())
    }
}
