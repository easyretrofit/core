package io.github.easyretrofit.core;

import io.github.easyretrofit.core.builder.*;

/**
 * for global Retrofit Builder , Just one instance
 */
public interface RetrofitBuilderExtension {

    boolean enable();

    String globalBaseUrl();

    Class<? extends BaseCallAdapterFactoryBuilder>[] globalCallAdapterFactoryBuilderClazz();

    Class<? extends BaseConverterFactoryBuilder>[] globalConverterFactoryBuilderClazz();

    Class<? extends BaseOkHttpClientBuilder> globalOkHttpClientBuilderClazz();

    Class<? extends BaseCallBackExecutorBuilder> globalCallBackExecutorBuilderClazz();

    Class<? extends BaseCallFactoryBuilder> globalCallFactoryBuilderClazz();

    String globalValidateEagerly();
}
