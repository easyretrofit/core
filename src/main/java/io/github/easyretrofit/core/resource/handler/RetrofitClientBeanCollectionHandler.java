package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitClientBeanGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class RetrofitClientBeanCollectionHandler {
    private final List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList;

    public RetrofitClientBeanCollectionHandler(List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList) {
        this.retrofitApiInterfaceBeanList = retrofitApiInterfaceBeanList;
    }

    public Set<RetrofitClientBean> getRetrofitClients() {
        List<RetrofitClientBean> baseRetrofitClients = getBaseRetrofitClients();
        return getSingleRetrofitClients(baseRetrofitClients);
    }

    private List<RetrofitClientBean> getBaseRetrofitClients() {
        List<RetrofitClientBean> retrofitClients = new ArrayList<>();
        Set<? extends Class<?>> clientClasses = retrofitApiInterfaceBeanList.stream().map(RetrofitApiInterfaceBean::getParentClazz).collect(Collectors.toSet());
        for (Class<?> clientClass : clientClasses) {
            List<RetrofitApiInterfaceBean> collect = retrofitApiInterfaceBeanList.stream().filter(bean -> bean.getParentClazz().equals(clientClass)).collect(Collectors.toList());
            retrofitClients.add(new RetrofitClientBeanGenerator(collect, clientClass).generate());
        }
        return retrofitClients;
    }

    private Set<RetrofitClientBean> getSingleRetrofitClients(List<RetrofitClientBean> baseRetrofitClients) {
        Map<String, RetrofitClientBean> mergedMap = new HashMap<>();
        for (RetrofitClientBean baseRetrofitClient : baseRetrofitClients) {
            if (mergedMap.containsKey(baseRetrofitClient.getUniqueKey())) {
                RetrofitClientBean existingRetrofitClient = mergedMap.get(baseRetrofitClient.getUniqueKey());
                existingRetrofitClient.getRetrofitApiInterfaceBeans().addAll(baseRetrofitClient.getRetrofitApiInterfaceBeans());
                existingRetrofitClient.getInterceptors().addAll(baseRetrofitClient.getInterceptors());
            } else {
                mergedMap.put(baseRetrofitClient.getUniqueKey(), baseRetrofitClient);
            }
        }
        return new HashSet<>(mergedMap.values());
    }
}
