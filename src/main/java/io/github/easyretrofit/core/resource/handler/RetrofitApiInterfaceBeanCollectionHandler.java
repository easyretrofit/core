package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.EnvManager;
import io.github.easyretrofit.core.RetrofitBuilderExtension;
import io.github.easyretrofit.core.RetrofitInterceptorExtension;
import io.github.easyretrofit.core.resource.PreRetrofitResourceApiInterfaceClassBean;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBeanGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RetrofitApiInterfaceBeanCollectionHandler {
    private final Set<PreRetrofitResourceApiInterfaceClassBean> apiInterfaceClassBeans;
    private final RetrofitBuilderExtension globalRetrofitBuilderExtension;
    private final List<RetrofitInterceptorExtension> interceptorExtensions;
    private final EnvManager envManager;

    public RetrofitApiInterfaceBeanCollectionHandler(Set<PreRetrofitResourceApiInterfaceClassBean> apiInterfaceClassBeans,
                                                     RetrofitBuilderExtension globalRetrofitBuilderExtension,
                                                     List<RetrofitInterceptorExtension> interceptorExtensions,
                                                     EnvManager envManager) {
        this.apiInterfaceClassBeans = apiInterfaceClassBeans;
        this.globalRetrofitBuilderExtension = globalRetrofitBuilderExtension;
        this.interceptorExtensions = interceptorExtensions;
        this.envManager = envManager;
    }

    public List<RetrofitApiInterfaceBean> getRetrofitApiInterfaceBeans() {
        List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList = new ArrayList<>();
        for (PreRetrofitResourceApiInterfaceClassBean apiInterfaceClassBean : apiInterfaceClassBeans) {
            RetrofitApiInterfaceBean apiInterfaceBean = new RetrofitApiInterfaceBeanGenerator(apiInterfaceClassBean, envManager, globalRetrofitBuilderExtension, interceptorExtensions).generate();
            if (apiInterfaceBean != null) {
                retrofitApiInterfaceBeanList.add(apiInterfaceBean);
            }
        }
        return retrofitApiInterfaceBeanList;
    }
}
