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
    private final static String DOUBLE_COLON = " :: ";
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
            log.debug("==========================RETROFIT CLIENT INFO [{}] BEGIN==========================", index);
            final String retrofitInstanceName = retrofitClient.getRetrofitInstanceName();
            final String realHostUrl = retrofitClient.getRealHostUrl();
            if (retrofitClient.getUrlStatus().equals(UrlStatus.DYNAMIC_URL_ONLY)) {
                log.debug("*--RETROFIT CLIENT INFO [{}]: hostURL[Dummy]: {}, retrofitInstanceName: {}", index, realHostUrl, retrofitInstanceName);
            } else {
                log.debug("*--RETROFIT CLIENT INFO [{}]: hostURL: {}, retrofitInstanceName: {}", index, realHostUrl, retrofitInstanceName);
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
                log.debug("");
                log.debug("|--API INTERFACE INFO: name: {} , rootName: {}, self2RootPath: {}, childrenName: {}", selfClazz.getName(), parentClazzName, self2ParentClasses, childrenClasses);
                retrofitApiInterfaceDebugLog(retrofitApiInterface, retrofitClient);
            }
            log.debug("==========================RETROFIT CLIENT INFO [{}] END==========================", index);
            log.debug("");
            index++;
        }
    }


    private void SystemOutPrintln(String... messages) {

        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        if (messages == null || messages.length == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ANSI_GREEN);
        // 处理前面的所有参数（除了最后一个）
        for (int i = 0; i < messages.length - 1; i++) {
            sb.append(DOUBLE_COLON).append(messages[i]);
        }
        sb.append(DOUBLE_COLON);
        sb.append(ANSI_RESET);
        // 处理最后一个参数
        sb.append("(").append(messages[messages.length - 1]).append(")");

        System.out.println(sb);
    }

    private void getLogoInfo(RetrofitWebFramewrokInfoBean logBean) {
        String logo = "\n" +
                " ___  __    ____   __   ___ ___ _____ ___  __  ___ _ _____  \n" +
                "| __|/  \\ /' _| `v' /__| _ \\ __|_   _| _ \\/__\\| __| |_   _| \n" +
                "| _|| /\\ |`._`.`. .'|__| v / _|  | | | v / \\/ | _|| | | |   \n" +
                "|___|_||_||___/ !_!    |_|_\\___| |_| |_|_\\\\__/|_| |_| |_|   \n";
        System.out.println(logo);

        Package pkgInfo = this.getClass().getPackage();

        List<String> params = new ArrayList<>();

        //add easy-retrofit info
        params.add(appendEasyRetrofit(pkgInfo.getImplementationTitle()));
        params.add(pkgInfo.getImplementationVersion());
        SystemOutPrintln(appendEasyRetrofit(pkgInfo.getImplementationTitle()), pkgInfo.getImplementationVersion());

        if (logBean.getTitle() != null && logBean.getVersion() != null) {
            params.add(logBean.getTitle() == null ? "" : appendEasyRetrofit(logBean.getTitle()));
            params.add(logBean.getVersion() == null ? "" : logBean.getVersion());
            SystemOutPrintln(logBean.getTitle() == null ? "" : appendEasyRetrofit(logBean.getTitle()), logBean.getVersion() == null ? "" : logBean.getVersion());

            Class<?> builderExtensionClazz = context.getRetrofitBuilderExtensionClazz();
            if (builderExtensionClazz.getPackage().getImplementationTitle() != null && !params.stream().anyMatch(s -> s.equals(appendEasyRetrofit(logBean.getTitle())))) {
                params.add(appendEasyRetrofit(builderExtensionClazz.getPackage().getImplementationTitle()));
                params.add(builderExtensionClazz.getPackage().getImplementationVersion());
                SystemOutPrintln(appendEasyRetrofit(builderExtensionClazz.getPackage().getImplementationTitle()), builderExtensionClazz.getPackage().getImplementationVersion());
            }
            List<Class<?>> interceptorExtensionsClasses = context.getInterceptorExtensionsClasses();
            for (Class<?> interceptorExtensionsClass : interceptorExtensionsClasses) {
                if (interceptorExtensionsClass.getPackage().getImplementationTitle() == null) {
                    continue;
                }
                params.add(appendEasyRetrofit(interceptorExtensionsClass.getPackage().getImplementationTitle()));
                params.add(interceptorExtensionsClass.getPackage().getImplementationVersion());
                SystemOutPrintln(appendEasyRetrofit(interceptorExtensionsClass.getPackage().getImplementationTitle()), interceptorExtensionsClass.getPackage().getImplementationVersion());
            }
        }
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
        log.trace("|--BUILDER INFO: hostURL: {}; urlStatus: {}; globalEnable: {}; callAdapterFactory: {}; converterFactory:{}; callbackExecutor: {}; client: {}; callFactory: {}; validateEagerly: {}",
                realHostUrl, retrofitClient.getUrlStatus(), globalEnable, CallAdapterFactoryString, ConverterFactoryString, callbackExecutorString, clientString, callFactoryString, validateEagerlyString);
        for (RetrofitInterceptorBean interceptor : retrofitClient.getInterceptors()) {
            log.trace("|--INTERCEPTOR INFO: handler: {}", interceptor.getHandler());
            log.trace("   |--type: {}", interceptor.getType());
            log.trace("   |--defaultScopeClasses: {}", StringUtils.join(interceptor.getDefaultScopeClasses(), ","));
            log.trace("   |--include: {}", StringUtils.join(interceptor.getInclude(), ","));
            log.trace("   |--exclude: {}", StringUtils.join(interceptor.getExclude(), ","));
            log.trace("   |--sort: {}", interceptor.getSort());
        }

    }

    private void retrofitApiInterfaceDebugLog(RetrofitApiInterfaceBean retrofitApiInterface, RetrofitClientBean retrofitClient) {
        Map<Class<?>, Set<RetrofitInterceptorBean>> parentInterceptors = getParentInterceptors(retrofitApiInterface, retrofitClient);
        for (Map.Entry<Class<?>, Set<RetrofitInterceptorBean>> entry : parentInterceptors.entrySet()) {
            for (RetrofitInterceptorBean retrofitInterceptorBean : entry.getValue()) {
                log.trace("   |--INTERCEPTOR INFO: handler: {}", retrofitInterceptorBean.getHandler());
                log.trace("      |--belongsTo: {}", entry.getKey());
                log.trace("      |--type: {}", retrofitInterceptorBean.getType());
                log.trace("      |--defaultScopeClasses: {}", StringUtils.join(retrofitInterceptorBean.getDefaultScopeClasses(), ","));
                log.trace("      |--include: {}", StringUtils.join(retrofitInterceptorBean.getInclude(), ","));
                log.trace("      |--exclude: {}", StringUtils.join(retrofitInterceptorBean.getExclude(), ","));
                log.trace("      |--sort: {}", retrofitInterceptorBean.getSort());
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
        if (title != null) {
            return searchStr + " :: " + title;
        }
        return null;
    }
}
