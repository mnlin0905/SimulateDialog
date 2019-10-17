package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;

import com.knowledge.mnlin.simulatedialog.base.ShadeMaskView;

/**
 * dispatch the mask's action
 */
public interface MaskOperateListener {
    /**
     * dispatch the click action
     *
     * @param mask target
     */
    void dispatchMaskOnClick(@NonNull ShadeMaskView mask);
}
