package io.github.easyretrofit.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibDynamicProxy {

    public static <T> T create(Class<?> clazz,
                               MethodInterceptor invocationHandler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(invocationHandler);
        return (T) enhancer.create();
    }
}
