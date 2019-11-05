package com.knowledge.mnlin.page_annotation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019/10/26  16:15
 * function : Mark this as a page-trans-anim
 *
 * @author mnlin0905@gmail.com
 */
@Inherited
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
public @interface PageTransAnimTag {
    String key() default "";
}