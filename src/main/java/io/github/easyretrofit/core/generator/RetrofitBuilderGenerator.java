package io.github.easyretrofit.core.generator;

import io.github.easyretrofit.core.CDIBeanManager;
import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.annotation.InterceptorType;
import io.github.easyretrofit.core.builder.*;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.extension.DynamicBaseUrlInterceptor;
import io.github.easyretrofit.core.extension.UrlOverWriteInterceptor;
import io.github.easyretrofit.core.resource.RetrofitBuilderBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitInterceptorBean;
import io.github.easyretrofit.core.util.CollectionUtils;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Generate RetrofitBuilder instance
 *
 * @author liuziyuan
 */
public final class RetrofitBuilderGenerator implements Generator<Retrofit.Builder> {
    private final RetrofitClientBean clientBean;
    private final RetrofitResourceContext context;
    private final Retrofit.Builder builder;

    private final CDIBeanManager cdiBeanManager;


    public RetrofitBuilderGenerator(RetrofitClientBean clientBean, RetrofitResourceContext context, CDIBeanManager cdiBeanManager) {
        this.builder = new Retrofit.Builder();
        this.clientBean = clientBean;
        this.context = context;
        this.cdiBeanManager = cdiBeanManager;
    }

    @Override
    public Retrofit.Builder generate() {
        setBaseUrl();
        setRetrofitCallAdapterFactory();
        setRetrofitConverterFactory();
        setCallBackExecutor();
        setValidateEagerly();
        setRetrofitOkHttpClient();
        setCallFactory();
        return builder;
    }


    private void setBaseUrl() {
        builder.baseUrl(clientBean.getRealHostUrl());
    }

