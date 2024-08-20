package io.github.easyretrofit.core.data.clients;

import io.github.easyretrofit.core.EnvManager;

public class LocalEnvManager implements EnvManager {
    @Override
    public String resolveRequiredPlaceholders(String text) {
        return text;
    }
}
