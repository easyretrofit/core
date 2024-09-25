//package io.github.easyretrofit.core.proxy;
//
//import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
//import io.github.easyretrofit.core.delegate.CglibProxyExceptionHandler;
//import io.github.easyretrofit.core.delegate.JdkProxyExceptionHandler;
//import io.github.easyretrofit.core.exception.RetrofitExtensionException;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//import java.util.Set;
//
//public class RetrofitExtensionFallbackCglibInvocationHandler<T> implements MethodInterceptor {
//
//    private T t;
//    private final Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates;
//
//    public RetrofitExtensionFallbackCglibInvocationHandler(T t, Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates) {
//        this.exceptionDelegates = exceptionDelegates;
//        this.t = t;
//    }
//
//    @Override
//    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
//        try {
//            return method.invoke(t, args);
//        } catch (Exception e) {
//            CglibProxyExceptionHandler cglibProxyExceptionHandler = new CglibProxyExceptionHandler(exceptionDelegates);
//            return cglibProxyExceptionHandler.handle(proxy, method, args, e, methodProxy);
//        }
//    }
//}
