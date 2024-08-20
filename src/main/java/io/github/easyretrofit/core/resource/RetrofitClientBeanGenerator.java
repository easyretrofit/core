package io.github.easyretrofit.core.resource;

import io.github.easyretrofit.core.Generator;

import java.util.List;

/**
 * generate RetrofitClientBean object
 *
 * @author liuziyuan
 */
public class RetrofitClientBeanGenerator implements Generator<RetrofitClientBean> {
    private final List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList;
    private final Class<?> clientBeanClass;

    public RetrofitClientBeanGenerator(List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeanList, Class<?> clazz) {
        this.retrofitApiInterfaceBeanList = retrofitApiInterfaceBeanList;
        this.clientBeanClass = clazz;
    }

    @Override
    public RetrofitClientBean generate() {
        RetrofitClientBean clientBean = null;
        RetrofitApiInterfaceBean apiInterfaceBean = retrofitApiInterfaceBeanList.stream().filter(api -> api.getSelfClazz().equals(clientBeanClass)).findFirst().orElse(null);
        if (apiInterfaceBean != null) {
            clientBean = new RetrofitClientBean(apiInterfaceBean, retrofitApiInterfaceBeanList);
            return clientBean;
        }
        throw new RuntimeException("not found RetrofitClientBean class: " + clientBeanClass);
    }


}
