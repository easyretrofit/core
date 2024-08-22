package io.github.easyretrofit.core;

public class RetrofitExtensionLogBean {

    private final String title;
    private final String version;

    public RetrofitExtensionLogBean(String title, String version)
    {
        this.title = title;
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }
}
