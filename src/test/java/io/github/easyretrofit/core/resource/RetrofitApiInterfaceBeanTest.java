package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.data.apiinterface.same.SameApi;
import io.github.easyretrofit.core.data.apiinterface.same.SameApi2;
import io.github.easyretrofit.core.data.clients.LocalEnvManager;
import io.github.easyretrofit.core.data.common.GlobalRetrofitBuilderExtension;
import io.github.easyretrofit.core.data.pre.*;
import io.github.easyretrofit.core.resource.handler.PreRetrofitSourceClassBeanCollectionHandler;
import io.github.easyretrofit.core.resource.handler.RetrofitApiInterfaceBeanCollectionHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
public class RetrofitApiInterfaceBeanTest {

    Set<Class<?>> classes;
    @Before
    public void setUp() throws Exception {
        classes = new HashSet<>();
        classes.add(SameApi.class);
        classes.add(SameApi2.class);
    }

    @Test
    public void compareTest() {
        PreRetrofitSourceClassBeanCollectionHandler classBeanCollectionHandler = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        List<RetrofitApiInterfaceBean> apiInterfaceBeans = new RetrofitApiInterfaceBeanCollectionHandler(classBeanCollectionHandler.getInterfaceClassBeans(), new GlobalRetrofitBuilderExtension(), new ArrayList<>(), new LocalEnvManager()).getRetrofitApiInterfaceBeans();
        RetrofitApiInterfaceBean apiInterfaceBean = apiInterfaceBeans.get(0);
        RetrofitApiInterfaceBean apiInterfaceBean1 = apiInterfaceBeans.get(1);
        System.out.println(apiInterfaceBean.toString());
        System.out.println(apiInterfaceBean1.toString());
//        assertTrue(Arrays.asList(apiInterfaceBean.toString().split(",")).contains(" myInterceptors=1227340805"));
//        assertTrue(Arrays.asList(apiInterfaceBean1.toString().split(",")).contains(" myInterceptors=1578179019"));
    }
  
}