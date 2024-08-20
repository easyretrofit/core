package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitClientBeanGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RetrofitClientCollectionHandler {
    private final Set<RetrofitClientBean> retrofitClients;
    private final List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList;

    public RetrofitClientCollectionHandler(List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList) {
        retrofitClients = new HashSet<>();
        this.retrofitApiInterfaceBeanList = retrofitApiInterfaceBeanList;
    }

    public Set<RetrofitClientBean> getRetrofitClients() {
        Set<? extends Class<?>> clientClasses = retrofitApiInterfaceBeanList.stream().map(RetrofitApiInterfaceBean::getParentClazz).collect(Collectors.toSet());
        for (Class<?> clientClass : clientClasses) {
            retrofitClients.add(new RetrofitClientBeanGenerator(retrofitApiInterfaceBeanList, clientClass).generate());
        }
        return retrofitClients;
    }
}
