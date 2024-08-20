package io.github.easyretrofit.core.data.builder;

import io.github.easyretrofit.core.RetrofitBuilderExtension;
import io.github.easyretrofit.core.builder.*;

public class GlobalRetrofitBuilderExtension implements RetrofitBuilderExtension {
    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public String globalBaseUrl() {
        return "";
    }

    @Override
    public Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz() {
        return new Class[0];
    }

    @Override
    public Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz() {
        return new Class[0];
    }

    @Override
    public Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz() {
        return null;
    }

    @Override
    public Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz() {
        return null;
    }

    @Override
    public Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz() {
        return null;
    }

    @Override
    public String globalValidateEagerly() {
        return "";
    }
}
