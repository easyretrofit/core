package io.github.easyretrofit.core.annotation;

public @interface RetrofitInterceptorParam {
    InterceptorType type() default InterceptorType.DEFAULT;

    String[] include() default {"/**"};

    String[] exclude() default {};

    int sort() default 0;
}
