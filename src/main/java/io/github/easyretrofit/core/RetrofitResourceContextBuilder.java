package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.*;
import io.github.easyretrofit.core.resource.handler.PreRetrofitSourceClassBeanCollectionHandler;
import io.github.easyretrofit.core.resource.PreRetrofitResourceApiInterfaceClassBean;
import io.github.easyretrofit.core.resource.handler.RetrofitApiInterfaceBeanCollectionHandler;
import io.github.easyretrofit.core.resource.handler.RetrofitClientBeanCollectionHandler;
//import io.github.easyretrofit.core.resource.pre.RetrofitResourceClientClassBean;

import java.util.*;

/**
 * the builder of Retrofit resource context, used to assemble all the retrofit resource
 *
 * @author liuziyuan
 */
public class RetrofitResourceContextBuilder {
    private Set<PreRetrofitResourceApiInterfaceClassBean> apiInterfaceClassBeans;
    private Set<RetrofitClientBean> retrofitClientBeanList;
    private List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList;
    private final Map<String, RetrofitApiInterfaceBean> retrofitServiceBeanHashMap;
    private Class<?> retrofitBuilderExtensionClazz;
    private final List<Class<?>> interceptorExtensionsClasses;


    public RetrofitResourceContextBuilder() {
        apiInterfaceClassBeans = new HashSet<>();
        retrofitClientBeanList = new HashSet<>();
        retrofitApiInterfaceBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        interceptorExtensionsClasses = new ArrayList<>();
    }

    public RetrofitResourceContext buildContextInstance(String[] basePackages,
                                                        Set<Class<?>> retrofitBuilderClassSet,
                                                        RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                                        List<RetrofitInterceptorExtension> interceptorExtensions,
                                                        EnvManager envManager) {
        // pre-step, fill apiInterfaceClassBeans list;
        apiInterfaceClassBeans = new PreRetrofitSourceClassBeanCollectionHandler(retrofitBuilderClassSet).getInterfaceClassBeans();
        // step1 generate list of api interface bean
        retrofitApiInterfaceBeanList = new RetrofitApiInterfaceBeanCollectionHandler(apiInterfaceClassBeans, globalRetrofitBuilderExtension, interceptorExtensions, envManager).getRetrofitApiInterfaceBeans();
        // step2 generate list of client bean
        retrofitClientBeanList = new RetrofitClientBeanCollectionHandler(retrofitApiInterfaceBeanList).getRetrofitClients();
        // step3 set map of api interface bean
        setRetrofitApiInterfaceBeanHashMap();
        // step4 set retrofit builder extension clazz
        setRetrofitBuilderExtensionClazz(globalRetrofitBuilderExtension);
        // step5 set interceptor extensions classes
        setInterceptorExtensionsClasses(interceptorExtensions);
        // step6 return context
        return new RetrofitResourceContext(basePackages,
                retrofitClientBeanList, retrofitServiceBeanHashMap, retrofitBuilderExtensionClazz, interceptorExtensionsClasses, envManager);
    }


    public Set<RetrofitClientBean> getRetrofitClientBeanList() {
        return retrofitClientBeanList;
    }

    public Map<String, RetrofitApiInterfaceBean> getRetrofitServiceBeanHashMap() {
        return retrofitServiceBeanHashMap;
    }

    private void setRetrofitApiInterfaceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitApiInterfaceBean retrofitService : retrofitClient.getRetrofitApiInterfaceBeans()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
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
