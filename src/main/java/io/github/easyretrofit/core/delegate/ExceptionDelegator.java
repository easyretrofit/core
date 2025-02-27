//package io.github.easyretrofit.core.delegate;
//
//import io.github.easyretrofit.core.exception.RetrofitInterceptorException;
//
//import java.lang.reflect.Method;
//@Deprecated
//public class ExceptionDelegator<T extends RetrofitInterceptorException> implements ExceptionDelegate<T> {
//
//    private final ExceptionDelegate<T> exceptionDelegate;
//
//    public ExceptionDelegator(ExceptionDelegate<T> exceptionDelegate) {
//        this.exceptionDelegate = exceptionDelegate;
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args, RetrofitInterceptorException throwable) {
//        return exceptionDelegate.invoke(proxy, method, args, throwable);
//    }
//}
