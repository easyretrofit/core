package io.github.easyretrofit.core;

import io.github.easyretrofit.core.resource.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Print logs in the launcher</p>
 * <p>When you create a server-side web framework extension based on easy-retrofit-core, you need to call this class to complete easy-retrofit launcher log printing</p>
 *
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
     * Print logo and log information for easy-retrofit
     *
     * @param logBean easy-retrofit information in web framework
     */
    public void showLog(RetrofitWebFramewrokInfoBean logBean) {
        getLogoInfo(logBean);
        int index = 0;
        for (RetrofitClientBean retrofitClient : context.getRetrofitClients()) {
            log.info("==========================RETROFIT CLIENT INFO [{}] BEGIN==========================", index);
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.warn("*--RETROFIT CLIENT INFO [{}]: hostURL[Dummy]: {}, retrofitInstanceName: {}", index, realHostUrl, retrofitInstanceName);
            } else {
                log.info("*--RETROFIT CLIENT INFO [{}]: hostURL: {}, retrofitInstanceName: {}", index, realHostUrl, retrofitInstanceName);
            }
            retrofitClientDebugLog(retrofitClient);
            for (RetrofitApiInterfaceBean retrofitApiInterface : retrofitClient.getRetrofitApiInterfaceBeans()) {
                final Class<?> selfClazz = retrofitApiInterface.getSelfClazz();
                final Class<?> parentClazz = retrofitApiInterface.getParentClazz();
                String parentClazzName = null;
                if (!parentClazz.getName().equals(selfClazz.getName())) {
                    parentClazzName = parentClazz.getName();
                }
                final String self2ParentClasses = StringUtils.join(retrofitApiInterface.getSelf2ParentClasses(), "->");
                final String childrenClasses = StringUtils.join(retrofitApiInterface.getChildrenClasses(), ",");
                log.info("");
                log.info("|--API INTERFACE INFO: name: {} , rootName: {}, self2RootPath: {}, childrenName: {}", selfClazz.getName(), parentClazzName, self2ParentClasses, childrenClasses);
                retrofitApiInterfaceDebugLog(retrofitApiInterface, retrofitClient);
            }
            log.info("==========================RETROFIT CLIENT INFO [{}] END==========================", index);
            log.info("");
            index++;
        }
    }

    private void getLogoInfo(RetrofitWebFramewrokInfoBean logBean) {
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
        params.add(appendEasyRetrofit(pkgInfo.getSpecificationTitle()));
        params.add(pkgInfo.getSpecificationVersion());
        //add easy-retrofit info
        sb.append(LOG_INFO);
        params.add(appendEasyRetrofit(pkgInfo.getImplementationTitle()));
        params.add(pkgInfo.getImplementationVersion());
        if (logBean.getTitle() != null && logBean.getVersion() != null) {
            sb.append(LOG_INFO);
            params.add(logBean.getTitle() == null ? "" : appendEasyRetrofit(logBean.getTitle()));
            params.add(logBean.getVersion() == null ? "" : logBean.getVersion());

            Class<?> builderExtensionClazz = context.getRetrofitBuilderExtensionClazz();
            if (builderExtensionClazz.getPackage().getImplementationTitle() != null && !params.stream().anyMatch(s -> s.equals(logBean.getTitle()))) {
                sb.append(LOG_INFO);
                params.add(appendEasyRetrofit(builderExtensionClazz.getPackage().getImplementationTitle()));
                params.add(builderExtensionClazz.getPackage().getImplementationVersion());
            }
            List<Class<?>> interceptorExtensionsClasses = context.getInterceptorExtensionsClasses();
            for (Class<?> interceptorExtensionsClass : interceptorExtensionsClasses) {
                if (interceptorExtensionsClass.getPackage().getImplementationTitle() == null) {
                    continue;
                }
                sb.append(LOG_INFO);
                params.add(appendEasyRetrofit(interceptorExtensionsClass.getPackage().getImplementationTitle()));
                params.add(interceptorExtensionsClass.getPackage().getImplementationVersion());
            }
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
        log.debug("|--BUILDER INFO: hostURL: {}; urlStatus: {}; globalEnable: {}; callAdapterFactory: {}; converterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString);
        for (RetrofitInterceptorBean interceptor : retrofitClient.getInterceptors()) {
            log.debug("|--INTERCEPTOR INFO: handler: {}", interceptor.getHandler());
            log.debug("   |--type: {}", interceptor.getType());
            log.debug("   |--defaultScopeClasses: {}", StringUtils.join(interceptor.getDefaultScopeClasses(), ","));
            log.debug("   |--include: {}", StringUtils.join(interceptor.getInclude(), ","));
            log.debug("   |--exclude: {}", StringUtils.join(interceptor.getExclude(), ","));
            log.debug("   |--sort: {}", interceptor.getSort());
        }

    }

    private void retrofitApiInterfaceDebugLog(RetrofitApiInterfaceBean retrofitApiInterface, RetrofitClientBean retrofitClient) {
        Map<Class<?>, Set<RetrofitInterceptorBean>> parentInterceptors = getParentInterceptors(retrofitApiInterface, retrofitClient);
        for (Map.Entry<Class<?>, Set<RetrofitInterceptorBean>> entry : parentInterceptors.entrySet()) {
            for (RetrofitInterceptorBean retrofitInterceptorBean : entry.getValue()) {
                log.debug("   |--INTERCEPTOR INFO: handler: {}", retrofitInterceptorBean.getHandler());
                log.debug("      |--belongsTo: {}", entry.getKey());
                log.debug("      |--type: {}", retrofitInterceptorBean.getType());
                log.debug("      |--defaultScopeClasses: {}", StringUtils.join(retrofitInterceptorBean.getDefaultScopeClasses(), ","));
                log.debug("      |--include: {}", StringUtils.join(retrofitInterceptorBean.getInclude(), ","));
                log.debug("      |--exclude: {}", StringUtils.join(retrofitInterceptorBean.getExclude(), ","));
                log.debug("      |--sort: {}", retrofitInterceptorBean.getSort());
            }
        }
    }

    private Map<Class<?>, Set<RetrofitInterceptorBean>> getParentInterceptors(RetrofitApiInterfaceBean retrofitApiInterface, RetrofitClientBean retrofitClient) {
        List<RetrofitApiInterfaceBean> retrofitApiInterfaceBeans = retrofitClient.getRetrofitApiInterfaceBeans();
        LinkedHashSet<Class<?>> self2ParentClasses = retrofitApiInterface.getSelf2ParentClasses();
        Map<Class<?>, Set<RetrofitInterceptorBean>> activeInterceptorMap = new HashMap<>();
        // add myself interceptor
        activeInterceptorMap.put(retrofitApiInterface.getSelfClazz(), retrofitApiInterface.getMyInterceptors());
        // add parents interceptor
        for (Class<?> self2ParentClass : self2ParentClasses) {
            RetrofitApiInterfaceBean apiInterfaceBean = retrofitApiInterfaceBeans.stream().filter(api -> api.getSelfClazz().equals(self2ParentClass)).findFirst().get();
            activeInterceptorMap.put(self2ParentClass, apiInterfaceBean.getMyInterceptors());
        }
        return activeInterceptorMap;
    }

    private String appendEasyRetrofit(String title) {
        String searchStr = "easy-retrofit";
        if (title != null && !io.github.easyretrofit.core.util.StringUtils.contains(title, searchStr) && !io.github.easyretrofit.core.util.StringUtils.startsWithPrefix(title, searchStr)) {
            return searchStr + "-" + title;
        }
        return title;
    }
}
