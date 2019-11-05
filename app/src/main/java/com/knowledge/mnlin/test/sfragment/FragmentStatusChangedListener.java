package com.knowledge.mnlin.test.sfragment;

/**
 * Created on 2019/4/12  10:18
 * function : Fragment 状态变化时,监听事件
 *
 * @author mnlin
 */
public interface FragmentStatusChangedListener {
    /**
     * run on fragment state : {@link SimulateFragmentInterface#STATUS_FIRST_APPEAR}
     *
     * @return 同 {@link SimulateFragmentInterface#onFragmentStatusChanged(int)}
     */
    default boolean onFragmentFirstAppear() {
        return false;
    }

    /**
     * run on fragment state : {@link SimulateFragmentInterface#STATUS_DISAPPEAR}
     *
     * @return 同 {@link SimulateFragmentInterface#onFragmentStatusChanged(int)}
     */
    default boolean onFragmentDisappear() {
        return false;
    }

    /**
     * run on fragment state : {@link SimulateFragmentInterface#STATUS_REPEAT_APPEAR}
     *
     * @return 同 {@link SimulateFragmentInterface#onFragmentStatusChanged(int)}
     */
    default boolean onFragmentRepeatAppear() {
        return false;
    }
}
