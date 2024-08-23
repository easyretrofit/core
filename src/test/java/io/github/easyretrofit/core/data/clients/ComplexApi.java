package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.data.common.*;

@RetrofitBuilder(baseUrl = "http://localhost:8100/v1",
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class, GuavaCallAdapterFactoryBuilder.class},
        addConverterFactory = {GsonConvertFactoryBuilder.class, JacksonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder2.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)
public interface ComplexApi {
}
