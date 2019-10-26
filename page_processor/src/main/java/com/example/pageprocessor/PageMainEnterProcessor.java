package com.example.pageprocessor;

import com.example.page_annotation.PageEnterPoint;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.example.Consts.ProcessorConst.PAGE_ENTER_POINT_CLASS;
import static com.example.Consts.ProcessorConst.PAGE_ENTER_POINT_METHOD;
import static com.example.Consts.ProcessorConst.PAGE_MUDULE_PACKAGE;
import static com.example.Consts.ProcessorConst.PAGE_MUDULE_WARNING_TIP;

/**
 * The first interface entrance ensures that there is a visual interface when the application enters
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.page_annotation.PageEnterPoint")
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class PageMainEnterProcessor extends AbstractProcessor {
    private static final String TAG = "PageMainEnterProcessor";

    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(PageEnterPoint.class);
        Iterator<? extends Element> iterator = elementsAnnotatedWith.iterator();
        TypeElement typeElement;
        if (iterator.hasNext()) {
            typeElement = (TypeElement) iterator.next();
        } else {
            messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : ABORT");
            return false;
        }

        messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : BEGIN-PROCESS ");

        if (typeElement == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG + " : no page-class annotate '@PageEnterPoint', page module can not work ");
        } else if (!typeUtils.isSubtype(typeElement.asType(),
                elementUtils.getTypeElement(PAGE_MUDULE_PACKAGE + ".interfaces.Page").asType())) {
            messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG + " : Only 'page' type class can inject '@PageEnterPoint' annotation " + elementUtils.getTypeElement(PAGE_MUDULE_PACKAGE + ".interfaces.Page").asType());
        } else {
            try {
                JavaFile.builder(PAGE_MUDULE_PACKAGE + ".core", createClass(typeElement))
                        .addFileComment(PAGE_MUDULE_WARNING_TIP)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " :  " + e.getMessage());
            }
        }

        return true;
    }

    private TypeSpec createClass(TypeElement typeElement) {
        return TypeSpec.classBuilder(PAGE_ENTER_POINT_CLASS)
                .addJavadoc(PAGE_MUDULE_WARNING_TIP)
                .addMethod(createMethod(typeElement))
                .addModifiers(Modifier.FINAL)
                .build();
    }

    private MethodSpec createMethod(TypeElement typeElement) {
        TypeName returnType = TypeName.get(typeElement.asType());

        return MethodSpec.methodBuilder(PAGE_ENTER_POINT_METHOD)
                .addModifiers(Modifier.FINAL, Modifier.STATIC)
                .returns(returnType)
                .addComment("Annotation class must have a default 'public'' constructor")
                .addStatement("return new " + returnType.toString() + "()") //returnType.getClass().getCanonicalName()
                .build();
    }
}