package com.pukka.ydepg.compiler.processor;

import com.pukka.ydepg.annotation.RetrofitBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.compiler.processor.RetrofitProcessor.java
 * @date: 2018-01-09 15:14
 * @version: V1.0 动态生成retrofit模板
 */


public class RetrofitProcessor implements IProcessor {
    private String packageName = "com.pukka.ydepg.processor.net";

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor abstractProcessor) {
        for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(RetrofitBuilder.class))) {
            String apiClassName, apiPkgName;
            try {
                Class<?> retrofitAPiClz = element.getAnnotation(RetrofitBuilder.class).httpAPiName();
                apiClassName = retrofitAPiClz.getSimpleName();
                apiPkgName = retrofitAPiClz.getPackage().getName();
            } catch (MirroredTypeException mte) {
                TypeMirror classTypeMirror = mte.getTypeMirror();
                Element classTypeElement = abstractProcessor.env.getTypeUtils().asElement(classTypeMirror);
                PackageElement packageElement = abstractProcessor.env.getElementUtils().getPackageOf(classTypeElement);
                apiPkgName = packageElement.getQualifiedName().toString();
                apiClassName = classTypeElement.getSimpleName().toString();
                abstractProcessor.logger.info("pkgName: " + apiPkgName + "@" + "simpleName:" + apiClassName);
            }
            String className = apiClassName + "Factory";
//            abstractProcessor.logger.info( "apiClassName: " + apiClassName + "className" + className);
            TypeSpec.Builder classBuilder = classBuilder(className).addModifiers(PUBLIC, FINAL).addJavadoc("这个类是自动生成的代码，不要修改");
            for (Element e : element.getEnclosedElements()) {
                //根据注解生成方法
                ExecutableElement executableElement = (ExecutableElement) e;
                MethodSpec.Builder methodBuilder =
                        MethodSpec.methodBuilder(e.getSimpleName().toString())
                                .addModifiers(PUBLIC, STATIC);
                methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
                String paramsString = "";
                for (VariableElement ep : executableElement.getParameters()) {
                    methodBuilder.addParameter(TypeName.get(ep.asType()), ep.getSimpleName().toString());
                    paramsString += ep.getSimpleName().toString() + ",";
                }
                ClassName apiClzName = ClassName.get(apiPkgName, apiClassName);
                String paramsStr = "";
                if (!paramsString.equals("")) {
                    paramsStr = paramsString.substring(0, paramsString.length() - 1);
                }
                methodBuilder.addStatement(
                        "return $T.getInstance()" +
                                ".getService().$L($L)"
                        , apiClzName
                        , e.getSimpleName().toString(), paramsStr);
                classBuilder.addMethod(methodBuilder.build());
            }
            try {
                JavaFile.builder(packageName, classBuilder.build()).build().writeTo(abstractProcessor.filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
