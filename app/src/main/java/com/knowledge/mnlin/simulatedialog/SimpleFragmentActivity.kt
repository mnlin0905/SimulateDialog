package com.knowledge.mnlin.simulatedialog

import android.os.Bundle
import com.knowledge.mnlin.sdialog.base.SDActivity
import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.sdialog.utils.toast
import com.knowledge.mnlin.simulatedialog.R.id.*
import kotlinx.android.synthetic.main.activity_test_fragment.*
import kotlinx.android.synthetic.main.dialog_first.view.*
import kotlinx.android.synthetic.main.dialog_second.view.*

/**
 * function : 测试Fragment 的activity
 *
 * Created on 2019/4/10  15:43
 * @author mnlin
 */
class SimpleFragmentActivity : SDActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fragment)

        ssf_one.generateView().apply {
            bt_submit.dOnClick {
                toast("消失")
                ssf_one.setInVisible()
            }
        }

        ssf_two.generateView().apply {
            bt_close.dOnClick {
                toast("窗口2")
                ssf_two.setInVisible()
            }
        }

        tv_show_one.dOnClick {
            ssf_one.setVisible()
        }

        tv_show_two.dOnClick {
            ssf_two.setVisible()
        }
    }
}
