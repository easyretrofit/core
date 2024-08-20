package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;

@RetrofitBuilder(baseUrl = "http://localhost:8100")
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
public interface ComplexApi {
}
