package com.knowledge.mnlin.page_processor.processors;

import com.google.auto.service.AutoService;
import com.knowledge.mnlin.page_annotation.annotations.PageTransAnimTag;
import com.knowledge.mnlin.page_annotation.consts.PageGenPackageConst;
import com.knowledge.mnlin.page_processor.consts.ProcessorConst;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_TRANS_ANIM_KEY;
import static com.knowledge.mnlin.page_annotation.consts.PageGenClassConst.CLASS_PAGE_GEN_TRANS_ANIM_PROVIDER;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE_TRANS_ANIMATION;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_MODULE_WARNING_TIP;

/**
 * create animation key (const)
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes(ProcessorConst.PATH_ANNOTATION_PAGE_TRANS_ANIM_TAG)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class PageTransAnimTagProcessor extends AbstractProcessor {
    private static final String TAG = "PageTransAnimTagProcessor";

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
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(PageTransAnimTag.class);
        Iterator<? extends Element> iterator = elementsAnnotatedWith.iterator();
        TypeElement typeElement;

        messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : BEGIN-PROCESS ");

        /* 1.n : for "PageTransAnimKeyConst" ; 2.n for "PageTransAnimProvider" */

        // 1.1 : create static field for "PageTransAnimKeyConst"
        List<FieldSpec> fieldSpecs = new LinkedList<>();

        // 2.1 create animation holder for "PageTransAnimProvider"
        Map<String, TypeElement> keyClazz = new HashMap<>();

        try {
            while (iterator.hasNext()) {
                typeElement = (TypeElement) iterator.next();

                //
                TypeMirror classTypeRequired = elementUtils.getTypeElement(COMMON_CLASS_PAGE_TRANS_ANIMATION.reflectionName()).asType();

                if (!typeUtils.isSubtype(typeElement.asType(), classTypeRequired)) {
                    // judge is or not implement of PageTransAnimation
                    messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG
                            + " : Only 'PageTransAnimation' type class can inject '@PageTransAnimTag' annotation "
                            + classTypeRequired);
                } else {
                    // count annotations-string
                    PageTransAnimTag annotation = typeElement.getAnnotation(PageTransAnimTag.class);
                    String animationKey = annotation.key();
                    String fieldName = typeElement.getSimpleName().toString();

                    // if null or empty
                    if (animationKey.isEmpty()) {
                        animationKey = fieldName;
                        animationKey = animationKey.replaceAll("[A-Z]", "_$0").substring(1).toUpperCase();
                    }

                    fieldSpecs.add(FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("\"" + animationKey + "\"")
                            .build());

                    keyClazz.put(fieldName, typeElement);
                }
            }

            // 1.2 : create Class for "PageTransAnimKeyConst"
            TypeSpec clazz = TypeSpec.classBuilder(CLASS_PAGE_GEN_TRANS_ANIM_KEY)
                    .addJavadoc(PAGE_MODULE_WARNING_TIP)
                    .addFields(fieldSpecs)
                    .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                    .build();

            // 1.3 : create file for "PageTransAnimKeyConst"
            JavaFile.builder(PageGenPackageConst.PAGE_MODULE_MAIN_PACKAGE + ".factory", clazz)
                    .addFileComment(PAGE_MODULE_WARNING_TIP)
                    .build()
                    .writeTo(filer);

            // 2.2 : create animation holder
            WildcardTypeName wildTypeName = WildcardTypeName.subtypeOf(COMMON_CLASS_PAGE_TRANS_ANIMATION);
            String singleStaticFieldName = "pageAnimationHolder";
            FieldSpec pageAnimationField = FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class), wildTypeName)), singleStaticFieldName)
                    .addModifiers(Modifier.STATIC, Modifier.PRIVATE)
                    .addJavadoc("hold the animation-class")
                    .build();

            // 2.3 : create "init" method ,inject animation-key to "fieldName"
            MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addComment("To facilitate processing, all classes are loaded directly, but object instantiation is delayed.")
                    .beginControlFlow("if ($L == null)", singleStaticFieldName)
                    .addStatement("$L = new $T<>()", singleStaticFieldName, HashMap.class);
            keyClazz.forEach((s, typeElement1) -> initMethodBuilder.addStatement("$L.put($L.$L, $T.class)", singleStaticFieldName, CLASS_PAGE_GEN_TRANS_ANIM_KEY, s, typeElement1));
            MethodSpec initMethod = initMethodBuilder.endControlFlow().build();

            // 2.4 : create
            MethodSpec createAnimMethod = MethodSpec.methodBuilder("createPageTransAnimation")
                    .addModifiers(Modifier.STATIC, Modifier.SYNCHRONIZED)
                    .returns(COMMON_CLASS_PAGE_TRANS_ANIMATION)
                    .addParameter(ParameterSpec.builder(String.class, "key").addAnnotation(ProcessorConst.COMMON_ANNOTATION_NONNULL).build())
                    .addStatement("$N()", initMethod)
                    .addStatement("Class<? extends $T> clazz = $L.get(key)", ProcessorConst.COMMON_CLASS_PAGE_TRANS_ANIMATION, singleStaticFieldName)
                    .beginControlFlow("try")
                    .addStatement("$T<? extends PageTransAnimation> declaredConstructor = clazz.getDeclaredConstructor()", Constructor.class)
                    .addStatement("declaredConstructor.setAccessible(true)")
                    .addStatement("return declaredConstructor.newInstance()")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("return null")
                    .endControlFlow()
                    .build();

            // 2.5 build type
            TypeSpec providerClass = TypeSpec.classBuilder(CLASS_PAGE_GEN_TRANS_ANIM_PROVIDER)
                    .addJavadoc(PAGE_MODULE_WARNING_TIP)
                    .addField(pageAnimationField)
                    .addMethod(initMethod)
                    .addMethod(createAnimMethod)
                    .addModifiers(Modifier.FINAL)
                    .build();

            // 2.5 build file
            JavaFile.builder(PageGenPackageConst.PAGE_MODULE_MAIN_PACKAGE + ".factory", providerClass)
                    .addFileComment(PAGE_MODULE_WARNING_TIP)
                    .build()
                    .writeTo(filer);
        } catch (Exception err) {
            messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " :  " + err.toString());
        }
        return true;
    }
}