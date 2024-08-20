package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.data.builder.GlobalRetrofitBuilderExtension;
import io.github.easyretrofit.core.data.clients.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class RetrofitClientCollectionHandlerTest {

    Set<Class<?>> classes;
    @Before
    public void setUp() throws Exception {
        classes = new HashSet<>();
        classes.add(ComplexApi.class);
        classes.add(ComplexL2Api.class);
        classes.add(ComplexL3Api.class);
        classes.add(ComplexL4Api.class);
    }
    @Test
    public void getRetrofitClients() {
        PreRetrofitSourceClassBeanCollectionHandler classBeanCollectionHandler = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        new RetrofitApiInterfaceBeanCollectionHandler(classBeanCollectionHandler.getInterfaceClassBeans(), new GlobalRetrofitBuilderExtension(),null, null);
//        RetrofitClientCollectionHandler handler = new RetrofitClientCollectionHandler();
    }
}