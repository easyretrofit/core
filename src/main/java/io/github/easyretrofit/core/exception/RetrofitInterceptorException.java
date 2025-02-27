package io.github.easyretrofit.core.exception;


import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import okhttp3.Request;

import java.io.IOException;

/**
 * Easy Retrofit Extension Exception, when extension exception occurred, it needs throw this exception.
 * 非常重要的的, Retrofit Interceptor 一定要抛出IOException 才不会出现Exception in thread "OkHttp Dispatcher"
 * @author liuziyuan
 */
public class RetrofitInterceptorException extends IOException {

    protected final RetrofitApiInterfaceBean retrofitApiInterfaceBean;

    protected final Request request;

    public RetrofitInterceptorException(String message, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(message);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitInterceptorException(String message, Throwable cause, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(message, cause);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitInterceptorException(Throwable cause, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(cause);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

//    public RetrofitExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
//        super(message, cause, enableSuppression, writableStackTrace);
//        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
//        this.request = request;
//    }

    public RetrofitApiInterfaceBean getRetrofitApiServiceBean() {
        return retrofitApiInterfaceBean;
    }

    public Request getRequest() {
        return request;
    }
}
