package io.github.easyretrofit.core.data.builder;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.data.common.*;

@RetrofitBuilder(baseUrl = "http://localhost:8100",
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class, GuavaCallAdapterFactoryBuilder.class},
        addConverterFactory = {GsonConvertFactoryBuilder.class, JacksonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder2.class)
public class BuilderApi2 {
}
