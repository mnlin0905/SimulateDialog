package com.knowledge.mnlin.page.sfragment;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created on 2019/3/23  20:37
 * function : 模拟Fragment的控件
 *
 * 该模块跟 Fragment 其实原理类似,都相当于代理了一个View,然后在需要界面显示时,将view进行添加
 *
 * @author mnlin
 */
public interface SimulateFragmentInterface extends SimulateViewInterface {
    /**
     * 该Fragment 界面变化可能有三种状态:
     * <p>
     * (未知的状态),显示,消失
     */
    int STATUS_UNKNOWN = 0;
    int STATUS_APPEAR = 1;
    int STATUS_DISAPPEAR = 2;

    /**
     * 获取布局
     *
     * <b>请确保: 多次调用该接口应当返回同一对象</b>
     *
     * @return View或者ViewGroup
     */
    @NonNull
    View generateView();

    /**
  * 当页面位置有变化时,通知该 Fragment 进行相关逻辑处理
     *
     * 一般来说,该方法中去处理复杂的网络请求等操作,切记,该方法只有在可见的时候才会被调用,以此来达到加速 activity 显示的目的
     *
     * @param status 当前 Fragment 状态发生改变
     * @return 该返回值主要用于在ViewPager中切换效果,true表示切换流程终止,界面将保持不变
     */
    boolean onFragmentStatusChanged(@IntRange(from = STATUS_APPEAR, to = STATUS_DISAPPEAR) int status);

    /**
     * @return 当前碎片所处的状态, 可能值为: {@link STATUS_UNKNOWN} {@link STATUS_APPEAR} {@link STATUS_DISAPPEAR}
     */
    int getCurrentFragmentStatus();

    @Override
    default void setGone() {
        generateView().setVisibility(View.GONE);
    }

    @Override
    default void setVisible() {
        generateView().setVisibility(View.VISIBLE);
    }

    @Override
    default void setInVisible() {
        generateView().setVisibility(View.INVISIBLE);
    }

    @Override
    default int judgeVisible() {
        return generateView().getVisibility();
    }
}
