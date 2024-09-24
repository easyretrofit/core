package io.github.easyretrofit.core.extension;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import okhttp3.Request;
import retrofit2.Invocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class InterceptorUtils {

    /**
     * Obtain the class name of the class where the method is located through the method
     *
     * @param method current request method
     * @return class name
     */
    public static String getClazzNameByMethod(Method method) {
        return method.getDeclaringClass().getName();
    }

    public static Class<?> getClassByMethod(Method method) {
        return method.getDeclaringClass();
    }

    public static Class<?> getClassByRequest(Request request) {
        return Objects.requireNonNull(request.tag(Invocation.class)).method().getDeclaringClass();
    }

    /**
     * Obtain the method of the request location through the request
     *
     * @param request current request
     * @return Method of the request
     */
    protected static Method getRequestMethod(Request request) {
        return Objects.requireNonNull(request.tag(Invocation.class)).method();
    }

    public static List<Annotation> getAnnotationFromMethod(Class<? extends Annotation> annotationClazz, Method method) {
        List<Annotation> annotations = new ArrayList<>();
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.annotationType().equals(annotationClazz)) {
                annotations.add(declaredAnnotation);
            }
        }
        return annotations;
    }

    public static List<Annotation> getAnnotationFromCurrentClass(Class<? extends Annotation> annotationClazz, Class<?> clazz) {
        List<Annotation> annotations = new ArrayList<>();
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.annotationType().equals(annotationClazz)) {
                annotations.add(declaredAnnotation);
            }
        }

        return annotations;
    }

    public static List<Annotation> getAnnotationFromParentClass(Class<? extends Annotation> annotationClazz, RetrofitApiInterfaceBean currentApiInterfaceBean) {
        List<Annotation> annotations = new ArrayList<>();
        for (Class<?> parentClass : currentApiInterfaceBean.getSelf2ParentClasses()) {
            Annotation annotation = parentClass.getAnnotation(annotationClazz);
            if (annotation != null && self2ParentHasAnnotation(currentApiInterfaceBean.getSelf2ParentClasses(), annotationClazz, currentApiInterfaceBean)) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    public static Annotation getExtensionAnnotation(Class<? extends Annotation> annotationClazz, RetrofitApiInterfaceBean currentApiInterfaceBean) {
        Annotation annotation = null;
        for (Class<?> parentClass : currentApiInterfaceBean.getSelf2ParentClasses()) {
            annotation = parentClass.getAnnotation(annotationClazz);
            if (annotation != null && self2ParentHasAnnotation(currentApiInterfaceBean.getSelf2ParentClasses(), annotationClazz, currentApiInterfaceBean))
                return annotation;
        }

        return annotation;
    }

    private static boolean self2ParentHasAnnotation(LinkedHashSet<Class<?>> self2ParentClasses, Class<? extends Annotation> annotationClazz, RetrofitApiInterfaceBean currentApiInterfaceBean) {
        for (Class<?> clazz : self2ParentClasses) {
            Annotation annotationResource = currentApiInterfaceBean.getAnnotationResource(annotationClazz);
            if (annotationResource != null && annotationResource.annotationType().equals(annotationClazz)) {
                return true;
            }
        }
        return false;
    }
}
