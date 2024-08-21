package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.annotation.RetrofitBase;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.core.exception.RetrofitBaseException;

import java.util.*;

public class PreRetrofitResourceApiInterfaceClassBean {

    private final Class<?> myself;

    private Class<?> ancestor;

    private LinkedHashSet<Class<?>> self2Ancestors;

    private final Set<Class<?>> children;

    public PreRetrofitResourceApiInterfaceClassBean(Class<?> myself) {
        this.myself = myself;
        this.self2Ancestors = new LinkedHashSet<>();
        this.children = new LinkedHashSet<>();
        fillParentsBean(myself);
        if (this.myself == this.ancestor) {
            this.self2Ancestors.add(this.myself);
            this.children.add(this.myself);
        }
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

    private void fillParentsBean(Class<?> clazz) {
        LinkedHashSet<Class<?>> parentClazzSet = new LinkedHashSet<>();
        this.ancestor = findParentClazzIncludeRetrofitBuilderAndBase(clazz, parentClazzSet);
        this.self2Ancestors = parentClazzSet;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreRetrofitResourceApiInterfaceClassBean)) return false;

        PreRetrofitResourceApiInterfaceClassBean that = (PreRetrofitResourceApiInterfaceClassBean) o;
        return myself.equals(that.myself) &&
                ancestor.equals(that.ancestor) &&
                self2Ancestors.equals(that.self2Ancestors) &&
                children.equals(that.children);
    }

    @Override
    public int hashCode() {
        int result = myself.hashCode();
        result = 31 * result + ancestor.hashCode();
        result = 31 * result + self2Ancestors.hashCode();
        result = 31 * result + children.hashCode();
        return result;
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
