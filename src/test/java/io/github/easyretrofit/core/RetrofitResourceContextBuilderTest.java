package io.github.easyretrofit.core;

import io.github.easyretrofit.core.data.clients.*;
import io.github.easyretrofit.core.data.common.GlobalRetrofitBuilderExtension;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RetrofitResourceContextBuilderTest {
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
    public void buildContextInstanceTest() {
        RetrofitResourceContextBuilder builder = new RetrofitResourceContextBuilder();
        RetrofitResourceContext retrofitResourceContext = builder.buildContextInstance(new String[]{"io.github.easyretrofit.core.data.clients"}, classes, new GlobalRetrofitBuilderExtension(), null, new LocalEnvManager());
        assertNotNull(retrofitResourceContext);
        assertEquals(1, retrofitResourceContext.getRetrofitClients().size());
    }
}