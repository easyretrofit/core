package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;


public abstract class BaseExceptionDelegate<T extends RetrofitExtensionException> implements ExceptionDelegate<RetrofitExtensionException> {
    private final Class<T> exceptionClass;

    public BaseExceptionDelegate(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public Class<T> getExceptionClass() {
        return exceptionClass;
    }
}
