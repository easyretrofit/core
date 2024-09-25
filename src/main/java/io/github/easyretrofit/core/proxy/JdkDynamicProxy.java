package io.github.easyretrofit.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkDynamicProxy {

    public static <T> T create(ClassLoader loader,
                               Class<?>[] interfaces,
                               InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(loader, interfaces, invocationHandler);
    }
}
