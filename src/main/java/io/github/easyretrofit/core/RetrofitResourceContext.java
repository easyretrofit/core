package io.github.easyretrofit.core;



import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The top structure contains all the objects created in the starter
 *
 * @author liuziyuan
 */
public class RetrofitResourceContext {

    private String[] basePackages;
    private Class<?> retrofitBuilderExtensionClazz;
    private List<Class<?>> interceptorExtensionsClasses;
    private Set<RetrofitClientBean> retrofitClients;
    private Map<String, RetrofitApiInterfaceBean> retrofitApiInterfaces;
    private EnvManager envManager;

    public RetrofitResourceContext() {

    }

    public RetrofitResourceContext(String[] basePackages,
                                   Set<RetrofitClientBean> retrofitClients,
                                   Map<String, RetrofitApiInterfaceBean> retrofitApiInterfaces,
                                   Class<?> retrofitBuilderExtensionClazz,
                                   List<Class<?>> interceptorExtensionsClasses,
                                   EnvManager envManager) {
        this.retrofitApiInterfaces = retrofitApiInterfaces;
        this.retrofitClients = retrofitClients;
        this.basePackages = basePackages;
        this.retrofitBuilderExtensionClazz = retrofitBuilderExtensionClazz;
        this.interceptorExtensionsClasses = interceptorExtensionsClasses;
        this.envManager = envManager;
    }

    public Set<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitApiInterfaceBean getRetrofitApiInterfaceBean(String clazzFullName) {
        return retrofitApiInterfaces.get(clazzFullName);
    }

    public RetrofitApiInterfaceBean getRetrofitApiInterfaceBean(Class<?> clazz) {
        return retrofitApiInterfaces.get(clazz.getName());
    }

    public String[] getBasePackages() {
        return basePackages;
    }


    public List<Class<?>> getInterceptorExtensionsClasses() {
        return interceptorExtensionsClasses;
    }

    public Class<?> getRetrofitBuilderExtensionClazz() {
        return retrofitBuilderExtensionClazz;
    }

    public EnvManager getEnvManger() {
        return envManager;
    }
}
