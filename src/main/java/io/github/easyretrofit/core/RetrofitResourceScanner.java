package io.github.easyretrofit.core;

import io.github.easyretrofit.core.annotation.RetrofitBase;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>Scan the resources required for easy-retrofit in the customized packages</p>
 * <p>When you create a server-side web framework extension based on easy-retrofit-core, you need to call this class to complete the scanning of resources</p>
 *
 * @author liuziyuan
 */

public class RetrofitResourceScanner {
    private static final Logger log = LoggerFactory.getLogger(RetrofitResourceScanner.class);

    /**
     * Scan retrofit resources when using @RetrofitBuilder or @RetrofitBase annotation <br>
     * @param basePackages packages that need to be scanned
     * @return easy-retrofit base resources
     */
    public Set<Class<?>> doScan(String... basePackages) {
        Reflections reflections = getReflections(basePackages);
        final Set<Class<?>> retrofitBuilderClasses = getRetrofitResourceClasses(reflections, RetrofitBuilder.class);
        final Set<Class<?>> retrofitBaseApiClasses = getRetrofitResourceClasses(reflections, RetrofitBase.class);
        retrofitBuilderClasses.addAll(retrofitBaseApiClasses);
        return retrofitBuilderClasses;
    }

    /**
     * Scan retrofit extension resources when implemented RetrofitBuilderExtension interface or RetrofitInterceptorExtension interface
     * @param basePackages Java packages that need to be scanned
     * @return easy-retrofit extension resources
     */
    public RetrofitExtension doScanExtension(String... basePackages) {
        Reflections reflections = getReflections(basePackages);
        RetrofitExtension retrofitExtension = new RetrofitExtension();
        Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses = reflections.getSubTypesOf(RetrofitBuilderExtension.class);
        Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses = reflections.getSubTypesOf(RetrofitInterceptorExtension.class);
        retrofitExtension.setRetrofitBuilderClasses(retrofitBuilderClasses);
        retrofitExtension.setRetrofitInterceptorClasses(retrofitInterceptorClasses);
        return retrofitExtension;
    }

    private Set<Class<?>> getRetrofitResourceClasses(Reflections reflections, Class<? extends Annotation> annotationClass) {
        final Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(annotationClass);
        Iterator<Class<?>> iterator = classSet.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            if (!clazz.isInterface()) {
                iterator.remove();
                log.warn("[{}] requires an interface type", clazz.getName());
            }
        }
        return classSet;
    }

    private Reflections getReflections(String[] basePackages) {
        ConfigurationBuilder configuration;
        if (basePackages.length == 0) {
            configuration = new ConfigurationBuilder().forPackages("");
        } else {
            Pattern filterPattern = Pattern.compile(Arrays.stream(basePackages)
                    .map(s -> s.replace(".", "/"))
                    .collect(Collectors.joining("|", ".*?(", ").*?")));
            log.debug("Scanner Pattern : {}", filterPattern.pattern());
            configuration = new ConfigurationBuilder().forPackages(basePackages).filterInputsBy(s ->
            {
                log.debug("Filter inputs {}", s);
                return filterPattern.matcher(s).matches();
            });

        }
        return new Reflections(configuration);
    }


    public static class RetrofitExtension {
        Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses;
        Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses;

        public RetrofitExtension() {
            retrofitInterceptorClasses = new HashSet<>();
            retrofitBuilderClasses = new HashSet<>();
        }


        public Set<Class<? extends RetrofitBuilderExtension>> getRetrofitBuilderClasses() {
            return retrofitBuilderClasses;
        }

        public void setRetrofitBuilderClasses(Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses) {
            this.retrofitBuilderClasses = retrofitBuilderClasses;
        }

        public Set<Class<? extends RetrofitInterceptorExtension>> getRetrofitInterceptorClasses() {
            return retrofitInterceptorClasses;
        }

        public void setRetrofitInterceptorClasses(Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses) {
            this.retrofitInterceptorClasses = retrofitInterceptorClasses;
        }
    }

}
