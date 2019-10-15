package com.knowledge.mnlin.simulatedialog.interfaces;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2019/10/12  22:03
 * function :
 *
 * @author mnlin0905@gmail.com
 */
public interface PageProvider {
    /**
     * @return real-view in page ; if it is null , the system reads the annotated values
     */
    @NonNull
    View providerContentView();

    /**
     * @return the layout-params put in parent-view , if it is null , the system will take the default value (read from xml)
     */
    @NonNull
    ViewGroup.LayoutParams providerIntegrateParams();
}
