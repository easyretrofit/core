package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
import io.github.easyretrofit.core.delegate.ExceptionDelegator;
import io.github.easyretrofit.core.delegate.ProxyExceptionHandler;
import io.github.easyretrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * The dynamic proxy of RetrofitService
 *
 * @author liuziyuan
 */
public class RetrofitServiceProxy<T> implements InvocationHandler {

    private final T t;

    private final Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates;

    public RetrofitServiceProxy(T t, Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates) {
        this.t = t;
        this.exceptionDelegates = exceptionDelegates;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(t, args);
        } catch (Exception e) {
            ProxyExceptionHandler proxyExceptionHandler = new ProxyExceptionHandler(exceptionDelegates);
            return proxyExceptionHandler.handle(proxy, method, args, e);
        }
    }


}
