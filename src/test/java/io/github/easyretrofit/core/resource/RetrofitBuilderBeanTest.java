package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.data.builder.BuilderApi;
import io.github.easyretrofit.core.data.builder.GlobalRetrofitBuilderExtension;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RetrofitBuilderBeanTest {


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void compare() {
        RetrofitBuilderBean retrofitBuilderBean = new RetrofitBuilderBean(BuilderApi.class, new GlobalRetrofitBuilderExtension());
        RetrofitBuilderBean retrofitBuilderBean2 = new RetrofitBuilderBean(BuilderApi.class, new GlobalRetrofitBuilderExtension());

        assertEquals(retrofitBuilderBean, retrofitBuilderBean2);
        assertEquals(retrofitBuilderBean.hashCode(), retrofitBuilderBean2.hashCode());
        assertEquals(retrofitBuilderBean.generateUniqueKey(), retrofitBuilderBean2.generateUniqueKey());
    }
}