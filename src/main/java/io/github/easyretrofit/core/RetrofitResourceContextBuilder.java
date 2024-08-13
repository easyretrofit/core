package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.*;

import java.util.*;

/**
 * the builder of Retrofit resource context, used to assemble all the retrofit resource
 *
 * @author liuziyuan
 */
public class RetrofitResourceContextBuilder {
    private final List<RetrofitClientBean> retrofitClientBeanList;
    private final List<RetrofitApiServiceBean> retrofitApiServiceBeanList;
    private final Map<String, RetrofitApiServiceBean> retrofitServiceBeanHashMap;
    private Class<?> retrofitBuilderExtensionClazz;
    private final List<Class<?>> interceptorExtensionsClasses;
    private final EnvManager envManager;

    public RetrofitResourceContextBuilder(EnvManager envManager) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitApiServiceBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        interceptorExtensionsClasses = new ArrayList<>();
        this.envManager = envManager;
    }

    public RetrofitResourceContext buildContextInstance(String[] basePackages,
                                                        Set<Class<?>> retrofitBuilderClassSet,
                                                        RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                                        List<RetrofitInterceptorExtension> interceptorExtensions,
                                                        EnvManager envManager) {
        setRetrofitServiceBeanList(retrofitBuilderClassSet, globalRetrofitBuilderExtension, interceptorExtensions);
        setRetrofitClientBeanList();
        setRetrofitServiceBeanHashMap();

        setRetrofitBuilderExtensionClazz(globalRetrofitBuilderExtension);
        setInterceptorExtensionsClasses(interceptorExtensions);
        return new RetrofitResourceContext(basePackages,
                retrofitClientBeanList, retrofitServiceBeanHashMap, retrofitBuilderExtensionClazz, interceptorExtensionsClasses, envManager);
    }


    public List<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
    }

    public Map<String, RetrofitApiServiceBean> getRetrofitServiceBeanHashMap() {
        return retrofitServiceBeanHashMap;
    }

    public List<RetrofitApiServiceBean> getRetrofitServiceBean() {
        return retrofitApiServiceBeanList;
    }

    private void setRetrofitServiceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitApiServiceBean retrofitService : retrofitClient.getRetrofitApiServiceBeans()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
    }

    private void setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet,
                                            RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                            List<RetrofitInterceptorExtension> interceptorExtensions) {
        RetrofitApiServiceBeanGenerator serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitApiServiceBeanGenerator(clazz, envManager, globalRetrofitBuilderExtension, interceptorExtensions);
            final RetrofitApiServiceBean serviceBean = serviceBeanHandler.generate();
            if (serviceBean != null) {
                retrofitApiServiceBeanList.add(serviceBean);
            }
        }
    }

    private void setRetrofitClientBeanList() {
        RetrofitClientBeanGenerator clientBeanHandler;
        for (RetrofitApiServiceBean serviceBean : getRetrofitServiceBean()) {
            clientBeanHandler = new RetrofitClientBeanGenerator(retrofitClientBeanList, serviceBean);
            final RetrofitClientBean retrofitClientBean = clientBeanHandler.generate();
            if (retrofitClientBean != null && retrofitClientBeanList.stream().noneMatch(clientBean -> clientBean.getRetrofitInstanceName().equals(retrofitClientBean.getRetrofitInstanceName()))) {
                retrofitClientBeanList.add(retrofitClientBean);
            }
        }
    }


    private void setInterceptorExtensionsClasses(List<RetrofitInterceptorExtension> interceptorExtensions) {
        if (interceptorExtensions != null) {
            for (RetrofitInterceptorExtension interceptorExtension : interceptorExtensions) {
                this.interceptorExtensionsClasses.add(interceptorExtension.getClass());
            }
        }
    }

    private void setRetrofitBuilderExtensionClazz(RetrofitBuilderExtension globalRetrofitBuilderExtension) {
        if (globalRetrofitBuilderExtension != null) {
            this.retrofitBuilderExtensionClazz = globalRetrofitBuilderExtension.getClass();
        }
    }

}
