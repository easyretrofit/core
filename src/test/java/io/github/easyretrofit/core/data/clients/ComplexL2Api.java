package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.data.common.MyRetrofitInterceptor2;

@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)
public interface ComplexL2Api extends ComplexApi {
}
