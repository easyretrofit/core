package io.github.easyretrofit.core.data.common;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:41 PM
 */
public class MyRetrofitInterceptor1 extends BaseInterceptor {
    @Override
    protected Response executeIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }

    @Override
    protected RetrofitResourceContext getInjectedRetrofitResourceContext() {
        return null;
    }
}
