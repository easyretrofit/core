package io.github.easyretrofit.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitFallBack {

    Class<?> value();
}
