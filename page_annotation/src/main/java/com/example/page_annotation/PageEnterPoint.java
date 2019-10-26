package com.example.page_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019/10/26  16:15
 * function : page module enter point
 * <p>
 * There should be only one application
 *
 * @author mnlin0905@gmail.com
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
public @interface PageEnterPoint {

}
