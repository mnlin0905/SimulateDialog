package com.knowledge.mnlin.sdialog.utils;

import android.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.annotation.LayoutRes;
import android.annotation.Nullable;

import com.knowledge.mnlin.sdialog.interfaces.ProvideIncludeDialogVGAnimator;
import com.knowledge.mnlin.sdialog.interfaces.SimulateDialogInterface;
import com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup;

/**
 * Created on 2019/4/3  16:31
 * function : 默认的 dialog 生成 提供器,可以方便用户创建出 dialog
 *
 * @author mnlin
 * @param <V> V 代表 dialog 布局文件中的根部局，请务必于真实类型对应
 * @param <L> L 代表 dialog 根部局加载时使用的 LayoutParams 类型，一般固定为 {@link android.widget.FrameLayout.LayoutParams}
 */
public class DefaultSimulateDialogImpl<V extends View, L extends ViewGroup.LayoutParams> implements SimulateDialogInterface<V, L> {
    /**
     * 保存容器类布局,方便使用
     */
    @NonNull
    private IncludeDialogViewGroup parentContainer;

    /**
     * 本 dialog 对应的 View 布局
     */
    @NonNull
    private V contentView;

    /**
     * 构造函数需要 container 参数
     *
     * @param container IncludeDialogViewGroup类型实例
     */
    public DefaultSimulateDialogImpl(@NonNull IncludeDialogViewGroup container, @LayoutRes int layoutRes) {
        this.parentContainer = container;
        contentView = (V) LayoutInflater.from(parentContainer.getContext()).inflate(layoutRes, parentContainer, false);

        // 将 此dialog 注册进入 IncludeDialogViewGroup 容器
        parentContainer.registerDialog(this);
    }

    @NonNull
    @Override
    public V generateView() {
        return contentView;
    }

    @Nullable
    @Override
    public L generateLayoutParams() {
        return (L) contentView.getLayoutParams();
    }

    /**
     * 添加快速显示 dialog 的方法
     * <p>
     * 默认只显示当前dialog
     *
     * @param animator 动画对象; 为null表示不使用动画
     */
    public void show(@Nullable ProvideIncludeDialogVGAnimator animator) {
        parentContainer.showDialogs(this, null, false, animator);
    }

    /**
     * 默认不使用动画
     *
     * @see #show(ProvideIncludeDialogVGAnimator)
     */
    public void show() {
        show(null);
    }

    /**
     * 添加快速隐藏dialog的方法
     *
     * @param useAnimator 值为 true 时,如果调用 {{@link #show(ProvideIncludeDialogVGAnimator)}} 方法时,参数不为空,则会启动关闭动画
     */
    public void close(boolean useAnimator) {
        parentContainer.closeDialogsOpened(this, null, false, useAnimator);
    }

    /**
     * 添加快速隐藏dialog的方法
     * <p>
     * 默认无动画
     */
    public void close() {
        close(false);
    }
}
