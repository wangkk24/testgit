package com.pukka.ydepg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author yangjunyan
 * @FileName: com.easier.zjyd.annotation.RetrofitBuilder.java
 * @date: 2018-01-09 15:20
 * @version: V1.0 描述当前版本功能
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface RetrofitBuilder {
    Class httpAPiName();
}
