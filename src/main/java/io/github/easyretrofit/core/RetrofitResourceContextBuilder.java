package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBeanGenerator;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitClientBeanGenerator;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceApiInterfaceClassBean;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceClassBean;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceClientClassBean;

import java.util.*;
import java.util.stream.Collectors;

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


    public RetrofitResourceContextBuilder(EnvManager envManager) {
        retrofitClientBeanList = new ArrayList<>();
        retrofitApiInterfaceBeanList = new ArrayList<>();
        retrofitServiceBeanHashMap = new HashMap<>();
        interceptorExtensionsClasses = new ArrayList<>();
    }

    public RetrofitResourceContext buildContextInstance(String[] basePackages,
                                                        Set<Class<?>> retrofitBuilderClassSet,
                                                        RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                                        List<RetrofitInterceptorExtension> interceptorExtensions,
                                                        EnvManager envManager) {
        RetrofitResourceClassBean resourceClassBean = preRetrofitResourceClassHandle(retrofitBuilderClassSet);
        // step1 generate list of api interface bean
        setRetrofitApiInterfaceBeanList(resourceClassBean, globalRetrofitBuilderExtension, interceptorExtensions, envManager);
        // step2 generate list of client bean
        setRetrofitClientBeanList(resourceClassBean);
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

    private void setRetrofitApiInterfaceBeanList(RetrofitResourceClassBean resourceClassBean, RetrofitBuilderExtension globalRetrofitBuilderExtension, List<RetrofitInterceptorExtension> interceptorExtensions, EnvManager envManager) {
        for (RetrofitResourceApiInterfaceClassBean apiInterfaceClassBean : resourceClassBean.getApiInterfaceClassBeans()) {
            RetrofitApiInterfaceBean apiInterfaceBean = new RetrofitApiInterfaceBeanGenerator(apiInterfaceClassBean, envManager, globalRetrofitBuilderExtension, interceptorExtensions).generate();
            if (apiInterfaceBean != null) {
                retrofitApiInterfaceBeanList.add(apiInterfaceBean);
            }
        }
    }

    private RetrofitResourceClassBean preRetrofitResourceClassHandle(Set<Class<?>> retrofitBuilderClassSet) {
        return new RetrofitResourceClassBean(retrofitBuilderClassSet);
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

    private void setRetrofitApiInterfaceBeanHashMap() {
        for (RetrofitClientBean retrofitClient : getRetrofitClientBeanList()) {
            for (RetrofitApiInterfaceBean retrofitService : retrofitClient.getRetrofitApiInterfaceBeans()) {
                retrofitServiceBeanHashMap.put(retrofitService.getSelfClazz().getName(), retrofitService);
            }
        }
    }

    private void setRetrofitClientBeanList(RetrofitResourceClassBean resourceClassBean) {

        for (RetrofitResourceClientClassBean clientClassBean : resourceClassBean.getClientClassBeans()) {
            List<RetrofitApiInterfaceBean> belongsApiInterfaceBeans = new ArrayList<>();
            // find belongs api interface beans
            for (RetrofitApiInterfaceBean bean : retrofitApiInterfaceBeanList) {
                for (RetrofitResourceApiInterfaceClassBean classBean : clientClassBean.getApiInterfaceClassBeans()) {
                    if (classBean.getMyself().equals(bean.getSelfClazz())){
                        belongsApiInterfaceBeans.add(bean);
                    }
                }
            }
            RetrofitClientBeanGenerator clientBeanGenerator = new RetrofitClientBeanGenerator(belongsApiInterfaceBeans, clientClassBean.getClientClazz());
            retrofitClientBeanList.add(clientBeanGenerator.generate());
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
