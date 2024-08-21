package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.data.pre.*;
import io.github.easyretrofit.core.resource.PreRetrofitResourceApiInterfaceClassBean;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PreRetrofitSourceClassBeanCollectionHandlerTest {

        Set<Class<?>> classes;
        @Before
        public void setUp() throws Exception {
            classes = new HashSet<>();
            classes.add(ComplexApi.class);
            classes.add(ComplexL2Api.class);
            classes.add(ComplexL3Api.class);
            classes.add(ComplexL4Api.class);
            classes.add(BaseApi.class);
            classes.add(G1L1Api.class);
            classes.add(G1L2Api.class);
            classes.add(G1L3Api.class);
            classes.add(G2L1Api.class);
            classes.add(G2L2Api.class);
            classes.add(G2L3Api.class);
            classes.add(MixedApi.class);
            classes.add(OtherApi.class);
        }

    @Test
    public void getInterfaceClassBeans() {
        PreRetrofitSourceClassBeanCollectionHandler preClassBean = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        Set<PreRetrofitResourceApiInterfaceClassBean> interfaceClassBeans = preClassBean.getInterfaceClassBeans();
        assertEquals(interfaceClassBeans.size(), 13);
        PreRetrofitResourceApiInterfaceClassBean bean = interfaceClassBeans.stream().filter(api -> api.getMyself().equals(ComplexApi.class)).findFirst().get();
        assertEquals(bean.getChildren().size(), 4);
        assertTrue(bean.getChildren().contains(ComplexApi.class));
    }
}