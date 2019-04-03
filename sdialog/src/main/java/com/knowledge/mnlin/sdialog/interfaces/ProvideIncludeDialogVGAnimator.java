package com.knowledge.mnlin.sdialog.interfaces;


import com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup;

/**
 * Created on 2019/3/29  11:49
 * function : 为 {@link com.wallet.usdp.view.IncludeDialogViewGroup} 添加动画处理效果
 *
 * 注意:
 *      开始动画和结束动画前后,要保证 dialog的 状态完全恢复,否则,可能会影响弹出关闭后再次弹出的显示效果
 *
 * 注意:
 *      处理动画时,仅将 dialog.generateView 当做普通的{@link android.view.View} 来进行动画处理即可
 *
 * 例如:
 *
 * 当设置透明度变化动画时:
 *  在开始动画{@link ProvideIncludeDialogVGAnimator#startAnimator}前,要保证 view 透明度为最小值;以达到动画显示效果
 *  在结束动画 {@link ProvideIncludeDialogVGAnimator#stopAnimator}后,要保证 View 透明度为最大值,因为可能下次弹出此 dialog 时动画不再是透明度变化动画
 *
 * 详细逻辑可参考{@link com.wallet.usdp.util.AlphaIDVGAnimatorImpl}
 *
 * @author mnlin
 */
public interface ProvideIncludeDialogVGAnimator {
    /**
     * 开始动画 处理逻辑
     *
     * @param dialog                  被模拟的dialog
     * @param parent                  dialog-container
     * @param mustCallOnAnimatorStart 当自己动画开始之前,必须调用执行该 runnable  方法,保证dialog可以正常添加到屏幕上
     */
    void startAnimator(IncludeDialogViewGroup parent, SimulateDialogInterface<?, ?> dialog, Runnable mustCallOnAnimatorStart);

    /**
     * 结束动画 处理逻辑
     *
     * @param dialog                被模拟的dialog
     * @param parent                dialog-container
     * @param mustCallOnAnimatorEnd 当自己处理完动画后,必须调用执行该 runnable  方法,保证dialog可以正常关闭
     */
    void stopAnimator(IncludeDialogViewGroup parent, SimulateDialogInterface<?, ?> dialog, Runnable mustCallOnAnimatorEnd);
}
