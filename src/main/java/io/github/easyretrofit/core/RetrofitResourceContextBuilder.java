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
    private final List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList;
    private final Map<String, RetrofitApiInterfaceBean> retrofitServiceBeanHashMap;
    private Class<?> retrofitBuilderExtensionClazz;
    private final List<Class<?>> interceptorExtensionsClasses;
    private final EnvManager envManager;

    public RetrofitResourceContextBuilder(EnvManager envManager) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitApiInterfaceBeanList = new ArrayList<>();
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

    public Map<String, RetrofitApiInterfaceBean> getRetrofitServiceBeanHashMap() {
        return retrofitServiceBeanHashMap;
    }

    public List<RetrofitApiInterfaceBean> getRetrofitServiceBean() {
        return retrofitApiInterfaceBeanList;
    }

    private void setRetrofitServiceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitApiInterfaceBean retrofitService : retrofitClient.getRetrofitApiServiceBeans()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
    }

    private void setRetrofitServiceBeanList(Set<Class<?>> retrofitBuilderClassSet,
                                            RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                            List<RetrofitInterceptorExtension> interceptorExtensions) {
        RetrofitApiInterfaceBeanGenerator serviceBeanHandler;
        for (Class<?> clazz : retrofitBuilderClassSet) {
            serviceBeanHandler = new RetrofitApiInterfaceBeanGenerator(clazz, envManager, globalRetrofitBuilderExtension, interceptorExtensions);
            final RetrofitApiInterfaceBean serviceBean = serviceBeanHandler.generate();
            if (serviceBean != null) {
                retrofitApiInterfaceBeanList.add(serviceBean);
            }
        }
    }

    private void setRetrofitClientBeanList() {
        RetrofitClientBeanGenerator clientBeanHandler;
        for (RetrofitApiInterfaceBean serviceBean : getRetrofitServiceBean()) {
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
