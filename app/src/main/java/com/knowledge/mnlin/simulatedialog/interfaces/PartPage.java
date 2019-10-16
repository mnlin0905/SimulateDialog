package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.knowledge.mnlin.simulatedialog.base.ShadeMaskView;

/**
 * Created on 2019/10/16  12:10
 * function :  part type
 * <p>
 * provider method be aimed at part-page
 *
 * @author mnlin0905@gmail.com
 */
public interface PartPage extends Page {
    /**
     * @return attempt to get host's mast-view, it can be null
     */
    @Nullable
    ShadeMaskView peekMaskForPart();

    /**
     * inject host's mast-view
     *
     * @param mask mask-view
     */
    void injectMaskForPart(@NonNull ShadeMaskView mask);

    /**
     * @return closed if click mask (outside of page)
     */
    default boolean closedOnClickOutside(){
        return true;
    }
}
