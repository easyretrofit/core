package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.data.common.MyRetrofitInterceptor3;

@RetrofitInterceptor(handler = MyRetrofitInterceptor3.class)
public interface ComplexL3Api extends ComplexL2Api {
}
