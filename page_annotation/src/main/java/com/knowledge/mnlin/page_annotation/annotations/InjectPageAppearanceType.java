package com.knowledge.mnlin.page_annotation.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * function : inject page-appearance-type
 *
 * Created on 2019/10/15  17:48
 * @author mnlin0905@gmail.com
 */
@Inherited
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
public @interface InjectPageAppearanceType {
    /**
     * @return {@link PageAppearance.PAGE_APPEARANCE_FULLSCREEN } or  {@link PageAppearance.PAGE_APPEARANCE_PART}
     */
    int appearanceType();
}