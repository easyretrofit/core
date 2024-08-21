package io.github.easyretrofit.core.data.builder;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.data.common.*;

@RetrofitBuilder(baseUrl = "http://localhost:8100",
        addCallAdapterFactory = {GuavaCallAdapterFactoryBuilder.class, RxJavaCallAdapterFactoryBuilder.class},
        addConverterFactory = {JacksonConvertFactoryBuilder.class, GsonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder.class)
public class BuilderApi {
}
