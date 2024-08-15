package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.util.UniqueKeyUtils;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RetrofitServiceBean with the same @RetrofitBuilder are aggregated into RetrofitClientBean
 *
 * @author liuziyuan
 */

public final class RetrofitClientBean implements UniqueKey {

    private String retrofitInstanceName;
    private String realHostUrl;
    private UrlStatus urlStatus;
    private RetrofitBuilderBean retrofitBuilder;
    private final Set<RetrofitInterceptorBean> interceptors;
    //    private Set<RetrofitInterceptorBean> inheritedInterceptors;
    private List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeans;


    public RetrofitClientBean(RetrofitApiInterfaceBean serviceBean) {
        this.interceptors = new LinkedHashSet<>();
//        this.inheritedInterceptors = new LinkedHashSet<>();
        this.retrofitApiInterfaceBeans = new ArrayList<>();
        this.setRetrofitBuilder(serviceBean.getRetrofitBuilder());
        this.setRealHostUrl(serviceBean.getRetrofitUrl().getDefaultUrl().getRealHostUrl());
        this.setUrlStatus(serviceBean.getRetrofitUrl().getUrlStatus());
        this.setInterceptors(serviceBean.getInterceptors());
        this.addInheritedInterceptors(serviceBean.getMyInterceptors());
        this.setRetrofitInstanceName();
    }

    public void addRetrofitApiServiceBean(RetrofitApiInterfaceBean retrofitApiInterfaceBean) {
        retrofitApiInterfaceBeans.add(retrofitApiInterfaceBean);
        retrofitApiInterfaceBean.setRetrofitClientBean(this);
    }

    public void addInheritedInterceptors(Set<RetrofitInterceptorBean> serviceInheritedInterceptors) {
        this.interceptors.addAll(serviceInheritedInterceptors);
    }

    public String getRetrofitInstanceName() {
        return retrofitInstanceName;
    }

    public String getRealHostUrl() {
        return realHostUrl;
    }

    void setRealHostUrl(String realHostUrl) {
        this.realHostUrl = realHostUrl;
    }

    public UrlStatus getUrlStatus() {
        return urlStatus;
    }

    void setUrlStatus(UrlStatus urlStatus) {
        this.urlStatus = urlStatus;
    }

    public RetrofitBuilderBean getRetrofitBuilder() {
        return retrofitBuilder;
    }

    void setRetrofitBuilder(RetrofitBuilderBean retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    public Set<RetrofitInterceptorBean> getInterceptors() {
        return interceptors;
    }

    void setInterceptors(Set<RetrofitInterceptorBean> interceptors) {
        this.interceptors.addAll(interceptors);
    }

    public List<RetrofitApiInterfaceBean> getRetrofitApiServiceBeans() {
        return retrofitApiInterfaceBeans;
    }

    void setRetrofitApiServiceBeans(List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeans) {
        this.retrofitApiInterfaceBeans = retrofitApiInterfaceBeans;
    }

    @Override
    public String toString() {
        String interceptorsStr = null;
        if (interceptors != null) {
            interceptorsStr = interceptors.stream().map(RetrofitInterceptorBean::toString).collect(Collectors.joining(","));
        }
        return "RetrofitClientBean{" +
                "realHostUrl='" + realHostUrl + '\'' +
                ", urlStatus=" + urlStatus.toString() +
                ", retrofitBuilder=" + retrofitBuilder.toString() +
                ", interceptors=" + interceptorsStr +
                '}';
    }

    @Override
    public String generateUniqueKey() {
        return UniqueKeyUtils.generateUniqueKey(this.toString());
    }

    /**
     * set retrofit instance name when RetrofitClientBean instance created, the retrofitInstanceName need whole attributes filled.
     * important
     */
    public void setRetrofitInstanceName() {
        this.retrofitInstanceName = Retrofit.class.getSimpleName().concat("@" + this.generateUniqueKey());
    }
}
