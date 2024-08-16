package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBeanGenerator;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitClientBeanGenerator;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceApiInterfaceClassBean;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceClassBean;
import io.github.easyretrofit.core.resource.pre.RetrofitResourceClientClassBean;

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
        RetrofitResourceClassBean resourceClassBean = preRetrofitResourceClassHandle(retrofitBuilderClassSet);
        retrofitResourceHandle(resourceClassBean, globalRetrofitBuilderExtension, interceptorExtensions, envManager);
//        setRetrofitApiInterfaceBeanList(retrofitBuilderClassSet, globalRetrofitBuilderExtension, interceptorExtensions);
//        setRetrofitApiInterfaceBeanChildrenClass();
        setRetrofitClientBeanList();
        setRetrofitApiInterfaceBeanHashMap();

        setRetrofitBuilderExtensionClazz(globalRetrofitBuilderExtension);
        setInterceptorExtensionsClasses(interceptorExtensions);
        return new RetrofitResourceContext(basePackages,
                retrofitClientBeanList, retrofitServiceBeanHashMap, retrofitBuilderExtensionClazz, interceptorExtensionsClasses, envManager);
    }

    private void retrofitResourceHandle(RetrofitResourceClassBean resourceClassBean, RetrofitBuilderExtension globalRetrofitBuilderExtension, List<RetrofitInterceptorExtension> interceptorExtensions, EnvManager envManager) {
        fillRetrofitApiInterfaceBean(resourceClassBean, globalRetrofitBuilderExtension, interceptorExtensions, envManager);
    }

    private void fillRetrofitApiInterfaceBean(RetrofitResourceClassBean resourceClassBean, RetrofitBuilderExtension globalRetrofitBuilderExtension, List<RetrofitInterceptorExtension> interceptorExtensions, EnvManager envManager) {
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

//    private void setRetrofitApiInterfaceBeanList(Set<Class<?>> retrofitBuilderClassSet,
//                                                 RetrofitBuilderExtension globalRetrofitBuilderExtension,
//                                                 List<RetrofitInterceptorExtension> interceptorExtensions) {
//        RetrofitApiInterfaceBeanGenerator serviceBeanHandler;
//        for (Class<?> clazz : retrofitBuilderClassSet) {
//            serviceBeanHandler = new RetrofitApiInterfaceBeanGenerator(clazz, envManager, globalRetrofitBuilderExtension, interceptorExtensions);
//            final RetrofitApiInterfaceBean serviceBean = serviceBeanHandler.generate();
//            if (serviceBean != null) {
//                retrofitApiInterfaceBeanList.add(serviceBean);
//            }
//        }
//    }

//    private void setRetrofitApiInterfaceBeanChildrenClass() {
//        for (RetrofitApiInterfaceBean serviceBean : retrofitApiInterfaceBeanList) {
//            final Class<?> selfClazz = serviceBean.getSelfClazz();
//            final Class<?> parentClazz = serviceBean.getParentClazz();
//            if (parentClazz != null) {
//                for (RetrofitApiInterfaceBean apiInterfaceBean : retrofitApiInterfaceBeanList) {
//                    //如果他的父系中有当前的对象,则将当前的类加入到当前对象childrenClasses中
//                    if (apiInterfaceBean.getSelf2ParentClasses().contains(selfClazz)) {
//                        serviceBean.getChildrenClasses().add(apiInterfaceBean.getSelfClazz());
//                    }
//                }
//            }
//        }
//    }


    private void setRetrofitClientBeanList() {
        Set<Class<?>> clientClasses = new HashSet<>();
        //获取所有父类
        for (RetrofitApiInterfaceBean apiInterfaceBean : retrofitApiInterfaceBeanList) {
            if (apiInterfaceBean.getParentClazz() != null) {
                clientClasses.add(apiInterfaceBean.getParentClazz());
            }

        }
        //完成ClientBean创建
        for (Class<?> clazz : clientClasses) {
            RetrofitClientBeanGenerator clientBeanGenerator = new RetrofitClientBeanGenerator(retrofitApiInterfaceBeanList, clazz);
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
