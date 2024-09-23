package io.github.easyretrofit.core.extension;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.util.AntPathMatcher;
import io.github.easyretrofit.core.util.PathMatcher;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class of Interceptor, The inner interceptor needs to inherit it
 *
 * @author liuziyuan
 */
public abstract class BaseInterceptor implements Interceptor, Cloneable {

    protected RetrofitResourceContext context;
    private Class<?>[] defaultScopeClasses;
    private String[] include;
    private String[] exclude;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private Chain chain;


    protected BaseInterceptor() {
    }

    protected BaseInterceptor(RetrofitResourceContext context) {
        this.context = context;
    }

    @Override
    public final Response intercept(Chain chain) throws IOException {
        this.chain = chain;
        Request request = chain.request();
        String path = request.url().encodedPath();
        Method method = this.getRequestMethod(request);
        String clazzName = this.getClazzNameByMethod(method);
        if (!functionInDefaultScope(clazzName) || functionInDefaultScope(clazzName) && isMatch(exclude, path)) {
            return chain.proceed(request);
        }
        if (!functionInDefaultScope(clazzName) || functionInDefaultScope(clazzName) && include != null && !isMatch(include, path)) {
            return chain.proceed(request);
        }
        return executeIntercept(chain);
    }

    /**
     * execute intercept for OKHttpClient Interceptor
     *
     * @param chain Chain
     * @return Response
     */
    protected abstract Response executeIntercept(Chain chain) throws IOException;

    /**
     * get RetrofitResourceContext when interceptor using DI inject RetrofitResourceContext Object<br>
     * if the interceptor does not use DI, return null<br>
     *
     * @return RetrofitResourceContext injected through DI
     */
    protected abstract RetrofitResourceContext getInjectedRetrofitResourceContext();

    private boolean functionInDefaultScope(String className) {
        if (defaultScopeClasses == null) {
            return true;
        }
        for (Class<?> defaultScopeClass : defaultScopeClasses) {
            if (defaultScopeClass.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatch(String[] patterns, String path) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            boolean match = pathMatcher.match(pattern, path);
            if (match) {
                return true;
            }
        }
        return false;
    }

    public void setInclude(String[] include) {
        this.include = include;
    }

    public void setExclude(String[] exclude) {
        this.exclude = exclude;
    }

    public void setDefaultScopeClasses(Class<?>[] defaultScopeClasses) {
        this.defaultScopeClasses = defaultScopeClasses;
    }

    /**
     * Obtain the class name of the class where the method is located through the method
     *
     * @param method current request method
     * @return class name
     */
    protected String getClazzNameByMethod(Method method) {
        return method.getDeclaringClass().getName();
    }

    protected Class<?> getClassByMethod(Method method) {
        return method.getDeclaringClass();
    }

    /**
     * Obtain the method of the request location through the request
     *
     * @param request current request
     * @return Method of the request
     */
    protected Method getRequestMethod(Request request) {
        return Objects.requireNonNull(request.tag(Invocation.class)).method();
    }

    /**
     * merged Injected RetrofitResourceContext and default RetrofitResourceContext<br>
     * when the interceptor does not use DI, return default RetrofitResourceContext
     *
     * @return
     */
    protected RetrofitResourceContext getMergedRetrofitResourceContext() {
        RetrofitResourceContext extensionContext = getInjectedRetrofitResourceContext();
        if (context == null && extensionContext != null) {
            return extensionContext;
        }
        return context;
    }

    protected List<Annotation> getAnnotationFromMethod(Class<? extends Annotation> annotationClazz) {
        List<Annotation> annotations = new ArrayList<>();
        if (chain != null) {
            Request request = chain.request();
            final Method method = this.getRequestMethod(request);
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if (declaredAnnotation.annotationType().equals(annotationClazz)) {
                    annotations.add(declaredAnnotation);
                }
            }
        }
        return annotations;
    }

    protected List<Annotation> getAnnotationFromCurrentClass(Class<? extends Annotation> annotationClazz) {
        List<Annotation> annotations = new ArrayList<>();
        if (chain != null) {
            Request request = chain.request();
            final Method method = this.getRequestMethod(request);
            Class<?> clazz = this.getClassByMethod(method);
            Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                if (declaredAnnotation.annotationType().equals(annotationClazz)) {
                    annotations.add(declaredAnnotation);
                }
            }
        }
        return annotations;
    }

    protected List<Annotation> getAnnotationFromParentClass(Class<? extends Annotation> annotationClazz) {
        List<Annotation> annotations = new ArrayList<>();
        if (chain != null) {
            Request request = chain.request();
            final Method method = this.getRequestMethod(request);
            Class<?> clazz = this.getClassByMethod(method);
            final RetrofitApiInterfaceBean currentServiceBean = getMergedRetrofitResourceContext().getRetrofitApiInterfaceBean(clazz.getName());
            for (Class<?> parentClass : currentServiceBean.getSelf2ParentClasses()) {
                Annotation annotation = parentClass.getAnnotation(annotationClazz);
                if (annotation != null && self2ParentHasAnnotation(currentServiceBean.getSelf2ParentClasses(), annotationClazz)) {
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

    protected Annotation getExtensionAnnotation(Class<? extends Annotation> annotationClazz) {
        Annotation annotation = null;
        if (chain != null) {
            Request request = chain.request();
            final Method method = this.getRequestMethod(request);
            String clazzName = this.getClazzNameByMethod(method);
            final RetrofitApiInterfaceBean currentServiceBean = getMergedRetrofitResourceContext().getRetrofitApiInterfaceBean(clazzName);
            for (Class<?> parentClass : currentServiceBean.getSelf2ParentClasses()) {
                annotation = parentClass.getAnnotation(annotationClazz);
                if (annotation != null && self2ParentHasAnnotation(currentServiceBean.getSelf2ParentClasses(), annotationClazz))
                    return annotation;
            }
        }
        return annotation;
    }

    private boolean self2ParentHasAnnotation(LinkedHashSet<Class<?>> self2ParentClasses, Class<? extends Annotation> annotationClazz) {
        for (Class<?> clazz : self2ParentClasses) {
            RetrofitApiInterfaceBean retrofitApiInterfaceBean = getMergedRetrofitResourceContext().getRetrofitApiInterfaceBean(clazz);
            Annotation annotationResource = retrofitApiInterfaceBean.getAnnotationResource(annotationClazz);
            if (annotationResource != null && annotationResource.annotationType().equals(annotationClazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BaseInterceptor clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (BaseInterceptor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
