package io.github.easyretrofit.core;



import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;

import java.util.List;
import java.util.Map;

/**
 * The top structure contains all the objects created in the starter
 *
 * @author liuziyuan
 */
public class RetrofitResourceContext {

    private String[] basePackages;
    private Class<?> retrofitBuilderExtensionClazz;
    private List<Class<?>> interceptorExtensionsClasses;
    private List<RetrofitClientBean> retrofitClients;
    private Map<String, RetrofitApiInterfaceBean> retrofitApiServices;
    private EnvManager envManager;

    public RetrofitResourceContext() {

    }

    public RetrofitResourceContext(String[] basePackages,
                                   List<RetrofitClientBean> retrofitClients,
                                   Map<String, RetrofitApiInterfaceBean> retrofitApiServices,
                                   Class<?> retrofitBuilderExtensionClazz,
                                   List<Class<?>> interceptorExtensionsClasses,
                                   EnvManager envManager) {
        this.retrofitApiServices = retrofitApiServices;
        this.retrofitClients = retrofitClients;
        this.basePackages = basePackages;
        this.retrofitBuilderExtensionClazz = retrofitBuilderExtensionClazz;
        this.interceptorExtensionsClasses = interceptorExtensionsClasses;
        this.envManager = envManager;
    }

    public List<RetrofitClientBean> getRetrofitClients() {
        return retrofitClients;
    }

    public RetrofitApiInterfaceBean getRetrofitApiServiceBean(String clazzFullName) {
        return retrofitApiServices.get(clazzFullName);
    }

    public RetrofitApiInterfaceBean getRetrofitApiServiceBean(Class<?> clazz) {
        return retrofitApiServices.get(clazz.getName());
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
