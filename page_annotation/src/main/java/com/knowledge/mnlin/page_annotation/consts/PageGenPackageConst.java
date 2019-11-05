package com.knowledge.mnlin.page_annotation.consts;

/**
 * Created on 2019/11/4  19:20
 * function : package path
 *
 * @author mnlin0905@gmail.com
 */
public class PageGenPackageConst {
    /**
     * module base package
     */
    public static final String PAGE_MODULE_PACKAGE = "com.knowledge.mnlin";

    /**
     * page-main-lib package
     */
    public static final String PAGE_MODULE_MAIN_PACKAGE = PageGenPackageConst.PAGE_MODULE_PACKAGE + ".page";

    /**
     * page-annotation-lib package
     */
    public static final String PAGE_MODULE_ANNOTATION_PACKAGE = PageGenPackageConst.PAGE_MODULE_PACKAGE + ".page_annotation";

    /**
     * page-processor-lib package
     */
    public static final String PAGE_MODULE_PROCESSOR_PACKAGE = PageGenPackageConst.PAGE_MODULE_PACKAGE + ".page_processor";

    /**
     * page-main-lib package : core
     */
    public static final String PAGE_MODULE_MAIN_CORE_PACKAGE = PAGE_MODULE_MAIN_PACKAGE + ".core";
}
