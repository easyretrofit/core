package io.github.easyretrofit.core.resource.ext;

import java.util.HashSet;
import java.util.Set;

public class ExtensionPropertiesBean {

    /**
     * scan resource base packages form extension properties file when extension create a retrofit builder
     */
    private Set<String> resourcePackages;

    /**
     * scan RetrofitBuilderExtension or RetrofitInterceptorExtension file path form extension properties file
     */
    private Set<String> extensionClassPaths;

    public ExtensionPropertiesBean() {
        resourcePackages = new HashSet<>();
        extensionClassPaths = new HashSet<>();
    }

    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public Set<String> getExtensionClassPaths() {
        return extensionClassPaths;
    }

    public void setExtensionClassPaths(Set<String> extensionClassPaths) {
        this.extensionClassPaths = extensionClassPaths;
    }
}
