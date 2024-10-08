package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.exception.RetrofitExtensionException;
import io.github.easyretrofit.core.delegate.BaseExceptionDelegate;
import io.github.easyretrofit.core.util.UniqueKeyUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Each API interface file corresponds to a RetrofitServiceBean
 *
 * @author liuziyuan
 */
public final class RetrofitApiInterfaceBean implements UniqueKey {
    private String retrofitClientBeanInstanceName;
    private Class<?> selfClazz;
    private Class<?> parentClazz;
    private LinkedHashSet<Class<?>> self2ParentClasses;
    private Set<Class<?>> childrenClasses;
    private RetrofitUrl retrofitUrl;
    private RetrofitBuilderBean retrofitBuilder;
    private Set<RetrofitInterceptorBean> myInterceptors;
    private Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> exceptionDelegates;

    public RetrofitApiInterfaceBean() {
        childrenClasses = new LinkedHashSet<>();
    }

    public void setRetrofitClientBean(RetrofitClientBean retrofitClientBean) {
        this.retrofitClientBeanInstanceName = retrofitClientBean.getRetrofitInstanceName();
        this.retrofitBuilder = retrofitClientBean.getRetrofitBuilder();
    }

    public void addExceptionDelegate(Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> clazz) {
        if (exceptionDelegates == null) {
            exceptionDelegates = new HashSet<>();
        }
        exceptionDelegates.add(clazz);
    }

    public <T extends Annotation> T getAnnotationResource(Class<? extends Annotation> clazz) {
        Class<?> selfClazz = this.getSelfClazz();
        Class<?> parentClazz = this.getParentClazz();
        T annotationInSelf = (T) selfClazz.getDeclaredAnnotation(clazz);
        if (annotationInSelf != null) {
            return annotationInSelf;
        }

        if (parentClazz != null) {
            T annotationInParent = (T) parentClazz.getDeclaredAnnotation(clazz);
            if (annotationInParent != null) {
                return annotationInParent;
            }
        }

        return null;
    }

    public Class<?> getSelfClazz() {
        return selfClazz;
    }

    void setSelfClazz(Class<?> selfClazz) {
        this.selfClazz = selfClazz;
    }

    public Class<?> getParentClazz() {
        return parentClazz;
    }

    void setParentClazz(Class<?> parentClazz) {
        this.parentClazz = parentClazz;
    }

    public RetrofitUrl getRetrofitUrl() {
        return retrofitUrl;
    }

    void setRetrofitUrl(RetrofitUrl retrofitUrl) {
        this.retrofitUrl = retrofitUrl;
    }

    public RetrofitBuilderBean getRetrofitBuilder() {
        return retrofitBuilder;
    }

    void setRetrofitBuilder(RetrofitBuilderBean retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public LinkedHashSet<Class<?>> getSelf2ParentClasses() {
        return self2ParentClasses;
    }

    public void setSelf2ParentClasses(LinkedHashSet<Class<?>> self2ParentClasses) {
        this.self2ParentClasses = self2ParentClasses;
    }

    public Set<Class<?>> getChildrenClasses() {
        return childrenClasses;
    }

    public void setChildrenClasses(Set<Class<?>> childrenClasses) {
        this.childrenClasses = childrenClasses;
    }
    //    public Set<RetrofitInterceptorBean> getInterceptors() {
//        return interceptors;
//    }
//
//    void setInterceptors(Set<RetrofitInterceptorBean> interceptors) {
//        this.interceptors = interceptors;
//    }

    public Set<RetrofitInterceptorBean> getMyInterceptors() {
        return myInterceptors;
    }

    void setMyInterceptors(Set<RetrofitInterceptorBean> myInterceptors) {
        this.myInterceptors = myInterceptors;
    }

    public String getRetrofitClientBeanInstanceName() {
        return retrofitClientBeanInstanceName;
    }

    public Set<Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>>> getExceptionDelegates() {
        return exceptionDelegates;
    }

    @Override
    public String toString() {
        String myInterceptorStr = null;
        if (myInterceptors != null) {
            myInterceptorStr = myInterceptors.stream().map(RetrofitInterceptorBean::toString).collect(Collectors.toSet()).toString();
        }
        String exceptionDelegateStr = null;
        if (exceptionDelegates != null) {
            exceptionDelegateStr = exceptionDelegates.stream().map(Class::getName).collect(Collectors.toSet()).toString();
        }
        return "RetrofitApiServiceBean{" +
                "selfClazz=" + selfClazz +
                ", parentClazz=" + parentClazz +
                ", retrofitUrl=" + retrofitUrl.getUniqueKey() +
                ", retrofitBuilder=" + retrofitBuilder.getUniqueKey() +
                ", myInterceptors=" + myInterceptorStr +
                ", exceptionDelegates=" + exceptionDelegateStr +
                '}';
    }

    @Override
    public String getUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }

}
