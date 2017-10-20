package com.yinting.core.datadriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Driver {
public DataType type() default DataType.EXCEL ;
/**
 *excel path,sheet
 *db sqlPath
 *xml ....
 * */
public String[] parameter();
}
