package com.pukka.ydepg.compiler.processor;

import com.pukka.ydepg.compiler.util.Logger;
import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
@SupportedAnnotationTypes({//标注注解处理器支持的注解类型
        "com.pukka.ydepg.annotation.RetrofitBuilder",
        "com.pukka.ydepg.annotation.Router"
})
public class AnnotationProcessor extends AbstractProcessor {
    public Filer filer; //   写文件的类
    public Elements elements;
    public Logger logger; //日志信息类
    public ProcessingEnvironment env; //获取工具类

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.env = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());
        new RetrofitProcessor().process(roundEnv, this);
        new RouterProcessor().process(roundEnv, this);
        return true;
    }
}

//@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
//public class AnnotationProcessor extends AbstractProcessor {
//    public Filer filer; //   写文件的类
//    public Elements elements;
//    public Messager messager; //日志信息类
//    public String packageName = "com.app.processor";
//
//        @Override
//    public synchronized void init(ProcessingEnvironment processingEnvironment) {
//        super.init(processingEnvironment);
//        filer = processingEnv.getFiler();
//        elements = processingEnv.getElementUtils();
//        messager = processingEnv.getMessager();
//    }
//
//    /**
//     * 支持的注解类型
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set type = new HashSet<String>();
//        type.add(Router.class);
//        type.add(RetrofitBuilder.class);
//        return type;
//    }
//
//    /**
//     * 核心功能，通过解析注解生成代码。
//     * @param annotations
//     * @param roundEnv
//     * @return
//     */
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        new RouterProcessor().process(roundEnv, this);
//        new RetrofitProcessor().process(roundEnv,this);
//        return true;
//    }
//}
