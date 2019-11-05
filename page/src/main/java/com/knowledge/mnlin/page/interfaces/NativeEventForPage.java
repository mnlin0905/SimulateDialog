package com.knowledge.mnlin.page.interfaces;

import android.content.Intent;
import android.annotation.Nullable;

/**
 * Created on 2019/10/25  10:15
 * function : native event , provider by native Activity
 *
 * @author mnlin0905@gmail.com
 */
public interface NativeEventForPage {
    /**
     * Assign events to page for processing, page at the top of the stack takes precedence
     * <p>
     * So the page at the top of the stack has the right to modify the intent property.
     *
     *
     *
     * @return true if break the chain of responsibility
     * @see android.app.Activity#onNewIntent(Intent)
     */
    boolean dispatchOnNewIntent(@Nullable Intent newIntent);
}
