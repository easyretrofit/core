package io.github.easyretrofit.core.resource.handler;

import io.github.easyretrofit.core.data.builder.GlobalRetrofitBuilderExtension;
import io.github.easyretrofit.core.data.clients.*;
import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RetrofitApiInterfaceBeanCollectionHandlerTest {
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
    public void getRetrofitApiInterfaceBeans() {
        PreRetrofitSourceClassBeanCollectionHandler classBeanCollectionHandler = new PreRetrofitSourceClassBeanCollectionHandler(classes);
        List<RetrofitApiInterfaceBean> apiInterfaceBeans = new RetrofitApiInterfaceBeanCollectionHandler(classBeanCollectionHandler.getInterfaceClassBeans(), new GlobalRetrofitBuilderExtension(), new ArrayList<>(), new LocalEnvManager()).getRetrofitApiInterfaceBeans();
        assertEquals(4, apiInterfaceBeans.size());
        RetrofitApiInterfaceBean complexApiBean = apiInterfaceBeans.stream().filter(bean -> bean.getSelfClazz().equals(ComplexApi.class)).findFirst().get();
        assertEquals(complexApiBean.getSelf2ParentClasses().size(), 0);
        assertTrue(complexApiBean.getChildrenClasses().contains(ComplexL2Api.class));
        assertTrue(complexApiBean.getChildrenClasses().contains(ComplexL3Api.class));
        assertTrue(complexApiBean.getChildrenClasses().contains(ComplexL4Api.class));
        RetrofitApiInterfaceBean complexApiBean2 = apiInterfaceBeans.stream().filter(bean -> bean.getSelfClazz().equals(ComplexL2Api.class)).findFirst().get();
        assertEquals(complexApiBean2.getSelf2ParentClasses().size(), 1);
        assertTrue(complexApiBean2.getSelf2ParentClasses().contains(ComplexApi.class));
        assertTrue(complexApiBean2.getChildrenClasses().contains(ComplexL3Api.class));
        assertTrue(complexApiBean2.getChildrenClasses().contains(ComplexL4Api.class));
        RetrofitApiInterfaceBean complexApiBean3 = apiInterfaceBeans.stream().filter(bean -> bean.getSelfClazz().equals(ComplexL3Api.class)).findFirst().get();
        assertEquals(complexApiBean3.getSelf2ParentClasses().size(), 2);
        assertTrue(complexApiBean3.getSelf2ParentClasses().contains(ComplexApi.class));
        assertTrue(complexApiBean3.getSelf2ParentClasses().contains(ComplexL2Api.class));
        assertTrue(complexApiBean3.getChildrenClasses().contains(ComplexL4Api.class));
        RetrofitApiInterfaceBean complexApiBean4 = apiInterfaceBeans.stream().filter(bean -> bean.getSelfClazz().equals(ComplexL4Api.class)).findFirst().get();
        assertEquals(complexApiBean4.getSelf2ParentClasses().size(), 3);
        assertTrue(complexApiBean4.getSelf2ParentClasses().contains(ComplexApi.class));
        assertTrue(complexApiBean4.getSelf2ParentClasses().contains(ComplexL2Api.class));
        assertTrue(complexApiBean4.getSelf2ParentClasses().contains(ComplexL3Api.class));
        assertTrue(complexApiBean4.getChildrenClasses().isEmpty());
    }
}