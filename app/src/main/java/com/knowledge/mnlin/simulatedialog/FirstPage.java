package com.knowledge.mnlin.simulatedialog;

import com.knowledge.mnlin.simulatedialog.base.PageImpl;
import com.knowledge.mnlin.simulatedialog.interfaces.PageAppearance;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageLayoutRes;
import com.knowledge.mnlin.simulatedialog.plugins.InjectPageAppearanceType;

/**
 * Created on 2019/10/14  12:21
 * function : test
 *
 * @author mnlin0905@gmail.com
 */
@InjectPageLayoutRes(layoutResId = R.layout.fragment_first)
@InjectPageAppearanceType(pageAppearanceType = PageAppearance.PAGE_APPEARANCE_FULLSCREEN)
public class FirstPage extends PageImpl {

}
