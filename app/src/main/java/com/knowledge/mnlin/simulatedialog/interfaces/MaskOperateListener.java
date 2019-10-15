package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * dispatch the mask's action
 */
public interface MaskOperateListener {
    /**
     * dispatch the click action
     * @param view target
     */
    void dispatchMaskOnClick(@NonNull View view);
}