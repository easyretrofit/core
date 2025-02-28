package io.github.easyretrofit.core.delegate;

import io.github.easyretrofit.core.exception.RetrofitRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JdkProxyExceptionHandler {

    private final Object fallBackBean;

    public JdkProxyExceptionHandler(Object fallBackBean) {
        this.fallBackBean = fallBackBean;
    }


    public Object handle(Object proxy, Method method, Object[] args, Throwable throwable) throws Throwable {
        Throwable cause = throwable.getCause();

        if (cause instanceof RetrofitRuntimeException) {
            return getProxyExceptionObject(proxy, method, args, cause);
        } else if (cause.getCause() instanceof RetrofitRuntimeException) {
            return getProxyExceptionObject(proxy, method, args, cause.getCause());
        } else if (cause instanceof RuntimeException) {
            return getProxyExceptionObject(proxy, method, args, new RetrofitRuntimeException(cause, null, null) );
        }
        throw throwable;
    }

    private Object getProxyExceptionObject(Object proxy, Method method, Object[] args, Throwable cause) throws Throwable {
        Object exObj = null;
        if (fallBackBean != null) {
            Class<?> fallbackClazz = fallBackBean.getClass();
            Class<?>[] parameterTypes = method.getParameterTypes();
            int i = parameterTypes.length + 1;
            Class<?>[] parameterTypesAndEx = new Class[parameterTypes.length + 1];
            parameterTypesAndEx[i - 1] = RetrofitRuntimeException.class;
            Method fallbackMethod;
            try {
                fallbackMethod = fallbackClazz.getDeclaredMethod(method.getName(), parameterTypesAndEx);
                Object[] newArgs = new Object[parameterTypes.length + 1];
                if (args != null) {
                    System.arraycopy(args, 0, newArgs, 0, parameterTypes.length);
                }
                newArgs[parameterTypes.length] = cause;
                return fallbackMethod.invoke(fallBackBean, newArgs);
            } catch (NoSuchMethodException e) {
                try {
                    fallbackMethod = fallbackClazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    return fallbackMethod.invoke(fallBackBean, args);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw cause;
        }

    }


}
