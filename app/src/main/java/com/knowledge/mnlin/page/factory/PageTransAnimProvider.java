package com.knowledge.mnlin.page.factory;

import com.knowledge.mnlin.page.animations.PageTANoEffect;
import com.knowledge.mnlin.page.interfaces.PageTransAnimation;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import libcore.util.Nullable;

/**
 * Created on 2019/10/29  21:05
 * function :
 *
 * @author mnlin0905@gmail.com
 */
class PageTransAnimProvider {
    private static Map<String, Class<? extends PageTransAnimation>> pageAnimationHolder;

    /**
     * 为了方便,直接load所有默认的animation,但先不创建对象
     */
    private static void init() {
        if (pageAnimationHolder == null) {
            pageAnimationHolder = new HashMap<>();
            pageAnimationHolder.put(PageTransAnimKeyConst.KEY_PAGE_TRANS_ONE, PageTANoEffect.class);
            // ...
        }
    }

    @Nullable
    synchronized static PageTransAnimation createPageTransAnimation(@NotNull String key) {
        init();
        Class<? extends PageTransAnimation> clazz = pageAnimationHolder.get(key);
        try {
            Constructor<? extends PageTransAnimation> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
