package io.github.easyretrofit.core.resource.pre;

import java.util.Set;

public class RetrofitResourceClientClassBean {

    private final Class<?> clientClazz;

    private final Set<RetrofitResourceApiInterfaceClassBean> apiInterfaceClassBeans;

    public RetrofitResourceClientClassBean(Set<RetrofitResourceApiInterfaceClassBean> apiBeans, Class<?> clientClazz) {
        apiInterfaceClassBeans = apiBeans;
        this.clientClazz = clientClazz;
    }

    public Class<?> getClientClazz() {
        return clientClazz;
    }

    public Set<RetrofitResourceApiInterfaceClassBean> getApiInterfaceClassBeans() {
        return apiInterfaceClassBeans;
    }
}
