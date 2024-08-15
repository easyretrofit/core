package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.util.UniqueKeyUtils;
import retrofit2.Retrofit;

import java.util.*;
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
    private Set<RetrofitInterceptorBean> interceptors;
    //    private Set<RetrofitInterceptorBean> inheritedInterceptors;
    private List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeans;


    public RetrofitClientBean(RetrofitApiInterfaceBean serviceBean, List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList) {
//        this.inheritedInterceptors = new LinkedHashSet<>();
        this.setRetrofitApiInterfaceBeans(serviceBean, retrofitApiInterfaceBeanList);
        this.setRetrofitBuilder(serviceBean.getRetrofitBuilder());
        this.setRealHostUrl(serviceBean.getRetrofitUrl().getDefaultUrl().getRealHostUrl());
        this.setUrlStatus(serviceBean.getRetrofitUrl().getUrlStatus());
        this.setInterceptors(serviceBean, retrofitApiInterfaceBeanList);
        this.setRetrofitInstanceName();

    }

    private void setRetrofitApiInterfaceBeans(RetrofitApiInterfaceBean serviceBean, List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList) {
        retrofitApiInterfaceBeans = new ArrayList<>();
        for (RetrofitApiInterfaceBean apiInterfaceBean : retrofitApiInterfaceBeanList) {
            if (apiInterfaceBean.getParentClazz() != null && serviceBean.getSelfClazz().equals(apiInterfaceBean.getParentClazz())) {
                retrofitApiInterfaceBeans.add(apiInterfaceBean);
            }
        }
    }

    private void setInterceptors(RetrofitApiInterfaceBean serviceBean, List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList) {
        interceptors = new HashSet<>();
        if (serviceBean.getMyInterceptors() != null) {
            interceptors.addAll(serviceBean.getMyInterceptors());
        }
        for (RetrofitApiInterfaceBean retrofitApiInterfaceBean : retrofitApiInterfaceBeans) {
            if (retrofitApiInterfaceBean.getMyInterceptors() != null) {
                Set<RetrofitInterceptorBean> childrenInterceptors = retrofitApiInterfaceBean.getMyInterceptors();
                for (RetrofitInterceptorBean childrenInterceptor : childrenInterceptors) {
                    childrenInterceptor.setDefaultScopeClasses(retrofitApiInterfaceBean.getChildrenClasses().stream().map(Class::getName).collect(Collectors.toSet()));
                }
                interceptors.addAll(childrenInterceptors);
            }
        }
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


    public List<RetrofitApiInterfaceBean> getRetrofitApiInterfaceBeans() {
        return retrofitApiInterfaceBeans;
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
