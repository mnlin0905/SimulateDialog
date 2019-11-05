package com.knowledge.mnlin.page.plugins;

import com.knowledge.mnlin.page.interfaces.PageGenCombineOperate;
import com.knowledge.mnlin.page.interfaces.Page;
import com.knowledge.mnlin.page_annotation.consts.PageGenClassConst;

import org.jetbrains.annotations.NotNull;


/**
 * Created on 2019/10/30  10:32
 * function : add for operate gen file or value inject
 *
 * @author mnlin0905@gmail.com
 */
public class PagePlugin {

    /**
     * @param page page-self
     * @return null or gen-combine-file
     */
    @NotNull
    public static PageGenCombineOperate findCombineOperateForPage(@NotNull Page page) {
        try {
            Class<?> clazz = Class.forName(page.getClass().getCanonicalName() + PageGenClassConst.CLASS_PAGE_GEN_COMBINE_SUFFIX);
            return (PageGenCombineOperate) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new PageGenCombineOperate() {
            };
        }
    }
}
