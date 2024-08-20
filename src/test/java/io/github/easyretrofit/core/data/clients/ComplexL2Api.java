package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface ComplexL2Api extends ComplexApi {
}
