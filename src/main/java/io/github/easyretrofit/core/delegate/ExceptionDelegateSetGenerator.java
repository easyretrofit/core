//package io.github.easyretrofit.core.delegate;
//
//import io.github.easyretrofit.core.exception.RetrofitInterceptorException;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.function.Function;
//@Deprecated
//public class ExceptionDelegateSetGenerator {
//
//    public static Set<BaseExceptionDelegate<? extends RetrofitInterceptorException>> generate(
//            Set<Class<? extends BaseExceptionDelegate<? extends RetrofitInterceptorException>>> exceptionDelegateSet,
//            Function<Class<? extends BaseExceptionDelegate<? extends RetrofitInterceptorException>>, BaseExceptionDelegate<? extends RetrofitInterceptorException>> function) {
//        Set<BaseExceptionDelegate<? extends RetrofitInterceptorException>> exceptionDelegates = new HashSet<>();
//        if (exceptionDelegateSet != null) {
//            for (Class<? extends BaseExceptionDelegate<? extends RetrofitInterceptorException>> entry : exceptionDelegateSet) {
//                BaseExceptionDelegate<? extends RetrofitInterceptorException> exceptionDelegate = function.apply(entry);
//                exceptionDelegates.add(exceptionDelegate);
//            }
//        }
//
//        return exceptionDelegates;
//    }
//}
