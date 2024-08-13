package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.EnvManager;
import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.RetrofitBuilderExtension;
import io.github.easyretrofit.core.RetrofitInterceptorExtension;
import io.github.easyretrofit.core.annotation.*;
import io.github.easyretrofit.core.exception.RetrofitBaseException;
import io.github.easyretrofit.core.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * generate RetrofitServiceBean object
 *
 * @author liuziyuan
 */
public class RetrofitApiServiceBeanGenerator implements Generator<RetrofitApiServiceBean> {
    private final Class<?> clazz;
    private final EnvManager env;
    private final RetrofitBuilderExtension globalRetrofitBuilderExtension;
    private final List<RetrofitInterceptorExtension> interceptorExtensions;

    public RetrofitApiServiceBeanGenerator(Class<?> clazz,
                                           EnvManager env,
                                           RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                           List<RetrofitInterceptorExtension> interceptorExtensions) {
        this.clazz = clazz;
        this.env = env;
        this.globalRetrofitBuilderExtension = globalRetrofitBuilderExtension;
        this.interceptorExtensions = interceptorExtensions;
    }

    @Override
    public RetrofitApiServiceBean generate() {
        Class<?> retrofitBuilderClazz = getParentRetrofitBuilderClazz();
        RetrofitApiServiceBean retrofitApiServiceBean = new RetrofitApiServiceBean();
        retrofitApiServiceBean.setSelfClazz(clazz);
        retrofitApiServiceBean.setParentClazz(retrofitBuilderClazz);
        //将RetrofitBuilder注解信息注入到RetrofitBuilderBean中
        RetrofitBuilderBean retrofitBuilderBean = new RetrofitBuilderBean(retrofitBuilderClazz, globalRetrofitBuilderExtension);
        retrofitApiServiceBean.setRetrofitBuilder(retrofitBuilderBean);
        Set<RetrofitInterceptorBean> interceptors = getInterceptors(retrofitBuilderClazz);
        Set<RetrofitInterceptorBean> myInterceptors = getInterceptors(clazz);
        if (interceptorExtensions != null) {
            for (RetrofitInterceptorExtension interceptorExtension : interceptorExtensions) {
                addExtensionInterceptors(interceptorExtension, retrofitApiServiceBean, retrofitBuilderClazz, myInterceptors);
                addExtensionInterceptors(interceptorExtension, retrofitApiServiceBean, clazz, myInterceptors);
            }
        }
        retrofitApiServiceBean.setMyInterceptors(myInterceptors);
        retrofitApiServiceBean.setInterceptors(interceptors);
        RetrofitUrl retrofitUrl = getRetrofitUrl(retrofitBuilderBean);
        retrofitApiServiceBean.setRetrofitUrl(retrofitUrl);
        return retrofitApiServiceBean;
    }

