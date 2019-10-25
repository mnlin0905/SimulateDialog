package com.knowledge.mnlin.simulatedialog.interfaces;

import com.knowledge.mnlin.simulatedialog.configs.PageConfigs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2019/10/22  12:11
 * function : Add embedded points for branch logic, which can be implemented automatically if necessary
 *
 * @author mnlin0905@gmail.com
 */
public interface PageMethodPiling {
    List<MethodPilingCallback> methodPilingCallback = new LinkedList<>();

    /**
     * For users to operate
     */
    default void dispatchPiling() {
        if (PageConfigs.sOpenMethodPiling) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement[] stackUseful = Arrays.copyOfRange(stackTrace, 3, stackTrace.length);
            for (MethodPilingCallback callback : methodPilingCallback) {
                callback.run(stackUseful);
            }
        }
    }

    /**
     * callback for PageMethodPiling
     */
    interface MethodPilingCallback {
        void run(StackTraceElement[] stackUseful);
    }
}
