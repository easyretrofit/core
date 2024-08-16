package io.github.easyretrofit.core.resource.pre;

import io.github.easyretrofit.core.annotation.RetrofitBase;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.exception.RetrofitBaseException;

import java.util.*;

public class RetrofitResourceApiInterfaceClassBean {

    private final Class<?> myself;

    private Class<?> ancestor;

    private LinkedHashSet<Class<?>> self2Ancestors;

    private Set<Class<?>> children;

    public RetrofitResourceApiInterfaceClassBean(Class<?> myself) {
        this.myself = myself;
        this.self2Ancestors = new LinkedHashSet<>();
        this.children = new LinkedHashSet<>();
        fillParentsBean(myself);
    }

    public LinkedHashSet<Class<?>> getSelf2Ancestors() {
        return self2Ancestors;
    }

    public Class<?> getAncestor() {
        return ancestor;
    }

    public Class<?> getMyself() {
        return myself;
    }

    public Set<Class<?>> getChildren() {
        return children;
    }

    public void setChildren(Set<Class<?>> children) {
        this.children = children;
    }


    private void fillParentsBean(Class<?> clazz) {
        LinkedHashSet<Class<?>> parentClazzSet = new LinkedHashSet<>();
        this.ancestor = findParentClazzIncludeRetrofitBuilderAndBase(clazz, parentClazzSet);
        this.self2Ancestors =  parentClazzSet;
    }


    private Class<?> findParentClazzIncludeRetrofitBuilderAndBase(Class<?> clazz, Set<Class<?>> parentClazzSet) {

        Class<?> retrofitBuilderClazz;
        if (clazz.getDeclaredAnnotation(RetrofitBase.class) != null) {
            retrofitBuilderClazz = findParentRetrofitBaseClazz(clazz, parentClazzSet);
        } else {
            retrofitBuilderClazz = findParentRetrofitBuilderClazz(clazz, parentClazzSet);
        }
        if (retrofitBuilderClazz.getDeclaredAnnotation(RetrofitBuilder.class) == null) {
            retrofitBuilderClazz = findParentClazzIncludeRetrofitBuilderAndBase(retrofitBuilderClazz, parentClazzSet);
        }
        return retrofitBuilderClazz;
    }

    private Class<?> findParentRetrofitBuilderClazz(Class<?> clazz, Set<Class<?>> parentClazzSet) {
        RetrofitBuilder retrofitBuilder = clazz.getDeclaredAnnotation(RetrofitBuilder.class);
        Class<?> targetClazz = clazz;
        if (retrofitBuilder == null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                parentClazzSet.add(interfaces[0]);
                targetClazz = findParentRetrofitBuilderClazz(interfaces[0], parentClazzSet);
            } else {
                if (clazz.getDeclaredAnnotation(RetrofitBase.class) == null) {
                    throw new RetrofitBaseException("The baseApi of @RetrofitBase in the [" + clazz.getSimpleName() + "] Interface, does not define @RetrofitBuilder");
                }
            }
        }
        return targetClazz;
    }

    private Class<?> findParentRetrofitBaseClazz(Class<?> clazz, Set<Class<?>> parentClazzSet) {
        RetrofitBase retrofitBase = clazz.getDeclaredAnnotation(RetrofitBase.class);
        Class<?> targetClazz = clazz;
        if (retrofitBase != null) {
            final Class<?> baseApiClazz = retrofitBase.baseInterface();
            if (baseApiClazz != null) {
                parentClazzSet.add(baseApiClazz);
                targetClazz = findParentRetrofitBaseClazz(baseApiClazz, parentClazzSet);
            }
        }
        return targetClazz;
    }
}
