package com.pukka.ydepg.compiler.util;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.compiler.util.Logger.java
 * @date: 2018-01-09 16:50
 * @version: V1.0 描述当前版本功能
 */


public class Logger {
    private Messager messager;

    public Logger(Messager messager) {
        this.messager = messager;
    }

    public void info(CharSequence info) {
        messager.printMessage(Diagnostic.Kind.NOTE, info);
    }

    public void info(Element element, CharSequence info) {
        messager.printMessage(Diagnostic.Kind.NOTE, info, element);
    }

    public void warn(CharSequence info) {
        messager.printMessage(Diagnostic.Kind.WARNING, info);
    }

    public void warn(Element element, CharSequence info) {
        messager.printMessage(Diagnostic.Kind.WARNING, info, element);
    }

    public void error(CharSequence info) {
        messager.printMessage(Diagnostic.Kind.ERROR, info);
    }

    public void error(Element element, CharSequence info) {
        messager.printMessage(Diagnostic.Kind.ERROR, info, element);
    }
}
