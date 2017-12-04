package com.swordyt.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.swordyt.core.DataType;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Driver {
	public String type();
	public String[] parameter();
}
