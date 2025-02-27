package io.github.easyretrofit.core.proxy;

import io.github.easyretrofit.core.delegate.JdkProxyExceptionHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * The dynamic proxy of RetrofitService
 *
 * @author liuziyuan
 */
public class RetrofitApiInterfaceInvocationHandler<T> implements InvocationHandler {

    private final T t;

    private final Object fallBackBean;

    public RetrofitApiInterfaceInvocationHandler(T t, Object fallBackBean) {
        this.t = t;
        this.fallBackBean = fallBackBean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(t, args);
        } catch (Exception e) {
            JdkProxyExceptionHandler jdkProxyExceptionHandler = new JdkProxyExceptionHandler(fallBackBean);
            return jdkProxyExceptionHandler.handle(proxy, method, args, e);
        }
    }


}
