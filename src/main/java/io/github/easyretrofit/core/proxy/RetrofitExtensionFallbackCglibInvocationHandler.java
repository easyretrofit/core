package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Set;

public class RetrofitExtensionFallbackCglibInvocationHandler implements MethodInterceptor {

    private final Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates;

    public RetrofitExtensionFallbackCglibInvocationHandler(Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates) {
        this.exceptionDelegates = exceptionDelegates;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}
