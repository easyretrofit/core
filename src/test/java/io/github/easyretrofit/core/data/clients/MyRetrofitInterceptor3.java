package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author liuziyuan
 * @date 1/5/2022 5:44 PM
 */
public class MyRetrofitInterceptor3 extends BaseInterceptor {

    public MyRetrofitInterceptor3(RetrofitResourceContext context) {
        super(context);
    }

    @Override
    protected Response executeIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
