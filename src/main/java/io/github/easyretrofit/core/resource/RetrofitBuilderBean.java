package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.OverrideRule;
import io.github.easyretrofit.core.RetrofitBuilderExtension;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.builder.*;
import io.github.easyretrofit.core.util.ArrayUtils;
import io.github.easyretrofit.core.util.BooleanUtil;
import io.github.easyretrofit.core.util.UniqueKeyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.easyretrofit.core.util.ArrayUtils.hasSameElement;


public final class RetrofitBuilderBean implements UniqueKey {
    private boolean enable = false;

    private OverrideRule overwriteType = OverrideRule.GLOBAL_FIRST;

    private String baseUrl = "";

    private Class<? extends BaseCallAdapterFactoryBuilder>[] addCallAdapterFactory;

    private Class<? extends BaseConverterFactoryBuilder>[] addConverterFactory;

    private Class<? extends BaseOkHttpClientBuilder> client;

    private Class<? extends BaseCallBackExecutorBuilder> callbackExecutor;

    private Class<? extends BaseCallFactoryBuilder> callFactory;

    private boolean validateEagerly;


    public RetrofitBuilderBean(Class<?> retrofitBuilderClazz, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        RetrofitBuilder retrofitBuilderAnnotation = retrofitBuilderClazz.getDeclaredAnnotation(RetrofitBuilder.class);
        if (retrofitBuilderAnnotation.globalOverwriteRule().equals(OverrideRule.LOCAL_ONLY)) {
            setRetrofitBuilderBeanByLocalOnly(retrofitBuilderAnnotation);
        } else if (retrofitBuilderAnnotation.globalOverwriteRule().equals(OverrideRule.LOCAL_FIRST) && globalRetrofitBuilderExtension.enable()) {
            setRetrofitBuilderBeanByLocalFirst(retrofitBuilderAnnotation, globalRetrofitBuilderExtension);
        } else if (retrofitBuilderAnnotation.globalOverwriteRule().equals(OverrideRule.MERGE) && globalRetrofitBuilderExtension.enable()) {
            setRetrofitBuilderBeanByMerge(retrofitBuilderAnnotation, globalRetrofitBuilderExtension);
        } else if (retrofitBuilderAnnotation.globalOverwriteRule().equals(OverrideRule.GLOBAL_FIRST) && globalRetrofitBuilderExtension.enable()) {
            setRetrofitBuilderBeanByGlobalFirst(retrofitBuilderAnnotation, globalRetrofitBuilderExtension);
        } else if (retrofitBuilderAnnotation.globalOverwriteRule().equals(OverrideRule.GLOBAL_ONLY) && globalRetrofitBuilderExtension.enable()) {
            setRetrofitBuilderBeanByGlobalOnly(globalRetrofitBuilderExtension);
        } else {
            setRetrofitBuilderBeanByLocalOnly(retrofitBuilderAnnotation);
        }
    }

    private void setRetrofitBuilderBeanByLocalFirst(RetrofitBuilder retrofitBuilderAnnotation, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        this.setEnable(true);
        this.setBaseUrl(StringUtils.isNotBlank(retrofitBuilderAnnotation.baseUrl()) ? retrofitBuilderAnnotation.baseUrl() : globalRetrofitBuilderExtension.globalBaseUrl());
        this.setClient(getClientClazz(!retrofitBuilderAnnotation.client().getName().equals(BaseOkHttpClientBuilder.class.getName()) ? retrofitBuilderAnnotation.client() : globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz() == null ? retrofitBuilderAnnotation.client() : globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz()));
        this.setCallbackExecutor(getCallBackExecutorClazz(!retrofitBuilderAnnotation.callbackExecutor().getName().equals(BaseCallBackExecutorBuilder.class.getName()) ? retrofitBuilderAnnotation.callbackExecutor() : globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz() == null ? retrofitBuilderAnnotation.callbackExecutor() : globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz()));
        this.setAddCallAdapterFactory(retrofitBuilderAnnotation.addCallAdapterFactory().length != 0 ? retrofitBuilderAnnotation.addCallAdapterFactory() : globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz());
        this.setAddConverterFactory(retrofitBuilderAnnotation.addConverterFactory().length != 0 ? retrofitBuilderAnnotation.addConverterFactory() : globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz());
        this.setValidateEagerly(retrofitBuilderAnnotation.validateEagerly());
        this.setCallFactory(getCallFactoryClazz(!retrofitBuilderAnnotation.callFactory().getName().equals(BaseCallFactoryBuilder.class.getName()) ? retrofitBuilderAnnotation.callFactory() : globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz() == null ? retrofitBuilderAnnotation.callFactory() : globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz()));
    }

