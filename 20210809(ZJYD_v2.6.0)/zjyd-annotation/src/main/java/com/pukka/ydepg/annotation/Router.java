package com.pukka.ydepg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author yangjunyan
 * @FileName: com.easier.zjyd.annotation.Router.java
 * @date: 2018-01-08 17:38
 * @version: V1.0 描述当前版本功能
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Router {
    String name();
}
