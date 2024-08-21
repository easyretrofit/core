package io.github.easyretrofit.core.data.common;


import io.github.easyretrofit.core.builder.BaseCallAdapterFactoryBuilder;
import retrofit2.CallAdapter;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

/**
 * @author liuziyuan
 */
public class GuavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return GuavaCallAdapterFactory.create();
    }
}
