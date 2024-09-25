package io.github.easyretrofit.core.delegate;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ExceptionDelegateSetGenerator {

    public static Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> generate(
            Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegateSet,
            Function<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>, BaseExceptionDelegate<? extends RetrofitExtensionException>> function) {
        Set<BaseExceptionDelegate<? extends RetrofitExtensionException>> exceptionDelegates = new HashSet<>();
        if (exceptionDelegateSet != null) {
            for (Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> entry : exceptionDelegateSet) {
                BaseExceptionDelegate<? extends RetrofitExtensionException> exceptionDelegate = function.apply(entry);
                exceptionDelegates.add(exceptionDelegate);
            }
        }

        return exceptionDelegates;
    }
}
