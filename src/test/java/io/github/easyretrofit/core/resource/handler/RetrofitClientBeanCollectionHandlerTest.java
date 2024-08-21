package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.data.common.*;
import io.github.easyretrofit.core.data.clients.*;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.RetrofitInterceptorBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RetrofitClientBeanCollectionHandlerTest {

    Set<Class<?>> classes;
    @Before
    public void setUp() throws Exception {
        classes = new HashSet<>();
        classes.add(OtherApi.class);
        classes.add(ComplexApi.class);
        classes.add(ComplexL2Api.class);
        classes.add(ComplexL3Api.class);
        classes.add(ComplexL4Api.class);
    }
    @Test
    public void getRetrofitClients() {
        PreRetrofitSourceClassBeanCollectionHandler classBeanCollectionHandler = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        List<RetrofitApiInterfaceBean> apiInterfaceBeans = new RetrofitApiInterfaceBeanCollectionHandler(classBeanCollectionHandler.getInterfaceClassBeans(), new GlobalRetrofitBuilderExtension(), new ArrayList<>(), new LocalEnvManager()).getRetrofitApiInterfaceBeans();
        Set<RetrofitClientBean> retrofitClients = new RetrofitClientBeanCollectionHandler(apiInterfaceBeans).getRetrofitClients();
        assertEquals(1, retrofitClients.size());
        RetrofitClientBean retrofitClientBean = retrofitClients.stream().findFirst().get();
        Set<RetrofitInterceptorBean> interceptors = retrofitClientBean.getInterceptors();
        assertTrue(interceptors.stream().anyMatch(retrofitInterceptorBean -> retrofitInterceptorBean.getHandler().equals(MyRetrofitInterceptor.class)));
        assertTrue(interceptors.stream().anyMatch(retrofitInterceptorBean -> retrofitInterceptorBean.getHandler().equals(MyRetrofitInterceptor1.class)));
        assertTrue(interceptors.stream().anyMatch(retrofitInterceptorBean -> retrofitInterceptorBean.getHandler().equals(MyRetrofitInterceptor2.class)));
        assertTrue(interceptors.stream().anyMatch(retrofitInterceptorBean -> retrofitInterceptorBean.getHandler().equals(MyRetrofitInterceptor3.class)));
        List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeans = retrofitClientBean.getRetrofitApiInterfaceBeans();
        assertEquals(5, retrofitApiInterfaceBeans.size());

    }
}