package com.knowledge.mnlin.simulatedialog

import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.simulatedialog.annotations.InjectPageLauncherType
import com.knowledge.mnlin.simulatedialog.annotations.InjectPageLayoutRes
import com.knowledge.mnlin.simulatedialog.core.PageImpl
import com.knowledge.mnlin.simulatedialog.interfaces.PageLauncherType
import kotlinx.android.synthetic.main.fragment_second.view.*

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author 80270427
 **************************************/

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
        }
    }

    /**
     * page visible and interactive
     */
    override fun onPageActive() {
        super.onPageActive()

         pageContext.addPage(FirstPage())

        //pageContext.insertPage(4, FirstPage())

        //pageParent.removePage(pageParent.)
    }
}
