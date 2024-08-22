package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.RetrofitApiInterfaceBean;
import io.github.easyretrofit.core.resource.RetrofitBuilderBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import io.github.easyretrofit.core.resource.UrlStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Print logs in the launcher</p>
 * <p>When you create a server-side web framework extension based on easy-retrofit-core, you need to call this class to complete easy-retrofit launcher log printing</p>
 * @author liuziyuan
 */
public class RetrofitResourceContextLog {
    private static final Logger log = LoggerFactory.getLogger(RetrofitResourceContextLog.class);

    private final static String LOG_INFO = "::{} :: ({})\n";
    private final RetrofitResourceContext context;


    public RetrofitResourceContextLog(RetrofitResourceContext context) {
        this.context = context;
    }

    /**
     * Print all plugins information for easy-retrofit
     * @param logBeans List of all plugins information for easy-retrofit
     */
    public void showLog(List<RetrofitExtensionLogBean> logBeans) {
        getLogoInfo(logBeans);

        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.warn("---Retrofit Client : HostURL[Dummy]: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            } else {
                log.info("---Retrofit Client : HostURL: {}, Retrofit instance name: {}", realHostUrl, retrofitInstanceName);
            }
            retrofitClientDebugLog(retrofitClient);
            for (RetrofitApiInterfaceBean retrofitService : retrofitClient.getRetrofitApiInterfaceBeans()) {
                final Class<?> selfClazz = retrofitService.getSelfClazz();
                final Class<?> parentClazz = retrofitService.getParentClazz();
                String parentClazzName = null;
                if (!parentClazz.getName().equals(selfClazz.getName())) {
                    parentClazzName = parentClazz.getName();
                }
                log.info("|--API Services: Interface name: {} , Parent Interface name: {}", selfClazz.getName(), parentClazzName);
                log.debug(retrofitService.toString());
            }
        }
    }

    private void getLogoInfo(List<RetrofitExtensionLogBean> logBeans){
        String logo = "\n" +
                " ___  __    ____   __   ___ ___ _____ ___  __  ___ _ _____  \n" +
                "| __|/  \\ /' _| `v' /__| _ \\ __|_   _| _ \\/__\\| __| |_   _| \n" +
                "| _|| /\\ |`._`.`. .'|__| v / _|  | | | v / \\/ | _|| | | |   \n" +
                "|___|_||_||___/ !_!    |_|_\\___| |_| |_|_\\\\__/|_| |_| |_|   \n";
//        String logo = "\n" +
//                "____ ___  ___  _         ____ ____ ____ ____ ____ ____ ____ ____ \n" +
//                "| __\\|  \\ | _\\ ||_/\\ ___ | . \\| __\\|_ _\\| . \\|   ||  _\\|___\\|_ _\\\n" +
//                "|  ]_| . \\[__ \\| __/|___\\|  <_|  ]_  || |  <_| . || _\\ | /    || \n" +
//                "|___/|/\\_/|___/|/        |/\\_/|___/  |/ |/\\_/|___/|/   |/     |/ \n";
        StringBuilder sb = new StringBuilder();
        sb.append(logo);
        List<String> params = new ArrayList<>();
        //add retrofit info
        sb.append(LOG_INFO);
        Package pkgInfo = this.getClass().getPackage();
        params.add(pkgInfo.getSpecificationTitle());
        params.add(pkgInfo.getSpecificationVersion());
        //add easy-retrofit info
        sb.append(LOG_INFO);
        params.add(pkgInfo.getImplementationTitle());
        params.add(pkgInfo.getImplementationVersion());
        for (RetrofitExtensionLogBean logBean : logBeans) {
            if (logBean.getTitle() == null)
                continue;
            sb.append(LOG_INFO);
            params.add(logBean.getTitle() == null ? "" : logBean.getTitle());
            params.add(logBean.getVersion() == null ? "" : logBean.getVersion());
        }
        String logStr = sb.toString();
        log.info(logStr, params.toArray());
    }


    private void retrofitClientDebugLog(RetrofitClientBean retrofitClient) {
        RetrofitBuilderBean retrofitBuilder = retrofitClient.getRetrofitBuilder();
        final String realHostUrl = retrofitClient.getRealHostUrl();
        final String globalEnable = retrofitBuilder.isEnable() ? "true" : "false";
        String CallAdapterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddCallAdapterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String ConverterFactoryString = StringUtils.join(Arrays.stream(retrofitBuilder.getAddConverterFactory()).map(Class::getSimpleName).collect(Collectors.toList()), ",");
        String callbackExecutorString = retrofitBuilder.getCallbackExecutor().getSimpleName();
        String clientString = retrofitBuilder.getClient().getSimpleName();
        String callFactoryString = retrofitBuilder.getCallFactory().getSimpleName();
        String validateEagerlyString = retrofitBuilder.isValidateEagerly() ? "true" : "false";
        String interceptor = retrofitClient.getInterceptors().toString();
        log.debug("RetrofitClientBean: HostURL: {}; UrlStatus: {}; globalEnable: {}; CallAdapterFactory: {}; ConverterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}; interceptor: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString, interceptor);

    }
}
