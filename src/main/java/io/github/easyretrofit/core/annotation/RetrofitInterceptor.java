package io.github.easyretrofit.core.annotation;

import io.github.easyretrofit.core.extension.BaseInterceptor;

import java.lang.annotation.*;

/**
 * Annotation of OkHttpClient Interceptor
 *
 * @author liuziyuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Interceptors.class)
public @interface RetrofitInterceptor {
    Class<? extends BaseInterceptor> handler();

    InterceptorType type() default InterceptorType.DEFAULT;

    String[] include() default {"/**"};

    String[] exclude() default {};

    int sort() default 0;
}
