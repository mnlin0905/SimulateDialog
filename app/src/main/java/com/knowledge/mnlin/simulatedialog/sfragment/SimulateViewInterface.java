package com.knowledge.mnlin.simulatedialog.sfragment;

/**
 * Created on 2019/4/11  14:26
 * function : 模仿view 可能存在的 三种 状态:{@link android.view.View.GONE} {@link android.view.View.INVISIBLE} {@link android.view.View.VISIBLE}
 *
 * @author mnlin
 */
public interface SimulateViewInterface {
    /**
     * hide,隐藏自身,隐藏时会引起布局重置 {@link android.view.View.GONE}
     */
    void setGone();

    /**
     * show  ,令 view 显示出来, {@link android.view.View.VISIBLE}
     */
    void setVisible();

    /**
     * invisible ,不可见,但会暂用原有的布局空间 {@link android.view.View.INVISIBLE}
     */
    void setInVisible();

    /**
     * 判断可见状态:
     *
     * @return {@link android.view.View.GONE} {@link android.view.View.INVISIBLE} {@link android.view.View.VISIBLE}
     */
    int judgeVisible();
}