    private void addExtensionInterceptors(RetrofitInterceptorExtension interceptorExtension, RetrofitApiServiceBean retrofitApiServiceBean, Class<?> apiClazz, Set<RetrofitInterceptorBean> interceptors) {
        try {
            Annotation extensionAnnotation = Arrays.stream(apiClazz.getDeclaredAnnotations()).filter(annotation -> annotation.annotationType() == interceptorExtension.createAnnotation()).findFirst().orElse(null);
            if (extensionAnnotation != null) {
                RetrofitInterceptor interceptorAnnotation = extensionAnnotation.annotationType().getAnnotation(RetrofitInterceptor.class);
                if (interceptorAnnotation != null) {
                    RetrofitInterceptorBean retrofitInterceptorBean = new RetrofitInterceptorBean(interceptorAnnotation);
                    retrofitInterceptorBean = getInterceptorParamsAnnotation(interceptorExtension, apiClazz, interceptorAnnotation, retrofitInterceptorBean);
                    assert Objects.requireNonNull(retrofitInterceptorBean).getHandler() == interceptorExtension.createInterceptor();
                    if (interceptorExtension.createExceptionDelegate() != null) {
                        retrofitApiServiceBean.addExceptionDelegate(interceptorExtension.createExceptionDelegate());
                    }
                    interceptors.add(retrofitInterceptorBean);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private RetrofitInterceptorBean getInterceptorParamsAnnotation(RetrofitInterceptorExtension interceptorExtension, Class<?> apiClazz, RetrofitInterceptor interceptorAnnotation, RetrofitInterceptorBean retrofitInterceptorBean) {
        Annotation declaredAnnotation = apiClazz.getDeclaredAnnotation(interceptorExtension.createAnnotation());
        Method[] methods = declaredAnnotation.getClass().getMethods();
        for (Method method : methods) {
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType == RetrofitInterceptorParam.class) {
                String paramsName = method.getName();
                RetrofitInterceptorParam extensionObj = (RetrofitInterceptorParam) ReflectUtils.getMethodReturnValue(declaredAnnotation, paramsName);
                assert extensionObj != null;
                return new RetrofitInterceptorBean(interceptorAnnotation, extensionObj);
            }
        }
        return retrofitInterceptorBean;
    }


    private RetrofitUrl getRetrofitUrl(RetrofitBuilderBean retrofitBuilderBean) {
        final RetrofitUrlPrefix retrofitUrlPrefix = clazz.getDeclaredAnnotation(RetrofitUrlPrefix.class);
        final RetrofitDynamicBaseUrl retrofitDynamicBaseUrl = clazz.getDeclaredAnnotation(RetrofitDynamicBaseUrl.class);
        String retrofitDynamicBaseUrlValue = retrofitDynamicBaseUrl == null ? null : retrofitDynamicBaseUrl.value();
        return new RetrofitUrl(retrofitBuilderBean.getBaseUrl(),
                retrofitDynamicBaseUrlValue,
                retrofitUrlPrefix == null ? null : retrofitUrlPrefix.value(),
                env);
    }

    private Class<?> getParentRetrofitBuilderClazz() {
        return findParentClazzIncludeRetrofitBuilderAndBase(clazz);
    }

    private Class<?> findParentClazzIncludeRetrofitBuilderAndBase(Class<?> clazz) {
        Class<?> retrofitBuilderClazz;
        if (clazz.getDeclaredAnnotation(RetrofitBase.class) != null) {
            retrofitBuilderClazz = findParentRetrofitBaseClazz(clazz);
        } else {
            retrofitBuilderClazz = findParentRetrofitBuilderClazz(clazz);
        }
        if (retrofitBuilderClazz.getDeclaredAnnotation(RetrofitBuilder.class) == null) {
            retrofitBuilderClazz = findParentClazzIncludeRetrofitBuilderAndBase(retrofitBuilderClazz);
        }
        return retrofitBuilderClazz;
    }

    private Class<?> findParentRetrofitBuilderClazz(Class<?> clazz) {
        RetrofitBuilder retrofitBuilder = clazz.getDeclaredAnnotation(RetrofitBuilder.class);
        Class<?> targetClazz = clazz;
        if (retrofitBuilder == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                targetClazz = findParentRetrofitBuilderClazz(interfaces[0]);
            } else {
                if (clazz.getDeclaredAnnotation(RetrofitBase.class) == null) {
                    throw new RetrofitBaseException("The baseApi of @RetrofitBase in the [" + clazz.getSimpleName() + "] Interface, does not define @RetrofitBuilder");
                }
            }
        }
        return targetClazz;
    }

    private Class<?> findParentRetrofitBaseClazz(Class<?> clazz) {
        RetrofitBase retrofitBase = clazz.getDeclaredAnnotation(RetrofitBase.class);
        Class<?> targetClazz = clazz;
        if (retrofitBase != null) {
            final Class<?> baseApiClazz = retrofitBase.baseInterface();
            if (baseApiClazz != null) {
                targetClazz = findParentRetrofitBaseClazz(baseApiClazz);
            }
        }
        return targetClazz;
    }

    private Set<RetrofitInterceptorBean> getInterceptors(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        Set<RetrofitInterceptorBean> retrofitInterceptorAnnotations = new LinkedHashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Interceptors) {
                RetrofitInterceptor[] values = ((Interceptors) annotation).value();
                for (RetrofitInterceptor retrofitInterceptor : values) {
                    retrofitInterceptorAnnotations.add(new RetrofitInterceptorBean(retrofitInterceptor));
                }
            } else if (annotation instanceof RetrofitInterceptor) {
                retrofitInterceptorAnnotations.add(new RetrofitInterceptorBean((RetrofitInterceptor) annotation));
            }
        }
        return retrofitInterceptorAnnotations;
    }


}
