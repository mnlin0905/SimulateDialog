package com.knowledge.mnlin.sdialog.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import com.knowledge.mnlin.sdialog.interfaces.ProvideIncludeDialogVGAnimator;
import com.knowledge.mnlin.sdialog.interfaces.SimulateDialogInterface;
import com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup;

/**
 * Created on 2019/3/29  11:54
 * function : 默认的透明度变化动画
 *
 * @author mnlin
 */
public class AlphaIDVGAnimatorImpl implements ProvideIncludeDialogVGAnimator {
    /**
     * minAlpha          最小透明度
     * maxAlpha          最小透明度
     * animatorStartTime 启动动画持续时间
     * animatorStopTime  终止动画持续时间
     */
    private final float minAlpha;
    private final float maxAlpha;
    private final long animatorStartTime;
    private final long animatorStopTime;

    public AlphaIDVGAnimatorImpl() {
        this(0F, 1F, 500L, 500L);
    }

    /**
     * 构造函数:  透明度取值区间:0F-1F,动画时间单位为ms
     *
     * @param minAlpha          最小透明度
     * @param maxAlpha          最小透明度
     * @param animatorStartTime 启动动画持续时间
     * @param animatorStopTime  终止动画持续时间
     */
    public AlphaIDVGAnimatorImpl(float minAlpha, float maxAlpha, long animatorStartTime, long animatorStopTime) {
        this.minAlpha = minAlpha;
        this.maxAlpha = maxAlpha;
        this.animatorStartTime = animatorStartTime;
        this.animatorStopTime = animatorStopTime;
    }

    /**
     * 开始动画 处理逻辑
     *
     * @param parent                  dialog-container
     * @param dialog                  被模拟的dialog
     * @param mustCallOnAnimatorStart 当自己处理完动画后,必须调用执行该 runnable  方法,保证dialog可以正常关闭
     */
    @Override
    public void startAnimator(IncludeDialogViewGroup parent, SimulateDialogInterface<?, ?> dialog, Runnable mustCallOnAnimatorStart) {
        View contentView = dialog.generateView();

        //初始化为最小透明度
        contentView.setAlpha(minAlpha);
        contentView.animate()
                .alpha(maxAlpha)
                .setDuration(animatorStartTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mustCallOnAnimatorStart.run();
                    }
                }).start();
    }

    /**
     * 结束动画 处理逻辑
     *
     * @param parent                dialog-container
     * @param dialog                被模拟的dialog
     * @param mustCallOnAnimatorEnd 当自己处理完动画后,必须调用执行该 runnable  方法,保证dialog可以正常关闭
     */
    @Override
    public void stopAnimator(IncludeDialogViewGroup parent, SimulateDialogInterface<?, ?> dialog, Runnable mustCallOnAnimatorEnd) {
        View contentView = dialog.generateView();

        contentView.animate()
                .alpha(minAlpha)
                .setDuration(animatorStopTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mustCallOnAnimatorEnd.run();

                        //还原为最大透明度
                        contentView.setAlpha(maxAlpha);
                    }
                }).start();
    }
}
