package com.knowledge.mnlin.simulatedialog.interfaces;

/**
 * Created on 2019/10/14  11:17
 * function : top-level interface
 * <p>
 * Some interfaces are adapted here to ensure that page can call methods more easily;
 * however, this interface will also cause too many overloads
 * <p>
 * Or add a method to trigger listening
 * <p>
 * It will do a lot of operations that can simplify the logic.
 *
 * @author mnlin0905@gmail.com
 */
public interface Page extends
        NativeEventForPage,
        PageLifeCycle,
        PageProvider,
        PageLauncherType,
        PageKeyWatch,
        PageAppearance,
        PageMethodReversal,
        PageChannel<Page>,
        PageMethodPiling {

    // This class only adapts to the page.

    //////////////////////////////////////////////////////////////////////////////////////////
    ////  adapter to transition-animation {PageTransAnimation}
    //////////////////////////////////////////////////////////////////////////////////////////

    void performPageRecordRightPush();

    void performPageRecordLeftInsert();

    void performPageRecordRightPop(PageCallback<Page> mustCalledWhenEndOrCancel);

    void performPageRecordLeftRemove(PageCallback<Page> mustCalledWhenEndOrCancel);

    void performReturnToAttachStatus();

    void performReturnToDetachStatus();

    void performCancelPageAnimation();
}