    /**
     * if merge, default date is local_first, then merge global CallAdapterFactory and ConverterFactory to local_first
     *
     */
    private void setRetrofitBuilderBeanByMerge(RetrofitBuilder retrofitBuilderAnnotation, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        this.setEnable(true);
        this.setBaseUrl(StringUtils.isNotBlank(retrofitBuilderAnnotation.baseUrl()) ? retrofitBuilderAnnotation.baseUrl() : globalRetrofitBuilderExtension.globalBaseUrl());
        this.setClient(getClientClazz(!retrofitBuilderAnnotation.client().getName().equals(BaseOkHttpClientBuilder.class.getName()) ? retrofitBuilderAnnotation.client() : globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz() == null ? retrofitBuilderAnnotation.client() : globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz()));
        this.setCallbackExecutor(getCallBackExecutorClazz(!retrofitBuilderAnnotation.callbackExecutor().getName().equals(BaseCallBackExecutorBuilder.class.getName()) ? retrofitBuilderAnnotation.callbackExecutor() : globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz() == null ? retrofitBuilderAnnotation.callbackExecutor() : globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz()));
        this.setValidateEagerly(retrofitBuilderAnnotation.validateEagerly());
        this.setCallFactory(getCallFactoryClazz(!retrofitBuilderAnnotation.callFactory().getName().equals(BaseCallFactoryBuilder.class.getName()) ? retrofitBuilderAnnotation.callFactory() : globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz() == null ? retrofitBuilderAnnotation.callFactory() : globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz()));
        this.setAddCallAdapterFactory(getCallAdapterFactories(retrofitBuilderAnnotation, globalRetrofitBuilderExtension));
        this.setAddConverterFactory(getConverterFactories(retrofitBuilderAnnotation, globalRetrofitBuilderExtension));
    }

    private Class<? extends BaseCallAdapterFactoryBuilder>[] getCallAdapterFactories(RetrofitBuilder retrofitBuilderAnnotation, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        Set<Class<? extends BaseCallAdapterFactoryBuilder>> addCallAdapterFactoryBuilderList = new HashSet<>();
        if (globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz() != null) {
            addCallAdapterFactoryBuilderList.addAll(Arrays.asList(globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz()));
        }
        if (retrofitBuilderAnnotation.addCallAdapterFactory().length != 0) {
            addCallAdapterFactoryBuilderList.addAll(Arrays.asList(retrofitBuilderAnnotation.addCallAdapterFactory()));
        }
        return addCallAdapterFactoryBuilderList.toArray(new Class[0]);
    }

    private Class<? extends BaseConverterFactoryBuilder>[] getConverterFactories(RetrofitBuilder retrofitBuilderAnnotation, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        Set<Class<? extends BaseConverterFactoryBuilder>> addConverterFactoryBuilderList = new HashSet<>();
        if (globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz() != null) {
            addConverterFactoryBuilderList.addAll(Arrays.asList(globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz()));
        }
        if (retrofitBuilderAnnotation.addConverterFactory().length != 0) {
            addConverterFactoryBuilderList.addAll(Arrays.asList(retrofitBuilderAnnotation.addConverterFactory()));
        }
        return addConverterFactoryBuilderList.toArray(new Class[0]);
    }

