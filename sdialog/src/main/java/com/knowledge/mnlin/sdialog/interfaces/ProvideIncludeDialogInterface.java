package com.knowledge.mnlin.sdialog.interfaces;

import android.support.annotation.Nullable;

import com.knowledge.mnlin.sdialog.widgets.IncludeDialogViewGroup;

/**
 * Created on 2019/3/29  11:35
 * function : 接口:提供IncludeDialogViewGroup对象
 *
 * @author mnlin
 */
public interface ProvideIncludeDialogInterface {
    /**
     * @return IncludeDialogViewGroup对象
     */
    @Nullable
    default IncludeDialogViewGroup getIncludeDialog() {
        return null;
    }
}
