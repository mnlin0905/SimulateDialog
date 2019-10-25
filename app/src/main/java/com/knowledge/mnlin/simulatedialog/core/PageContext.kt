package com.knowledge.mnlin.simulatedialog.core

import android.content.Context
import android.os.Handler
import com.knowledge.mnlin.simulatedialog.interfaces.Page
import com.knowledge.mnlin.simulatedialog.interfaces.PageMethodPiling
import com.orhanobut.logger.Logger
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created on 2019/10/24  14:15
 * function : facade of PageParent
 *
 * In order to control the access properties of some methods and the addition of new methods,
 * it is necessary to restrict the access capability through agents.
 *
 * @author mnlin0905@gmail.com
 */
class PageContext(pageParent: PageParent) : PageMethodPiling {
    /**
     * entity part
     */
    private val mParent: WeakReference<PageParent> = WeakReference(pageParent)

    /**
     * TODO Release other application resources and close some connections with irreversible errors when exceptions occur
     *
     * throw new RuntimeException("PageParent has finished");
     *
     * @return page-parent instance
     */
    private val pageParent: PageParent?
        get() {
            val pageParent = mParent.get()
            return if (pageParent == null) {
                Logger.v("PageParent has finished ,so instance should release itself;")
                PageParent.sInstance = null
                dispatchPiling()
                null
            } else {
                pageParent
            }
        }

    /**
     * @return get context from page-parent
     */
    val context: Context?
        get() = pageParent

    /**
     * @return main-handler
     */
    val mainHandler: Handler?
        get() = pageParent?.mainHandler

    /**
     * @return [.singlePageManager]
     */
    val pageManager: PageManager?
        get() = pageParent?.pageManager

    /**
     * @see PageParent.addPage
     */
    fun addPage(page: Page) {
        pageParent?.insertPageForUser(page)
    }

    /**
     * @see PageParent.addPage
     */
    fun insertPage(index: Int, page: Page) {
        pageParent?.insertPageForUser(index, page)
    }

    /**
     * @see PageParent.removePage
     */
    fun removePage(page: Page): Boolean {
        return pageParent?.removePageForUser(page) ?: false
    }

    /**
     * @see PageParent.removePage
     */
    fun removePage(index: Int): Boolean {
        return pageParent?.removePageForUser(index) ?: false
    }

    /**
     * @see PageParent.findPage
     */
    fun findPage(index: Int): Page? {
        return pageParent?.findPage(index)
    }

    /**
     * @see PageParent.indexOfPage
     */
    fun getIndexOfPage(page: Page): Int {
        return pageParent?.indexOfPage(page) ?: -1
    }

    /**
     * @see PageParent.findAllPages
     */
    fun getAllPages(): LinkedList<Page> {
        return pageParent?.findAllPages() ?: LinkedList()
    }
}
