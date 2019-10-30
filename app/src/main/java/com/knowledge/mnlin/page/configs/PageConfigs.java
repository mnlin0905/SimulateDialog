package com.knowledge.mnlin.page.configs;

/**
 * Created on 2019/10/22  12:19
 * function : configs param
 *
 * @author mnlin0905@gmail.com
 */
public class PageConfigs {
    /**
     * method piling (for development)
     */
    public static boolean sOpenMethodPiling = false;

    /**
     * Judge whether the visible page can be manipulated
     */
    public static boolean canOperateBackVisiblePage = false;

    /**
     * If there is only one interface, can the return key be processed by default?
     */
    public static boolean canBackOnlyOnePage = true;

    /**
     * if animation is {@link com.knowledge.mnlin.page.animations.PageTANoEffect},whether to inject appropriate animation according to page type
     */
    public static boolean autoInjectPageTransAnim = true;
}
