package com.knowledge.mnlin.simulatedialog

import android.content.Intent
import android.os.Bundle
import com.knowledge.mnlin.sdialog.base.SDActivity
import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.simulatedialog.R.id.*
import kotlinx.android.synthetic.main.activity_menu_route.*


/**
 * function : 入口activity,跳转到不同的界面中
 *
 * Created on 2019/4/3  15:16
 * @author mnlin
 */
class MenuRouteActivity : SDActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_route)

        tv_one.dOnClick {
            startActivity(Intent(this@MenuRouteActivity,SimpleFragmentActivity::class.java))
        }

        tv_two.dOnClick {
            startActivity(Intent(this@MenuRouteActivity,MultiFragmentActivity::class.java))
        }

        tv_three.dOnClick {
            startActivity(Intent(this@MenuRouteActivity,MainActivity::class.java))
        }
    }
}
