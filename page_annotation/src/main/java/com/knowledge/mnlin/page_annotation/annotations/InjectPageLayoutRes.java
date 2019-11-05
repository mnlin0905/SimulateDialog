package com.knowledge.mnlin.page_annotation.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * function : inject layout
 * <p>
 * Created on 2018/6/30  17:29
 *
 * @author mnlin
 */
@Inherited
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
public @interface InjectPageLayoutRes {
    /**
     * @return int value,must be "@LayoutRes"
     */
    int layoutResId();
}