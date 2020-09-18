package com.hkrt.androidaspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2020/9/17
 * @修改人和其它信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoubleClick {

    int value();

    String path();
}
