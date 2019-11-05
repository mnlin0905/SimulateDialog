package com.knowledge.mnlin.page_annotation.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * function : inject page-launcher-type
 * <p>
 * Created on 2019/10/15  17:48
 *
 * @author mnlin0905@gmail.com
 */
@Inherited
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
public @interface InjectPageLauncherType {
    /**
     * @return from {@link PageLauncherType#LAUNCHER_DEFAULT_TYPE } to  {@link PageLauncherType.LAUNCHER_SINGLE_TASK}
     */
    int launcherType();
}