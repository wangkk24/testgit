package com.pukka.ydepg.compiler.processor;

import com.pukka.ydepg.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * 生成路由的代码
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.compiler.processor.RouterProcessor.java
 * @date: 2018-01-09 08:48
 * @version: V1.0 描述当前版本功能
 */


public class RouterProcessor implements IProcessor{
    private String packageName = "com.pukka.ydepg.processor.router";
    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor abstractProcessor) {
        String className  = "RouterMapping";
        TypeSpec.Builder classBuilder = classBuilder(className).addModifiers(PUBLIC, FINAL).addJavadoc("这个类是自动生成的代码，不要修改");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Router.class))) {

            ElementKind kind = element.getKind();
            // 判断该元素是否为类
            if (kind == ElementKind.CLASS) {
                String activityName = element.getAnnotation(Router.class).name();
                ClassName activityClassName = ClassName.get((TypeElement) element);
                abstractProcessor.logger.info("name:" + activityName + "activityClassName:" + activityClassName.simpleName());
                methodBuilder.addStatement("$T.map($S, $T.class)",ClassName.get("com.pukka.ydepg.common.router","ZjRouter") ,activityName, activityClassName);
                methodBuilder.addCode("\n");
            }
        }
        classBuilder.addMethod(methodBuilder.build());
        try {
            JavaFile.builder(packageName, classBuilder.build()).build().writeTo(abstractProcessor.filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
