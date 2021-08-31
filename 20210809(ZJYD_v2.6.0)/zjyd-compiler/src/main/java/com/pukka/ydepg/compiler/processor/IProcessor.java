package com.pukka.ydepg.compiler.processor;

import javax.annotation.processing.RoundEnvironment;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.compiler.processor.IProcessor.java
 * @date: 2018-01-09 09:02
 * @version: V1.0 描述当前版本功能
 */


public interface IProcessor {
    void process(RoundEnvironment roundEnv, AnnotationProcessor mAbstractProcessor);
}
