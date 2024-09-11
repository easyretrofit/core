package io.github.easyretrofit.core.util;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;

public class StringUtils {
    public static String[] tokenizeToStringArray(
            @Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    public static boolean startsWithPrefix(String input, String prefix) {
        if (input == null || prefix == null) {
            return false;
        }
        int prefixLength = prefix.length();
        if (input.length() < prefixLength) {
            return false;
        }
        String lowerInput = input.substring(0, prefixLength).toLowerCase();
        String lowerPrefix = prefix.toLowerCase();
        return lowerInput.equals(lowerPrefix);
    }

    public static boolean contains(String input, String searchStr) {
        if (input == null || searchStr == null) {
            return false;
        }
        return input.toLowerCase().contains(searchStr.toLowerCase());
    }
}
