package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.EnvManager;
import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.RetrofitBuilderExtension;
import io.github.easyretrofit.core.RetrofitInterceptorExtension;
import io.github.easyretrofit.core.annotation.*;
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
public class RetrofitApiInterfaceBeanGenerator implements Generator<RetrofitApiInterfaceBean> {
    PreRetrofitResourceApiInterfaceClassBean bean;
    private final Class<?> clazz;
    private final EnvManager env;
    private final RetrofitBuilderExtension globalRetrofitBuilderExtension;
    private final List<RetrofitInterceptorExtension> interceptorExtensions;

    public RetrofitApiInterfaceBeanGenerator(PreRetrofitResourceApiInterfaceClassBean bean,
                                             EnvManager env,
                                             RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                             List<RetrofitInterceptorExtension> interceptorExtensions) {
        this.bean = bean;
        this.clazz = bean.getMyself();
        this.env = env;
        this.globalRetrofitBuilderExtension = globalRetrofitBuilderExtension;
        this.interceptorExtensions = interceptorExtensions;
    }

    @Override
    public RetrofitApiInterfaceBean generate() {
        // create RetrofitApiInterfaceBean
        RetrofitApiInterfaceBean retrofitApiInterfaceBean = new RetrofitApiInterfaceBean();
        retrofitApiInterfaceBean.setSelfClazz(bean.getMyself());
        retrofitApiInterfaceBean.setParentClazz(bean.getAncestor());
        retrofitApiInterfaceBean.setSelf2ParentClasses(bean.getSelf2Ancestors());
        retrofitApiInterfaceBean.setChildrenClasses(bean.getChildren());
        //将RetrofitBuilder注解信息注入到RetrofitBuilderBean中
        RetrofitBuilderBean retrofitBuilderBean = new RetrofitBuilderBean(bean.getAncestor(), globalRetrofitBuilderExtension);
        retrofitApiInterfaceBean.setRetrofitBuilder(retrofitBuilderBean);
        Set<RetrofitInterceptorBean> myInterceptors = getInterceptors(bean);
        if (interceptorExtensions != null) {
            for (RetrofitInterceptorExtension interceptorExtension : interceptorExtensions) {
                addExtensionInterceptors(interceptorExtension, retrofitApiInterfaceBean, clazz, myInterceptors);
            }
        }
        retrofitApiInterfaceBean.setMyInterceptors(myInterceptors);
        RetrofitUrl retrofitUrl = getRetrofitUrl(retrofitBuilderBean);
        retrofitApiInterfaceBean.setRetrofitUrl(retrofitUrl);
        return retrofitApiInterfaceBean;
    }

    private void addExtensionInterceptors(RetrofitInterceptorExtension interceptorExtension, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Class<?> apiClazz, Set<RetrofitInterceptorBean> interceptors) {
        try {
            Annotation extensionAnnotation = Arrays.stream(apiClazz.getDeclaredAnnotations()).filter(annotation -> annotation.annotationType() == interceptorExtension.createAnnotation()).findFirst().orElse(null);
            if (extensionAnnotation != null) {
                RetrofitInterceptor interceptorAnnotation = extensionAnnotation.annotationType().getAnnotation(RetrofitInterceptor.class);
                if (interceptorAnnotation != null) {
                    RetrofitInterceptorBean retrofitInterceptorBean = new RetrofitInterceptorBean(interceptorAnnotation, bean.getChildren());
                    retrofitInterceptorBean = getInterceptorParamsAnnotation(interceptorExtension, apiClazz, interceptorAnnotation, retrofitInterceptorBean);
                    assert Objects.requireNonNull(retrofitInterceptorBean).getHandler() == interceptorExtension.createInterceptor();
                    if (interceptorExtension.createExceptionDelegate() != null) {
                        retrofitApiInterfaceBean.addExceptionDelegate(interceptorExtension.createExceptionDelegate());
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
                return new RetrofitInterceptorBean(interceptorAnnotation, extensionObj, bean.getChildren());
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


    private Set<RetrofitInterceptorBean> getInterceptors(PreRetrofitResourceApiInterfaceClassBean bean) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        Set<RetrofitInterceptorBean> retrofitInterceptorAnnotations = new LinkedHashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Interceptors) {
                RetrofitInterceptor[] values = ((Interceptors) annotation).value();
                for (RetrofitInterceptor retrofitInterceptor : values) {
                    retrofitInterceptorAnnotations.add(new RetrofitInterceptorBean(retrofitInterceptor, bean.getChildren()));
                }
            } else if (annotation instanceof RetrofitInterceptor) {
                retrofitInterceptorAnnotations.add(new RetrofitInterceptorBean((RetrofitInterceptor) annotation, bean.getChildren()));
            }
        }
        return retrofitInterceptorAnnotations;
    }


}
