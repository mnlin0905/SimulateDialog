package com.knowledge.mnlin.page_processor.processors;

import com.google.auto.service.AutoService;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageAppearanceType;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageLauncherType;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageLayoutRes;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageTransAnim;
import com.knowledge.mnlin.page_annotation.annotations.PageTag;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_APPEAR_TYPE_SUFFIX;
import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_COMBINE_SUFFIX;
import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_LAUNCHER_TYPE_SUFFIX;
import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_LAYOUT_RES_SUFFIX;
import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_TRANS_ANIM_SUFFIX;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE_GEN_COMBINE_OPERATE;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE_IMPL;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE_TRANS_ANIMATION;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_APPEARANCE_TYPE_METHOD;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_LAUNCHER_TYPE_METHOD;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_LAYOUT_RES_METHOD;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_MODULE_WARNING_TIP;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_TRANS_ANIM_METHOD;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PATH_ANNOTATION_PAGE_TAG;

@AutoService(Processor.class)
@SupportedAnnotationTypes(PATH_ANNOTATION_PAGE_TAG)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class PageTagProcessor extends AbstractProcessor {
    private static final String TAG = "PageTagProcessor";

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
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(PageTag.class);
        Iterator<? extends Element> iterator = elementsAnnotatedWith.iterator();
        TypeElement typeElement;

        messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : BEGIN-PROCESS ");

        try {
            while (iterator.hasNext()) {
                typeElement = (TypeElement) iterator.next();

                // page interface
                TypeMirror classTypeRequired = elementUtils.getTypeElement(COMMON_CLASS_PAGE.reflectionName()).asType();

                messager.printMessage(Diagnostic.Kind.WARNING, ">>>>tag类型 " + TAG + typeElement.getModifiers());
                if (typeElement.getQualifiedName().toString().equals(COMMON_CLASS_PAGE_IMPL.reflectionName()) ||
                        typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
                    // not create for PageImpl
                    continue;
                } else if (!typeUtils.isSubtype(typeElement.asType(), classTypeRequired)) {
                    // judge is or not implement of PageTransAnimation
                    messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG
                            + " : Only 'Page' type class can inject '@PageTag' annotation "
                            + classTypeRequired);
                } else {
                    // class
                    TypeSpec.Builder classBuilder =
                            TypeSpec.classBuilder(typeElement.getSimpleName() + CLASS_PAGE_GEN_COMBINE_SUFFIX)
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(COMMON_CLASS_PAGE_GEN_COMBINE_OPERATE);

                    // judge InjectPageTransAnim
                    if (typeElement.getAnnotation(InjectPageTransAnim.class) != null) {
                        String methodAnimStr = "getPageTransAnimation";
                        MethodSpec methodAnim = MethodSpec.methodBuilder(methodAnimStr)
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .returns(COMMON_CLASS_PAGE_TRANS_ANIMATION)
                                .addStatement("return $T$L.$L()", typeElement, CLASS_PAGE_GEN_TRANS_ANIM_SUFFIX,
                                        PAGE_TRANS_ANIM_METHOD)
                                .build();

                        classBuilder.addMethod(methodAnim);
                    }

                    // judge InjectPageAppearanceType
                    if (typeElement.getAnnotation(InjectPageAppearanceType.class) != null) {
                        String methodAppearanceStr = "getPageAppearanceType";
                        MethodSpec methodAppearance = MethodSpec.methodBuilder(methodAppearanceStr)
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .returns(int.class)
                                .addStatement("return $T$L.$L()", typeElement, CLASS_PAGE_GEN_APPEAR_TYPE_SUFFIX,
                                        PAGE_APPEARANCE_TYPE_METHOD)
                                .build();

                        classBuilder.addMethod(methodAppearance);
                    }

                    // judge InjectPageLauncherType
                    if (typeElement.getAnnotation(InjectPageLauncherType.class) != null) {
                        String methodLauncherStr = "getPageLauncherType";
                        MethodSpec methodLauncher = MethodSpec.methodBuilder(methodLauncherStr)
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .returns(int.class)
                                .addStatement("return $T$L.$L()", typeElement, CLASS_PAGE_GEN_LAUNCHER_TYPE_SUFFIX,
                                        PAGE_LAUNCHER_TYPE_METHOD)
                                .build();

                        classBuilder.addMethod(methodLauncher);
                    }

                    // judge InjectPageLauncherType
                    if (typeElement.getAnnotation(InjectPageLayoutRes.class) != null) {
                        String methodLayoutStr = "getPageLayoutRes";
                        MethodSpec methodLayout = MethodSpec.methodBuilder(methodLayoutStr)
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Override.class)
                                .returns(int.class)
                                .addStatement("return $T$L.$L()", typeElement, CLASS_PAGE_GEN_LAYOUT_RES_SUFFIX,
                                        PAGE_LAYOUT_RES_METHOD)
                                .build();

                        classBuilder.addMethod(methodLayout);
                    }

                    // count package-path
                    PackageElement pkgElement = (PackageElement) typeElement.getEnclosingElement();
                    String package_target;
                    if (pkgElement.isUnnamed()) {
                        package_target = "";
                    } else {
                        package_target = pkgElement.getQualifiedName().toString();
                    }

                    // create file
                    JavaFile.builder(package_target, classBuilder.build())
                            .addFileComment(PAGE_MODULE_WARNING_TIP)
                            .build()
                            .writeTo(filer);
                }
            }
        } catch (Exception err) {
            messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " :  " + err.toString());
        }
        return true;
    }
}