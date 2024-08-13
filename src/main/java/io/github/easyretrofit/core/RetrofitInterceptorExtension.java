package io.github.easyretrofit.core;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.proxy.BaseExceptionDelegate;

import java.lang.annotation.Annotation;

public interface RetrofitInterceptorExtension {

    Class<? extends Annotation> createAnnotation();

    Class<? extends BaseInterceptor> createInterceptor();

    Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate();

}
