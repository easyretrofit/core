package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
import io.github.easyretrofit.core.delegate.JdkProxyExceptionHandler;
import io.github.easyretrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * The dynamic proxy of RetrofitService
 *
 * @author liuziyuan
 */
public class RetrofitApiInterfaceInvocationHandler<T> implements InvocationHandler {

    private final T t;

    private final Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates;

    public RetrofitApiInterfaceInvocationHandler(T t, Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates) {
        this.t = t;
        this.exceptionDelegates = exceptionDelegates;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(t, args);
        } catch (Exception e) {
            JdkProxyExceptionHandler jdkProxyExceptionHandler = new JdkProxyExceptionHandler(exceptionDelegates);
            return jdkProxyExceptionHandler.handle(proxy, method, args, e);
        }
    }


}
