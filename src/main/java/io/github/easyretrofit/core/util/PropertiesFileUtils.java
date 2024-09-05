package io.github.easyretrofit.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class PropertiesFileUtils {

    public static Set<String> getPropertiesKeys(Reader reader, String extensionClazzName) {
        Set<String> extensionNames = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String finalStr = sb.toString().replaceAll("\\\\", "").trim();
            String[] split = finalStr.split("=");
            if (extensionClazzName.equalsIgnoreCase(split[0].trim())) {
                String className = split[1].trim();
                if (className.contains(",")) {
                    String[] classNames = className.split(",");
                    for (String classname : classNames) {
                        extensionNames.add(getExtensionPackageName(classname));
                    }
                } else {
                    extensionNames.add(getExtensionPackageName(className));
                }
            }
            return extensionNames;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getExtensionPackageName(String classname) {
        int lastDotIndex = classname.lastIndexOf('.');
        return classname.substring(0, lastDotIndex);
    }
}
