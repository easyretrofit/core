package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.annotation.InterceptorType;
import io.github.easyretrofit.core.annotation.RetrofitInterceptor;
import io.github.easyretrofit.core.annotation.RetrofitInterceptorParam;
import io.github.easyretrofit.core.extension.BaseInterceptor;
import io.github.easyretrofit.core.util.UniqueKeyUtils;

import java.util.Arrays;
import java.util.Set;

public final class RetrofitInterceptorBean implements UniqueKey {
    private final Class<? extends BaseInterceptor> handler;

    private final Class<?>[] defaultScopeClasses;

    private InterceptorType type;

    private final String[] include;

    private final String[] exclude;

    private final int sort;


    public RetrofitInterceptorBean(RetrofitInterceptor retrofitInterceptor, RetrofitInterceptorParam retrofitInterceptorParam, Set<Class<?>> defaultScopeClasses) {
        this.defaultScopeClasses = defaultScopeClasses.toArray(new Class[0]);
        this.handler = retrofitInterceptor.handler();
        this.type = retrofitInterceptorParam.type();
        this.include = retrofitInterceptorParam.include();
        this.exclude = retrofitInterceptorParam.exclude();
        this.sort = retrofitInterceptorParam.sort();
    }

    public RetrofitInterceptorBean(RetrofitInterceptor retrofitInterceptor, Set<Class<?>> defaultScopeClasses) {
        this.defaultScopeClasses = defaultScopeClasses.toArray(new Class[0]);
        this.handler = retrofitInterceptor.handler();
        this.type = retrofitInterceptor.type();
        this.include = retrofitInterceptor.include();
        this.exclude = retrofitInterceptor.exclude();
        this.sort = retrofitInterceptor.sort();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        RetrofitInterceptorBean that = (RetrofitInterceptorBean) o;
//        return sort == that.sort && handler.equals(that.handler) && defaultScopeClasses.equals(that.defaultScopeClasses) && type == that.type && Arrays.equals(include, that.include) && Arrays.equals(exclude, that.exclude);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = handler.hashCode();
//        result = 31 * result + defaultScopeClasses.hashCode();
//        result = 31 * result + type.hashCode();
//        result = 31 * result + Arrays.hashCode(include);
//        result = 31 * result + Arrays.hashCode(exclude);
//        result = 31 * result + sort;
//        return result;
//    }

    public Class<? extends BaseInterceptor> getHandler() {
        return handler;
    }

    public InterceptorType getType() {
        return type;
    }

    public void setType(InterceptorType type) {
        this.type = type;
    }

    public String[] getInclude() {
        return include;
    }

    public String[] getExclude() {
        return exclude;
    }

    public Class<?>[] getDefaultScopeClasses() {
        return defaultScopeClasses;
    }

    public int getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return "RetrofitInterceptorBean{" +
                "handler=" + handler +
                ", type=" + type +
                ", defaultScopeClasses=" + Arrays.toString(defaultScopeClasses) +
                ", include=" + Arrays.toString(include) +
                ", exclude=" + Arrays.toString(exclude) +
                ", sort=" + sort +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }
}
