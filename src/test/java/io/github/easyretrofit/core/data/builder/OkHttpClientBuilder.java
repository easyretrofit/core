package io.github.easyretrofit.core.data.builder;

import io.github.easyretrofit.core.builder.BaseOkHttpClientBuilder;
import okhttp3.OkHttpClient;

public class OkHttpClientBuilder extends BaseOkHttpClientBuilder {
    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return new OkHttpClient.Builder();
    }
}
