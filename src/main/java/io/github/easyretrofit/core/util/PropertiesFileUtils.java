package io.github.easyretrofit.core.util;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropertiesFileUtils {
    public static Set<String> getPropertiesKeys(Reader reader, String extensionClazzName) {
        Set<String> extensionNames = new HashSet<>();
        Properties properties = new Properties();
        try {
            properties.load(reader);
            for (String key : properties.stringPropertyNames()) {
                String className = properties.getProperty(key);
                if (extensionClazzName.equalsIgnoreCase(key)) {
                    if (className.contains(",")) {
                        String[] classNames = className.split(",");
                        for (String classname : classNames) {
                            extensionNames.add(getExtensionPackageName(classname));
                        }
                    } else {
                        extensionNames.add(getExtensionPackageName(className));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extensionNames;
    }

    private static String getExtensionPackageName(String classname) {
        int lastDotIndex = classname.lastIndexOf('.');
        return classname.substring(0, lastDotIndex);
    }
}
