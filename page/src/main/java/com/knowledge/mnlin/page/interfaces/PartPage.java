package com.knowledge.mnlin.page.interfaces;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.graphics.drawable.Drawable;

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
    Page peekMaskForPart();

    /**
     * inject host's mast-view
     *
     * @param mask mask-view
     */
    void injectMaskForPart(@NonNull Page mask);

    /**
     * @return closed if click mask (outside of page)
     */
    default boolean closedOnClickOutside() {
        return true;
    }

    /**
     * @return {@link PartPage#setMaskDrawable(Drawable)}
     */
    @Nullable
    Drawable getMaskDrawable();

    /**
     * @param background mask-view-bg
     */
    void setMaskDrawable(Drawable drawable);
}