    private void setRetrofitBuilderBeanByGlobalFirst(RetrofitBuilder retrofitBuilderAnnotation, RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        this.setEnable(true);
        this.setBaseUrl(StringUtils.isNotBlank(globalRetrofitBuilderExtension.globalBaseUrl()) ? globalRetrofitBuilderExtension.globalBaseUrl() : retrofitBuilderAnnotation.baseUrl());
        this.setClient(getClientClazz(globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz() != null ? globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz() : retrofitBuilderAnnotation.client()));
        this.setCallbackExecutor(getCallBackExecutorClazz(globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz() != null ? globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz() : retrofitBuilderAnnotation.callbackExecutor()));
        this.setAddCallAdapterFactory(globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz() != null ? globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz() : retrofitBuilderAnnotation.addCallAdapterFactory());
        this.setAddConverterFactory(globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz() != null ? globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz() : retrofitBuilderAnnotation.addConverterFactory());
        this.setValidateEagerly(globalRetrofitBuilderExtension.globalValidateEagerly() != null ? BooleanUtil.transformToBoolean(globalRetrofitBuilderExtension.globalValidateEagerly()) : retrofitBuilderAnnotation.validateEagerly());
        this.setCallFactory(getCallFactoryClazz(globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz() != null ? globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz() : retrofitBuilderAnnotation.callFactory()));
    }

    private void setRetrofitBuilderBeanByLocalOnly(RetrofitBuilder retrofitBuilderAnnotation) {
        this.setEnable(false);
        this.setBaseUrl(retrofitBuilderAnnotation.baseUrl());
        this.setClient(retrofitBuilderAnnotation.client());
        this.setCallbackExecutor(retrofitBuilderAnnotation.callbackExecutor());
        this.setAddCallAdapterFactory(retrofitBuilderAnnotation.addCallAdapterFactory());
        this.setAddConverterFactory(retrofitBuilderAnnotation.addConverterFactory());
        this.setValidateEagerly(retrofitBuilderAnnotation.validateEagerly());
        this.setCallFactory(retrofitBuilderAnnotation.callFactory());
    }

    private void setRetrofitBuilderBeanByGlobalOnly(RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        this.setEnable(globalRetrofitBuilderExtension.enable());
        this.setBaseUrl(globalRetrofitBuilderExtension.globalBaseUrl());
        this.setClient(getClientClazz(globalRetrofitBuilderExtension.globalOkHttpClientBuilderClazz()));
        this.setCallbackExecutor(getCallBackExecutorClazz(globalRetrofitBuilderExtension.globalCallBackExecutorBuilderClazz()));
        this.setAddCallAdapterFactory(globalRetrofitBuilderExtension.globalCallAdapterFactoryBuilderClazz());
        this.setAddConverterFactory(globalRetrofitBuilderExtension.globalConverterFactoryBuilderClazz());
        this.setValidateEagerly(BooleanUtil.transformToBoolean(globalRetrofitBuilderExtension.globalValidateEagerly()));
        this.setCallFactory(getCallFactoryClazz(globalRetrofitBuilderExtension.globalCallFactoryBuilderClazz()));
    }

    private Class<? extends BaseOkHttpClientBuilder> getClientClazz(Class<? extends BaseOkHttpClientBuilder> clazz) {
        if (clazz == null) {
            return BaseOkHttpClientBuilder.class;
        }
        return clazz;
    }

    private Class<? extends BaseCallBackExecutorBuilder> getCallBackExecutorClazz(Class<? extends BaseCallBackExecutorBuilder> clazz) {
        if (clazz == null) {
            return BaseCallBackExecutorBuilder.class;
        }
        return clazz;
    }

    private Class<? extends BaseCallFactoryBuilder> getCallFactoryClazz(Class<? extends BaseCallFactoryBuilder> clazz) {
        if (clazz == null) {
            return BaseCallFactoryBuilder.class;
        }
        return clazz;
    }

