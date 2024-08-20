package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.resource.PreRetrofitResourceApiInterfaceClassBean;

import java.util.HashSet;
import java.util.Set;

public class PreRetrofitSourceClassBeanCollectionHandler {


    private final Set<PreRetrofitResourceApiInterfaceClassBean> interfaceClassBeans;

    public PreRetrofitSourceClassBeanCollectionHandler(Set<Class<?>> resourceClassSet) {
        interfaceClassBeans = new HashSet<>();
        createAndInitApiInterfaceClassBeans(resourceClassSet);
        fillChildrenClasses2ApiInterfaceClassBeans();
    }

    public Set<PreRetrofitResourceApiInterfaceClassBean> getInterfaceClassBeans() {
        return interfaceClassBeans;
    }

    private void createAndInitApiInterfaceClassBeans(Set<Class<?>> resourceClassSet) {
        PreRetrofitResourceApiInterfaceClassBean apiInterfaceClassBean;
        for (Class<?> clazz : resourceClassSet) {
            apiInterfaceClassBean = new PreRetrofitResourceApiInterfaceClassBean(clazz);
            interfaceClassBeans.add(apiInterfaceClassBean);
        }
    }

    private void fillChildrenClasses2ApiInterfaceClassBeans() {
        for (PreRetrofitResourceApiInterfaceClassBean apiInterfaceClassBean : interfaceClassBeans) {
            final Class<?> selfClazz = apiInterfaceClassBean.getMyself();
            for (PreRetrofitResourceApiInterfaceClassBean bean : interfaceClassBeans) {
                //如果他的父系中有当前的对象,则将当前的类加入到当前对象childrenClasses中
                if (bean.getSelf2Ancestors().contains(selfClazz)) {
                    apiInterfaceClassBean.getChildren().add(bean.getMyself());
                }
            }
        }
    }
}
