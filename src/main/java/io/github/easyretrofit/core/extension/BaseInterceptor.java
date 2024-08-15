package io.github.easyretrofit.core.extension;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.util.AntPathMatcher;
import io.github.easyretrofit.core.util.PathMatcher;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract class of Interceptor, The inner interceptor needs to inherit it
 *
 * @author liuziyuan
 */
public abstract class BaseInterceptor implements Interceptor {

    protected RetrofitResourceContext context;
    private Set<Class<?>> defaultScopeClasses;
    private String[] include;
    private String[] exclude;
    private final PathMatcher pathMatcher = new AntPathMatcher();


    protected BaseInterceptor() {
    }

    protected BaseInterceptor(RetrofitResourceContext context) {
        this.context = context;
    }

    @Override
    public final Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();
        if (isMatch(exclude, path)) {
            return chain.proceed(request);
        }
        if (include != null && !isMatch(include, path)) {
            return chain.proceed(request);
        }
        return executeIntercept(chain);
    }

    /**
     * execute intercept for OKHttpClient Interceptor
     *
     * @param chain Chain
     * @return Response
     */
    protected abstract Response executeIntercept(Chain chain) throws IOException;

    private boolean isMatch(String[] patterns, String path) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            boolean match = pathMatcher.match(pattern, path);
            if (match) {
                return true;
            }
        }
        return false;
    }


    public void setInclude(String[] include) {
        this.include = include;
    }

    public void setExclude(String[] exclude) {
        this.exclude = exclude;
    }


    protected String getClazzNameByMethod(Method method) {
        return method.getDeclaringClass().getName();
    }

    protected Method getRequestMethod(Request request) {
        return Objects.requireNonNull(request.tag(Invocation.class)).method();
    }

}