    private void setValidateEagerly() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        builder.validateEagerly(retrofitBuilder.isValidateEagerly());
    }

    private void setCallFactory() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        final Class<? extends BaseCallFactoryBuilder> callFactoryBuilderClazz = retrofitBuilder.getCallFactory();
        CallFactoryGenerator callFactoryGenerator = new CallFactoryGenerator(callFactoryBuilderClazz) {
            @Override
            public BaseCallFactoryBuilder buildInjectionObject(Class<? extends BaseCallFactoryBuilder> clazz) {
                return cdiBeanManager.getBean(clazz);
            }
        };
        final Call.Factory factory = callFactoryGenerator.generate();
        if (factory != null) {
            builder.callFactory(factory);
        }
    }

    private void setCallBackExecutor() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        final Class<? extends BaseCallBackExecutorBuilder> callbackExecutorBuilderClazz = retrofitBuilder.getCallbackExecutor();
        CallBackExecutorGenerator callBackExecutorGenerator = new CallBackExecutorGenerator(callbackExecutorBuilderClazz) {
            @Override
            public BaseCallBackExecutorBuilder buildInjectionObject(Class<? extends BaseCallBackExecutorBuilder> clazz) {
                return cdiBeanManager.getBean(clazz);
            }
        };
        final Executor executor = callBackExecutorGenerator.generate();
        if (executor != null) {
            builder.callbackExecutor(executor);
        }
    }

    private void setRetrofitCallAdapterFactory() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<CallAdapter.Factory> callAdapterFactories = getCallAdapterFactories(retrofitBuilder.getAddCallAdapterFactory());
        if (!CollectionUtils.isEmpty(callAdapterFactories)) {
            callAdapterFactories.forEach(builder::addCallAdapterFactory);
        }
    }

    private void setRetrofitConverterFactory() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        final List<Converter.Factory> converterFactories = getConverterFactories(retrofitBuilder.getAddConverterFactory());
        if (!CollectionUtils.isEmpty(converterFactories)) {
            converterFactories.forEach(builder::addConverterFactory);
        }
    }

    private void setRetrofitOkHttpClient() {
        final RetrofitBuilderBean retrofitBuilder = clientBean.getRetrofitBuilder();
        Set<RetrofitInterceptorBean> allInterceptors = new LinkedHashSet<>(clientBean.getInterceptors());
        final List<RetrofitInterceptorBean> interceptors = new ArrayList<>(allInterceptors);
        OkHttpClient.Builder okHttpClientBuilder;
        if (retrofitBuilder.getClient() != null) {
            final OkHttpClientBuilderGenerator clientBuilderGenerator = new OkHttpClientBuilderGenerator(retrofitBuilder.getClient()) {
                @Override
                public BaseOkHttpClientBuilder buildInjectionObject(Class<? extends BaseOkHttpClientBuilder> clazz) {
                    return cdiBeanManager.getBean(clazz);
                }
            };
            okHttpClientBuilder = clientBuilderGenerator.generate();
        } else {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }
        okHttpClientBuilder.addInterceptor(new DynamicBaseUrlInterceptor(context));
        okHttpClientBuilder.addInterceptor(new UrlOverWriteInterceptor(context));
        final List<Interceptor> okHttpDefaultInterceptors = getOkHttpInterceptors(interceptors, InterceptorType.DEFAULT);
        final List<Interceptor> okHttpNetworkInterceptors = getOkHttpInterceptors(interceptors, InterceptorType.NETWORK);
        okHttpDefaultInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        okHttpNetworkInterceptors.forEach(okHttpClientBuilder::addNetworkInterceptor);
        builder.client(okHttpClientBuilder.build());
    }

    private List<Interceptor> getOkHttpInterceptors(List<RetrofitInterceptorBean> interceptors, InterceptorType type) {
        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpInterceptorGenerator okHttpInterceptorGenerator;
        interceptors.sort(Comparator.comparing(RetrofitInterceptorBean::getSort));
        for (RetrofitInterceptorBean interceptor : interceptors) {
            if (interceptor.getType() == type) {
                okHttpInterceptorGenerator = new OkHttpInterceptorGenerator(interceptor, context) {
                    @Override
                    public BaseInterceptor buildInjectionObject(Class<? extends BaseInterceptor> clazz) {
                        return cdiBeanManager.getBean(clazz);
                    }
                };
                final Interceptor generateInterceptor = okHttpInterceptorGenerator.generate();
                interceptorList.add(generateInterceptor);
            }
        }
        return interceptorList;
    }

    private List<CallAdapter.Factory> getCallAdapterFactories(Class<? extends BaseCallAdapterFactoryBuilder>[] callAdapterFactoryClasses) {
        List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        CallAdapterFactoryGenerator callAdapterFactoryGenerator;
        for (Class<? extends BaseCallAdapterFactoryBuilder> callAdapterFactoryClazz : callAdapterFactoryClasses) {
            callAdapterFactoryGenerator = new CallAdapterFactoryGenerator(callAdapterFactoryClazz) {
                @Override
                public BaseCallAdapterFactoryBuilder buildInjectionObject(Class<? extends BaseCallAdapterFactoryBuilder> clazz) {
                    return cdiBeanManager.getBean(clazz);
                }
            };
            callAdapterFactories.add(callAdapterFactoryGenerator.generate());
        }
        return callAdapterFactories;
    }

    private List<Converter.Factory> getConverterFactories(Class<? extends BaseConverterFactoryBuilder>[] converterFactoryBuilderClasses) {
        List<Converter.Factory> converterFactories = new ArrayList<>();
        ConverterFactoryGenerator converterFactoryGenerator;
        for (Class<? extends BaseConverterFactoryBuilder> converterFactoryBuilderClazz : converterFactoryBuilderClasses) {
            converterFactoryGenerator = new ConverterFactoryGenerator(converterFactoryBuilderClazz) {
                @Override
                public BaseConverterFactoryBuilder buildInjectionObject(Class<? extends BaseConverterFactoryBuilder> clazz) {
                    return cdiBeanManager.getBean(clazz);
                }
            };
            converterFactories.add(converterFactoryGenerator.generate());
        }
        return converterFactories;
    }

}
