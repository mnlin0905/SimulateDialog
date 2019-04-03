package com.knowledge.mnlin.sdialog.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2019/3/23  20:37
 * function : 模拟dialog的控件,必须具有以下功能:
 * <p>
 * 1.获取加载到ViewGroup中的layoutParams方式
 * 2.获取可加载到ViewGroup中的View类型布局
 *
 * @author mnlin
 */
public interface SimulateDialogInterface<V extends View, L extends ViewGroup.LayoutParams> {
    /**
     * 获取布局
     *
     * <b>请确保: 多次调用该接口应当返回同一对象</b>
     *
     * @return View或者ViewGroup
     */
    @NonNull
    V generateView();

    /**
     * 获取layout-params,加载到FrameLayout中的方式
     */
    @Nullable
    L generateLayoutParams();
}
