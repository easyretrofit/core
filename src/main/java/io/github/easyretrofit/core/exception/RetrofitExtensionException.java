package io.github.easyretrofit.core.exception;


import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import okhttp3.Request;

/**
 * Easy Retrofit Extension Exception, when extension exception occurred, it needs throw this exception.
 *
 * @author liuziyuan
 */
public class RetrofitExtensionException extends RuntimeException {

    protected final RetrofitApiInterfaceBean retrofitApiInterfaceBean;

    protected final Request request;

    public RetrofitExtensionException(String message, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(message);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitExtensionException(String message, Throwable cause, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(message, cause);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitExtensionException(Throwable cause, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(cause);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RetrofitApiInterfaceBean retrofitApiInterfaceBean, Request request) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.retrofitApiInterfaceBean = retrofitApiInterfaceBean;
        this.request = request;
    }

    public RetrofitApiInterfaceBean getRetrofitApiServiceBean() {
        return retrofitApiInterfaceBean;
    }

    public Request getRequest() {
        return request;
    }
}
