package com.knowledge.mnlin.simulatedialog

import android.os.Bundle
import android.support.v7.widget.CardView
import android.widget.FrameLayout
import com.knowledge.mnlin.sdialog.animators.AlphaIDVGAnimatorImpl
import com.knowledge.mnlin.sdialog.base.SDActivity
import com.knowledge.mnlin.sdialog.utils.DefaultSimulateDialogImpl
import com.knowledge.mnlin.sdialog.utils.dOnClick
import com.knowledge.mnlin.sdialog.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_first.view.*
import kotlinx.android.synthetic.main.dialog_second.view.*

/**
 * function : 模拟dialog窗口,实现多个dialog启动,以及动画效果
 *
 * Created on 2019/4/3  15:16
 * @author mnlin
 */
class MainActivity : SDActivity() {

    /**
     * 两个弹出框 dialog 引用
     */
    private lateinit var firstDialog: DefaultSimulateDialogImpl<CardView, FrameLayout.LayoutParams>
    private lateinit var secondDialog: DefaultSimulateDialogImpl<FrameLayout, FrameLayout.LayoutParams>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //只有布局文件中存在  IncludeDialogViewGroup 布局,才可以 进行弹出框等逻辑处理
        includeDialog?.also { container ->
            //初始化弹出框控件
            firstDialog =
                    DefaultSimulateDialogImpl<CardView, FrameLayout.LayoutParams>(
                        container,
                        R.layout.dialog_first
                    ).also {
                        //对 dialog 中的控件,进行逻辑处理,或者添加点击等事件
                        it.generateView().apply {
                            //确认按钮
                            bt_submit.dOnClick {
                                toast("不会关闭弹窗")
                            }
                        }
                    }

            secondDialog =
                    DefaultSimulateDialogImpl<FrameLayout, FrameLayout.LayoutParams>(
                        container,
                        R.layout.dialog_second
                    ).also {
                        //对 dialog 中的控件,进行逻辑处理,或者添加点击等事件
                        it.generateView().apply {
                            //关闭按钮
                            bt_close.dOnClick {
                                secondDialog.close(true)
                                toast("将关闭弹窗")
                            }
                        }
                    }

            //添加点击事件

            bt_open_first.dOnClick {
                firstDialog.show()
            }
            bt_open_first_animator.dOnClick {
                firstDialog.show(AlphaIDVGAnimatorImpl(0f, 1f, 1000L, 500L))
            }

            bt_open_second.dOnClick {
                secondDialog.show()
            }
            bt_open_second_animator.dOnClick {
                secondDialog.show(AlphaIDVGAnimatorImpl(0f, 1f, 1000L, 500L))
            }

            bt_open_all.dOnClick {
                firstDialog.show()
                secondDialog.show()
            }
            bt_open_all_animator.dOnClick {
                firstDialog.show(AlphaIDVGAnimatorImpl(0f, 1f, 1000L, 500L))
                secondDialog.show(AlphaIDVGAnimatorImpl(0f, 1f, 1000L, 500L))
            }
        }
    }
}
