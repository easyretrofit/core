package io.github.easyretrofit.core.generator;

import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * Generate OkHttpClientBuilder instance
 *
 * @author liuziyuan
 */
public abstract class OkHttpClientBuilderGenerator implements Generator<OkHttpClient.Builder> {
    private final Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz;

    public OkHttpClientBuilderGenerator(Class<? extends BaseOkHttpClientBuilder> okHttpClientBuilderClazz) {
        this.okHttpClientBuilderClazz = okHttpClientBuilderClazz;
    }

    public abstract BaseOkHttpClientBuilder buildInjectionObject(Class<? extends BaseOkHttpClientBuilder> clazz);

    @Override
    public OkHttpClient.Builder generate() {

        final BaseOkHttpClientBuilder baseOkHttpClientBuilder = buildInjectionObject(okHttpClientBuilderClazz);
        if (baseOkHttpClientBuilder != null) {
            return baseOkHttpClientBuilder.executeBuild();
        }
        if (okHttpClientBuilderClazz != null) {
            final String okHttpClientBuilderClazzName = okHttpClientBuilderClazz.getName();
            final String baseOkHttpClientBuilderClazzName = BaseOkHttpClientBuilder.class.getName();
            if (okHttpClientBuilderClazzName.equals(baseOkHttpClientBuilderClazzName)) {
                return new OkHttpClient.Builder();
            } else {
                try {
                    return this.okHttpClientBuilderClazz.newInstance().executeBuild();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
