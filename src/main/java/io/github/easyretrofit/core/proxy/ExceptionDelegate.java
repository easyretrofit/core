package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.Method;

public interface ExceptionDelegate<T extends RetrofitExtensionException> {

    Object invoke(Object proxy, Method method, Object[] args, RetrofitExtensionException throwable);
}
