package io.github.easyretrofit.core;

import io.github.easyretrofit.core.extension.BaseInterceptor;
//import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;

import java.lang.annotation.Annotation;

public interface RetrofitInterceptorExtension {

    Class<? extends Annotation> createAnnotation();

    Class<? extends BaseInterceptor> createInterceptor();

    // 不再需要委托扩展做定制化的异常委托处理
//    Class<? extends BaseExceptionDelegate<? extends RetrofitInterceptorException>> createExceptionDelegate();

}
