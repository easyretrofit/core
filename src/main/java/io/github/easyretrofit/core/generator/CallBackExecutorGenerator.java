package io.github.easyretrofit.core.generator;

import io.github.easyretrofit.core.Generator;
import io.github.easyretrofit.core.builder.BaseCallBackExecutorBuilder;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public abstract class CallBackExecutorGenerator implements Generator<Executor> {
    private final Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz;

    public CallBackExecutorGenerator(Class<? extends BaseCallBackExecutorBuilder> callBackExecutorBuilderClazz) {
        this.callBackExecutorBuilderClazz = callBackExecutorBuilderClazz;
    }

    public abstract BaseCallBackExecutorBuilder buildInjectionObject(Class<? extends BaseCallBackExecutorBuilder> clazz);

    @Override
    public Executor generate() {

        final BaseCallBackExecutorBuilder baseCallBackExecutorBuilder = buildInjectionObject(callBackExecutorBuilderClazz);
        if (baseCallBackExecutorBuilder != null) {
            return baseCallBackExecutorBuilder.executeBuild();
        }
        if (callBackExecutorBuilderClazz != null) {
            final String baseCallBackExecutorClazzName = BaseCallBackExecutorBuilder.class.getName();
            final String callBackExecutorClazzName = callBackExecutorBuilderClazz.getName();
            if (baseCallBackExecutorClazzName.equals(callBackExecutorClazzName)) {
                return null;
            } else {
                final BaseCallBackExecutorBuilder builder;
                try {
                    builder = callBackExecutorBuilderClazz.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                return builder.executeBuild();
            }
        }
        return null;
    }
}
