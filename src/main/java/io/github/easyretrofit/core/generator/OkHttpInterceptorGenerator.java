package io.github.easyretrofit.core.generator;

import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.resource.RetrofitInterceptorBean;
import okhttp3.Interceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Generate OkHttpInterceptor instance
 *
 * @author liuziyuan
 */
public abstract class OkHttpInterceptorGenerator implements Generator<Interceptor> {
    private final Class<? extends BaseInterceptor> interceptorClass;
    private final RetrofitInterceptorBean retrofitInterceptor;
    private final RetrofitResourceContext resourceContext;
    private BaseInterceptor interceptor;

    public OkHttpInterceptorGenerator(RetrofitInterceptorBean retrofitInterceptor, RetrofitResourceContext resourceContext) {
        this.retrofitInterceptor = retrofitInterceptor;
        this.interceptorClass = retrofitInterceptor.getHandler();
        this.resourceContext = resourceContext;
        this.interceptor = null;
    }

    public abstract BaseInterceptor buildInjectionObject(Class<? extends BaseInterceptor> clazz);

    @Override
    public Interceptor generate() {
        interceptor = buildInjectionObject(retrofitInterceptor.getHandler());
        if (interceptor == null && interceptorClass != null) {
            Constructor<? extends BaseInterceptor> constructor;
            BaseInterceptor interceptorInstance;
            try {
                constructor = interceptorClass.getConstructor(RetrofitResourceContext.class);
                interceptorInstance = constructor.newInstance(resourceContext);
            } catch (NoSuchMethodException exception) {
                try {
                    interceptorInstance = interceptorClass.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            interceptor = interceptorInstance;
        }
        if (interceptor != null) {
            interceptor.setInclude(retrofitInterceptor.getInclude());
            interceptor.setExclude(retrofitInterceptor.getExclude());
        }
        return interceptor;

    }
}
