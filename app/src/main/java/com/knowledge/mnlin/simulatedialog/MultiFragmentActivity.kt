package com.knowledge.mnlin.simulatedialog

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.knowledge.mnlin.simulatedialog.sfragment.DefaultSimulateFragmentImpl
import com.knowledge.mnlin.simulatedialog.sfragment.SimulateFragmentAdapter
import com.knowledge.mnlin.simulatedialog.sfragment.SimulateFragmentInterface
import kotlinx.android.synthetic.main.activity_multi_fragment.*

/**
 * function : 多个Fragment,通过ViewPager  方式进行组合
 *
 * Created on 2019/4/11  14:52
 * @author mnlin
 */
class MultiFragmentActivity : AppCompatActivity() {

    /**
     * adapter
     */
    private lateinit var adapter: SimulateFragmentAdapter<SimulateFragmentInterface>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_fragment)

        // tab-layout + view-pager
        adapter =
                SimulateFragmentAdapter(mutableListOf<SimulateFragmentInterface>(
                    DefaultSimulateFragmentImpl<MultiFragmentActivity>(this, vp_pager, R.layout.fragment_first).apply {
                    },
                    DefaultSimulateFragmentImpl<MultiFragmentActivity>(this, vp_pager, R.layout.fragment_second).apply {
                    },
                    DefaultSimulateFragmentImpl<MultiFragmentActivity>(this, vp_pager, R.layout.fragment_third).apply {
                    },
                    DefaultSimulateFragmentImpl<MultiFragmentActivity>(this, vp_pager, R.layout.fragment_fourth).apply {
                    }
                ), mutableListOf("第一", "第二", "第三", "第四"))
        vp_pager.adapter = this.adapter
        tl_tab.setupWithViewPager(vp_pager)


        //底部导航栏: 被点击 -> 通知 view-pager 滑动
        bv_navigation.setOnNavigationItemSelectedListener {
            if (vp_pager.currentItem != it.order) {
                vp_pager.setCurrentItem(it.order, true)
            }
            true
        }

        //view-pager -> bottom-navigation;主动触发刷新Fragment
        tl_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                // 重复点击同一个tab,此种情况不予考虑
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                adapter.datas
                        .getOrNull(p0?.position ?: -1)
                        ?.onFragmentStatusChanged(SimulateFragmentInterface.STATUS_DISAPPEAR)
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.let {
                    bv_navigation.selectedItemId = arrayOf(
                            R.id.action_one,
                            R.id.action_two,
                            R.id.action_three,
                            R.id.action_four
                    )[it.position]

                    adapter.datas
                        .getOrNull(p0.position)
                        ?.onFragmentStatusChanged(SimulateFragmentInterface.STATUS_APPEAR)
                }
            }
        })

        //
        vp_pager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                adapter.datas
                    .getOrNull(p0)
                    ?.onFragmentStatusChanged(SimulateFragmentInterface.STATUS_APPEAR)
            }
        })
    }
}




