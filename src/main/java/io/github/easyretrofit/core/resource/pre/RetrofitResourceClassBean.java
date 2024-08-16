package io.github.easyretrofit.core.resource.pre;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RetrofitResourceClassBean {
    private final Set<RetrofitResourceApiInterfaceClassBean> apiInterfaceClassBeans;
    private final Set<RetrofitResourceClientClassBean> clientClassBeans;

    public RetrofitResourceClassBean(Set<Class<?>> resourceClassSet) {
        clientClassBeans = new HashSet<>();
        apiInterfaceClassBeans = new HashSet<>();
        createAndInitApiInterfaceClassBeans(resourceClassSet);
        fillChildrenClasses2ApiInterfaceClassBeans();
        generateClientClassBeans();
    }

    public Set<RetrofitResourceApiInterfaceClassBean> getApiInterfaceClassBeans() {
        return apiInterfaceClassBeans;
    }

    public Set<RetrofitResourceClientClassBean> getClientClassBeans() {
        return clientClassBeans;
    }

    private void generateClientClassBeans() {
        Set<Class<?>> clientClasses = apiInterfaceClassBeans.stream().map(RetrofitResourceApiInterfaceClassBean::getAncestor).collect(Collectors.toSet());
        for (Class<?> clientClass : clientClasses) {
            Set<RetrofitResourceApiInterfaceClassBean> apiBeans = apiInterfaceClassBeans.stream().filter(apiBean -> apiBean.getAncestor().equals(clientClass)).collect(Collectors.toSet());
            clientClassBeans.add(new RetrofitResourceClientClassBean(apiBeans, clientClass));
        }
    }


    private void createAndInitApiInterfaceClassBeans(Set<Class<?>> resourceClassSet) {
        RetrofitResourceApiInterfaceClassBean apiInterfaceClassBean;
        for (Class<?> clazz : resourceClassSet) {
            apiInterfaceClassBean = new RetrofitResourceApiInterfaceClassBean(clazz);
            apiInterfaceClassBeans.add(apiInterfaceClassBean);
        }
    }

    private void fillChildrenClasses2ApiInterfaceClassBeans() {
        for (RetrofitResourceApiInterfaceClassBean apiInterfaceClassBean : apiInterfaceClassBeans) {
            final Class<?> selfClazz = apiInterfaceClassBean.getMyself();
            for (RetrofitResourceApiInterfaceClassBean bean : apiInterfaceClassBeans) {
                //如果他的父系中有当前的对象,则将当前的类加入到当前对象childrenClasses中
                if (bean.getSelf2Ancestors().contains(selfClazz)) {
                    apiInterfaceClassBean.getChildren().add(bean.getMyself());
                }
            }
        }
    }


}