    public boolean isEnable() {
        return enable;
    }

    void setEnable(boolean enable) {
        this.enable = enable;
    }

    public OverrideRule getOverwriteType() {
        return overwriteType;
    }

    void setOverwriteType(OverrideRule overwriteType) {
        this.overwriteType = overwriteType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Class<? extends BaseCallAdapterFactoryBuilder>[] getAddCallAdapterFactory() {
        return addCallAdapterFactory;
    }

    void setAddCallAdapterFactory(Class<? extends BaseCallAdapterFactoryBuilder>[] addCallAdapterFactory) {
        this.addCallAdapterFactory = addCallAdapterFactory;
    }

    public Class<? extends BaseConverterFactoryBuilder>[] getAddConverterFactory() {
        return addConverterFactory;
    }

    void setAddConverterFactory(Class<? extends BaseConverterFactoryBuilder>[] addConverterFactory) {
        this.addConverterFactory = addConverterFactory;
    }

    public Class<? extends BaseOkHttpClientBuilder> getClient() {
        return client;
    }

    void setClient(Class<? extends BaseOkHttpClientBuilder> client) {
        this.client = client;
    }

    public Class<? extends BaseCallBackExecutorBuilder> getCallbackExecutor() {
        return callbackExecutor;
    }

    void setCallbackExecutor(Class<? extends BaseCallBackExecutorBuilder> callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }

    public Class<? extends BaseCallFactoryBuilder> getCallFactory() {
        return callFactory;
    }

    void setCallFactory(Class<? extends BaseCallFactoryBuilder> callFactory) {
        this.callFactory = callFactory;
    }

    public boolean isValidateEagerly() {
        return validateEagerly;
    }

    void setValidateEagerly(boolean validateEagerly) {
        this.validateEagerly = validateEagerly;
    }

    @Override
    public String toString() {
        String addCallAdapterFactoryStr = null;
        if (addCallAdapterFactory != null) {
            addCallAdapterFactoryStr = Arrays.stream(addCallAdapterFactory).map(Class::getName).sorted().collect(Collectors.joining(","));
        }
        String addConverterFactoryStr = null;
        if (addConverterFactory != null) {
            addConverterFactoryStr = Arrays.stream(addConverterFactory).map(Class::getName).sorted().collect(Collectors.joining(","));
        }
        return "RetrofitBuilderBean{" +
                "enable=" + enable +
                ", overwriteType=" + overwriteType +
                ", baseUrl='" + baseUrl + '\'' +
                ", addCallAdapterFactory=" + addCallAdapterFactoryStr +
                ", addConverterFactory=" + addConverterFactoryStr +
                ", client=" + client +
                ", callbackExecutor=" + callbackExecutor +
                ", callFactory=" + callFactory +
                ", validateEagerly=" + validateEagerly +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetrofitBuilderBean)) return false;

        RetrofitBuilderBean that = (RetrofitBuilderBean) o;
        return enable == that.enable
                && validateEagerly == that.validateEagerly
                && overwriteType == that.overwriteType
                && baseUrl.equals(that.baseUrl)
                && hasSameElement(addCallAdapterFactory, that.addCallAdapterFactory)
                && hasSameElement(addConverterFactory, that.addConverterFactory)
                && client.equals(that.client)
                && callbackExecutor.equals(that.callbackExecutor)
                && callFactory.equals(that.callFactory);
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(enable);
        result = 31 * result + overwriteType.hashCode();
        result = 31 * result + baseUrl.hashCode();
        result = 31 * result + ArrayUtils.toSet(addCallAdapterFactory).hashCode();
        result = 31 * result + ArrayUtils.toSet(addConverterFactory).hashCode();
        result = 31 * result + client.hashCode();
        result = 31 * result + callbackExecutor.hashCode();
        result = 31 * result + callFactory.hashCode();
        result = 31 * result + Boolean.hashCode(validateEagerly);
        return result;
    }
}
