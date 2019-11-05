package com.knowledge.mnlin.page_processor.processors;

import com.google.auto.service.AutoService;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageLauncherType;
import com.knowledge.mnlin.page_annotation.consts.PageGenClassConst;
import com.knowledge.mnlin.page_processor.consts.ProcessorConst;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_MODULE_WARNING_TIP;

@AutoService(Processor.class)
@SupportedAnnotationTypes(ProcessorConst.PATH_ANNOTATION_PAGE_INJECT_LAUNCHER_TYPE)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class PageLauncherTypeProcessor extends AbstractProcessor {
    private static final String TAG = "PageLauncherTypeProcessor";

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
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(InjectPageLauncherType.class);
        Iterator<? extends Element> iterator = elementsAnnotatedWith.iterator();
        TypeElement typeElement;

        messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : BEGIN-PROCESS ");

        // will create more than one file
        while (iterator.hasNext()) {
            typeElement = (TypeElement) iterator.next();

            // page-annotation-package
            // gen-target-package
            String interface_page = COMMON_CLASS_PAGE.reflectionName();
            String package_target;

            try {
                if (!typeUtils.isSubtype(typeElement.asType(),
                        elementUtils.getTypeElement(interface_page).asType())) {
                    messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG
                            + " : Only 'page' type class can inject '@InjectPageLauncherType' annotation "
                            + elementUtils.getTypeElement(interface_page).asType());
                } else {
                    // count package-path
                    PackageElement pkgElement = (PackageElement) typeElement.getEnclosingElement();
                    if (pkgElement.isUnnamed()) {
                        package_target = "";
                    } else {
                        package_target = pkgElement.getQualifiedName().toString();
                    }

                    // count annotations-string
                    InjectPageLauncherType annotation = typeElement.getAnnotation(InjectPageLauncherType.class);
                    int launcherType = annotation.launcherType();

                    JavaFile.builder(package_target, createClass(typeElement, launcherType))
                            .addFileComment(PAGE_MODULE_WARNING_TIP)
                            .build()
                            .writeTo(filer);
                }
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " :  " + e.toString());
                break;
            }
        }

        return true;
    }

    private TypeSpec createClass(TypeElement typeElement, int launcherType) {
        String className = typeElement.getSimpleName() + PageGenClassConst.CLASS_PAGE_GEN_LAUNCHER_TYPE_SUFFIX;

        return TypeSpec.classBuilder(className)
                .addJavadoc(PAGE_MODULE_WARNING_TIP)
                .addMethod(createMethod(launcherType))
                .addModifiers(Modifier.FINAL)
                .build();
    }

    private MethodSpec createMethod(int launcherType) {
        // create return type
        return MethodSpec.methodBuilder(ProcessorConst.PAGE_LAUNCHER_TYPE_METHOD)
                .addModifiers(Modifier.STATIC)
                .returns(int.class)
                .addStatement("return $L", launcherType)
                .build();
    }
}