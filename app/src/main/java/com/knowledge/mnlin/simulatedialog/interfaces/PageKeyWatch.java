package com.knowledge.mnlin.simulatedialog.interfaces;

/**
 * Created on 2019/10/14  14:27
 * function : keep watch on keys(include back-press ,etc)
 *
 * @author mnlin0905@gmail.com
 */
public interface PageKeyWatch {
    /**
     * called when "back" be pressed
     *
     * @return if true,manager will not close the page (regard as a dialog)
     */
    boolean onBackPressed();
}
