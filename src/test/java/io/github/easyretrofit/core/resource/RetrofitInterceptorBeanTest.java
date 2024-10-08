package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.data.apiinterface.same.SameApi;
import io.github.easyretrofit.core.data.apiinterface.same.SameApi2;
import io.github.easyretrofit.core.data.clients.LocalEnvManager;
import io.github.easyretrofit.core.data.common.GlobalRetrofitBuilderExtension;
import io.github.easyretrofit.core.data.interceptor.SingleInterceptorApi;
import io.github.easyretrofit.core.resource.handler.PreRetrofitSourceClassBeanCollectionHandler;
import io.github.easyretrofit.core.resource.handler.RetrofitApiInterfaceBeanCollectionHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RetrofitInterceptorBeanTest {
    Set<Class<?>> classes;
    @Before
    public void setUp() throws Exception {
        classes = new HashSet<>();
        classes.add(SingleInterceptorApi.class);
    }

    @Test
    public void test() {
        PreRetrofitSourceClassBeanCollectionHandler classBeanCollectionHandler = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        List<RetrofitApiInterfaceBean> apiInterfaceBeans = new RetrofitApiInterfaceBeanCollectionHandler(classBeanCollectionHandler.getInterfaceClassBeans(), new GlobalRetrofitBuilderExtension(), new ArrayList<>(), new LocalEnvManager()).getRetrofitApiInterfaceBeans();
        assertEquals(apiInterfaceBeans.get(0).getMyInterceptors().size(), 1);
    }
}