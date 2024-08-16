package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.builder.BaseCallAdapterFactoryBuilder;
import io.github.easyretrofit.core.builder.BaseConverterFactoryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author liuziyuan
 */
public class RetrofitClientComparer {

    private final RetrofitClientBean sourceBean;
    private final RetrofitClientBean comparerBean;

    public RetrofitClientComparer(RetrofitClientBean sourceBean, RetrofitClientBean comparerBean) {
        this.sourceBean = sourceBean;
        this.comparerBean = comparerBean;
    }

    public boolean okHttpClientInstanceCompare() {
        final String clientBeanRetrofitBuilderOkHttpClientSimpleName = sourceBean.getRetrofitBuilder().getClient().getName();
        final String serviceBeanRetrofitBuilderOkHttpClientSimpleName = comparerBean.getRetrofitBuilder().getClient().getName();
        return StringUtils.equals(clientBeanRetrofitBuilderOkHttpClientSimpleName, serviceBeanRetrofitBuilderOkHttpClientSimpleName);
    }

    public boolean callBackExecutorCompare() {
        final String clientBeanCallBackExecutorName = sourceBean.getRetrofitBuilder().getCallbackExecutor().getName();
        final String serviceBeanCallBackExecutorName = comparerBean.getRetrofitBuilder().getCallbackExecutor().getName();
        return StringUtils.equals(clientBeanCallBackExecutorName, serviceBeanCallBackExecutorName);
    }

    public boolean callFactoryCompare() {
        final String clientBeanCallFactoryName = sourceBean.getRetrofitBuilder().getCallFactory().getName();
        final String serviceBeanCallFactoryName = comparerBean.getRetrofitBuilder().getCallFactory().getName();
        return StringUtils.equals(clientBeanCallFactoryName, serviceBeanCallFactoryName);
    }

    public boolean validateEagerlyCompare() {
        final boolean clientBeanValidateEagerly = sourceBean.getRetrofitBuilder().isValidateEagerly();
        final boolean serviceBeanValidateEagerly = comparerBean.getRetrofitBuilder().isValidateEagerly();
        return clientBeanValidateEagerly == serviceBeanValidateEagerly;
    }

    public boolean callAdapterFactoryCompare() {
        List<String> clientBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends BaseCallAdapterFactoryBuilder> clazz : sourceBean.getRetrofitBuilder().getAddCallAdapterFactory()) {
            clientBeanCallAdapterFactoryList.add(clazz.getName());
        }
        List<String> serviceBeanCallAdapterFactoryList = new ArrayList<>();
        for (Class<? extends BaseCallAdapterFactoryBuilder> clazz : comparerBean.getRetrofitBuilder().getAddCallAdapterFactory()) {
            serviceBeanCallAdapterFactoryList.add(clazz.getName());
        }
        return new HashSet<>(clientBeanCallAdapterFactoryList).equals(new HashSet<>(serviceBeanCallAdapterFactoryList));
    }

    public boolean converterFactoryCompare() {
        List<String> clientBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : sourceBean.getRetrofitBuilder().getAddConverterFactory()) {
            clientBeanConverterFactoryList.add(clazz.getName());
        }
        List<String> serviceBeanConverterFactoryList = new ArrayList<>();
        for (Class<? extends BaseConverterFactoryBuilder> clazz : comparerBean.getRetrofitBuilder().getAddConverterFactory()) {
            serviceBeanConverterFactoryList.add(clazz.getName());
        }
        return new HashSet<>(clientBeanConverterFactoryList).equals(new HashSet<>(serviceBeanConverterFactoryList));
    }

    public boolean interceptorsCompare() {
        List<String> clientBeanInterceptorNameList = new ArrayList<>();
        sourceBean.getInterceptors().forEach(i -> clientBeanInterceptorNameList.add(i.getHandler().getName()));
        List<String> serviceBeanInterceptorNameList = new ArrayList<>();
        comparerBean.getInterceptors().forEach(i -> serviceBeanInterceptorNameList.add(i.getHandler().getName()));
        return new HashSet<>(clientBeanInterceptorNameList).equals(new HashSet<>(serviceBeanInterceptorNameList));
    }

    public boolean hostUrlCompare() {
        return StringUtils.equals(comparerBean.getRealHostUrl(), sourceBean.getRealHostUrl());
    }

    public boolean isDummyUrlCompare() {
        return comparerBean.getUrlStatus().equals(sourceBean.getUrlStatus());
    }


    public boolean isSameRetrofitBuilderInstance() {
        return okHttpClientInstanceCompare() &&
                callBackExecutorCompare() &&
                callFactoryCompare() &&
                validateEagerlyCompare() &&
                callAdapterFactoryCompare() &&
                converterFactoryCompare() && interceptorsCompare() && hostUrlCompare() && isDummyUrlCompare();
    }

}
