package io.github.easyretrofit.core.exception;


import io.github.easyretrofit.core.resource.RetrofitApiServiceBean;
import okhttp3.Request;

/**
 * Easy Retrofit Extension Exception, when extension exception occurred, it needs throw this exception.
 * @author liuziyuan
 */
public class RetrofitExtensionException extends RuntimeException {

    protected final RetrofitApiServiceBean retrofitApiServiceBean;

    protected final Request request;

    public RetrofitExtensionException(String message, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }

    public RetrofitExtensionException(String message, Throwable cause, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(message, cause);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }

    public RetrofitExtensionException(Throwable cause, RetrofitApiServiceBean retrofitApiServiceBean, Request request) {
        super(cause);
        this.retrofitApiServiceBean = retrofitApiServiceBean;
        this.request = request;
    }

    public RetrofitApiServiceBean getRetrofitApiServiceBean() {
        return retrofitApiServiceBean;
    }

    public Request getRequest() {
        return request;
    }
}
