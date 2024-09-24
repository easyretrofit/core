package io.github.easyretrofit.core.proxy;

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
            Throwable cause = e.getCause();
            if (cause instanceof RetrofitExtensionException) {
                return getProxyExceptionObject(proxy, method, args, cause);
            }
//            else {
//                Throwable targetCause = ((InvocationTargetException) e).getTargetException();
//                if (targetCause instanceof RetrofitExtensionException) {
//                    return getProxyExceptionObject(proxy, method, args, targetCause);
//                }
//            }
            throw cause;
        }
    }

    private Object getProxyExceptionObject(Object proxy, Method method, Object[] args, Throwable cause) {
        Object exObj = null;
        for (BaseExceptionDelegate<? extends RetrofitExtensionException> exceptionDelegate : exceptionDelegates) {
            if (exceptionDelegate.getExceptionClass().isAssignableFrom(cause.getClass())) {
                ExceptionDelegator<? extends Throwable> delegator = new ExceptionDelegator<>(exceptionDelegate);
                Object invoke = delegator.invoke(proxy, method, args, (RetrofitExtensionException) cause);
                if (invoke != null) {
                    exObj = invoke;
                    break;
                }
            }
        }
        return exObj;
    }
}
