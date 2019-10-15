package com.knowledge.mnlin.simulatedialog

import com.knowledge.mnlin.simulatedialog.base.PageImpl
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageAppearanceType
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageLayoutRes

/**************************************
 * function : test activity
 *
 * Created on 2019/10/15  21:46
 * @author 80270427
 **************************************/

@InjectPageLayoutRes(layoutResId = R.layout.fragment_first)
class FirstPage : PageImpl()

@InjectPageLayoutRes(layoutResId = R.layout.fragment_two)
@InjectPageAppearanceType(pageAppearanceType = PageAppearance.PAGE_APPEARANCE_PART)
class SecondPage : PageImpl()

@InjectPageLayoutRes(layoutResId = R.layout.fragment_three)
@InjectPageAppearanceType(pageAppearanceType = PageAppearance.PAGE_APPEARANCE_FULLSCREEN)
class ThirdPage : PageImpl()

@InjectPageLayoutRes(layoutResId = R.layout.fragment_four)
@InjectPageAppearanceType(pageAppearanceType = PageAppearance.PAGE_APPEARANCE_PART)
class FourthPage : PageImpl()

@InjectPageLayoutRes(layoutResId = R.layout.fragment_fifth)

class FifthPage : PageImpl()
