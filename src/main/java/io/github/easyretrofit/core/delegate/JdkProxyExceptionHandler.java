package io.github.easyretrofit.core.delegate;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;

import java.lang.reflect.Method;
import java.util.Set;

public class JdkProxyExceptionHandler {

    private final Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates;

    public JdkProxyExceptionHandler(Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates) {
        this.exceptionDelegates = exceptionDelegates;
    }


    public Object handle(Object proxy, Method method, Object[] args, Throwable throwable) throws Throwable {
        Throwable cause = throwable.getCause();
        if (cause instanceof RetrofitExtensionException) {
            return getProxyExceptionObject(proxy, method, args, cause);
        }
        throw throwable;
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
